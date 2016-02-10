
<%@ page import="com.munix.Forwarder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'forwarder.label', default: 'Forwarder')}" />
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
            <td valign="top" class="name"><g:message code="forwarder.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: forwarderInstance, field: "identifier")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="forwarder.description.label" default="Description" /></td>
        <td valign="top" class="value">${fieldValue(bean: forwarderInstance, field: "description")}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="forwarder.contact.label" default="Contact" /></td>
        <td valign="top" class="value">${forwarderInstance?.contact}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="forwarder.contactPosition.label" default="Position" /></td>
        <td valign="top" class="value">${forwarderInstance?.contactPosition}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="forwarder.address.label" default="Address" /></td>
        <td valign="top" class="value">${forwarderInstance?.formatAddress()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="forwarder.landline.label" default="Landline" /></td>
        <td valign="top" class="value">${fieldValue(bean: forwarderInstance, field: "landline")}</td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${forwarderInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
