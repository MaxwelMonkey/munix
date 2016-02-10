
<%@ page import="com.munix.CheckWarehousing" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkWarehousing.label', default: 'CheckWarehousing')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="filter"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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
            <td valign="top" class="name"><g:message code="checkWarehousing.id.label" default="Id" /></td>
        <td valign="top" class="value">${checkWarehousingInstance}</td>
        <td valign="top" class="name"><g:message code="checkWarehousing.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${checkWarehousingInstance?.date}" format="MM/dd/yyyy" /></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="checkWarehousing.originWarehouse.label" default="Origin" /></td>
        <td valign="top" class="value"><g:link controller="checkWarehouse" action="show" id="${checkWarehousingInstance?.originWarehouse?.id}">${checkWarehousingInstance?.originWarehouse?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="checkWarehousing.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${checkWarehousingInstance?.preparedBy}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="checkWarehousing.destinationWarehouse.label" default="Destination" /></td>
        <td valign="top" class="value"><g:link controller="checkWarehouse" action="show" id="${checkWarehousingInstance?.destinationWarehouse?.id}">${checkWarehousingInstance?.destinationWarehouse?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="checkWarehousing.approvedBy.label" default="Approved By" /></td>
        <td valign="top" class="value">${checkWarehousingInstance?.approvedBy}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="checkWarehousing.status.label" default="Status" /></td>
          <td valign="top" class="value">${fieldValue(bean: checkWarehousingInstance, field: "status")}</td>
	      <td valign="top" class="name"><g:message code="checkWarehousing.remark.label" default="Remarks" /></td>
	      <td valign="top" class="value">${fieldValue(bean: checkWarehousingInstance, field: "remark")}</td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Checks</h2>
        <thead>
          <tr>
            <th>Number</th>
            <th>DP#</th>
            <th class="center">Date</th>
            <th>Bank</th>
            <th class="right">Amount</th>
          </tr>
        </thead>

        <tbody class="editable">
        <g:each in="${checkWarehousingInstance.checks.sort{it?.date}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directPayment/show/${i?.directPaymentItem?.directPayment?.id}'">
            <td>${i?.checkNumber}</td>
            <td>${i?.directPaymentItem?.directPayment}</td>
            <td class="center"><g:formatDate date="${i?.date}" format="MM/dd/yyyy" /></td>
            <td>${i?.formatBank()}</td>
            <td class="right">${i?.formatAmount()}</td>
          </tr>
        </g:each>
        </tbody>

        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${checkWarehousingInstance?.formatTotal()}</strong></td>
          </tr>
        </tfoot>

      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:if test="${checkWarehousingInstance?.status == 'Unapproved'}">
          <g:hiddenField name="id" value="${checkWarehousingInstance?.id}" />
          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
          <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
            <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:ifAnyGranted>
        </g:if>
        <span class="button"><g:link class="print" controller="print" action="checkWarehousing" target="checkWarehousing" params="${[id:checkWarehousingInstance.id]}" >Print</g:link></span>
      </g:form>
    </div>
  </div>
</body>
</html>
