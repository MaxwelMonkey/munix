<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Age of Receivables</title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	
	.right{
		text-align:right;
	}
  </style>
</head>
<body>
	<div class="body">
    <div class="list">
      <table>
        <thead>
          <tr>	
	        <th>Date</th>
	        <th>Sales Delivery #</th>
	        <th>DR #</th>
	        <th>SI #</th>
	        <th>SO #</th>
	        <th>Amount</th>
	        <th>Balance</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
    	    		<td>${formatDate(date:bean['date'], format:'MM/dd/yyyy')}</td>
    	    		<td><g:link controller="salesDelivery" action="show" id="${bean?.id}">${bean}</g:link></td>
    	    		<td>${bean.deliveryReceiptNumber}</td>
    	    		<td>${bean.salesDeliveryNumber}</td>
    	    		<td><g:link controller="salesOrder" action="show" id="${bean.invoice?.id}">${bean.invoice}</g:link></td>
    	    		<td>${String.format('%,.2f',bean.computeTotalAmount())}</td>
    	    		<td>${String.format('%,.2f',bean.computeAmountDue())}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
	</div>
</body>
</html>
