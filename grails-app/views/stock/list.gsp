
<%@ page import="com.munix.Stock" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'stock.label', default: 'Stock')}" />
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

        <g:sortableColumn property="id" title="${message(code: 'stock.id.label', default: 'Id')}" />

        <g:sortableColumn property="warehouse" title="${message(code: 'stock.warehouse.label', default: 'Warehouse')}" />

        <g:sortableColumn property="product" title="${message(code: 'stock.product.label', default: 'Product')}" />

        <g:sortableColumn property="qty" title="${message(code: 'stock.qty.label', default: 'Qty')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${stockInstanceList}" status="i" var="stockInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}stock/show/${stockInstance.id}'">

            <td><g:link action="show" id="${stockInstance.id}">${fieldValue(bean: stockInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: stockInstance, field: "warehouse")}</td>

          <td>${fieldValue(bean: stockInstance, field: "product")}</td>

          <td>${fieldValue(bean: stockInstance, field: "qty")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${stockInstanceTotal}" />
    </div>
  </div>
</body>
</html>
