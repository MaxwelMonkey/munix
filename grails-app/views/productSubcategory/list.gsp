
<%@ page import="com.munix.ProductSubcategory" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'productSubcategory.label', default: 'ProductSubcategory')}" />
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
      <table id="subcategoryListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'productSubcategory.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'productSubcategory.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${productSubcategoryInstanceList}" status="i" var="productSubcategoryInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}productSubcategory/show/${productSubcategoryInstance.id}'">

            <td id="rowSubcategory${i}">${fieldValue(bean: productSubcategoryInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: productSubcategoryInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${productSubcategoryInstanceTotal}" />
    </div>
  </div>
</body>
</html>
