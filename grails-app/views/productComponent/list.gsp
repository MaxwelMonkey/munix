
<%@ page import="com.munix.ProductComponent" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'productComponent.label', default: 'ProductComponent')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table id="componentTable">
        <thead>
          <tr>

            <th><g:message code="productComponent.component.label" default="Component" /></th>

        <g:sortableColumn property="qty" title="${message(code: 'productComponent.qty.label', default: 'Quantity')}" />

        <th><g:message code="productComponent.product.label" default="Product for Assembly" /></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${productComponentInstanceList}" status="i" var="productComponentInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}product/show/${productComponentInstance.product.id}'">

            <td id="rowComponent${i}">${fieldValue(bean: productComponentInstance, field: "component")}</td>

            <td>${fieldValue(bean: productComponentInstance, field: "qty")}</td>

            <td>${fieldValue(bean: productComponentInstance, field: "product")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${productComponentInstanceTotal}" />
    </div>
  </div>
</body>
</html>
