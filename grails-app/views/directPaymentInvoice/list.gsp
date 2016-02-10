
<%@ page import="com.munix.DirectPaymentInvoice" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'directPaymentInvoice.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="type" title="${message(code: 'directPaymentInvoice.type.label', default: 'Type')}" />
                        
                            <g:sortableColumn property="amount" title="${message(code: 'directPaymentInvoice.amount.label', default: 'Amount')}" />
                        
                            <th><g:message code="directPaymentInvoice.directPayment.label" default="Direct Payment" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${directPaymentInvoiceInstanceList}" status="i" var="directPaymentInvoiceInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${directPaymentInvoiceInstance.id}">${fieldValue(bean: directPaymentInvoiceInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: directPaymentInvoiceInstance, field: "type")}</td>
                        
                            <td>${fieldValue(bean: directPaymentInvoiceInstance, field: "amount")}</td>
                        
                            <td>${fieldValue(bean: directPaymentInvoiceInstance, field: "directPayment")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${directPaymentInvoiceInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
