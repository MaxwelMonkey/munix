<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkWarehousing.label', default: 'CheckWarehousing')}" />
  <title>Check Warehousing filter</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">

    <g:form action="create" method="post">
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="originWarehouse"><g:message code="checkWarehousing.originWarehouse.label" default="Origin Warehouse" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'originWarehouse', 'errors')}">
          <g:select name="originWarehouse.id" from="${com.munix.CheckWarehouse.list().sort{it.toString()}}" optionKey="id" value="${checkWarehousingInstance?.originWarehouse?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="date"><g:message code="check.date.label" default="Start Date" /></label>
            </td>
            <td valign="top" class="value">
          <g:datePicker name="startDate" precision="day" value=""  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="date"><g:message code="check.date.label" default="End Date" /></label>
            </td>
            <td valign="top" class="value">
          <g:datePicker name="endDate" precision="day" value=""  />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:submitButton name="filter" class="filter" value="Filter" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
