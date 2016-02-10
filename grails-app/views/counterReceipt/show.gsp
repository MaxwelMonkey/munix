<%@ page import="com.munix.CounterReceipt" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'counterReceipt.label', default: 'CounterReceipt')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="counterReceipt.id.label" default="Id" /></td>
        <td valign="top" class="value">${counterReceiptInstance}</td>
        <td valign="top" class="name"><g:message code="counterReceipt.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${counterReceiptInstance?.date}" format="MM/dd/yyyy" /></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="counterReceipt.customer.label" default="Customer" /></td>
        <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${counterReceiptInstance?.customer?.id}">${counterReceiptInstance?.customer?.encodeAsHTML()}</g:link></td>
        <td class="name"><g:message code="counterReceipt.dueDate.label" default="Due Date" /></td>
        <td class="value"><g:formatDate date="${counterReceiptInstance?.dueDate}" format="MM/dd/yyyy" /></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="counterReceipt.counterDate.label" default="Counter Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${counterReceiptInstance?.counterDate}" format="MM/dd/yyyy" /> 
          <g:each in="${collectionSchedules.collectionScheduleForCounter?}" var="scheduleForCounter">
	       	<g:link id="${scheduleForCounter.id}" controller="collectionSchedule" action="show">[${scheduleForCounter}]</g:link>
       	  </g:each>
        </td>
        <td valign="top" class="name"><g:message code="counterReceipt.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${counterReceiptInstance?.preparedBy}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="counterReceipt.collectionDate.label" default="Collection Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${counterReceiptInstance?.collectionDate}" format="MM/dd/yyyy" />
          <g:each in="${collectionSchedules.collectionScheduleForCollection?}" var="scheduleForCollection">
            <g:link id="${scheduleForCollection?.id}" controller="collectionSchedule" action="show">[${scheduleForCollection}]</g:link>
          </g:each>
        </td>
        <td valign="top" class="name"><g:message code="counterReceipt.approvedBy.label" default="Approved By" /></td>
        <td valign="top" class="value">${counterReceiptInstance?.approvedBy}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        <td valign="top" class="name"><g:message code="counterReceipt.cancelledBy.label" default="Cancelled By" /></td>
        <td valign="top" class="value">${counterReceiptInstance?.cancelledBy}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="counterReceipt.status.label" default="Status" /></td>
          <td valign="top" class="value">${fieldValue(bean: counterReceiptInstance, field: "status")}</td>
          <td valign="top" class="name"><g:message code="counterReceipt.remark.label" default="Remarks" /></td>
          <td valign="top" class="value">${fieldValue(bean: counterReceiptInstance, field: "remark")}</td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table>
        <h2>Invoices</h2>
        <thead>
        <th>ID</th>
        <th>Type</th>
        <th>Date</th>
        <th class="right">Original Amount</th>
        <th class="right">Amount</th>
        <th class="right">Paid</th>
        <th class="right">Due</th>
        </thead>
        <tbody class="editable">
        <g:each in="${deliveries.sort{it.invoice.salesDeliveryId}}" var="delivery" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}salesDelivery/show/${delivery?.invoice?.id}'">
            <td>${delivery?.invoice}</td>
            <td>Delivery</td>
            <td><g:formatDate date="${delivery?.invoice?.date}" format="MM/dd/yyyy"/></td>
            <td class="right">PHP <g:formatNumber number="${delivery?.invoice?.computeTotalAmount()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${delivery?.amount}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${delivery?.invoice?.amountPaid}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${delivery?.invoice?.computeAmountDue()}" format="###,##0.00" /></td>
          </tr>
        </g:each>

        <g:each in="${charges.sort{it.invoice.id}}" var="charge" status="colors">
          <tr class="${(colors % 2) == 0 ? 'even' : 'odd'}" onclick="window.location='${createLink(uri:'/')}customerCharge/show/${charge?.invoice?.id}'">
            <td>${charge?.invoice}</td>
            <td>Charge</td>
            <td><g:formatDate date="${charge?.invoice?.date}" format="MM/dd/yyyy"/></td>
            <td class="right">PHP <g:formatNumber number="${charge?.invoice?.computeTotalAmount()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${charge?.amount}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${charge?.invoice?.amountPaid}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${charge?.invoice?.computeProjectedDue()}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        
        <g:each in="${creditMemos.sort{it.invoice.id}}" var="creditMemo" status="colors">
          <tr class="${(colors % 2) == 0 ? 'even' : 'odd'}" onclick="window.location='${createLink(uri:'/')}creditMemo/show/${creditMemo?.invoice?.id}'">
            <td>${creditMemo?.invoice}</td>
            <g:set var="creditMemoTotalAmount" value="${creditMemo?.invoice?.computeCreditMemoTotalAmount()}"/>
            
            <g:if test="${creditMemoTotalAmount<1}">
            	<td>Credit Memo</td>
            </g:if>
            <g:else>
            	<td>Debit Memo</td>
            </g:else>
            
            <td><g:formatDate date="${creditMemo?.invoice?.date}" format="MM/dd/yyyy"/></td>
            <td class="right">PHP (<g:formatNumber number="${creditMemo?.invoice?.computeCreditMemoTotalAmount()}" format="###,##0.00" />)</td>
            <td class="right">PHP (<g:formatNumber number="${creditMemo?.amount}" format="###,##0.00" />)</td>            
            <td class="right">PHP <g:formatNumber number="${creditMemo?.invoice?.amountPaid}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${creditMemo?.invoice?.computeProjectedDue()}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        
        <g:each in="${bouncedChecks.sort{it.invoice.id}}" var="bouncedCheck" status="colors">
          <tr class="${(colors % 2) == 0 ? 'even' : 'odd'}" onclick="window.location='${createLink(uri:'/')}bouncedCheck/show/${bouncedCheck?.invoice.id}'">
            <td>${bouncedCheck?.invoice}</td>
            <td>Bounced Check</td>
            <td><g:formatDate date="${bouncedCheck?.invoice?.date}" format="MM/dd/yyyy"/></td>
            <td class="right">PHP <g:formatNumber number="${bouncedCheck?.invoice.computeTotalAmount()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${bouncedCheck?.amount}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${bouncedCheck?.invoice?.amountPaid}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${bouncedCheck?.invoice?.computeProjectedDue()}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        
        <tbody>
        <tfoot>
          <tr class="total">
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <g:formatNumber number="${counterReceiptInstance?.computeOriginalAmountTotal()}" format="###,##0.00" /></strong></td>
            <td class="right"><strong>PHP <g:formatNumber number="${counterReceiptInstance?.computeInvoicesTotal()}" format="###,##0.00" /></strong></td>
            <td class="right"><strong>PHP <g:formatNumber number="${counterReceiptInstance?.computeAmountPaidTotal()}" format="###,##0.00" /></strong></td>
            <td class="right"><strong>PHP <g:formatNumber number="${counterReceiptInstance?.computeInvoicesAmountDueTotal()}" format="###,##0.00" /></strong></td>
          </tr>
        </tfoot>
      </table>

    </div>
    <div class="subTable">
      <table>
        <h2>Print Logs</h2>
        <thead>
            <th>User</th>
            <th>Date</th>
        </thead>
        <tbody>
            <g:each in="${counterReceiptInstance?.printLogs}" var="i">
                <tr>
                  <td>
                    ${i.user.userRealName}
                  </td>
                  <td><g:formatDate date="${i?.date}" format="MM/dd/yyyy" /></td>
                </tr>
            </g:each>
        <tbody>
        <tfoot>
          <tr class="total">
            <td><strong>Print Count</strong></td>
            <td>${counterReceiptInstance?.printLogs.size()}</td>
          </tr>
        </tfoot>
      </table>

    </div>
      <div class="buttons">
        <g:form>
          <g:hiddenField name="id" value="${counterReceiptInstance?.id}" />
            <g:if test="${counterReceiptInstance?.isUnapproved()}">
              <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
              <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
                <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
              </g:ifAnyGranted>
              <g:ifAnyGranted role="ROLE_ACCOUNTING">
                <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
              </g:ifAnyGranted>
            </g:if>
            <g:if test="${counterReceiptInstance?.isApproved()}">
                <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
                    <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.unapprove.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
              </g:ifAnyGranted>
            </g:if>
            <%--<span class="button"><g:actionSubmit class="print" action="doPrint" value="Print CR"/><g:actionSubmit class="print" action="doPrint" value="Print SOA"/></span> --%>
        	<span class="button"><g:link controller="print" class="print" target="counterReceipt" action="counterReceipt" params="${[id:params.id, receiptType:'CR'] }">Print CR</g:link> <g:link controller="print" class="print" action="counterReceipt" target="statementOfAccount" params="${[id:params.id, receiptType:'SOA'] }">Print SOA</g:link></span>
        </g:form>
      </div>
  </div>
</body>
</html>
