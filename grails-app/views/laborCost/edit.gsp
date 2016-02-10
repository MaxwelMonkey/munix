<%@ page import="com.munix.LaborCost" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'laborCost.label', default: 'Labor Cost')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
	<g:javascript src="generalmethods.js" />
	<g:javascript>
	$(document).ready(function(){
		$("#amount").ForceNumericOnly(true)
	})
	</g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${laborCostInstance}">
      <div class="errors">
        <g:renderErrors bean="${laborCostInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${laborCostInstance?.id}" />
      <g:hiddenField name="version" value="${laborCostInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="product"><g:message code="laborCost.product.label" default="Product" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'po', 'errors')}">
                <g:link action="show" controller="product" id="${laborCostInstance?.product?.id}">${laborCostInstance?.product?.identifier}</g:link>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="type"><g:message code="laborCost.type.label" default="Type" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: laborCostInstance, field: 'type', 'errors')}">
                <g:textField name="type" maxlength="255" value="${fieldValue(bean: laborCostInstance, field: 'type')}" />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="amount"><g:message code="laborCost.amount.label" default="Amount" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: laborCostInstance, field: 'amount', 'errors')}">
                <g:textField name="amount" id="amount" maxlength="16" value="${laborCostInstance.amount}" />
              </td>
            </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
