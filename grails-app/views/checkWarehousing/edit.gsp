
<%@ page import="com.munix.CheckWarehousing" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkWarehousing.label', default: 'CheckWarehousing')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
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
    <span class="menuButton"><g:link class="create" action="filter"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${checkWarehousingInstance}">
      <div class="errors">
        <g:renderErrors bean="${checkWarehousingInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${checkWarehousingInstance?.id}" />
      <g:hiddenField name="version" value="${checkWarehousingInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="date"><g:message code="checkWarehousing.date.label" default="Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'date', 'errors')}">
                <g:formatDate date="${checkWarehousingInstance?.date}" format="MM/dd/yyyy" />

              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="originWarehouse"><g:message code="checkWarehousing.originWarehouse.label" default="Origin" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'originWarehouse', 'errors')}">
                ${checkWarehousingInstance?.originWarehouse}
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="destinationWarehouse"><g:message code="checkWarehousing.destinationWarehouse.label" default="Destination" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: checkWarehousingInstance, field: 'destinationWarehouse', 'errors')}">
                <g:select name="destinationWarehouse.id" from="${com.munix.CheckWarehouse.list().sort{it.toString()}}" optionKey="id" value="${checkWarehousingInstance?.destinationWarehouse?.id}"  />
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
                <label for="checks"><g:message code="checkWarehousing.checks.label" default="Checks" /></label>
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
                      <g:each in="${checkPayments}" var="check">
                        <tr>
                          <g:set var="ticked" value="false"/>
                          <g:if test="${checkWarehousingInstance?.checks?.contains(check)}">
                            <g:set var="ticked" value="true"/>
                          </g:if>
                          <td><g:checkBox name="checks" class="check" value="${check.id}" checked="${ticked}"/></td>
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
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
