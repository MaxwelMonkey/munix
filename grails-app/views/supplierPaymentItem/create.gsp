<%@ page import="com.munix.SupplierPaymentItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="jquery.ui.core.js" />
  <g:javascript src="numbervalidation.js" />
  <g:javascript>
    SUPPLIER_PAYMENT_ITEM = {
    	initPage: function() {
    		var selectedIndex = $("#type").attr("selectedIndex") - 1
        	var isCheck = $("#isCheck" + selectedIndex).val()
            if(isCheck == "true") {
                enableCheckElements()
            } else {
                disableCheckElements()
            }
    	}
    }
		    
    var enableCheckElements = function() {
        $(".check").removeAttr('disabled');
        $(".check").css({"background-color": "white"})     
    }

    var disableCheckElements = function() {
        $(".check").attr('disabled', 'disabled')
        $(".check").css({"background-color": "CCCCCC"})
        $(".check").val("")
    }
    
    $(document).ready(function() {
        $("#type").change(function() {
            var selectedIndex = this.value - 1
            var isCheck = $("#isCheck" + selectedIndex).val() 
            if(isCheck == "true") {
                enableCheckElements()
            } else {
                disableCheckElements()
            }
        })
        SUPPLIER_PAYMENT_ITEM.initPage()
        $("#amount").ForceNumericOnly(true)
    })
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <g:set value="${com.munix.SupplierPayment.get(params?.id)}" var="supplierPaymentInstance" />
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${supplierPaymentItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${supplierPaymentItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="payment"><g:message code="supplierPaymentItem.payment.label" default="Payment" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'payment', 'errors')}">
          <g:link action="show" controller="supplierPayment" id="${supplierPaymentInstance?.id}">${supplierPaymentInstance}</g:link>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="supplierPaymentItem.type.label" default="Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'type', 'errors')}">
              <g:select name="type.id" id="type" from="${com.munix.PaymentType.list()}" optionKey="id" value="${supplierPaymentItemInstance?.type?.id}" noSelection="['null':'select one..']"/>
              <g:each in="${com.munix.PaymentType.list()}" var="paymentType" status="i">
                <g:hiddenField name="isCheck${i}" value="${paymentType.isCheck}"/>
              </g:each>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="amount"><g:message code="supplierPaymentItem.amount.label" default="Amount" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'amount', 'errors')}">
              <g:textField name="amount" value="${fieldValue(bean: supplierPaymentItemInstance, field: 'amount')}" />
            </td>
          </tr>

          <calendar:resources lang="en" theme="aqua"/>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="date"><g:message code="supplierPaymentItem.date.label" default="Date" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'date', 'errors')}">
              <calendar:datePicker name="date"  years="2009,2030" value="${supplierPaymentItemInstance?.date}"/>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="checkNumber"><g:message code="supplierPaymentItem.checkNumber.label" default="Check Number" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'checkNumber', 'errors')}">
          <g:textField class="check" name="checkNumber" value="${supplierPaymentItemInstance?.checkNumber}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="checkBank"><g:message code="supplierPaymentItem.checkBank.label" default="Check Bank" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'checkBank', 'errors')}">
              <g:select class="check" name="checkBank.id" from="${com.munix.Bank.list().sort{it.toString()}}" optionKey="id" value="${supplierPaymentItemInstance?.checkBank?.id}" noSelection="['null': '']" />
            </td>
          </tr>

		  <tr class="prop">
            <td valign="top" class="name">
              <label for="checkBranch"><g:message code="supplierPaymentItem.checkBranch.label" default="Branch" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'checkBranch', 'errors')}">
          <g:textField class="check" name="checkBranch" value="${supplierPaymentItemInstance?.checkBranch}" />
          </td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name">
              <label for="checkType"><g:message code="supplierPaymentItem.checkType.label" default="Check Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'checkType', 'errors')}">
          <g:select class="check" name="checkType.id" from="${com.munix.CheckType.list().sort{Integer.parseInt(it.routingNumber)}}" optionKey="id" value="${supplierPaymentItemInstance?.checkType?.id}" noSelection="['null': '']" />
          </td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="supplierPaymentItem.remark.label" default="Remarks" /></label>
	        </td>
    	    <td valign="top" class="value ${hasErrors(bean: supplierPaymentItemInstance, field: 'remark', 'errors')}">
        	  <g:textArea name="remark" maxlength="255" value="${supplierPaymentItemInstance?.remark}" />
            </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <input type="hidden" name="id" value="${supplierPaymentInstance?.id}"/>
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
