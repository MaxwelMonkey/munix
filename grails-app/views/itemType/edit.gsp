
<%@ page import="com.munix.ItemType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="itemType.edit" default="Edit Item Type" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="itemType.list" default="Item Type List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="itemType.new" default="New Item Type" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="itemType.edit" default="Edit ItemType" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${itemTypeInstance}">
            <div class="errors">
                <g:renderErrors bean="${itemTypeInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${itemTypeInstance?.id}" />
                <g:hiddenField name="version" value="${itemTypeInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="identifier"><g:message code="itemType.identifier" default="Identifier" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemTypeInstance, field: 'identifier', 'errors')}">
                                    <g:textField name="identifier" value="${fieldValue(bean: itemTypeInstance, field: 'identifier')}" maxlength="100"/>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="itemType.description" default="Description" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemTypeInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${fieldValue(bean: itemTypeInstance, field: 'description')}" maxlength="100"/>

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'update', 'default': 'Update')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
