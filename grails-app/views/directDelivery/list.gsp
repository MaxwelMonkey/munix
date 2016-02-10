
<%@ page import="com.munix.DirectDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
    <g:set var="entityName" value="${message(code: 'directDelivery.label', default: 'DirectDelivery')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="generalmethods.js" />
    <g:javascript src="calendarStructTemplate.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
            $("#searchIdentifier").ForceNumericOnlyEnterAllowed(true)
            $("#searchTripTicketId").ForceNumericOnlyEnterAllowed(true)
			setCalendarStruct($("#dateBeforeText"), $("#dateBeforeText_value"))
        	setCalendarStruct($("#dateAfterText"), $("#dateAfterText_value"))
        	            
            if(document.getElementById("searchWithTripTicket").value=="Yes") {
				document.getElementById("searchTripTicket").style.display="";
			}
            
           	$('#searchWithTripTicket').click(function() {
	    		if(document.getElementById("searchWithTripTicket").value=="Yes") {
					document.getElementById("searchTripTicket").style.display="";
				}else {
					document.getElementById("searchTripTicket").style.display="none";
					document.getElementById("searchTripTicketId").value=null;
			}
		});

        })
    </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="['Direct Delivery']" /></g:link></span>
  </div>
  <div class="body">	
  	<calendar:resources lang="en" theme="aqua"/>
    <h1><g:message code="default.list.label" args="['Direct Delivery']" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

     <div id="search">
      <g:form controller="directDelivery" action="list" >
        <table>
          <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField name="searchIdentifier" id="searchIdentifier" value ="${params.searchIdentifier}"/></td>
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
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" from="${['Processing', 'Cancelled', 'Complete']}" noSelection="['':'']" value ="${params.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value" width="900px"><calendar:datePicker name="dateBeforeText" years="2009,2030" id="dateBeforeText" value='${dateMap.dateBeforeText}'/>
            to
            <calendar:datePicker name="dateAfterText" years="2009,2030" id="dateAfterText" value='${dateMap.dateAfterText}'/></td>
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

        <g:sortableColumn class="center" property="id" title="${message(code: 'directDelivery.id.label', default: 'ID')}" params="${params}"/>
        <th><g:message code="directDelivery.tripTicket.label" default="Trip Ticket" /></th>
        <th><g:message code="directDelivery.customer.label" default="Customer" /></th>
        <g:sortableColumn property="status" title="${message(code: 'directDelivery.status.label', default: 'Status')}" params="${params}"/>
        <g:sortableColumn class="center" property="date" title="${message(code: 'directDelivery.date.label', default: 'Date')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${directDeliveryInstanceList}" status="i" var="directDeliveryInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directDelivery/show/${directDeliveryInstance?.id}'">

            <td class="center">${directDeliveryInstance}</td>
            <td><g:link controller="tripTicket" action="show" id="${directDeliveryInstance?.tripTicket?.tripTicket?.id}">${fieldValue(bean: directDeliveryInstance, field: "tripTicket")}</g:link></td>
            <td>${fieldValue(bean: directDeliveryInstance, field: "customer")}</td>
            <td>${fieldValue(bean: directDeliveryInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${directDeliveryInstance.date}" format="MM/dd/yyyy"/></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${directDeliveryInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
