
<%@ page import="com.munix.SalesDeliveryItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem')}" />
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
    <g:hasErrors bean="${salesDeliveryItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${salesDeliveryItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${salesDeliveryItemInstance?.id}" />
      <g:hiddenField name="version" value="${salesDeliveryItemInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="delivery"><g:message code="salesDeliveryItem.delivery.label" default="Delivery" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: salesDeliveryItemInstance, field: 'delivery', 'errors')}">
          <g:link action="show" controller="salesDelivery" id="${salesDeliveryItemInstance?.delivery?.id}"> ${salesDeliveryItemInstance?.delivery?.toString()}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="product"><g:message code="salesDeliveryItem.product.label" default="Product" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesDeliveryItemInstance, field: 'product', 'errors')}">
${salesDeliveryItemInstance?.product?.toString()}
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