
<%@ page import="com.munix.CustomerDiscount" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'customerDiscount.label', default: 'Customer Discount')}" />
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
                            <g:sortableColumn property="customer" title="${message(code: 'customerDiscount.customer.label', default: 'Customer')}" />
                            <g:sortableColumn property="type" title="${message(code: 'customerDiscount.type.label', default: 'Type')}" />
                            <g:sortableColumn property="discountGroup" title="${message(code: 'customerDiscount.discountGroup.label', default: 'Discount Group')}" />
                            <g:sortableColumn property="discountType" title="${message(code: 'customerDiscount.discountType.label', default: 'Discount Type')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${customerDiscountInstanceList}" status="i" var="customerDiscountInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}customer/show/${customerDiscountInstance.customer.id}'">
                            <td>${fieldValue(bean: customerDiscountInstance, field: "customer")}</td>
                            <td>${fieldValue(bean: customerDiscountInstance, field: "type")}</td>
                            <td>${fieldValue(bean: customerDiscountInstance, field: "discountGroup")}</td>
                        	<td>${fieldValue(bean: customerDiscountInstance, field: "discountType")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${customerDiscountInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
