
<%@ page import="com.munix.Forwarder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'forwarder.label', default: 'Forwarder')}" />
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
    <g:hasErrors bean="${forwarderInstance}">
      <div class="errors">
        <g:renderErrors bean="${forwarderInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="forwarder.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${forwarderInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="forwarder.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${forwarderInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="contact"><g:message code="forwarder.contact.label" default="Contact" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'contact', 'errors')}">
          <g:textField name="contact" value="${forwarderInstance?.contact}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="contactPosition"><g:message code="forwarder.contactPosition.label" default="Position" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'contactPosition', 'errors')}">
          <g:textField name="contactPosition" value="${forwarderInstance?.contactPosition}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="street"><g:message code="forwarder.street.label" default="Street" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'street', 'errors')}">
          <g:textField name="street" value="${forwarderInstance?.street}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="city"><g:message code="forwarder.city.label" default="City" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'city', 'errors')}">
          <g:select name="city.id" from="${com.munix.City.list().sort{it.toString()}}" optionKey="id" value="${forwarderInstance?.city?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="zip"><g:message code="forwarder.zip.label" default="Zip" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'zip', 'errors')}">
          <g:textField name="zip" value="${forwarderInstance?.zip}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="landline"><g:message code="forwarder.landline.label" default="Landline" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: forwarderInstance, field: 'landline', 'errors')}">
          <g:textField name="landline" value="${forwarderInstance?.landline}" />
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
