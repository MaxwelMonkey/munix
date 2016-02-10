
<%@ page import="com.munix.DirectPaymentItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directPaymentItem.label', default: 'DirectPaymentItem')}" />
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
    <g:hasErrors bean="${directPaymentItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${directPaymentItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${directPaymentItemInstance?.id}" />
      <g:hiddenField name="version" value="${directPaymentItemInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="directPayment"><g:message code="directPaymentItem.directPayment.label" default="Direct Payment" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'directPayment', 'errors')}">
          <g:link action="show" controller="directPayment" id="${directPaymentItemInstance?.directPayment?.id}">${directPaymentItemInstance?.directPayment}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="date"><g:message code="directPaymentItem.date.label" default="Date" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'date', 'errors')}">
          <g:datePicker name="date" precision="day" value="${directPaymentItemInstance?.date}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="paymentType"><g:message code="directPaymentItem.paymentType.label" default="Payment Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'paymentType', 'errors')}">
          <g:select name="paymentType.id" from="${com.munix.PaymentType.list()}" optionKey="id" value="${directPaymentItemInstance?.paymentType?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="amount"><g:message code="directPaymentItem.amount.label" default="Amount" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'amount', 'errors')}">
          <g:textField name="amount" value="${fieldValue(bean: directPaymentItemInstance, field: 'amount')}" />
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
