
<%@ page import="com.munix.Country" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'country.label', default: 'Country')}" />
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
      <table id="countryListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'country.identifier.label', default: 'Identifier')}" />
        <g:sortableColumn property="description" title="${message(code: 'country.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${countryInstanceList}" status="i" var="countryInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}country/show/${countryInstance.id}'">

            <td id="rowCountry${i}">${fieldValue(bean: countryInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: countryInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${countryInstanceTotal}" />
    </div>
  </div>
</body>
</html>
