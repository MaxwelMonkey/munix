<%@ page import="com.munix.CheckWarehousing" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkWarehousing.label', default: 'CheckWarehousing')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript>
    var $ = jQuery.noConflict()

    $(document).ready(function () {
    	$("#checkAll").click(function() {
    		$(".check").attr('checked', $("#checkAll").attr('checked'))
    	})
    }) 
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${checkWarehousingInstance}">
      <div class="errors">
        <g:renderErrors bean="${checkWarehousingInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

          <g:set var="checkPaymentInstance" value="${checkPaymentInstance}" />
          <g:set var="originWarehouse" value="${com.munix.CheckWarehouse.get(originWarehouse.id)}" />

          <tr class="prop">
            <td valign="top" class="name">
              <label for="originWarehouse"><g:message code="checkWarehousing.originWarehouse.label" default="Origin" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'originWarehouse', 'errors')}">
          <g:hiddenField name="originWarehouse.id" value="${originWarehouse.id}" />
${originWarehouse}
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="destinationWarehouse"><g:message code="checkWarehousing.destinationWarehouse.label" default="Destination" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'destinationWarehouse', 'errors')}">
          <g:select name="destinationWarehouse.id" from="${com.munix.CheckWarehouse.list().findAll{it != originWarehouse}.sort{it.toString()}}" optionKey="id" value=""  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="checkWarehousingInstance.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'remark', 'errors')}">
              <g:textArea name="remark" maxlength= "250" value="${checkWarehousingInstance?.remark}" />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="checks"><g:message code="checkWarehousingInstance.checks.label" default="Checks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'checks', 'errors')}">
              <div class="subTable">
			  	<table>
				  <thead>
				  	<th style="width:30px;"><g:checkBox name="checkAll" checked="false"/></th>
				  	<th>Number</th>
					<th style="width:80px;">Date</th>
					<th>Bank</th>
					<th>Branch</th>
					<th>Amount</th>
					<th>Customer</th>
				  </thead>
				  <tbody>
					<g:each in="${checkPayments.sort{it.toString()}}" var="check">
				  	  <tr>
						<td><g:checkBox name="checks" class="check" value="${check.id}" checked="false"/></td>
						<td>${check}</td>
						<td><g:formatDate date="${check.date}" format="MMM dd, yyyy" /></td>
						<td>${check.bank}</td>
						<td>${check.branch}</td>
						<td class="right"><g:formatNumber number="${check.amount}" format="#,##0.00" /></td>
						<td>${check.customer}</td>
					  </tr>
					</g:each>
				  </tbody>
				</table>
			  </div>
          
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
