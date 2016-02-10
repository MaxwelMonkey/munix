
<%@ page import="com.munix.CollectionScheduleItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${collectionScheduleItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${collectionScheduleItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${collectionScheduleItemInstance?.id}" />
      <g:hiddenField name="version" value="${collectionScheduleItemInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="schedule"><g:message code="collectionScheduleItem.schedule.label" default="Schedule" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'schedule', 'errors')}">
          <g:link controller="collectionSchedule" action="show" id="${collectionScheduleItemInstance?.schedule?.id}">${collectionScheduleItemInstance?.schedule}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="counterReceipt"><g:message code="collectionScheduleItem.counterReceipt.label" default="Counter Receipt" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'counterReceipt', 'errors')}">
${collectionScheduleItemInstance?.counterReceipt}
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="collectionScheduleItem.type.label" default="Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'type', 'errors')}">
        <g:select name="type" id="collectionScheduleItemTypeList" from="${types}" value="${collectionScheduleItemInstance?.type}"  />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="amount"><g:message code="collectionScheduleItem.amount.label" default="Amount" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'amount', 'errors')}">
PHP <g:formatNumber number="${collectionScheduleItemInstance?.amount}" format="###,##0.00" />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="collectionScheduleItem.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'remark', 'errors')}">
          <g:textArea name="remark" value="${collectionScheduleItemInstance?.remark}" />
          </td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name">
              <label for="isComplete"><g:message code="collectionScheduleItem.isComplete.label" default="Is Complete" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'isComplete', 'errors')}">
          <g:checkBox name="isComplete" value="${collectionScheduleItemInstance?.isComplete}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
