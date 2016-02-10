
<%@ page import="com.munix.SupplierItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'supplierItem.label', default: 'SupplierItem')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'supplierItem.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="productCode" title="${message(code: 'supplierItem.productCode.label', default: 'Product Code')}" />
                        
                            <th><g:message code="supplierItem.product.label" default="Product" /></th>
                   	    
                            <g:sortableColumn property="price" title="${message(code: 'supplierItem.price.label', default: 'Price')}" />
                        
                            <th><g:message code="supplierItem.supplier.label" default="Supplier" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${supplierItemInstanceList}" status="i" var="supplierItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}supplierItem/show/${supplierItemInstance.id}'">
                        
                            <td><g:link action="show" id="${supplierItemInstance.id}">${fieldValue(bean: supplierItemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: supplierItemInstance, field: "productCode")}</td>
                        
                            <td>${fieldValue(bean: supplierItemInstance, field: "product")}</td>
                        
                            <td>${fieldValue(bean: supplierItemInstance, field: "price")}</td>
                        
                            <td>${fieldValue(bean: supplierItemInstance, field: "supplier")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${supplierItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
