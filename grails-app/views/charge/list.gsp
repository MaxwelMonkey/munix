
<%@ page import="com.munix.Charge" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'charge.label', default: 'Charge')}" />
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
      <table id="chargeListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'charge.identifier.label', default: 'Identifier')}" />
        <g:sortableColumn property="description" title="${message(code: 'charge.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${chargeInstanceList}" status="i" var="chargeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}charge/show/${chargeInstance?.id}'">

            <td id="rowCharge${i}">${fieldValue(bean: chargeInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: chargeInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${chargeInstanceTotal}" />
    </div>
  </div>
</body>
</html>
