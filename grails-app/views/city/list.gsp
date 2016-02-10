
<%@ page import="com.munix.City" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'city.label', default: 'City')}" />
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
      <table id="cityListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'city.identifier.label', default: 'Identifier')}" />
        <g:sortableColumn property="description" title="${message(code: 'city.description.label', default: 'Description')}" />
        <g:sortableColumn property="province" title="${message(code: 'city.province.label', default: 'Province')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${cityInstanceList}" status="i" var="cityInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}city/show/${cityInstance.id}'">

            <td id="rowCity${i}">${fieldValue(bean: cityInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: cityInstance, field: "description")}</td>
            <td>${fieldValue(bean: cityInstance, field: "province")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${cityInstanceTotal}" />
    </div>
  </div>
</body>
</html>
