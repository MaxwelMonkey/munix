
<%@ page import="com.munix.Schedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'schedule.label', default: 'Schedule')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <link rel="stylesheet" href="${resource(dir:'css',file:'jsgantt.css')}" />
  <g:javascript src="jsgantt.js" />
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
            <td valign="top" class="name"><g:message code="schedule.id.label" default="ID" /></td>
        <td valign="top" class="value">${scheduleInstance}</td>
        <td valign="top" class="name"><g:message code="schedule.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${scheduleInstance?.preparedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="schedule.startDate.label" default="Start Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${scheduleInstance?.startDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
        <td valign="top" class="name"><g:message code="schedule.endDate.label" default="End Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${scheduleInstance?.endDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"></td>
          <td valign="top" class="value"></td>
          <td valign="top" class="name"></td>
          <td valign="top" class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="schedule.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: scheduleInstance, field: "status")}</td>
        <td valign="top" class="name">Completion</td>
        <td valign="top" class="value">${scheduleInstance?.formatCompletion()}</td>
        </tr>

        </tbody>
      </table>
    </div>


    <div class="subTable">
      <table>
        <h2>Job Orders</h2>
        <thead>
          <tr>
            <th class="center">ID</th>
            <th>Identifier</th>
            <th>Description</th>
            <th>Assigned To</th>
            <th class="right">Qty</th>
            <th class="right">Out</th>
            <th class="right">Remaining</th>
            <th class="right">Completion</th>
            <th class="center">Start</th>
            <th class="center">Target</th>
            <th class="center">End</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${scheduleInstance.jobOrders.sort{it.startDate}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location = '${createLink(uri:'/')}jobOrder/show/${i.id}'">
            <td class="center">${i}</td>
            <td>${i?.product}</td>
            <td>${i?.product?.description}</td>
            <td>${i?.assignedTo}</td>
            <td class="right">${i?.qty}</td>
            <td class="right">${i?.computeOutTotal()}</td>
            <td class="right">${i?.computeRemainingBalance()}</td>
            <td class="right">${i?.formatCompletion()}</td>
            <td class="center"><g:formatDate format="MM/dd/yyyy hh:mm aaa" date="${i?.startDate}" /></td>
          <td class="center"><g:formatDate format="MM/dd/yyyy hh:mm aaa" date="${i?.targetDate}" /></td>
          <td class="center"><g:formatDate format="MM/dd/yyyy hh:mm aaa" date="${i?.endDate}" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div style="position:relative" class="gantt" id="GanttChartDIV"></div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${scheduleInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
        <span class="button"><g:link class="chart" action="chart">view chart</g:link></span>
      </g:form>
    </div>
  </div>
</body>
</html>
