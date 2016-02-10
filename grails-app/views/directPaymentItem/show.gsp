
<%@ page import="com.munix.DirectPaymentItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directPaymentItem.label', default: 'DirectPaymentItem')}" />
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
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="directPaymentItem.id.label" default="Id" /></td>

        <td valign="top" class="value">${fieldValue(bean: directPaymentItemInstance, field: "id")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPaymentItem.date.label" default="Date" /></td>

        <td valign="top" class="value"><g:formatDate date="${directPaymentItemInstance?.date}" /></td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPaymentItem.paymentType.label" default="Payment Type" /></td>

        <td valign="top" class="value"><g:link controller="paymentType" action="show" id="${directPaymentItemInstance?.paymentType?.id}">${directPaymentItemInstance?.paymentType?.encodeAsHTML()}</g:link></td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPaymentItem.amount.label" default="Amount" /></td>

        <td valign="top" class="value">${fieldValue(bean: directPaymentItemInstance, field: "amount")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPaymentItem.type.label" default="Type" /></td>

        <td valign="top" class="value">${fieldValue(bean: directPaymentItemInstance, field: "type")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directPaymentItem.directPayment.label" default="Direct Payment" /></td>

        <td valign="top" class="value"><g:link controller="directPayment" action="show" id="${directPaymentItemInstance?.directPayment?.id}">${directPaymentItemInstance?.directPayment?.encodeAsHTML()}</g:link></td>

        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${directPaymentItemInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
