
<%@ page import="com.munix.MaterialRequisition" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRequisition.label', default: 'MaterialRequisition')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table>
        <thead>
          <tr>

        <g:sortableColumn class="center" property="id" title="${message(code: 'materialRequisition.id.label', default: 'Id')}" />
        <th class="center"><g:message code="materialRequisition.jobOrder.label" default="Job Order" /></th>
        <th>Identifier</th>
        <th>Description</th>
        <g:sortableColumn property="preparedBy" title="${message(code: 'materialRequisition.preparedBy.label', default: 'Prepared By')}" />
        <g:sortableColumn property="status" title="${message(code: 'materialRequisition.status.label', default: 'Status')}" />
        <g:sortableColumn class="center" property="date" title="${message(code: 'materialRequisition.date.label', default: 'Date')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${materialRequisitionInstanceList}" status="i" var="materialRequisitionInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}materialRequisition/show/${materialRequisitionInstance.id}'">

            <td class="center">${materialRequisitionInstance}</td>
            <td class="center"><g:link controller="jobOrder" action="show" id="${materialRequisitionInstance?.jobOrder?.id}">${fieldValue(bean: materialRequisitionInstance, field: "jobOrder")}</g:link></td>
        <td>${materialRequisitionInstance?.jobOrder?.product}</td>
        <td>${materialRequisitionInstance?.jobOrder?.product?.description}</td>
        <td>${fieldValue(bean: materialRequisitionInstance, field: "preparedBy")}</td>
          <td>${fieldValue(bean: materialRequisitionInstance, field: "status")}</td>
          <td class="center"><g:formatDate date="${materialRequisitionInstance.date}" format="MM/dd/yyyy" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${materialRequisitionInstanceTotal}" />
    </div>
  </div>
</body>
</html>
