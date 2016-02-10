
<%@ page import="com.munix.Forwarder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'forwarder.label', default: 'Forwarder')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
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
    <report:report id="forwarder" report="forwarder" format="PDF" >Generate Report</report:report>
    
    <div id="search">
      <g:form controller="forwarder" action="list" >
        <table>
          <tr>
            <td class="name" width="400px">Identifier</td>
            <td class="value" width="900px"><g:textField name="searchIdentifier" value ="${params.searchIdentifier}"/></td>
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
        <g:sortableColumn property="identifier" title="${message(code: 'forwarder.identifier.label', default: 'Identifier')}" params="${params}" />
        <g:sortableColumn property="description" title="${message(code: 'forwarder.description.label', default: 'Description')}" params="${params}"/>
        <th><g:message code="forwarder.landline.label" default="Landline" params="${params}" /></th>
        <th><g:message code="forwarder.contact.label" default="Contact" params="${params}"/></th>
        <th><g:message code="forwarder.address.label" default="Address" params="${params}"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${forwarderInstanceList}" status="i" var="forwarderInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}forwarder/show/${forwarderInstance.id}'">

            <td id="rowForwarderIdentifier${i}">${fieldValue(bean: forwarderInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: forwarderInstance, field: "description")}</td>
            <td>${fieldValue(bean: forwarderInstance, field: "landline")}</td>
            <td>${fieldValue(bean: forwarderInstance, field: "contact")}</td>
            <td>${forwarderInstance.formatAddress()}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${forwarderInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
