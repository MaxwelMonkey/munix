
<%@ page import="com.munix.CustomerCharge" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'customerCharge.label', default: 'CustomerCharge')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="generalmethods.js"/>
    <g:javascript src="calendarStructTemplate.js" />
    <g:javascript>
        $(document).ready(function (){
            $("#searchIdentifier").ForceNumericOnlyEnterAllowed(true)
            setCalendarStruct($("#searchDateFrom"), $("#searchDateFrom_value"))
        	setCalendarStruct($("#searchDateTo"), $("#searchDateTo_value"))
        });
    </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <calendar:resources lang="en" theme="aqua"/>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div id="search">
      <g:form controller="customerCharge" action="list" >
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
            <td class="value"><g:textField name="searchCustomerName" value ="${params.searchCustomerName}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="${['':'']}" from="${['Unpaid', 'Unapproved', 'Paid', 'Taken', 'Cancelled']}" value ="${params.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Remarks</td>
            <td class="value"><g:textArea name="searchRemarks" value ="${params.searchRemarks}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value" width="900px">
              <calendar:datePicker name="searchDateFrom" years="2009,2030" value='${dateMap.searchDateFrom}'/>
           	  to
           	  <calendar:datePicker name="searchDateTo" years="2009,2030" value='${dateMap.searchDateTo}'/>
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

        <g:sortableColumn class="center" property="id" title="${message(code: 'customerCharge.id.label', default: 'Id')}" params="${params}"/>
        <g:sortableColumn property="customer" title="${message(code: 'customerCharge.customer.label', default: 'Customer')}" params="${params}"/>
        <g:sortableColumn property="remark" title="${message(code: 'customerCharge.remark.label', default: 'Remarks')}" params="${params}"/>
        <th class="right">Amount</th>
        <g:sortableColumn property="status" title="${message(code: 'customerCharge.status.label', default: 'Status')}" params="${params}"/>
        <g:sortableColumn class="center" property="date" title="${message(code: 'customerCharge.date.label', default: 'Date')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${customerChargeInstanceList}" status="i" var="customerChargeInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}customerCharge/show/${customerChargeInstance.id}'">

          <td id="rowCustomerChargeId${i}" class="center">${customerChargeInstance}</td>
          <td>${fieldValue(bean: customerChargeInstance, field: "customer")}</td>
          <td>${fieldValue(bean: customerChargeInstance, field: "remark")}</td>
          <td class="right">PHP <g:formatNumber number="${customerChargeInstance?.computeTotalAmount()}" format="###,##0.00" /></td>
          <td>${customerChargeInstance?.status}</td>
          <td class="center"><g:formatDate date="${customerChargeInstance.date}" format="MM/dd/yyyy" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${customerChargeInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
