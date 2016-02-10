
<%@ page import="com.munix.SupplierPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplierPayment.label', default: 'SupplierPayment')}" />
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

        <g:sortableColumn property="id" title="${message(code: 'supplierPayment.id.label', default: 'Id')}" />
        <th><g:message code="supplierPayment.supplier.label" default="Supplier" /></th>
        <g:sortableColumn property="status" title="${message(code: 'supplierPayment.status.label', default: 'Status')}" />
        <g:sortableColumn property="paymentTotal" title="${message(code: 'supplierPayment.paymentTotal.label', default: 'Total Amount of Payments')}" />
        <g:sortableColumn property="date" title="${message(code: 'supplierPayment.date.label', default: 'Date')}" />
        
        </tr>
        </thead>
        <tbody>
        <g:each in="${supplierPaymentInstanceList}" status="i" var="supplierPaymentInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}supplierPayment/show/${supplierPaymentInstance?.id}'">

          <td id="rowSupplierPaymentId${i}" class="center">${supplierPaymentInstance}</td>
          <td>${fieldValue(bean: supplierPaymentInstance, field: "supplier")}</td> 
          <td>${fieldValue(bean: supplierPaymentInstance, field: "status")}</td>
          <td class="right">PHP ${supplierPaymentInstance?.formatPaymentTotal()}</td>
          <td class="center"><g:formatDate date="${supplierPaymentInstance.date}" format="MM/dd/yyyy" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${supplierPaymentInstanceTotal}" />
    </div>
  </div>
</body>
</html>
