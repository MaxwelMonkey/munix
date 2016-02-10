
<%@ page import="com.munix.CheckPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'unpaidCheckPayment.label', default: 'Unpaid Check Payment')}" />
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
  	<h1><g:message code="unpaidCheckPayment.list" default="Unpaid Check Payment List" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

    <div class="dialog">
      <table>
        <tbody>
          	<tr class="prop">
            	<td valign="top" class="name"><g:message code="customer.identifier.label" default="Customer" /></td>
        		<td valign="top" class="value"><g:link action="show" controller="customer" id="${customerInstance.id}">${customerInstance.toString()}</g:link></td>
        	</tr>
          	<tr class="prop">
        		<td valign="top" class="name"><g:message code="bouncedCheck.unpaidTotal.label" default="Total" /></td>
        		<td valign="top" class="value">${totalUnpaidCheckPayments}</td>
        	</tr>
        </tbody>
       </table>
     </div>
        
    <div class="list">
      <table>
        <thead>
          <tr>
          	<th class="center">Check Date</th>
          	<th class="center">Check Number</th>
          	<th class="center">Bank - Branch</th>
          	<th class="center">Amount</th>
          	<th class="center">Direct Payment #</th>
          	<th class="center">Check Deposit #</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${unpaidCheckPaymentList}" status="cp" var="unpaidCheckPaymentInstance">
          <tr class="${(cp % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directPayment/show/${unpaidCheckPaymentInstance?.directPaymentItem?.directPayment?.id}'">
          	<td class="right"><g:formatDate date="${unpaidCheckPaymentInstance.date}" format="MM/dd/yyyy" /></td>
          	<td class="right">${unpaidCheckPaymentInstance.checkNumber}</td>
          	<td>${unpaidCheckPaymentInstance?.bank?.identifier} - ${unpaidCheckPaymentInstance?.branch}</td>
            <td class="right">${unpaidCheckPaymentInstance?.formatAmount()}</td>
            <td class="right">${unpaidCheckPaymentInstance?.directPaymentItem?.directPayment?.toString()}</td>
            <td class="right">${unpaidCheckPaymentInstance?.retrieveCurrentCheckDeposit()?.toString()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${unpaidCheckPaymentTotal}" />
    </div>
  </div>
</body>
</html>