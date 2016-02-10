<%@ page import="com.munix.PurchaseInvoice" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  	<calendar:resources lang="en" theme="aqua"/>

	<g:javascript src="generalmethods.js" />
	<g:javascript>
	$(document).ready(function(){
		$("#exchangeRate").ForceNumericOnly(true)
	})
	</g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${purchaseInvoiceInstance}">
      <div class="errors">
        <g:renderErrors bean="${purchaseInvoiceInstance}" as="list" />
      </div>
    </g:hasErrors>
    
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="supplier"><g:message code="purchaseInvoice.supplier.label" default="Supplier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoiceInstance, field: 'supplier', 'errors')}">
                ${purchaseInvoiceInstance.supplier}
                <input type='hidden' name="supplier.id" id="supplierId" value="${purchaseInvoiceInstance.supplier.id}" />
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="reference"><g:message code="purchaseInvoice.reference.label" default="*Reference" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoiceInstance, field: 'reference', 'errors')}">
                <g:textField name="reference" id="reference" maxlength="255" value="${purchaseInvoiceInstance?.reference}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="supplierReference"><g:message code="purchaseInvoice.supplierReference.label" default="Supplier Reference" /></label>
              </td>
              <td valign="top" class="value">
                <g:textField name="supplierReference" maxlength="255" id="supplierReference" value="${purchaseInvoiceInstance?.supplierReference}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="invoiceDate"><g:message code="purchaseInvoice.invoiceDate.label" default="Invoice Date" /></label>
              </td>
              <td valign="top" class="value">
                <calendar:datePicker name="invoiceDate" years="2009,2030" value="${purchaseInvoiceInstance?.invoiceDate}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="exchangeRate"><g:message code="purchaseInvoice.exchangeRate.label" default="Exchange Rate" /></label>
              </td>
              <td valign="top" class="value">
                <g:textField name="exchangeRate" maxlength="16" id="exchangeRate" value="${purchaseInvoiceInstance?.exchangeRate}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="deliveryDate"><g:message code="purchaseInvoice.deliveryDate.label" default="*Delivery Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoiceInstance, field: 'deliveryDate', 'errors')}">
                <calendar:datePicker name="deliveryDate" years="2009,2030" value="${purchaseInvoiceInstance?.deliveryDate}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="warehouse"><g:message code="purchaseInvoice.warehouse.label" default="*Warehouse" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoiceInstance, field: 'warehouse', 'errors')}">
                <g:select name="warehouse.id" from="${warehouses}" optionValue="identifier" optionKey="id" value="${purchaseInvoiceInstance?.warehouse?.id}" noSelection="['':'select one..']"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="type"><g:message code="purchaseInvoice.type.label" default="*Type" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoiceInstance, field: 'type', 'errors')}">
                ${purchaseInvoiceInstance.supplier.getLocality()}
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="remark"><g:message code="purchaseInvoice.remark.label" default="Remarks" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoice, field: 'remark', 'errors')}">
                <g:textArea name="remark" maxlength="255" value="${purchaseInvoiceInstance?.remark}" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
          
      <g:render template="items" model="['purchaseInvoiceInstance': purchaseInvoiceInstance]" />
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" onclick="return confirm('Are You Sure?')" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
