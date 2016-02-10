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
    	<g:set var="paymentTotal" value="${0.00}"/>
        <g:set var="commission" value="${0.00}"/>
      <g:each in="${list.keySet()}" status="i" var="salesAgent">
      <table>
        <thead>
          <tr>
	        <th colspan="99">Sales Agent: ${salesAgent}</th>
        </tr>
          <tr>
              <th colspan="99">Commission: ${salesAgent?.commission?(salesAgent?.commission*100)+'%':'1%'}</th>
              <g:set var="commission" value="${commission += (salesAgent?.commission?:0.01)}"/>
          </tr>
	      <g:each in="${list[salesAgent].keySet()}" status="j" var="paymentType">
          <tr>
	        <th colspan="99">Payment Type: ${paymentType}</th>
        </tr>
          <tr>
	        <th>Date</th>
	        <th>Customer</th>
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
        	<g:set var="total" value="${0.00}"/>
	        	<g:each in="${list[salesAgent][paymentType]}" var="bean">
	        	<g:set var="total" value="${total + bean.amount}"/>
    	    	<tr>
	       			<td><g:formatDate date="${bean['date']}" format="MM/dd/yyyy"/></td>
	       			<td>${bean.directPayment.customer.name}</td>
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
	        <td></td><td></td><td></td><td></td><td></td><td></td><td></td>
	        <th class="right">Total</th>
	        <th class="right">${String.format('%,.2f',total)}</th>
        </tr>
	    	</g:each>
    	</g:each>
    	<tbody>
    	<tr>
    	<td colspan=2>
    	<table>
        <tr>        	
	        <th>Payment Totals:</th>
        </tr>
         <g:set var="deductibleFromSales" value="${0.00}"/>
	      <g:each in="${paymentTypeTotals.keySet()}" status="j" var="paymentType">
        <tr>
	        <td>${paymentType} Total:</td>
	        <td style="text-align:right">${String.format('%,.2f',paymentTypeTotals[paymentType])}</td>
	       
	        <g:if test="${paymentType.toString() in deductibleToSales}">
	        	<g:set var="deductibleFromSales" value="${deductibleFromSales + paymentTypeTotals[paymentType]}"/>	
	        </g:if>
        </tr>
        </g:each>
        <tr>
	        <th>Payment Grand Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',paymentTotal)}</td>
        </tr>
        </table>
        </td>
        <td colspan=5>
        <table>
          <tr>
	        <th colspan=2>Applied Invoices (Included in Commission) Total:</th>
	        <td colspan=2></td>
        </tr>
        	<g:set var="appliedGrandTotal" value="${0.00}"/>
	      <%--<g:each in="${totals.keySet()}" status="j" var="key"> --%>
	      <g:if test="${totalsAppliedInc}">
        		<g:each in="${totalsAppliedInc.keySet()}" status="k" var="disc">
        			<tr>
	        			<td>${disc} Total:</td>
	        			<td style="text-align:right">${String.format('%,.2f',totalsAppliedInc[disc])}</td>
	        			<g:set var="appliedGrandTotal" value="${appliedGrandTotal + totalsAppliedInc[disc]}"/>
        			</tr>
        		</g:each>
          </g:if>
	      <g:set var="keys" value="${['Credit Memo']}"/>
          <g:each in="${keys}" status="j" var="key">
          <tr>
	        <td>${key} Total:</td>
	        <td style="text-align:right">${String.format('%,.2f',totals[key])}</td>
        </tr>
        	<g:set var="appliedGrandTotal" value="${appliedGrandTotal + totals[key]}"/>
        </g:each>
        <%--</g:each> --%>
        <tr>
	        <th>Applied Invoices Grand (Included in Commission) Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',appliedGrandTotal)}</td>
        </tr>
        </table>
        <table>
          <tr>
	        <th colspan=2>Applied Invoices (Excluded in Commission) Total:</th>
	        <td colspan=2></td>
        </tr>
        	<g:set var="appliedGrandTotalExc" value="${0.00}"/>
        	<g:if test="${totalsAppliedExc}">
        		<g:each in="${totalsAppliedExc.keySet()}" status="k" var="disc">
        			<tr>
	        			<td>${disc} Total:</td>
	        			<td style="text-align:right">${String.format('%,.2f',totalsAppliedExc[disc])}</td>
	        			<g:set var="appliedGrandTotalExc" value="${appliedGrandTotalExc + totalsAppliedExc[disc]}"/>
        			</tr>
        		</g:each>
        	</g:if>
	       <g:set var="keys" value="${['Bounced Check','Charge']}"/>
          <g:each in="${keys}" status="j" var="key">
          	<tr>
	        	<td>${key} Total:</td>
	        	<td style="text-align:right">${String.format('%,.2f',totals[key])}</td>
        	</tr>
        	<g:set var="appliedGrandTotalExc" value="${appliedGrandTotalExc + totals[key]}"/>
       	  </g:each>
        <tr>
	        <th>Applied Invoices Grand (Excluded in Commission) Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',appliedGrandTotalExc)}</td>
        </tr>
        </table>

        </td>
        <td colspan=2>
        <table>
          <tr>
	        <th>Unapplied Overpayment Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',balance)}</td>
        </tr>
        <g:set var="grandTotal" value="${paymentTotal-appliedGrandTotalExc}"/>
        <g:if test="${params.totalHidden != 'Y'}">
            <tr>
	        <th>Grand Total:</th>
	        <td style="text-align:right">${String.format('%,.2f',grandTotal)}</td>
        </tr>
        </g:if>
        <tr>
          	
	        <th>Less:</th>
	        
	        <td style="text-align:right">${String.format('%,.2f',deductibleFromSales)}</td>
        </tr>
        <tr>
          	
	        <th>Net Total:</th>
	        <g:set var="netTotal" value="${grandTotal - deductibleFromSales}"/>
	        <td style="text-align:right">${String.format('%,.2f',netTotal)}</td>
        </tr>
          <tr>
	        <th>Commission Rate:</th>
	        <%-- <td style="text-align:right">${String.format('%,.2f',grandTotal*0.01)}</td>--%>
            <g:set var="commissionAve" value="${commission/list.size()}"/>
            <td style="text-align:right">${String.format('%,.2f', (commissionAve*100))}%</td>
        </tr>
          <tr>
	        <th>Net Commission:</th>
	        <td style="text-align:right">${String.format('%,.2f',netTotal*commissionAve)}</td>
        </tr>
		</table>
        </td>
        </tr>
        </tbody>
      </table>
    </div>
</body>
</html>
