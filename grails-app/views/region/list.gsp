
<%@ page import="com.munix.Region" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'region.label', default: 'Region')}" />
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
      <table id="regionListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'region.identifier.label', default: 'identifier')}" />
        <g:sortableColumn property="description" title="${message(code: 'region.description.label', default: 'Description')}" />
        <th><g:message code="region.country.label" default="Country" /></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${regionInstanceList}" status="i" var="regionInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}region/show/${regionInstance.id}'">

            <td id="rowRegion${i}">${fieldValue(bean: regionInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: regionInstance, field: "description")}</td>
            <td>${fieldValue(bean: regionInstance, field: "country")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${regionInstanceTotal}" />
    </div>
  </div>
</body>
</html>
