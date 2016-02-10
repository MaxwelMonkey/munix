<%@ page import="com.munix.LaborCost" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'laborCost.label', default: 'Labor Cost')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
	<g:javascript src="generalmethods.js" />
	<g:javascript>
	$(document).ready(function(){
		$("#amount").ForceNumericOnly(true)
	})
	</g:javascript>
</head>
<body>
<g:set value="${com.munix.Product.get(params?.id)}" var="productInstance" />

<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${laborCostInstance}">
    <div class="errors">
      <g:renderErrors bean="${laborCostInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="product"><g:message code="laborCost.product.label" default="Product" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: laborCostInstance, field: 'product', 'errors')}">
				<g:link action="show" controller="product" id="${productInstance?.id}">${productInstance?.identifier}</g:link>
       		</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="laborCost.type.label" default="Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: laborCostInstance, field: 'type', 'errors')}">
              <g:textField name="type" maxlength="255" value="${laborCostInstance.type}" />
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
      <input type="hidden" name="id" value="${productInstance?.id}"/>
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
