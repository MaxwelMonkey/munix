
<%@ page import="com.munix.Supplier" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplier.label', default: 'Supplier')}" />
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
    <g:hasErrors bean="${supplierInstance}">
      <div class="errors">
        <g:renderErrors bean="${supplierInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${supplierInstance?.id}" />
      <g:hiddenField name="version" value="${supplierInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="supplier.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" value="${supplierInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="name"><g:message code="supplier.name.label" default="Name" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'name', 'errors')}">
          <g:textField name="name" value="${supplierInstance?.name}" />
          </td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name">
              <label for="country"><g:message code="supplier.country.label" default="Country" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'country', 'errors')}">
          <g:select name="country.id" from="${com.munix.Country.list().sort{it.toString()}}" optionKey="id" value="${supplierInstance?.country?.id}" noSelection="['null': '']" />
          </td>
          </tr>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="country"><g:message code="supplier.localeType.label" default="Locale Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'country', 'errors')}">
            <g:select name="localeType" from="${['Local', 'Foreign']}" value="${supplierInstance?.localeType}" />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="address"><g:message code="supplier.address.label" default="Address" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'address', 'errors')}">
          <g:textArea name="address" value="${supplierInstance?.address}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="tin"><g:message code="supplier.tin.label" default="TIN" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'tin', 'errors')}">
          <g:textField name="tin" value="${supplierInstance?.tin}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="term"><g:message code="supplier.term.label" default="Terms" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'term', 'errors')}">
          <g:select name="term.id" from="${com.munix.Term.list().sort{it.id}}" optionKey="id" value="${supplierInstance?.term?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="currency"><g:message code="supplier.currency.label" default="Currency" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'currency', 'errors')}">
          <g:select name="currency.id" from="${com.munix.CurrencyType.list().sort{it.toString()}}" optionKey="id" value="${supplierInstance?.currency?.id}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="contact"><g:message code="supplier.contact.label" default="Contact" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'contact', 'errors')}">
          <g:textField name="contact" value="${supplierInstance?.contact}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="landline"><g:message code="supplier.landline.label" default="Landline" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'landline', 'errors')}">
          <g:textField name="landline" value="${supplierInstance?.landline}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="fax"><g:message code="supplier.fax.label" default="Fax" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'fax', 'errors')}">
          <g:textField name="fax" value="${supplierInstance?.fax}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="email"><g:message code="supplier.email.label" default="Email" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'email', 'errors')}">
          <g:textField name="email" value="${supplierInstance?.email}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="skype"><g:message code="supplier.skype.label" default="Skype" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'skype', 'errors')}">
          <g:textField name="skype" value="${supplierInstance?.skype}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="yahoo"><g:message code="supplier.yahoo.label" default="Yahoo" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'yahoo', 'errors')}">
          <g:textField name="yahoo" value="${supplierInstance?.yahoo}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="website"><g:message code="supplier.website.label" default="Website" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierInstance, field: 'website', 'errors')}">
          <g:textField name="website" value="${supplierInstance?.website}" />
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
