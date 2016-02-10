
<%@ page import="com.munix.WaybillPackage" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'waybillPackage.label', default: 'WaybillPackage')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <g:javascript src="numbervalidation.js" />
  <g:javascript>
  	$(document).ready(function () {
	  	$("#qty").ForceNumericOnly(true)
	})
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${waybillPackageInstance}">
      <div class="errors">
        <g:renderErrors bean="${waybillPackageInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${waybillPackageInstance?.id}" />
      <g:hiddenField name="version" value="${waybillPackageInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="waybill"><g:message code="waybillPackage.waybill.label" default="Waybill" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: waybillPackageInstance, field: 'waybill', 'errors')}">
          <g:link controller="waybill" action="show" id="${waybillPackageInstance?.waybill?.id}">${waybillPackageInstance?.waybill}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="packaging"><g:message code="waybillPackage.packaging.label" default="Packaging" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: waybillPackageInstance, field: 'packaging', 'errors')}">
${waybillPackageInstance?.packaging}
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="waybillPackage.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: waybillPackageInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${waybillPackageInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="qty"><g:message code="waybillPackage.qty.label" default="Qty" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: waybillPackageInstance, field: 'qty', 'errors')}">
          <g:textField name="qty" value="${fieldValue(bean: waybillPackageInstance, field: 'qty')}" />
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
