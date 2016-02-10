
<%@ page import="com.munix.JobOut" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOut.label', default: 'jobOut')}" />
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
      <table>
        <thead>
          <tr>

        <g:sortableColumn class="center" property="id" title="${message(code: 'jobOut.id.label', default: 'Id')}" />
        <g:sortableColumn class="center" property="jobOrder" title="${message(code: 'jobOut.jobOrder.label', default: 'Job Order')}" />
        <g:sortableColumn class="center" property="date" title="${message(code: 'jobOut.date.label', default: 'Date')}" />
        <g:sortableColumn class="center" property="product" title="${message(code: 'jobOut.product.label', default: 'Product')}" />
        <g:sortableColumn class="center" property="qty" title="${message(code: 'jobOut.qty.label', default: 'Quantity')}" />
        <g:sortableColumn class="center" property="warehouse" title="${message(code: 'jobOut.warehouse.label', default: 'Warehouse')}" />
        <g:sortableColumn class="center" property="assignedTo" title="${message(code: 'jobOut.assignedTo.label', default: 'Assigned To')}" />
        <g:sortableColumn class="center" property="status" title="${message(code: 'jobOut.status.label', default: 'Status')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${jobOutInstanceList}" status="i" var="jobOutInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}jobOut/show/${jobOutInstance.id}'">

              <td>${jobOutInstance}</td>
              <td>${fieldValue(bean: jobOutInstance, field: "jobOrder")}</td>
              <td><g:formatDate date="${jobOutInstance.date}" format="MM/dd/yyyy" /></td>
              <td>${jobOutInstance.jobOrder.product}</td>
              <td class="right">${fieldValue(bean: jobOutInstance, field: "qty")}</td>
              <td>${fieldValue(bean: jobOutInstance, field: "warehouse")}</td>
              <td>${jobOutInstance?.jobOrder?.assignedTo}</td>
              <td>${fieldValue(bean: jobOutInstance, field: "status")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${jobOutInstanceTotal}" />
    </div>
  </div>
</body>
</html>
