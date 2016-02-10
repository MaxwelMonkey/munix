
<%@ page import="com.munix.ProductColor" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'productColor.label', default: 'ProductColor')}" />
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
      <table id="colorListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'productColor.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'productColor.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${productColorInstanceList}" status="i" var="productColorInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}productColor/show/${productColorInstance.id}'">

            <td id="rowColor${i}">${fieldValue(bean: productColorInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: productColorInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${productColorInstanceTotal}" />
    </div>
  </div>
</body>
</html>
