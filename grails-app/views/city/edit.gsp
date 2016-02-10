
<%@ page import="com.munix.City" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'city.label', default: 'City')}" />
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
    <g:hasErrors bean="${cityInstance}">
      <div class="errors">
        <g:renderErrors bean="${cityInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${cityInstance?.id}" />
      <g:hiddenField name="version" value="${cityInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="city.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: cityInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${cityInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="city.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: cityInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${cityInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="province"><g:message code="city.province.label" default="Province" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: cityInstance, field: 'province', 'errors')}">
          <g:select name="province.id" from="${com.munix.Province.list().sort{it.toString()}}" optionKey="id" value="${cityInstance?.province?.id}"  />
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
