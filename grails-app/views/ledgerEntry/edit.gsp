
<%@ page import="com.munix.LedgerEntry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'ledgerEntry.label', default: 'LedgerEntry')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${ledgerEntryInstance}">
            <div class="errors">
                <g:renderErrors bean="${ledgerEntryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${ledgerEntryInstance?.id}" />
                <g:hiddenField name="version" value="${ledgerEntryInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="ledgerEntry.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ledgerEntryInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${ledgerEntryInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="type"><g:message code="ledgerEntry.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ledgerEntryInstance, field: 'type', 'errors')}">
                                    <g:textField name="type" value="${ledgerEntryInstance?.type}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="customer"><g:message code="ledgerEntry.customer.label" default="Customer" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ledgerEntryInstance, field: 'customer', 'errors')}">
                                    <g:select name="customer.id" from="${com.munix.Customer.list()}" optionKey="id" value="${ledgerEntryInstance?.customer?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="debit"><g:message code="ledgerEntry.debit.label" default="Debit" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ledgerEntryInstance, field: 'debit', 'errors')}">
                                    <g:textField name="debit" value="${fieldValue(bean: ledgerEntryInstance, field: 'debit')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="credit"><g:message code="ledgerEntry.credit.label" default="Credit" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ledgerEntryInstance, field: 'credit', 'errors')}">
                                    <g:textField name="credit" value="${fieldValue(bean: ledgerEntryInstance, field: 'credit')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="balance"><g:message code="ledgerEntry.balance.label" default="Balance" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ledgerEntryInstance, field: 'balance', 'errors')}">
                                    <g:textField name="balance" value="${fieldValue(bean: ledgerEntryInstance, field: 'balance')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="items"><g:message code="ledgerEntry.items.label" default="Items" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ledgerEntryInstance, field: 'items', 'errors')}">
                                    
<ul>
<g:each in="${ledgerEntryInstance?.items?}" var="i">
    <li><g:link controller="ledgerEntryItem" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="ledgerEntryItem" action="create" params="['ledgerEntry.id': ledgerEntryInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'ledgerEntryItem.label', default: 'LedgerEntryItem')])}</g:link>

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
