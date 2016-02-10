
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
  	<h1><g:message code="unpaidBouncedCheck.list" default="Unpaid Bounced Check List" /></h1>
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
        		<td valign="top" class="value">${totalUnpaidBouncedChecks}</td>
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
          	<th class="center">Balance</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${unpaidBouncedCheckList}" status="bc" var="unpaidBouncedCheckInstance">
          <tr class="${(bc % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}bouncedCheck/show/${unpaidBouncedCheckInstance?.id}'">
          	<td class="right"><g:formatDate date="${unpaidBouncedCheckInstance.date}" format="MM/dd/yyyy" /></td>
          	<td>${unpaidBouncedCheckInstance?.toString()}</td>
            <td class="right">${unpaidBouncedCheckInstance?.formatTotal()}</td>
            <g:if test="${unpaidBouncedCheckInstance.forRedeposit}">
                <td class="right">PHP <g:formatNumber number="${unpaidBouncedCheckInstance?.computeNotClearedChecks()}" format="###,##0.00" /></td>
            </g:if>
            <g:else>
                <td class="right">PHP <g:formatNumber number="${unpaidBouncedCheckInstance?.computeProjectedDue()}" format="###,##0.00" /></td>
            </g:else>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${unpaidBouncedCheckTotal}" />
    </div>
  </div>
</body>
</html>