
<%@ page import="com.munix.Stock" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'stock.label', default: 'Stock')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${stockInstance}">
            <div class="errors">
                <g:renderErrors bean="${stockInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="warehouse"><g:message code="stock.warehouse.label" default="Warehouse" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: stockInstance, field: 'warehouse', 'errors')}">
                                    <g:select name="warehouse.id" from="${com.munix.Warehouse.list()}" optionKey="id" value="${stockInstance?.warehouse?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="product"><g:message code="stock.product.label" default="Product" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: stockInstance, field: 'product', 'errors')}">
                                    <g:select name="product.id" from="${com.munix.Product.list()}" optionKey="id" value="${stockInstance?.product?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="qty"><g:message code="stock.qty.label" default="Qty" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: stockInstance, field: 'qty', 'errors')}">
                                    <g:textField name="qty" value="${fieldValue(bean: stockInstance, field: 'qty')}" />
                                </td>
                            </tr>
                        
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
