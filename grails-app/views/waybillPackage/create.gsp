
<%@ page import="com.munix.WaybillPackage" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'waybillPackage.label', default: 'WaybillPackage')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="numbervalidation.js" />
  <g:javascript>
  	$(document).ready(function () {
	  	$("#qty").ForceNumericOnly(true)
	})
  </g:javascript>
</head>
<body>
<g:set var="waybillInstance" value="${com.munix.Waybill.get(params.id)}" />

<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${waybillPackageInstance}">
    <div class="errors">
      <g:renderErrors bean="${waybillPackageInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="waybill"><g:message code="waybillPackage.waybill.label" default="Waybill" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: waybillPackageInstance, field: 'waybill', 'errors')}">
          <g:link controller="waybill" action="show" id="${waybillInstance?.id}">${waybillInstance}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="packaging"><g:message code="waybillPackage.packaging.label" default="Packaging" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: waybillPackageInstance, field: 'packaging', 'errors')}">
<g:select name="packaging.id" from="${com.munix.Packaging.list().sort{it.toString()}}" optionKey="id" value="${waybillPackageInstance?.packaging?.id}"  />
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
      <input type="hidden" name="id" value="${waybillInstance?.id}" />
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
