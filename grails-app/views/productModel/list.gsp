
<%@ page import="com.munix.ProductModel" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'productModel.label', default: 'ProductModel')}" />
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
      <table id="modelListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'productModel.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'productModel.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${productModelInstanceList}" status="i" var="productModelInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}productModel/show/${productModelInstance.id}'">

            <td id="rowModel${i}">${fieldValue(bean: productModelInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: productModelInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${productModelInstanceTotal}" />
    </div>
  </div>
</body>
</html>
