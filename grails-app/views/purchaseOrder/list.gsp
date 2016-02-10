
<%@ page import="com.munix.PurchaseOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseOrder.label', default: 'PurchaseOrder')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <g:javascript src="calendarStructTemplate.js" />
  <g:javascript>
  	$(document).ready(function(){
        setCalendarStruct($("#startDate"), $("#startDate_value"))
        setCalendarStruct($("#endDate"), $("#endDate_value"))
    });
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>

  <div class="body">
    <calendar:resources lang="en" theme="aqua"/>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <div id="search">
      <g:form action="list" >
        <table>
          <tr>
            <td class="name">ID</td>
            <td class="value"><g:textField name="poId" id="poId" value="${params.poId}"/></td>
          </tr>
          <tr>
            <td class="name">Start Date</td>
            <td class="value"><calendar:datePicker name="startDate"  years="2009,2030" value="${dateMap.startDate}"/></td>
          </tr>
          <tr>
            <td class="name">End Date</td>
            <td class="value"><calendar:datePicker name="endDate"  years="2009,2030" value="${dateMap.endDate}"/></td>
          </tr>
          <tr>
            <td class="name">Supplier</td>
            <td class="value"><g:textField name="supplier" id="supplier" value="${params.supplier}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="status" noSelection="${['':'Select One...']}" from="${['Unapproved','Approved','Complete','Cancelled']}" value="${params.status}"/></td>
          </tr>
        </table>
        <div>
          <input type="submit" class="button" value="Search"/>
        </div>

      </g:form>
    </div>

    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table>
        <thead>
          <tr>
        <th><g:message code="purchaseOrder.id.label" default="Id" params="${params}"/></th>
        <g:sortableColumn property="status" title="${message(code: 'purchaseOrder.status.label', default: 'Status')}" params="${params}"/>
        <th>Complete</th>
        <g:sortableColumn property="date" title="${message(code: 'purchaseOrder.date.label', default: 'Date')}" params="${params}" />
        <th><g:message code="purchaseOrder.supplier.label" default="Supplier" params="${params}"/></th>
        <th class="right">Amount</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${purchaseOrderInstanceList}" status="i" var="purchaseOrderInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}purchaseOrder/show/${purchaseOrderInstance.id}'">
              <td id="rowPurchaseOrderIdentifier${i}" class="center">${purchaseOrderInstance.formatId()}</td>
              <td>${fieldValue(bean: purchaseOrderInstance, field: "status")}</td>
              <td class="center">
              <g:if test="${purchaseOrderInstance?.status == 'Complete'}">
                X
              </g:if>
              </td>
              <td class="center"><g:formatDate date="${purchaseOrderInstance.date}" format="MMM. dd, yyyy" /></td>
              <td>${purchaseOrderInstance?.supplier?.name }</td>
              <td class="right">${purchaseOrderInstance?.formatTotal()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${purchaseOrderInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
