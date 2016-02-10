
<%@ page import="com.munix.Company" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'company.label', default: 'Company')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${companyInstance}">
      <div class="errors">
        <g:renderErrors bean="${companyInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post"  enctype="multipart/form-data">
      <g:hiddenField name="id" value="${companyInstance?.id}" />
      <g:hiddenField name="version" value="${companyInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="name"><g:message code="company.name.label" default="Name" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'name', 'errors')}">
          <g:textField name="name" value="${companyInstance?.name}" />
          </td>
          </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="code"><g:message code="company.code.label" default="Code" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'code', 'errors')}">
          <g:textField name="code" value="${companyInstance?.code}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="registeredAddress"><g:message code="company.registeredAddress.label" default="Registered Address" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'registeredAddress', 'errors')}">
          <g:textField name="registeredAddress" value="${companyInstance?.registeredAddress}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="billingAddress"><g:message code="company.billingAddress.label" default="Billing Address" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'billingAddress', 'errors')}">
          <g:textField name="billingAddress" value="${companyInstance?.billingAddress}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="fax"><g:message code="company.fax.label" default="Fax" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'fax', 'errors')}">
          <g:textField name="fax" value="${companyInstance?.fax}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="landline"><g:message code="company.landline.label" default="Landline" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'landline', 'errors')}">
          <g:textField name="landline" value="${companyInstance?.landline}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="email"><g:message code="company.email.label" default="Email" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'email', 'errors')}">
          <g:textField name="email" value="${companyInstance?.email}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="website"><g:message code="company.website.label" default="Website" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: companyInstance, field: 'website', 'errors')}">
          <g:textField name="website" value="${companyInstance?.website}" />
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
