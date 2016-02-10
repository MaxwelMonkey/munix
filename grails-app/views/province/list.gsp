
<%@ page import="com.munix.Province" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'province.label', default: 'Province')}" />
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
      <table id="provinceListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'province.identifier.label', default: 'Identifier')}" />
        <g:sortableColumn property="description" title="${message(code: 'province.description.label', default: 'Description')}" />
        <th><g:message code="province.region.label" default="Region" /></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${provinceInstanceList}" status="i" var="provinceInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}province/show/${provinceInstance.id}'">

            <td id="rowProvince${i}">${fieldValue(bean: provinceInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: provinceInstance, field: "description")}</td>
            <td>${fieldValue(bean: provinceInstance, field: "region")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${provinceInstanceTotal}" />
    </div>
  </div>
</body>
</html>
