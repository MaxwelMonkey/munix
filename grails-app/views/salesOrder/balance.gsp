
<%@ page import="com.munix.DiscountGroup; com.munix.Customer; com.munix.PrintLogSalesOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <title><g:message code="default.balanceVsQuantity.label" default="Balance vs. Quantity"/></title>
</head>
<body>
  <div class="body">
    <h1><g:message code="default.balanceVsQuantity.label" default="Balance vs. Quantity"/></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.id.label" default="Id" /></td>
	        <td valign="top" class="value">${salesOrderInstance}</td>
	        <td valign="top" class="name"><g:message code="salesOrder.date.label" default="Date" /></td>
	        <td valign="top" class="value"><g:formatDate date="${salesOrderInstance?.date}" format="MMM. dd, yyyy" /></td>
          </tr>

        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Discounted Price Items</h2>

        <thead>
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Part Number</th>
            <th class="center">Description</th>
	        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="i">
	          <th class="right">${i?.identifier}</th>
	        </g:each>        
            <th class="center">Quantity</th>
            <th class="center">Delivered</th>
            <th class="center">Remaining</th>
          </tr>
        </thead>
          <tbody class="uneditable">

        <g:each in="${salesOrderInstance?.items?.findAll {!it?.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
          <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" >
          </g:if>

          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          </g:else>

          <td>${i?.product}</td>
          <td>${i?.product?.partNumber}</td>
          <td>${i?.product?.description}</td>
	        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
            <td class="right">${i?.product?.formatSOH(warehouse)}</td>
	        </g:each>        
          <td class="right">${i?.formatQty()}</td>
          <td class="right">${i?.formatDeliveredQty()}</td>
          <td class="right">${i?.formatRemainingBalance()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>


      <div class="subTable">
        <table>
          <h2>Net Price Items</h2>

          <thead>
            <tr>
              <th class="center">Identifier</th>
              <th class="center">Part Number</th>
              <th class="center">Description</th>
	        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="i">
	          <th class="right">${i?.identifier}</th>
	        </g:each>        
            <th class="center">Quantity</th>
            <th class="center">Delivered</th>
            <th class="center">Remaining</th>
            </tr>
          </thead>
            <tbody class="uneditable">

          <g:each in="${salesOrderInstance?.items?.findAll {it?.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
            <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
              <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
            </g:if>

            <g:else>
              <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
            </g:else>

            <td>${i?.product}</td>
            <td>${i?.product?.partNumber}</td>
            <td>${i?.product?.description}</td>
	        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
            <td class="right">${i?.product?.formatSOH(warehouse)}</td>
	        </g:each>        
	          <td class="right">${i?.formatQty()}</td>
	          <td class="right">${i?.formatDeliveredQty()}</td>
	          <td class="right">${i?.formatRemainingBalance()}</td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>

  </div>
  
</body>
</html>
