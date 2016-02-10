
<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title>Unpaid Sales Delivery List</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Unpaid Sales Delivery List</h1>
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
        		<td valign="top" class="value">PHP <g:formatNumber number="${totalUnpaidSalesDelivery}" format="###,##0.00" /></td>
        	</tr>
        </tbody>
       </table>
     </div>
    
    <div class="list">
      <table>
        <thead>
          <tr>
       	<g:sortableColumn class="center" property="date" title="${message(code: 'salesDelivery.date.label', default: 'Date')}" params="${params}"/>
       	<g:sortableColumn class="center" property="id" title="${message(code: 'salesDelivery.id.label', default: 'Id')}" params="${params}"/>
       	<g:sortableColumn class="center" property="deliveryReceiptNumber" title="${message(code: 'salesDelivery.deliveryReceiptNumber.label', default: 'DR#')}" params="${params}"/>
        <g:sortableColumn class="center" property="invoice" title="Sales Order" params="${params}"/>
        <th class="center">Amount</th>
        <th class="center">Balance</th>
        
        </tr>
        </thead>
        <tbody>
        <g:each in="${salesDeliveryInstanceList}" status="i" var="salesDeliveryInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}salesDelivery/show/${salesDeliveryInstance.id}'">
            <td class="center"><g:formatDate date="${salesDeliveryInstance.date}" format="MM/dd/yyyy"/></td>
            <td id="rowSalesDeliveryId${i}" class="center">${salesDeliveryInstance}</td>
            <td>${salesDeliveryInstance.deliveryReceiptNumber}</td>
            <td class="center"><g:link controller="salesOrder" action="show" id="${salesDeliveryInstance?.invoice?.id}">${fieldValue(bean: salesDeliveryInstance, field: "invoice")}</g:link></td>
          
          <g:if test="${salesDeliveryInstance.isCancelled()}">
            <td class="right">PHP 0.00</td>
            <td class="right">PHP 0.00</td>
          </g:if>
          <g:else>
            <td class="right">PHP <g:formatNumber number="${salesDeliveryInstance?.computeTotalAmount()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${salesDeliveryInstance?.computeAmountDue()}" format="###,##0.00" /></td>
          </g:else>
          
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${salesDeliveryInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
