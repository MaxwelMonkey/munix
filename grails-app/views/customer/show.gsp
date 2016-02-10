
<%@ page import="com.munix.Customer" %>
<%@ page import="com.munix.CustomerDiscountLog" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
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
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="customer.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "identifier")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.name.label" default="Name" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "name")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.tin.label" default="TIN" /></td>
        <td valign="top" class="value">${customerInstance?.tin?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.type.label" default="Type" /></td>
        <td valign="top" class="value">${customerInstance?.type?.description?.encodeAsHTML()}</td>
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
          <td valign="top" class="name"><g:message code="customer.owner.label" default="Owner" /></td>
        <td valign="top" class="value">${customerInstance?.owner?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.busAddrStreet.label" default="Business Address" /></td>
        <td valign="top" class="value">${customerInstance?.formatBusinessAddress()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.bilAddrStreet.label" default="Billing Address" /></td>
        <td valign="top" class="value">${customerInstance?.formatBillingAddress()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.forwarder.label" default="Forwarder" /></td>
        <td valign="top" class="value"><g:link controller="forwarder" action="show" id="${customerInstance?.forwarder?.id}">${customerInstance?.forwarder?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="customer.declaredValue.label" default="Declared Value" /></td>
        <td valign="top" class="value">${customerInstance?.formatDeclaredValue()}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.salesAgent.label" default="Sales Agent" /></td>
        <td valign="top" class="value"><g:link controller="salesAgent" action="show" id="${customerInstance?.salesAgent?.id}">${customerInstance?.salesAgent?.encodeAsHTML()}</g:link></td>
        <td valign="top" class="name"><g:message code="customer.commissionRate.label" default="Commission Rate" /></td>
        <td valign="top" class="value">${customerInstance?.formatCommissionRate()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.creditLimit.label" default="Credit Limit" /></td>
        <td valign="top" class="value">${customerInstance?.formatCreditLimit()}</td>
        <td valign="top" class="name"><g:message code="customer.creditRemark.label" default="Credit Remarks" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "creditRemark")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.enableCreditLimit.label" default="Enable Credit Limit" /></td>
        <td valign="top" class="value"><g:formatBoolean boolean="${customerInstance?.enableCreditLimit}" /></td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.term.label" default="Terms" /></td>
        <td valign="top" class="value">${customerInstance?.term?.encodeAsHTML()}</td>
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
          <td valign="top" class="name"><g:message code="customer.bank1.label" default="Bank 1" /></td>
        <td valign="top" class="value">${customerInstance?.bank1?.identifier?.encodeAsHTML()} - ${customerInstance?.branch1}</td>
        <td valign="top" class="name"><g:message code="customer.collectionMode.label" default="Collection Mode" /></td>
        <td valign="top" class="value">${customerInstance?.collectionMode?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountNumber1.label" default="Account Number 1" /></td>
        <td valign="top" class="value">${customerInstance?.accountNumber1?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="customer.collectionPreference.label" default="Collection Preference" /></td>
        <td valign="top" class="value">${customerInstance?.collectionPreference?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountName1.label" default="Account Name 1" /></td>
        <td valign="top" class="value">${customerInstance?.accountName1?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="customer.collectionSchedule.label" default="Collection Schedule" /></td>
        <td valign="top" class="value">${customerInstance?.collectionSchedule?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.bank2.label" default="Bank 2" /></td>
        <td valign="top" class="value">${customerInstance?.bank2?.identifier?.encodeAsHTML()} - ${customerInstance?.branch2}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountNumber2.label" default="Account Number 2" /></td>
        <td valign="top" class="value">${customerInstance?.accountNumber2?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountName2.label" default="Account Name 2" /></td>
        <td valign="top" class="value">${customerInstance?.accountName2?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.bank3.label" default="Bank 3" /></td>
        <td valign="top" class="value">${customerInstance?.bank3?.identifier?.encodeAsHTML()} - ${customerInstance?.branch3}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountNumber3.label" default="Account Number 3" /></td>
        <td valign="top" class="value">${customerInstance?.accountNumber3?.encodeAsHTML()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.accountName3.label" default="Account Name 3" /></td>
        <td valign="top" class="value">${customerInstance?.accountName3?.encodeAsHTML()}</td>
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
          <td valign="top" class="name"><g:message code="customer.contact.label" default="Main Contact" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "contact")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.contact.label" default="Position" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "contactPosition")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.secondaryContact.label" default="Secondary Contact" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "secondaryContact")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.secondaryContactPosition.label" default="Position" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "secondaryContactPosition")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.landline.label" default="Landline" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "landline")}</td>
        <td valign="top" class="name"><g:message code="customer.email.label" default="Email" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "email")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.mobile.label" default="Mobile" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "mobile")}</td>
        <td valign="top" class="name"><g:message code="customer.yahoo.label" default="Yahoo" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "yahoo")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.fax.label" default="Fax" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "fax")}</td>
        <td valign="top" class="name"><g:message code="customer.skype.label" default="Skype" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "skype")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.website.label" default="Website" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "website")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.autoApprove.label" default="Auto Approve (Sales Order)" /></td>
          <td valign="top" class="value"><g:formatBoolean boolean="${customerInstance?.autoApprove}" /></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.autoSecondApprove.label" default="Auto Second Approve (Sales Order)" /></td>
        <td valign="top" class="value"><g:formatBoolean boolean="${customerInstance?.autoSecondApprove}" /></td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.generalRemark.label" default="General Remarks" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "generalRemark")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.status.label" default="Status" /></td>
        <td valign="top" class="value">${fieldValue(bean: customerInstance, field: "status")}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="customer.managerSecondApprove.label" default="2nd Approve only by Manager" /></td>
          <td valign="top" class="value"><g:formatBoolean boolean="${customerInstance?.managerSecondApprove}" /></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        </tbody>
      </table>
    </div>
      <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${customerInstance?.id}" />
        <g:hiddenField name="customerId" value="${customerInstance?.id}" />
        <g:ifAnyGranted role="ROLE_ACCOUNTING">
        	<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
       	  	<span class="button"><g:actionSubmit class="viewTable" action="auditLogs" value="${message(code: 'default.button.auditLogs.label', default: 'View History of Changes')}" /></span>
		</g:ifAnyGranted>
        <span class="button"><g:link controller="customerLedger" action="show" id="${customerInstance?.customerLedger?.id}" elementId="viewCustomerLedger">View Ledger</g:link></span>
        <span class="button"><g:link action="salesSummary" id="${customerInstance?.id}">Sales Summary</g:link></span>
      </g:form>
    </div>
    <div class="subTable">
      <table>
        <h2>Discount</h2>
        <g:link class="addItem" controller="customerDiscount" action="create" id="${customerInstance?.id}" elementId="addCustomerDiscount">Add Item</g:link>
        <thead>
          <tr>
            <th class="center">Discount Type</th>
            <th>Discount Group</th>
            <th>Type</th>
            <th>Log</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${customerInstance.discounts.sort{it.id}}" var="customerDiscount" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}customerDiscount/edit/${customerDiscount.id}'">
            <td>${customerDiscount?.discountType}</td>
            <td>${customerDiscount?.discountGroup}</td>
			<td>${customerDiscount?.type}</td>
            <td>
            	<g:each in="${customerDiscount?.log?.items}" var="log">
	               ${log.formatLog()}<br/>
                </g:each>
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

  </div>
</body>
</html>
