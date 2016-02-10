
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
	        <th onclick="sortReport('date')">Date</th>
	        <th onclick="sortReport('salesDeliveryId')">SD #</th>
	        <th onclick="sortReport('deliveryReceiptNumber')">DR #</th>
	        <th onclick="sortReport('salesDeliveryNumber')">SI #</th>
	        <th onclick="sortReport('invoice.discountType')">Discount Type</th>
	        <th onclick="sortReport('customer.identifier')">Customer</th>
            <th onclick="sortReport('computeDiscountedTotal()')" class="right">Discount Total</th>
            <th onclick="sortReport('computeNetItemsTotal()')" class="right">Net Total</th>
          <th onclick="sortReport('computeTotalAmount()')" class="right">Grand Total</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="discountTotal" value="${0}"/>
        	<g:set var="netTotal" value="${0}"/>
        	<g:set var="grandTotal" value="${0}"/>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
	       			<td><g:formatDate date="${bean['date']}" format="MM/dd/yyyy"/></td>
		        	<td>${bean.salesDeliveryId}</td>
		        	<td>${bean.deliveryReceiptNumber}</td>
		        	<td>${bean.salesDeliveryNumber}</td>
		        	<td>${bean.invoice?.discountType}</td>
		        	<td>${bean.customer}</td>
                        <g:if test="${bean.status=='Cancelled'}">
                        <td class="right">0.00</td>
                        <td class="right">0.00</td>
                        </g:if>
                        <g:else>
                        <td class="right">${String.format('%,.2f',bean.computeDiscountedTotal())}</td>
                        <td class="right">${String.format('%,.2f',bean.computeNetTotal())}</td>
                        <td class="right">${String.format('%,.2f',bean.computeTotalAmount())}</td>
                        <g:set var="discountTotal" value="${discountTotal + bean.computeDiscountedTotal()}"/>
                        <g:set var="netTotal" value="${netTotal + bean?.computeNetTotal()}"/> <%--computation: netTotal + bean?.computeNetTotal() --%>
                        <g:set var="grandTotal" value="${grandTotal + bean.computeTotalAmount()}"/>
                        </g:else>
		    	</tr>
	    	</g:each>
        </tbody>

        <g:if test="${params.totalHidden != 'Y'}">
          <tr>
	        <th></th>
	        <th></th>
	        <th></th>
	        <th></th>
	        <th></th>
	        <th class="right">Grand Total</th>
	        <g:if test="${list.size()==0}">
	        <th class="right">0.00</th>
	        <th class="right">0.00</th>
	        <th class="right">0.00</th>
	        </g:if>
	        <g:else>
	        <th class="right">${String.format('%,.2f',discountTotal)}</th>
	        <th class="right">${String.format('%,.2f',netTotal)}</th>
            <th class="right">${String.format('%,.2f',grandTotal)}</th>
	        </g:else>
          </tr>
       </g:if>

      </table>
    </div>
</body>
</html>
