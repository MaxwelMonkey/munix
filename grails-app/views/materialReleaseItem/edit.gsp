
<%@ page import="com.munix.MaterialReleaseItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem')}" />
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
            <g:hasErrors bean="${materialReleaseItemInstance}">
            <div class="errors">
                <g:renderErrors bean="${materialReleaseItemInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${materialReleaseItemInstance?.id}" />
                <g:hiddenField name="version" value="${materialReleaseItemInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                          <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="materialRelease"><g:message code="materialReleaseItem.materialRelease.label" default="Material Release" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialReleaseItemInstance, field: 'materialRelease', 'errors')}">
                                    <g:link action="show" controller="materialRelease" id="${materialReleaseItemInstance?.materialRelease?.id}">${materialReleaseItemInstance?.materialRelease}</g:link>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="component"><g:message code="materialReleaseItem.component.label" default="Component" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialReleaseItemInstance, field: 'component', 'errors')}">
                                   ${materialReleaseItemInstance?.component}
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="qty"><g:message code="materialReleaseItem.qty.label" default="Qty" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialReleaseItemInstance, field: 'qty', 'errors')}">
                                    <g:textField name="qty" value="${fieldValue(bean: materialReleaseItemInstance, field: 'qty')}" />
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
