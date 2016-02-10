
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
      <table>
        <thead>
          <tr>
	        <th>Date</th>
	        <th>DP #</th>
	        <th>Customer</th>
	        <th class="right">Amount</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="dpId" value="${null}"/>
        	<g:set var="dp" value="${null}"/>
        	<g:set var="total" value="${0}"/>
        	<!--- Merge DP Items into DPs --->
	        <g:each in="${list}" status="i" var="bean">
	        	<g:if test="${dpId!=bean.directPayment.formatId()}">
		        	<g:if test="${dp!=null}">
	    	    	<tr>
		       			<td><g:formatDate date="${dp['date']}" format="MM/dd/yyyy"/></td>
			        	<td>${dp}</td>
			        	<td>${dp.customer}</td>
			        	<td class="right">${String.format('%,.2f',dp.computePaymentTotal())}</td>
			    	</tr>
			    	</g:if>
	        		<g:set var="dpId" value="${bean.directPayment.formatId()}"/>
	        		<g:set var="dp" value="${bean.directPayment}"/>
		        	<g:set var="total" value="${total + dp.computePaymentTotal()}"/>
	        	</g:if>
	    	</g:each>
        	<g:if test="${dp!=null}">
	    	<tr>
       			<td><g:formatDate date="${dp['date']}" format="MM/dd/yyyy"/></td>
	        	<td>${dp.formatId()}</td>
	        	<td>${dp.customer}</td>
	        	<td class="right">${String.format('%,.2f',dp.computePaymentTotal())}</td>
	    	</tr>
	    	</g:if>
        </tbody>
          <g:if test="${params.totalHidden != 'Y'}">
          <tr>
	        <th></th>
	        <th></th>
	        <th class="right">Total</th>
	        <th class="right">${String.format('%,.2f',total)}</th>
        </tr>
          </g:if>
      </table>
    </div>
</body>
</html>
