
<%@ page import="com.munix.CounterReceipt" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'counterReceipt.label', default: 'CounterReceipt')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <g:javascript>
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
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${counterReceiptInstance}">
      <div class="errors">
        <g:renderErrors bean="${counterReceiptInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${counterReceiptInstance?.id}" />
      <g:hiddenField name="version" value="${counterReceiptInstance?.version}" />
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
        
        <table>
          <thead>
            <th class="center"><g:checkBox name="checkAll" onClick="checkFields(document.getElementsByName('deliveries'), this)"/></th>
            <th class="center">Sales Delivery</th>
            <th class="center">Counter Receipt</th>
            <th class="center">Amount</th>
            <th class="center">Date</th>
          </thead>
          <tbody>
            <g:each in="${deliveries}" var="delivery">
              <g:set var="checked" value="${false}"/>
              <g:if test="${counterReceiptInstance.deliveries.contains(delivery)}">
                <g:set var="checked" value="${true}"/>
              </g:if>
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="deliveries" value="${delivery.id}" checked="${checked}" />
                </td>
                <td>
                  <g:link action="show" controller="salesDelivery" id="${delivery.id}">${delivery.salesDeliveryId}</g:link>
                </td>
                <td>
                  <g:each in="${delivery.counterReceipts}" var="counterReceipt">
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
            <th class="center">Customer Charge</th>
            <th class="center">Counter Receipt</th>
            <th class="center">Amount</th>
            <th class="center">Date</th>
          </thead>
          <tbody>
            <g:each in="${charges}" var="customerCharge">
              <g:set var="checked" value="${false}"/>
              <g:if test="${counterReceiptInstance.charges.contains(customerCharge)}">
                <g:set var="checked" value="${true}"/>
              </g:if>
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="charges" value="${customerCharge.id}" checked="${checked}"/>
                </td>
                <td>
                  <g:link action="show" controller="customerCharge" id="${customerCharge.id}">${customerCharge}</g:link>
                </td>
                <td>
               	  <g:each in="${customerCharge.counterReceipts}" var="counterReceipt">
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
            <th class="center">Credit Memo</th>
            <th class="center">Counter Receipt</th>
            <th class="center">Amount</th>
            <th class="center">Date</th>
          </thead>
          <tbody>
            <g:each in="${creditMemos}" var="creditMemo">
              <g:set var="checked" value="${false}"/>
              <g:if test="${counterReceiptInstance.creditMemos.contains(creditMemo)}">
                <g:set var="checked" value="${true}"/>
              </g:if>
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="creditMemos" value="${creditMemo.id}" checked="${checked}"/>
                </td>
                <td>
                  <g:link action="show" controller="creditMemo" id="${creditMemo.id}">${creditMemo}</g:link>
                </td>
                <td>
               	  <g:each in="${creditMemo.counterReceipts}" var="counterReceipt">
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
            <th class="center">Bounced Check</th>
            <th class="center">Counter Receipt</th>
            <th class="center">Amount</th>
            <th class="center">Date</th>
          </thead>
          <tbody>
            <g:each in="${bouncedChecks}" var="bouncedCheck">
              <g:set var="checked" value="${false}"/>
              <g:if test="${counterReceiptInstance.bouncedChecks.contains(bouncedCheck)}">
                <g:set var="checked" value="${true}"/>
              </g:if>
              <tr class="prop">
                <td valign="top" class="center">
                  <g:checkBox name="bouncedChecks" value="${bouncedCheck.id}" checked="${checked}"/>
                </td>
                <td>
                  <g:link action="show" controller="creditMemo" id="${bouncedCheck.id}">${bouncedCheck}</g:link>
                </td>
                <td>
                  <g:each in="${bouncedCheck.counterReceipts}" var="counterReceipt">
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
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
