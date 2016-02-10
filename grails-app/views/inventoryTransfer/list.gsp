
<%@ page import="com.munix.InventoryTransfer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'inventoryTransfer.label', default: 'Inventory Transfer')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  	<g:javascript src="generalmethods.js" />
	<g:javascript src="calendarStructTemplate.js" />  	
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function () {
			$("#searchId").ForceNumericOnlyEnterAllowed(true)
			setCalendarStruct($("#searchDateCreatedFrom"), $("#searchDateCreatedFrom_value"))
        	setCalendarStruct($("#searchDateCreatedTo"), $("#searchDateCreatedTo_value"))
        })
    </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
  	<calendar:resources lang="en" theme="aqua"/>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div id="search">
      <g:form controller="inventoryTransfer" action="list" method="post">
        <table>
          <tr>
            <td class="name" width="400px">ID</td>
            <td class="value"><g:textField maxlength="17" id="searchId" name="searchId" value="${params?.searchId}"/></td>
          </tr>
          <tr>
            <td class="name">Origin Warehouse</td>
            <td class="value"><g:select name="searchOriginWarehouse" noSelection="['':'']" from="${warehouses}" optionValue="identifier" optionKey="id" value="${params?.searchOriginWarehouse}"/></td>
          </tr>
          <tr>
            <td class="name">Destination Warehouse</td>
            <td class="value"><g:select name="searchDestinationWarehouse" noSelection="['':'']" from="${warehouses}" optionValue="identifier" optionKey="id" value="${params?.searchDestinationWarehouse}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="['':'']" from="${statuses}" optionValue="name" optionKey="name" value="${params?.searchStatus}"/></td>
          </tr>
          <tr>
            <td class="name">Date Created</td>
            <td class="value" width="1000px">
              <calendar:datePicker name="searchDateCreatedFrom" years="2009,2030" value='${dateMap.searchDateCreatedFrom}' />
           	  to
           	  <calendar:datePicker name="searchDateCreatedTo" years="2009,2030" value='${dateMap.searchDateCreatedTo}'/>
           	</td>
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

        <g:sortableColumn class="center" property="id" title="${message(code: 'inventoryTransfer.id.label', default: 'Id')}" params="${params}"/>
        <g:sortableColumn class="center" property="originWarehouse" title="${message(code: 'inventoryTransfer.originWarehouse.label', default: 'Origin')}" params="${params}"/>
        <g:sortableColumn class="center" property="destinationWarehouse" title="${message(code: 'inventoryTransfer.destinationWarehouse.label', default: 'Destination')}" params="${params}" />
        <g:sortableColumn class="center" property="status" title="${message(code: 'inventoryTransfer.status.label', default: 'Status')}" params="${params}"/>
        <g:sortableColumn class="center" property="date" title="${message(code: 'inventoryTransfer.date.label', default: 'Date Created')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${inventoryTransferList}" status="i" var="inventoryTransferInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}inventoryTransfer/show/${inventoryTransferInstance.id}'">

            <td class="left">${inventoryTransferInstance}</td>
            <td>${fieldValue(bean: inventoryTransferInstance, field: "originWarehouse")}</td>
            <td>${fieldValue(bean: inventoryTransferInstance, field: "destinationWarehouse")}</td>
            <td>${fieldValue(bean: inventoryTransferInstance, field: "status")}</td>
            <td class="left"><g:formatDate date="${inventoryTransferInstance.date}" format="MM/dd/yyyy"/></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${inventoryTransferInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
