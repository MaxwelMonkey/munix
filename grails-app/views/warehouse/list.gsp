
<%@ page import="com.munix.Warehouse" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'warehouse.label', default: 'Inventory Warehouse')}" />
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

        <g:sortableColumn property="identifier" title="${message(code: 'warehouse.identifier.label', default: 'Identifier')}" params="${params}"/>
        <g:sortableColumn property="description" title="${message(code: 'warehouse.description.label', default: 'Description')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${warehouseInstanceList}" status="i" var="warehouseInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}warehouse/show/${warehouseInstance.id}'">

            <td id="rowWarehouseIdentifier${i}">${fieldValue(bean: warehouseInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: warehouseInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${warehouseInstanceTotal}" />
    </div>
  </div>
</body>
</html>
