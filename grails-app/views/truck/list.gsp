
<%@ page import="com.munix.Truck" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'truck.label', default: 'Truck')}" />
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
      <table id="truckListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'truck.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="model" title="${message(code: 'truck.model.label', default: 'Model')}" />

        <g:sortableColumn property="plateNumber" title="${message(code: 'truck.plateNumber.label', default: 'Plate Number')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${truckInstanceList}" status="i" var="truckInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}truck/show/${truckInstance.id}'">

            <td id="rowTruck${i}">${fieldValue(bean: truckInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: truckInstance, field: "model")}</td>

            <td>${fieldValue(bean: truckInstance, field: "plateNumber")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${truckInstanceTotal}" />
    </div>
  </div>
</body>
</html>
