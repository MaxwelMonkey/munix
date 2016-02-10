<%@ page import="com.munix.CreditMemo" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'Credit Memo')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="generalmethods.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
      	    $("#searchIdentifier").ForceNumericOnlyEnterAllowed(true)
        })
    </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="['Credit Memo']" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="['Credit Memo']" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    
    <div id="search">
      <g:form controller="creditMemo" action="list" >
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
            <td class="name">Discount Type</td>
            <td class="value"><g:select name="searchDiscountType" noSelection="['':'']" from="${discountTypes}" optionKey="identifier" value="${params.searchDiscountType}" /></td>
          </tr>
          <tr>
            <td class="name">Reason</td>
            <td class="value"><g:select name="searchReason" noSelection="['':'']" from="${reasons}" optionKey="identifier" value="${params.searchReason}" /></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value" width="900px"><g:select name="searchStatus" noSelection="['':'']" from="${['Approved', 'Unapproved', 'Paid', 'Taken', 'Cancelled', 'Second Approval Pending']}" value ="${params.searchStatus}"/></td>
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

        <g:sortableColumn class="center" property="id" title="${message(code: 'creditMemo.id.label', default: 'ID')}" />
        <th width="350px"><g:message code="creditMemo.customer.label" default="Customer" /></th>
        <th width="110px"><g:message code="creditMemo.discountType.label" default="Discount Type" /></th>
        <th width="100px"><g:message code="creditMemo.reason.label" default="Reason" /></th>
        <th width="150px"><g:message code="creditMemo.amountTotal.label" default="Final amount" /></th>
        <th><g:message code="creditMemo.status.label" default="Status" /></th>
        <th><g:message code="creditMemo.date.label" default="Date" /></th>
        <th><g:message code="creditMemo.remark.label" default="Remarks" /></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}creditMemo/show/${creditMemoInstance?.id}'">

            <td id="rowCreditMemoId${i}" class="center">${creditMemoInstance}</td>
            <td>${fieldValue(bean: creditMemoInstance, field: "customer")}</td>
            <td>${fieldValue(bean: creditMemoInstance, field: "discountType")}</td>
            <td>${fieldValue(bean: creditMemoInstance, field: "reason")}</td>
			<td class="right">PHP <g:formatNumber number="${creditMemoInstance.computeTotalAmount()}" format="###,##0.00"/></td>
            <td>${fieldValue(bean: creditMemoInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${creditMemoInstance.date}" format="MM/dd/yyyy" /></td>
            <td>${fieldValue(bean: creditMemoInstance, field: "remark")}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${creditMemoInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
