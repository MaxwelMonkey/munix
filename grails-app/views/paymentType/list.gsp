
<%@ page import="com.munix.PaymentType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'paymentType.label', default: 'PaymentType')}" />
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
      <table id="paymentTypeListTable">
        <thead>
          <tr>

            <g:sortableColumn property="identifier" title="${message(code: 'paymentType.identifier.label', default: 'Identifier')}" />

            <g:sortableColumn property="description" title="${message(code: 'paymentType.description.label', default: 'Description')}" />

          </tr>
        </thead>
        <tbody>
          <g:each in="${paymentTypeInstanceList}" status="i" var="paymentTypeInstance">
            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}paymentType/show/${paymentTypeInstance.id}'">
              <td id="rowPaymentType${i}">${fieldValue(bean: paymentTypeInstance, field: "identifier")}</td>
              <td>${fieldValue(bean: paymentTypeInstance, field: "description")}</td>
            </tr>
          </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${paymentTypeInstanceTotal}" />
    </div>
  </div>
</body>
</html>
