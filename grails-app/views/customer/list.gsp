
<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
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
    <span class="menuButton"><g:link class="list" action="list" controller="customerDiscount">Customer Discount List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <report:report id="customer" report="customer" format="PDF"> Generate Report </report:report>
    <div id="search">
      <g:form controller="customer" action="list" >
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
            <td class="name">Sales Agent</td>
            <td class="value"><g:textField name="searchSalesAgent" value="${params?.searchSalesAgent}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" from="${['Active', 'Inactive', 'On Hold', 'Bad Account']}" noSelection="['':'']" value="${params?.searchStatus}"/></td>
          </tr>

        </table>
        <div>
          <input type="submit" class="button" value="Search"/>
        </div>

      </g:form>
    </div>

    <div class="list">
      <table>
        <thead>
          <tr>
        <g:sortableColumn property="identifier" title="${message(code: 'customer.identifier.label', default: 'Identifier')}" params="${params}" />
        <g:sortableColumn property="name" title="${message(code: 'customer.name.label', default: 'Name')}" params="${params}"/>
        <th>Business Address</th>
        <g:sortableColumn property="salesAgent" title="${message(code: 'customer.salesAgent.label', default: 'Sales Agent')}" params="${params}" />
        <g:sortableColumn property="forwarder" title="${message(code: 'customer.forwarderDescription.label', default: 'Forwarder')}" params="${params}"/>
        <g:sortableColumn property="declaredValue" title="${message(code: 'customer.declaredValue.label', default: 'Declared Value')}" params="${params}"/>
        <g:sortableColumn class="right" property="forwarder" title="${message(code: 'customer.forwarder.label', default: 'Credit Limit')}" params="${params}"/>
        <g:sortableColumn property="enableCreditLimit" title="${message(code: 'customer.enableCreditLimit.label', default: 'Enabled Credit Limit')}" params="${params}"/>
        <g:sortableColumn property="term" title="${message(code: 'customer.term.label', default: 'Terms')}" params="${params}"/>
		<g:sortableColumn property="landline" title="${message(code: 'customer.landline.label', default: 'Landline')}" params="${params}"/>
		<g:sortableColumn property="mobile" title="${message(code: 'customer.mobile.label', default: 'Mobile')}" params="${params}"/>
		<g:sortableColumn property="fax" title="${message(code: 'customer.fax.label', default: 'Fax')}" params="${params}"/>
		<g:sortableColumn property="email" title="${message(code: 'customer.email.label', default: 'Email')}" params="${params}" />
        <g:sortableColumn property="status" title="${message(code: 'customer.status.label', default: 'Status')}" params="${params}"/>
        </tr>
        </thead>
        <tbody>
        <g:each in="${customerInstanceList}" status="i" var="customerInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}customer/show/${customerInstance.id}'">

            <td id="rowCustomerIdentifier${i}">${fieldValue(bean: customerInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: customerInstance, field: "name")}</td>
            <td>${customerInstance.formatBusinessAddress()}</td>
            <td>${fieldValue(bean: customerInstance, field: "salesAgent")}</td>
            <td>${fieldValue(bean: customerInstance, field: "forwarder.description")}</td>
            <td>${fieldValue(bean: customerInstance, field: "declaredValue")}</td>
            <td class="right">${customerInstance.formatCreditLimit()}</td>
            <td>${fieldValue(bean: customerInstance, field: "enableCreditLimit")}</td>
            <td>${fieldValue(bean: customerInstance, field: "term.description")}</td>
            <td>${fieldValue(bean: customerInstance, field: "landline")}</td>
            <td>${fieldValue(bean: customerInstance, field: "mobile")}</td>
            <td>${fieldValue(bean: customerInstance, field: "fax")}</td>
            <td>${fieldValue(bean: customerInstance, field: "email")}</td>
            <td>${fieldValue(bean: customerInstance, field: "status")}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${customerInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
