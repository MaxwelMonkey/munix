var LAZY_LIST = {
		productId: 0,
		productIdentifier: 1,
		productDescription: 2,
		originWarehouseQty: 3,
		destinationWarehouseQty: 4,
		createRow: function(data, count) {
			var row = $(
					"<tr>" +
					"<td>" + data[this.productIdentifier] + "</td>" +
					"<td>" + data[this.productDescription] + "</td>" +
					"<td>" + data[this.originWarehouseQty] + "</td>" +
					"<td>" + data[this.destinationWarehouseQty] + "</td>" +
					"<td class=\"right\"><input type=\"text\" class=\"qty\" maxlength=\"7\" id=\"itemList[" + count + "].qty\" maxLength=\"17\" name=\"itemList[" + count + "].qty\" id=\"itemList[" + count + "].qty\" value=\"1\"/></td>" +
					"<td class=\"right\" id=\"cancelExisting\"><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"itemList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" class=\"originWarehouseStock\" name=\"originWarehouseStock\" value='" + data[this.originWarehouseQty] + "'/>" +
					"<input type=\"hidden\" class=\"destinationWarehouseStock\" name=\"destinationWarehouseStock\" value='" + data[this.destinationWarehouseQty] + "'/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"itemList["+ count +"].product.id\" value='" + data[this.productId] + "'/>" +
			"</tr>")
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
			})
		},
		cancelRemoveFunction: function(row) {
			$("input:hidden.deleted", row).val(false)
			$(row).removeClass("removed")
			$(row).show()
		},
		initSelectedInventoryTransferItems: function() {
	        $.each($("tbody tr", "#componentsTable"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    }
}

var validateStock = function(tr){
	if($("td input.qty", tr).val() == ""){
		alert("Quantity value cannot be blank.")
		$("td input.qty", tr).val("1")
	}
}

var clearTable = function(){
	$.each($("tbody tr", "#componentsTable"), function(idx, tr) {
            $("input:hidden.deleted", this).val(true)
            $(tr).addClass("removed")
            $(tr).hide()
    })
}

var createMode = true;


$(document).ready(function() {
	$("input[name^='itemList']").ForceNumericOnly(true)

	$.each($("tbody tr", $("#componentsTable")), function(idx, tr) {
		$("td input:text",tr).keyup(function (){
			validateStock(tr)
		})
	})

	$("#originWarehouseId").change(function (){
        $.ajax({ url: 'updateDestinationWarehouseSelectType',
            data: "selectedValue="+$('#originWarehouseId').val(),
            success: function(resp){
                    $('#destinationWarehouseId')
					.find('option')
                	.remove()
                	.end()
                $.each(resp.warehouses, function(key, value)
                {
                    $('#destinationWarehouseId')
                        .append('<option value="'+value+'">'+value+'</option>')
                });
            }
        });
		clearTable()
		searchInventoryTransferItemsTable.fnDraw()
	})
	
	if(createMode){
		$("#destinationWarehouseId").change(function (){
			clearTable()
			searchInventoryTransferItemsTable.fnDraw()
		})
	}
	
    var columnsSettings = new Array()
    columnsSettings.push({"bVisible": false}, null, null, null, {"bVisible": false})

	var searchInventoryTransferItemsTable = $("#searchInventoryTransferItemsTable").dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"bPaginate": false,
		"bScrollCollapse": true,
		"sScrollYInner": "150%",
		"sScrollY": "180px",
		"sAjaxSource": "retrieveInventoryTransferItems",
		"aoColumns": columnsSettings,
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			aoData.push( { "name": "originWarehouseId", "value": $("#originWarehouseId").val() } )
			aoData.push( { "name": "destination", "value": $("#destinationWarehouseId").val() } )

			$.ajax( {
				"dataType": 'json',
				"type": "POST",
				"url": sSource,
				"data": aoData,
				"success": fnCallback
			} );
		},
		"fnDrawCallback": function() {
			$("tbody tr", searchInventoryTransferItemsTable).click(function () {
				var data = searchInventoryTransferItemsTable.fnGetData(this)
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
						})
					})
				} else {
					alert("Product: "+ data[LAZY_LIST.productIdentifier] + " already added!")
				}
			})
		}
	}).fnSetFilteringDelay()//end of data table
	LAZY_LIST.initSelectedInventoryTransferItems()
	
	if(createMode){
	    $.ajax({ url: 'updateDestinationWarehouseSelectType',
	        data: "selectedValue="+$('#originWarehouseId').val(),
	        success: function(resp){
	                $('#destinationWarehouseId')
					.find('option')
	            	.remove()
	            	.end()
	            $.each(resp.warehouses, function(key, value)
	            {
	                $('#destinationWarehouseId')
	                    .append('<option value="'+value+'">'+value+'</option>')
	                   $('#destinationWarehouseId').val('+value+')
	            });
	        }
	    });
    }
})