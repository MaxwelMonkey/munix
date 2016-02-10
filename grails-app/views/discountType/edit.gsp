
<%@ page import="com.munix.DiscountType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'discountType.label', default: 'DiscountType')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript src="numbervalidation.js" />
        <g:javascript>
        	$(document).ready(function() {
        		$("#discountedItemMargin").ForceNumericOnly(true)
        		$("#netItemMargin").ForceNumericOnly(true)
        	});
        </g:javascript>
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
            <g:hasErrors bean="${discountTypeInstance}">
            <div class="errors">
                <g:renderErrors bean="${discountTypeInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${discountTypeInstance?.id}" />
                <g:hiddenField name="version" value="${discountTypeInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="identifier"><g:message code="discountType.identifier.label" default="Identifier" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: discountTypeInstance, field: 'identifier', 'errors')}">
                                    <g:textField name="identifier" value="${discountTypeInstance?.identifier}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="description"><g:message code="discountType.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: discountTypeInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${discountTypeInstance?.description}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="netItemMargin"><g:message code="discountType.netItemMargin.label" default="Net Item Margin" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: discountTypeInstance, field: 'netItemMargin', 'errors')}">
                                    <g:textField name="netItemMargin" id="netItemMargin" maxlength="7" value="${discountTypeInstance?.netItemMargin}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="discountedItemMargin"><g:message code="discountType.discountedItemMargin.label" default="Discounted Item Margin" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: discountTypeInstance, field: 'discountedItemMargin', 'errors')}">
                                    <g:textField name="discountedItemMargin" id="discountedItemMargin" maxlength="7" value="${discountTypeInstance?.discountedItemMargin}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="excludeInCommission"><g:message code="discountType.excludeInCommission.label" default="Exclude In Commission" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: discountTypeInstance, field: 'excludeInCommission', 'errors')}">
                                    <g:checkBox name="excludeInCommission" value="${discountTypeInstance?.excludeInCommission}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
