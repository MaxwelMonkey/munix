
<%@ page import="com.munix.CollectionSchedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'collectionSchedule.label', default: 'CollectionSchedule')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript src="generalmethods.js" />
    <g:javascript>
        var $ = jQuery.noConflict()
        $(document).ready(function (){
            $("#searchId").ForceNumericOnlyEnterAllowed(true)
        })
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

      <div id="search">
          <g:form controller="collectionSchedule" action="list" >
            <table>
              <tr>
                <td class="name">ID</td>
                <td class="value"><g:textField name="searchId" id = "searchId" value ="${params.searchId}"/></td>
              </tr>
              <tr>
                <td class="name">Identifier</td>
                <td class="value"><g:textField name="searchIdentifier" id="searchIdentifier" value ="${params.searchIdentifier}"/></td>
              </tr>
              <tr>
                <td class="name">Description</td>
                <td class="value"><g:textField name="searchDescription" id="searchDescription" value ="${params.searchDescription}"/></td>
              </tr>
              <tr>
                <td class="name">Collector</td>
                <td class="value"><g:textField name="searchCollector" value ="${params.searchCollector}"/></td>
              </tr>
              <tr>
                <td class="name">Status</td>
                <td class="value"><g:select name="searchStatus" from="${['Processing', 'Cancelled', 'Complete']}" noSelection="['':'']" value ="${params.searchStatus}"/></td>
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
        <g:sortableColumn class="center" property="id" title="${message(code: 'collectionSchedule.id.label', default: 'ID')}" params="${params}"/>
        <g:sortableColumn property="identifier" title="${message(code: 'collectionSchedule.identifier.label', default: 'Identifier')}" params="${params}"/>
        <g:sortableColumn property="description" title="${message(code: 'collectionSchedule.identifier.label', default: 'Description')}" params="${params}"/>
        <g:sortableColumn property="collector" title="${message(code: 'collectionSchedule.identifier.label', default: 'Collector')}" params="${params}"/>
        <g:sortableColumn class="center" property="startDate" title="${message(code: 'collectionSchedule.startDate.label', default: 'Start Date')}" params="${params}"/>
        <g:sortableColumn class="center" property="endDate" title="${message(code: 'collectionSchedule.endDate.label', default: 'End Date')}" params="${params}"/>
        <g:sortableColumn property="status" title="${message(code: 'collectionSchedule.status.label', default: 'Status')}" params="${params}"/>
        <th class="center"> Amount (Counter)</th>
        <th class="center"> Amount (Collection)</th>
        <th class="center">Amount (Both)</th>
        <th class="center" width="120px"> Total Amount</th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${collectionScheduleInstanceList}" status="i" var="collectionScheduleInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}collectionSchedule/show/${collectionScheduleInstance.id}'">

            <td id="rowCollectionScheduleId${i}" class="center">${collectionScheduleInstance}</td>
            <td>${fieldValue(bean: collectionScheduleInstance, field: "identifier")}</td>
            <td>${fieldValue(bean: collectionScheduleInstance, field: "description")}</td>
            <td>${fieldValue(bean: collectionScheduleInstance, field: "collector.description")}</td>
            <td class="center"><g:formatDate date="${collectionScheduleInstance.startDate}" format="MM/dd/yyyy" /></td>
            <td class="center"><g:formatDate date="${collectionScheduleInstance.endDate}" format="MM/dd/yyyy" /></td>
            <td>${fieldValue(bean: collectionScheduleInstance, field: "status")}</td>
            <td class="right">PHP <g:formatNumber number="${collectionScheduleInstance?.computeAmountCounterTotal()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${collectionScheduleInstance?.computeAmountCollectionTotal()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${collectionScheduleInstance?.computeAmountBothTotal()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${collectionScheduleInstance?.computeTotalAmount()}" format="###,##0.00" /></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="paginateButtons">
      <g:paginate total="${collectionScheduleInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
