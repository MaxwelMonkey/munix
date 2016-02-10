
<%@ page import="com.munix.Bank" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'bank.label', default: 'Bank')}" />
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
      <table id="bankListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'bank.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'bank.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${bankInstanceList}" status="i" var="bankInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}bank/show/${bankInstance.id}'">

          <td id="rowBank${i}">${fieldValue(bean: bankInstance, field: "identifier")}</td>

          <td>${fieldValue(bean: bankInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${bankInstanceTotal}" />
    </div>
  </div>
</body>
</html>
