
<%@ page import="com.munix.TripTicketItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tripTicketItem.label', default: 'TripTicketItem')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'tripTicketItem.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="priority" title="${message(code: 'tripTicketItem.priority.label', default: 'Priority')}" />
                        
                            <g:sortableColumn property="type" title="${message(code: 'tripTicketItem.type.label', default: 'Type')}" />
                        
                            <th><g:message code="tripTicketItem.tripTicket.label" default="Trip Ticket" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${tripTicketItemInstanceList}" status="i" var="tripTicketItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${tripTicketItemInstance.id}">${fieldValue(bean: tripTicketItemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: tripTicketItemInstance, field: "priority")}</td>
                        
                            <td>${fieldValue(bean: tripTicketItemInstance, field: "type")}</td>
                        
                            <td>${fieldValue(bean: tripTicketItemInstance, field: "tripTicket")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${tripTicketItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
