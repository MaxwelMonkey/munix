
<%@ page import="com.munix.BankAccount" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'bankAccount.label', default: 'BankAccount')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${bankAccountInstance}">
      <div class="errors">
        <g:renderErrors bean="${bankAccountInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${bankAccountInstance?.id}" />
      <g:hiddenField name="version" value="${bankAccountInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="accountName"><g:message code="bankAccount.accountName.label" default="Account Name" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: bankAccountInstance, field: 'accountName', 'errors')}">
          <g:textField name="accountName" value="${bankAccountInstance?.accountName}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountNumber"><g:message code="bankAccount.accountNumber.label" default="Account Number" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: bankAccountInstance, field: 'accountNumber', 'errors')}">
          <g:textField name="accountNumber" value="${bankAccountInstance?.accountNumber}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="bank"><g:message code="bankAccount.bank.label" default="Bank" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: bankAccountInstance, field: 'bank', 'errors')}">
          <g:select name="bank.id" from="${com.munix.Bank.list().sort{it.toString()}}" optionKey="id" value="${bankAccountInstance?.bank?.id}"  />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
