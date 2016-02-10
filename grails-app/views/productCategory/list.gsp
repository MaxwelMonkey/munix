
<%@ page import="com.munix.ProductCategory" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'productCategory.label', default: 'ProductCategory')}" />
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
      <table id="categoryListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'productCategory.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'productCategory.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${productCategoryInstanceList}" status="i" var="productCategoryInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}productCategory/show/${productCategoryInstance.id}'">

            <td id="rowCategory${i}">${fieldValue(bean: productCategoryInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: productCategoryInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${productCategoryInstanceTotal}" />
    </div>
  </div>
</body>
</html>
