<%@ page import="com.munix.CollectionSchedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'collectionSchedule.label', default: 'CollectionSchedule')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
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
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>    
    <div class="dialog">
      <table>
        <tbody>

        <tr class="prop">
        <td valign="top" class="name"><g:message code="collectionSchedule.id.label" default="ID" /></td>
        <td valign="top" class="value">${collectionScheduleInstance}</td>
        <td valign="top" class="name"><g:message code="collectionSchedule.preparedBy.label" default="Prepared By" /></td>
	    <td valign="top" class="value"> ${collectionScheduleInstance?.preparedBy?.encodeAsHTML()} </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="collectionSchedule.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: collectionScheduleInstance, field: "identifier")}</td>
        <td valign="top" class="name"><g:message code="collectionSchedule.closedBy.label" default="Closed By" /></td>
	    <td valign="top" class="value"> ${collectionScheduleInstance?.closedBy?.encodeAsHTML()} </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="collectionSchedule.description.label" default="Description" /></td>
        <td valign="top" class="value">${fieldValue(bean: collectionScheduleInstance, field: "description")}</td>
        <td valign="top" class="name"><g:message code="collectionSchedule.cancelledBy.label" default="Cancelled By" /></td>
	    <td valign="top" class="value"> ${collectionScheduleInstance?.cancelledBy?.encodeAsHTML()} </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="collectionSchedule.collector.label" default="Collector" /></td>
        <td valign="top" class="value">${fieldValue(bean: collectionScheduleInstance, field: "collector")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="collectionSchedule.startDate.label" default="Start Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${collectionScheduleInstance?.startDate}"  format="MM/dd/yyyy"/></td>
        <td valign="top" class="name"><g:message code="collectionSchedule.endDate.label" default="End Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${collectionScheduleInstance?.endDate}" format="MM/dd/yyyy" /></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="collectionSchedule.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: collectionScheduleInstance, field: "status")}</td>
          <td valign="top" class="name"><g:message code="collectionSchedule.remarks.label" default="Remarks" /></td>
        <td valign="top" class="value">${fieldValue(bean: collectionScheduleInstance, field: "remarks")}</td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Items</h2>
        <thead>
        <g:if test="${collectionScheduleInstance?.isProcessing()}">
          <g:link class="addItem" controller="collectionScheduleItem" action="create" id="${collectionScheduleInstance?.id}">Add Item</g:link>
        </g:if>
        <tr>
          <th>Customer</th>
          <th>Counter Receipt</th>
          <th class="center">Date</th>
          <th class="center">Counter Date</th>
          <th class="center">Collection Date</th>
          <th>Type</th>
          <th>Remarks</th>
          <th class="center">Complete</th>
          <th class="center">Amount</th>
        </tr>
        </thead>
        <g:if test="${collectionScheduleInstance?.status != 'Complete'}">
          <tbody class="editable">
        </g:if>
        <g:else>
          <tbody class="uneditable">
        </g:else>
        <g:each in="${collectionScheduleInstance.items.sort{it.toString()}}" var="i" status="colors">
          <g:if test="${collectionScheduleInstance?.isComplete()}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          </g:if>
          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}collectionScheduleItem/edit/${i?.id}'">
          </g:else>
          <td>${i?.counterReceipt?.customer}</td>
          <td><g:link controller="counterReceipt" action="show" id="${i?.counterReceipt?.id}">${i?.counterReceipt}</g:link></td>
          <td class="center"><g:formatDate date="${i?.counterReceipt?.date}" format="MM/dd/yyyy"/></td>
          <td class="center"><g:formatDate date="${i?.counterReceipt?.counterDate}" format="MM/dd/yyyy"/></td>
          <td class="center"><g:formatDate date="${i?.counterReceipt?.collectionDate}" format="MM/dd/yyyy"/></td>
          <td>${i?.type}</td>
          <td>${i?.remark}</td>
          <g:if test="${i?.isComplete}">
            <td class="center">X</td>
          </g:if>
          <g:else>
            <td class="center"></td>
          </g:else>
          <td class="right">PHP <g:formatNumber number="${i?.amount}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        </tbody>
         <tfoot class="total">
          <tr>
          <g:set var="c" value="${collectionScheduleInstance}"/>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${c?.computeTotalAmount()}" format="###,##0.00" /></strong></td>
          </tr>
         </tfoot>
      </table>
    </div>
    
    <div class="subTable">
       <table>
         <tfoot class="total">
          <tr>
          	<td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
          	<td><strong>Amount (Counter)</strong></td>
			<td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${c?.computeAmountCounterTotal()}" format="###,##0.00" /></strong></td>
          </tr>
          <tr>
          	<td><strong>Amount (Collection)</strong></td>
			<td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${c?.computeAmountCollectionTotal()}" format="###,##0.00" /></strong></td>
          </tr>
          <tr>
          	<td><strong>Amount (Both)</strong></td>
			<td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${c?.computeAmountBothTotal()}" format="###,##0.00" /></strong></td>
          </tr>
          <tr>
          	<td><strong>Total Amount</strong></td>
			<td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${c?.computeTotalAmount()}" format="###,##0.00" /></strong></td>
          </tr>                      
        </tfoot>
      </table>
    </div>
      <div class="buttons">
        <g:form>
          <g:hiddenField name="id" value="${collectionScheduleInstance?.id}" />
  	      <g:if test="${collectionScheduleInstance?.isProcessing()}">
            <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
            <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
            <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
              <span class="button"><g:actionSubmit class="markAsComplete" action="markAsComplete" value="${message(code: 'default.button.markAsComplete.label', default: 'Mark As Complete')}" onclick="return confirm('${message(code: 'default.button.markAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
            </g:ifAnyGranted>
	      </g:if>
          <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
	        <g:if test="${isUnmarkableAsComplete}">
              <span class="button"><g:actionSubmit class="unmarkAsComplete" action="unmarkAsComplete" value="${message(code: 'default.button.unmarkAsComplete.label', default: 'Unmark As Complete')}" onclick="return confirm('${message(code: 'default.button.unmarkAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
            </g:if>
          </g:ifAnyGranted>
          <span class="button"><g:link class="print" controller="print" action="collectionSchedule" target="blank" id="${collectionScheduleInstance?.id}" >Print</g:link></span>
        </g:form>
      </div>
  </div>
</body>
</html>
