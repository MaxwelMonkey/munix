
<%@ page import="com.munix.SalesOrderItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'SalesOrderItem.label', default: 'SalesOrderItem')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
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
    <g:hasErrors bean="${salesOrderItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${salesOrderItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${salesOrderItemInstance?.id}" />
      <g:hiddenField name="version" value="${salesOrderItemInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="invoice"><g:message code="SalesOrderItem.invoice.label" default="Invoice" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'invoice', 'errors')}">
          <g:link action="show" controller="salesInvoice" id="${salesOrderItemInstance?.invoice?.id}">${salesOrderItemInstance?.invoice} </g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="product"><g:message code="SalesOrderItem.product.label" default="Product" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'product', 'errors')}">
               <!--  <g:select name="product.id" from="${com.munix.Product.list()}" optionKey="id" value="${salesOrderItemInstance?.product?.id}"  />-->
${salesOrderItemInstance?.product}
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="finalPrice"><g:message code="SalesOrderItem.finalPrice.label" default="Final Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'finalPrice', 'errors')}">
          <g:textField name="finalPrice" value="${fieldValue(bean: salesOrderItemInstance, field: 'finalPrice')}" />
          </td>
          </tr>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="qty"><g:message code="SalesOrderItem.qty.label" default="Qty" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'qty', 'errors')}">
          <g:textField name="qty" value="${fieldValue(bean: salesOrderItemInstance, field: 'qty')}" />
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