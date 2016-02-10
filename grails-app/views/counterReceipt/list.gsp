
<%@ page import="com.munix.CounterReceipt" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  <g:set var="entityName" value="${message(code: 'counterReceipt.label', default: 'CounterReceipt')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
	<g:javascript src="calendarStructTemplate.js"/>
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
            setCalendarStruct($("#collectionDateFrom"), $("#collectionDateFrom_value"))
        	setCalendarStruct($("#collectionDateTo"), $("#collectionDateTo_value"))
            setCalendarStruct($("#counterDateFrom"), $("#counterDateFrom_value"))
        	setCalendarStruct($("#counterDateTo"), $("#counterDateTo_value"))
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
      <g:form controller="counterReceipt" action="list" >
        <table>
          <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField name="id" id="id" value="${params.id}"/></td>
          </tr>
          <tr>
            <td class="name">Customer ID</td>
            <td class="value"><g:textField name="customerId" id="customerId" value="${params.customerId}"/></td>
          </tr>          
          <tr>
            <td class="name">Customer Name</td>
            <td class="value"><g:textField name="customerName" id="customerName" value="${params.customerName}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="status" noSelection="${['':'']}" from="${['Unapproved','Approved','Cancelled']}" id="status"  value="${params.status}"/></td>
          </tr>
          <tr>
            <td class="name">Date</td>
            <td class="value" width="900px"><calendar:datePicker name="dateFrom" years="2009,2030" id="dateFrom" value ="${dateMap.dateFrom}"/>
            to
            <calendar:datePicker name="dateTo" years="2009,2030" id="dateTo" value ="${dateMap.dateTo}"/>
            </td>
          </tr>
          <tr>
            <td class="name">Collection Date</td>
            <td class="value"><g:select name="collectionDate" noSelection="${['':'']}" from="${['Yes','No']}" id="collectionDate" value="${params.collectionDate}"/></td>
          </tr>
          <tr>
            <td class="name">Counter Date</td>
            <td class="value"><g:select name="counterDate" noSelection="${['':'']}" from="${['Yes','No']}" id="counterDate" value="${params.counterDate}"/></td>
          </tr>
          <tr>
            <td class="name">Collection Date</td>
            <td class="value">
            <calendar:datePicker name="collectionDateFrom" years="2009,2030" id="collectionDateFrom" value ="${dateMap.collectionDateFrom}"/>
           	to
           	<calendar:datePicker name="collectionDateTo" years="2009,2030" id="collectionDateTo" value ="${dateMap.collectionDateTo}"/>
           	</td>
          </tr>
          <tr>
            <td class="name">Counter Date</td>
            <td class="value">
           	<calendar:datePicker name="counterDateFrom" years="2009,2030" id="counterDateFrom" value ="${dateMap.counterDateFrom}"/>
           	to
           	<calendar:datePicker name="counterDateTo" years="2009,2030" id="counterDateTo" value ="${dateMap.counterDateTo}"/>
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

        <g:sortableColumn class="center" property="id" title="${message(code: 'counterReceipt.id.label', default: 'Id')}" />
        <g:sortableColumn property="customer" title="${message(code: 'counterReceipt.customer.label', default: 'Customer')}" />
        <g:sortableColumn property="status" title="${message(code: 'counterReceipt.status.label', default: 'Status')}" />
        <g:sortableColumn class="center" property="date" title="${message(code: 'counterReceipt.date.label', default: 'Date Created')}" />
        <g:sortableColumn class="center" property="collectionDate" title="${message(code: 'counterReceipt.collectionDate.label', default: 'Collection Date')}" />
        <g:sortableColumn class="center" property="counterDate" title="${message(code: 'counterReceipt.counterDate.label', default: 'Counter Date')}" />
        <th class="center">Total Due</th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${counterReceiptInstanceList}" status="i" var="counterReceiptInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}counterReceipt/show/${counterReceiptInstance.id}'">

            <td id="rowCounterReceiptId${i}" class="center">${counterReceiptInstance}</td>
            <td>${fieldValue(bean: counterReceiptInstance, field: "customer")}</td>
            <td>${fieldValue(bean: counterReceiptInstance, field: "status")}</td>
            <td class="center" ><g:formatDate date="${counterReceiptInstance.date}" format="MM/dd/yyyy"/></td>
            <td class="center" ><g:formatDate date="${counterReceiptInstance.collectionDate}" format="MM/dd/yyyy"/></td>
            <td class="center" ><g:formatDate date="${counterReceiptInstance.counterDate}" format="MM/dd/yyyy"/></td>
            <td class="right" ><g:formatNumber number="${counterReceiptInstance.computeInvoicesAmountDueTotal()}" format="###,##0.00"/></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${counterReceiptInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
