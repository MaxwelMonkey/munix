
<%@ page import="com.munix.DirectDeliveryPackage" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="numbervalidation.js" />
  <g:javascript>
  	$(document).ready(function () {
	  	$("#qty").ForceNumericOnly(true)
	})
  </g:javascript>
</head>
<body>
  <g:set value="${com.munix.DirectDelivery.get(params.id)}" var="directDeliveryInstance" />
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${directDeliveryPackageInstance}">
      <div class="errors">
        <g:renderErrors bean="${directDeliveryPackageInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>
            
            <tr class="prop">
              <td valign="top" class="name">
                <label for="directDelivery"><g:message code="directDeliveryPackage.directDelivery.label" default="Direct Delivery" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directDeliveryPackageInstance, field: 'directDelivery', 'errors')}">
          <g:link controller="directDelivery" action="show" id="${directDeliveryInstance?.id}">${directDeliveryInstance}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="packaging"><g:message code="directDeliveryPackage.packaging.label" default="Packaging" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directDeliveryPackageInstance, field: 'packaging', 'errors')}">
          <g:select name="packaging.id" from="${com.munix.Packaging.list()}" optionKey="id" value="${directDeliveryPackageInstance?.packaging?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="directDeliveryPackage.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directDeliveryPackageInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${directDeliveryPackageInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="qty"><g:message code="directDeliveryPackage.qty.label" default="Quantity" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directDeliveryPackageInstance, field: 'qty', 'errors')}">
          <g:textField name="qty" id="qty" value="${fieldValue(bean: directDeliveryPackageInstance, field: 'qty')}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <g:hiddenField name="id" value="${params?.id}" />
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
