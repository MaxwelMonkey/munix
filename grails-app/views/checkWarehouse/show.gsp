
<%@ page import="com.munix.CheckWarehouse" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkWarehouse.label', default: 'CheckWarehouse')}" />
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
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="checkWarehouse.identifier.label" default="Identifier" /></td>
        <td id="checkWarehouseIdentifier" valign="top" class="value">${fieldValue(bean: checkWarehouseInstance, field: "identifier")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="checkWarehouse.description.label" default="Description" /></td>
        <td id="checkWarehouseDescription" valign="top" class="value">${fieldValue(bean: checkWarehouseInstance, field: "description")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="checkWarehouse.isDefault.label" default="Default" /></td>
        <td valign="top" class="value"><g:formatBoolean boolean="${checkWarehouseInstance?.isDefault}" /></td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${checkWarehouseInstance?.id}" />
        <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
        </g:ifAnyGranted>
      </g:form>
    </div>
  </div>
</body>
</html>
