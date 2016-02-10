
<%@ page import="com.munix.MaterialRequisition" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRequisition.label', default: 'MaterialRequisition')}" />
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
    <g:hasErrors bean="${materialRequisitionInstance}">
      <div class="errors">
        <g:renderErrors bean="${materialRequisitionInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="date"><g:message code="materialRequisition.date.label" default="Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'date', 'errors')}">
          <g:datePicker name="date" precision="day" value="${materialRequisitionInstance?.date}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="jobOrder"><g:message code="materialRequisition.jobOrder.label" default="Job Order" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: materialRequisitionInstance, field: 'jobOrder', 'errors')}">
          <g:select name="jobOrder.id" from="${com.munix.JobOrder.list()}" optionKey="id" value="${materialRequisitionInstance?.jobOrder?.id}"  />
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
