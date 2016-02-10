
<%@ page import="com.munix.DirectDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directDelivery.label', default: 'DirectDelivery')}" />
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
    <g:hasErrors bean="${directDeliveryInstance}">
      <div class="errors">
        <g:renderErrors bean="${directDeliveryInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${directDeliveryInstance?.id}" />
      <g:hiddenField name="version" value="${directDeliveryInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="deliveries"><g:message code="directDelivery.deliveries.label" default="Deliveries" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directDeliveryInstance, field: 'deliveries', 'errors')}">
          <g:select name="deliveries" from="${deliveryInstance?.sort{it.id}}" multiple="yes" optionKey="id" size="5" value="${directDeliveryInstance?.deliveries}" />
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
