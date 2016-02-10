
<%@ page import="com.munix.Personnel" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'personnel.label', default: 'Personnel')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="personnel.identifier.label" default="Identifier" /></td>
        <td id="identifier" valign="top" class="value">${fieldValue(bean: personnelInstance, field: "identifier")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="personnel.lastName.label" default="Last Name" /></td>
        <td id="lastName" valign="top" class="value">${fieldValue(bean: personnelInstance, field: "lastName")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="personnel.firstName.label" default="First Name" /></td>
        <td id="firstName" valign="top" class="value">${fieldValue(bean: personnelInstance, field: "firstName")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="personnel.type.label" default="Type" /></td>
        <td id="personnelType" valign="top" class="value">${personnelInstance?.type?.encodeAsHTML()}</td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${personnelInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
