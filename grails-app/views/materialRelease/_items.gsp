<link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
<g:javascript src="table/jquery.dataTables.js" />
<g:javascript src="table/jquery.dataTables.forceReload.js" />
<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
<g:javascript src="numbervalidation.js" />
<script type="text/javascript">
var LAZY_LIST = {
		identifier: 0,
		description: 1,
		unitsRequired: 2,
		productId: 5,
		materialRequisitionItemId: 6,
		product: 7,
		createRow: function(data, count) {
			var row = $(
					"<tr>" +
					"<td>" + data[this.identifier] + "</td>" +
					"<td>" + data[this.description] + "</td>" +
					"<td>" + data[this.unitsRequired] + "</td>" +
					"<td><input type=\"text\" class=\"release\" maxLength=\"17\" name=\"releaseItemList[" + count + "].qty\" id=\"releaseItemList[" + count + "].qty\" value='0'/></td>" +
					"<td><img src=\"../../images/cancel.png\" class=\"remove\"/></td>" +
					"<input type=\"hidden\" class=\"deleted\" name=\"releaseItemList["+ count +"].isDeleted\" value=\"false\"/>" +
					"<input type=\"hidden\" name=\"releaseItemList["+ count +"].materialRequisitionItem.id\" value='" + data[this.materialRequisitionItemId] + "'/>" +
					"<input type=\"hidden\" class=\"productId\" name=\"productId\" value='" + data[this.productId] + "'/>" +
			"</tr>")
			$(".release", row).ForceNumericOnly(true)
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
		initSelectedMaterialReleaseItems: function() {
	    	$(".received").ForceNumericOnly(true)
	    	$(".finalPrice").ForceNumericOnly(true)
	        $.each($("tbody tr", "#releaseItemsTable"), function(idx, tr) {
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
    columnsSettings.push(null, null, null, null, null, {"bVisible": false}, {"bVisible": false}, {"bVisible": false})

    var requisitionItemsTable = $("#requisitionItemsTable").dataTable({
		"bPaginate": false,
		"bScrollCollapse": true,
		"sScrollYInner": "150%",
		"sScrollY": "200px",
		"sAjaxSource": "../retrieveMaterialRequisitionItems", 
		"aoColumns": columnsSettings,
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			aoData.push( { "name": "jobOrderId", "value": $("#jobOrderId").val() } )
			$.ajax( {
				"dataType": 'json',
				"type": "POST",
				"url": sSource,
				"data": aoData,
				"success": fnCallback
			} );
		},
		"fnDrawCallback": function() {
			$("tbody tr", requisitionItemsTable).click(function () {
				var data = requisitionItemsTable.fnGetData(this)
				var productId = data[LAZY_LIST.productId]
			
				if(LAZY_LIST.isAddedButRemoved(productId, $('#releaseItemsTable'))) {
				}
				else if(!LAZY_LIST.isAdded(productId, $('#releaseItemsTable'))){
					var row = LAZY_LIST.createRow(data, $("#releaseItemsTable tbody tr .remove").size())
					LAZY_LIST.appendRow(row, $('#releaseItemsTable'))
					LAZY_LIST.addRemoveFunction(row)
				} else {
					alert("Item: "+ data[LAZY_LIST.product] + " already added!")
				}
			})				
		}
	}).fnSetFilteringDelay() //end of data table 

	LAZY_LIST.initSelectedMaterialReleaseItems()
})

</script>

    <div class="list">
      <table id="requisitionItemsTable">
      	<h2>Available Requisition Items</h2>
        <thead>
            <th class="center" width="100px">Identifier</th>
            <th class="center" width="350px">Description</th>
            <th class="center" width="100px">Units Required</th>
            <th class="center" width="100px">Quantity</th>
            <th class="center" width="100px">Balance</th>
            <th>Product ID</th>
            <th>Material Requisition Item ID</th>
            <th>Product</th>
        </thead>
        <tbody>
        </tbody>
      </table>
    </div>
    <div class="list">
	<table id="releaseItemsTable">
		<h2>Release Items</h2>
		<thead>
			<tr>
				<th>Identifier</th>
				<th>Description</th>
				<th>Units Required</th>
				<th>Quantity</th>
				<th>Remove</th>				
			</tr>
		</thead>
		<tbody>
			<g:each in="${materialReleaseInstance.items}" status="i" var="item">
				<tr <g:if test="${item.isDeleted}"> class="removed" style="display:none"</g:if>>
					<td>${item?.materialRequisitionItem?.component?.identifier}</td>
					<td>${item?.materialRequisitionItem?.component?.description}</td>
					<td>${item?.materialRequisitionItem?.unitsRequired}</td>
					<td><g:textField class="released" maxLength="17" name="releaseItemList[${i}].qty" value="${item?.qty}" /></td>
					<td id="cancelExisting">
						<img src="../../images/cancel.png" class="remove">
						<g:hiddenField class="deleted" name="releaseItemList[${i}].isDeleted" value="${item?.isDeleted}" />
					</td>
					<g:hiddenField name="releaseItemList[${i}].materialRequisitionItem.id" value="${item?.materialRequisitionItem?.id}" />
					<g:hiddenField class="productId" name="productId" value="${item?.materialRequisitionItem?.component?.id}"/>
				</tr>
			</g:each>
		</tbody>
	</table>
</div>

