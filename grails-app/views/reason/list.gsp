
<%@ page import="com.munix.Reason" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'reason.label', default: 'Reason')}" />
  <title>Credit Memo Reason List</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create">New Credit Memo Reason</g:link></span>
  </div>
  <div class="body">
    <h1>Credit Memo Reason List</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table id="reasonListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'reason.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'reason.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${reasonInstanceList}" status="i" var="reasonInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}reason/show/${reasonInstance.id}'">

            <td id="rowReason${i}">${fieldValue(bean: reasonInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: reasonInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${reasonInstanceTotal}" />
    </div>
  </div>
</body>
</html>
