
<%@ page import="com.munix.SalesAgent" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesAgent.label', default: 'SalesAgent')}" />
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
            <td valign="top" class="name"><g:message code="salesAgent.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: salesAgentInstance, field: "identifier")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.name.label" default="Name" /></td>
        <td valign="top" class="value">${salesAgentInstance?.formatName()}</td>
        </tr>

        
        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.address.label" default="Address" /></td>
        <td valign="top" class="value">${salesAgentInstance?.formatAddress()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.landline.label" default="Landline" /></td>
        <td valign="top" class="value">${fieldValue(bean: salesAgentInstance, field: "landline")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.mobile.label" default="Mobile" /></td>
        <td valign="top" class="value">${fieldValue(bean: salesAgentInstance, field: "mobile")}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.email.label" default="Email" /></td>
        <td valign="top" class="value">${fieldValue(bean: salesAgentInstance, field: "email")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.skype.label" default="Skype" /></td>
        <td valign="top" class="value">${fieldValue(bean: salesAgentInstance, field: "skype")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.yahoo.label" default="Yahoo" /></td>
        <td valign="top" class="value">${fieldValue(bean: salesAgentInstance, field: "yahoo")}</td>
        </tr>
        
        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesAgent.commission.label" default="Commission" /></td>
        <td valign="top" class="value">${fieldValue(bean: salesAgentInstance, field: "commission")}</td>
        </tr>

        </tbody>
      </table>
    </div>
    <g:ifAnyGranted role="ROLE_ACCOUNTING">
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${salesAgentInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
    </g:ifAnyGranted>
  </div>
</body>
</html>
