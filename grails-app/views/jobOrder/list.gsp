<%@ page import="com.munix.JobOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOrder.label', default: 'JobOrder')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  	<g:javascript src="generalmethods.js" />
  	<g:javascript src="calendarStructTemplate.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function () {
			$("#searchId").ForceNumericOnlyEnterAllowed(true)
			setCalendarStruct($("#searchStartDateFrom"), $("#searchStartDateFrom_value"))
        	setCalendarStruct($("#searchStartDateTo"), $("#searchStartDateTo_value"))
        	setCalendarStruct($("#searchTargetDateFrom"), $("#searchTargetDateFrom_value"))
        	setCalendarStruct($("#searchTargetDateTo"), $("#searchTargetDateTo_value"))
        	setCalendarStruct($("#searchEndDateFrom"), $("#searchEndDateFrom_value"))
        	setCalendarStruct($("#searchEndDateTo"), $("#searchEndDateTo_value"))
        })
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
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    
    <div id="search">
      <g:form controller="jobOrder" action="list" method="post">
        <table>
          <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField maxlength="17" id="searchId" name="searchId" value="${params?.searchId}"/></td>
          </tr>
          <tr>
            <td class="name">Identifier</td>
            <td class="value"><g:textField name="searchIdentifier" value="${params?.searchIdentifier}" /></td>
          </tr>
          <tr>
            <td class="name">Description</td>
            <td class="value"><g:textField name="searchDescription" value="${params?.searchDescription}"/></td>
          </tr>
          <tr>
            <td class="name">Assigned To</td>
            <td class="value"><g:select name="searchAssignedTo" noSelection="['':'']" from="${assemblers}" optionKey="identifier" value="${params?.searchAssignedTo}" /></td>
          </tr>
          <tr>
            <td class="name">Remarks</td>
            <td class="value"><g:textField name="searchRemarks" value="${params?.searchRemarks}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="['':'']" from="${statuses}" optionValue="name" value="${params?.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Start Date</td>
            <td class="value" width="900px">
              <calendar:datePicker name="searchStartDateFrom" years="2009,2030" value='${dateMap.searchStartDateFrom}' />
           	  to
           	  <calendar:datePicker name="searchStartDateTo" years="2009,2030" value='${dateMap.searchStartDateTo}'/>
           	</td>
          </tr>
          <tr>
            <td class="name">Target Date</td>
            <td class="value" width="900px">
              <calendar:datePicker name="searchTargetDateFrom" years="2009,2030" value='${dateMap.searchTargetDateFrom}' />
           	  to
           	  <calendar:datePicker name="searchTargetDateTo" years="2009,2030" value='${dateMap.searchTargetDateTo}'/>
           	</td>
          </tr>
          <tr>
            <td class="name">End Date</td>
            <td class="value" width="900px">
              <calendar:datePicker name="searchEndDateFrom" years="2009,2030" value='${dateMap.searchEndDateFrom}' />
           	  to
           	  <calendar:datePicker name="searchEndDateTo" value='${dateMap.searchEndDateTo}'/>
           	</td>
          </tr>

        </table>

        <div>
          <input type="submit" class="button" value="Search"/>
        </div>

      </g:form>
    </div>

    <div class="list">
      <table>
        <thead>
          <tr>

        <g:sortableColumn class="center" params="${params}" property="id" title="${message(code: 'jobOrder.id.label', default: 'ID')}" />
        <g:sortableColumn property="product" params="${params}" title="${message(code: 'jobOrder.product.label', default: 'Identifier')}" />
        <th>Description</th>
        <g:sortableColumn class="right" params="${params}" property="qty" title="${message(code: 'jobOrder.qty.label', default: 'Quantity')}" />
        <th class="right">Remaining</th>
        <g:sortableColumn class="center" params="${params}" property="assignedTo" title="${message(code: 'jobOrder.assignedTo.label', default: 'Assigned to')}" />
        <th>Remarks</th>
        <g:sortableColumn property="status" params="${params}" title="${message(code: 'jobOrder.status.label', default: 'Status')}" />
        <g:sortableColumn class="center" params="${params}" property="startDate" title="${message(code: 'jobOrder.startDate.label', default: 'Start Date')}" />
        <g:sortableColumn class="center" params="${params}" property="targetDate" title="${message(code: 'jobOrder.targetDate.label', default: 'Target Date')}" />
        <g:sortableColumn class="center" params="${params}" property="endDate" title="${message(code: 'jobOrder.endDate.label', default: 'End Date')}" />
        

        </tr>
        </thead>
        <tbody>
        <g:each in="${jobOrderInstanceList}" status="i" var="jobOrderInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}jobOrder/show?jobOrderId=${jobOrderInstance.id}'">

            <td id="rowJobOrderId${i}" class="center">${jobOrderInstance}</td>
            <td>${fieldValue(bean: jobOrderInstance, field: "product")}</td>
            <td>${jobOrderInstance?.product?.description}</td>
            <td class="right">${jobOrderInstance?.formatQty()}</td>
            <td class="right"><g:formatNumber number="${jobOrderInstance?.computeRemainingBalance()}" format="#,##0" /></td>
            <td>${jobOrderInstance?.assignedTo}</td>
            <td>${jobOrderInstance?.remark}</td>
            <td>${jobOrderInstance?.status}</td>
            <td class="center"><g:formatDate date="${jobOrderInstance.startDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
          <td class="center"><g:formatDate date="${jobOrderInstance.targetDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
          <td class="center"><g:formatDate date="${jobOrderInstance.endDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
          

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${jobOrderInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
