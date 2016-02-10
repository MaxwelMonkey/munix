
<%@ page import="com.munix.CreditMemo" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'Credit Memo')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="['Credit Memo']" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="['Credit Memo']" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${creditMemoInstance}">
      <div class="errors">
        <g:renderErrors bean="${creditMemoInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${creditMemoInstance?.id}" />
      <g:hiddenField name="version" value="${creditMemoInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="creditMemo.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: creditMemoInstance, field: 'customer', 'errors')}">
                ${creditMemoInstance?.customer}
              </td>
            </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="discountType"><g:message code="creditMemo.discountType.label" default="Discount Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoInstance, field: 'discountType', 'errors')}">
            ${creditMemoInstance?.discountType}
          </td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name">
              <label for="reason"><g:message code="creditMemo.reason.label" default="Reason" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoInstance, field: 'reason', 'errors')}">
          	  <g:select name="reason.id" from="${com.munix.Reason.list()}" optionKey="id" value="${creditMemoInstance?.reason?.id}"  />
          	</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="warehouse"><g:message code="creditMemo.warehouse.label" default="Warehouse" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoInstance, field: 'warehouse', 'errors')}">
              <g:select name="warehouse.id" from="${com.munix.Warehouse.list()}" optionKey="id" value="${creditMemoInstance?.warehouse?.id}"  noSelection="['':'select one..']"/>
          	</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="creditMemo.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoInstance, field: 'remark', 'errors')}">
          <g:textArea name="remark" value="${creditMemoInstance?.remark}" maxlength="255"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="additionalDiscount"><g:message code="creditMemo.additionalDiscount.label" default="Add'l Discount (%)" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoInstance, field: 'additionalDiscount', 'errors')}">
          		<g:textField name="additionalDiscount" value="${creditMemoInstance?.additionalDiscount}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="additionalDiscount"><g:message code="creditMemo.applyFourMonthRule.label" default="Apply Four-month Rule?" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoInstance, field: 'applyFourMonthRule', 'errors')}">
				<g:checkBox name="applyFourMonthRule" value="${creditMemoInstance.applyFourMonthRule}"/>
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
