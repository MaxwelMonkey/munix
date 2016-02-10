<%@ page import="com.munix.CheckPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  <g:set var="entityName" value="${message(code: 'checkPayment.label', default: 'CheckPayment')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <g:javascript src="calendarStructTemplate.js" />
  <g:javascript>
      var $ = jQuery.noConflict()
      $(document).ready(function (){
      	setCalendarStruct($("#dateBeforeText"), $("#dateBeforeText_value"))
        setCalendarStruct($("#dateAfterText"), $("#dateAfterText_value"))
      })
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
  	<calendar:resources lang="en" theme="aqua"/>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div id="search">
      <g:form controller="checkPayment" action="list" >
        <table>
          <tr>
            <td class="name" width="400px">Customer ID</td>
            <td class="value"><g:textField name="searchCustomerId" id="searchCustomerId" value ="${params.searchCustomerId}"/></td>
          </tr>
          <tr>
            <td class="name">Customer Name</td>
            <td class="value"><g:textField name="searchCustomerName" id="searchCustomerName" value ="${params.searchCustomerName}"/></td>
          </tr>          
          <tr>
            <td class="name">Check No.</td>
            <td class="value"><g:textField name="searchCheckNumber" id="searchCheckNumber" value ="${params.searchCheckNumber}"/></td>
          </tr>
          <tr>
            <td class="name">Bank</td>
            <td class="value"><g:select name="searchBank" from="${com.munix.Bank.list()}" noSelection="['':'']" value ="${params.searchBank}" optionKey="id"/></td>
          </tr>
          <tr>
            <td class="name">Branch</td>
            <td class="value"><g:textField name="searchBranch" id="searchBranch" value ="${params.searchBranch}"/></td>
          </tr>
          <tr>
            <td class="name">Warehouse</td>
            <td class="value"><g:select name="searchWarehouse" from="${com.munix.CheckWarehouse.list()}" noSelection="['':'']" value ="${params.searchWarehouse}" optionKey="id"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" from="${['Pending', 'Bounced', 'Deposited', 'For redeposit']}" noSelection="['':'']" value ="${params.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value" width="900px"><calendar:datePicker name="dateBeforeText" id="dateBeforeText" years="2009,2030" value='${dateMap.dateBeforeText}'/>
            to
            <calendar:datePicker name="dateAfterText" id="dateAfterText" years="2009,2030" value='${dateMap.dateAfterText}'/></td>
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

        <g:sortableColumn property="customer" title="${message(code: 'checkPayment.customer.label', default: 'Customer')}" params="${params}"/>
        <g:sortableColumn class="center" property="date" title="${message(code: 'checkPayment.date.label', default: 'Check Date')}" params="${params}"/>
        <th><g:message code="checkPayment.bank.label" default="Bank" params="${params}"/></th>
        <g:sortableColumn property="checkNumber" title="${message(code: 'checkPayment.checkNumber.label', default: 'Check Number')}" params="${params}"/>
        <g:sortableColumn class="right" property="amount" title="${message(code: 'checkPayment.amount.label', default: 'Amount')}" params="${params}"/>
        <th><g:message code="checkPayment.warehouse.label" default="Warehouse" params="${params}"/></th>
        <g:sortableColumn property="status" title="${message(code: 'checkPayment.status.label', default: 'Status')}" params="${params}"/>
        <g:sortableColumn property="directPaymentItem.directPayment" title="${message(code: 'checkPayment.directPaymentItem.directPayment.label', default: 'Direct Payment')}" params="${params}"/>
        <g:sortableColumn property="checkDeposit" title="${message(code: 'checkPayment.checkDeposit.label', default: 'Check Deposit')}" params="${params}"/>
        <g:sortableColumn property="bouncedCheck" title="${message(code: 'checkPayment.bouncedCheck.label', default: 'Bounced Check')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${checkPaymentInstanceList}" status="i" var="checkPaymentInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directPayment/show/${checkPaymentInstance?.directPaymentItem?.directPayment?.id}'">

            <td id="rowCheckPayment${i}">${fieldValue(bean: checkPaymentInstance, field: "customer")}</td>
            <td class="center"><g:formatDate date="${checkPaymentInstance.date}" format="MM/dd/yyyy" /></td>
            <td>${checkPaymentInstance?.bank?.identifier} - ${checkPaymentInstance?.branch}</td>
            <td>${fieldValue(bean: checkPaymentInstance, field: "checkNumber")}</td>
            <td class="right">${checkPaymentInstance?.formatAmount()}</td>
            <td>${checkPaymentInstance.warehouse}</td>
            <td>${checkPaymentInstance.status}</td>
            <td><g:link elementId="showDirectPayment" controller="directPayment" action="show" id="${checkPaymentInstance?.directPaymentItem?.directPayment?.id}">${checkPaymentInstance?.directPaymentItem?.directPayment}</g:link></td>
            
            <td>
            	<g:each in="${checkPaymentInstance.checkDeposits}" var="checkDeposit">
            		<g:link controller="checkDeposit" action="show" id="${checkDeposit?.id}">${checkDeposit} <g:if test="${((checkDeposit?.depositDate.getTime() - checkPaymentInstance?.date.getTime())/1000/60/60/24)>5}"><span style="color:red"></g:if>(<g:formatDate date="${checkDeposit?.depositDate}" format="MM/dd/yyyy" />)<g:if test="${((checkDeposit?.depositDate.getTime() - checkPaymentInstance?.date.getTime())/1000/60/60/24)>5}"></span></g:if></g:link>
            	</g:each>
            </td>
            <td>
            	<g:each in="${checkPaymentInstance.bouncedChecks}" var="bouncedCheck">
            		<g:link controller="bouncedCheck" action="show" id="${bouncedCheck?.id}">${bouncedCheck} - ${bouncedCheck.bouncedCheckStatus?.description}</g:link><br>
            	</g:each>
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${checkPaymentInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
