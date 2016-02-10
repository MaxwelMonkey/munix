<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1>Customer History of Changes</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table>
        <thead>
          <th class="center">User</th>
          <th class="center">Date</th>
          <th class="center">Field Name</th>
          <th class="center">Previous Entry</th>
          <th class="center">New Entry</th>
          <th class="center">Approved By</th>
        </thead>
        <tbody>
        <g:each in="${customerInstance.auditTrails.sort{it.date}}" status="i" var="auditTrail">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
          	<td>${fieldValue(bean: auditTrail, field: "user.userRealName")}</td>
           	<td class="right"><g:formatDate date="${auditTrail.date}" format="MM/dd/yyyy hh:mm a"/></td>
            <td>${fieldValue(bean: auditTrail, field: "fieldName")}</td>
            <td>${fieldValue(bean: auditTrail, field: "previousEntry")}</td>
            <td>${fieldValue(bean: auditTrail, field: "newEntry")}</td>
            <td>${fieldValue(bean: auditTrail, field: "approvedBy")}</td>
          </tr>
        </g:each>

        </tbody>
      </table>
    </div>
    
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${customerInstance?.id}" />
       	  <span class="button"><g:actionSubmit class="show" action="show" value="${message(code: 'default.button.show.label', default: 'Back to Customer')}" /></span>
      </g:form>
    </div>

  </div>
</body>
</html>
