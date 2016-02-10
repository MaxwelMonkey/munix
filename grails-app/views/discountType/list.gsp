
<%@ page import="com.munix.DiscountType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'discountType.label', default: 'DiscountType')}" />
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
      <table id="discountTypeListTable">
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'discountType.identifier.label', default: 'Identifier')}" />

        <g:sortableColumn property="description" title="${message(code: 'discountType.description.label', default: 'Description')}" />

		<g:sortableColumn property="discountedItemMargin" title="${message(code: 'discountType.discountedItemMargin.label', default: 'Discounted Item Margin')}" />
		
		<g:sortableColumn property="netItemMargin" title="${message(code: 'discountType.netItemMargin.label', default: 'Net Item Margin')}" />
		
        </tr>
        </thead>
        <tbody>
        <g:each in="${discountTypeInstanceList}" status="i" var="discountTypeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}discountType/show/${discountTypeInstance.id}'">

            <td id="rowDiscountType${i}">${fieldValue(bean: discountTypeInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: discountTypeInstance, field: "description")}</td>
			<td class="right">${fieldValue(bean: discountTypeInstance, field: "discountedItemMargin")}</td>
			<td class="right">${fieldValue(bean: discountTypeInstance, field: "netItemMargin")}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${discountTypeInstanceTotal}" />
    </div>
  </div>
</body>
</html>
