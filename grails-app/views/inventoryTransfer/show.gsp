
<%@ page import="com.munix.InventoryTransfer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'inventoryTransfer.label', default: 'Inventory Transfer')}" />
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
            <td valign="top" class="name"><g:message code="inventoryTransfer.id.label" default="Id" /></td>
        <td valign="top" class="value">${inventoryTransferInstance}</td>
        <td valign="top" class="name"><g:message code="inventoryTransfer.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${inventoryTransferInstance?.date}" format="MM/dd/yyyy" /></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="inventoryTransfer.originWarehouse.label" default="Origin" /></td>
        <td valign="top" class="value">${inventoryTransferInstance?.originWarehouse?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="inventoryTransfer.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${inventoryTransferInstance?.preparedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="inventoryTransfer.destinationWarehouse.label" default="Destination" /></td>
        <td valign="top" class="value">${inventoryTransferInstance?.destinationWarehouse?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="inventoryTransfer.receivedBy.label" default="Received By" /></td>
        <td valign="top" class="value">${inventoryTransferInstance?.receivedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        <td valign="top" class="name"><g:message code="inventoryTransfer.cancelledBy.label" default="Cancelled By" /></td>
        <td valign="top" class="value">${inventoryTransferInstance?.cancelledBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="inventoryTransfer.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: inventoryTransferInstance, field: "status")}</td>
        <td valign="top" class="name"><g:message code="inventoryTransfer.remark.label" default="Remarks" /></td>
        <td valign="top" class="value">${fieldValue(bean: inventoryTransferInstance, field: "remark")}</td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <thead>
          <tr>
            <th>Identifier</th>
            <th>Description</th>
            <th>Origin Warehouse Stock</th>
            <th>Destination Warehouse Stock</th>
            <th class="right">Quantity</th>
          </tr>
        </thead>
        <tbody class="uneditable">
        <g:each in="${items.sort{it.description}}" var="item" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" >
            <td>${item.identifier}</td>
            <td>${item.description}</td>
            <td>${item.originWarehouseStock}</td>
            <td>${item.destinationWarehouseStock}</td>
            <td class="right">${item.qty}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${inventoryTransferInstance?.id}" />
        <g:if test="${inventoryTransferInstance?.isUnapproved()}">
        	<span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'cancel', 'default': 'Cancel')}" onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');" /></span>
        	<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        	<span class="button"><g:actionSubmit class="approve" action="receive" value="${message(code: 'default.button.receive.label', default: 'Receive')}" onclick="return confirm('${message(code: 'default.button.receive.confirm.message', default: 'Are you sure?')}');" /></span>
        </g:if>
        <span class="button"><g:link class="print" controller="print" action="inventoryTransfer" target="inventoryTransfer" id="${inventoryTransferInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
      </g:form>
    </div>
  </div>
</body>
</html>
