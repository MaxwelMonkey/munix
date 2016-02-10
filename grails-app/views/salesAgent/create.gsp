
<%@ page import="com.munix.SalesAgent" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesAgent.label', default: 'SalesAgent')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${salesAgentInstance}">
      <div class="errors">
        <g:renderErrors bean="${salesAgentInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="salesAgent.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${salesAgentInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="lastName"><g:message code="salesAgent.lastName.label" default="Last Name" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'lastName', 'errors')}">
          <g:textField name="lastName" value="${salesAgentInstance?.lastName}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="firstName"><g:message code="salesAgent.firstName.label" default="First Name" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'firstName', 'errors')}">
          <g:textField name="firstName" value="${salesAgentInstance?.firstName}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="street"><g:message code="salesAgent.street.label" default="Street" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'street', 'errors')}">
          <g:textField name="street" value="${salesAgentInstance?.street}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="city"><g:message code="salesAgent.city.label" default="City" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'city', 'errors')}">
          <g:select name="city.id" from="${com.munix.City.list().sort{it.toString()}}" optionKey="id" value="${salesAgentInstance?.city?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="zip"><g:message code="salesAgent.zip.label" default="Zip" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'zip', 'errors')}">
          <g:textField name="zip" value="${salesAgentInstance?.zip}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="landline"><g:message code="salesAgent.landline.label" default="Landline" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'landline', 'errors')}">
          <g:textField name="landline" value="${salesAgentInstance?.landline}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="mobile"><g:message code="salesAgent.mobile.label" default="Mobile" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'mobile', 'errors')}">
          <g:textField name="mobile" value="${salesAgentInstance?.mobile}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="email"><g:message code="salesAgent.email.label" default="Email" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'email', 'errors')}">
          <g:textField name="email" value="${salesAgentInstance?.email}" />
          </td>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="skype"><g:message code="salesAgent.skype.label" default="Skype" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'skype', 'errors')}">
          <g:textField name="skype" value="${salesAgentInstance?.skype}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="yahoo"><g:message code="salesAgent.yahoo.label" default="Yahoo" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'yahoo', 'errors')}">
          <g:textField name="yahoo" value="${salesAgentInstance?.yahoo}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="commission"><g:message code="salesAgent.commission.label" default="Commission" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesAgentInstance, field: 'commission', 'errors')}">
          	  <g:textField name="commission" value="${salesAgentInstance?.commission}" />
          	</td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
