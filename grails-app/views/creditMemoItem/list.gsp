
<%@ page import="com.munix.CreditMemoItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'creditMemoItem.label', default: 'Credit Memo Item')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'creditMemoItem.id.label', default: 'Id')}" />
                        
                            <th><g:message code="creditMemoItem.delivery.label" default="Delivery" /></th>
                   	    
                            <th><g:message code="creditMemoItem.product.label" default="Product" /></th>
                   	    
                            <g:sortableColumn property="oldQty" title="${message(code: 'creditMemoItem.oldQty.label', default: 'Old Qty')}" />
                        
                            <g:sortableColumn property="oldPrice" title="${message(code: 'creditMemoItem.oldPrice.label', default: 'Old Price')}" />
                        
                            <g:sortableColumn property="newQty" title="${message(code: 'creditMemoItem.newQty.label', default: 'New Qty')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${creditMemoItemInstanceList}" status="i" var="creditMemoItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${creditMemoItemInstance.id}">${fieldValue(bean: creditMemoItemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: creditMemoItemInstance, field: "delivery")}</td>
                        
                            <td>${fieldValue(bean: creditMemoItemInstance, field: "product")}</td>
                        
                            <td>${fieldValue(bean: creditMemoItemInstance, field: "oldQty")}</td>
                        
                            <td>${fieldValue(bean: creditMemoItemInstance, field: "oldPrice")}</td>
                        
                            <td>${fieldValue(bean: creditMemoItemInstance, field: "newQty")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${creditMemoItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
