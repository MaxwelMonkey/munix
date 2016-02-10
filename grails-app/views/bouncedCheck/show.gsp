<%@ page import="com.munix.BouncedCheck" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'bouncedCheck.label', default: 'BouncedCheck')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="filter"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
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
            <td valign="top" class="name"><g:message code="bouncedCheck.id.label" default="Id" /></td>
            <td valign="top" class="value">${bouncedCheckInstance}</td>
            <td valign="top" class="name"><g:message code="bouncedCheck.date.label" default="Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${bouncedCheckInstance?.date}" format="MM/dd/yyyy" /></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="bouncedCheck.customer.label" default="Customer" /></td>
            <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${bouncedCheckInstance?.customer?.id}">${bouncedCheckInstance?.customer?.encodeAsHTML()}</g:link></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="bouncedCheck.counterReceipt.label" default="Counter Receipt" /></td>
            <td>
              <g:each in="${bouncedCheckInstance?.counterReceipts.sort{it.id}}" var="receipt">
                <g:link controller="counterReceipt" action="show" id="${receipt.id}">${receipt.encodeAsHTML()}</g:link>
                <br/>
              </g:each>
            </td>
            <td valign="top" class="name"><g:message code="bouncedCheck.directPayment.label" default="Direct Payment" /></td>
            <td>
              <g:each in="${bouncedCheckInstance?.invoices}" var="invoice">
                <g:link controller="directPayment" action="show" id="${invoice.directPayment.id}">${invoice.directPayment.encodeAsHTML()}</g:link>
                <br/>
              </g:each>
            </td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name">Bounced Check Status</td>
            <td valign="top" class="value">${bouncedCheckInstance?.bouncedCheckStatus?.description}</td>
            <td valign="top" class="name">For Redeposit</td>
            <td valign="top" class="value">${forRedepositStatus}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="bouncedCheck.remark.label" default="Remarks" /></td>
            <td valign="top" class="value">${bouncedCheckInstance?.remark?.encodeAsHTML()}</td>
            <td valign="top" class="name"><g:message code="bouncedCheck.preparedBy.label" default="Prepared By" /></td>
            <td valign="top" class="value">${bouncedCheckInstance?.preparedBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="bouncedCheck.status.label" default="Status" /></td>
            <td valign="top" class="value">${bouncedCheckInstance?.status?.encodeAsHTML()}</td>
            <td valign="top" class="name"><g:message code="bouncedCheck.approvedBy.label" default="Approved By" /></td>
            <td valign="top" class="value">${bouncedCheckInstance?.approvedBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"></td>
            <td valign="top" class="value"></td>
            <td valign="top" class="name"><g:message code="bouncedCheck.cancelledBy.label" default="Cancelled By" /></td>
            <td valign="top" class="value">${bouncedCheckInstance?.cancelledBy?.encodeAsHTML()}</td>
          </tr>

        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <thead>
          <tr>
            <th class="center">Check Number</th>
            <th class="center">Bank</th>
            <th class="center">Date</th>
            <th class="center">Check Deposit</th>
            <th class="center">Redeposited</th>
            <th class="center">Amount</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${bouncedCheckInstance.checks.sort{it.id}}" var="check" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directPayment/show/${check?.directPaymentItem?.directPayment?.id}'">
            <td>${check?.checkNumber}</td>
            <td>${check?.bank?.identifier} - ${check?.branch}</td>
            <td class="right"><g:formatDate date="${check?.date}" format="MM/dd/yyyy"/></td>
            <td><g:link controller="checkDeposit" action="show" id="${check?.retrieveCurrentCheckDeposit()?.id}">${check?.retrieveCurrentCheckDeposit()}</g:link></td>
			<td>${check?.isDeposited()}</td>
            <td class="right">${check?.formatAmount()}</td>
          </tr>
        </g:each>
        </tbody>

        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${bouncedCheckInstance?.formatTotal()}</strong></td>
          </tr>
        </tfoot>

      </table>
    </div>
    
    <h1>Payments</h1>
    <div class="subTable">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Status</th>
            <th class="right">Total</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${bouncedCheckInstance.invoices}" var="invoice" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directPayment/show/${invoice?.directPayment?.id}'">
            <td>${invoice?.directPayment}</td>
            <td><g:formatDate date="${invoice?.directPayment?.date}" format="MM/dd/yyyy" /></td>
            <td>${invoice?.directPayment?.status}</td>
            <td class="right"><g:formatNumber number="${invoice?.amount}" format="#,##0.00" /></td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    
      <div class="buttons">
        <g:form>
        <g:hiddenField name="id" value="${bouncedCheckInstance?.id}" />
	    <g:if test="${bouncedCheckInstance.isUnapproved()}">
          <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>          
          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
          <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
            <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:ifAnyGranted>
    	</g:if>
	    <g:if test="${bouncedCheckInstance.isUnapprovable()}">
          <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
            <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.approve.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:ifAnyGranted>
    	</g:if>
         <span class="button"><g:link class="print" target="blank" controller="print" action="bouncedCheck" id="${bouncedCheckInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
        </g:form>
      </div>
  </div>
</body>
</html>
