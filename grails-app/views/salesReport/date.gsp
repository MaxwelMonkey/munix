
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
  </style>
</head>
<body>
    <div class="list">
      <table>
        <thead>
          <tr>
	        <th>Date</th>
	        <th>Day</th>
	        <th>Total Amount</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="currDate" value=""/>
        	<g:set var="currDay" value=""/>
        	<g:set var="currTotal" value="0"/>
	       	<g:set var="netTotal" value="${0}"/>
	        <g:each in="${list}" status="i" var="bean">
	        	<g:if test="${currDate != formatDate(date:bean['date'], format:'MM/dd/yyyy')}">
	        		<g:if test="${currDate != ''}">
	    	    	<tr>
		       			<td>${currDate}</td>
		       			<td>${currDay}</td>
			        	<td>PHP ${String.format('%,.2f',currTotal)}</td>
			    	</tr>
			       	<g:set var="netTotal" value="${netTotal + currTotal}"/>
			    	</g:if>
	        		<g:set var="currDate" value="${formatDate(date:bean['date'], format:'MM/dd/yyyy')}"/>
	        		<g:set var="currDay" value="${formatDate(date:bean['date'], format:'EEEE')}"/>
		        	<g:set var="currTotal" value="0"/>
		    	</g:if>
		    	<g:else>
		    	</g:else>
		    	<g:set var="currTotal" value="${currTotal.toBigDecimal() + bean.computeTotalAmount()}"/>
		    	<%--<g:set var="currTotal" value="${currTotal.toBigDecimal() + bean.computeGrossTotal()}"/> --%>
	    	</g:each>
    		<g:if test="${currDate != ''}">
	    	<tr>
       			<td>${currDate}</td>
       			<td>${currDay}</td>
	        	<td>PHP ${String.format('%,.2f',currTotal)}</td>
	    	</tr>
	       	<g:set var="netTotal" value="${netTotal + currTotal}"/>
	       	</g:if>
        </tbody>
          <tr>
	        <th></th>
	        <th align="right">Grand Total</th>
	        <g:if test="${list.size()==0}">
	        <th>PHP 0.00</th>
	        </g:if>
	        <g:else>
	        <th>PHP ${String.format('%,.2f',netTotal)}</th>
	        </g:else>
        </tr>
      </table>
    </div>
</body>
</html>
