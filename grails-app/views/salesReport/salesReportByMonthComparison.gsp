<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	div.list table tr td, div.list table tr th {
		font-size:60%;
	}
	
	div.list table tr td{
	}

	div.list table tr th{
		background-color:white;
		color:black;
		border-top:0px;
		cursor:pointer;
		text-align:center;
	}

	div.list table{
		border-left:0px;
		border-right:0px;
	}
	div.list table tr td,div.list table tr th{
		
	}
	
	.right{
		text-align:right;
	}
  </style>
</head>
<body>
    <div class="list">
      <table>
        <thead>
          <tr>
	        <th rowspan="2">Date</th>
	        <g:each in="${customerTypes}" var="customerType">
		        <th colspan="${discTypes.size()}">${customerType.description}</th>
	        </g:each>
	      </tr>
          <tr>
	        <g:each in="${customerTypes}" var="customerType">
		        <g:each in="${discTypes}" status="i" var="discountType">
		        <th>${discountType}</th>
		        </g:each>
		    </g:each>
	      </tr>
        </thead>
        <tbody>
        	<g:set var="grandTotal" value="${0.00}"/>
        	<g:set var="discountTypeTotals" value="${[:]}"/>
	        <g:each in="${months}" status="i" var="month">
	        	<tr>
	        		<td>${month}</td>
		        <g:each in="${customerTypes}" var="customerType">
			        <g:each in="${discTypes}" var="discountType">
			        	<g:set var="amount" value="${result[month]?.get(customerType.id)?.get(discountType.id)?:0.00}"/>
			        	<td style="text-align:right;">${String.format('%,.2f',amount)}</td>
			    	</g:each>
		    	</g:each>
		    	</tr>
	    	</g:each>
        	<tr>
        		<th rowspan="3">Totals</th>
	        <g:each in="${customerTypes}" var="customerType">
		        <g:each in="${discTypes}" var="discountType">
		        	<g:set var="amount" value="${totals.get(customerType.id)?.get(discountType.id)?:0.00}"/>
		        	<th style="text-align:right">${String.format('%,.2f',amount)}</th>
		    	</g:each>
	    	</g:each>
	    	</tr>
        	<tr>
	        <g:each in="${customerTypes}" var="customerType">
	        	<g:set var="customerTypeAmount" value="${0.00}"/>
		        <g:each in="${discTypes}" var="discountType">
		        	<g:set var="customerTypeAmount" value="${customerTypeAmount+ (totals.get(customerType.id)?.get(discountType.id)?:0.00)}"/>
		    	</g:each>
	        	<td colspan="${discTypes.size()}"><strong>${customerType.description} Total : ${String.format('%,.2f',customerTypeAmount)}</strong></td>
	        	<g:set var="grandTotal" value="${grandTotal + customerTypeAmount}"/>
	    	</g:each>
	    	</tr>
	    	<tr>
	    		<td colspan="99"><strong>Grand Total: ${String.format('%,.2f',grandTotal)}</strong></td>
	    	</tr>
        </tbody>
      </table>
    </div>
    <script>alert("Please print in Landscape mode.");</script>
</body>
</html>
