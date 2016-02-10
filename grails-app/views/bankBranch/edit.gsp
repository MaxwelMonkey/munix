
<%@ page import="com.munix.BankBranch" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'bankBranch.label', default: 'BankBranch')}" />
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
            <g:hasErrors bean="${bankBranchInstance}">
            <div class="errors">
                <g:renderErrors bean="${bankBranchInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${bankBranchInstance?.id}" />
                <g:hiddenField name="version" value="${bankBranchInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="identifier"><g:message code="bankBranch.identifier.label" default="Identifier" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bankBranchInstance, field: 'identifier', 'errors')}">
                                    <g:textField name="identifier" value="${bankBranchInstance?.identifier}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="bankBranch.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bankBranchInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${bankBranchInstance?.description}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="bank"><g:message code="bankBranch.bank.label" default="Bank" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: bankBranchInstance, field: 'bank', 'errors')}">
                                    <g:select name="bank.id" from="${com.munix.Bank.list().sort{it.identifier}}" optionKey="id" value="${bankBranchInstance?.bank?.id}"  />
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
