
<%@ page import="com.munix.Region" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'region.label', default: 'Region')}" />
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
    <g:hasErrors bean="${regionInstance}">
      <div class="errors">
        <g:renderErrors bean="${regionInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${regionInstance?.id}" />
      <g:hiddenField name="version" value="${regionInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="region.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: regionInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${regionInstance?.identifier}" />
          </td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="region.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: regionInstance, field: 'description', 'errors')}">
          <g:textField name="description" value="${regionInstance?.description}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="country"><g:message code="region.country.label" default="Country" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: regionInstance, field: 'country', 'errors')}">
          <g:select name="country.id" from="${com.munix.Country.list()}" optionKey="id" value="${regionInstance?.country?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="provinces"><g:message code="region.provinces.label" default="Provinces" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: regionInstance, field: 'provinces', 'errors')}">

              <ul>
                <g:each in="${regionInstance?.provinces?}" var="p">
                  <li><g:link controller="province" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
                </g:each>
              </ul>
          <g:link controller="province" action="create" params="['region.id': regionInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'province.label', default: 'Province')])}</g:link>

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
