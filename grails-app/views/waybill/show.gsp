
<%@ page import="com.munix.Waybill" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'waybill.label', default: 'Waybill')}" />
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
            <td valign="top" class="name"><g:message code="waybill.id.label" default="Id" /></td>
        <td valign="top" class="value">${waybillInstance}</td>
        <td valign="top" class="name"><g:message code="waybill.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${waybillInstance?.date}"  format="MM/dd/yyyy"/></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="waybill.tripTicket.label" default="Trip Ticket" /></td>
        <td valign="top" class="value"><g:link controller="tripTicket" action="show" id="${waybillInstance?.tripTicket?.tripTicket?.id}">${waybillInstance?.tripTicket?.encodeAsHTML()}</g:link></td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr  class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="waybill.customer.label" default="Customer" /></td>
        <td valign="top" class="value"><g:link controller="customer" action="show" id="${waybillInstance?.customer?.id}">${waybillInstance?.customer?.encodeAsHTML()}</g:link></td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="waybill.forwarder.label" default="Forwarder" /></td>
        <td valign="top" class="value"><g:link controller="forwarder" action="show" id="${waybillInstance?.forwarder?.id}">${waybillInstance?.forwarder?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="waybill.declaredValue.label" default="Declared Value" /></td>
        <td valign="top" class="value">${waybillInstance?.formatDeclaredValue()}</td>
        </tr>

        <tr  class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="waybill.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${waybillInstance?.preparedBy?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="waybill.cancelledBy.label" default="Cancelled By" /></td>
        <td valign="top" class="value">${waybillInstance?.cancelledBy?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="waybill.status.label" default="Status" /></td>
        <td valign="top" class="value">${waybillInstance?.status?.encodeAsHTML()}</td>
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
        <g:if test="${waybillInstance?.status != 'Complete'}">
          <th></th>
        </g:if>
        </thead>
        <tbody class="editable">
        <g:each in="${waybillInstance.deliveries.sort{it.id}}" var="delivery" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}salesDelivery/show/${delivery?.id}'">
            <td>${delivery}</td>
            <td><g:formatDate date="${delivery?.date}" format="MM/dd/yyyy"/></td>
          <td>${delivery?.status}</td>
          <td class="right">PHP <g:formatNumber number="${delivery?.computeWaybillDeclaredValue()}" format="###,##0.00" /></td>
          <g:if test="${waybillInstance?.status != 'Complete'}">
            <td class="center"><g:link action="deliveryRemove"
                                       params="${['waybillId':waybillInstance?.id]}" id="${delivery?.id}"
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
            <td class="right"><strong>${waybillInstance?.formatDeclaredValueTotal()}</strong></td>
        <g:if test="${waybillInstance?.status != 'Complete'}">
          <td></td>
        </g:if>
        </tr>
        </tfoot>
      </table>

    </div>


    <div class="subTable">
      <table>
        <h2>Package Details</h2>
        <g:if test="${waybillInstance?.status != 'Complete'}">
          <g:link class="addItem" action="create" controller="waybillPackage" id="${waybillInstance.id}">Add Item</g:link>
        </g:if>
        <thead>

        <th>Packaging</th>
        <th>Description</th>
        <th class="right">Qty</th>
        <g:if test="${waybillInstance?.status != 'Complete'}">
          <th></th>
        </g:if>

        </thead>
        <g:if test="${waybillInstance?.status != 'Complete'}">
          <tbody class="editable">
        </g:if>
        <g:else>
          <tbody class="uneditable">
        </g:else>
        <g:each in="${waybillInstance.packages.sort{it.id}}" var="i" status="colors">
          <g:if test="${waybillInstance?.status != 'Complete'}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}waybillPackage/edit/${i?.id}'">
          </g:if>
          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          </g:else>

          <td>${i?.packaging}</td>
          <td>${i?.description}</td>
          <td class="right">${i?.formatQty()}</td>

		  <g:if test="${waybillInstance?.status != 'Complete'}">
            <td class="center"><g:link action="waybillPackageRemove"
                                       params="${['waybillId':waybillInstance?.id]}" id="${i?.id}"
                                       onClick="return confirm('Are you sure?');">X</g:link></td>
          </g:if>
          </tr>
        </g:each>
        <tbody>
      </table>

    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${waybillInstance?.id}" />
        <g:if test="${waybillInstance?.isProcessing()}">
          <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        </g:if>
        <span class="button"><g:actionSubmit class="print" action="print" value="${message(code: 'default.button.print.label', default: 'Print')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
