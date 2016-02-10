
<%@ page import="com.munix.DirectPaymentInvoice" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${directPaymentInvoiceInstance}">
      <div class="errors">
        <g:renderErrors bean="${directPaymentInvoiceInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${directPaymentInvoiceInstance?.id}" />
      <g:hiddenField name="version" value="${directPaymentInvoiceInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="directPayment"><g:message code="directPaymentInvoice.directPayment.label" default="Direct Payment" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directPaymentInvoiceInstance, field: 'directPayment', 'errors')}">
          <g:link controller="directPayment" action="show" id="${directPaymentInvoiceInstance?.directPayment?.id}">${directPaymentInvoiceInstance?.directPayment}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="directPaymentInvoice.invoice.label" default="Invoice" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentInvoiceInstance, field: 'invoice', 'errors')}">
          <g:if test="${directPaymentInvoiceInstance?.type == 'Delivery'}">
            <g:link controller="salesDelivery" id="${directPaymentInvoiceInstance?.item?.id}" action="show">${directPaymentInvoiceInstance?.item}</g:link>
          </g:if>
          <g:else>
            <g:link controller="customerCharge" id="${directPaymentInvoiceInstance?.item?.id}" action="show">${directPaymentInvoiceInstance?.item}</g:link>
          </g:else>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="directPaymentInvoice.type.label" default="Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentInvoiceInstance, field: 'type', 'errors')}">
${directPaymentInvoiceInstance?.type}
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="amount"><g:message code="directPaymentInvoice.amount.label" default="Amount" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentInvoiceInstance, field: 'amount', 'errors')}">
          <g:textField name="amount" value="${fieldValue(bean: directPaymentInvoiceInstance, field: 'amount')}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
