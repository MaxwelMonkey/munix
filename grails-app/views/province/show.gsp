
<%@ page import="com.munix.Province" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'province.label', default: 'Province')}" />
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
            <td valign="top" class="name"><g:message code="province.identifier.label" default="Identifier" /></td>
        <td id="identifier" valign="top" class="value">${fieldValue(bean: provinceInstance, field: "identifier")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="province.description.label" default="Description" /></td>
        <td id="description" valign="top" class="value">${fieldValue(bean: provinceInstance, field: "description")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="province.region.label" default="Region" /></td>
        <td id="region" valign="top" class="value"><g:link controller="region" action="show" id="${provinceInstance?.region?.id}">${provinceInstance?.region?.encodeAsHTML()}</g:link></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="province.cities.label" default="Cities" /></td>

        <td valign="top" style="text-align: left;" class="value">
          <ul>
            <g:each in="${provinceInstance.cities}" var="c">
              <li><g:link controller="city" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
            </g:each>
          </ul>
        </td>

        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${provinceInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
