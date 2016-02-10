
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
		color:black !important;
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
    	<g:set var="paymentTotal" value="${0}"/>
      <g:each in="${list.keySet()}" status="i" var="salesAgent">
      <table>
        <thead>
          <tr>
	        <th colspan="99">Sales Agent: ${salesAgent}</th>
        </tr>
	      <g:each in="${list[salesAgent].keySet()}" status="j" var="paymentType">
          <tr>
	        <th colspan="99">Payment Type: ${paymentType}</th>
        </tr>
          <tr>
	        <th>Date</th>
	        <th>DP #</th>
	        <th>Reference #</th>
	        <th>Bank Branch</th>
	        <th>Check #</th>
	        <th>Check Date</th>
	        <th>Remarks</th>
	        <th class="right">Amount</th>
        </tr>
        </thead>
        <tbody>
        	<g:set var="total" value="${0}"/>
	        	<g:each in="${list[salesAgent][paymentType]}" var="bean">
	        	<g:set var="total" value="${total + bean.amount}"/>
    	    	<tr>
	       			<td><g:formatDate date="${bean['date']}" format="MM/dd/yyyy"/></td>
		        	<td>${bean.directPayment}</td>
		        	<td>${bean.relatedCreditMemo()}</td>
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
	    	<g:set var="paymentTotal" value="${paymentTotal + bean.amount}"/>
	    	</g:each>
        </tbody>
          <tr>
	        <td></td><td></td><td></td><td></td><td></td><td></td>
	        <th class="right">Total</th>
	        <th class="right">${String.format('%,.2f',total)}</th>
          </tr>
	    	</g:each>
    	</g:each>
          <tr>
          	<th colspan="6"></th>
	        <th>Payment Totals:</th>
        </tr>
	      <g:each in="${paymentTypeTotals.keySet()}" status="j" var="paymentType">
          <tr>
          	<td colspan="6"></td>
	        <td>${paymentType} Total:</td>
	        <td style="text-align:right">${String.format('%,.2f',paymentTypeTotals[paymentType])}</td>
        </tr>
        	</g:each>
          <tr>
          	<th colspan="6"></th>
	        <th>Payment Grand Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',paymentTotal)}</td>
        </tr>
          <tr>
          	<th colspan="6"></th>
	        <th>Applied Invoices Total:</th>
        </tr>
        	<g:set var="appliedGrandTotal" value="${0}"/>
	      <g:each in="${totals.keySet()}" status="j" var="key">
          <tr>
          	<th colspan="6"></th>
	        <td>${key}</td>
	        <td style="text-align:right">${String.format('%,.2f',totals[key])}</td>
        </tr>
        	<g:set var="appliedGrandTotal" value="${appliedGrandTotal + totals[key]}"/>
        </g:each>
        <tr>
          	<th colspan="6"></th>
	        <th>Applied Invoices Grand Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',appliedGrandTotal)}</td>
        </tr>
          <tr>
          	<th colspan="6"></th>
	        <th>Unapplied Overpayment Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',balance)}</td>
        </tr>
        <g:set var="grandTotal" value="${paymentTotal}"/>
          <g:if test="${params.totalHidden != 'Y'}">
          <tr>
          	<th colspan="6"></th>
	        <th>Grand Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',grandTotal)}</td>
        </tr>
          </g:if>
        <tr>
          	<th colspan="6"></th>
	        <th>Net Total:</th>
	        <g:set var="netTotal" value="${grandTotal}"/>
	        <td style="text-align:right">${String.format('%,.2f',netTotal)}</td>
        </tr>
          <tr>
          	<th colspan="6"></th>
	        <th>Commission:</th>
	        <td style="text-align:right">${String.format('%,.2f',grandTotal*0.01)}</td>
        </tr>
          <tr>
          	<th colspan="6"></th>
	        <th>Net Commission:</th>
	        <td style="text-align:right">${String.format('%,.2f',netTotal*0.01)}</td>
        </tr>
      </table>
    </div>
</body>
</html>
