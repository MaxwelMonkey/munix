<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Top Customers Report</title>
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
	        <th>Customer Code</th>
	        <th>Customer Name</th>
	        <th>Total Purchases</th>
	        <th>Balance (Total Purchases)</th>
	        <th>Misc. Charges</th>
	        <th>Balance (Misc. Charges)</th>
	        <th>Balance</th>
	        <th>Total Pending Payments</th>
	        <th>Net Balance</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
	        	<g:set var="totalPurchasesBalNet" value="${totalPurchasesBalanceNet[bean.id]?totalPurchasesBalanceNet[bean.id]:0.00}"/>
	        	<g:set var="totalPurchasesBalDisc" value="${totalPurchasesBalanceDiscounted[bean.id]?totalPurchasesBalanceDiscounted[bean.id]:0.00}"/>
	        	<g:set var="amountPd" value="${amountPaid[bean.id]?amountPaid[bean.id]:0.00}"/>
	        	<g:set var="totalPurchasesBal" value="${totalPurchasesBalNet + totalPurchasesBalDisc - amountPd}"/>
	        	<g:set var="customerCharge" value="${customerCharges[bean.id]?customerCharges[bean.id]:0.00}"/>
	        	<g:set var="bouncedCheck" value="${bouncedChecks[bean.id]?bouncedChecks[bean.id]:0.00}"/>
	        	<g:set var="miscCharges" value="${customerCharge+bouncedCheck}"/>
	        	<g:set var="miscChargesBal" value="${miscChargesBalance[bean.id]?miscChargesBalance[bean.id]:0.00}"/>
	        	<g:set var="balance" value="${totalPurchasesBal + miscChargesBal}"/>
	        	<g:set var="totalPendingPayment" value="${totalPendingPayments[bean.id]?totalPendingPayments[bean.id]:0.00}"/>
	        	<g:set var="netBalance" value="${balance + totalPendingPayment}"/>
    	    	<tr>
    	    		<td>${bean.identifier}</td>
    	    		<td>${bean.name}</td>
    	    		<td class="right">${String.format('%,.2f',bean.total_purchases)}</td>
    	    		<td class="right">${String.format('%,.2f',totalPurchasesBal)}</td>
    	    		<td class="right">${String.format('%,.2f',miscCharges)}</td>
    	    		<td class="right">${String.format('%,.2f',miscChargesBal)}</td>
    	    		<td class="right">${String.format('%,.2f',balance)}</td>
    	    		<td class="right">${String.format('%,.2f',totalPendingPayment)}</td>
    	    		<td class="right">${String.format('%,.2f',netBalance)}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
</body>
</html>
