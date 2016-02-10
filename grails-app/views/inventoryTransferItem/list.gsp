
<%@ page import="com.munix.InventoryTransferItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'inventoryTransferItem.id.label', default: 'Id')}" />
                        
                            <th><g:message code="inventoryTransferItem.product.label" default="Product" /></th>
                   	    
                            <g:sortableColumn property="qty" title="${message(code: 'inventoryTransferItem.qty.label', default: 'Qty')}" />
                        
                            <g:sortableColumn property="receivedQty" title="${message(code: 'inventoryTransferItem.receivedQty.label', default: 'Received Qty')}" />
                        
                            <th><g:message code="inventoryTransferItem.transfer.label" default="Transfer" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${inventoryTransferItemInstanceList}" status="i" var="inventoryTransferItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}inventoryTransferItem/show/${inventoryTransferItemInstance.id}'">
                        
                            <td><g:link action="show" id="${inventoryTransferItemInstance.id}">${fieldValue(bean: inventoryTransferItemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: inventoryTransferItemInstance, field: "product")}</td>
                        
                            <td>${fieldValue(bean: inventoryTransferItemInstance, field: "qty")}</td>
                        
                            <td>${fieldValue(bean: inventoryTransferItemInstance, field: "receivedQty")}</td>
                        
                            <td>${fieldValue(bean: inventoryTransferItemInstance, field: "transfer")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${inventoryTransferItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
