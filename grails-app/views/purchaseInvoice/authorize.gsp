
<%@ page import="com.munix.PurchaseInvoice" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
	 <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>

</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div>
        <div>
          <g:form >
            <p>
              <label for='username'>Username</label><br />
              <input type='text' name='username' id='username' value="${user}"/><br />
            </p>
            <p>
              <label for='password'>Password</label><br />
              <input type='password'  name='password' id='password' value="${pass}"/><br />
            </p>
            <p>
              <g:actionSubmit value="Validate" action="validate"/>
            </p>
          </g:form>
        </div>
      </div>
  </div>
</body>
</html>
