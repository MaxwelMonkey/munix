<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
    <g:javascript library="reports/filter" />
    <title>Pending SO/PO</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1>Pending SO/PO for ${product}</h1>
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
          <th>Discount Type</th>
	        <th class="center">Quantity</th>
    	    <th class="center">Delivered</th>
        	<th class="center">Remaining</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${pendingSo}" status="i" var="soi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${soi.invoice.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="salesOrder" action="show" id="${soi.invoice.id}">${soi.invoice}</g:link></td>
          	<td>${soi.invoice.discountType}</td>
	          <td class="right">${soi?.formatQty()}</td>
	          <td class="right">${soi?.formatDeliveredQty()}</td>
	          <td class="right">${soi?.formatRemainingBalance()}</td>
          </tr>
        </g:each>

        </tbody>
      </table>
    </div>
    
    <div class="list">
    	<h2>Pending PO</h2>
      <table>
        <thead>
          <tr>
			<th>Date</th>
			<th>Reference #</th>
	        <th class="right">Price</th>
	        <th class="right">Final Price</th>
	        <th class="right">Quantity</th>
	        <th class="right">Received</th>
	        <th class="right">Remaining</th>
	        <th class="right">Amount</th>
	      </tr>
        </thead>
        <tbody>
        <g:each in="${pendingPo}" status="i" var="poi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${poi.po.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="purchaseOrder" action="show" id="${poi.po.id}">${poi.po.formatId()}</g:link></td>
	          <td class="right">
          	  	${poi?.formatPrice()}
          	  </td>
	          	
	          <td class="right">
	          	${poi?.formatFinalPrice()}
	          </td>
	          <td class="right">${poi?.formatQty()}</td>
	          <td class="right">${poi?.formatReceivedQty()}</td>
	          <td class="right">${poi?.formatRemainingBalance()}</td>
	          <td class="right">${poi?.formatAmount()}</td>
          </tr>
        </g:each>

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
          <th>Discount Type</th>
	        <th class="center">Quantity</th>
    	    <th class="center">Delivered</th>
        	<th class="center">Remaining</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${pendingUnapprovedSo}" status="i" var="soi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${soi.invoice.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="salesOrder" action="show" id="${soi.invoice.id}">${soi.invoice}</g:link></td>
          	<td>${soi.invoice.discountType}</td>
	          <td class="right">${soi?.formatQty()}</td>
	          <td class="right">${soi?.formatDeliveredQty()}</td>
	          <td class="right">${soi?.formatRemainingBalance()}</td>
          </tr>
        </g:each>

        </tbody>
      </table>
    </div>
    
    <div class="list">
    	<h2>Pending Unapproved PO</h2>
      <table>
        <thead>
          <tr>
			<th>Date</th>
			<th>Reference #</th>
	        <th class="right">Price</th>
	        <th class="right">Final Price</th>
	        <th class="right">Quantity</th>
	        <th class="right">Received</th>
	        <th class="right">Remaining</th>
	        <th class="right">Amount</th>
	      </tr>
        </thead>
        <tbody>
        <g:each in="${pendingUnapprovedPo}" status="i" var="poi">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
           	<td><g:formatDate date="${poi.po.date}" format="MM/dd/yyyy"/></td>
          	<td><g:link controller="purchaseOrder" action="show" id="${poi.po.id}">${poi.po.formatId()}</g:link></td>
	          <td class="right">
          	  	${poi?.formatPrice()}
          	  </td>
	          	
	          <td class="right">
	          	${poi?.formatFinalPrice()}
	          </td>
	          <td class="right">${poi?.formatQty()}</td>
	          <td class="right">${poi?.formatReceivedQty()}</td>
	          <td class="right">${poi?.formatRemainingBalance()}</td>
	          <td class="right">${poi?.formatAmount()}</td>
          </tr>
        </g:each>

        </tbody>
      </table>
    </div>
    
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${customerInstance?.id}" />
       	  <span class="button"><g:actionSubmit class="show" action="show" value="${message(code: 'default.button.show.label', default: 'Back to Customer')}" /></span>
      </g:form>
    </div>

  </div>
</body>
</html>
