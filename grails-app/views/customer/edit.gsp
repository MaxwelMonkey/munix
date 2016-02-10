
<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'customer.label', default: 'Customer')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <g:javascript src="generalmethods.js" />
  <g:javascript src="numbervalidation.js" />
  <g:javascript>
    	$(document).ready(function(){
     		$(".declaredValue").ForceNumericOnly(true)
     		$(".commissionRate").ForceNumericOnly(true)
     		$(".creditLimit").ForceNumericOnly(true)
    	})
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
    <g:hasErrors bean="${customerInstance}">
      <div class="errors">
        <g:renderErrors bean="${customerInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${customerInstance?.id}" />
      <g:hiddenField name="version" value="${customerInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="identifier"><g:message code="customer.identifier.label" default="Identifier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'identifier', 'errors')}">
          <g:textField name="identifier" maxlength="255" value="${customerInstance?.identifier}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="name"><g:message code="customer.name.label" default="Name" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'name', 'errors')}">
          <g:textField name="name" maxlength="255" value="${customerInstance?.name}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="tin"><g:message code="customer.tin.label" default="TIN" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'tin', 'errors')}">
          <g:textField name="tin" maxlength="255" value="${customerInstance?.tin}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="type"><g:message code="customer.type.label" default="Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'type', 'errors')}">
          <g:select name="type.id" from="${com.munix.CustomerType.list().sort{it.toString()}}" optionKey="id" optionValue="description" value="${customerInstance?.type?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="owner"><g:message code="customer.owner.label" default="Owner" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'owner', 'errors')}">
          <g:textField name="owner" maxlength="255" value="${customerInstance?.owner}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="busAddrStreet"><g:message code="customer.busAddrStreet.label" default="Street (Business)" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'busAddrStreet', 'errors')}">
          <g:textField name="busAddrStreet" maxlength="255" value="${customerInstance?.busAddrStreet}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="busAddrCity"><g:message code="customer.busAddrCity.label" default="City (Business)" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'busAddrCity', 'errors')}">
          <g:select name="busAddrCity.id" from="${com.munix.City.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.busAddrCity?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="busAddrZip"><g:message code="customer.busAddrZip.label" default="Zip (Business)" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'busAddrZip', 'errors')}">
          <g:textField name="busAddrZip" maxlength="255" value="${customerInstance?.busAddrZip}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="bilAddrStreet"><g:message code="customer.bilAddrStreet.label" default="Street (Billing)" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'bilAddrStreet', 'errors')}">
          <g:textField name="bilAddrStreet" maxlength="255" value="${customerInstance?.bilAddrStreet}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="bilAddrCity"><g:message code="customer.bilAddrCity.label" default="City (Billing)" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'bilAddrCity', 'errors')}">
          <g:select name="bilAddrCity.id" from="${com.munix.City.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.bilAddrCity?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="bilAddrZip"><g:message code="customer.bilAddrZip.label" default="Zip (Billing)" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'bilAddrZip', 'errors')}">
          <g:textField name="bilAddrZip" maxlength="255" value="${customerInstance?.bilAddrZip}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="forwarder"><g:message code="customer.forwarder.label" default="Forwarder" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'forwarder', 'errors')}">
          <g:select name="forwarder.id" from="${com.munix.Forwarder.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.forwarder?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="salesAgent"><g:message code="customer.salesAgent.label" default="Sales Agent" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'salesAgent', 'errors')}">
          <g:select name="salesAgent.id" from="${com.munix.SalesAgent.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.salesAgent?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="declaredValue"><g:message code="customer.declaredValue.label" default="Declared Value" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'declaredValue', 'errors')}">
          <g:textField name="declaredValue" maxlength="17" class="declaredValue" value="${fieldValue(bean: customerInstance, field: 'declaredValue')}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="commissionRate"><g:message code="customer.commissionRate.label" default="Commission Rate" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'commissionRate', 'errors')}">
          <g:textField name="commissionRate" maxlength="17" class="commissionRate" value="${customerInstance?.commissionRate}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="creditLimit"><g:message code="customer.creditLimit.label" default="Credit Limit" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'creditLimit', 'errors')}">
          <g:textField name="creditLimit" maxlength="17" class="creditLimit" value="${customerInstance?.creditLimit}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="enableCreditLimit"><g:message code="customer.enableCreditLimit.label" default="Enable Credit Limit" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'enableCreditLimit', 'errors')}">
          <g:checkBox name="enableCreditLimit" value="${customerInstance?.enableCreditLimit}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="term"><g:message code="customer.term.label" default="Terms" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'terms', 'errors')}">
          <g:select name="term.id" from="${com.munix.Term.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.term?.id}" noSelection="['null': '']" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="fax"><g:message code="customer.bank1.label" default="Bank 1" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'fax', 'errors')}">
              <g:select name="bank1.id" from="${com.munix.Bank.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.bank1?.id}" optionValue="description" noSelection="['null': '']" />
              <g:textField name="branch1" maxlength="255" value="${customerInstance?.branch1}"/>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountName1"><g:message code="customer.accountName1.label" default="Account Name 1" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'accountName1', 'errors')}">
          <g:textField name="accountName1" maxlength="255" value="${customerInstance?.accountName1}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountNumber1"><g:message code="customer.accountNumber1.label" default="Account Number 1" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'accountNumber1', 'errors')}">
          <g:textField name="accountNumber1" maxlength="255" value="${customerInstance?.accountNumber1}" />
          </td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name">
              <label for="fax"><g:message code="customer.bank2.label" default="Bank 2" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'fax', 'errors')}">
              <g:select name="bank2.id" from="${com.munix.Bank.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.bank2?.id}" optionValue="description" noSelection="['null': '']" />
              <g:textField name="branch2" maxlength="255" value="${customerInstance?.branch2}"/>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountName2"><g:message code="customer.accountName2.label" default="Account Name 2" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'accountName2', 'errors')}">
          <g:textField name="accountName2" maxlength="255" value="${customerInstance?.accountName2}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountNumber2"><g:message code="customer.accountNumber2.label" default="Account Number 2" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'accountNumber2', 'errors')}">
          <g:textField name="accountNumber2" maxlength="255" value="${customerInstance?.accountNumber2}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="fax"><g:message code="customer.bank3.label" default="Bank 3" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'fax', 'errors')}">
              <g:select name="bank3.id" from="${com.munix.Bank.list().sort{it.toString()}}" optionKey="id" value="${customerInstance?.bank3?.id}" optionValue="description" noSelection="['null': '']" />
              <g:textField name="branch3" maxlength="255" value="${customerInstance?.branch3}"/>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountName3"><g:message code="customer.accountName3.label" default="Account Name 3" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'accountName3', 'errors')}">
          <g:textField name="accountName3" maxlength="255" value="${customerInstance?.accountName3}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="accountNumber3"><g:message code="customer.accountNumber3.label" default="Account Number 3" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'accountNumber3', 'errors')}">
          <g:textField name="accountNumber3" maxlength="255" value="${customerInstance?.accountNumber3}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="collectionMode"><g:message code="customer.collectionMode.label" default="Collection Mode" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'collectionMode', 'errors')}">
          <g:textField name="collectionMode" maxlength="255" value="${customerInstance?.collectionMode}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="collectionPreference"><g:message code="customer.collectionPreference.label" default="Collection Preference" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'collectionPreference', 'errors')}">
          <g:textField name="collectionPreference" maxlength="255" value="${customerInstance?.collectionPreference}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="collectionSchedule"><g:message code="customer.collectionSchedule.label" default="Collection Schedule" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'collectionSchedule', 'errors')}">
          <g:textField name="collectionSchedule" maxlength="255" value="${customerInstance?.collectionSchedule}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="contact"><g:message code="customer.contact.label" default="Main Contact" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'contact', 'errors')}">
          <g:textField name="contact" maxlength="255" value="${customerInstance?.contact}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="contactPosition"><g:message code="customer.contactPosition.label" default="Position" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'contactPosition', 'errors')}">
          <g:textField name="contactPosition" maxlength="255" value="${customerInstance?.contactPosition}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="secondaryContact"><g:message code="customer.secondaryContact.label" default="Secondary Contact" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'secondaryContact', 'errors')}">
          <g:textField name="secondaryContact" maxlength="255" value="${customerInstance?.secondaryContact}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="secondaryContactPosition"><g:message code="customer.secondaryContactPosition.label" default="Position" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'secondaryContactPosition', 'errors')}">
          <g:textField name="secondaryContactPosition" maxlength="255" value="${customerInstance?.secondaryContactPosition}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="landline"><g:message code="customer.landline.label" default="Landline" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'landline', 'errors')}">
          <g:textField name="landline" maxlength="255" value="${customerInstance?.landline}" />
          </td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name">
              <label for="mobile"><g:message code="customer.mobile.label" default="Mobile" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'mobile', 'errors')}">
          <g:textField name="mobile" maxlength="255" value="${customerInstance?.mobile}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="fax"><g:message code="customer.fax.label" default="Fax" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'fax', 'errors')}">
          <g:textField name="fax" maxlength="255" value="${customerInstance?.fax}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="email"><g:message code="customer.email.label" default="Email" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'email', 'errors')}">
          <g:textField name="email" maxlength="255" value="${customerInstance?.email}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="yahoo"><g:message code="customer.yahoo.label" default="Yahoo" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'yahoo', 'errors')}">
          <g:textField name="yahoo" maxlength="255" value="${customerInstance?.yahoo}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="skype"><g:message code="customer.skype.label" default="Skype" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'skype', 'errors')}">
          <g:textField name="skype" maxlength="255" value="${customerInstance?.skype}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="website"><g:message code="customer.website.label" default="Website" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'website', 'errors')}">
          <g:textField name="website" maxlength="255" value="${customerInstance?.website}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="autoApprove"><g:message code="customer.autoApprove.label" default="Auto Approve" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'autoApprove', 'errors')}">
            <g:checkBox name="autoApprove" value="${customerInstance?.autoApprove}" />
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="autoSecondApprove"><g:message code="customer.autoSecondApprove.label" default="Auto Second Approve" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'autoSecondApprove', 'errors')}">
            <g:checkBox name="autoSecondApprove" value="${customerInstance?.autoSecondApprove}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="creditRemark"><g:message code="customer.creditRemark.label" default="Credit Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'creditRemark', 'errors')}">
          <g:textArea name="creditRemark" value="${customerInstance?.creditRemark}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="generalRemark"><g:message code="customer.generalRemark.label" default="General Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'generalRemark', 'errors')}">
          <g:textArea name="generalRemark" value="${customerInstance?.generalRemark}" />
          </td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name">
              <label for="status"><g:message code="customer.status.label" default="Status" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'status', 'errors')}">
          <g:select name="status" from="${com.munix.Customer.Status}" value="${customerInstance?.status}" />
          </td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name">
              <label for="managerSecondApprove"><g:message code="customer.managerSecondApprove.label" default="2nd Approve only by Manager" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: customerInstance, field: 'managerSecondApprove', 'errors')}">
            <g:checkBox name="managerSecondApprove" value="${customerInstance?.managerSecondApprove}" />
            </td>
          </tr>
          </tbody>
        </table>
        <div class="buttons">
          <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
        </div>
      </div>

    </g:form>
  </div>
</body>
</html>
