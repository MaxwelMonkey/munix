
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
	        <th>SD Date</th>
	        <th>SD #</th>
	        <th>Customer</th>
	        <th>Invoice Total</th>
	        <th>Cost Total</th>
	        <th>PHP Diff</th>
	        <th>% Margin</th>
	        <th>Grand Total</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="sd">
    	    	<tr>
	       			<td>${formatDate(date:sd.date, format:'MM/dd/yyyy')}</td>
		        	<td>${sd}</td>
		        	<td>${sd.customer}</td>
		        	<td class="right">${String.format('%,.2f',sd.computeTotalAmount())}</td>
		        	<td class="right">${String.format('%,.2f',sd.computeCostTotal())}</td>
		        	<td class="right">${String.format('%,.2f',sd.computePhpDiffTotal())}</td>
		        	<td class="right">
		        	<% try{ %>
		        	${String.format('%,.2f',sd.computeAverageMarginPercentage())}
		        	<%}catch (Exception e){ %>
		        	error: ${sd.computeAverageMarginPercentage()}
		        	<%}%>
		        	</td>
		        	<td class="right">${String.format('%,.2f',sd.computeMarginGrandTotal())}</td>
		    	</tr>
	    	</g:each>
        </tbody>


      </table>
    </div>
</body>
</html>
