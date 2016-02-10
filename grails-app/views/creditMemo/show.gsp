
<%@ page import="com.munix.CreditMemo" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'Credit Memo')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="['Credit Memo']" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="['Credit Memo']" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="['Credit Memo']" /></h1>
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
            <td valign="top" class="name"><g:message code="creditMemo.id.label" default="ID" /></td>
            <td valign="top" class="value">${creditMemoInstance}</td>
            <td valign="top" class="name"><g:message code="creditMemo.date.label" default="Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${creditMemoInstance?.date}" format="MM/dd/yyyy" /></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.customer.label" default="Customer" /></td>
            <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${creditMemoInstance?.customer?.id}">${creditMemoInstance?.customer?.encodeAsHTML()}</g:link></td>
            <td valign="top" class="name"><g:message code="creditMemo.customerType.label" default="Customer Type" /></td>
            <td valign="top" class="value">${creditMemoInstance?.customer?.type?.description}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.discountType.label" default="Discount Type" /></td>
            <td valign="top" class="value">${creditMemoInstance?.discountType?.encodeAsHTML()}</td>
            <td valign="top" class="name"><g:message code="creditMemo.preparedBy.label" default="Prepared By" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "preparedBy")}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.reason.label" default="Reason" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "reason")}</td>
            <td valign="top" class="name"><g:message code="creditMemo.approvedBy.label" default="1st Approval" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "approvedBy")}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.reason.label" default="Warehouse" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "warehouse")}</td>
            <td valign="top" class="name"><g:message code="creditMemo.approveTwoBy.label" default="2nd Approval" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "approvedTwoBy")}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.status.label" default="Status" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "status")}</td>
            <td valign="top" class="name"><g:message code="creditMemo.cancelledBy.label" default="Cancelled By" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "cancelledBy")}</td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.remark.label" default="Remarks" /></td>
            <td valign="top" class="value">${fieldValue(bean: creditMemoInstance, field: "remark")}</td>
            <td valign="top" class="name"></td>
            <td valign="top" class="value"></td>
		  </tr>
          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.additionalDiscount.label" default="Add'l Discount (%)" /></td>
            <td valign="top" class="value">${creditMemoInstance.formatAdditionalDiscount()}</td>
            <td valign="top" class="name"></td>
            <td valign="top" class="value"></td>
		  </tr>
          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.applyFourMonthRule.label" default="Apply Four-month Rule?" /></td>
            <td valign="top" class="value">${creditMemoInstance.applyFourMonthRule}</td>
            <td valign="top" class="name"></td>
            <td valign="top" class="value"></td>
		  </tr>
		  
          <tr class="prop">
            <td valign="top" class="name"><g:message code="creditMemo.directPayment.label" default="Direct Payment" /></td>
            <td class="value">
              <g:each in="${creditMemoInstance.invoices}" var="invoice">
                <g:link controller="directPayment" action="show" id="${invoice?.directPayment?.id}">${invoice?.directPayment}</g:link>
              </g:each>
              <g:link controller="directPayment" action="show" id="${creditMemoInstance?.directPaymentItem?.directPayment?.id}">${creditMemoInstance?.directPaymentItem?.directPayment}</g:link>
            </td>
            <td valign="top" class="name"><g:message code="creditMemo.counterReceipt.label" default="Counter Receipt" /></td>
            <td class="value">
              <g:each in="${creditMemoInstance.counterReceipts}" var="counterReceipt">
                <g:link controller="counterReceipt" action="show" id="${counterReceipt?.id}">${counterReceipt}</g:link>
              </g:each>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="subTable">
      <table >
        <h2>Items</h2>
        <thead>
        <g:if test="${creditMemoInstance.isUnapproved()}">
            <g:link class="addItem" controller="creditMemoItem" action="create" id="${creditMemoInstance?.id}">Add Item</g:link>
        </g:if>
        <tr>
          <th>Date</th>
          <th>Sales Delivery</th>
          <th>Identifier</th>
          <th>Description</th>
          <th>Remarks</th>
          <th class="right">Old Quantity</th>
          <th class="right">Old Price</th>
          <th class="right">New Quantity</th>
          <th class="right">New Price</th>
          <th class="right">SD Discount %</th>
          <th class="right">SD Discount</th>
          <th class="right">Add'l Discount</th>
          <th class="right">Discounted New Price per Unit</th>
          <!---th class="right">Amount</th>
          <th class="right">Discount</th--->
          <th class="right">Final Amount</th>
        </tr>
        </thead>
        <tbody class="editable">

        <g:each in="${creditMemoInstance.items.sort{it.toString()}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" <g:if test="${creditMemoInstance.isUnapproved()}">onclick="window.location='${createLink(uri:'/')}creditMemoItem/edit/${i?.id}'"</g:if>>
            <td><g:formatDate date="${i?.deliveryItem?.delivery?.date}" format="MM/dd/yyyy" /></td>
            <td><g:link controller="salesDelivery" action="show" id="${i?.deliveryItem?.delivery?.id}">${i?.deliveryItem?.delivery}</g:link></td>
            <td><g:link controller="product" action="show" id="${i?.deliveryItem?.product?.id}">${i?.deliveryItem?.product}</g:link></td>
            <td>${i?.deliveryItem?.product?.description}</td>
            <td>${i?.remark}</td>
            <td class="right">${i?.formatOldQty()}</td>
            <td class="right">${i?.formatOldPrice()}</td>

          <g:if test="${i?.newQty != i?.oldQty}">
            <td class="right"><strong>${i?.formatNewQty()}</strong></td>
          </g:if>
          <g:else>
            <td class="right">${i?.formatNewQty()}</td>
          </g:else>

          <g:if test="${i?.newPrice != i?.oldPrice}">
            <td class="right"><strong>${i?.formatNewPrice()}</strong></td>
          </g:if>
          <g:else>
            <td class="right">${i?.formatNewPrice()}</td>
          </g:else>
          <g:set var="disc" value="${i?.newPrice * i?.obtainDiscountRate()/100}"/>
            <td class="right">${i?.obtainDiscountGroup()}</td>
            <td class="right">PHP <g:formatNumber number="${disc}" format="###,##0.00" /></td>
          	<g:if test="${i?.creditMemo?.additionalDiscount}">
	            <td class="right">PHP <g:formatNumber number="${(i?.newPrice - disc)*(i?.creditMemo?.additionalDiscount/100)}" format="###,##0.00" /></td>
	          </g:if>
	          <g:else>
	            <td class="right">PHP 0.00</td>
	          </g:else>
            <td class="right">PHP <g:formatNumber number="${i?.computeDiscountedNewPricePerUnit()}" format="###,##0.00" /></td>
            <!---td class="right">${i?.formatFinalAmount()}</td>
            <td class="right">${i?.formatDiscountAmount()}</td--->
            <td class="right">${i?.formatDiscountedAmount()}</td>
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
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <!---td class="right"><strong>${creditMemoInstance?.formatAmountTotal()}</strong></td>
            <td class="right"><strong>${creditMemoInstance?.formatDiscountTotal()}</strong></td--->
            <td class="right"><strong>${creditMemoInstance?.formatDiscountedAmountTotal()}</strong></td>
          </tr>
          <tr>
            <td><strong>Commission</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <!---td></td>
            <td></td--->
            <td class="right"><strong>${creditMemoInstance?.formatCommissionRate()}</strong></td>
            <td class="right"><strong>${creditMemoInstance?.formatCommissionAmount()}</strong></td>
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
		</tr>
	  </thead>
	  <tbody class="editable">
	  <g:each in="${creditMemoInstance?.printLogs?.sort{it?.date}}" var="i" status="colors">
		<tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
		  <td>${i.user.userRealName}</td>
		  <td><g:formatDate date="${i?.date}" format="MMM. dd, yyyy - hh:mm a" /></td>
		</tr>
	  </g:each>
	  	 <tfoot class="total">
          <tr>
             <td>Print Count</td>
			 <td>${creditMemoInstance.printLogs.size()}</td>
          </tr>
        </tfoot>
	  </tbody>
	</table>
  </div>
      <div class="buttons">
        <g:form>
	    <g:if test="${creditMemoInstance?.status != 'Cancelled'}">
          <g:hiddenField name="id" value="${creditMemoInstance?.id}" />
          <g:if test = "${!creditMemoInstance.invoices}">
             <g:if test = "${showCancelButton}">
                <g:if test = "${!creditMemoInstance?.isApproved() && !creditMemoInstance?.isPaid()}">
                	<span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:if>
             </g:if>
              <g:if test="${creditMemoInstance.isUnapproved()}">
                <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                <g:ifAnyGranted role="ROLE_MANAGER_SALES">
                  <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:ifAnyGranted>
              </g:if>
              <g:if test="${creditMemoInstance.isFirstApproved()}">
                <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
                  <span class="button"><g:actionSubmit class="approve" action="approveTwo" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:ifAnyGranted>
              </g:if>
              <g:if test="${creditMemoInstance.isApproved()}">
                <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
                  <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.approve.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:ifAnyGranted>
              </g:if>
          </g:if>
	    </g:if>
            <span class="button"><g:link class="print" target="blank" controller="print" action="creditMemo" id="${creditMemoInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
        </g:form>
      </div>
  </div>
</body>
</html>
