
<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title>Sales Order Summary Report</title>
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
	}

	div.list table{
		border-left:0px;
		border-right:0px;
	}
  </style>
</head>
<body>
    <div class="list">
      <g:each in="${list.keySet()}" status="i" var="bean">
      <table>
        <thead>
          <tr>
	        <th colspan="3">${bean}</th>
        </tr>
        	<tr>
        		<th>Date</th>
        		<th>Sales Order #</th>
        		<th>Product</th>
        		<th>Quantity Remaining</th>
        	</tr>
        </thead>
        <tbody>
        	<g:set var="total" value="${0}"/>
	        	<g:each in="${list[bean]}" var="record">
		        	<g:set var="total" value="${total+record.computeRemainingBalance()}"/>
	    	    	<tr>
		       			<td><g:formatDate date="${record.invoice?.date}" format="MM/dd/yyyy"/></td>
		       			<td>${record.invoice}</td>
		       			<td>${record.product} - ${record.product?.description}</td>
		       			<td>${record.formatRemainingBalance()}</td>
			    	</tr>
			    </g:each>
        </tbody>
          <tr>
	        <th></th>
	        <th></th>
	        <th align="right">Total</th>
	        <th><g:formatNumber number="${total}" format="###,##0" /></th>
        </tr>
      </table>
	  </g:each>
    </div>
</body>
</html>
