
<%@ page import="com.munix.Company" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
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
            <td valign="top" class="name"><g:message code="company.name.label" default="Name" /></td>
        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "name")}</td>
        </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="company.code.label" default="Code" /></td>
        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "code")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="company.registeredAddress.label" default="Registered Address" /></td>
        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "registeredAddress")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="company.billingAddress.label" default="Billing Address" /></td>
        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "billingAddress")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="company.fax.label" default="Fax" /></td>
        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "fax")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="company.landline.label" default="Landline" /></td>
        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "landline")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="company.email.label" default="Email" /></td>

        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "email")}</td>

        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="company.website.label" default="Website" /></td>

        <td valign="top" class="value">${fieldValue(bean: companyInstance, field: "website")}</td>

        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${companyInstance?.id}" />
        <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
      </g:form>
    </div>
  </div>
</body>
</html>
