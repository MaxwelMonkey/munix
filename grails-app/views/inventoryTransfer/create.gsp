
<%@ page import="com.munix.InventoryTransfer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'inventoryTransfer.label', default: 'Inventory Transfer')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
    <link rel="stylesheet" href="${resource(dir:'js/table/css',file:'demo_table.css')}" />
    <g:javascript src="generalmethods.js" />
    <g:javascript src="numbervalidation.js" />
    <g:javascript src="jquery.calculation.js" />
    <g:javascript src="table/jquery.dataTables.js" />
	<g:javascript src="table/jquery.dataTables.forceReload.js" />
	<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
  	<g:javascript src="inventoryTransferTemplate.js" />
</head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="inventoryTransfer.list" default="Inventory Transfer List" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="inventoryTransfer.create" default="Create Inventory Transfer" /></h1>
            <g:if test="${flash.message}">
      			<div class="message">${flash.message}</div>
   			</g:if>
            <g:hasErrors bean="${inventoryTransferInstance}">
            <div class="errors">
                <g:renderErrors bean="${inventoryTransferInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                        	<tr class="prop">
            					<td valign="top" class="name">
              						<label for="originWarehouse"><g:message code="inventoryTransfer.originWarehouse.label" default="Origin Warehouse" /></label>
            					</td>
            					<td valign="top" class="value ${hasErrors(bean: inventoryTransferInstance, field: 'originWarehouse', 'errors')}">
          							<g:select name="originWarehouse.id" id="originWarehouseId" from="${com.munix.Warehouse.list().sort{it.toString()}}" optionKey="id" value="${inventoryTransferInstance?.originWarehouse?.id}" noSelection="['null':'Please select one...']" />
          						</td>
          					</tr>

                        	<tr class="prop">
            					<td valign="top" class="name">
              						<label for="destinationWarehouse"><g:message code="inventoryTransfer.destinationWarehouse.label" default="Destination Warehouse" /></label>
            					</td>
            					<td valign="top" class="value ${hasErrors(bean: inventoryTransferInstance, field: 'destinationWarehouse', 'errors')}">
          							<g:select name="destination" id="destinationWarehouseId" from="[]" value="${inventoryTransferInstance?.destinationWarehouse?.id}" />
          						</td>
          					</tr>
                        
          					<tr class="prop">
            					<td valign="top" class="name">
              						<label for="remark"><g:message code="inventoryTransfer.remark.label" default="Remarks" /></label>
            					</td>
            					<td valign="top" class="value ${hasErrors(bean: inventoryTransferInstance, field: 'remark', 'errors')}">
            						<g:textArea name="remark" maxlength="255" value="${inventoryTransferInstance?.remark}" />
          						</td>
          					</tr>
                        </tbody>
                    </table>
                </div>
                
                <div class="list">
					<table id="searchInventoryTransferItemsTable">
					<thead>
						<th>Product Id</th>
						<th width="20%">Identifier</th>
						<th width="50%">Description</th>
						<th>Origin Warehouse Stock</th>
						<th>Destination Warehouse Stock</th>
					</thead>
					</table>
				</div>
				
    			<div class="subTable">
      				<table id="componentsTable">
        				<thead class="componentsTable">
          					<tr>
            					<th class="center">Identifier</th>
            					<th class="center">Description</th>
            					<th class="center">Origin Warehouse Stock</th>
            					<th class="center">Destination Warehouse Stock</th>
            					<th class="center">Quantity</th>
            					<th class="center">Remove</th>
          					</tr>
        				</thead>
        				<tbody class="editable components" >
         				<g:each in="${items}" var="item" status="idx">
          					<tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
          						<g:hiddenField class="productId" name="itemList[${item.index}].product.id" value="${item.productId}" />
          						<g:hiddenField class="originWarehouseStock" name="originWarehouseStock" value="${item.originWarehouseStock}" />
          						<g:hiddenField class="destinationWarehouseStock" name="destinationWarehouseStock" value="${item.destinationWarehouseStock}" />
          						
            					<td><a href="${createLink(uri:'/')}product/show/${item?.index}">${item?.identifier}</a></td>
            					<td>${item?.description}</td>
            					<td>${item?.originWarehouseStock}</td>
            					<td>${item?.destinationWarehouseStock}</td>
            					<td class="right">
            						<input type="text" class="qty" maxlength="7" name="itemList[${item.index}].qty" id="itemList[${item.index}].qty" value="${item?.qty}" />
            					</td>
            					<td class="right" id="cancelExisting">
									<img src="../images/cancel.png" class="remove">
									<g:hiddenField class="deleted" name="itemList[${item.index}].isDeleted" value="${item.isDeleted}" />
								</td>
          					</tr>
        				</g:each> 
        				</tbody>
        			</table>
        		</div>
        		
				<div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
