
<%@ page import="com.munix.Supplier" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplier.label', default: 'Supplier')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <g:javascript>
    Event.observe(window, 'load', function() {
    $('search').hide();
    Event.observe('searchButton', 'click', function(){
    $('search').toggle();
    });
    });
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">

    <h1><g:message code="default.list.label" args="[entityName]" /></h1>

    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <report:report id="supplier" report="supplier" format="PDF" > Generate Report
    </report:report>
    <input type="button" id="searchButton" value="Search"/>
    <input type="button" value="Reset" onClick="window.location = '${createLink(uri:'/')}supplier/list'"/>
    <div id="search">
      <g:form controller="supplier" action="list" >
        <table>
          <tr>
            <td class="name">Identifier</td>
            <td class="value"><g:textField name="searchIdentifier" value="${params?.searchIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Name</td>
            <td class="value"><g:textField name="searchName" value="${params?.searchName}"/></td>
          </tr>
          <tr>
            <td class="name">Country</td>
            <td class="value"><g:textField name="searchCountry" value="${params?.searchCountry}"/></td>
          </tr>
          <tr>
            <td class="name">Contact</td>
            <td class="value"><g:textField name="searchContact" value="${params?.searchContact}"/></td>
          </tr>
        </table>
        <div>
          <input type="submit" class="button" value="Go"/>
        </div>
      </g:form>
    </div>
    <div class="list">
      <table>
        <thead>
          <tr>

        <g:sortableColumn property="identifier" title="${message(code: 'supplier.identifier.label', default: 'Identifier')}" params="${params}"/>
        <g:sortableColumn property="name" title="${message(code: 'supplier.name.label', default: 'Name')}" params="${params}" />
        <g:sortableColumn property="country" title="${message(code: 'supplier.country.label', default: 'Country')}" params="${params}" />
        <g:sortableColumn property="contact" title="${message(code: 'supplier.contact.label', default: 'Contact')}" params="${params}"/>
        <g:sortableColumn property="tin" title="${message(code: 'supplier.tin.label', default: 'Tin')}" params="${params}"/>
        <g:sortableColumn property="term" title="${message(code: 'supplier.term.label', default: 'Terms')}" params="${params}"/>
        <g:sortableColumn property="landline" title="${message(code: 'supplier.landline.label', default: 'Landline')}" params="${params}"/>
        <g:sortableColumn property="email" title="${message(code: 'supplier.email.label', default: 'Email')}" params="${params}"/>
        <g:sortableColumn property="fax" title="${message(code: 'supplier.fax.label', default: 'Fax')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${supplierInstanceList}" status="i" var="supplierInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}supplier/show/${supplierInstance.id}'">

            <td id="rowSupplierIdentifier${i}">${fieldValue(bean: supplierInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "name")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "country")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "contact")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "tin")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "term")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "landline")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "email")}</td>
            <td>${fieldValue(bean: supplierInstance, field: "fax")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${supplierInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
