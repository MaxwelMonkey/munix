
<%@ page import="com.munix.TripTicket" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'tripTicket.label', default: 'TripTicket')}" />
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
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="tripTicket.id.label" default="Id" /></td>
        <td valign="top" class="value">${tripTicketInstance}</td>
        <td valign="top" class="name"><g:message code="tripTicket.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${tripTicketInstance?.date}" format="MM/dd/yyyy"/></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="tripTicket.truck.label" default="Truck" /></td>
        <td valign="top" class="value">${tripTicketInstance?.truck?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="tripTicket.driver.label" default="Driver" /></td>
        <td valign="top" class="value">${tripTicketInstance?.driver?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="tripTicket.helpers.label" default="Helpers" /></td>
        <td valign="top" class="value">
          <ul>
            <g:each in="${tripTicketInstance?.helpers?.sort{it.toString()}}" var="i">
              <li>${i}</li>
            </g:each>
          </ul>
        </td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="tripTicket.remark.label" default="Remarks" /></td>
        <td valign="top" class="value">${tripTicketInstance?.remark}</td>
        <td valign="top" class="name"><g:message code="tripTicket.preparedBY.label" default="Prepared By" /></td>
        <td valign="top" class="value">${tripTicketInstance?.preparedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="tripTicket.status.label" default="Status" /></td>
        <td valign="top" class="value">${tripTicketInstance?.status}</td>
        <td valign="top" class="name"><g:message code="tripTicket.markAsCompleteBy.label" default="Completed By" /></td>
        <td valign="top" class="value">${tripTicketInstance?.markAsCompleteBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"></td>
        <td valign="top" class="value"></td>
        <td valign="top" class="name"><g:message code="tripTicket.cancelledBy.label" default="Cancelled By" /></td>
        <td valign="top" class="value">${tripTicketInstance?.cancelledBy?.encodeAsHTML()}</td>
        </tr>


        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Items</h2>
        <thead>
          <tr>
            <th>ID</th>
            <th>Type</th>
            <th>Customer</th>
            <th>Forwarder</th>
            <th class="right">Priority</th>
        <g:if test="${tripTicketInstance?.status != 'Complete'}">
          <th></th>
        </g:if>
        </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${tripTicketInstance.items.sort{it.priority}}" var="i" status="colors">
          <g:if test="${i?.type == 'Waybill'}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}waybill/show/${i.item.id}'">
          </g:if>
          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directDelivery/show/${i.item.id}'">
          </g:else>
          <td>${i?.item}</td>
          <td>${i?.type}</td>
          <g:if test="${i?.type == 'Waybill'}">
            <td>${i?.item?.customer}</td>
            <td>${i?.item?.forwarder}</td>
          </g:if>
          <g:else>
            <td>${i?.item?.customer}</td>
            <td>${i?.item?.customer?.forwarder}</td>
          </g:else>
          <g:if test="${tripTicketInstance?.status != 'Complete'}">
            <td class="right"><g:link controller="tripTicketItem" action="edit" id="${i?.id}">${i?.formatPriority()}</g:link></td>
          </g:if>
          <g:else>
            <td class="right">${i?.priority}</td>
          </g:else>
          <g:if test="${tripTicketInstance?.status != 'Complete'}">
            <td class="center"><g:link params="${[tripTicketId:tripTicketInstance?.id]}" controller="tripTicket" action="removeItem" id="${i?.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">X</g:link></td>
          </g:if>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <g:if test="${tripTicketInstance?.status != 'Complete' && tripTicketInstance?.status != 'Cancelled'}">
      <div class="buttons">
        <g:form>
          <g:hiddenField name="id" value="${tripTicketInstance?.id}" />
          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
          <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
          <span class="button"><g:actionSubmit class="markAsComplete" action="markAsComplete" value="${message(code: 'default.button.markAsComplete.label', default: 'Mark as Complete')}" onclick="return confirm('${message(code: 'default.button.markAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
          <span class="button"><g:link class="print" action="print" target="blank" id="${tripTicketInstance?.id}" >Print</g:link></span>
        </g:form>
      </div>
    </g:if>
    <g:if test="${tripTicketInstance?.status == 'Complete' && tripTicketInstance?.status != 'Cancelled'}">
      <div class="buttons">
    	<g:form>
    		<g:hiddenField name="id" value="${tripTicketInstance?.id}" />
    		<span class="button"><g:actionSubmit class="markAsComplete" action="unapprove" value="Unapprove" onclick="return confirm('${message(code: 'default.button.markAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
    	</g:form>
      </div>
    </g:if>
  </div>
</body>
</html>
