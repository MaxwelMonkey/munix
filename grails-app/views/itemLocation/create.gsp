
<%@ page import="com.munix.ItemLocation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="itemLocation.create" default="Create Item Location" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="itemLocation.list" default="Item Location List" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="itemLocation.create" default="Create Item Location" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${itemLocationInstance}">
            <div class="errors">
                <g:renderErrors bean="${itemLocationInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="itemLocation.description" default="Description" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemLocationInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${fieldValue(bean: itemLocationInstance, field: 'description')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="warehouse"><g:message code="itemLocation.warehouse" default="Warehouse" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: itemLocationInstance, field: 'warehouse', 'errors')}">
                                    <g:select name="warehouse.id" from="${com.munix.Warehouse.list()}" optionKey="id" value="${itemLocationInstance?.warehouse?.id}"  />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
