
<%@ page import="com.munix.LedgerEntry" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'ledgerEntry.label', default: 'LedgerEntry')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'ledgerEntry.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'ledgerEntry.description.label', default: 'Description')}" />
                        
                            <g:sortableColumn property="type" title="${message(code: 'ledgerEntry.type.label', default: 'Type')}" />
                        
                            <th><g:message code="ledgerEntry.customer.label" default="Customer" /></th>
                   	    
                            <g:sortableColumn property="debit" title="${message(code: 'ledgerEntry.debit.label', default: 'Debit')}" />
                        
                            <g:sortableColumn property="credit" title="${message(code: 'ledgerEntry.credit.label', default: 'Credit')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${ledgerEntryInstanceList}" status="i" var="ledgerEntryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${ledgerEntryInstance.id}">${fieldValue(bean: ledgerEntryInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: ledgerEntryInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: ledgerEntryInstance, field: "type")}</td>
                        
                            <td>${fieldValue(bean: ledgerEntryInstance, field: "customer")}</td>
                        
                            <td>${fieldValue(bean: ledgerEntryInstance, field: "debit")}</td>
                        
                            <td>${fieldValue(bean: ledgerEntryInstance, field: "credit")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${ledgerEntryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
