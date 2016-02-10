
<%@ page import="com.munix.MaterialRequisitionItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'materialRequisitionItem.label', default: 'MaterialRequisitionItem')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'materialRequisitionItem.id.label', default: 'Id')}" />
                        
                            <th><g:message code="materialRequisitionItem.component.label" default="Component" /></th>
                   	    
                            <g:sortableColumn property="qty" title="${message(code: 'materialRequisitionItem.qty.label', default: 'Qty')}" />
                        
                            <g:sortableColumn property="qtyReceived" title="${message(code: 'materialRequisitionItem.qtyReceived.label', default: 'Qty Received')}" />
                        
                            <th><g:message code="materialRequisitionItem.requisition.label" default="Requisition" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${materialRequisitionItemInstanceList}" status="i" var="materialRequisitionItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${materialRequisitionItemInstance.id}">${fieldValue(bean: materialRequisitionItemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: materialRequisitionItemInstance, field: "component")}</td>
                        
                            <td>${fieldValue(bean: materialRequisitionItemInstance, field: "qty")}</td>
                        
                            <td>${fieldValue(bean: materialRequisitionItemInstance, field: "qtyReceived")}</td>
                        
                            <td>${fieldValue(bean: materialRequisitionItemInstance, field: "requisition")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${materialRequisitionItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
