
<%@ page import="com.munix.MaterialRequisition" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'materialRequisition.label', default: 'MaterialRequisition')}" />
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
            <g:hasErrors bean="${materialRequisitionInstance}">
            <div class="errors">
                <g:renderErrors bean="${materialRequisitionInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${materialRequisitionInstance?.id}" />
                <g:hiddenField name="version" value="${materialRequisitionInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="date"><g:message code="materialRequisition.date.label" default="Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'date', 'errors')}">
                                    <g:datePicker name="date" precision="day" value="${materialRequisitionInstance?.date}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="preparedBy"><g:message code="materialRequisition.preparedBy.label" default="Prepared By" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'preparedBy', 'errors')}">
                                    <g:textField name="preparedBy" value="${materialRequisitionInstance?.preparedBy}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="status"><g:message code="materialRequisition.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'status', 'errors')}">
                                    <g:textField name="status" value="${materialRequisitionInstance?.status}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="jobOrder"><g:message code="materialRequisition.jobOrder.label" default="Job Order" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'jobOrder', 'errors')}">
                                    <g:select name="jobOrder.id" from="${com.munix.JobOrder.list()}" optionKey="id" value="${materialRequisitionInstance?.jobOrder?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="items"><g:message code="materialRequisition.items.label" default="Items" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'items', 'errors')}">
                                    
<ul>
<g:each in="${materialRequisitionInstance?.items?}" var="i">
    <li><g:link controller="materialRequisitionItem" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="materialRequisitionItem" action="create" params="['materialRequisition.id': materialRequisitionInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'materialRequisitionItem.label', default: 'MaterialRequisitionItem')])}</g:link>

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="releases"><g:message code="materialRequisition.releases.label" default="Releases" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'releases', 'errors')}">
                                    
<ul>
<g:each in="${materialRequisitionInstance?.releases?}" var="r">
    <li><g:link controller="materialRelease" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="materialRelease" action="create" params="['materialRequisition.id': materialRequisitionInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'materialRelease.label', default: 'MaterialRelease')])}</g:link>

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
