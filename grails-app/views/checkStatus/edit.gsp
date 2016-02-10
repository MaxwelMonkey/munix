
<%@ page import="com.munix.CheckStatus" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="checkStatus.edit" default="Edit Check Status" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="checkStatus.list" default="Check Status List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="checkStatus.new" default="New Check Status" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="checkStatus.edit" default="Edit CheckStatus" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${checkStatusInstance}">
            <div class="errors">
                <g:renderErrors bean="${checkStatusInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${checkStatusInstance?.id}" />
                <g:hiddenField name="version" value="${checkStatusInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="identifier"><g:message code="checkStatus.identifier" default="Identifier" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: checkStatusInstance, field: 'identifier', 'errors')}">
                                    <g:textField name="identifier" value="${fieldValue(bean: checkStatusInstance, field: 'identifier')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="checkStatus.description" default="Description" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: checkStatusInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${fieldValue(bean: checkStatusInstance, field: 'description')}" />

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
