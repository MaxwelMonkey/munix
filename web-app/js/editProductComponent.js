var LAZY_LIST = {
		componentId: 0,
		component: 1,
		componentDescription: 2,
		qty: 3, 
		componentUnit: 4,
		productId: 5,
		createRow: function(data, count) {
			var row = $(
					"<tr>" +
					"<td>" + data[this.component] + "</td>" +
					"<td>" + data[this.componentDescription] + "</td>" +
					"<td class=\"right\"><input type=\"text\" class=\"qty\" id=\"componentList[" + count + "].qty\" maxLength=\"17\" name=\"componentList[" + count + "].qty\" id=\"componentList[" + count + "].qty\" value=\"1\"></td>" +
					"<td>" + data[this.componentUnit] + "</td>" +
					"<td class=\"center\" id=\"cancelExisting\"><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"componentList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" class=\"componentId\" name=\"componentList["+ count +"].component.id\" value='" + data[this.componentId] + "'/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"componentList["+ count +"].product.id\" value='" + data[this.productId] + "'/>" +
			"</tr>")
			$(".unitsRequired", row).ForceNumericOnly(true)
			return row
		},
		
		isAdded: function(value, table) {
			var added = false
			$.each($("tbody tr:not('.removed') .componentId", table), function() {
				if($(this).val() == value){
					added = true
				}
			})
			return added
		},
		isAddedButRemoved: function(value, table) {
			var added = false
			$.each($("tbody tr:'.removed'", table), function() {
				if($(".componentId", this).val() == value){
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
	        $.each($("tbody tr", "#componentsTable"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    }
}

$(document).ready(function() {
	$("input[name^='componentList']").ForceNumericOnly(true)
	
    var columnsSettings = new Array()
    columnsSettings.push({"bVisible": false}, null, null, {"bVisible": false}, {"bVisible": false}, {"bVisible": false})

	var searchProductComponentTable = $("#searchProductComponentTable").dataTable({
		"bServerSide": true,
		"bPaginate": false,
		"bScrollCollapse": true,
		"sScrollYInner": "150%",
		"sScrollY": "200px",
		"sAjaxSource": "generateProductComponentTable", 
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
			$("tbody tr", searchProductComponentTable).click(function () {
				var data = searchProductComponentTable.fnGetData(this)
				var componentId = data[LAZY_LIST.componentId]
				if(LAZY_LIST.isAddedButRemoved(componentId, $('#componentsTable'))) {
				}
				else if(!LAZY_LIST.isAdded(componentId, $('#componentsTable'))){
					var row = LAZY_LIST.createRow(data, $("#componentsTable tbody tr .remove").size())
					LAZY_LIST.appendRow(row, $('#componentsTable'))
					LAZY_LIST.addRemoveFunction(row)
				} else {
					alert("Product: "+ data[LAZY_LIST.componentDescription] + " already added!")
				}				
			})
		}
	}).fnSetFilteringDelay()//end of data table
	
	LAZY_LIST.initSelectedProductComponents()

	
})