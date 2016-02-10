
<%@ page import="com.munix.CheckType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkType.label', default: 'CheckType')}" />
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
      <table id="checkTypeListTable">
        <thead>
          <tr>

        <g:sortableColumn property="routingNumber" title="${message(code: 'checkType.routingNumber.label', default: 'Routing Number')}" />
        <g:sortableColumn property="branch" title="${message(code: 'checkType.branch.label', default: 'Branch')}" />
        <g:sortableColumn property="description" title="${message(code: 'checkType.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${checkTypeInstanceList}" status="i" var="checkTypeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}checkType/show/${checkTypeInstance.id}'">

            <td id="rowCheckType${i}">${fieldValue(bean: checkTypeInstance, field: "routingNumber")}</td>
            <td>${fieldValue(bean: checkTypeInstance, field: "branch")}</td>
            <td>${fieldValue(bean: checkTypeInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${checkTypeInstanceTotal}" />
    </div>
  </div>
</body>
</html>
