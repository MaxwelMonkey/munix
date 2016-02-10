var LAZY_LIST = {
		product: 1,
		qty: 2,
		poPrice: 4,
		poFinalPrice: 5,
		received: 6,
		productId: 7,
		purchaseOrderItemId: 8,
		createRow: function(data, count) {
			var row = $(
					"<tr>" +
					"<td class=\"productDesc\">" + data[this.product] + "</td>" +
					"<td>" + data[this.qty] + "</td>" +
					"<td>" + data[this.poPrice] + "</td>" +
					"<td>" + data[this.poFinalPrice] + "</td>" +
					"<td><input type=\"text\" class=\"received\" maxLength=\"17\" name=\"invoiceItemList[" + count + "].qty\" id=\"invoiceItemList[" + count + "].qty\" value='0'/></td>" +
					"<td><input type=\"text\" class=\"finalPrice\" maxLength=\"17\" name=\"invoiceItemList[" + count + "].finalPrice\" id=\"invoiceItemList[" + count + "].finalPrice\" value='" + data[this.poFinalPrice] + "'/></td>" +
					"<td><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"invoiceItemList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" name=\"invoiceItemList["+ count +"].purchaseOrderItem.id\" value='" + data[this.purchaseOrderItemId] + "'/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"productId\" value='" + data[this.productId] + "-" + data[this.purchaseOrderItemId] + "'/>" +
			"</tr>")
			$(".received", row).ForceNumericOnly(true)
			$(".finalPrice", row).ForceNumericOnly(true)
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
			})
		},
		cancelRemoveFunction: function(row) {
			$("input:hidden.deleted", row).val(false)
			$(row).removeClass("removed")
			$(row).show()
		},
		initSelectedPurchaseOrderItems: function() {
	    	$(".received").ForceNumericOnly(true)
	    	$(".finalPrice").ForceNumericOnly(true)
	        $.each($("tbody tr", "#invoiceItemsTable"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    }
}
$(document).ready(function() {
    var columnsSettings = new Array()
    columnsSettings.push(null, null, null, null, {"bVisible": false}, {"bVisible": false}, {"bVisible": false}, {"bVisible": false}, {"bVisible": false})

	var searchPurchaseOrdersTable = $("#searchPurchaseOrdersTable").dataTable({
		"bServerSide": true,
		"bPaginate": false,
		"bScrollCollapse": true,
		"sScrollYInner": "150%",
		"sScrollY": "200px",
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
			$("tbody tr", searchPurchaseOrdersTable).click(function () {
				var data = searchPurchaseOrdersTable.fnGetData(this)
				var productId = data[LAZY_LIST.productId] + "-" + data[LAZY_LIST.purchaseOrderItemId]
				if(LAZY_LIST.isAddedButRemoved(productId, $('#invoiceItemsTable'))) {
				}
				else if(!LAZY_LIST.isAdded(productId, $('#invoiceItemsTable'))){
					var row = LAZY_LIST.createRow(data, $("#invoiceItemsTable tbody tr .remove").size())
					LAZY_LIST.appendRow(row, $('#invoiceItemsTable'))
					LAZY_LIST.addRemoveFunction(row)
				} else {
					alert("Item: "+ data[LAZY_LIST.product] + " already added!")
				}
			})
		}
	}).fnSetFilteringDelay()//end of data table
	
	LAZY_LIST.initSelectedPurchaseOrderItems()
})