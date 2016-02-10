
<%@ page import="com.munix.CustomerType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'customerType.label', default: 'CustomerType')}" />
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
      <table id="customerTypeListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'customerType.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'customerType.description.label', default: 'Description')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${customerTypeInstanceList}" status="i" var="customerTypeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}customerType/show/${customerTypeInstance.id}'">

            <td id="rowCustomerType${i}">${fieldValue(bean: customerTypeInstance, field: "identifier")}</td>

            <td>${fieldValue(bean: customerTypeInstance, field: "description")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${customerTypeInstanceTotal}" />
    </div>
  </div>
</body>
</html>
