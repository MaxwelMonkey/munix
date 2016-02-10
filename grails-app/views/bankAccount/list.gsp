
<%@ page import="com.munix.BankAccount" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'bankAccount.label', default: 'BankAccount')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table is="bankAccountListTable">
        <thead>
          <tr>

        <g:sortableColumn property="accountNumber" title="${message(code: 'bankAccount.accountNumber.label', default: 'Account Number')}" />
        <g:sortableColumn property="accountName" title="${message(code: 'bankAccount.accountName.label', default: 'Account Name')}" />
        <g:sortableColumn property="bank" title="${message(code: 'bankAccount.bank.label', default: 'Bank')}" />

        </tr>
        </thead>
        <tbody>
        <g:each in="${bankAccountInstanceList}" status="i" var="bankAccountInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}bankAccount/show/${bankAccountInstance.id}'">
            <td id="rowBankAccount${i}">${fieldValue(bean: bankAccountInstance, field: "accountNumber")}</td>
            <td>${fieldValue(bean: bankAccountInstance, field: "accountName")}</td>
            <td>${fieldValue(bean: bankAccountInstance, field: "bank")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${bankAccountInstanceTotal}" />
    </div>
  </div>
</body>
</html>
