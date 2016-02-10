
<%@ page import="com.munix.MaterialRequisitionItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRequisitionItem.label', default: 'MaterialRequisitionItem')}" />
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
    <g:hasErrors bean="${materialRequisitionItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${materialRequisitionItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${materialRequisitionItemInstance?.id}" />
      <g:hiddenField name="version" value="${materialRequisitionItemInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="requisition"><g:message code="materialRequisitionItem.requisition.label" default="Requisition" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: materialRequisitionItemInstance, field: 'requisition', 'errors')}">
          <g:link action="show" controller="materialRequisition" id="${materialRequisitionItemInstance?.requisition?.id}" >${materialRequisitionItemInstance?.requisition}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="component"><g:message code="materialRequisitionItem.component.label" default="Component" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: materialRequisitionItemInstance, field: 'component', 'errors')}">
${materialRequisitionItemInstance?.component}
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="qty"><g:message code="materialRequisitionItem.qty.label" default="Qty" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: materialRequisitionItemInstance, field: 'qty', 'errors')}">
          <g:textField name="qty" value="${fieldValue(bean: materialRequisitionItemInstance, field: 'qty')}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
