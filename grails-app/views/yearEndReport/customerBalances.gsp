<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Year End Accounting - Customer Balances</title>
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
	        <th>Customer Code - Customer Name</th>
	        <th>Balance</th>
	        <th>Pending Checks</th>
	        <th>Net Balance</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="totalBalance" value="${0}"/>
        	<g:set var="totalPendingChecks" value="${0}"/>
        	<g:set var="totalNetBalance" value="${0}"/>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
    	    		<td>${bean.identifier} - ${bean.name}</td>
    	    		<td class="right">${String.format('%,.2f',bean?.balance)}</td>
    	    		<td class="right">${bean.pending_checks?String.format('%,.2f',bean?.pending_checks):''}</td>
    	    		<td class="right">${bean.net_balance?String.format('%,.2f',bean?.net_balance):''}</td>
		    	</tr>
	        	<g:set var="totalBalance" value="${totalBalance+bean?.balance}"/>
	        	<g:set var="totalPendingChecks" value="${totalPendingChecks+(bean?.pending_checks?bean?.pending_checks:0)}"/>
	        	<g:set var="totalNetBalance" value="${totalNetBalance+(bean?.net_balance?bean?.net_balance:0)}"/>
	    	</g:each>
	    	<tr class="totals">
	    		<td>Total</td>
	    		<td>${totalBalance==0?"0.00":String.format('%,.2f',totalBalance)}</td>
	    		<td>${totalPendingChecks==0?"0.00":String.format('%,.2f',totalPendingChecks)}</td>
	    		<td>${totalNetBalance==0?"0.00":String.format('%,.2f',totalNetBalance)}</td>
	    	</tr>
        </tbody>
      </table>
    </div>
</body>
</html>
