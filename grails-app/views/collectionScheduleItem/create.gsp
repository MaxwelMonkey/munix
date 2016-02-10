
<%@ page import="com.munix.CollectionScheduleItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="generalmethods.js" />
  <g:javascript>
    var $ = jQuery.noConflict()
    
    $(document).ready(function () {
    
      $("#counterReceiptId").change(function () {  
            $.ajax({ url: '<g:createLink action="updateSelectType"/>',
                data: "selectedValue="+$('#counterReceiptId').val(),
                success: function(resp){  
               		$('#collectionScheduleItemTypeList')
						.find('option')
                    	.remove()
                    	.end()
                    $.each(resp.typeList, function(key, value)
                    {
                        $('#collectionScheduleItemTypeList')
                            .append('<option value="'+value+'">'+value+'</option>')
                    });
                    $('#collectionScheduleItemAmount').val(resp.amount)
                    $('amount').text(formatString(resp.amount))
                }
            });
        });
                  
     })
  </g:javascript>
</head>
<body>
<g:set var="collectionScheduleInstance" value="${com.munix.CollectionSchedule.get(params.id)}" />
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${collectionScheduleItemInstance}">
    <div class="errors">
      <g:renderErrors bean="${collectionScheduleItemInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="schedule"><g:message code="collectionScheduleItem.schedule.label" default="Schedule" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'schedule', 'errors')}">
        <g:link controller="collectionSchedule" action="show" id="${collectionScheduleInstance?.id}">${collectionScheduleInstance}</g:link>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="counterReceipt"><g:message code="collectionScheduleItem.counterReceipt.label" default="Counter Receipt" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'counterReceipt', 'errors')}">
        <g:select name="counterReceipt.id" id="counterReceiptId" noSelection="${['':'Select One...']}" from="${counterReceiptList}" optionKey="id" value="${collectionScheduleItemInstance?.counterReceipt?.id}"  />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="type"><g:message code="collectionScheduleItem.type.label" default="Type" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'type', 'errors')}">
        <g:select name="type" id="collectionScheduleItemTypeList" from="[]" value="${collectionScheduleItemInstance?.type}"  />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="type"><g:message code="collectionScheduleItem.amount.label" default="Amount" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: collectionScheduleItemInstance, field: 'amount', 'errors')}">
              <input type="hidden" name="amount" id="collectionScheduleItemAmount" value="0"/>
              <amount></amount>
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

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <input type="hidden" name="id" value="${params?.id}" />
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
