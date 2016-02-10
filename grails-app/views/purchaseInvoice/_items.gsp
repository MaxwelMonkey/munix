<link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
<g:javascript src="table/jquery.dataTables.js" />
<g:javascript src="table/jquery.dataTables.forceReload.js" />
<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
<g:javascript src="numbervalidation.js" />
<g:javascript src="purchaseInvoiceTemplate.js" />

<div class="list">
	<table id="searchPurchaseOrdersTable">
		<thead>
			<th width="350px">Purchase Order</th>
			<th width="350px">Product</th>
			<th width="100px">Quantity</th>
			<th width="116px">Remaining</th>
			<th>Price>
			<th>Final Price</th>
			<th>Received Quantity</th>
			<th>Product ID</th>
			<th>Purchase Order Item ID</th>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<div class="list">
	<table id="invoiceItemsTable">
		<h2>Invoice Items</h2>
		<thead>
			<tr>
				<th>Product</th>
				<th>Quantity</th>
				<th>PO Price</th>
				<th>PO Final Price</th>
				<th>Received</th>
				<th>Final Price</th>
				<th>Remove</th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${purchaseInvoiceInstance.items}" status="i" var="item">
				<tr <g:if test="${item.isDeleted}"> class="removed" style="display:none"</g:if>>
					<td class="productDesc">${item?.purchaseOrderItem?.product?.identifier}-${item?.purchaseOrderItem?.product?.description}</td>
					<td>${item?.purchaseOrderItem?.qty}</td>
					<td>${item?.purchaseOrderItem?.price}</td>
					<td>${item?.purchaseOrderItem?.finalPrice}</td>
					<td><g:textField class="received" maxLength="17" name="invoiceItemList[${i}].qty" value="${item?.qty}" /></td>
					<td><g:textField class="received" maxLength="17" name="invoiceItemList[${i}].finalPrice" value="${item?.finalPrice}" /></td>
					<td id="cancelExisting">
						<img src="../images/cancel.png" class="remove">
						<g:hiddenField class="deleted" name="invoiceItemList[${i}].isDeleted" value="${item.isDeleted}" />
					</td>
					<g:hiddenField name="invoiceItemList[${i}].purchaseOrderItem.id" value="${item?.purchaseOrderItem?.id}" />
					<g:hiddenField class="productId" name="productId" value="${item?.purchaseOrderItem?.product?.id}-${item?.purchaseOrderItem?.id}"/>
				</tr>
			</g:each>
		</tbody>
	</table>
</div>
