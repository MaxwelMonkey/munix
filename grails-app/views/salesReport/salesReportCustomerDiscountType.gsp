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
		cursor:pointer;
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
    ${discTypes}
      <table>
        <thead>
          <tr>
	        <th rowspan="2">Date</th>
	        <g:each in="${discTypes}" status="i" var="discountType">
	        <th colspan="${customerTypes.size()}">${discountType}</th>
	        </g:each>
	      </tr>
          <tr>
	        <g:each in="${discTypes}" status="i" var="discountType">
		        <g:each in="${customerTypes}" var="customerType">
		        <th>${customerType.description}</th>
		        </g:each>
	        </g:each>
	      </tr>
        </thead>
        <tbody>
        	<g:set var="grandTotal" value="${0.00}"/>
        	<g:set var="currentDate" value="${'a'}"/>
        	<g:set var="discountTypeTotals" value="${[:]}"/>
	        <g:each in="${list}" status="i" var="bean">
	        	<g:set var="date" value="${formatDate(date:bean.date, format:'MM/dd/yyyy')}"/>
	        	<g:if test="${currentDate!=date}">
	        		<g:if test="${currentDate!='a'}">
		    	    	<tr>
			       			<td>${currentDate}</td>
					        <g:each in="${discTypes}" var="discountType">
						        <g:each in="${customerTypes}" var="customerType">
						        	<g:set var="discTypeTotal" value="${discountTypeTotals[discountType.id]}"/>
						        	<g:set var="custTypeTotal" value="${0.00}"/>
						        	<g:if test="${discTypeTotal}">
							        	<g:set var="custTypeTotal" value="${discTypeTotal[customerType.id]}"/>
							        	<g:if test="${!custTypeTotal}">
							        	<g:set var="custTypeTotal" value="${0.00}"/>
							        	</g:if>
						        	</g:if>
							        <td class="right">${String.format('%,.2f',custTypeTotal)}</td>
								 	<g:set var="grandTotal" value="${grandTotal + custTypeTotal}"/>
						        </g:each>
					        </g:each>
				    	</tr>
				    </g:if>
		        	<g:set var="discountTypeTotals" value="${[:]}"/>
	        		<g:set var="currentDate" value="${date}"/>
		    	</g:if>
        		<g:set var="discTypeTotal" value="${discountTypeTotals[bean.invoice.discountType.id]}"/>
        		<g:set var="custTypeTotal" value="${0}"/>
	        	<g:if test="${discTypeTotal}">
	        		<g:set var="custTypeTotal" value="${discTypeTotal[bean.customer.type.id]}"/>
		        	<g:if test="${custTypeTotal}">
		        		<g:set var="custTypeTotal" value="${custTypeTotal + bean.computeTotalAmount()}"/>
		        	</g:if>
		        	<g:else>
        			<g:set var="custTypeTotal" value="${0}"/>
		        	</g:else>
	        	</g:if>
	        	<g:else>
	        		<g:set var="discTypeTotal" value="${[:]}"/>
	        		<g:set var="custTypeTotal" value="${bean.computeTotalAmount()}"/>
	        	</g:else>
				<g:if test="${bean.status=='Cancelled'}">
        			<g:set var="custTypeTotal" value="${0}"/>
        		</g:if>
        		<%
        			discTypeTotal[bean.customer.type.id] = custTypeTotal;
        			discountTypeTotals[bean.invoice.discountType.id] = discTypeTotal;
        		%>
	    	</g:each>
        </tbody>
          <g:if test="${params.totalHidden != 'Y'}">
          <tr>
	        <th></th>
	        <th></th>
	        <th></th>
	        <th class="right">Grand Total</th>
	        <g:if test="${list.size()==0}">
	        <th class="right">0.00</th>
	        </g:if>
	        <g:else>
	        <th class="right">${String.format('%,.2f',grandTotal)}</th>
	        </g:else>
          </tr>
          </g:if>
      </table>
    </div>
</body>
</html>
