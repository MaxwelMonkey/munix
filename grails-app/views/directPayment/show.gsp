<%@ page import="com.munix.DirectPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript src="generalmethods.js" />
  	<g:set var="entityName" value="${message(code: 'directPayment.label', default: 'DirectPayment')}" />
	<g:javascript src="jquery.ui.core.js" />
  	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  	<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>
         <tr class="prop">
            <td valign="top" class="name"><g:message code="directPayment.id.label" default="Id" /></td>
        <td valign="top" class="value">${directPaymentInstance}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${directPaymentInstance?.date}" format="MM/dd/yyyy"/></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.customer.label" default="Customer" /></td>
        <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${directPaymentInstance?.customer?.id}">${directPaymentInstance?.customer?.encodeAsHTML()}</g:link></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.customerTerm.label" default="Term" /></td>
        <td valign="top" class="value">${directPaymentInstance?.customer?.term?.encodeAsHTML()}</td>
        </tr>
        
        <tr class="prop">
       	    <td valign="top" class="name"><g:message code="directPayment.oldestUnpaidInvoice.label" default="Oldest Unpaid Invoice" /></td>
       	    <g:set var="oui" value="${com.munix.SalesDelivery.findAllByCustomerAndStatus(directPaymentInstance.customer, 'Unpaid')?.sort{it.date}}"/>
       	    <g:if test="${oui.size()>0}"><g:set var="oui" value="${oui.get(0)}"/>
		    <td valign="top" class="value"><g:link controller="salesDelivery" action="show" id="${oui.id}"><g:formatDate date="${oui.date}"  format="MMM. dd, yyyy"/> ${oui} <g:formatNumber number="${oui?.computeAmountDue()}" format="###,##0.00" /></g:link></td>
		    </g:if>
		    <g:else>
		    <td valign="top" class="value"></td>
		    </g:else>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${directPaymentInstance?.preparedBy}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.approvedBy.label" default="Approved By" /></td>
        <td valign="top" class="value">${directPaymentInstance?.approvedBy}</td>
        </tr>
        
        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.cancelledBy.label" default="Cancelled By" /></td>
        <td valign="top" class="value">${directPaymentInstance?.cancelledBy}</td>
        </tr>
        
        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.remark.label" default="Remarks" /></td>
          <td valign="top" class="value">${fieldValue(bean: directPaymentInstance, field: "remark")}</td>
		</tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPayment.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: directPaymentInstance, field: "status")}</td>
        </tr>
		
        </tbody>
      </table>
    </div>
    <g:form>
    <div class="subTable">
      <h2>Payment</h2>
      <table>
        <thead>
          <tr>
            <th class="center">Payment Type</th>
            <th class="center">Reference #</th>
            <th class="center">Date</th>
            <th class="center">Check #</th>
            <th class="center">Check Date</th>
            <th class="center">Term</th>
            <th class="center">Bank</th>
            <th class="center">Type</th>
            <th class="center">Deductible From Sales</th>
            <th class="center">Remarks</th>
            <th class="center">Amount</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${items}" var="item" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" >
	        <td>${item?.paymentType}</td>
	        <td><g:link controller="creditMemo" action="show" id="${item?.debitMemo?.id}">${item?.debitMemo}</g:link></td>
	        <td class="right"><g:formatDate date="${item?.date}" format="MMM. dd, yyyy" /></td>
            <td>${item?.checkNumber}</td>
            <td class="right"><g:formatDate date="${item?.checkDate}" format="MMM. dd, yyyy"/></td>
            <td>${item?.term}</td>
            <td>${item?.checkBank}</td>
            <td>${item?.checkType}</td>
            <td>${item?.deductibleFromSales}</td>
            <td>${item?.remark}</td>
            <td class="right">PHP <g:formatNumber number="${item?.amount}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        </tbody>
        <tfoot>
          <tr class="total">
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <g:formatNumber number="${paymentTotal}" format="###,##0.00" /></strong></td>
          </tr>
        </tfoot>
      </table>
    </div>
 	
    <div class="subTable">
      <table>
        <h2>Invoices</h2>
        <thead>
          <tr>
            <th class="center">Invoice Date</th>
            <th class="center">Delivery Date</th>
            <th class="center">Days Diff.</th>
            <th class="center">Invoice</th>
            <th class="center">Type</th>
            <th class="center">Discount Type</th>
            <th class="center">Discounted Discount</th>
            <th class="center">Net Discount</th>
            <th class="center">Amount</th>
            <th class="center">Due</th>
            <th class="center">Amount Applied</th>
            <th class="center">Net Due</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${invoices}" var="invoice" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" >
              <td><g:formatDate date="${invoice?.date}" format="MMM. dd, yyyy"/></td>
              <td><g:formatDate date="${invoice?.deliveryDate}" format="MMM. dd, yyyy" /></td>
              <td>${invoice?.daysDiff}</td>
           	  <td><g:link url='${invoice?.customerPayment.createLink()}'>${invoice?.customerPayment}</g:link></td>
              <td>${invoice?.type}</td>
              <td class="center">${invoice?.discountType}</td>
              <td class="right">${invoice?.discountedDiscount}</td>
              <td class="right">${invoice?.netDiscount}</td>
              <td class="right">PHP <g:formatNumber number="${invoice?.amount}" format="###,##0.00" /></td>
	          <td class="right">PHP <g:formatNumber number="${invoice?.due}" format="###,##0.00" /></td>
	          <td class="right">PHP <g:formatNumber number="${invoice?.applied}" format="###,##0.00" /></td>
	          <td class="right">PHP <g:formatNumber number="${invoice?.net}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        </tbody>
        <tfoot>
          <tr class="total">
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <g:formatNumber number="${invoiceTotals?.amount}" format="###,##0.00" /></strong></td>
            <td class="right"><strong>PHP <g:formatNumber number="${invoiceTotals?.due}" format="###,##0.00" /></strong></td>
            <td class="right"><strong>PHP <g:formatNumber number="${invoiceTotals?.applied}" format="###,##0.00" /></strong></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${invoiceTotals?.net}" format="###,##0.00" /></strong></td>
          </tr>
        </tfoot>
      </table>
    </div>
   	<div class="buttons">
      	<strong>Balance: PHP <g:formatNumber number="${balance}" format="###,##0.00" /></strong>
    </div>
    <div class="subTable">
	<table>
	  <h2>Print History</h2>
	  <thead>
		<tr>
		  <th class="center">Printed By</th>
		  <th class="center">Date</th>
		</tr>
	  </thead>
	  <tbody class="editable">
	  <g:each in="${directPaymentInstance?.printLogs?.sort{it?.date}}" var="i" status="colors">
		<tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
		  <td>${i.user.userRealName}</td>
		  <td><g:formatDate date="${i?.date}" format="MMM. dd, yyyy - hh:mm a" /></td>
		</tr>
	  </g:each>
	  	 <tfoot class="total">
          <tr>
             <td>Print Count</td>
			 <td>${directPaymentInstance.printLogs.size()}</td>
          </tr>
        </tfoot>
	  </tbody>
	</table>
  </div>
    <div class="buttons">
      
      	<g:hiddenField name="counter" id="counter" value="0" />
        <g:hiddenField name="id" id="id" value="${directPaymentInstance?.id}" />
        <g:if test="${directPaymentInstance?.isUnapproved()}">
            <span class="button"><g:actionSubmit class="cancel" action="cancel" value="Cancel" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
	        <span class="button"><g:actionSubmit class="edit" action="edit" value="Edit Direct Payment" /></span>
        	<span class="button"><g:actionSubmit class="edit" action="changeValues" value="Edit Transactions"/></span>
        	<g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
            	<span class="button"><g:actionSubmit class="approve" action="approve" value="Approve" id="approve" onclick="return confirm('${message(code: 'default.button.delete.approve.message', default: 'Are you sure?')}');" /></span>
          	</g:ifAnyGranted>
        </g:if>
        <g:if test="${directPaymentInstance?.isApproved()}">
          <g:if test="${checker}">
            <span class="button"><g:actionSubmit action="unapprove" class="unapprove" value="Unapprove" id="unapprove" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
          </g:if>
        </g:if>
        <span class="button"><g:actionSubmit class="print" action="doPrint" value="Print" /></span>
        <span class="button"><g:link class="print" controller="print" action="directPaymentAccount" id="${directPaymentInstance?.id}">Print (Back of Check)</g:link></span>
      </g:form>
    </div>
  </div>
</body>
</html>
