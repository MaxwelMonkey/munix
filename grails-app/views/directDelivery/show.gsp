
<%@ page import="com.munix.DirectDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directDelivery.label', default: 'Direct Delivery')}" />
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
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="directDelivery.id.label" default="Id" /></td>
        <td valign="top" class="value">${directDeliveryInstance}</td>
        <td valign="top" class="name"><g:message code="directDelivery.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${directDeliveryInstance?.date}" format="MM/dd/yyyy" /></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directDelivery.tripTicket.label" default="Trip Ticket" /></td>
        <td valign="top" class="value"><g:link controller="tripTicket" action="show" id="${directDeliveryInstance?.tripTicket?.tripTicket?.id}">${directDeliveryInstance?.tripTicket?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="directDelivery.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${fieldValue(bean: directDeliveryInstance, field: "preparedBy")}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        <td valign="top" class="name"><g:message code="directDelivery.cancelledBy.label" default="Cancelled By" /></td>
        <td valign="top" class="value">${fieldValue(bean: directDeliveryInstance, field: "cancelledBy")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directDelivery.customer.label" default="Customer" /></td>
        <td valign="top" class="value"><g:link controller="customer" action="show" id="${directDeliveryInstance?.customer?.id}">${directDeliveryInstance?.customer?.encodeAsHTML()}</g:link></td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="directDelivery.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: directDeliveryInstance, field: "status")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Sales Delivery</h2>
        <thead>
        <th>ID</th>
        <th>Date</th>
        <th>Status</th>
        <th class="right">Amount</th>
        <g:if test="${directDeliveryInstance?.status.toString() != 'Complete'}">
          <th></th>
        </g:if>
        </thead>
        <tbody class="editable">
        <g:each in="${directDeliveryInstance.deliveries.sort{it.id}}" var="delivery" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}salesDelivery/show/${delivery?.id}'">
            <td>${delivery}</td>
            <td><g:formatDate date="${delivery?.date}" format="MM/dd/yyyy"/></td>
          <td>${delivery?.status}</td>
          <td class="right">PHP <g:formatNumber number="${delivery?.computeTotalAmount()}" format="###,##0.00" /></td>
          <g:if test="${directDeliveryInstance?.status.toString() != 'Complete'}">
            <td class="center"><g:link action="deliveryRemove"
                                       params="${['directDeliveryId':directDeliveryInstance?.id]}" id="${delivery?.id}"
                                       onClick="return confirm('Are you sure?');">X</g:link></td>
          </g:if>
          </tr>
        </g:each>
        <tbody>
        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${directDeliveryInstance?.formatTotal()}</strong></td>
        <g:if test="${directDeliveryInstance?.status.toString() != 'Complete'}">
          <td></td>
        </g:if>
        </tr>
        </tfoot>
      </table>

    </div>


    <div class="subTable">
      <table>
        <h2>Package Details</h2>
        <g:if test="${directDeliveryInstance?.status.toString() != 'Complete'}">
          <g:link class="addItem" action="create" controller="directDeliveryPackage" id="${directDeliveryInstance.id}">Add Item</g:link>
        </g:if>
        <thead>

        <th>Packaging</th>
        <th>Description</th>
        <th class="right">Qty</th>
		<g:if test="${directDeliveryInstance?.status.toString() != 'Complete'}">
          <th></th>
        </g:if>
        </thead>
        <g:if test="${directDeliveryInstance?.status?.toString() != 'Complete'}">
          <tbody class="editable">
        </g:if>
        <g:else>
          <tbody class="uneditable">
        </g:else>
        <g:each in="${directDeliveryInstance.packages.sort{it.id}}" var="i" status="colors">
          <g:if test="${directDeliveryInstance?.status != 'Complete'}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directDeliveryPackage/edit/${i?.id}'">
          </g:if>
          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          </g:else>

          <td>${i?.packaging}</td>
          <td>${i?.description}</td>
          <td class="right">${i?.formatQty()}</td>
		  <g:if test="${directDeliveryInstance?.status?.toString() != 'Complete'}">
            <td class="center"><g:link action="deliveryPackageRemove"
                                       params="${['directDeliveryId':directDeliveryInstance?.id]}" id="${i?.id}"
                                       onClick="return confirm('Are you sure?');">X</g:link></td>
          </g:if>
          </tr>
        </g:each>
        <tbody>
      </table>

    </div>
      <div class="buttons">
        <g:form>
	    <g:if test="${directDeliveryInstance?.isProcessing()}">
          <g:hiddenField name="id" value="${directDeliveryInstance?.id}" />
          <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
    	</g:if>
		  <span class="button"><g:link class="print" target="blank" controller="print" action="directDelivery" id="${directDeliveryInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
        </g:form>
      </div>
  </div>
</body>
</html>
