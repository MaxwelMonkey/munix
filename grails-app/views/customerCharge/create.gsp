
<%@ page import="com.munix.CustomerCharge" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'customerCharge.label', default: 'CustomerCharge')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
   <g:javascript>
    var $ = jQuery.noConflict()
    
    $(document).ready(function () {
    
      var setCustomerValue = function(setValue, toBeSet){
            $(toBeSet).val($(setValue).val())
      }
    
      $(".customerName").change(function () {
        setCustomerValue($(".customerName"),$(".customerId"))
      })
      
      $(".customerId").change(function () {
        setCustomerValue($(".customerId"),$(".customerName"))
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
    <g:hasErrors bean="${customerChargeInstance}">
      <div class="errors">
        <g:renderErrors bean="${customerChargeInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="customer"><g:message code="customerCharge.customer.label" default="Customer" /></label>
            </td>
           
            <td valign="top" class="value ${hasErrors(bean: customerChargeInstance, field: 'customer', 'errors')}" width="650px">
               <g:set var="customersSortedByIdentifier" value="${com.munix.Customer.list().sort{it.identifier}}" />
               <g:select name="customer.id" class="customerId" from="${customersSortedByIdentifier}" optionKey="id" optionValue="identifier" value="${customerChargeInstance?.customer?.id}" noSelection="['':'select one..']"/>
               <g:set var="customersSortedByName" value="${com.munix.Customer.list().sort{it.name}}" />
               <g:select name="customerName" class="customerName" from="${customersSortedByName}" optionKey="id" optionValue="name" value="${customerChargeInstance?.customer?.id}" noSelection="['':'select one..']"/>               
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="customerCharge.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerChargeInstance, field: 'remark', 'errors')}">
          <g:textArea name="remark" value="${customerChargeInstance?.remark}" />
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
