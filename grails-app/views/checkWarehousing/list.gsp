
<%@ page import="com.munix.CheckWarehousing" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkWarehousing.label', default: 'CheckWarehousing')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="filter"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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

        <g:sortableColumn class="center" property="id" title="${message(code: 'checkWarehousing.id.label', default: 'Id')}" />
        <th><g:message code="checkWarehousing.originWarehouse.label" default="Origin" /></th>
        <th><g:message code="checkWarehousing.destinationWarehouse.label" default="Destination" /></th>
        <g:sortableColumn property="status" title="${message(code: 'checkWarehousing.status.label', default: 'Status')}" />
        <g:sortableColumn class="center" property="date" title="${message(code: 'checkWarehousing.date.label', default: 'Date')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${checkWarehousingInstanceList}" status="i" var="checkWarehousingInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onClick="window.location='${createLink(uri:'/')}checkWarehousing/show/${checkWarehousingInstance?.id}'">

            <td class="center">${checkWarehousingInstance}</td>
            <td>${fieldValue(bean: checkWarehousingInstance, field: "originWarehouse")}</td>
            <td>${fieldValue(bean: checkWarehousingInstance, field: "destinationWarehouse")}</td>
            <td>${fieldValue(bean: checkWarehousingInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${checkWarehousingInstance.date}" format="MM/dd/yyyy"/></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${checkWarehousingInstanceTotal}" />
    </div>
  </div>
</body>
</html>
