
<%@ page import="com.munix.SupplierItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplierItem.label', default: 'SupplierItem')}" />
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
    <g:hasErrors bean="${supplierItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${supplierItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${supplierItemInstance?.id}" />
      <g:hiddenField name="version" value="${supplierItemInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>


            <tr class="prop">
              <td valign="top" class="name">
                <label for="supplier"><g:message code="supplierItem.supplier.label" default="Supplier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'supplier', 'errors')}">
<g:link controller="supplier" action="show" id="${supplierItemInstance?.supplier?.id}">${supplierItemInstance?.supplier}</g:link>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="product"><g:message code="supplierItem.product.label" default="Product" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'product', 'errors')}">
${supplierItemInstance?.product?.identifier}
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="productCode"><g:message code="supplierItem.productCode.label" default="Product Code" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'productCode', 'errors')}">
          <g:textField name="productCode" value="${supplierItemInstance?.productCode}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="cost"><g:message code="supplierItem.price.label" default="Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'cost', 'errors')}">
          <g:textField name="cost" value="${fieldValue(bean: supplierItemInstance, field: 'cost')}" />
          </td>
          </tr>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="cost"><g:message code="supplierItem.status.label" default="Status" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'cost', 'errors')}">
            <g:select from="${com.munix.SupplierItem.Status}" name="status" optionValue="description" value="${supplierItemInstance.status}"/>
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
