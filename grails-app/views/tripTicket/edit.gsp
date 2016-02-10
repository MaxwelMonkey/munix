
<%@ page import="com.munix.TripTicket" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'tripTicket.label', default: 'TripTicket')}" />
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
    <g:hasErrors bean="${tripTicketInstance}">
      <div class="errors">
        <g:renderErrors bean="${tripTicketInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${tripTicketInstance?.id}" />
      <g:hiddenField name="version" value="${tripTicketInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="truck"><g:message code="tripTicket.truck.label" default="Truck" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: tripTicketInstance, field: 'truck', 'errors')}">
          <g:select name="truck.id" from="${com.munix.Truck.list().sort{it.toString()}}" optionKey="id" value="${tripTicketInstance?.truck?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="driver"><g:message code="tripTicket.driver.label" default="Driver" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: tripTicketInstance, field: 'driver', 'errors')}">
          <g:select name="driver.id" from="${com.munix.Personnel.list().sort{it.toString()}}" optionKey="id" value="${tripTicketInstance?.driver?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="helpers"><g:message code="tripTicket.helpers.label" default="Helpers" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: tripTicketInstance, field: 'helpers', 'errors')}">
          <g:select name="helpers" from="${com.munix.Personnel.list().sort{it.toString()}}" multiple="yes" optionKey="id" size="5" value="${tripTicketInstance?.helpers}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="waybill"><g:message code="tripTicket.waybill.label" default="Waybill" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: tripTicketInstance, field: 'waybills', 'errors')}">
          <g:select name="waybills" from="${waybillInstance.sort{it.toString()}}" size="10" multiple="yes" optionKey="id" value=""  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="directDelivery"><g:message code="tripTicket.directDelivery.label" default="Direct Delivery" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: tripTicketInstance, field: 'deliveries', 'errors')}">
          <g:select name="deliveries" from="${directDeliveryInstance.sort{it.toString()}}" size="10" multiple="yes" optionKey="id" value=""  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="tripTicket.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: tripTicketInstance, field: 'remark', 'errors')}">
          <g:textArea name="remark" value="${tripTicketInstance?.remark}" />
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
