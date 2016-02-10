
<%@ page import="com.munix.PurchaseInvoice" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  	<link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
    <g:javascript>
     var $ = jQuery.noConflict()
    
    $(document).ready(function () {
    
      var setSupplierValue = function(setValue, toBeSet){
            $(toBeSet).val($(setValue).val())
      }
    
      $(".supplierName").change(function () {
        setSupplierValue($(".supplierName"),$(".supplierId"))
          createPurchaseInvoice($(this).val())
      })
      
      $(".supplierId").change(function () {
        setSupplierValue($(".supplierId"),$(".supplierName"))
          createPurchaseInvoice($(this).val())
      })
     })

      var createPurchaseInvoice = function(fieldValue) {
        $(".form").submit()
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
    <g:form action="create" method="post" class="form">
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="supplier"><g:message code="purhcaseInvoice.supplier.label" default="Supplier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseInvoiceInstance, field: 'supplier', 'errors')}">
                <g:set var="suppliersSortedByIdentifier" value="${com.munix.Supplier.list().sort{it.identifier.toLowerCase()}}" />
                <g:select name="supplier.id" class="supplierId" from="${suppliersSortedByIdentifier}" optionValue="identifier" optionKey="id" value="${params.'supplier.id'}" noSelection="['':'select one..']"/>
                <g:set var="suppliersSortedByName" value="${com.munix.Supplier.list().sort{it.name.toLowerCase()}}" />
                <g:select name="supplierName" class="supplierName" from="${suppliersSortedByName}" optionValue="name" optionKey="id" value="${params.'supplier.name'}" noSelection="['':'select one..']"/>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </g:form>
  </div>
</body>
</html>