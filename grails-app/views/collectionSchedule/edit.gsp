
<%@ page import="com.munix.CollectionSchedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'collectionSchedule.label', default: 'CollectionSchedule')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${collectionScheduleInstance}">
      <div class="errors">
        <g:renderErrors bean="${collectionScheduleInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${collectionScheduleInstance?.id}" />
      <g:hiddenField name="version" value="${collectionScheduleInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="collectionSchedule.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: collectionScheduleInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${collectionScheduleInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="collectionSchedule.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${collectionScheduleInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="collector"><g:message code="collectionSchedule.collector.label" default="Collector" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleInstance, field: 'charge', 'errors')}">
          <g:select name="collector.id" from="${com.munix.Collector.list().sort{it.toString()}}" optionKey="id" value="${collectionScheduleInstance?.collector?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="startDate"><g:message code="collectionSchedule.startDate.label" default="Start Date" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleInstance, field: 'startDate', 'errors')}">
          <g:datePicker name="startDate" precision="day" value="${collectionScheduleInstance?.startDate}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="endDate"><g:message code="collectionSchedule.endDate.label" default="End Date" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleInstance, field: 'endDate', 'errors')}">
          <g:datePicker name="endDate" precision="day" value="${collectionScheduleInstance?.endDate}"  />
          </td>
          </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="remarks"><g:message code="collectionSchedule.remarks.label" default="Remarks" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: collectionSchedule, field: 'remarks', 'errors')}">
                <textarea name="remarks">${collectionScheduleInstance?.remarks}</textarea>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
