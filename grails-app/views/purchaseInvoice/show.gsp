
<%@ page import="com.munix.PurchaseInvoice" %>
<%@ page import="java.lang.String" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
	 <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <style type="text/css">
        .hidden {
            display: none;
        }
        .filter {
            display: none;
        }

        html>body thead.fixedHeader th {
	        width: 350px
        }

        html>body thead.fixedHeader th + th {
	        width: 350px
        }

        html>body thead.fixedHeader th + th + th {
	        width: 100px
        }
        html>body thead.fixedHeader th + th + th +th{
	        width: 116px
        }

        html>body tbody.scrollContent td {
	        width: 350px
        }

        html>body tbody.scrollContent td + td {
	        width: 350px
        }

        html>body tbody.scrollContent td + td + td {
	        width: 100px
        }
        html>body tbody.scrollContent td + td + td + td{
	        width: 100px
        }

    </style>
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
    <g:if test="${purchaseInvoiceInstance?.exchangeRate <= 0}">
      <div class="message">Invoice cannot be approved while exchange rate is not entered!</div>
    </g:if>
    <g:if test="${exceedMessage}">
      <div class="errors">${exceedMessage}</div>
    </g:if>
    <g:hasErrors bean="${purchaseInvoiceInstance}">
      <div class="errors">
        <g:renderErrors bean="${purchaseInvoiceInstance}" as="list" />
      </div>
    </g:hasErrors>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="purchaseInvoice.reference.label" default="Reference" /></td>
            <td valign="top" class="value">${fieldValue(bean: purchaseInvoiceInstance, field: "reference")}</td>
        	<td valign="top" class="name"><g:message code="purchaseInvoiceInstance.preparedBy.label" default="Prepared By" /></td>
        	<td valign="top" class="value">${purchaseInvoiceInstance?.preparedBy?.encodeAsHTML()}</td>
          </tr>


        <tr class="prop">
          <td valign="top" class="name">Supplier Reference</td>
            <td valign="top" class="value">${purchaseInvoiceInstance?.supplierReference}</td>
          <td valign="top" class="name"><g:message code="purchaseInvoiceInstance.approvedBy.label" default="Approved By" /></td>
          <td valign="top" class="value">${purchaseInvoiceInstance?.approvedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="purchaseInvoice.date.label" default="Date Created" /></td>
          <td valign="top" class="value"><g:formatDate date="${purchaseInvoiceInstance?.date}" format="MM/dd/yyyy" /></td>
          <td valign="top" class="name"><g:message code="purchaseInvoiceInstance.cancelledBy.label" default="Cancelled By" /></td>
          <td valign="top" class="value">${purchaseInvoiceInstance?.cancelledBy?.encodeAsHTML()}</td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name"><g:message code="purchaseInvoice.invoiceDate.label" default="Invoice Date" /></td>
          <td valign="top" class="value"><g:formatDate date="${purchaseInvoiceInstance?.invoiceDate}" format="MM/dd/yyyy" /></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
		<tr class="prop">
          <td valign="top" class="name"><g:message code="purchaseInvoiceInstance.deliveryDate.label" default="Delivery Date" /></td>
          <td valign="top" class="value"><g:formatDate date="${purchaseInvoiceInstance?.deliveryDate}" format="MM/dd/yyyy" /></td>
          <td valign="top" class="name"><g:message code="purchaseInvoiceInstance.supplierPayment.label" default="Supplier Payment" /></td>
          <td valign="top" class="value"><g:link controller="supplierPayment" action="show" id="${supplierPayment?.id}">${supplierPayment}</g:link></td>
        </tr>
        <tr class="prop">
            <td valign="top" class="name"><g:message code="purchaseInvoice.supplier.label" default="Supplier" /></td>
            <td valign="top" class="value"><g:link elementId="supplierShowLink" controller="supplier" action="show" id="${purchaseInvoiceInstance?.supplier?.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "supplier")}</g:link></td>
            <td valign="top" class="name"><g:message code="purchaseInvoice.warehouse.label" default="Warehouse" /></td>
            <td valign="top" class="value">${fieldValue(bean: purchaseInvoiceInstance, field: "warehouse")}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="purchaseInvoice.exchangeRate.label" default="Exchange Rate" /></td>
          <td valign="top" class="value">${purchaseInvoiceInstance?.formatExchangeRate()}</td>
          <td valign="top" class="name"><g:message code="purchaseInvoice.type.label" default="Type" /></td>
          <td valign="top" class="value">${fieldValue(bean: purchaseInvoiceInstance, field: "type")}</td>
        </tr>
  
        <tr class="prop">
          <td valign="top" class="name"><g:message code="purchaseInvoice.status.label" default="Remarks" /></td>
          <td valign="top" class="value">${fieldValue(bean: purchaseInvoiceInstance, field: "remark")}</td>
          <td valign="top" class="name"></td>
          <td valign="top" class="value"></td>
        </tr>

        </tbody>
      </table>
    </div>

      <g:form>
            <div id="scroll" >
                <div class="list">
                        <table class="invoiceItems">
                        <h2>Invoice Items</h2>
                            <thead >
                                <tr>
                                	<th>Purchase Order#</th>
                                    <th>Product</th>
                                    <th>Quantity</th>
                                    <th>Received</th>
                                    <th>PO Final Price</th>
                                    <th>Final Price</th>
                                    <th>Amount (Foreign Currency)</th>
                                    <th>Amount (PHP)</th>                            
                                </tr>
                            </thead>
                            <tbody class="uneditable">
                                <g:each in="${purchaseInvoiceInstance?.items}" var="item" status="i">  
                                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                                    	<td><g:link controller="purchaseOrder" id="${item?.purchaseOrderItem?.po?.id}" action="show">${item?.purchaseOrderItem?.po.formatId()}</g:link></td>
                                        <td><g:link controller="product" id="${item?.purchaseOrderItem?.product?.id}" action="show">${item?.purchaseOrderItem?.product?.identifier}-${item?.purchaseOrderItem?.product?.description}</g:link></td>
                                        <td class="right">${item.purchaseOrderItem?.qty.intValue()}</td>
                                        <td class="right">
                                            ${item.qty?.intValue()}
                                        </td>
                                        <td class="right">${String.format("%,.4f",item?.purchaseOrderItem?.finalPrice.setScale(4,BigDecimal.ROUND_HALF_UP))}</td>
                                        <td class="right">
                                            <g:if test="${item?.finalPrice?.compareTo(item?.purchaseOrderItem?.finalPrice) != 0}" >
                                                <strong>${String.format("%,.4f",item.finalPrice?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                            </g:if>
                                            <g:else>
                                                ${String.format("%,.4f",item.finalPrice?.setScale(4,BigDecimal.ROUND_HALF_UP))}
                                            </g:else>
                                        </td>
                                        <td class="right">
                                            ${String.format("%,.4f",item?.finalPrice?.multiply(item.qty?.intValue())?.setScale(4,BigDecimal.ROUND_HALF_UP))}
                                        </td>
                                        <td class="right">
                                            ${String.format("%,.4f",item?.finalPrice?.multiply(item.qty?.intValue())?.multiply((purchaseInvoiceInstance?.exchangeRate?:0))?.setScale(4,BigDecimal.ROUND_HALF_UP))}
                                        </td>
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
                                    <td class="right">
                                        <strong>${String.format("%,.4f",purchaseInvoiceInstance.computeFinalPriceTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                    </td>
                                    <td class="right">
                                        <strong>${String.format("%,.4f",purchaseInvoiceInstance.computeForeignAmountTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                    </td>
                                    <td class="right">
                                        <strong>${String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoicePhpTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>Discount</strong></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td class="right">
                                        <strong>${purchaseInvoiceInstance?.discountRate}%</strong>
                                    </td>
                                    <td class="right">
                                        <strong>${String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountForeignAmount()?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                    </td>
                                    <td class="right">
                                        <strong>${String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountPhpAmount()?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>Grand Total</strong></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td class="right">
                                        <strong>${String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountedForeignTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                    </td>
                                    <td class="right">
                                        <strong>${String.format("%,.4f",purchaseInvoiceInstance.computePurchaseInvoiceDiscountedPhpTotal()?.setScale(4,BigDecimal.ROUND_HALF_UP))}</strong>
                                    </td>

                                </tr>
                            </tfoot>
                        </table>
                </div>
            </div>

    <!---div class="subTable">
      <table>
        <h2>
            <g:if test="${supplierPayment}">
                <g:link controller="supplierPayment" action="show" id="${supplierPayment?.id}">Supplier Payment</g:link>
            </g:if>
            <g:else>
                Supplier Payment
            </g:else>
        </h2>

        <thead>
          <tr>
          	<th>Supplier Payment #</th>
          	<th class="center">Date</th>
            <th>Payment Type</th> 
            <th>Check Number</th>
            <th>Bank</th>
            <th>Check Type</th>
            <th class="right">Amount (PHP)</th>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${supplierPayment?.items?.sort{it.id}}" var="i" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}supplierPaymentItem/show/${i.id}'">
          	<td>${i?.payment?.toString()}
          	<td class="center"><g:formatDate date="${i?.date}" format="MM/dd/yyyy" /></td>
            <td>${i?.type}</td>
            <td>${i?.checkNumber}</td>
            <td>${i?.checkBank}</td>
            <td>${i?.checkType?.description}</td>
          <td class="right">${i?.formatAmount()}</td>
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
            <td class="right"><strong>${supplierPayment?.formatPaymentTotal()}</strong></td>
          </tr>
          <tr>
            <td><strong>Remaining</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${supplierPayment?.formatRemainingBalance()}</strong></td>
          </tr>
        </tfoot>
      </table>
    </div--->

	<g:if test="${!purchaseInvoiceInstance?.isCancelled()}" >
    <div class="buttons">
		<g:hiddenField name="id" value="${purchaseInvoiceInstance?.id}" />
        <g:if test="${purchaseInvoiceInstance?.isUnapproved()}" >
            <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');"/></span>
        </g:if>
        
        <g:if test="${purchaseInvoiceInstance?.isUnapproved()}">
       	  <span class="button"><g:actionSubmit class="save" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>

          <g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
            <g:if test="${purchaseInvoiceInstance?.exchangeRate > 0}" >
                <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
            </g:if>
          </g:ifAnyGranted>
        </g:if>
        <g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
          <g:if test="${purchaseInvoiceInstance?.isApproved()}">
            <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.unapprove.label', default: 'Unapprove')}" onclick="return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
          </g:if>
          <g:if test="${purchaseInvoiceInstance?.isApproved() || purchaseInvoiceInstance?.isPaid()}">
			  <span class="button"><g:link class="print" target="blank" controller="print" action="purchaseInvoice" id="${purchaseInvoiceInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print</g:link></span>
			  <span class="button"><g:link class="print" target="blank" controller="print" action="purchaseInvoiceNoPrice" id="${purchaseInvoiceInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">Print (No Price)</g:link></span>
          </g:if>
        </g:ifAnyGranted>
	  </div>
	  </g:if>
      </g:form>
    </div>
  </div>
</body>
</html>
