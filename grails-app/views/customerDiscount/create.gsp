
<%@ page import="com.munix.CustomerDiscount" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'customerDiscount.label', default: 'CustomerDiscount')}" />
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
            <g:hasErrors bean="${customerDiscountInstance}">
            <div class="errors">
                <g:renderErrors bean="${customerDiscountInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="customer"><g:message code="customerDiscount.customer.label" default="Customer" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: customerDiscountInstance, field: 'customer', 'errors')}">
                                    <g:link elementId="customerLink" action="show" controller="customer" id="${params?.id}">${com.munix.Customer.get(params.id)}</g:link>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="discountType"><g:message code="customerDiscount.discountType.label" default="Discount Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: customerDiscountInstance, field: 'discountType', 'errors')}">
                                    <g:select name="discountType.id" from="${com.munix.DiscountType.list()}" optionKey="id" value="${customerDiscountInstance?.discountType?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="discountGroup"><g:message code="customerDiscount.discountGroup.label" default="Discount Group" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: customerDiscountInstance, field: 'discountGroup', 'errors')}">
                                    <g:select name="discountGroup.id" from="${com.munix.DiscountGroup.list()}" optionKey="id" value="${customerDiscountInstance?.discountGroup?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="customerDiscount.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: customerDiscountInstance, field: 'type', 'errors')}">
                                    <g:select name="type" from="${CustomerDiscount.Type}" optionKey="name" value="${customerDiscountInstance?.type?.name}"  />
                                </td>
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <g:hiddenField name="id" value="${params?.id}" />
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
