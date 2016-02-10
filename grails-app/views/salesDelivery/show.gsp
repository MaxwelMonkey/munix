<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<g:if test="${params.a=='print'}">
<script>alert("Your request has been sent for approval.");</script>
</g:if>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
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
            <td valign="top" class="name"><g:message code="salesDelivery.id.label" default="Id" /></td>
        <td valign="top" class="value">${salesDeliveryInstance}</td>
        <td valign="top" class="name"><g:message code="salesDelivery.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${salesDeliveryInstance?.date}" format="MMM. dd, yyyy"/></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesDelivery.invoice.label" default="Order" /></td>
        <td valign="top" class="value"><g:link elementId="salesOrderShowLink" action="show" controller="salesOrder" id="${salesDeliveryInstance?.invoice?.id}">${salesDeliveryInstance?.invoice}</g:link></td>
        <td valign="top" class="name"><g:message code="salesDelivery.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${salesDeliveryInstance?.preparedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
	      <td valign="top" class="name">Sales Invoice #</td>
	      <td valign="top" class="value">${salesDeliveryInstance?.salesDeliveryNumber}</td>
        <td valign="top" class="name"><g:message code="salesDelivery.approvedBy.label" default="Approved By" /></td>
        <td valign="top" class="value">${salesDeliveryInstance?.approvedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesDelivery.warehouse.label" default="Warehouse" /></td>
	        <td valign="top" class="value">${salesDeliveryInstance?.warehouse?.description?.encodeAsHTML()}</td>
	          
	        <td valign="top" class="name"><g:message code="salesDelivery.autoApprovedBy.label" default="Auto-approved By" /></td>
            <td valign="top" class="value">${salesDeliveryInstance?.autoApprovedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesDelivery.deliveryType.label" default="Delivery Type" /></td>
          <td valign="top" class="value">${salesDeliveryInstance?.deliveryType?.encodeAsHTML()}</td>
	        <td valign="top" class="name"><g:message code="salesDelivery.cancelledBy.label" default="Cancelled By" /></td>
            <td valign="top" class="value">${salesDeliveryInstance?.cancelledBy?.encodeAsHTML()}</td>
        </tr>
        
          <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesDelivery.remark.label" default="Remarks" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesDeliveryInstance, field: "remark")}</td>
	      <td valign="top" class="name">Delivery Receipt #</td>
	      <td valign="top" class="value">${salesDeliveryInstance?.deliveryReceiptNumber}</td>
          </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesDelivery.customer.label" default="Customer" /></td>
        <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${salesDeliveryInstance?.customer?.id}">${salesDeliveryInstance?.customer}</g:link></td>
	        <td valign="top" class="name"><g:message code="salesDelivery.discountType.label" default="Discount Type" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesDeliveryInstance.invoice, field: "discountType")}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
        <td valign="top" class="name"><g:message code="salesDelivery.salesAgent.label" default="Sales Agent" /></td>
        <td valign="top" class="value"><g:link controller="salesAgent" action="show" id="${salesDeliveryInstance?.salesAgent?.id}">${salesDeliveryInstance?.salesAgent?.firstName} ${salesDeliveryInstance?.salesAgent?.lastName}</g:link></td>
        </tr>
        <g:ifAnyGranted role="ROLE_ACCOUNTING">
          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesDelivery.termDay.label" default="Term Days" /></td>
          <td valign="top" class="value">${salesDeliveryInstance?.termDay?.encodeAsHTML()}</td>
          <td class="name">Counter Receipt</td>
          <td class="value">
              <g:each in="${salesDeliveryInstance.counterReceipts?.findAll{it?.isApproved()}.sort{it.id}}" var="counterReceipt">
                <g:link controller="counterReceipt" action="show" id="${counterReceipt?.id}">${counterReceipt}</g:link>
              </g:each>
          </td>
          </tr>
        </g:ifAnyGranted>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          

        <td valign="top" class="name"><g:message code="salesDelivery.tripTicket.label" default="Trip Ticket" /></td>
        <g:if test="${salesDeliveryInstance?.directDelivery != null}">
          <td valign="top" class="value"><g:link action="show" controller="tripTicket" id="${salesDeliveryInstance?.directDelivery?.tripTicket?.tripTicket?.id}">${salesDeliveryInstance?.directDelivery?.tripTicket} <g:formatDate date="${salesDeliveryInstance?.directDelivery?.tripTicket?.tripTicket?.date}" format="MM/dd/yyyy" /></g:link></td>
        </g:if>
        <g:elseif test="${salesDeliveryInstance?.waybill != null}">
          <td valign="top" class="value"><g:link action="show" controller="tripTicket" id="${salesDeliveryInstance?.waybill?.tripTicket?.tripTicket?.id}">${salesDeliveryInstance?.waybill?.tripTicket} <g:formatDate date="${salesDeliveryInstance?.waybill?.tripTicket?.tripTicket?.date}" format="MM/dd/yyyy" /></g:link></td>
        </g:elseif>
        <g:else>
          <td class="value"></td>
        </g:else>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">Waybill</td>
      	  <td valign="top" class="value"><g:link action="show" controller="waybill" id="${salesDeliveryInstance?.waybill?.id}">${salesDeliveryInstance?.waybill}  <g:formatDate date="${salesDeliveryInstance?.waybill?.date}" format="MM/dd/yyyy" /></g:link></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
        <tr class="prop">
        <td valign="top" class="name"><g:message code="salesDelivery.delivery.label" default="Delivery" /></td>

        <g:if test="${salesDeliveryInstance?.directDelivery != null}">
          <td valign="top" class="value"><g:link action="show" controller="directDelivery" id="${salesDeliveryInstance?.directDelivery?.id}">${salesDeliveryInstance?.directDelivery}</g:link></td>
        </g:if>
        <g:else>
          <td></td>
        </g:else>
          <td valign="top" class="name"><g:message code="salesDelivery.status.label" default="Status" /></td>
        <td valign="top" class="value">${salesDeliveryInstance?.status}</td>
        </tr>
        <tr class="prop">
          <td class="name"></td>
      	  <td class="value"></td>
          <td class="name">Balance</td>
          <td class="value">PHP <g:formatNumber number="${salesDeliveryInstance?.computeAmountDue()}" format="###,##0.00" /></td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Sales Delivery Items</h2>
        <h2>Discounted Items</h2>
        <thead>
          <tr>
            <th>Identifier</th>
            <th>Description</th>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="right">Price</th>
            </g:ifAnyGranted>
            <th class="right">Quantity</th>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="right">Amount</th>
            </g:ifAnyGranted>
        </tr>
        </thead>
        <tbody class="uneditable">

        <g:each in="${salesDeliveryInstance.items.sort{it?.product?.description}}" var="i" status="colors">
       	  <g:if test="${!i?.orderItem?.isNet && i?.qty?.intValue() > 0}">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
            <td>${i?.product?.encodeAsHTML()}</td>
            <td>${i?.product?.description}</td>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <td class="right"><g:if test="${i?.price==0}">(FREE)</g:if><g:else>${i?.formatPrice()}</g:else></td>
            </g:ifAnyGranted>
            <td class="right">${i?.qty}</td>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <td class="right">${i?.formatAmount()}</td>
            </g:ifAnyGranted>
          </tr>
          </g:if>
        </g:each>
        </tbody>
		<g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance?.computeDiscountedItemsTotal()}" format="###,##0.00" /></strong></td>
        </tr>
        <tr>
          <td><strong>Discount</strong></td>
          <td></td>
          <td></td>
          <td class="right"><strong>${salesDeliveryInstance?.invoice?.discountGroup}</strong></td>
          <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance?.computeDiscountedDiscount()}" format="###,##0.00" /></strong></td>
        </tr>
        <tr>
          <td><strong>Discounted Total</strong></td>
          <td></td>
          <td></td>
          <td></td>
          <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance.computeDiscountedTotal()}" format="###,##0.00" /></strong></td>
        </tr>
        </tfoot>
        </g:ifAnyGranted>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Net Price Items</h2>
        <thead>
          <tr>
            <th>Identifier</th>
            <th>Description</th>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="right">Price</th>
            </g:ifAnyGranted>
            <th class="right">Quantity</th>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="right">Amount</th>
            </g:ifAnyGranted>
        </tr>
        </thead>
        <tbody class="uneditable">

        <g:each in="${salesDeliveryInstance.items.sort{it?.product?.description}}" var="i" status="colors">
       	  <g:if test="${i?.orderItem?.isNet && i?.qty?.intValue() > 0}" >
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
            <td>${i?.product?.encodeAsHTML()}</td>
            <td>${i?.product?.description}</td>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <td class="right"><g:if test="${i?.price==0}">(FREE)</g:if><g:else>${i?.formatPrice()}</g:else></td>
            </g:ifAnyGranted>
            <td class="right">${i?.qty}</td>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <td class="right">${i?.formatAmount()}</td>
            </g:ifAnyGranted>
          </tr>
          </g:if>
        </g:each>
        </tbody>
		<g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance?.computeNetItemsTotal()}" format="###,##0.00" /></strong></td>
        </tr>
        <tr>
          <td><strong>Discount</strong></td>
          <td></td>
          <td></td>
          <td class="right"><strong>${salesDeliveryInstance?.invoice?.netDiscountGroup}</strong></td>
          <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance?.computeNetDiscount()}" format="###,##0.00" /></strong></td>
        </tr>
        <tr>
          <td><strong>Discounted Total</strong></td>
          <td></td>
          <td></td>
          <td></td>
          <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance.computeNetTotal()}" format="###,##0.00" /></strong></td>
        </tfoot>
        </g:ifAnyGranted>
      </table>
    </div>
	<g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
    <div class="subTable">
      <table>
        <h2>Total</h2>
        <tbody class="uneditable">
        </tbody>
        <tfoot class="total">
        	<tr>
	          <td><strong>Discounted Items Total</strong></td>
	          <td></td>
	          <td></td>
	          <td></td>
	          <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance.computeDiscountedTotal()}" format="###,##0.00" /></strong></td>
	        </tr>
        	<tr>
	          <td><strong>Net Items Total</strong></td>
	          <td></td>
	          <td></td>
	          <td></td>
	          <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance.computeNetTotal()}" format="###,##0.00" /></strong></td>
	        </tr>
	        <tr>
	          <td><strong>Grand Total</strong></td>
	          <td></td>
	          <td></td>
	          <td></td>
	          <td class="right">
                  <strong>
                      <g:if test="${salesDeliveryInstance.isCancelled()}">PHP 0.00 </g:if>
                      <g:else>PHP <g:formatNumber number="${salesDeliveryInstance.computeTotalAmount()}" format="###,##0.00" /></g:else>
                  </strong>
              </td>
	        </tr>
	        <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
	          <tr>
	            <td><strong>Commission</strong></td>
	            <td></td>
	            <td></td>
	            <td class="right"><strong><g:formatNumber number="${salesDeliveryInstance.commissionRate}" format="###,##0.00" />%</strong></td>
	            <td class="right"><strong>PHP <g:formatNumber number="${salesDeliveryInstance.computeCommissionAmount()}" format="###,##0.00" /></strong></td>
	          </tr>
	        </g:ifAnyGranted>
        </tfoot>
      </table>
    </div>
    </g:ifAnyGranted>
    <div class="subTable">
      <table>
        <h2>Credit Memo Items</h2>
        <thead>
          <tr>
          	<th>Credit Memo #</th>
            <th>Date</th>
            <th>Product Identifier</th>
            <th>Description</th>
            <th>New Qty</th>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th>New Price</th>
            </g:ifAnyGranted>
            <th>Status</th>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="right">Total</th>
            </g:ifAnyGranted>
        </tr>
        </thead>
        <tbody class="editable">
          <g:each in="${creditMemoItems}" var="i" status="colors">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}creditMemo/show/${i?.creditMemo?.id}'">
            	<td>${i?.creditMemo.toString()}</td>
                <td><g:formatDate date="${i?.date}" format="MMM. dd, yyyy - hh:mm a" /></td>
                <td>${i?.deliveryItem.product}</td>
                <td>${i?.deliveryItem.product.formatDescription()}</td>
                <g:if test="${i?.hasQtyChanged()}">
                	<td><b>${i?.newQty}</b></td>
                </g:if>
                <g:else>
                	<td>${i?.newQty}</td>
                </g:else>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
                <g:if test="${i?.hasPriceChanged()}">
                	<td><b>${i?.newPrice}</b></td>
                </g:if>
                <g:else>
                	<td>${i?.newPrice}</td>
                </g:else>
            </g:ifAnyGranted>
                <td>${i?.creditMemo.status}</td>
	        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
                <td class="right">${i?.formatDiscountedAmount()}</td>
            </g:ifAnyGranted>
            </tr>
          </g:each>
        </tbody>
      </table>
    </div>
	<g:ifAnyGranted role="ROLE_ACCOUNTING">
    <div class="subTable">
      <table>
        <h2>Payments</h2>
        <thead>
          <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Status</th>
            <th class="right">Total</th>
        </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${salesDeliveryInstance.invoices}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}directPayment/show/${i?.directPayment?.id}'">
            <td>${i?.directPayment}</td>
            <td><g:formatDate date="${i?.directPayment?.date}" format="MM/dd/yyyy" /></td>
            <td>${i?.directPayment?.status}</td>
            <td class="right">${i?.formatAmount()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Counter Receipts</h2>
        <thead>
          <tr>
            <th>Reference #</th>
            <th>Date</th>
            <th>Amount</th>
            <th class="right">Amount Due</th>
        </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${salesDeliveryInstance.counterReceipts.sort{it.id}}" var="counterReceipt" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}counterReceipt/show/${counterReceipt?.id}'">
            <td>${counterReceipt}</td>
            <td><g:formatDate date="${counterReceipt?.date}" format="MM/dd/yyyy" /></td>
            <td>PHP <g:formatNumber number="${counterReceipt?.computeTotal()}" format="###,##0.00" /></td>
            <td class="right">PHP <g:formatNumber number="${counterReceipt?.computeAmountDueTotal()}" format="###,##0.00" /></td>
          </tr>
        </g:each>
        <tfoot class="total">
          <tr>
             <td>Total</td>
			 <td></td>
			 <td>PHP <g:formatNumber number="${salesDeliveryInstance.computeCounterReceiptsAmountTotal()}" format="###,##0.00" /></td>
			 <td class="right">PHP <g:formatNumber number="${salesDeliveryInstance.computeCounterReceiptsAmountDueTotal()}" format="###,##0.00" /></td>
          </tr>
        </tfoot>
        </tbody>
      </table>
    </div>
    </g:ifAnyGranted>
	<div class="subTable">
	<table>
	  <h2>Print History</h2>
	  <thead>
		<tr>
		  <th>Printed By</th>
		  <th>Type</th>
		  <th>Date</th>
		  <th>Approved By</th>
		  <th>Approved Date</th>
		</tr>
	  </thead>
	  <tbody class="editable">
	  <g:each in="${salesDeliveryInstance?.printLogs?.sort{it?.date}}" var="i" status="colors">
		<tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
		  <td>${i.user.userRealName}</td>
		  <td>${i.type}</td>
		  <td><g:formatDate date="${i?.date}" format="MMM. dd, yyyy - hh:mm a" /></td>
		  <td>${i.approvedBy?.userRealName}</td>
		  <td><g:formatDate date="${i?.approvedDate}" format="MMM. dd, yyyy - hh:mm a" /></td>
		</tr>
	  </g:each>
	  	 <tfoot class="total">
          <tr>
             <td>Print Count</td>
			 <td>${salesDeliveryInstance.printLogs.size()}</td>
             <td></td>
             <td></td>
             <td></td>
          </tr>
        </tfoot>
	  </tbody>
	</table>
  </div>


    <g:if test="${!salesDeliveryInstance?.isCancelled()}">
      <div class="buttons">
        <g:form>
          <g:hiddenField name="id" value="${salesDeliveryInstance?.id}" />
          <g:if test="${salesDeliveryInstance?.isUnapproved()}">
            <g:ifAnyGranted role="ROLE_SALES">
              <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
            </g:ifAnyGranted>
            <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
            <g:ifAnyGranted role="ROLE_MANAGER_DELIVERY,ROLE_MANAGER_SALES">
              <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
            </g:ifAnyGranted>
          </g:if>
		  <g:if test="${showUnapprove}">
          	<g:if test="${salesDeliveryInstance?.isApproved()}">
              <g:ifAnyGranted role="ROLE_MANAGER_DELIVERY,ROLE_MANAGER_SALES">
                <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.unapprove.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
              </g:ifAnyGranted>
            </g:if>
          </g:if>
          <g:ifAnyGranted role="ROLE_MANAGER_SALES,ROLE_SALES">
	          <g:ifAnyGranted role="ROLE_MANAGER_SALES">
	            <span class="button"><g:link class="print" target="_printFrame" action="print" id="${salesDeliveryInstance?.id}">Packing List</g:link></span>
    	        <span class="button"><g:link class="print" target="_printFrame" action="print" params="[id:salesDeliveryInstance?.id, 'noPl':'Y']">Invoice</g:link></span>
    	      </g:ifAnyGranted>
	          <g:ifNotGranted role="ROLE_MANAGER_SALES">
	          	<g:if test="${printablePacking || approvedPacking}">
		            <span class="button"><g:link class="print" target="_printFrame" action="print" id="${salesDeliveryInstance?.id}">Packing List</g:link></span>
		        </g:if>
	            <g:else>
	            	<g:if test="${unapprovedPacking}">
			            <span class="button"><span class="print"><strong>Print Requested: Packing List</strong></span></span>
		            </g:if>
		            <g:else>
			            <span class="button"><g:link class="print" action="print" id="${salesDeliveryInstance?.id}">Request Print: Packing List</g:link></span>
		            </g:else>
		        </g:else>
	          	<g:if test="${printableInvoice || approvedInvoice}">
		            <span class="button"><g:link class="print" target="_printFrame" action="print" params="[id:salesDeliveryInstance?.id, 'noPl':'Y']">Invoice</g:link></span>
	            </g:if>
	            <g:else>
	            	<g:if test="${unapprovedInvoice}">
			            <span class="button"><span class="print"><strong>Print Requested: Invoice</strong></span></span>
		            </g:if>
		            <g:else>
			            <span class="button"><g:link class="print" action="print" params="[id:salesDeliveryInstance?.id, 'noPl':'Y']">Request Print: Invoice</g:link></span>
		            </g:else>
	            </g:else>
    	      </g:ifNotGranted>
          </g:ifAnyGranted>
          <g:ifAnyGranted role="ROLE_SUPER">
          <span class="button"><g:actionSubmit class="edit" action="viewPriceMargin" value = "View Margin"/></span>
          </g:ifAnyGranted>
        </g:form>
      </div>
    </g:if>
    <iframe name="_printFrame" style="width:0px;height:0px"/>
  </div>
</body>
</html>
