<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Customer List</title>
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
      <g:each in="${list.keySet()}" status="i" var="product">
      <table>
        <thead>
          <tr>
	        <th colspan="99">Product: ${product}</th>
        </tr>
          <tr>	
	        <th>Job Order Date</th>
	        <th>Job Order #</th>
	        <th>Job Out Date</th>
	        <th>Job Out #</th>
	        <th>Assembly Group Name</th>
	        <th>Quantity</th>
	        <th>Cost</th>
	        <th>Total</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="total" value="${0.00}"/>
        	<g:each in="${list[product]}" var="jobOrder">
	        	<g:each in="${jobOrder.jobOuts}" var="jobOut">
	    	    	<tr>
		       			<td><g:formatDate date="${jobOrder.endDate}" format="MM/dd/yyyy"/></td>
		       			<td>${jobOrder}</td>
		       			<td><g:formatDate date="${jobOut.date}" format="MM/dd/yyyy"/></td>
			        	<td>${jobOut}</td>
			        	<td>${jobOrder.assignedTo}</td>
			        	<td class="right">${jobOut.qty}</td>
			        	<g:if test="${jobOut.laborCost?.amount}">
			        	<td class="right">${String.format('%,.2f',jobOut.laborCost?.amount?:0)}</td>
			        	<td class="right">${String.format('%,.2f',jobOut.qty * jobOut.laborCost.amount)}</td>
			        	<g:set var="total" value="${total + (jobOut.qty * jobOut.laborCost.amount)}"/>
			        	</g:if>
			        	<g:else>
			        	<td class="right">0.00</td>
			        	<td class="right">0.00</td>
			        	</g:else>
			    	</tr>
		    	</g:each>
	    	</g:each>
	    	<tr>
	    		<td colspan="4"></td>
	    		<th class="right">Total:</th>
	    		<td></td>
	    		<td></td>
		        <th class="right">${String.format('%,.2f',total)}</th>
	    	</tr>
        </tbody>
      </table>
   	</g:each>
    </div>
</body>
</html>
