
<%@ page import="com.munix.CheckStatus" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="checkStatus.create" default="Create Check Status" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="checkStatus.list" default="Check Status List" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="checkStatus.create" default="Create Check Status" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${checkStatusInstance}">
            <div class="errors">
                <g:renderErrors bean="${checkStatusInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
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
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
