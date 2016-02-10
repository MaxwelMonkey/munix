
<%@ page import="com.munix.DirectDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directDelivery.label', default: 'Direct Delivery')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript>
    var $ = jQuery.noConflict()
  
    function reload(){
    var customer = document.getElementById('customerName').value;
    window.location = '${createLink(uri:'/')}directDelivery/create/' + customer;
    }
    
    $(document).ready(function () {
    
      var setCustomerValue = function(setValue, toBeSet){
            $(toBeSet).val($(setValue).val())
      }
    
      $(".customerName").change(function () {
        setCustomerValue($(".customerName"),$(".customerId"))
        reload()
      })
      
      $(".customerId").change(function () {
        setCustomerValue($(".customerId"),$(".customerName"))
        reload()
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
    <g:hasErrors bean="${directDeliveryInstance}">
      <div class="errors">
        <g:renderErrors bean="${directDeliveryInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

          <g:if test="${params.id == null}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="directDelivery.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directDeliveryInstance, field: 'customer', 'errors')}">
                <g:set var="customersSortedByIdentifier" value="${com.munix.Customer.list().sort{it.identifier.toLowerCase()}}" />
          		<g:select name="customer.id" class="customerId" from="${customersSortedByIdentifier}" optionKey="id" optionValue="identifier" value="${directDeliveryInstance?.customer?.id}" noSelection="['':'select one..']"/>
          		<g:set var="customersSortedByName" value="${com.munix.Customer.list().sort{it.name.toLowerCase()}}" />
          		<g:select name="customerName" class="customerName" from="${customersSortedByName}" optionKey="id" optionValue="name" value="${directDeliveryInstance?.customer?.id}" noSelection="['':'select one..']" />
          	  </td>
            </tr>
          </g:if>

          <g:else>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="directDelivery.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value">
            <g:set var="customerInstance" value="${com.munix.Customer.get(params?.id)}" />
            <input type="hidden" name="customer.id" value="${customerInstance?.id}" />
${customerInstance}
            </td>
            </tr>
          </g:else>

          <g:if test="${params.id}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="deliveries"><g:message code="directDelivery.deliveries.label" default="Deliveries" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directDelivery, field: 'deliveries', 'errors')}">
            <g:select name="deliveries" from="${deliveryInstance.sort{it.toString()}}" optionKey="id" size="10" multiple="true" value="${waybillInstance?.deliveries?.id}" />
            </td>
            </tr>
          </g:if>

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
