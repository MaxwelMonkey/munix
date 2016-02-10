
<%@ page import="com.munix.Reason" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'reason.label', default: 'Reason')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list">Credit Memo Reason List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New Credit Memo Reason</g:link></span>
  </div>
  <div class="body">
    <h1>Show Credit Memo Reason</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="reason.identifier.label" default="Identifier" /></td>

        <td id="currencyIdentifier" valign="top" class="value">${fieldValue(bean: reasonInstance, field: "identifier")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="reason.description.label" default="Description" /></td>

        <td id="currencyDescription" valign="top" class="value">${fieldValue(bean: reasonInstance, field: "description")}</td>

        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${reasonInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
