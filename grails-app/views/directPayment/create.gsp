
<%@ page import="com.munix.DirectPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directPayment.label', default: 'DirectPayment')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript>
    var $ = jQuery.noConflict()
    
    $(document).ready(function () {
    
      $("#customerName").change(function () {
        createDirectPayment($(this).val())
      })
      $("#customerId").change(function () {
        createDirectPayment($(this).val())
      })
     })
     
     var createDirectPayment = function(fieldValue) {      
        window.location="${createLink(uri:'/')}directPayment/create/" + fieldValue          
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
    <g:hasErrors bean="${directPaymentInstance}">
      <div class="errors">
        <g:renderErrors bean="${directPaymentInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
      <div class="dialog">
        <table>
          <tbody>
          <g:set var="customerList" value="${com.munix.Customer.list().sort{it.identifier}}"/>
          <g:if test="${!params.id}">          
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="directPayment.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directPaymentInstance, field: 'customer', 'errors')}" width="650px">
                <g:set var="customerList" value="${com.munix.Customer.list().sort{it.identifier}}" /> 
                <select name="customerId" id="customerId">
                  <option>select one...</option>
                  <g:each in="${customerList}" var="i">
                    <option value="${i?.id}">${i.identifier}</option>
                  </g:each>
                </select>
                <g:set var="customerList" value="${com.munix.Customer.list().sort{it.name}}" />
                <select name="customerName" id="customerName">
                  <option>select one...</option>
                  <g:each in="${customerList}" var="i">
                    <option value="${i?.id}">${i.name}</option>
                  </g:each>
                </select>
              </td>
            </tr>
          </g:if>
          <g:else>
            <g:set var="customer" value="${com.munix.Customer.get(params?.id)}" />
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="directPayment.customer.label" default="Customer" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directPaymentInstance, field: 'customer', 'errors')}" width="650px">
                <g:set var="customersSortedByIdentifier" value="${com.munix.Customer.list().sort{it.identifier}}" />
                <g:select name="customer.id" id="customerId" from="${customersSortedByIdentifier}" optionKey="id" optionValue="identifier" value="${customer.id}"/>
                <g:set var="customersSortedByName" value="${com.munix.Customer.list().sort{it.name}}" />
                <g:select name="customerName" id="customerName" from="${customersSortedByName}" optionKey="id" optionValue="name" value="${customer.id}" />                
              </td>
              
            <tr class="prop">
              <td valign="top" class="name">
                <label for="remark"><g:message code="directPayment.customer.label" default="Remarks" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: directPaymentInstance, field: 'remark', 'errors')}">
          		<g:textArea name="remark" maxlength="255" value="${directPaymentInstance?.remark}" />
          	  </td>
            </tr>
              
            <tr class="prop">
              <td valign="top" class="name">
                <label for="deliveries"><g:message code="directPayment.deliveries.label" default="Deliveries" /></label>
              </td>
              <td valign="top" class="value" ${hasErrors(bean: directPaymentInstance, field: 'deliveries', 'errors')}">
			 	<div class="subTable">
			  		<table>
					  <thead>
						<tr>
						  <th style="width:30px;"></th>
						  <th style="width:400px;">Sales Deliveries</th>
						  <th class="center" style="width:100px;">Date</th>
						  <th>Total Amount</th>
						  <th>Amount Due</th>
						</tr>
					  </thead>
					  <tbody>
					  	<g:each in="${deliveries}" var="salesDelivery">
					  	  <tr>
							<td><g:checkBox name="deliveries" value="${salesDelivery.id}" checked="false"/></td>
						    <td>${salesDelivery.toString()}</td>
							<td class="center"><g:formatDate date="${salesDelivery.date}" format="MMM. dd, yyyy"/></td>
							<td>PHP <g:formatNumber number="${salesDelivery.computeTotalAmount()}" format="###,##0.00" /></td>
							<td>PHP <g:formatNumber number="${salesDelivery.computeAmountDue()}" format="###,##0.00" /></td>
						  </tr>
						</g:each>
					  </tbody>
				</table>
				</div>
          	</td>
          </tr>
		 
          <tr class="prop">
            <td valign="top" class="name">
              <label for="charge"><g:message code="directPayment.charge.label" default="Charge" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: directPaymentInstance, field: 'charge', 'errors')}">
				<div class="subTable">
				<table>
					<thead>
					  <tr>
						<th style="width:30px;"></th>
						<th style="width:400px;">Charge</th>
						<th class="center" style="width:100px;">Date</th>
						<th>Total Amount</th>
						<th>Amount Due</th>
					  </tr>
					</thead>
					<tbody>
						<g:each in="${charges}" var="customerCharge">
						<tr>
						  <td><g:checkBox name="charges" value="${customerCharge.id}" checked="false"/></td>
						  <td>${customerCharge.toString()}</td>
						  <td class="center"><g:formatDate date="${customerCharge.date}" format="MMM. dd, yyyy"/></td>
						  <td>PHP <g:formatNumber number="${customerCharge.computeTotalAmount()}" format="###,##0.00" /></td>
						  <td>PHP <g:formatNumber number="${customerCharge.computeProjectedDue()}" format="###,##0.00" /></td>
						</tr>
					  </g:each>
					</tbody>
	  			</table>
	  		</div>
          </td>
          </tr>
		  
		  <tr class="prop">
		    <td valign="top" class="name">
			  <label for="bouncedCheck">Bounced Check</label>
		    </td>
		    <td valign="top" class="value ${hasErrors(bean: directPaymentInstance, field: 'bouncedCheck', 'errors')}">
				<div class="subTable">
				<table>
					<thead>
					  <tr>
						<th style="width:30px;"></th>
						<th style="width:400px;">Bounced Check</th>
						<th class="center" style="width:100px;">Date</th>
						<th>Total Amount</th>
					  </tr>
					</thead>
					<tbody>
						<g:each in="${bouncedChecks}" var="bouncedCheck">
						  <tr>
						  <td>
							  <g:checkBox name="bouncedCheck" value="${bouncedCheck.id}" checked="false"/>
						  </td>
						  <td>
								${bouncedCheck.toString()}
						  </td>
						  <td class="center">
							  <g:formatDate date="${bouncedCheck.date}" format="MMM. dd,yyyy"/>
						  </td>
						  <td>
							  ${bouncedCheck.formatTotal()}
						  </td>
						</tr>
					  </g:each>
					</tbody>
				  </table>
			  </div>
		    </td>
		  </tr>
		  
		  <tr class="prop">
		    <td valign="top" class="name">
			  <label for="creditMemo">Credit Memo</label>
		    </td>
		    <td valign="top" class="value ${hasErrors(bean: directPaymentInstance, field: 'creditMemo', 'errors')}">
				<div class="subTable">
				<table>
					<thead>
					  <tr>
						<th style="width:30px;"></th>
						<th style="width:400px;">Credit Memo</th>
						<th class="center" style="width:100px;">Date</th>
						<th>Total Amount</th>
					  </tr>
					</thead>
					<tbody>
						<g:each in="${creditMemos}" var="creditMemo">
						  <tr>
						  <td>
							  <g:checkBox name="creditMemo" value="${creditMemo.id}" checked="false"/>
						  </td>
						  <td>
								${creditMemo.toString()}
						  </td>
						  <td class="center">
							  <g:formatDate date="${creditMemo.date}" format="MMM. dd, yyyy"/>
						  </td>
						  <td>
							  ${creditMemo.formatDiscountedAmountTotal()}
						  </td>
						</tr>
					  </g:each>
					</tbody>
				  </table>
			  </div>
		    </td>
		  </tr>
        </g:else>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
