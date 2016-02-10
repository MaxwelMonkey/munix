
<%@ page import="com.munix.CustomerCharge" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'customerCharge.label', default: 'CustomerCharge')}" />
    <title>Unpaid Customer Charge List</title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
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
        		<td valign="top" class="value">PHP <g:formatNumber number="${totalUnpaidCustomerCharges}" format="###,##0.00" /></td>
        	</tr>
        </tbody>
       </table>
     </div>
         
    <div class="list">
      <table>
        <thead>
          <tr>

		<g:sortableColumn class="center" property="date" title="${message(code: 'customerCharge.date.label', default: 'Date')}" params="${params}"/>
        <g:sortableColumn class="center" property="id" title="${message(code: 'customerCharge.id.label', default: 'Id')}" params="${params}"/>
        <th class="right">Amount</th>
        <th class="right">Balance</th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${customerChargeInstanceList}" status="i" var="customerChargeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}customerCharge/show/${customerChargeInstance.id}'">

          <td class="center"><g:formatDate date="${customerChargeInstance.date}" format="MM/dd/yyyy" /></td>
          <td id="rowCustomerChargeId${i}" class="center">${customerChargeInstance}</td>
          <td class="right">PHP <g:formatNumber number="${customerChargeInstance?.computeTotalAmount()}" format="###,##0.00" /></td>
          <td class="right">PHP <g:formatNumber number="${customerChargeInstance?.computeActualDue()}" format="###,##0.00" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${customerChargeInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
