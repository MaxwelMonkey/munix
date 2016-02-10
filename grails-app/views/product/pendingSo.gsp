<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
    <g:javascript library="reports/filter" />
    <title>Pending SO</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1>Pending SO for ${product}</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
    	<h2>Pending SO</h2>
      <table>
        <thead>
          <tr>
          <th>Date</th>
          <th>Reference #</th>
          <th>Customer</th>
          <th>Discount Type</th>
          <th>Price</th>
	        <th class="center">Quantity</th>
    	    <th class="center">Delivered</th>
        	<th class="center">Remaining</th>
          </tr>
        </thead>
        <tbody>
        <g:set var="total" value="${0.00}"/>
        <g:each in="${pendingSo}" status="i" var="soi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${soi.invoice.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="salesOrder" action="show" id="${soi.invoice.id}">${soi.invoice}</g:link></td>
          	<td><g:link controller="customer" action="show" id="${soi.invoice?.customer?.id}">${soi.invoice?.customer}</g:link></td>
          	<td>${soi.invoice.discountType}</td>
          	<td>${soi.formatFinalPrice()}</td>
	          <td class="right">${soi?.formatQty()}</td>
	          <td class="right">${soi?.formatDeliveredQty()}</td>
	          <td class="right">${soi?.formatRemainingBalance()}</td>
          </tr>
          <g:set var="total" value="${total + soi.computeRemainingBalance()}"/>
        </g:each>
		<tr class="odd">
			<td colspan="7" class="right"><strong>Total</strong></td>
			<td class="right"><strong>${String.format('%,.0f',total)}</strong></td>
		</tr>
        </tbody>
      </table>
    </div>
    
    
    <div class="list">
    	<h2>Pending Unapproved SO</h2>
      <table>
        <thead>
          <tr>
          <th>Date</th>
          <th>Reference #</th>
          <th>Customer</th>
          <th>Discount Type</th>
          <th>Price</th>
	        <th class="center">Quantity</th>
    	    <th class="center">Delivered</th>
        	<th class="center">Remaining</th>
          </tr>
        </thead>
        <tbody>
        <g:set var="total" value="${0.00}"/>
        <g:each in="${pendingUnapprovedSo}" status="i" var="soi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${soi.invoice.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="salesOrder" action="show" id="${soi.invoice.id}">${soi.invoice}</g:link></td>
          	<td><g:link controller="customer" action="show" id="${soi.invoice?.customer?.id}">${soi.invoice?.customer}</g:link></td>
          	<td>${soi.invoice.discountType}</td>
          	<td>${soi.formatFinalPrice()}</td>
	          <td class="right">${soi?.formatQty()}</td>
	          <td class="right">${soi?.formatDeliveredQty()}</td>
	          <td class="right">${soi?.formatRemainingBalance()}</td>
          </tr>
          <g:set var="total" value="${total + soi.computeRemainingBalance()}"/>
        </g:each>
		<tr class="odd">
			<td colspan="7" class="right"><strong>Total</strong></td>
			<td class="right"><strong>${String.format('%,.0f',total)}</strong></td>
		</tr>
        </tbody>
      </table>
    </div>
    
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${product?.id}" />
       	  <span class="button"><g:link action="show" id="${product.id}">${message(code: 'default.button.show.label', default: 'Back to Product')}</g:link></span>
      </g:form>
    </div>

  </div>
</body>
</html>
