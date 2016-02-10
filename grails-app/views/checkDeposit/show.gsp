
<%@ page import="com.munix.CheckDeposit" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'checkDeposit.label', default: 'CheckDeposit')}" />
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
            <td valign="top" class="name"><g:message code="checkDeposit.id.label" default="Id" /></td>
            <td valign="top" class="value">${checkDepositInstance}</td>
            <td valign="top" class="name"><g:message code="checkDeposit.preparedBy.label" default="Prepared By" /></td>
            <td valign="top" class="value">${checkDepositInstance?.preparedBy}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="checkDeposit.depositDate.label" default="Deposit Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${checkDepositInstance?.depositDate}" format="MM/dd/yyyy" /></td>
            <td valign="top" class="name"><g:message code="checkDeposit.approvedBy.label" default="Approved By" /></td>
            <td valign="top" class="value">${checkDepositInstance?.approvedBy}</td>
          </tr>
          
          <tr class="prop">
            <td class="name"></td>
            <td class="value"></td>
            <td valign="top" class="name"><g:message code="checkDeposit.cancelledBy.label" default="Cancelled By" /></td>
            <td valign="top" class="value">${checkDepositInstance?.cancelledBy}</td>
          </tr>

          <tr class="prop">
            <td class="name"></td>
            <td class="value"></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="checkDeposit.account.label" default="Account Number" /></td>
            <td valign="top" class="value">${checkDepositInstance?.account?.accountNumber}</td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="checkDeposit.accountName.label" default="Account Name" /></td>
            <td valign="top" class="value">${checkDepositInstance?.account?.accountName}</td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="checkDeposit.bank.label" default="Account Bank" /></td>
            <td valign="top" class="value">${checkDepositInstance?.account?.bank}</td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td class="name"></td>
            <td class="value"></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="checkDeposit.billsPurchase.label" default="Bills Purchase" /></td>
            <td valign="top" class="value"><g:formatBoolean boolean="${checkDepositInstance?.billsPurchase}" /></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="checkDeposit.status.label" default="Status" /></td>
            <td valign="top" class="value">${fieldValue(bean: checkDepositInstance, field: "status")}</td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <thead>
          <tr>
            <th>Check Number</th>
            <th class="center">Check Date</th>
            <th>Customer</th>
            <th>Bank - Branch</th>
		    <th class="right">Type</th>
            <th class="right">Amount</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${checkDepositInstance.checks.sort{it.id}}" var="check" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
            <td>${check?.checkNumber}</td>
            <td><g:formatDate date="${check?.date}" format="MM/dd/yyyy" /></td>
            <td>${check?.customer}</td>
            <td>${check?.bank?.identifier} - ${check?.branch}</td>            
            <td>${check?.type?.description}</td>
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
            <td class="right"><strong>${checkDepositInstance?.formatTotal()}</strong></td>
          </tr>
        </tfoot>
      </table>
    </div>
    <div class="subTable">
	<table>
	  <h2>Print History</h2>
	  <thead>
		<tr>
		  <th>Printed By</th>
		  <th>Date</th>
		  <th>Type</th>
		</tr>
	  </thead>
	  <tbody class="editable">
	  <g:each in="${checkDepositInstance?.printLogs?.sort{it?.date}}" var="i" status="colors">
		<tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
		  <td>${i.user.userRealName}</td>
		  <td><g:formatDate date="${i?.date}" format="MMM. dd, yyyy - hh:mm a" /></td>
		  <td>${i?.type}</td>
		</tr>
	  </g:each>
	  	 <tfoot class="total">
          <tr>
             <td>Print Count</td>
			 <td></td>
			 <td>${checkDepositInstance.printLogs.size()}</td>
          </tr>
        </tfoot>
	  </tbody>
	</table>
  </div>
      <div class="buttons">
        <g:form>
          <g:hiddenField name="id" value="${checkDepositInstance?.id}" />
          <g:if test="${checkDepositInstance?.isUnapproved()}">
            <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
          <g:ifAnyGranted role="ROLE_MANAGER_SALES,ROLE_ACCOUNTING">  
            <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
          </g:ifAnyGranted>
          <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
            <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Clear')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:ifAnyGranted>
          </g:if>
	      <span class="button"><g:actionSubmit action="doPrint" value="MBTC"/></span>
	      <span class="button"><g:actionSubmit action="doPrint" value="MBTC-BP"/></span>
	      <span class="button"><g:actionSubmit action="doPrint" value="BDO"/></span>
	      <span class="button"><g:actionSubmit action="doPrint" value="EWB"/></span>
        </g:form>
      </div>

  </div>
</body>
</html>
