var LAZY_LIST = {
		productId: 0,
		productIdentifier: 1,
		productPartNumber: 2,
		productCode: 3,
		productDescription: 4,
//		productCurrency: 5,
		productRunningCostBare: 5,
		productRunningCost: 6,
		supplierCurrency: 7,
		createRow: function(data, count) {
			currency = data[this.supplierCurrency];
			var row = $(
					"<tr>" +
					"<td class=\"right\" id=\"cancelExisting\"><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
					"<td>" + data[this.productIdentifier] + "</td>" +
					"<td>" + data[this.productPartNumber] + "</td>" +
					"<td>" + data[this.productCode] + "</td>" +
					"<td>" + data[this.productDescription] + "</td>" +
					//"<td>" + data[this.productCurrency] + " " + data[this.productRunningCost] + "</td>" +
					"<td>" + data[this.productRunningCost] + "</td>" +
					"<td class=\"right\">"+data[this.supplierCurrency]+"&nbsp;<input type=\"text\" class=\"finalPrice\" maxlength=\"7\" id=\"itemList[" + count + "].finalPrice\" maxLength=\"17\" name=\"itemList[" + count + "].finalPrice\" id=\"itemList[" + count + "].finalPrice\" value=\"" + data[this.productRunningCostBare]+ "\"></td>" +
					"<td class=\"right\"><input type=\"text\" class=\"qty\" maxlength=\"7\" id=\"itemList[" + count + "].qty\" maxLength=\"17\" name=\"itemList[" + count + "].qty\" id=\"itemList[" + count + "].qty\" value='0'/></td>" +
					"<td class=\"right amount\">0</td>" +
//					"<input type=\"hidden\" class=\"currency\" name=\"itemList["+ count +"].currency\" value='" + data[this.productCurrency] + "'/>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"itemList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"itemList["+ count +"].product.id\" value='" + data[this.productId] + "'/>" +
			"</tr>")
			$(".finalPrice", row).ForceNumericOnly(true)
			$(".qty", row).ForceNumericOnly(true)
			return row
		},

		isAdded: function(value, table) {
			var added = false
			$.each($("tbody tr:not('.removed') .productId", table), function() {
				if($(this).val() == value){
					added = true
				}
			})
			return added
		},
		isAddedButRemoved: function(value, table) {
			var added = false
			$.each($("tbody tr:'.removed'", table), function() {
				if($(".productId", this).val() == value){
					added = true
					LAZY_LIST.cancelRemoveFunction($(this))
				}
			})
			return added
		},
		appendRow: function(row, table) {
			$("tbody", table).append(row)
		},
		addRemoveFunction: function(row) {
			$("img.remove", row).click(function () {
				$("input:hidden.deleted", row).val(true)
				$(row).addClass("removed")
				$(row).hide()
				computeAmountTotal()
				computeDiscountedTotal()
			})
		},
		cancelRemoveFunction: function(row) {
			$("input:hidden.deleted", row).val(false)
			$(row).removeClass("removed")
			$(row).show()
		},
		initSelectedPurchaseOrderItems: function() {
	        $.each($("tbody tr", "#componentsTable"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    }
}

var clearTable = function(){
	$.each($("tbody tr", "#componentsTable"), function(idx, tr) {
            $("input:hidden.deleted", this).val(true)
            $(tr).addClass("removed")
            $(tr).hide()
    })
}

var computeAmount = function(tr){
	$("td.amount", tr).calc("finalPrice * qty", {finalPrice : $("td input.finalPrice", tr).val(),
		qty: $("input.qty",tr).val()},
		function(){})
//	$("td.amount", tr).html($("input.currency", tr).val()+" "+$("td.amount", tr).html());
}

var validateFinalPrice = function(tr){
	if($("td input.finalPrice", tr).val() == ""){
		alert("final price value cannot be blank.")
		$("td input.finalPrice", tr).val("0")
	}
}

var validateQuantity = function(tr){
	if($("td input.qty", tr).val() == ""){
		alert("quantity cannot be blank.")
		$("td input.qty", tr).val("0")
	}
}

var setCustomerValue = function(setValue, toBeSet){
    $(toBeSet).val($(setValue).val())
}

function computeAmountTotal(){
//	return; // removed total
	var total = $("tbody tr:not('.removed') .amount", $("#componentsTable")).sum()
	$("#amountTotalText").text(currency + " " +addCommas(total))
}


function computeDiscountedTotal() {
//	return; // removed total
	$("#grandTotal").calc("undiscountedTotal - (undiscountedTotal * rate / 100)",
	{
		undiscountedTotal : $("tbody tr:not('.removed') .amount", $("#componentsTable")).sum(),
		rate : $("#discountRate").val()
	},
	function(s){
		return currency + " " + addCommas(s)
	}
	)
}

$(document).ready(function() {
	$("#discount").text($("#discountRate").val() + "%")
	$("#discountRate").ForceNumericOnly(true)
	$("input[name^='itemList']").ForceNumericOnly(true)
    computeAmountTotal()
    computeDiscountedTotal()
	
    $("#cancelExisting").click(function() {
    	computeAmountTotal()
    	computeDiscountedTotal()
    })
    
	$(".supSelect").change(function () {
        setCustomerValue($(".supSelect"),$(".supSelectName"))
    })
    
    $(".supSelectName").change(function () {
        $(".supSelect").val($(".supSelectName").val())
        setCustomerValue($(".supSelectName"),$(".supSelect"))
    })
      
	$("#discountRate").change(function () {
        $("#discount").text($("#discountRate").val() + "%")
        computeAmountTotal()
        computeDiscountedTotal()
    })
	
	$("#supplierId").change(function (){
		clearTable()
		searchPurchaseOrderItemsTable.fnDraw()
	})

	$("#supplierName").change(function (){
		clearTable()
		searchPurchaseOrderItemsTable.fnDraw()
	})
	

	var searchPurchaseOrderItemsTable = $("#searchPurchaseOrderItemsTable").dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"bPaginate": false,
		"bScrollCollapse": true,
		"sScrollYInner": "150%",
		"sScrollY": "180px",
		"sAjaxSource": "retrievePurchaseOrderItems",
		"aoColumns": columnsSettings,
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			aoData.push( { "name": "supplierId", "value": $("#supplierId").val() } )
			
			$.ajax( {
				"dataType": 'json',
				"type": "POST",
				"url": sSource,
				"data": aoData,
				"success": fnCallback
			} );
		},
		"fnDrawCallback": function() {
			$("tbody tr", searchPurchaseOrderItemsTable).click(function () {
				var data = searchPurchaseOrderItemsTable.fnGetData(this)
				var productId = data[LAZY_LIST.productId]
				if(LAZY_LIST.isAddedButRemoved(productId, $('#componentsTable'))) {
				}
				else if(!LAZY_LIST.isAdded(productId, $('#componentsTable'))){
					var row = LAZY_LIST.createRow(data, $("#componentsTable tbody tr .remove").size())
					LAZY_LIST.appendRow(row, $('#componentsTable'))
					LAZY_LIST.addRemoveFunction(row)
					$.each($("tbody tr:not('.removed')", $('#componentsTable')), function(idx, tr) {
						$("td input:text",tr).keyup(function (){
							validateFinalPrice(tr)
							validateQuantity(tr)
							computeAmount(tr)
							computeAmountTotal()
							computeDiscountedTotal()
						})
					})
				} else {
					alert("Product: "+ data[LAZY_LIST.productIdentifier] + " already added!")
				}
			})
		}
	}).fnSetFilteringDelay()//end of data table
	LAZY_LIST.initSelectedPurchaseOrderItems()
})