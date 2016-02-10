<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Year End Accounting - Detailed Sales Report</title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	div.list table tr td{
		border-left:0px;
		border-right:0px;
	}

	div.list table tr th{
		background-color:white;
		color:black;
		border-top:0px;
		border-left:0px;
		border-right:0px;
		cursor:pointer;
	}

	div.list table{
		border-left:0px;
		border-right:0px;
	}
	
	.right{
		text-align:right;
	}
	
	tr.totals td{
		font-weight:bold;
		text-align:right;
	}
  </style>
</head>
<body>
    <div class="list">
      <table>
        <thead>
          <tr>	
	        <th>Date</th>
	        <th>Reference #</th>
	        <th>Discount Type</th>
	        <th>Customer Name</th>
	        <th>Amount</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="total" value="${0}"/>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
    	    		<td>${formatDate(date:bean['date'], format:'MM/dd/yyyy')}</td>
		        	<td>${bean?.sales_delivery_number}</td>
		        	<td>${bean?.discount_type}</td>
		        	<td>${bean?.customer}</td>
		        	<td>${String.format('%,.2f',bean?.total_amount)}</td>
		    	</tr>
	        	<g:set var="total" value="${total+bean?.total_amount}"/>
	    	</g:each>
	    	<tr class="totals">
	    		<td colspan="4">Total</td>
	    		<td>${total==0?"0.00":String.format('%,.2f',total)}</td>
	    	</tr>
        </tbody>
      </table>
    </div>
</body>
</html>
