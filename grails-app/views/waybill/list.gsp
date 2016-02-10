
<%@ page import="com.munix.Waybill" %>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  <g:set var="entityName" value="${message(code: 'waybill.label', default: 'Waybill')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <g:javascript src="jquery.ui.core.js" />
  <g:javascript src="jquery.ui.datepicker.js" />
  <g:javascript src="generalmethods.js" />
  <g:javascript src="calendarStructTemplate.js"/>
  <g:javascript>
	$(document).ready(function () {
	    $("#searchIdentifier").ForceNumericOnlyEnterAllowed(true)
  	    $("#searchTripTicket").ForceNumericOnlyEnterAllowed(true)
  	    setCalendarStruct($("#searchDateFrom"), $("#searchDateFrom_value"))
        setCalendarStruct($("#searchDateTo"), $("#searchDateTo_value"))
        
        if(document.getElementById("searchWithTripTicket").value=="Yes") {
			document.getElementById("searchTripTicket").style.display="";
		}
            
		$('#searchWithTripTicket').click(function() {
	    	if(document.getElementById("searchWithTripTicket").value=="Yes") {
				document.getElementById("searchTripTicket").style.display="";
			}
			else {
				document.getElementById("searchTripTicket").style.display="none";
				document.getElementById("searchTripTicketId").value=null;
			}
		});
	});
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
      <g:form controller="waybill" action="list" >
      	<table>
      	  <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField  name="searchIdentifier" id="searchIdentifier" value ="${params.searchIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Customer ID</td>
            <td class="value"><g:textField name="searchCustomerId" id="searchCustomerId" value ="${params.searchCustomerId}"/></td>
          </tr>
          <tr>
            <td class="name">Customer Name</td>
            <td class="value"><g:textField name="searchCustomerName" id="searchCustomerName" value ="${params.searchCustomerName}"/></td>
          </tr>
		  <tr>
            <td class="name">Forwarder</td>
            <td class="value"><g:textField name="searchForwarder" id="searchForwarder" value ="${params.searchForwarder}"/></td>
          </tr>
		  <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="${['':'']}" from="${['Processing','Complete','Cancelled']}" value ="${params.searchStatus}"/></td>
          </tr>         
		  <tr>
            <td class="name">Date</td>
            <td class="value" width="900px">
            	<calendar:datePicker name="searchDateFrom" years="2009,2030" id="searchDateFrom" value ="${dateMap.searchDateFrom}"/>
           		to
           		<calendar:datePicker name="searchDateTo" years="2009,2030" id="searchDateTo" value ="${dateMap.searchDateTo}"/>
           	</td>
          </tr> 
          <tr>
            <td class="name">With Trip Ticket</td>
            <td class="value" colspan="3"><g:select name="searchWithTripTicket" noSelection="${['null':'']}" from="${['Yes','No']}" value ="${params.searchWithTripTicket}"/></td>
          </tr>                    
          <tr id="searchTripTicket" style="display:none">
            <td class="name">Trip Ticket ID</td>
            <td class="value"><g:textField name="searchTripTicketId" id="searchTripTicketId" value ="${params.searchTripTicketId}"/></td>
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
        <g:sortableColumn class="center" property="id" title="ID" params="${params}"/>
        <g:sortableColumn class="center" property="tripTicket" title="Trip Ticket" params="${params}" />
        <g:sortableColumn property="customer" title="Customer" params="${params}" />
        <g:sortableColumn property="forwarder" title="Forwarder"  params="${params}"/>
        <g:sortableColumn property="status" title="Status"  params="${params}"/>
        <g:sortableColumn class="center" property="date" title="Date" params="${params}" />
        </tr>
        </thead>
        <tbody>
        <g:each in="${waybillInstanceList}" status="i" var="waybillInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}waybill/show/${waybillInstance.id}'">
            <td class="center">${waybillInstance}</td>
            <td class="center"><g:link controller="tripTicket" action="show" id="${waybillInstance?.tripTicket?.tripTicket?.id}">${fieldValue(bean: waybillInstance, field: "tripTicket")}</g:link></td>
            <td>${fieldValue(bean: waybillInstance, field: "customer")}</td>
            <td>${fieldValue(bean: waybillInstance, field: "forwarder")}</td>
            <td>${fieldValue(bean: waybillInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${waybillInstance.date}" format="MM/dd/yyyy"/></td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${waybillInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
