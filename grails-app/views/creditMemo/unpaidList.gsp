
<%@ page import="com.munix.BouncedCheck" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'unpaidBouncedCheck.label', default: 'Unpaid Bounced Check')}" />
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
  	<h1><g:message code="unpaidDebitMemo.list" default="Unpaid Debit Memo List" /></h1>
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
        		<td valign="top" class="name"><g:message code="debitMemo.unpaidTotal.label" default="Total" /></td>
        		<td valign="top" class="value">${debitMemoListTotal}</td>
        	</tr>
        </tbody>
       </table>
     </div>
        
    <div class="list">
      <table>
        <thead>
          <tr>
          	<th class="center">Date</th>
          	<th class="center">Reference #</th>
          	<th class="center">Amount</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${debitMemoList}" status="bc" var="unpaidCreditMemoInstance">
          <tr class="${(bc % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}creditMemo/show/${unpaidCreditMemoInstance?.id}'">
          	<td class="right"><g:formatDate date="${unpaidCreditMemoInstance.date}" format="MM/dd/yyyy" /></td>
          	<td>${unpaidCreditMemoInstance?.toString()}</td>
            <td class="right">${unpaidCreditMemoInstance?.computeCreditMemoTotalAmount()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${debitMemoListTotal}" />
    </div>
  </div>
</body>
</html>