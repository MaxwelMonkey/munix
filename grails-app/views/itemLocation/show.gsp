
<%@ page import="com.munix.ItemLocation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="itemLocation.show" default="Show Item Location" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="itemLocation.list" default="Item Location List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="itemLocation.new" default="New Item Location" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="itemLocation.show" default="Show Item Location" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${itemLocationInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="itemLocation.description" default="Description" />:</td>
                                
                                <td valign="top" class="value">${fieldValue(bean: itemLocationInstance, field: "description")}</td>
                                
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="itemLocation.warehouse" default="Warehouse" />:</td>
                                
                                <td valign="top" class="value"><g:link controller="warehouse" action="show" id="${itemLocationInstance?.warehouse?.id}">${itemLocationInstance?.warehouse?.encodeAsHTML()}</g:link></td>
                                
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
