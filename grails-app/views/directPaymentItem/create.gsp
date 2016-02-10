
<%@ page import="com.munix.DirectPaymentItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directPaymentItem.label', default: 'DirectPaymentItem')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
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
    <g:hasErrors bean="${directPaymentItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${directPaymentItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="directPayment"><g:message code="directPaymentItem.directPayment.label" default="Direct Payment" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'directPayment', 'errors')}">
          <g:link action="show" controller="directPayment" id="${params.id}">${com.munix.DirectPayment.get(params.id)}</g:link>
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

          <tr class="prop">
            <td valign="top" class="name">
              <label for="checkNumber"><g:message code="directPaymentItem.checkNumber.label" default="Check Number" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'checkNumber', 'errors')}">
          <g:textField name="checkNumber" value="" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="checkDate"><g:message code="directPaymentItem.checkDate.label" default="Check Date" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'checkDate', 'errors')}">
          <g:datePicker name="checkDate" precision="day" value="" noSelection="['': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="checkBank"><g:message code="directPaymentItem.checkBank.label" default="Check Bank" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'checkBank', 'errors')}">
          <g:select name="checkBank.id" from="${com.munix.Bank.list().sort{it.toString()}}" optionKey="id" value="" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="RT"><g:message code="directPaymentItem.checkType.label" default="RT" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentItemInstance, field: 'checkType', 'errors')}">
          <g:select name="checkType.id" from="${com.munix.CheckType.list().sort{it.toString()}}" optionKey="id" value="" noSelection="['null': '']" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <g:hiddenField name="id" value="${params?.id}" />
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
