
<%@ page import="com.munix.Warehouse" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'warehouse.label', default: 'Inventory Warehouse')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>
          
        <tr class="prop">
          <td valign="top" class="name"><g:message code="warehouse.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: warehouseInstance, field: "identifier")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="warehouse.description.label" default="Description" /></td>
        <td valign="top" class="value">${fieldValue(bean: warehouseInstance, field: "description")}</td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Stocks</h2>
        <thead>
          <tr>
            <th>Product</th>
            <th>Description</th>
            <th class="right">SOH</th>
          </tr>
        </thead>
        <tbody class="stockList">
        <g:each in="${warehouseInstance?.stocks.findAll{it.qty > 0}.sort{it.product.identifier}}" var="i" status="colors">
          <tr class="right ${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}product/show/${i?.product?.id}'">
            <td>${i?.product?.identifier}</td>
            <td>${i?.product?.description}</td>
            <td class="right ${(colors % 2) == 0 ? 'actualOdd' : 'actualEven'}">${i?.formatQty()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${warehouseInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
