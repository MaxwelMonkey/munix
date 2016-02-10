
<%@ page import="com.munix.InventoryAdjustment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="inventoryAdjustment.list" default="Inventory Adjustment List" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="inventoryAdjustment.new" default="New Inventory Adjustment" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="inventoryAdjustment.list" default="Inventory Adjustment List" /></h1>
            <g:if test="${flash.message}">
      			<div class="message">${flash.message}</div>
   			</g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	    <g:sortableColumn property="id" title="ID" titleKey="inventoryAdjustment.id" />
                   	    
                   	    <g:sortableColumn property="warehouse" title="Warehouse" titleKey="inventoryAdjustment.warehouse" />
                   	    
                   	    <g:sortableColumn property="itemType" title="Item Type" titleKey="inventoryAdjustment.itemType" />
                   	    
                   	    <g:sortableColumn property="inventoryStatus" title="Inventory Status" titleKey="inventoryAdjustment.inventoryStatus" />
                        
                   	    <g:sortableColumn property="remark" title="Remarks" titleKey="inventoryAdjustment.remark" />
                   	    
                   	    <g:sortableColumn property="dateCreated" title="Date Created" titleKey="inventoryAdjustment.dateCreated" />

                   	    <g:sortableColumn property="status" title="Status" titleKey="inventoryAdjustment.status" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${inventoryAdjustmentInstanceList.sort{it.id}}" status="i" var="inventoryAdjustmentInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}inventoryAdjustment/show/${inventoryAdjustmentInstance.id}'">

                        	<td>${inventoryAdjustmentInstance?.toString()}</td>
                        	
                            <td>${fieldValue(bean: inventoryAdjustmentInstance, field: "warehouse.identifier")}</td>
                            
                            <td>${fieldValue(bean: inventoryAdjustmentInstance, field: "itemType.identifier")}</td>
                            
                            <td>${fieldValue(bean: inventoryAdjustmentInstance, field: "inventoryStatus")}</td>
                            
                            <td>${fieldValue(bean: inventoryAdjustmentInstance, field: "remark")}</td>
                            
                            <td><g:formatDate date="${inventoryAdjustmentInstance?.dateGenerated}" format="MMMM dd, yyyy" /></td>
                            
						    <td>${fieldValue(bean: inventoryAdjustmentInstance, field: "status")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${inventoryAdjustmentInstanceTotal}" />
            </div>
        </div>
        
    </body>
</html>
