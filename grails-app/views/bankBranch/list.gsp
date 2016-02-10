
<%@ page import="com.munix.BankBranch" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'bankBranch.label', default: 'BankBranch')}" />
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
      <table>
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'bankBranch.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'bankBranch.description.label', default: 'Description')}" />

        <g:sortableColumn property="bank" title="Bank" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${bankBranchInstanceList}" status="i" var="bankBranchInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}bankBranch/show/${bankBranchInstance.id}'">

            <td>${fieldValue(bean: bankBranchInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: bankBranchInstance, field: "description")}</td>

            <td>${fieldValue(bean: bankBranchInstance, field: "bank")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${bankBranchInstanceTotal}" />
    </div>
  </div>
</body>
</html>
