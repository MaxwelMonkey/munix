<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'bouncedCheck.label', default: 'BouncedCheck')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript>
  var $ = jQuery.noConflict()
    
  
  	$(document).ready(function () {

      $(".customerId").change(function () {
        setCustomerValue($(".customerId"),$(".customerName"))
        createBouncedCheck($(this).val())
      })
      $(".customerName").change(function () {
        $(".customerId").val($(".customerName").val())
        setCustomerValue($(".customerName"),$(".customerId"))
        createBouncedCheck($(this).val())
      })
    })

    var createBouncedCheck = function(fieldValue) {
        $(".form").submit()
    }
    
    var setCustomerValue = function(setValue, toBeSet){
            $(toBeSet).val($(setValue).val())
      }
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
    <g:hasErrors bean="${bouncedCheckInstance}">
      <div class="errors">
        <g:renderErrors bean="${bouncedCheckInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="create" method="post" class="form" >
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="bouncedCheck.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: bouncedCheckInstance, field: 'customer', 'errors')}" width="650px">
              	<g:set var="customersSortedByIdentifier" value="${com.munix.Customer.list().sort{it.identifier.toLowerCase()}}" />
                <g:select name="id" class="customerId" from="${customersSortedByIdentifier}" optionValue="identifier" optionKey="id" noSelection="['':'select one..']"/>
                <g:set var="customersSortedByName" value="${com.munix.Customer.list().sort{it.name.toLowerCase()}}" />
                <g:select name="name" class="customerName" from="${customersSortedByName}" optionValue="name" optionKey="id" noSelection="['':'select one..']"/>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </g:form>
  </div>
</body>
</html>
