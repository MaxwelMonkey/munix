
<%@ page import="com.munix.CustomerChargeItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'customerChargeItem.label', default: 'CustomerChargeItem')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customerChargeItem.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerChargeItemInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customerChargeItem.charge.label" default="Charge" /></td>
                            
                            <td valign="top" class="value"><g:link controller="charge" action="show" id="${customerChargeItemInstance?.charge?.id}">${customerChargeItemInstance?.charge?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customerChargeItem.amount.label" default="Amount" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: customerChargeItemInstance, field: "amount")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="customerChargeItem.customerCharge.label" default="Customer Charge" /></td>
                            
                            <td valign="top" class="value"><g:link controller="customerCharge" action="show" id="${customerChargeItemInstance?.customerCharge?.id}">${customerChargeItemInstance?.customerCharge?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${customerChargeItemInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
