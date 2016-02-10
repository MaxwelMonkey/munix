
<%@ page import="com.munix.CheckType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'checkType.label', default: 'CheckType')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
    <g:javascript src="numbervalidation.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function(){
    	  $("#routingNumber").ForceNumericOnly(false)
        });
    </g:javascript>
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
    <g:hasErrors bean="${checkTypeInstance}">
      <div class="errors">
        <g:renderErrors bean="${checkTypeInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${checkTypeInstance?.id}" />
      <g:hiddenField name="version" value="${checkTypeInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="routingNumber"><g:message code="checkType.routingNumber.label" default="Routing Number" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: checkTypeInstance, field: 'routingNumber', 'errors')}">
                <g:textField name="routingNumber" id="routingNumber" value="${fieldValue(bean: checkTypeInstance, field: 'routingNumber')}" />
              </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="branch"><g:message code="checkType.branch.label" default="Branch" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkTypeInstance, field: 'branch', 'errors')}">
          <g:textField name="branch" value="${fieldValue(bean: checkTypeInstance, field: 'branch')}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="description"><g:message code="checkType.description.label" default="Description" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkTypeInstance, field: 'description', 'errors')}">
              <select name="description">
                <g:if test="${checkTypeInstance.description == 'Local'}">
                  <option value="Local" selected="selected">Local</option>
                  <option value="Regional">Regional</option>
                </g:if>
                <g:else>
                  <option value="Local">Local</option>
                  <option value="Regional" selected="selected">Regional</option>
                </g:else>
              </select>
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
