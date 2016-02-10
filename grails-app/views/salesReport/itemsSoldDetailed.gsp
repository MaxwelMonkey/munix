
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
	div.list table tr td,div.list table tr th{
		font-size:8px!important;
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
	        <th>Customer</th>
	        <th>Qty</th>
	        <th>Product Code</th>
	        <th>Product Description</th>
	        <th>Average Cost</th>
	        <th>Average Price</th>
	        <th>Total Amount</th>
        </tr>
        </thead>
       	<g:set var="netTotal" value="${0}"/>
       	<g:set var="qtyTotal" value="${0}"/>
        <tbody>
	        <g:each in="${customerIds}" status="h" var="customerId">
	        	<g:each in="${customers[customerId]['ids']}" status="i" var="id">
		        	<g:set var="product" value="${customers[customerId]['products'][id]}"/>
		        	<g:if test="${product.quantity!=0}">
	    	    	<tr>
		       			<td>${customers[customerId]['customer']}</td>
		       			<td class="right">${String.format('%,.0f',product.quantity)}</td>
			        	<td>${product.product.identifier}</td>
			        	<td>${product.product.description}</td>
			        	<td class="right">${String.format('%,.2f',product.cost / product.quantity)}</td>
			        	<td class="right">${String.format('%,.2f',product.amount / product.quantity)}</td>
                        %{--<g:if test="${params.totalHidden != 'Y'}">--}%
                            <td class="right">${String.format('%,.2f',product.amount)}</td>
                        %{--</g:if>--}%
			    	</tr>
		        	<g:set var="qtyTotal" value="${qtyTotal + product.quantity}"/>
		        	<g:set var="netTotal" value="${netTotal + (product.amount)}"/>
		        	</g:if>
		    	</g:each>
	    	</g:each>
        </tbody>

          <tr>
	        <th></th>
	        <g:if test="${list.size()==0}">
	        <th class="right">0</th>
	        </g:if>
	        <g:else>
	        <th class="right">${String.format('%,.0f',qtyTotal)}</th>
	        </g:else>
	        <th></th>
	        <th></th>
	        <th class="right"></th>
          <g:if test="${params.totalHidden != 'Y'}">
	        <g:if test="${list.size()==0}">
	        <th class="right">PHP 0.00</th>
	        </g:if>
	        <g:else>
	        <th class="right">${String.format('%,.2f',netTotal)}</th>
	        </g:else>
          </g:if>
          <g:else>
              <th></th>
          </g:else>
          </tr>

      </table>
    </div>
</body>
</html>
