
<%@ page import="com.munix.InventoryAdjustment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="inventoryAdjustment.create" default="Create InventoryAdjustment" /></title>
        <link rel="stylesheet" href="${resource(dir:'js/table/css',file:'demo_table.css')}" />
    <g:javascript src="generalmethods.js" />
    <g:javascript src="numbervalidation.js" />
    <g:javascript src="jquery.calculation.js" />
    <g:javascript src="table/jquery.dataTables.js" />
	<g:javascript src="table/jquery.dataTables.forceReload.js" />
	<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
	<g:javascript src="inventoryAdjustmentTemplate.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="inventoryAdjustment.list" default="Inventory Adjustment List" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="inventoryAdjustment.create" default="Create Inventory Adjustment" /></h1>
            <g:if test="${flash.message}">
      			<div class="message">${flash.message}</div>
   			</g:if>
            <g:hasErrors bean="${inventoryAdjustmentInstance}">
            <div class="errors">
                <g:renderErrors bean="${inventoryAdjustmentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                        	<tr class="prop">
            					<td valign="top" class="name">
              						<label for="warehouse"><g:message code="inventoryAdjustment.warehouse.label" default="Warehouse" /></label>
            					</td>
            					<td valign="top" class="value ${hasErrors(bean: inventoryAdjustmentInstance, field: 'warehouse', 'errors')}">
          							<g:select name="warehouse.id" id="warehouseId" from="${com.munix.Warehouse.list().sort{it.toString()}}" optionKey="id" value="${inventoryAdjustmentInstance?.warehouse?.id}" />
          						</td>
          					</tr>

                        	<tr class="prop">
            					<td valign="top" class="name">
              						<label for="itemType"><g:message code="inventoryAdjustment.itemType.label" default="Item Type" /></label>
            					</td>
            					<td valign="top" class="value ${hasErrors(bean: inventoryAdjustmentInstance, field: 'itemType', 'errors')}">
          							<g:select name="itemType.id" id="itemTypeId" from="${com.munix.ItemType.list().sort{it.toString()}}" optionKey="id" value="${inventoryAdjustmentInstance?.itemType?.id}" />
          						</td>
          					</tr>
          					
          					<tr class="prop">
            					<td valign="top" class="name">
              						<label for="status"><g:message code="inventoryAdjustment.inventoryStatus.label" default="Inventory Status" /></label>
            					</td>
            					<td valign="top">
          							<g:select name="inventoryStatus" id="inventoryStatus" from='["Active", "Inactive"]' value="${inventoryAdjustmentInstance?.inventoryStatus}"/>
          						</td>
          					</tr>
                        
          					<tr class="prop">
            					<td valign="top" class="name">
              						<label for="remark"><g:message code="inventoryAdjustment.remark.label" default="Remarks" /></label>
            					</td>
            					<td valign="top" class="value ${hasErrors(bean: inventoryAdjustmentInstance, field: 'remark', 'errors')}">
            						<g:textArea name="remark" maxlength="255" value="${inventoryAdjustmentInstance?.remark}" />
          						</td>
          					</tr>
          					
                        </tbody>
                    </table>
                </div>
                
                <div class="list">
					<table id="searchInventoryAdjustmentItemsTable">
					<thead>
						<th>Product Id</th>
						<th width="20%">Identifier</th>
						<th width="50%">Description</th>
						<th>Stock</th>
					</thead>
					</table>
				</div>

    			<div class="subTable">
      				<table id="componentsTable">
        				<thead class="componentsTable">
          					<tr>
            					<th class="center">Identifier</th>
            					<th class="center">Description</th>
            					<th class="center">Old Stock</th>
            					<th class="center">New Stock</th>
            					<th class="center">Difference</th>
            					<th class="center">Remove</th>
          					</tr>
        				</thead>
        				<tbody class="editable components" >
         				<g:each in="${items}" var="item" status="idx">
          					<tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
          						<g:hiddenField class="productId" name="itemList[${item.index}].product.id" value="${item.productId}" />
								<g:hiddenField class="oldStock" name="itemList[${item.index}].oldStock" value="${item?.oldStock}"/>
            					<td><a href="${createLink(uri:'/')}product/show/${item?.productId}">${item?.identifier}</a></td>
            					<td>${item?.description}</td>
            					<td class="right">
            						${item?.oldStock}
            					</td>
            					<td class="right">
            						<input type="text" class="newStock" maxlength="7" name="itemList[${item.index}].newStock" id="itemList[${item.index}].newStock" value="${item?.newStock}" />
            					</td>
								<td class="right difference">
            						${item?.stockDifference}
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
