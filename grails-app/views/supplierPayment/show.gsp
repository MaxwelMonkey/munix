
<%@ page import="com.munix.SupplierPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplierPayment.label', default: 'SupplierPayment')}" />
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
            <td valign="top" class="name"><g:message code="supplierPayment.id.label" default="Id" /></td>
        <td valign="top" class="value">${supplierPaymentInstance}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplierPayment.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${supplierPaymentInstance?.date}" format="MM/dd/yyyy" /></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplierPayment.supplier.label" default="Supplier" /></td>
        <td valign="top" class="value"><g:link elementId="supplierShowLink" controller="supplier" action="show" id="${supplierPaymentInstance?.supplier?.id}">${supplierPaymentInstance?.supplier?.encodeAsHTML()}</g:link></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplierPayment.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierPaymentInstance, field: "preparedBy")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplierPayment.approvedBy.label" default="Approved By" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierPaymentInstance, field: "approvedBy")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplierPayment.cancelledBy.label" default="Cancelled By" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierPaymentInstance, field: "cancelledBy")}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        </tr>
        
        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplierPayment.remark.label" default="Remarks" /></td>
          <td valign="top" class="value">${fieldValue(bean: supplierPaymentInstance, field: "remark")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="supplierPayment.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: supplierPaymentInstance, field: "status")}</td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Purchase Invoices</h2>
        <thead>
          <tr>
            <th>Reference</th>
            <th>Supplier Reference</th>
            <th>Supplier</th>
            <th class="center">Invoice Date</th>
            <th>Type</th>
            <th class="right">Amount</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${supplierPaymentInstance.purchaseInvoices.sort{it.id}}" var="invoice" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}purchaseInvoice/show/${invoice.id}'">
            <td>${invoice?.reference}</td>
            <td>${invoice?.supplierReference}</td>
            <td>${invoice?.supplier}</td>
            <td><g:formatDate date="${invoice?.invoiceDate}" format="MM/dd/yyyy" /></td>
          <td>${invoice?.type}</td>
          <td class="right">${invoice?.formatPurchaseInvoiceDiscountedForeignTotal()}</td>
          </tr>
        </g:each>
        </tbody>

        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${supplierPaymentInstance?.formatInvoiceTotal()}</strong></td>
          </tr>
        </tfoot>

      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Payment Method</h2>
        <g:if test="${supplierPaymentInstance?.isUnapproved()}">
                  <g:link class="addItem" controller="supplierPaymentItem" action="create" id="${supplierPaymentInstance?.id}">Add Item</g:link>
        </g:if>
        <thead>
          <tr>
            <th>Payment Type</th> 
            <th>Check Number</th>
            <th>Bank</th>
            <th>Check Type</th>
            <th class="center">Date</th>
            <th>Remarks</th>
            <th class="right">Amount</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${supplierPaymentInstance.items.sort{it.id}}" var="i" status="colors">
          <g:if test="${supplierPaymentInstance.isApproved()}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          </g:if>
          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}supplierPaymentItem/edit/${i.id}'">
          </g:else>
            <td>${i?.type}</td>
            <td>${i?.checkNumber}</td>
            <td>${i?.formatCheckBankAndBranch()}</td>
            <td>${i?.checkType?.description}</td>
            <td class="center"><g:formatDate date="${i?.date}" format="MM/dd/yyyy" /></td>
            <td>${i?.remark}</td>
            <td class="right">${i?.formatAmount()}</td>
          </tr>
        </g:each>
        </tbody>

        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${supplierPaymentInstance?.formatPaymentTotal()}</strong></td>
          </tr>
          
        </tfoot>
      </table>
    </div>

	<div class="buttons">
    	<strong>Balance: PHP ${supplierPaymentInstance?.formatRemainingBalance()}</strong>
    </div>

	<g:if test="${!supplierPaymentInstance?.isCancelled()}">
    	<div class="buttons">
      		<g:form>
        		<g:hiddenField name="id" value="${supplierPaymentInstance?.id}" />
        		<g:if test="${supplierPaymentInstance?.isUnapproved()}">
        			<g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
                        <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
                    </g:ifAnyGranted>
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        			<g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
        				<span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
        			</g:ifAnyGranted>
        		</g:if>
        		
        		<g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
        			<g:if test="${supplierPaymentInstance?.isApproved()}">
        				<span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.approve.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
        			</g:if>
        		</g:ifAnyGranted>
        		<span class="button"><g:link class="print" controller="print" action="supplierPayment" target="supplierPayment" id="${supplierPaymentInstance?.id}">Print</g:link></span>
      		</g:form>
    	</div>
    </g:if>
    <g:else>
    	<div class="buttons">
    		<g:form>
        		<g:hiddenField name="id" value="${supplierPaymentInstance?.id}" />
        		<span class="button"><g:link class="print" controller="print" action="supplierPayment"  target="supplierPayment" id="${supplierPaymentInstance?.id}">Print</g:link></span>
        	</g:form>
    	</div>
    </g:else>
  </div>
</body>
</html>
