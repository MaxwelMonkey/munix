
<%@ page import="com.munix.CounterReceipt" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'counterReceipt.label', default: 'CounterReceipt')}" />
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

      $(".customerId").change(function () {
        createCounterReceipt($(this).val())
      })
      $(".customerName").change(function () {
        createCounterReceipt($(this).val())
      })
    })
    
    var createCounterReceipt = function(fieldValue) {      
        window.location="${createLink(uri:'/')}counterReceipt/create/" + fieldValue          
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
    <g:hasErrors bean="${counterReceiptInstance}">
      <div class="errors">
        <g:renderErrors bean="${counterReceiptInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>
            <g:set var="customerList" value="${com.munix.Customer.list().sort{it.identifier}}" />
            <g:set var="customer" value="${com.munix.Customer.get(params?.id)}" />
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="counterReceipt.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: counterReceiptInstance, field: 'customer', 'errors')}" width="650px">
              <g:set var="customersSortedByIdentifier" value="${com.munix.Customer.list().sort{it.identifier}}" />
                <g:select name="customer.id" class="customerId" from="${customersSortedByIdentifier}" optionKey="id" optionValue="identifier" value="${customer?.id}" noSelection="['':'select one..']"/>
                <g:set var="customersSortedByName" value="${com.munix.Customer.list().sort{it.name}}" />
                <g:select name="customerName" class="customerName" from="${customersSortedByName}" optionKey="id" optionValue="name" value="${customer?.id}" noSelection="['':'select one..']"/>
              </td>
            </tr>

          </tbody>
        </table>
      </div>
      <g:if test="${params.id}">
      <div class="subtable">
        <table>
         <tbody>
           <tr class="prop">
              <td valign="top" class="name">
                <label for="dueDate"><g:message code="counterReceipt.dueDate.label" default="Due Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: counterReceiptInstance, field: 'dueDate', 'errors')}">
                <g:datePicker name="dueDate" precision="day" value="${counterReceiptInstance?.dueDate}"  />
              </td>
            </tr>
           <tr class="prop">
             <td valign="top" class="name">
               <label for="remark"><g:message code="counterReceipt.remark.label" default="Remarks" /></label>
             </td>
             <td valign="top" class="value ${hasErrors(bean: counterReceiptInstance, field: 'remark', 'errors')}">
               <g:textArea name="remark" value="${counterReceiptInstance?.remark}" maxlength="255"/>
             </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="subtable">
        <table>
          <thead>
            <th class="center"><g:checkBox name="checkAll" onClick="checkFields(document.getElementsByName('deliveries'), this)"/></th>
            <th>Sales Delivery</th>
            <th>Counter Receipt</th>
            <th>Amount</th>
            <th>Date</th>
          </thead>
          <tbody>
            <g:each in="${deliveries.sort{it.id}}" var="delivery">
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="deliveries" value="${delivery.id}" checked="false" />
                </td>
                <td>
                  <g:link action="show" controller="salesDelivery" id="${delivery.id}">${delivery.salesDeliveryId}</g:link>
                </td>
                <td>
                  <g:each in="${delivery.counterReceipts.sort{it.id}}" var="counterReceipt">
                    <g:link action="show" controller="counterReceipt" id="${counterReceipt.id}">${counterReceipt}</g:link>
                  </g:each>
                </td>
                <td class="right">PHP <g:formatNumber number="${delivery?.computeProjectedDue()}" format="###,##0.00" /></td>
                <td class="right" ><g:formatDate date="${delivery?.date}" format="MM/dd/yyyy"/></td> 
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="subtable">
        <table>
          <thead>
            <th class="center"><g:checkBox name="checkAll" onClick="checkFields(document.getElementsByName('charges'), this)"/></th>
            <th>Customer Charge</th>
            <th>Counter Receipt</th>
            <th>Amount</th>
            <th>Date</th>
          </thead>
          <tbody>
            <g:each in="${charges.sort{it.id}}" var="customerCharge">
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="charges" value="${customerCharge.id}" checked="false"/>
                </td>
                <td>
                  <g:link action="show" controller="customerCharge" id="${customerCharge.id}">${customerCharge}</g:link>
                </td>
                <td>
                  <g:each in="${customerCharge.counterReceipts.sort{it.id}}" var="counterReceipt">
                    <g:link action="show" controller="counterReceipt" id="${counterReceipt.id}">${counterReceipt}</g:link>
                  </g:each>
                </td>
                <td class="right">PHP <g:formatNumber number="${customerCharge?.computeProjectedDue()}" format="###,##0.00" /></td>
                <td class="right" ><g:formatDate date="${customerCharge?.date}" format="MM/dd/yyyy"/></td>
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="subtable">
        <table>
          <thead>
            <th class="center"><g:checkBox name="checkAll" onClick="checkFields(document.getElementsByName('creditMemos'), this)"/></th>
            <th>Credit Memo</th>
            <th>Counter Receipt</th>
            <th>Amount</th>
            <th>Date</th>
          </thead>
          <tbody>
            <g:each in="${creditMemos.sort{it.id}}" var="creditMemo">
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="creditMemos" value="${creditMemo.id}" checked="false"/>
                </td>
                <td>
                  <g:link action="show" controller="creditMemo" id="${creditMemo.id}">${creditMemo}</g:link>
                </td>
                <td>
              	  <g:each in="${creditMemo.counterReceipts.sort{it.id}}" var="counterReceipt">
                    <g:link action="show" controller="counterReceipt" id="${counterReceipt.id}">${counterReceipt}</g:link>
                  </g:each>
                </td>
                <g:set var="creditMemoTotalAmount" value="${creditMemo?.computeCreditMemoTotalAmount()}"/>
                  <g:if test="${creditMemoTotalAmount<0}">
                    <td class="right">PHP (<g:formatNumber number="${creditMemoTotalAmount.abs()}" format="###,##0.00" />)</td>
                  </g:if>
                  <g:else>
                    <td class="right">PHP <g:formatNumber number="${creditMemoTotalAmount}" format="###,##0.00" /></td>
                  </g:else>
                <td class="right" ><g:formatDate date="${creditMemo?.date}" format="MM/dd/yyyy"/></td>
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="subtable">
        <table>
          <thead>
            <th class="center"><g:checkBox name="checkAll" onClick="checkFields(document.getElementsByName('bouncedChecks'), this)"/></th>
            <th>Bounced Check</th>
            <th>Counter Receipt</th>
            <th>Amount</th>
            <th>Date</th>
          </thead>
          <tbody>
            <g:each in="${bouncedChecks.sort{it.id}}" var="bouncedCheck">
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="bouncedChecks" value="${bouncedCheck.id}" checked="false"/>
                </td>
                <td>
                  <g:link action="show" controller="bouncedCheck" id="${bouncedCheck.id}">${bouncedCheck}</g:link>
                </td>
                <td>
                  <g:each in="${bouncedCheck.counterReceipts.sort{it.id}}" var="counterReceipt">
                    <g:link action="show" controller="counterReceipt" id="${counterReceipt.id}">${counterReceipt}</g:link>
                  </g:each>
                </td>
                <td class="right">PHP <g:formatNumber number="${bouncedCheck?.computeProjectedDue()}" format="###,##0.00" /></td>
                <td class="right" ><g:formatDate date="${bouncedCheck?.date}" format="MM/dd/yyyy"/></td>
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      </g:if>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
