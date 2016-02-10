
<%@ page import="com.munix.DiscountGroup" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'discountGroup.label', default: 'DiscountGroup')}" />
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
            <td valign="top" class="name"><g:message code="discountGroup.identifier.label" default="Identifier" /></td>
        <td id="identifier" valign="top" class="value">${fieldValue(bean: discountGroupInstance, field: "identifier")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="discountGroup.description.label" default="Description" /></td>
        <td id="description" valign="top" class="value">${fieldValue(bean: discountGroupInstance, field: "description")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="discountGroup.rate.label" default="Rate" /></td>
        <td id="rate" valign="top" class="value">${discountGroupInstance?.formatRate()}</td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <input type="hidden" id="id" name="id" value="${params.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
