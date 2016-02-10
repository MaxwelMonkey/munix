
<%@ page import="com.munix.Schedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'schedule.label', default: 'Schedule')}" />
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
    <div class="search">
      <g:form controller="schedule" action="search" >
        <span><label>ID</label></span>
        <span><g:textField name="id" id="id"/></span>
        <span><label>Status</label></span>
        <span><g:textField name="status" id="status"/></span>
 	<span><label>Date</label></span>
        <span><g:textField name="date" id="date"/></span>
	<span><input type="submit" value="search"/></span>
      </g:form>
    </div>
    <div class="list">
      <table>
        <thead>
          <tr>
        <g:sortableColumn class="center" property="id" title="${message(code: 'schedule.id.label', default: 'Id')}" params="${params}"/>
        <g:sortableColumn property="status" title="${message(code: 'schedule.status.label', default: 'Status')}" params="${params}" />
        <g:sortableColumn class="center" property="startDate" title="${message(code: 'schedule.startDate.label', default: 'Start Date')}" params="${params}" />
        <g:sortableColumn class="center" property="endDate" title="${message(code: 'schedule.endDate.label', default: 'End Date')}" params="${params}" />
        </tr>
        </thead>
        <tbody>
        <g:each in="${scheduleInstanceList}" status="i" var="scheduleInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}schedule/show/${scheduleInstance.id}'">

            <td class="center">${scheduleInstance}</td>
            <td>${fieldValue(bean: scheduleInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${scheduleInstance.startDate}" format="MM/dd/yyyy"/></td>
          <td class="center"><g:formatDate date="${scheduleInstance.endDate}" format="MM/dd/yyyy"/></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${scheduleInstanceTotal}" />
    </div>
  </div>
</body>
</html>
