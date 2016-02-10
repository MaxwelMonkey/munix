
<%@ page import="com.munix.TripTicketItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tripTicketItem.label', default: 'TripTicketItem')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${tripTicketItemInstance}">
            <div class="errors">
                <g:renderErrors bean="${tripTicketItemInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="priority"><g:message code="tripTicketItem.priority.label" default="Priority" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tripTicketItemInstance, field: 'priority', 'errors')}">
                                    <g:textField name="priority" value="${fieldValue(bean: tripTicketItemInstance, field: 'priority')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="type"><g:message code="tripTicketItem.type.label" default="Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tripTicketItemInstance, field: 'type', 'errors')}">
                                    <g:textField name="type" value="${tripTicketItemInstance?.type}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="tripTicket"><g:message code="tripTicketItem.tripTicket.label" default="Trip Ticket" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tripTicketItemInstance, field: 'tripTicket', 'errors')}">
                                    <g:select name="tripTicket.id" from="${com.munix.TripTicket.list()}" optionKey="id" value="${tripTicketItemInstance?.tripTicket?.id}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
