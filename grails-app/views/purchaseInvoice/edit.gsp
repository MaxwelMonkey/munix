
<%@ page import="com.munix.PurchaseInvoice" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
	<link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
	<calendar:resources lang="en" theme="aqua"/>
	<g:javascript src="generalmethods.js" />
	<g:javascript>
	$(document).ready(function(){
		$("#exchangeRate").ForceNumericOnly(true)
		$("#discountRate").ForceNumericOnly(true)
	})
	</g:javascript>
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
    <g:if test="${purchaseInvoiceInstance?.exchangeRate <= 0}">
      <div class="message">Invoice cannot be approved while exchange rate is not entered!</div>
    </g:if>
    <g:hasErrors bean="${purchaseInvoiceInstance}">
      <div class="errors">
        <g:renderErrors bean="${purchaseInvoiceInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="update" method="post">
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name"><g:message code="purchaseInvoice.supplier.label" default="Supplier" /></td>
              <td valign="top" class="value">
                <g:link controller="supplier" action="show" id="${purchaseInvoiceInstance?.supplier?.id}">${purchaseInvoiceInstance?.supplier}</g:link>
                <input type='hidden' name="supplier.id" id="supplierId" value="${purchaseInvoiceInstance.supplier.id}" />
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name"><g:message code="purchaseInvoice.type.label" default="Type" /></td>
              <td valign="top" class="value">${purchaseInvoiceInstance?.type}</td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name"><g:message code="purchaseInvoice.reference.label" default="Reference" /></td>
              <td valign="top" class="value"><g:textField name="reference" id="reference" maxlength="255" value="${purchaseInvoiceInstance?.reference}"/></td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name"><g:message code="purchaseInvoice.supplierReference.label" default="Supplier Reference" /></td>
              <td valign="top" class="value"><g:textField name="supplierReference" maxlength="255" id="supplierReference" value="${purchaseInvoiceInstance?.supplierReference}"/></td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name"><g:message code="purchaseInvoice.warehouse.label" default="Warehouse" /></td>
              <td valign="top" class="value">
                <g:select name="warehouse.id" from="${warehouses}" optionValue="identifier" optionKey="id" value="${purchaseInvoiceInstance?.warehouse?.id}" noSelection="['null':'select one..']"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">Invoice Date</td>
              <td valign="top" class="value">
                <calendar:datePicker name="invoiceDate" years="2009,2030" value="${purchaseInvoiceInstance?.invoiceDate}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <g:message code="purhcaseInvoice.deliveryDate.label" default="Delivery Date" />
              </td>
              <td valign="top" class="value">
                <calendar:datePicker name="deliveryDate" years="2009,2030" value="${purchaseInvoiceInstance?.deliveryDate}"/>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <g:message code="purchaseInvoice.remark.label" default="Remarks" />
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoice, field: 'remark', 'errors')}">
                <g:textArea name="remark" maxlength="255" value="${purchaseInvoiceInstance?.remark}" />
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="exchangeRate"><g:message code="purhcaseInvoice.exchangeRate.label" default="Exchange Rate" /></label>
              </td>
              <td valign="top" class="value">
                <g:textField name="exchangeRate" maxlength="16" value="${purchaseInvoiceInstance?.exchangeRate}" />
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="discountRate"><g:message code="purhcaseInvoice.discountRate.label" default="Discount Rate" /></label>
              </td>
              <td valign="top" class="value">
                <g:textField name="discountRate" maxlength="5" value="${purchaseInvoiceInstance?.discountRate}" />%
              </td>
            </tr>
          </tbody>
        </table>

        <g:render template="items" model="['purchaseInvoiceInstance': purchaseInvoiceInstance]" />
        <div class="buttons">
          <g:hiddenField name="id" value="${purchaseInvoiceInstance?.id}" />
          <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="return confirm('${message(code: 'default.button.update.confirm.message', default: 'Are you sure?')}');"/></span>
       </div>
     </div>
   </g:form>
  </div>
</body>
</html>
