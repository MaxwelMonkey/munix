
<%@ page import="com.munix.MaterialRequisition" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRequisition.label', default: 'MaterialRequisition')}" />
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
    <report:report id="jobOrder" report="jobOrder" format="PDF"> Generate Report
      <g:hiddenField name="id" value="${materialRequisitionInstance?.id}" />
    </report:report>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="materialRequisition.id.label" default="Id" /></td>
        <td valign="top" class="value">${materialRequisitionInstance}</td>
        <td valign="top" class="name"><g:message code="materialRequisition.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${materialRequisitionInstance?.date}" format="MM/dd/yyyy"/></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="materialRequisition.jobOrder.label" default="Job Order" /></td>
        <td valign="top" class="value"><g:link controller="jobOrder" action="show" id="${materialRequisitionInstance?.jobOrder?.id}">${materialRequisitionInstance?.jobOrder?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="materialRequisition.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${fieldValue(bean: materialRequisitionInstance, field: "preparedBy")}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="materialRequisition.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: materialRequisitionInstance, field: "status")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Items</h2>
        <g:link class="addItem" controller="materialRequisitionItem" action="create" id="${materialRequisitionInstance?.id}">Add Item</g:link>
        <thead>
          <tr>
            <th>Identifier</th>
            <th>Description</th>
            <th class="right">Qty</th>
            <th class="right">Qty Received</th>
            <th class="right">Remaining Balance</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${materialRequisitionInstance.items.sort{it.component?.description}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}materialRequisitionItem/edit/${i?.id}'">
            <td>${i?.component}</td>
            <td>${i?.component?.description}</td>
            <td class="right">${i?.formatQty()}</td>
            <td class="right">${i?.formatQtyReceived()}</td>
            <td class="right">${i?.formatRemainingBalance()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Releases</h2>
        <thead>
          <tr>
            <th>ID</th>
            <th class="center">Date</th>
            <th>Prepared by</th>
            <th>Approve by</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${materialRequisitionInstance.releases.sort{it.id}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}materialRelease/show/${i.id}'">
            <td>${i}</td>
            <td class="center"><g:formatDate date="${i?.date}" format="MM/dd/yyyy"/></td>
          <td>${i?.preparedBy}</td>
          <td >${i?.approvedBy}</td>
          <td>${i?.status}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <g:if test="${materialRequisitionInstance?.status != 'Complete'}">
      <div class="buttons">
        <g:form>
          <g:hiddenField name="id" value="${materialRequisitionInstance?.id}" />
          <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
          <span class="button"><g:link action="create" controller="materialRelease" id="${materialRequisitionInstance?.id}">Release</g:link></span>
          <span class="button"><g:link action="print" controller="materialRequisition" id="${materialRequisitionInstance?.id}" target="blank">Print</g:link></span>
          <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION">
            <span class="button"><g:actionSubmit class="markAsComplete" action="markAsComplete" value="${message(code: 'default.button.markAsComplete.label', default: 'Mark As Complete')}" onclick="return confirm('${message(code: 'default.button.markAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:ifAnyGranted>
        </g:form>
      </div>
    </g:if>
  </div>
</body>
</html>
