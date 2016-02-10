
<%@ page import="com.munix.CurrencyType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'currencyType.label', default: 'CurrencyType')}" />
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
      <table id="currencyTypeListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'currencyType.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'currencyType.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${currencyTypeInstanceList}" status="i" var="currencyTypeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}currencyType/show/${currencyTypeInstance.id}'">

            <td id="rowCurrencyType${i}">${fieldValue(bean: currencyTypeInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: currencyTypeInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${currencyTypeInstanceTotal}" />
    </div>
  </div>
</body>
</html>
