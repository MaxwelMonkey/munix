var LAZY_LIST = {
		productId: 0,
		productIdentifier: 1,
		productDescription: 2,
		oldStock: 3,
		createRow: function(data, count) {
			var row = $(
					"<tr>" +
					"<td>" + data[this.productIdentifier] + "</td>" +
					"<td>" + data[this.productDescription] + "</td>" +
					"<td>" + data[this.oldStock] + "</td>" +
					"<td class=\"right\"><input type=\"text\" class=\"newStock\" maxlength=\"7\" id=\"itemList[" + count + "].newStock\" maxLength=\"17\" name=\"itemList[" + count + "].newStock\" id=\"itemList[" + count + "].newStock\" value='0'/></td>" +
					"<td class=\"right difference\">" + data[this.oldStock] + "</td>" +
					"<td class=\"right\" id=\"cancelExisting\"><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"itemList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" class=\"oldStock\" name=\"itemList["+ count +"].oldStock\" value='" + data[this.oldStock] + "'/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"itemList["+ count +"].product.id\" value='" + data[this.productId] + "'/>" +
			"</tr>")
			$(".newStock", row).ForceNumericOnly(true)
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
		initSelectedInventoryAdjustmentItems: function() {
	        $.each($("tbody tr", "#componentsTable"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    }
}

var computeStockDifference = function(tr){
	$("td.difference", tr).calc("newStock - oldStock", {newStock : $("td input.newStock", tr).val(),
		oldStock: $("input.oldStock",tr).val()},
		function(){})
}

var validateStock = function(tr){
	if($("td input.newStock", tr).val() == ""){
		alert("New stock value cannot be blank.")
		$("td input.newStock", tr).val("0")
	}
}

var clearTable = function(){
	$("tbody tr", "#componentsTable").empty()
}

$(document).ready(function() {
	$("input[name^='itemList']").ForceNumericOnly(true)
	
	$.each($("tbody tr", $("#componentsTable")), function(idx, tr) {
		$("td input:text",tr).keyup(function (){
			validateStock(tr)
			computeStockDifference(tr)
		})
	})

	$("#itemTypeId").change(function (){
		clearTable()
		searchInventoryAdjustmentItemsTable.fnDraw()
	})

	$("#warehouseId").change(function (){
		clearTable()
		searchInventoryAdjustmentItemsTable.fnDraw()
	})
	
	$("#inventoryStatus").change(function (){
		clearTable()
		searchInventoryAdjustmentItemsTable.fnDraw()
	})
	
    var columnsSettings = new Array()
    columnsSettings.push({"bVisible": false}, null, null, null)

	var searchInventoryAdjustmentItemsTable = $("#searchInventoryAdjustmentItemsTable").dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"bPaginate": false,
		"bScrollCollapse": true,
		"sScrollYInner": "150%",
		"sScrollY": "180px",
		"sAjaxSource": "retrieveInventoryAdjustmentItems",
		"aoColumns": columnsSettings,
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			aoData.push( { "name": "itemTypeId", "value": $("#itemTypeId").val() } )
			aoData.push( { "name": "warehouseId", "value": $("#warehouseId").val() } )
			aoData.push( { "name": "inventoryStatus", "value": $("#inventoryStatus").val() } )
			$.ajax( {
				"dataType": 'json',
				"type": "POST",
				"url": sSource,
				"data": aoData,
				"success": fnCallback
			} );
		},
		"fnDrawCallback": function() {
			$("tbody tr", searchInventoryAdjustmentItemsTable).click(function () {
				var data = searchInventoryAdjustmentItemsTable.fnGetData(this)
				var productId = data[LAZY_LIST.productId]
				if(LAZY_LIST.isAddedButRemoved(productId, $('#componentsTable'))) {
				}
				else if(!LAZY_LIST.isAdded(productId, $('#componentsTable'))){
					var row = LAZY_LIST.createRow(data, $("#componentsTable tbody tr .remove").size())
					LAZY_LIST.appendRow(row, $('#componentsTable'))
					LAZY_LIST.addRemoveFunction(row)
					$.each($("tbody tr:not('.removed')", $('#componentsTable')), function(idx, tr) {
						$("td input:text",tr).keyup(function (){
							validateStock(tr)
							computeStockDifference(tr)
						})
					})
				} else {
					alert("Product: "+ data[LAZY_LIST.productIdentifier] + " already added!")
				}
			})
		}
	}).fnSetFilteringDelay()//end of data table
	LAZY_LIST.initSelectedInventoryAdjustmentItems()
	
})