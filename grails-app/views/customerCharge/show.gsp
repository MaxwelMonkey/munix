
<%@ page import="com.munix.CustomerCharge" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'customerCharge.label', default: 'CustomerCharge')}" />
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
            <td valign="top" class="name"><g:message code="customerCharge.id.label" default="Id" /></td>
            <td valign="top" class="value">${customerChargeInstance}</td>
            <td valign="top" class="name"><g:message code="customerCharge.date.label" default="Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${customerChargeInstance?.date}" format="MM/dd/yyyy" /></td>
          </tr>

          <tr class="prop">
            <td class="name"></td>
            <td class="value"></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
       	    <td valign="top" class="name"><g:message code="customerCharge.customer.label" default="Customer" /></td>
            <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${customerChargeInstance?.customer?.id}">${customerChargeInstance?.customer?.encodeAsHTML()}</g:link></td>
            <td valign="top" class="name"><g:message code="customerCharge.preparedBy.label" default="Prepared By" /></td>
	        <td valign="top" class="value"> ${customerChargeInstance?.preparedBy?.encodeAsHTML()} </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="customerCharge.counterReceipt.label" default="Counter Receipt" /></td>
            <td>
              <g:each in="${customerChargeInstance?.counterReceipts.sort{it.id}}" var="receipt">
                <g:link controller="counterReceipt" action="show" id="${receipt.id}">${receipt.encodeAsHTML()}</g:link>
                <br/>
              </g:each>
            </td>
            <td valign="top" class="name"><g:message code="customerCharge.approvedBy.label" default="Approved By" /></td>
	        <td valign="top" class="value"> ${customerChargeInstance?.approvedBy?.encodeAsHTML()} </td>
          </tr>
        
          <tr class="prop">
            <td valign="top" class="name"><g:message code="customerCharge.directPaymentInvoice.label" default="Direct Payment" /></td>
            <td>
              <g:each in="${customerChargeInstance?.invoices}" var="invoice">
                <g:link controller="directPayment" action="show" id="${invoice.directPayment.id}">${invoice.directPayment.encodeAsHTML()}</g:link>
                <br/>
              </g:each>
            </td>
            <td valign="top" class="name"><g:message code="customerCharge.cancelledBy.label" default="Cancelled By" /></td>
	        <td valign="top" class="value"> ${customerChargeInstance?.cancelledBy?.encodeAsHTML()} </td>
          </tr>
        
          <tr class="prop">
            <td class="name"></td>
            <td class="value"></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="customerCharge.status.label" default="Status" /></td>
            <td valign="top" class="value">${fieldValue(bean: customerChargeInstance, field: "status")}</td>
            <td valign="top" class="name"><g:message code="customerCharge.remark.label" default="Remarks" /></td>
            <td valign="top" class="value">${fieldValue(bean: customerChargeInstance, field: "remark")}</td>
          </tr>

        </tbody>
      </table>
    </div>
    <div class="subTable">
      <h2>Items</h2>
      <table>
        <g:if test="${customerChargeInstance?.status == 'Unapproved'}">
          <g:link elementId="addCustomerChargeItem" class="addItem" controller="customerChargeItem" action="create" id="${customerChargeInstance?.id}">Add Item</g:link>
        </g:if>
        <thead>
          <tr>
            <th>Date</th>
            <th>Charge</th>
            <th>Reference</th>
            <th>Remarks</th>
            <th class="right">Amount</th>
          </tr>
        </thead>
        <g:if test="${customerChargeInstance?.status == 'Unapproved'}">
          <tbody class="editable">
        </g:if>
        <g:else>
          <tbody class="uneditable">
        </g:else>
        <g:each in="${customerChargeInstance.items.sort{it.id}}" var="item" status="colors">
          <g:if test="${customerChargeInstance?.status == 'Unapproved'}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}customerChargeItem/edit/${item?.id}'">
          </g:if>
          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          </g:else>
            <td><g:formatDate date="${item?.date}" format="MM/dd/yyyy" /></td>
            <td>${item?.charge}</td>
            <td>${item?.reference}</td>
            <td>${item?.remark}</td>
            <td class="right">PHP <g:formatNumber number="${item?.amount}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        </tbody>
        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <g:formatNumber number="${customerChargeInstance?.computeTotalAmount()}" format="###,##0.00" /></strong></td>
          </tr>
        </tfoot>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Counter Receipts</h2>
        <thead>
          <tr>
            <th>Reference #</th>
            <th>Date</th>
            <th>Amount</th>
            <th class="right">Amount Due</th>
        </tr>
        </thead>
        <tbody class="editable">
          <g:if test="${customerChargeInstance?.counterReceipts}">
            <g:each in="${customerChargeInstance?.counterReceipts.sort{it.id}}" var="receipt">
              <tr onclick="window.location='${createLink(uri:'/')}counterReceipt/show/${receipt.id}'">
                <td>${receipt}</td>
                <td><g:formatDate date="${receipt.date}" format="MM/dd/yyyy" /></td>
                <td>PHP <g:formatNumber number="${receipt.computeTotal()}" format="###,##0.00" /></td>
                <td class="right">PHP <g:formatNumber number="${receipt.computeAmountDueTotal()}" format="###,##0.00" /></td>
              </tr>
            </g:each>
            <tfoot class="total">
              <tr>
                 <td>Total</td>
                 <td></td>
                 <td>PHP <g:formatNumber number="${customerChargeInstance.computeReceiptsTotal()}" format="###,##0.00" /></td>
                 <td class="right">PHP <g:formatNumber number="${customerChargeInstance.computeReceiptsDueTotal()}" format="###,##0.00" /></td>
              </tr>
            </tfoot>

          </g:if>
        </tbody>

      </table>
    </div>
	<g:if test="${customerChargeInstance?.status != 'Cancelled'}">
    	<div class="buttons">
      		<g:form>
        		<g:hiddenField name="id" value="${customerChargeInstance?.id}" />
        		<g:if test="${customerChargeInstance?.isUnapproved()}">
          			<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
          			<span class="button"><g:actionSubmit class="cancel" action="cancel" value="Cancel" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
                    <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
          			  <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
          			</g:ifAnyGranted>  
        		</g:if>
        		<g:if test="${customerChargeInstance?.isApproved()}">
        		  <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
          			<span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.approve.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
          		  </g:ifAnyGranted>
        		</g:if>
                <span class="button"><g:link class="print" target="blank" controller="print" action="customerCharge" id="${customerChargeInstance?.id}">Print</g:link></span>
      		</g:form>
    	</div>
    </g:if>
  </div>
</body>
</html>
