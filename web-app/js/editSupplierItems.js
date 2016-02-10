var LAZY_LIST = {
		itemId: 0,
		itemIdentifier: 1,
		itemDescription: 2,
		status: 4,
		createRow: function(data, count) {

            var row = $(
					"<tr>" +
					"<td>" + data[this.itemIdentifier] + "</td>" +
					"<td><input type=\"text\" id=\"supplierItemList[" + count + "].productCode\" maxLength=\"17\" name=\"supplierItemList[" + count + "].productCode\" id=\"supplierItemList[" + count + "].productCode\" value=\"\"></td>" +
					"<td>" + data[this.itemDescription] + "</td>" +
                    "<td class=\"status\"></td>" +
					"<td class=\"center\" id=\"cancelExisting\"><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"supplierItemList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"supplierItemList["+ count +"].product.id\" value='" + data[this.itemId] + "'/>" +
			"</tr>")
            $(".status",row).append(createSelectStatus(count))
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
		initSelectedProductComponents: function() {
	        $.each($("tbody tr", "#supplierItemTable"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    }
}
function createSelectStatus(count){
    var select=$("<select name=\"supplierItemList[" + count + "].status\" id=\"supplierItemList[" + count + "].status\"></select>")
    $(".itemStatus").each(function(){
        select.append('<option value="'+$(this).val().toUpperCase()+'">'+$(this).val()+'</option>')
    })
    return select
}
$(document).ready(function() {
	$("input[name^='componentList']").ForceNumericOnly(true)
    var columnsSettings = new Array()
    columnsSettings.push({"bVisible": false}, {"bSortable":true}, {"bSortable":true})

	var productTable = $("#productTable").dataTable({
        "bServerSide": true,
        "bPaginate": false,
        "bScrollCollapse": true,
        "sScrollYInner": "150%",
        "sScrollY": "200px",
		"sAjaxSource": "generateAvailableProductsForSupplierItem",
		"aoColumns": columnsSettings,
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			aoData.push( { "name": "productId", "value": $("#productId").val() } )
			$.ajax( {
				"dataType": 'json',
				"type": "POST",
				"url": sSource,
				"data": aoData,
				"success": fnCallback
			} );
		},
		"fnDrawCallback": function() {
			$("tbody tr", productTable).click(function () {
				var data = productTable.fnGetData(this)
				var componentId = data[LAZY_LIST.itemId]
				if(LAZY_LIST.isAddedButRemoved(componentId, $('#supplierItemTable'))) {
				}
				else if(!LAZY_LIST.isAdded(componentId, $('#supplierItemTable'))){
					var row = LAZY_LIST.createRow(data, $("#supplierItemTable tbody tr .remove").size())
					LAZY_LIST.appendRow(row, $('#supplierItemTable'))
					LAZY_LIST.addRemoveFunction(row)
				} else {
					alert("Product: "+ data[LAZY_LIST.itemDescription] + " already added!")
				}
			})
		}
	}).fnSetFilteringDelay()//end of data table
	
	LAZY_LIST.initSelectedProductComponents()

	
})