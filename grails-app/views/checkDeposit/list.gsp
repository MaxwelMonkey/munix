<%@ page import="com.munix.CheckDeposit" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'checkDeposit.label', default: 'CheckDeposit')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="generalmethods.js" />
    <g:javascript src="calendarStructTemplate.js"/>
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
            $("#searchIdentifier").ForceNumericOnlyEnterAllowed(true)
            setCalendarStruct($("#searchDepositDateFrom"), $("#searchDepositDateFrom_value"))
        	setCalendarStruct($("#searchDepositDateTo"), $("#searchDepositDateTo_value"))
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
      <g:form controller="checkDeposit" action="list" >
        <table>
          <tr>
            <td class="name">ID</td>
            <td class="value"><g:textField name="searchIdentifier" value ="${params.searchIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Account</td>
            <td class="value"><g:textField name="searchAccount" value ="${params.searchAccount}"/></td>
          </tr>
          <tr>
            <td class="name" width="250px">Bills Purchase</td>
            <td class="value"><g:select name="searchBillsPurchase" noSelection="${['':'']}" from="${['True','False']}" value ="${params.searchBillsPurchase}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="${['':'']}" from="${['Cleared','Unapproved','Cancelled']}" value ="${params.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Deposit Date</td>
            <td class="value" width="850px">
              <calendar:datePicker name="searchDepositDateFrom" years="2009,2030" value='${dateMap.searchDepositDateFrom}' />
           	  to
           	  <calendar:datePicker name="searchDepositDateTo" years="2009,2030" value='${dateMap.searchDepositDateTo}'/>
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

        <g:sortableColumn property="id" title="${message(code: 'checkDeposit.id.label', default: 'Id')}" class="center" params="${params}"/>
        <th><g:message code="checkDeposit.account.label" default="Account" /></th>
        <g:sortableColumn property="billsPurchase" title="${message(code: 'checkDeposit.billsPurchase.label', default: 'Bills Purchase')}" params="${params}"/>
        <th><g:message code="checkDeposit.status.label" default="Status" /></th>
        <g:sortableColumn property="depositDate" class="center" title="${message(code: 'checkDeposit.depositDate.label', default: 'Deposit Date')}" params="${params}"/>
        <th>Amount</th>
        <th>Print Count</th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${checkDepositInstanceList}" status="i" var="checkDepositInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}checkDeposit/show/${checkDepositInstance?.id}'">

          <td id="rowCheckDepositId${i}" class="center">${checkDepositInstance}</td>
          <td>${fieldValue(bean: checkDepositInstance, field: "account")}</td>
          <td><g:formatBoolean boolean="${checkDepositInstance.billsPurchase}" /></td>
          <td>${checkDepositInstance?.status}</td>
          <td class="center"><g:formatDate date="${checkDepositInstance.depositDate}" format="MM/dd/yyyy"/></td>
          <td class="right">PHP <g:formatNumber number="${checkDepositInstance.computeTotal()}" format="###,##0.00"/></td>
		  <td class="right">${checkDepositInstance?.printLogs?.size()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${checkDepositInstanceTotal}" />
    </div>
  </div>
</body>
</html>
