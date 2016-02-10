<%@ page import="com.munix.CustomerChargeItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'customerChargeItem.label', default: 'CustomerChargeItem')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="numbervalidation.js" />
  
  <g:javascript>
  	$(document).ready(function () {
		$(".customerChargeAmount").ForceNumericOnly(true)
  	})
  </g:javascript>
  <calendar:resources lang="en" theme="aqua"/>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${customerChargeItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${customerChargeItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customerCharge"><g:message code="customerChargeItem.customerCharge.label" default="Customer Charge" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: customerChargeItemInstance, field: 'customerCharge', 'errors')}">
                <g:link controller="customerCharge" action="show" id="${params?.id}">${com.munix.CustomerCharge.get(params?.id)}</g:link>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="date"><g:message code="customerChargeItem.date.label" default="Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: customerChargeItemInstance, field: 'remark', 'errors')}">
                <calendar:datePicker name="date"  years="2009,2030" value="${customerChargeItemInstance?.date}"/>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="charge"><g:message code="customerChargeItem.charge.label" default="Charge" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: customerChargeItemInstance, field: 'charge', 'errors')}">
                <g:select name="charge.id" from="${com.munix.Charge.list()}" optionKey="id" value="${customerChargeItemInstance?.charge?.id}"  />
              </td>
            </tr>
            
            <tr class="prop">
              <td valign="top" class="name">
                <label for="reference"><g:message code="customerChargeItem.reference.label" default="Reference" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: customerChargeItemInstance, field: 'reference', 'errors')}">
                <g:textField name="reference" value="${fieldValue(bean: customerChargeItemInstance, field: 'reference')}" />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="remark"><g:message code="customerChargeItem.remark.label" default="Remarks" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: customerChargeItemInstance, field: 'remark', 'errors')}">
                <g:textArea name="remark" value="${fieldValue(bean: customerChargeItemInstance, field: 'remark')}" />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="amount"><g:message code="customerChargeItem.amount.label" default="Amount" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: customerChargeItemInstance, field: 'amount', 'errors')}">
                <g:textField name="amount" class="customerChargeAmount" value="${fieldValue(bean: customerChargeItemInstance, field: 'amount')}" />
              </td>
            </tr>
           
          </tbody>
        </table>
      </div>
      <div class="buttons">
        <input type="hidden" name="id" value="${params?.id}"/>
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
