
<%@ page import="com.munix.PurchaseOrderItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem')}" />
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
    <g:hasErrors bean="${purchaseOrderItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${purchaseOrderItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${purchaseOrderItemInstance?.id}" />
      <g:hiddenField name="version" value="${purchaseOrderItemInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="po"><g:message code="purchaseOrderItem.po.label" default="Po" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'po', 'errors')}">

          <g:link action="show" controller="purchaseOrder" id="${purchaseOrderItemInstance?.po?.id}">${purchaseOrderItemInstance?.po.formatId()}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="product"><g:message code="purchaseOrderItem.product.label" default="Product" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'product', 'errors')}">
${purchaseOrderItemInstance?.product}
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="price"><g:message code="purchaseOrderItem.price.label" default="Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'price', 'errors')}">
          <g:textField name="price" readOnly="readonly" value="${fieldValue(bean: purchaseOrderItemInstance, field: 'price')}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="finalPrice"><g:message code="purchaseOrderItem.finalPrice.label" default="Final Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'finalPrice', 'errors')}">
          <g:textField name="finalPrice" value="${fieldValue(bean: purchaseOrderItemInstance, field: 'finalPrice')}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="qty"><g:message code="purchaseOrderItem.qty.label" default="Qty" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'qty', 'errors')}">
          <g:textField name="qty" value="${fieldValue(bean: purchaseOrderItemInstance, field: 'qty')}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="isComplete"><g:message code="purchaseOrderItem.isComplete.label" default="Mark as Complete" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'isComplete', 'errors')}">
          <g:checkBox name="isComplete" value="${purchaseOrderItemInstance?.isComplete}" />
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
