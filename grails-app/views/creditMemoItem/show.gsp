
<%@ page import="com.munix.CreditMemoItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemoItem.label', default: 'Credit Memo Item')}" />
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
            <td valign="top" class="name"><g:message code="creditMemoItem.id.label" default="Id" /></td>
        <td valign="top" class="value">${fieldValue(bean: creditMemoItemInstance, field: "id")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="creditMemoItem.delivery.label" default="Delivery" /></td>
        <td valign="top" class="value"><g:link controller="salesDelivery" action="show" id="${creditMemoItemInstance?.deliveryItem?.delivery.id}">${creditMemoItemInstance?.deliveryItem?.delivery.encodeAsHTML()}</g:link></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="creditMemoItem.oldQty.label" default="Old Qty" /></td>
        <td valign="top" class="value">${fieldValue(bean: creditMemoItemInstance, field: "oldQty")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="creditMemoItem.oldPrice.label" default="Old Price" /></td>
        <td valign="top" class="value">${fieldValue(bean: creditMemoItemInstance, field: "oldPrice")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="creditMemoItem.newQty.label" default="New Qty" /></td>
        <td valign="top" class="value">${fieldValue(bean: creditMemoItemInstance, field: "newQty")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="creditMemoItem.newPrice.label" default="New Price" /></td>
        <td valign="top" class="value">${fieldValue(bean: creditMemoItemInstance, field: "newPrice")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="creditMemoItem.remark.label" default="Remarks" /></td>
        <td valign="top" class="value">${fieldValue(bean: creditMemoItemInstance, field: "remark")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="creditMemoItem.creditMemo.label" default="Credit Memo" /></td>
        <td valign="top" class="value"><g:link controller="creditMemo" action="show" id="${creditMemoItemInstance?.creditMemo?.id}">${creditMemoItemInstance?.creditMemo?.encodeAsHTML()}</g:link></td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${creditMemoItemInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
