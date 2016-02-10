
<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'directPayment.label', default: 'DirectPayment')}" />
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
      <g:each in="${list.keySet()}" status="i" var="type">
      <table>
        <thead>
          <tr>
	        <th colspan="99">${type}</th>
        </tr>
          <tr>
	        <th>Date</th>
	        <th>DP #</th>
	        <th>Customer Name</th>
	        <th>Bank Branch</th>
	        <th>Check #</th>
	        <th>Check Date</th>
	        <th>Remarks</th>
	        <th class="right">Amount</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="total" value="${0}"/>
	        	<g:each in="${list[type]}" var="bean">
	        	<g:set var="total" value="${total + bean.amount}"/>
    	    	<tr>
	       			<td><g:formatDate date="${bean['date']}" format="MM/dd/yyyy"/></td>
		        	<td>${bean.directPayment}</td>
		        	<td>${bean.directPayment?.customer}</td>
		        	<g:if test="${bean instanceof com.munix.DirectPaymentItemCheck}">
			        	<td>${bean?.checkPayment?.bank?.identifier} - ${bean?.checkPayment?.branch}</td>
			        	<td>${bean?.checkPayment?.checkNumber}</td>
			        	<td><g:formatDate date="${bean.checkPayment?.date}" format="MM/dd/yyyy"/></td>
	        		</g:if>
	        		<g:else>
	        			<td></td><td></td><td></td>
	        		</g:else>
		        	<td>${bean.remark}</td>
		        	<td class="right">${String.format('%,.2f',bean.amount)}</td>
		    	</tr>
	    	</g:each>
        </tbody>
          <g:if test="${params.totalHidden != 'Y'}">
          <tr>
	        <td></td><td></td><td></td><td></td><td></td><td></td>
	        <th class="right">Total</th>
	        <th class="right">${String.format('%,.2f',total)}</th>
          </tr>
          </g:if>
      </table>
    	</g:each>
    </div>
</body>
</html>
