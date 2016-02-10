
<%@ page import="com.munix.SalesAgent" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesAgent.label', default: 'SalesAgent')}" />
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
     <report:report id="salesAgent" report="salesAgent" format="PDF"> Generate Report
     </report:report>
    <div id="search">
      <g:form controller="salesAgent" action="list" >
      	<table>
      	  <tr>
            <td class="name" width="400px">Identifier</td>
            <td class="value" width="900px"><g:textField  name="searchIdentifier" id="searchIdentifier" value ="${params.searchIdentifier}"/></td>
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
        <g:sortableColumn property="identifier" title="${message(code: 'salesAgent.identifier.label', default: 'Identifier')}" params="${params}"/>
        <g:sortableColumn property="lastName" title="${message(code: 'salesAgent.lastName.label', default: 'Last Name')}" params="${params}"/>
        <g:sortableColumn property="firstName" title="${message(code: 'salesAgent.firstName.label', default: 'First Name')}" params="${params}"/>
        <th>Landline</th>
        <th>Mobile</th>
        <th>Email</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${salesAgentInstanceList}" status="i" var="salesAgentInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}salesAgent/show/${salesAgentInstance.id}'">

            <td id="rowSalesAgentIdentifier${i}">${fieldValue(bean: salesAgentInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: salesAgentInstance, field: "lastName")}</td>
            <td>${fieldValue(bean: salesAgentInstance, field: "firstName")}</td>
            <td>${fieldValue(bean: salesAgentInstance, field: "landline")}</td>
            <td>${fieldValue(bean: salesAgentInstance, field: "mobile")}</td>
            <td>${fieldValue(bean: salesAgentInstance, field: "email")}</td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${salesAgentInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
