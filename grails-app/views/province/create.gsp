
<%@ page import="com.munix.Province" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'province.label', default: 'Province')}" />
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
    <g:hasErrors bean="${provinceInstance}">
      <div class="errors">
        <g:renderErrors bean="${provinceInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="province.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: provinceInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${provinceInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="province.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: provinceInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${provinceInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="region"><g:message code="province.region.label" default="Region" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: provinceInstance, field: 'region', 'errors')}">
          <g:select name="region.id" from="${com.munix.Region.list().sort{it.toString()}}" optionKey="id" value="${provinceInstance?.region?.id}"  />
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
