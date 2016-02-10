
<%@ page import="com.munix.BouncedCheck" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'bouncedCheck.label', default: 'BouncedCheck')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="generalmethods.js" />
    <g:javascript src="calendarStructTemplate.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
            $("#searchIdentifier").ForceNumericOnlyEnterAllowed(true)
            setCalendarStruct($("#searchDateFrom"), $("#searchDateFrom_value"))
        	setCalendarStruct($("#searchDateTo"), $("#searchDateTo_value"))
        })
    </g:javascript>
</head>
<body>
  <calendar:resources lang="en" theme="aqua"/>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="filter"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    
    <div id="search">
      <g:form controller="bouncedCheck" action="list" >
        <table>
          <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField name="searchIdentifier" value ="${params.searchIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Customer ID</td>
            <td class="value"><g:textField name="searchCustomerId" value ="${params.searchCustomerId}"/></td>
          </tr>          
          <tr>
            <td class="name">Customer Name</td>
            <td class="value"><g:textField name="searchCustomerName" value ="${params.searchCustomerName}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="${['':'']}" from="${['Unapproved','Approved','Cancelled', 'Paid', 'Taken']}" value ="${params.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value" width="900px">
              <calendar:datePicker name="searchDateFrom" years="2009,2030" value='${dateMap.searchDateFrom}' />
           	  to
           	  <calendar:datePicker name="searchDateTo" years="2009,2030" value='${dateMap.searchDateTo}'/>
           	</td>
          </tr>
          <tr>
            <td class="name" >Bounced Check Status</td>
            <td class="value"><g:textField name="searchBouncedCheckStatus" value ="${params.searchBouncedCheckStatus}"/></td>
          </tr>
          <tr>
            <td class="name">For Redeposit</td>
            <td class="value"><g:select name="searchForRedeposit" noSelection="${['':'']}" from="${['True','False']}" value ="${params.searchForRedeposit}"/></td>
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
            <g:sortableColumn class="center" property="id" title="${message(code: 'bouncedCheck.id.label', default: 'Id')}" params="${params}"/>
            <th class="center"><g:message code="bouncedCheck.customer.label" default="Customer" /></th>
            <th class="center">Amount</th>
            <th class="center">Balance</th>
            <g:sortableColumn class="center" property="status" title="${message(code: 'bouncedCheck.status.label', default: 'Status')}" params="${params}"/>
            <g:sortableColumn class="center" property="date" title="${message(code: 'bouncedCheck.date.label', default: 'Date')}" params="${params}"/>
            <g:sortableColumn class="center" property="forRedeposit" title="${message(code: 'bouncedCheck.forRedeposit.label', default: 'For Redeposit')}" params="${params}"/>
            <g:sortableColumn class="center" property="bouncedCheckStatus" title="${message(code: 'bouncedCheck.bouncedCheckStatus.label', default: 'Bounced Check Status')}" params="${params}"/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${bouncedCheckInstanceList}" status="i" var="bouncedCheckInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}bouncedCheck/show/${bouncedCheckInstance?.id}'">
            <td id="rowBouncedCheckId${i}" class="center">${bouncedCheckInstance}</td>
            <td>${fieldValue(bean: bouncedCheckInstance, field: "customer")}</td>
            <td class="right">${bouncedCheckInstance?.formatTotal()}</td>
            <g:if test="${bouncedCheckInstance.isPaid()||bouncedCheckInstance.isCancelled()}">
                <td class="right">PHP <g:formatNumber number="0" format="###,##0.00" /></td>
            </g:if>
            <g:elseif test="${bouncedCheckInstance.forRedeposit}">
                <td class="right">PHP <g:formatNumber number="${bouncedCheckInstance?.computeNotClearedChecks()}" format="###,##0.00" /></td>
            </g:elseif>
            <g:else>
                <td class="right">PHP <g:formatNumber number="${bouncedCheckInstance?.computeProjectedDue()}" format="###,##0.00" /></td>
            </g:else>
            
            <td>${bouncedCheckInstance?.status}</td>
            <td class="right"><g:formatDate date="${bouncedCheckInstance.date}" format="MM/dd/yyyy" /></td>
            <td>${fieldValue(bean: bouncedCheckInstance, field: "forRedeposit")}</td>
            <td>${fieldValue(bean: bouncedCheckInstance, field: "bouncedCheckStatus.description")}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${bouncedCheckInstanceTotal}" />
    </div>
  </div>
</body>
</html>
