
<%@ page import="com.munix.Schedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'schedule.label', default: 'Schedule')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${scheduleInstance}">
      <div class="errors">
        <g:renderErrors bean="${scheduleInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="startDate"><g:message code="schedule.startDate.label" default="Start Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: scheduleInstance, field: 'startDate', 'errors')}">
          <g:datePicker name="startDate" value="${scheduleInstance?.startDate}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="endDate"><g:message code="schedule.endDate.label" default="End Date" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: scheduleInstance, field: 'endDate', 'errors')}">
          <g:datePicker name="endDate" value="${scheduleInstance?.endDate}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="jobOrders"><g:message code="schedule.jobOrders.label" default="Job Orders" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: scheduleInstance, field: 'jobOrders', 'errors')}">
          <g:select name="jobOrders" from="${com.munix.JobOrder.list().sort{it.toString()}}" optionKey="id" size="10" multiple="true" value="${schedule?.jobOrders?.id}"/>
          </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
