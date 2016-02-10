
<%@ page import="com.munix.DirectPayment" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'directPayment.label', default: 'DirectPayment')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
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
    <g:hasErrors bean="${directPaymentInstance}">
      <div class="errors">
        <g:renderErrors bean="${directPaymentInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${directPaymentInstance?.id}" />
      <g:hiddenField name="version" value="${directPaymentInstance?.version}" />
      <div class="dialog">
        <tr class="prop">
        <table>
          <tbody>
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
						  <th style="width:300px;">Sales Deliveries</th>
						  <th class="center" style="width:100px;">Date</th>
						  <th>Discounted Discount</th>
						  <th>Net Discount</th>
						  <th>Total Amount</th>
						  <th>Amount Due</th>
						</tr>
					  </thead>
					  <tbody>
					  	<g:each in="${deliveries}" var="delivery">
					  	  <tr>
					  	  	<g:set var="checked" value="false"/>
					  	  	<g:if test="${delivery?.hasInvoiceRelatedTo(directPaymentInstance)}">
					  	  		<g:set var="checked" value="true"/>
					  	  	</g:if>
							<td><g:checkBox name="deliveries" value="${delivery.id}" checked="${checked}"/></td>
						    <td>${delivery.toString()}</td>
							<td><g:formatDate date="${delivery.date}" format="MM/dd/yyyy"/></td>
			                <td>${delivery?.invoice?.discountGroup}</td>
            			    <td>${delivery?.invoice?.netDiscountGroup}</td>
							<td>PHP <g:formatNumber number="${delivery?.computeTotalAmount()}" format="###,##0.00" /></td>
							<td>PHP <g:formatNumber number="${delivery.computeAmountDue()}" format="###,##0.00" /></td>
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
						<g:each in="${charges}" var="charge">
						  <tr>
						  <g:set var="checked" value="false"/>
					  	  	<g:if test="${charge?.hasInvoiceRelatedTo(directPaymentInstance)}">
					  	  		<g:set var="checked" value="true"/>
					  	  	</g:if>
						  <td>
							  <g:checkBox name="charges" value="${charge.id}" checked="${checked}"/>
						  </td>
						  <td>
								${charge.toString()}
						  </td>
						  <td>
							  <g:formatDate date="${charge.date}" format="MM/dd/yyyy"/>
						  </td>
						  <td>
						  		PHP <g:formatNumber number="${charge?.computeTotalAmount()}" format="###,##0.00" />
					  	  </td>
						  <td>
						  		PHP <g:formatNumber number="${charge?.computeProjectedDue()}" format="###,##0.00" />
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
						  <g:set var="checked" value="false"/>
					  	  	<g:if test="${bouncedCheck?.hasInvoiceRelatedTo(directPaymentInstance)}">
					  	  		<g:set var="checked" value="true"/>
					  	  	</g:if>
						  <td>
							  <g:checkBox name="bouncedCheck" value="${bouncedCheck.id}" checked="${checked}"/>
						  </td>
						  <td>
								${bouncedCheck.toString()}
						  </td>
						  <td>
							  <g:formatDate date="${bouncedCheck.date}" format="MM/dd/yyyy"/>
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
						  <g:set var="checked" value="false"/>
					  	  	<g:if test="${creditMemo?.hasInvoiceRelatedTo(directPaymentInstance)}">
					  	  		<g:set var="checked" value="true"/>
					  	  	</g:if>
							<g:if test="${creditMemo.directPaymentItem != null}">
								<g:set var="checked" value="true"/>
					  	  	</g:if>
						  <td>
							  <g:checkBox name="creditMemo" value="${creditMemo.id}" checked="${checked}"/>
						  </td>
						  <td>
								${creditMemo.toString()}
						  </td>
						  <td>
							  <g:formatDate date="${creditMemo.date}" format="MM/dd/yyyy"/>
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
		  </tbody>
		  </table>
    </div>
    <div class="buttons">
      <span class="button"><g:actionSubmit class="save" action="updateDirectPayment" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
