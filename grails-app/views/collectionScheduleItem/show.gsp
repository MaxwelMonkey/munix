
<%@ page import="com.munix.CollectionScheduleItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="collectionScheduleItem.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: collectionScheduleItemInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="collectionScheduleItem.counterReceipt.label" default="Counter Receipt" /></td>
                            
                            <td valign="top" class="value"><g:link controller="counterReceipt" action="show" id="${collectionScheduleItemInstance?.counterReceipt?.id}">${collectionScheduleItemInstance?.counterReceipt?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="collectionScheduleItem.action.label" default="Action" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: collectionScheduleItemInstance, field: "action")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="collectionScheduleItem.remark.label" default="Remark" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: collectionScheduleItemInstance, field: "remark")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="collectionScheduleItem.isComplete.label" default="Is Complete" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${collectionScheduleItemInstance?.isComplete}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="collectionScheduleItem.schedule.label" default="Schedule" /></td>
                            
                            <td valign="top" class="value"><g:link controller="collectionSchedule" action="show" id="${collectionScheduleItemInstance?.schedule?.id}">${collectionScheduleItemInstance?.schedule?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${collectionScheduleItemInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
