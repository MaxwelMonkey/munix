
<%@ page import="com.munix.InventoryAdjustment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="inventoryAdjustment.show" default="Show InventoryAdjustment" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="inventoryAdjustment.list" default="Inventory Adjustment List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="inventoryAdjustment.new" default="New Inventory Adjustment" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="inventoryAdjustment.show" default="Show Inventory Adjustment" /></h1>
            <g:if test="${flash.message}">
      			<div class="message">${flash.message}</div>
   			</g:if>
            <g:if test="${flash.error}">
      			<div class="errors">${flash.error}</div>
   			</g:if>
            <g:form>
                <g:hiddenField name="id" value="${inventoryAdjustmentInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
            					<td valign="top" class="name"><g:message code="inventoryAdjustment.id.label" default="Id" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance}</td>
	        					<td valign="top" class="name"><g:message code="inventoryAdjustment.dateGenerated.label" default="Date" /></td>
	        					<td valign="top" class="value"><g:formatDate date="${inventoryAdjustmentInstance?.dateGenerated}" format="MMM. dd, yyyy" /></td>
          					</tr>
          					
          					<tr class="prop">
            					<td valign="top" class="name"><g:message code="inventoryAdjustment.warehouse.label" default="Warehouse" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.warehouse?.identifier}</td>
	        					<td valign="top" class="name"><g:message code="inventoryAdjustment.preparedBy.label" default="Prepared By" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.preparedBy}</td>
          					</tr>
          					
          					<tr class="prop">
            					<td valign="top" class="name"><g:message code="inventoryAdjustment.itemType.label" default="Item Type" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.itemType?.identifier}</td>
	        					<td valign="top" class="name"><g:message code="inventoryAdjustment.approvedBy.label" default="Approved By" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.approvedBy}</td>
          					</tr>
          					
          					<tr class="prop">
            					<td valign="top" class="name"><g:message code="inventoryAdjustment.inventoryStatus.label" default="Inventory Status" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.inventoryStatus}</td>
	        					<td valign="top" class="name"><g:message code="inventoryAdjustment.cancelledBy.label" default="Cancelled By" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.cancelledBy}</td>
          					</tr>
          					
          					<tr class="prop">
            					<td valign="top" class="name"><g:message code="inventoryAdjustment.status.label" default="Status" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.status}</td>
	        					<td valign="top" class="name"><g:message code="inventoryAdjustment.remark.label" default="Remarks" /></td>
	        					<td valign="top" class="value">${inventoryAdjustmentInstance?.remark}</td>
          					</tr>
                        </tbody>
                    </table>
                </div>
                
                <div class="subTable">
					<table>
          				<h2>Products</h2>
	  					<thead>
							<tr>
		  						<th>Identifier</th>
		  						<th>Description</th>
		  						<th>Old Stock</th>
		  						<th>New Stock</th>
		  						<th>Difference</th>
							</tr>
	  					</thead>
	  					<tbody class="editable">
	  						<g:each in="${items}" var="item" status="colors">
								<tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}product/show/${item?.productId}'">
		  							<td>${item?.identifier}</td>
		  							<td>${item?.description}</td>
		  							<td><g:formatNumber number="${item?.oldStock}" format="###,##0.00"/></td>
		  							<td><g:formatNumber number="${item?.newStock}" format="###,##0.00"/></td>
		  							<td><g:formatNumber number="${item?.stockDifference}" format="###,##0.00"/></td>
								</tr>
	  						</g:each>
	  					</tbody>
	  				</table>
	  			</div>
                
                <div class="buttons">
                	<g:if test="${inventoryAdjustmentInstance?.isUnapproved()}">
	                	<span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'cancel', 'default': 'Cancel')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
                    	<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
						<span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
                    </g:if>
                    <span class="button"><g:link class="print" controller="print" action="inventoryAdjustment" target="inventoryAdjustment" id="${inventoryAdjustmentInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
