var LAZY_LIST = {
		productId: 0,
		product: 1,
		unitsRequired: 2,
		unitsPerAssembly: 3,
		productDescription: 4,
		createRow: function(data, count) {
			var row = $(
					"<tr>" +
					"<td>" + data[this.product] + "</td>" +
					"<td>" + data[this.productDescription] + "</td>" +
					"<td class=\"right\"><input type=\"text\" class=\"unitsRequired\" id=\"materialList[" + count + "].unitsRequired\" maxLength=\"17\" name=\"materialList[" + count + "].unitsRequired\" id=\"materialList[" + count + "].unitsRequired\" value='" + data[this.unitsPerAssembly] + "'/></td>" +
					"<td class=\"right amount\">" + data[this.unitsRequired] + "</td>" +
					"<td class=\"right\" id=\"cancelExisting\"><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"materialList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" class=\"totalUnits\" name=\"totalUnits\" id=\"totalUnits\" value='" + data[this.unitsRequired] + "'/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"materialList["+ count +"].component.id\" value='" + data[this.productId] + "'/>" +
			"</tr>")
			$(".unitsRequired", row).ForceNumericOnly(true)
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
		initSelectedJobOrderMaterials: function() {
	        $.each($("tbody tr", "#componentsTable"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    }
}

var computeTotalUnitsRequired = function(tr){
	$("td.amount", tr).calc("unitsRequired * totalUnits", {unitsRequired : $("td input.unitsRequired", tr).val(),
		totalUnits: $("input.totalUnits",tr).val()},
		function(){})
}

$(document).ready(function() {
	$("input[name^='materialList']").ForceNumericOnly(true)
	

	var searchJobOrderMaterialsTable = $("#searchJobOrderMaterialsTable").dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"bPaginate": false,
		"bScrollCollapse": true,
		"sScrollYInner": "150%",
		"sScrollY": "200px",
		"sAjaxSource": "retrieveJobOrderMaterials", 
		"aoColumns": columnsSettings,
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			aoData.push( { "name": "productId", "value": $("#productId").val() } )
			aoData.push( { "name": "jobOrderQty", "value": $("#jobOrderQty").val() } )
			$.ajax( {
				"dataType": 'json',
				"type": "POST",
				"url": sSource,
				"data": aoData,
				"success": fnCallback
			} );
		},
		"fnDrawCallback": function() {
			$("tbody tr", searchJobOrderMaterialsTable).click(function () {
				var data = searchJobOrderMaterialsTable.fnGetData(this)
				var productId = data[LAZY_LIST.productId]
				if(LAZY_LIST.isAddedButRemoved(productId, $('#componentsTable'))) {
				}
				else if(!LAZY_LIST.isAdded(productId, $('#componentsTable'))){
					var row = LAZY_LIST.createRow(data, $("#componentsTable tbody tr .remove").size())
					LAZY_LIST.appendRow(row, $('#componentsTable'))
					LAZY_LIST.addRemoveFunction(row)
					$.each($("tbody tr:not('.removed')", $('#componentsTable')), function(idx, tr) {
						$("td input:text",tr).keyup(function (){
							computeTotalUnitsRequired(tr)
						})
					})
				} else {
					alert("Product: "+ data[LAZY_LIST.product] + " already added!")
				}
			})
		}
	}).fnSetFilteringDelay()//end of data table
	
	LAZY_LIST.initSelectedJobOrderMaterials()
	
	$.each($("tbody tr", $("#componentsTable")), function(idx, tr) {
		$("td input:text",tr).keyup(function (){
			computeTotalUnitsRequired(tr)
		})
	})
})