
<%@ page import="com.munix.DiscountType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'discountType.label', default: 'DiscountType')}" />
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
            <td valign="top" class="name"><g:message code="discountType.identifier.label" default="Identifier" /></td>

        <td id="identifier" valign="top" class="value">${fieldValue(bean: discountTypeInstance, field: "identifier")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="discountType.description.label" default="Description" /></td>

        <td id="description" valign="top" class="value">${fieldValue(bean: discountTypeInstance, field: "description")}</td>

        </tr>
        
        <tr class="prop">
            <td valign="top" class="name"><g:message code="discountType.netItemMargin.label" default="Net Item Margin" /></td>
        	<td id="margin" valign="top" class="value">${fieldValue(bean: discountTypeInstance, field: "netItemMargin")}</td>
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name"><g:message code="discountType.discountedItemMargin.label" default="Discounted Item Margin" /></td>
        	<td id="margin" valign="top" class="value">${fieldValue(bean: discountTypeInstance, field: "discountedItemMargin")}</td>
        </tr>
        
        <tr class="prop">
          <td valign="top" class="name"><g:message code="discountType.excludeInCommission.label" default="Exclude In Commission" /></td>

        <td id="excludeInCommission" valign="top" class="value">${fieldValue(bean: discountTypeInstance, field: "excludeInCommission")}</td>

        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${discountTypeInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
