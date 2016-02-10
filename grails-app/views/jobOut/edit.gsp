<%@ page import="com.munix.JobOut" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOut.label', default: 'jobOut')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <g:javascript src="generalmethods.js" />
  <g:javascript src="jobOutEdit.js" />
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${jobOutInstance}">
      <div class="errors">
        <g:renderErrors bean="${jobOutInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${jobOutInstance?.id}" />
      <g:hiddenField name="version" value="${jobOutInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

		  <tr class="prop">
            <td valign="top" class="name">
              <label for="id"><g:message code="jobOut.identifier.label" default="ID" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: jobOutInstance, field: 'id', 'errors')}">
              <g:link action="show" id="${jobOutInstance?.id}">${jobOutInstance}</g:link>
            </td>
          </tr>
          
		  <tr class="prop">
            <td valign="top" class="name">
              <label for="jobOrder"><g:message code="jobOut.jobOrder.label" default="Job Order" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: jobOutInstance, field: 'jobOrder', 'errors')}">
              <g:link action="show" controller="jobOrder" params="[jobOrderId: jobOutInstance?.jobOrder?.id]">${jobOutInstance?.jobOrder}</g:link>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="jobOut"><g:message code="jobOut.warehouse.label" default="Warehouse" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: jobOutInstance, field: 'warehouse', 'errors')}">
              <g:select name="warehouse.id" from="${com.munix.Warehouse.list().sort{it.toString()}}" optionKey="id" value="${jobOutInstance?.warehouse?.id}" />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="qty"><g:message code="jobOut.qty.label" default="Quantity" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: jobOutInstance, field: 'qty', 'errors')}">
              <g:textField id="qty" name="qty" value="${fieldValue(bean: jobOutInstance, field: 'qty')}" />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="laborCost"><g:message code="jobOut.laborCost.label" default="Labor Cost" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: jobOutInstance, field: 'laborCost', 'errors')}">
              <g:select name="laborCost.id" noSelection="${[null:'select one...']}" from="${jobOutInstance?.jobOrder?.product?.laborCosts}" optionKey="id" value="${jobOutInstance?.laborCost?.id}" />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              Remaining
            </td>
            <td valign="top" class="value">
              <g:hiddenField name="remaining" value="${jobOutInstance?.jobOrder?.computeRemainingBalance() + jobOutInstance?.qty}" /> 
		      <span id="remainingText"></span>
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
