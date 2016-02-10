<%@ page import="com.munix.SupplierPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplierPayment.label', default: 'SupplierPayment')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript>
    var $ = jQuery.noConflict()
    function checkFields(fields, check){
        if(check.checked){
            checkAll(fields)
        }else{
            uncheckAll(fields)
        }
    }
    function checkAll(field)
    {
        for (i = 0; i < field.length; i++)
            field[i].checked = true ;
    }

    function uncheckAll(field)
    {
        for (i = 0; i < field.length; i++)
            field[i].checked = false ;
    }
    $(document).ready(function () {
      $(".supplierId").change(function () {
        createSupplierPayment($(this).val())
      })
      $(".supplierName").change(function () {
        createSupplierPayment($(this).val())
      })
    })

    var createSupplierPayment = function(fieldValue) {
        window.location="${createLink(uri:'/')}supplierPayment/create/" + fieldValue
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
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <g:hasErrors bean="${supplierPaymentInstance}">
      <div class="errors">
        <g:renderErrors bean="${supplierPaymentInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="supplier"><g:message code="supplierPayment.supplier.label" default="Supplier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: supplierPaymentInstance, field: 'supplier', 'errors')}">
                <g:select from="${sortedByIdentifier}" style="width:150px" name="supplier.id" class="supplierId" id="id" optionKey="id" optionValue="identifier" value="${supplierPaymentInstance?.supplier?.id}" noSelection="['':'select one..']"/>
                <g:select from="${sortedByName}" style="width:325px" name="supplierName" class="supplierName" id="id" optionKey="id" optionValue="name" value="${supplierPaymentInstance?.supplier?.id}" noSelection="['':'select one..']"/>
              </td>
            </tr>
            
            <g:if test="${params.id}">
              <tr class="prop">
                <td valign="top" class="name">
                  <label for="remark"><g:message code="supplierPayment.remark.label" default="Remarks" /></label>
	            </td>
    	        <td valign="top" class="value ${hasErrors(bean: supplierPaymentInstance, field: 'remark', 'errors')}">
        	      <g:textArea name="remark" maxlength="255" value="${supplierPaymentInstance?.remark}" />
                </td>
              </tr>
            </g:if>
            
          </tbody>
        </table>

        <g:if test="${params.id}">
          <div class="list">
            <table>
                <thead>
                    <th class="center"><g:checkBox name="checkAll" onClick="checkFields(document.getElementsByName('purchaseInvoice'), this)"/></th>
                    <th>Invoice Date</th>
                    <th>Reference</th>
                    <th>Supplier Reference</th>
                    <th class="right">Amount</th>
                </thead>
                <tbody>
                    <g:each in="${purchaseInvoiceList}" var="invoice" status="color">
                        <tr class="${(color % 2) == 0 ? 'odd' : 'even'}">
                            <td class="center"><g:checkBox name="purchaseInvoice" value="${invoice.id}" checked="${false}"/></td>
                            <td><g:formatDate date="${invoice.invoiceDate}" format="MM/dd/yyyy"/></td>
                            <td>${invoice.reference}</td>
                            <td>${invoice.supplierReference}</td>
                            <td class="right">${invoice.formatPurchaseInvoiceDiscountedPhpTotal()}</td>
                        </tr>

                    </g:each>
                </tbody>
            </table>
          </div>
        </g:if>
      </div>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
