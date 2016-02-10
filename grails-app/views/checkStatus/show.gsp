
<%@ page import="com.munix.CheckStatus" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="checkStatus.show" default="Show Check Status" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="checkStatus.list" default="Check Status List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="checkStatus.new" default="New Check Status" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="checkStatus.show" default="Show Check Status" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${checkStatusInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="checkStatus.identifier" default="Identifier" />:</td>
                                
                                <td id="checkStatusIdentifier" valign="top" class="value">${fieldValue(bean: checkStatusInstance, field: "identifier")}</td>
                                
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="checkStatus.description" default="Description" />:</td>
                                
                                <td id="checkStatusDescription" valign="top" class="value">${fieldValue(bean: checkStatusInstance, field: "description")}</td>
                                
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
