
<%@ page import="com.munix.DirectPaymentItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'directPaymentItem.label', default: 'DirectPaymentItem')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'directPaymentItem.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="date" title="${message(code: 'directPaymentItem.date.label', default: 'Date')}" />
                        
                            <th><g:message code="directPaymentItem.paymentType.label" default="Payment Type" /></th>
                   	    
                            <g:sortableColumn property="amount" title="${message(code: 'directPaymentItem.amount.label', default: 'Amount')}" />
                        
                            <g:sortableColumn property="type" title="${message(code: 'directPaymentItem.type.label', default: 'Type')}" />
                        
                            <th><g:message code="directPaymentItem.directPayment.label" default="Direct Payment" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${directPaymentItemInstanceList}" status="i" var="directPaymentItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${directPaymentItemInstance.id}">${fieldValue(bean: directPaymentItemInstance, field: "id")}</g:link></td>
                        
                            <td><g:formatDate date="${directPaymentItemInstance.date}" /></td>
                        
                            <td>${fieldValue(bean: directPaymentItemInstance, field: "paymentType")}</td>
                        
                            <td>${fieldValue(bean: directPaymentItemInstance, field: "amount")}</td>
                        
                            <td>${fieldValue(bean: directPaymentItemInstance, field: "type")}</td>
                        
                            <td>${fieldValue(bean: directPaymentItemInstance, field: "directPayment")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${directPaymentItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
