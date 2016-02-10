
<%@ page import="com.munix.Personnel" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'personnel.label', default: 'Personnel')}" />
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
      <table id="personnelListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'personnel.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="lastName" title="${message(code: 'personnel.lastName.label', default: 'Last Name')}" />

        <g:sortableColumn property="firstName" title="${message(code: 'personnel.firstName.label', default: 'First Name')}" />

        <th><g:message code="personnel.type.label" default="Type" /></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${personnelInstanceList}" status="i" var="personnelInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}personnel/show/${personnelInstance.id}'">

            <td id="rowPersonnel${i}">${fieldValue(bean: personnelInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: personnelInstance, field: "lastName")}</td>

            <td>${fieldValue(bean: personnelInstance, field: "firstName")}</td>

            <td>${fieldValue(bean: personnelInstance, field: "type")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${personnelInstanceTotal}" />
    </div>
  </div>
</body>
</html>
