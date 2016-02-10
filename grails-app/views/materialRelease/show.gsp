<%@ page import="com.munix.MaterialRelease" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRelease.label', default: 'MaterialRelease')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
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
          <td valign="top" class="name"><g:message code="materialRelease.id.label" default="ID" /></td>
          <td valign="top" class="value">${materialReleaseInstance}</td>  
          <td valign="top" class="name"><g:message code="materialRelease.preparedBy.label" default="Prepared by" /></td>
          <td valign="top" class="value">${fieldValue(bean: materialReleaseInstance, field: "preparedBy")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="jobOrder.label" default="Job Order" /></td>
          <td valign="top" class="value"><g:link controller="jobOrder" action="show" params="[jobOrderId: materialReleaseInstance?.jobOrder?.id]">${materialReleaseInstance?.jobOrder}</g:link></td>
          <td valign="top" class="name"><g:message code="materialRelease.approvedBy.label" default="Approved by" /></td>
          <td valign="top" class="value">${fieldValue(bean: materialReleaseInstance, field: "approvedBy")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="materialRelease.warehouse.label" default="Warehouse" /></td>
          <td valign="top" class="value">${fieldValue(bean: materialReleaseInstance, field: "warehouse")}</td>
          <td valign="top" class="name"><g:message code="materialRelease.cancelledBy.label" default="Cancelled by" /></td>
          <td valign="top" class="value">${fieldValue(bean: materialReleaseInstance, field: "cancelledBy")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"></td>
          <td valign="top" class="value"></td>
          <td valign="top" class="name"><g:message code="materialRelease.status.label" default="Status" /></td>
          <td valign="top" class="value">${fieldValue(bean: materialReleaseInstance, field: "status")}</td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <thead>
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Description</th>
            <th class="center">Quantity</th>
          </tr>
        </thead>

        <g:if test="${materialReleaseInstance?.isApproved()}">
          <tbody class="editable">
        </g:if>
        <g:else>
          <tbody class="uneditable">
        </g:else>

        <g:each in="${materialReleaseItems.sort{it?.materialRequisitionItem?.component?.description}}" var="item" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${item?.materialRequisitionItem?.component}</td>
            <td>${item?.materialRequisitionItem?.component?.description}</td>
            <td class="right"><g:formatNumber number="${item?.qty}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${materialReleaseInstance?.id}" />
        <g:if test="${materialReleaseInstance?.isUnapproved()}">
          <span class="button"><g:actionSubmit class="cancel" action="cancel" value="Cancel" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');"/></span>
          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
          <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION">
            <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:ifAnyGranted>
        </g:if>
        <g:if test="${materialReleaseInstance?.isApproved() && !materialReleaseInstance?.jobOrder?.materialReleasesApprovedBy}">
          <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION">
            <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.unapprove.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:ifAnyGranted>
        </g:if>
 	    <span class="button"><g:link class="print" target="blank" controller="print" action="materialRelease" id="${materialReleaseInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
      </g:form>
    </div>

  </div>
</body>
</html>
