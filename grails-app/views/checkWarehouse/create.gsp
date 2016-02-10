
<%@ page import="com.munix.CheckWarehouse" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkWarehouse.label', default: 'CheckWarehouse')}" />
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
    <g:hasErrors bean="${checkWarehouseInstance}">
      <div class="errors">
        <g:renderErrors bean="${checkWarehouseInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="checkWarehouse.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: checkWarehouseInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${checkWarehouseInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="checkWarehouse.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkWarehouseInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${checkWarehouseInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="isDefault"><g:message code="checkWarehouse.isDefault.label" default="Make Default" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkWarehouseInstance, field: 'isDefault', 'errors')}">
          <g:checkBox name="isDefault" value="${checkWarehouseInstance?.isDefault}" />
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
