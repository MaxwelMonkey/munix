<%@ page import="com.munix.DirectPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'directPayment.label', default: 'DirectPayment')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="generalmethods.js" />
    <g:javascript src="calendarStructTemplate.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
            $("#searchIdentifier").ForceNumericOnlyEnterAllowed(true)
            setCalendarStruct($("#searchDateFrom"), $("#searchDateFrom_value"))
        	setCalendarStruct($("#searchDateTo"), $("#searchDateTo_value"))
        })
    </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a id="home" class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link elementId="newDirectPayment" class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
  	<calendar:resources lang="en" theme="aqua"/>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div id="search">
      <g:form controller="directPayment" action="list" >
        <table>
          <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField name="searchIdentifier" value ="${params.searchIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Customer ID</td>
            <td class="value"><g:textField name="searchCustomerId" value ="${params.searchCustomerId}"/></td>
          </tr>
          <tr>
            <td class="name">Customer Name</td>
            <td class="value"><g:textField name="searchCustomerName" value ="${params.searchCustomerName}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="${['':'']}" from="${['Unapproved','Approved','Cancelled']}" value ="${params.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value" width="900px">
              <calendar:datePicker name="searchDateFrom" years="2009,2030" value='${dateMap.searchDateFrom}' />
           	  to
           	  <calendar:datePicker name="searchDateTo" years="2009,2030" value='${dateMap.searchDateTo}'/>
           	</td>
          </tr>
          <tr>
            <td class="name">With Balance</td>
            <td class="value"><g:select name="searchBalance" noSelection="${['null':'']}" from="${['Yes','No']}" value='${params?.searchBalance}'/></td>
          </tr>
        </table>
        <div>
          <input type="submit" class="button" value="Search"/>
        </div>
      </g:form>
    </div>
    
    <div class="list">
      <table>
        <thead>
          <tr>

        <g:sortableColumn id="id" class="center" property="id" title="${message(code: 'directPayment.id.label', default: 'Id')}" params="${params}"/>
        <th><g:message code="directPayment.customer.label" default="Customer" /></th>
        <th class="right">Amount of Payment</th>
        <th class="right">Amount of Invoices Applied</th>
        <th class="right">Balance</th>
        <g:sortableColumn id="status" property="status" title="${message(code: 'direct`Payment.status.label', default: 'Status')}" params="${params}"/>
        <g:sortableColumn id="date" class="center" property="date" title="${message(code: 'directPayment.date.label', default: 'Date')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${directPaymentInstanceList}" status="i" var="directPaymentInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directPayment/show/${directPaymentInstance.id}'">

            <td id="rowDirectPaymentId${i}" class="center">${directPaymentInstance}</td>
            <td>${fieldValue(bean: directPaymentInstance, field: "customer")}</td>
            <td class="right">${directPaymentInstance.formatPaymentTotal()}</td>
            <td class="right">${directPaymentInstance.formatInvoiceTotal()}</td>
            <td class="right">${directPaymentInstance.formatBalance()}</td>

            <td>${fieldValue(bean: directPaymentInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${directPaymentInstance.date}" format="MM/dd/yyyy" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${directPaymentInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
