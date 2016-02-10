
<%@ page import="com.munix.Term" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'term.label', default: 'Term')}" />
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
      <table id="termListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'term.identifier.label', default: 'Identifier')}" />
        <g:sortableColumn property="description" title="${message(code: 'term.description.label', default: 'Description')}" />
        <g:sortableColumn class="right" property="dayValue" title="${message(code: 'term.dayValue.label', default: 'Day Value')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${termInstanceList}" status="i" var="termInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}term/show/${termInstance.id}'">

            <td id="rowTerm${i}">${fieldValue(bean: termInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: termInstance, field: "description")}</td>
            <td class="right">${fieldValue(bean: termInstance, field: "dayValue")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${termInstanceTotal}" />
    </div>
  </div>
</body>
</html>
