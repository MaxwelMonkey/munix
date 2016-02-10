
<%@ page import="com.munix.MaterialRelease" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRelease.label', default: 'MaterialRelease')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Material Releases</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table>
        <thead>
          <tr>

        <g:sortableColumn class="center" property="id" title="${message(code: 'materialRelease.id.label', default: 'Id')}" />
        <th class="center">Job Order</th>
        <g:sortableColumn property="preparedBy" title="${message(code: 'materialRelease.preparedBy.label', default: 'Prepared By')}" />
        <g:sortableColumn property="status" title="${message(code: 'materialRelease.status.label', default: 'Status')}" />
        <g:sortableColumn class="center" property="date" title="${message(code: 'materialRelease.date.label', default: 'Date')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${materialReleaseInstanceList}" status="i" var="materialReleaseInstance">

          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}materialRelease/show/${materialReleaseInstance?.id}'">
            <td class="center">${materialReleaseInstance}</td>
            <td class="center"><g:link controller="jobOrder" action="show" params="[jobOrderId:materialReleaseInstance?.jobOrder?.id]">${materialReleaseInstance?.jobOrder}</g:link></td>
            <td>${fieldValue(bean: materialReleaseInstance, field: "preparedBy")}</td>
            <td>${fieldValue(bean: materialReleaseInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${materialReleaseInstance.date}" format="MM/dd/yyyy" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${materialReleaseInstanceTotal}" />
    </div>
  </div>
</body>
</html>
