
<%@ page import="com.munix.TripTicket" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  <g:set var="entityName" value="${message(code: 'tripTicket.label', default: 'TripTicket')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <g:javascript src="generalmethods.js" />
  <g:javascript src="calendarStructTemplate.js" />
  <g:javascript>
      $(document).ready(function(){
			$("#id").ForceNumericOnlyEnterAllowed(true)
			setCalendarStruct($("#dateFrom"), $("#dateFrom_value"))
        	setCalendarStruct($("#dateTo"), $("#dateTo_value"))
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
      <g:form controller="tripTicket" action="list" >
        <table>
          <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField name="id" id="id" value="${params.id}"/></td>
          </tr>
          <tr>
            <td class="name">Truck</td>
            <td class="value"><g:textField name="truck" id="truck" value="${params.truck}"/></td>
          </tr>
          <tr>
            <td class="name">Driver</td>
            <td class="value"><g:textField name="driver" id="driver" value="${params.driver}"/></td>
          </tr>
          <tr>
            <td class="name">Helper</td>
            <td class="value"><g:textField name="helper" id="helper" value="${params.helper}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="status" noSelection="${['':'']}" from="${['Processing','Complete','Cancelled']}" id="status" value="${params.status}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value" width="900px">
            	<calendar:datePicker name="dateFrom" years="2009,2030" id="dateFrom" value ="${dateMap.dateFrom}"/>
           		to
           		<calendar:datePicker name="dateTo" years="2009,2030" id="dateTo" value ="${dateMap.dateTo}"/>
           	</td>
          </tr> 
        </table>
        <div>
          <input type="submit" class="button" value="search"/>
        </div>

      </g:form>
    </div>
    <div class="list">
      <table>
        <thead>
          <tr>
        <g:sortableColumn class="center" property="id" title="ID" params="${params}" />
        <g:sortableColumn property="truck" title="Truck" params="${params}" />
        <g:sortableColumn property="driver" title="Driver" params="${params}"/>
        <g:sortableColumn property="helper" title="Helpers" params="${params}"/>
        <g:sortableColumn property="remark" title="Remarks" params="${params}"/>
        <g:sortableColumn property="status" title="Status" params="${params}"/>
        <g:sortableColumn class="center" property="date" title="Date" params="${params}" />
        </tr>
        </thead>
        <tbody>
        <g:each in="${tripTicketInstanceList}" status="i" var="tripTicketInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}tripTicket/show/${tripTicketInstance.id}'">

            <td class="center">${tripTicketInstance}</td>
            <td>${fieldValue(bean: tripTicketInstance, field: "truck")}</td>
            <td>${fieldValue(bean: tripTicketInstance, field: "driver")}</td>
            <td>
            	<g:each in="${tripTicketInstance?.helpers}" var="helpers" status="s">
            		${(s==0) ? helpers : ', '+helpers}
            	</g:each>
            </td>
            <td>${fieldValue(bean: tripTicketInstance, field: "remark")}</td>
            <td>${fieldValue(bean: tripTicketInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${tripTicketInstance.date}" format="MM/dd/yyyy"/></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${tripTicketInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
