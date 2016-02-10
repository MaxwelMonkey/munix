
<%@ page import="com.munix.JobOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOrder.label', default: 'JobOrder')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
    <g:javascript src="numbervalidation.js" />
  <g:javascript>
    <munix:selectorConfig />
    //Selector
    var $ = jQuery.noConflict()

    var addSelectorListener = function(){
        $("#item").click(function(){
            $("#selector").toggle()
        })
    }

    function fillData(data){
        var arr = data.split('||');
        $('#product\\.id').val(arr[0]);
        $('#item').val(arr[1]);
        $('#selector').hide();
    }

    $(document).ready(
        function(){
            $("#selector").hide()
            $("#qty").ForceNumericOnly(false)
            addSelectorListener()
        }
    )
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${jobOrderInstance}">
      <div class="errors">
        <g:renderErrors bean="${jobOrderInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="updateJobOrder" method="post" >
      <calendar:resources lang="en" theme="aqua"/>
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="id"><g:message code="jobOrder.id.label" default="ID" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'id', 'errors')}">
                <g:link controller="jobOrder" action="show" params="[jobOrderId : jobOrderInstance.id]">${jobOrderInstance}</g:link>
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="product"><g:message code="jobOrder.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'identifier', 'errors')}">
                ${jobOrderInstance?.product?.identifier}
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="description"><g:message code="jobOrder.description.label" default="Description" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'description', 'errors')}">
                ${jobOrderInstance?.product?.description}
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="qty"><g:message code="jobOrder.qty.label" default="Quantity" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'qty', 'errors')}">
                <g:textField name="qty" maxlength="7" id="qty" value="${jobOrderInstance.qty?:''}" />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="startDate"><g:message code="jobOrder.startDate.label" default="Start Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'startDate', 'errors')}">
                <calendar:datePicker name="startDate" value="${jobOrderInstance?.startDate}"/>
                <g:select from="${00..23}" name="startDateHour" value="${jobOrderInstance?.startDate.getHours()}"/>
                :
                <g:select from="${00..59}" name="startDateMinute" value="${jobOrderInstance?.startDate.getMinutes()}"/>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="targetDate"><g:message code="jobOrder.targetDate.label" default="Target Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'targetDate', 'errors')}">
                <calendar:datePicker name="targetDate" value="${jobOrderInstance?.targetDate}"  />
                <g:select from="${00..23}" name="targetDateHour" value="${jobOrderInstance?.targetDate.getHours()}"/>
                :
                <g:select from="${00..59}" name="targetDateMinute" value="${jobOrderInstance?.targetDate.getMinutes()}"/>
              </td>
            </tr>


            <tr class="prop">
              <td valign="top" class="name">
                <label for="assignedTo"><g:message code="jobOrder.assignedTo.label" default="Assigned To" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'assignedTo', 'errors')}">
                <g:select name="assignedTo.id" from="${com.munix.Assembler.list().sort{it.toString()}}" optionKey="id" value="${jobOrderInstance?.assignedTo?.id}"  />
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="remark"><g:message code="jobOrder.remark.label" default="Remarks" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: jobOrderInstance, field: 'remark', 'errors')}">
                <g:textArea name="remark" maxlength="255" value="${fieldValue(bean: jobOrderInstance, field: 'remark')}" />
              </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <g:hiddenField name="id" value="${jobOrderInstance?.id}" />
        <span class="button"><g:submitButton onclick="return confirm('${message(code : 'default.prompt')}')" name="create" class="save" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
