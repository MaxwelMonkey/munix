
<%@ page import="com.munix.DiscountGroup; com.munix.Customer; com.munix.PrintLogSalesOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <g:set var="warehouseList" value="${com.munix.Warehouse.list().sort{it.identifier}}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <g:javascript>
    function goTo(url){
    window.location = url;
    }
    
    function uploadForm(){
    	$("#uploadDiv").show("slow");
    }
    
    function showWarehouse(wh, checked){
    	if(checked){
    		$(".warehouse"+wh).show();
    	}else{
    		$(".warehouse"+wh).hide();
    	}
    }
  </g:javascript>
  <style>
  .warehouseCheckbox{
  	padding-right:20px;
  }
  .warehouse{
  	display:none;
  }
  </style>
</head>
<body>
	<g:if test="${params.a=='print'}">
	<script>alert("Your request has been sent for approval.");</script>
	</g:if>
	<g:if test="${params.a=='printAllowed'}">
	<script>window.open("${createLink(uri: '/salesOrder/printApproved/'+salesOrderInstance.id)}");
	</script>
	</g:if>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create">Create</g:link></span>
    <span class="menuButton"><g:link class="create" action="upload">Create (from SO Form)</g:link></span>
    <span class="menuButton"><g:link class="create" action="excelForm">Download SO Form</g:link></span>
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
    <h2>Sales Order Details</h2>
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.id.label" default="Id" /></td>
	        <td valign="top" class="value">${salesOrderInstance}</td>
	        <td valign="top" class="name"><g:message code="salesOrder.date.label" default="Date" /></td>
	        <td valign="top" class="value"><g:formatDate date="${salesOrderInstance?.date}" format="MMM. dd, yyyy" /></td>
          </tr>

       	  <tr class="prop">
            <g:set var="printBadAccount" value=""></g:set>
            <g:if test="${salesOrderInstance.customer.isBadAccount()}">
                <g:set var="printBadAccount" value="Bad Account"></g:set>
            </g:if>
       	    <td valign="top" class="name"><g:message code="salesOrder.customer.label" default="Customer" /></td>
	        <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${salesOrderInstance?.customer?.id}">${salesOrderInstance?.customer?.encodeAsHTML()}</g:link> <font color="red"><strong>${printBadAccount}</strong></font></td>
		    <td valign="top" class="name"><g:message code="salesOrder.deliveryDate.label" default="Delivery Date" /></td>
		    <td valign="top" class="value"><g:formatDate date="${salesOrderInstance?.deliveryDate}"  format="MMM. dd, yyyy"/></td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.salesAgent.label" default="Sales Agent" /></td>
            <td valign="top" class="value"><g:link controller="salesAgent" action="show" id="${salesOrderInstance?.customer?.salesAgent?.id}">${salesOrderInstance?.customer?.salesAgent?.formatName()?.encodeAsHTML()}</g:link></td>
		 	<td valign="top" class="name"><g:message code="salesOrder.preparedBy.label" default="Prepared By" /></td>
	        <td valign="top" class="value"> ${salesOrderInstance?.preparedBy?.encodeAsHTML()} </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.forwarder.label" default="Forwarder" /></td>
            <td valign="top" class="value"><g:link controller="forwarder" action="show" id="${salesOrderInstance?.customer?.forwarder?.id}">${salesOrderInstance?.customer?.forwarder}</g:link></td>
     	    <td valign="top" class="name"><g:message code="salesOrder.approvedBy.label" default="1st Approval" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.approvedBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesOrder.discountType.label" default="Discount Type" /></td>
     	    <td valign="top" class="value">${salesOrderInstance?.discountType}</td>
            <td valign="top" class="name"><g:message code="salesOrder.approvedTwoBy.label" default="2nd Approval" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.approvedTwoBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.discountGroup.label" default="Discount Group" /></td>
            <td valign="top" class="value">
                <g:if test="${boldDiscountGroup}">
                  <strong>${salesOrderInstance?.discountGroup?.encodeAsHTML()}</strong>
                </g:if>
                <g:else>
                  ${salesOrderInstance?.discountGroup?.encodeAsHTML()}
                </g:else>
            </td>
            <td valign="top" class="name"><g:message code="salesOrder.status.label" default="Status" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesOrderInstance, field: "status")}</td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.netDiscountGroup.label" default="Net Discount Group" /></td>
            <td valign="top" class="value">
                <g:if test="${boldNetDiscountGroup}">
                  <strong>${salesOrderInstance?.netDiscountGroup?.encodeAsHTML()}</strong>
                </g:if>
                <g:else>
                  ${salesOrderInstance?.netDiscountGroup?.encodeAsHTML()}
                </g:else>
            </td>
	        <td valign="top" class="name"><g:message code="salesOrder.closedBy.label" default="Closed By" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesOrderInstance, field: "closedBy")}</td>
          </tr>

          <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesOrder.remark.label" default="Remarks" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesOrderInstance, field: "remark")}</td>
	        <td valign="top" class="name"><g:message code="salesOrder.cancelledBy.label" default="Cancelled By" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesOrderInstance, field: "cancelledBy")}</td>
          </tr>

        </tbody>
      </table>
    </div>
    <div class="dialog">
    <h2>Customer Account Details</h2>
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name" rowspan="2"><g:message code="salesOrder.salesDelivery.label" default="Sales Delivery" /></td>
       	    <g:set var="oui" value="${com.munix.SalesDelivery.findAllByCustomerAndStatus(salesOrderInstance?.customer, 'Unpaid')?.sort{it.date}}"/>
       	    <g:if test="${oui.size()>0}"><g:set var="oui" value="${oui.get(0)}"/>
		    <td valign="top" class="value"><g:link controller="salesDelivery" action="show" id="${oui.id}"><g:formatDate date="${oui.date}"  format="MMM. dd, yyyy"/> ${oui} <g:formatNumber number="${oui?.computeAmountDue()}" format="###,##0.00" /></g:link></td>
		    </g:if>
		    <g:else>
		    <td valign="top" class="value"></td>
		    </g:else>
	        <td valign="top" class="name"><g:message code="customer.term.label" default="Terms" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.customer?.term}</td>
          </tr>
          <tr class="prop">
	        <td valign="top" class="value"><g:link controller="salesDelivery" action="unpaidList" params="['customerId':salesOrderInstance?.customer?.id]">View Unpaid Sales Delivery List</g:link></td>
	        <td valign="top" class="name"><g:message code="customer.remainingCredit.label" default="Remaining Credit" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.customer?.getRemainingCredit()}</td>
          </tr>
          <tr class="prop">
            <td valign="top" class="name" rowspan="2"><g:message code="salesOrder.customerCharge.label" default="Customer Charge" /></td>
       	    <g:set var="oui" value="${com.munix.CustomerCharge.findAllByCustomerAndStatus(salesOrderInstance?.customer, 'Unpaid')?.sort{it.date}}"/>
       	    <g:if test="${oui.size()>0}"><g:set var="oui" value="${oui.get(0)}"/>
		    <td valign="top" class="value"><g:link controller="customerCharge" action="show" id="${oui.id}"><g:formatDate date="${oui.date}"  format="MMM. dd, yyyy"/> ${oui} <g:formatNumber number="${oui?.computeAmountDue()}" format="###,##0.00" /></g:link></td>
		    </g:if>
		    <g:else>
		    <td valign="top" class="value"></td>
		    </g:else>
	        <td valign="top" class="name"><g:message code="customer.remark.label" default="Customer Remarks" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.customer?.generalRemark}</td>
          </tr>
          <tr class="prop">
	        <td valign="top" class="value"><g:link controller="customerCharge" action="unpaidList" params="['customerId':salesOrderInstance?.customer?.id]">View Unpaid Customer Charge List</g:link></td>
	        <td valign="top" class="name"><g:message code="customer.billing.label" default="Billing" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.customer?.formatBillingAddress()}</td>
          </tr>
          <tr class="prop">
            <td valign="top" class="name" rowspan="2"><g:message code="salesOrder.bouncedCheck.label" default="Bounced Check" /></td>
       	    <g:set var="oui" value="${com.munix.BouncedCheck.findAllByCustomerAndStatus(salesOrderInstance?.customer, 'Unpaid')?.sort{it.date}}"/>
       	    <g:if test="${oui.size()>0}"><g:set var="oui" value="${oui.get(0)}"/>
		    <td valign="top" class="value"><g:link controller="bouncedCheck" action="show" id="${oui.id}"><g:formatDate date="${oui.date}"  format="MMM. dd, yyyy"/> ${oui} <g:formatNumber number="${oui?.computeAmountDue()}" format="###,##0.00" /></g:link></td>
		    </g:if>
		    <g:else>
		    <td valign="top" class="value"></td>
		    </g:else>
	        <td valign="top" class="name"><g:message code="customer.landline.label" default="Landline" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.customer?.landline}</td>
          </tr>
          <tr class="prop">
	        <td valign="top" class="value"><g:link controller="bouncedCheck" action="unpaidList" params="['customerId':salesOrderInstance?.customer?.id]">View Unpaid Bounced Check List</g:link></td>
	        <td valign="top" class="name"><g:message code="customer.mobile.label" default="Mobile" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.customer?.mobile}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <strong>Display Stocks:</strong>
    <g:each in="${warehouseList}" var="warehouse">
    	<span class="warehouseCheckbox"><span><input type="checkbox" onclick="showWarehouse('${warehouse.id}', this.checked);"></span><span>${warehouse.identifier}</span></span>
    </g:each>
    <div class="subTable">
      <table>
        <h2>Discounted Price Items</h2>

        <thead>
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Part Number</th>
            <th class="center">Description</th>
            <th class="center">Unit</th>
            <th class="center">Package Details</th>
            <th class="center">Net Item</th>
        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="center">Price</th>
            <th class="center">Final Price</th>
        </g:ifAnyGranted>
	        <g:each in="${warehouseList}" var="warehouse">
	          <th class="right warehouse warehouse${warehouse.id}">${warehouse?.identifier}</th>
	        </g:each>        
            <th class="center">Quantity</th>
            <th class="center">Delivered</th>
            <th class="center">Remaining</th>
        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="center">Amount</th>
        </g:ifAnyGranted>
          </tr>
        </thead>
        <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
          <tbody class="editable">
        </g:if>

        <g:else>
          <tbody class="uneditable">
        </g:else>

        <g:each in="${salesOrderInstance?.items?.findAll {!it?.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
          <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" >
          </g:if>

          <g:else>
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          </g:else>
          <td><g:link controller="product" action="show" id="${i?.product?.id}">${i?.product}</g:link></td>
          <td>${i?.product?.partNumber}</td>
          <td>${i?.product?.description}</td>
          <td>${i?.product?.unit?.description}</td>
          <td>${i?.product?.formatPackageDetails() }</td>
          <g:if test="${i?.isNet}">
            <td class="center">X</td>
          </g:if>
          <g:else>
            <td class="center"></td>
          </g:else>
        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
          <td class="right"><g:if test="${i?.price==0}">(FREE)</g:if><g:else>${i?.formatPrice()}</g:else></td>
          <td class="right">
          <g:if test="${i?.finalPrice==0}">(FREE)</g:if>
          <g:else>
	          <g:if test="${i?.finalPrice != i?.price}">
	            <strong>${i?.formatFinalPrice()}</strong>
	          </g:if>
	          <g:else>
	            ${i?.formatFinalPrice()}
	          </g:else>
	      </g:else>
          </td>
       </g:ifAnyGranted>
	        <g:each in="${warehouseList}" var="warehouse">
	        		<g:set var="positive" value="${''}"/>
	        	<g:if test="${i?.product?.getStock(warehouse).qty>0}">
	        		<g:set var="positive" value="${'positive'}"/>
	        	</g:if> 
	        	<g:if test="${i?.product?.getStock(warehouse).qty<0}">
	        		<g:set var="positive" value="${'negative'}"/>
	        	</g:if> 
	        	
            <td class="right warehouse ${positive} warehouse${warehouse.id}">${i?.product?.formatSOH(warehouse)}</td>
	        </g:each>        
          <td class="right">${i?.formatQty()}</td>
          <td class="right">${i?.formatDeliveredQty()}</td>
          <td class="right">${i?.formatRemainingBalance()}</td>
        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
          <td class="right">${i?.formatAmount()}</td>
        </g:ifAnyGranted>
          </tr>
        </g:each>
        </tbody>
		<g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
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
	        <g:each in="${warehouseList}" var="warehouse">
	          <td class="warehouse warehouse${warehouse.id}"></td>
	        </g:each>        
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${salesOrderInstance.formatDiscountedItemsTotal()}</strong></td>
          </tr>
          <tr>
            <td><strong>Discount</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
	        <g:each in="${warehouseList}" var="warehouse">
	          <td class="warehouse warehouse${warehouse.id}"></td>
	        </g:each>        
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${salesOrderInstance?.discountGroup?.formatRate()}</strong></td>
            <td class="right"><strong>${salesOrderInstance?.formatDiscountedDiscount()}</strong></td>
          </tr>
          <tr>
            <td><strong>Discounted Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
	        <g:each in="${warehouseList}" var="warehouse">
	          <td class="warehouse warehouse${warehouse.id}"></td>
	        </g:each>        
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${salesOrderInstance?.formatDiscountedTotal()}</strong></td>
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
              <th class="center">Identifier</th>
              <th class="center">Part Number</th>
              <th class="center">Description</th>
              <th class="center">Unit</th>
              <th class="center">Package Details</th>
              <th class="center">Net Item</th>
          <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
              <th class="center">Price</th>
              <th class="center">Final Price</th>
          </g:ifAnyGranted>
	        <g:each in="${warehouseList}" var="warehouse">
	          <th class="right warehouse warehouse${warehouse.id}">${warehouse?.identifier}</th>
	        </g:each>        
              <th class="center">Quantity</th>
              <th class="center">Delivered</th>
              <th class="center">Remaining</th>
          <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
              <th class="center">Amount</th>
          </g:ifAnyGranted>
            </tr>
          </thead>
          <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
            <tbody class="editable">
          </g:if>

          <g:else>
            <tbody class="uneditable">
          </g:else>

          <g:each in="${salesOrderInstance?.items?.findAll {it?.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
            <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
              <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
            </g:if>

            <g:else>
              <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
            </g:else>

	          <td><g:link controller="product" action="show" id="${i?.product?.id}">${i?.product}</g:link></td>
            <td>${i?.product?.partNumber}</td>
            <td>${i?.product?.description}</td>
            <td>${i?.product?.unit?.description}</td>
            <td>${i?.product?.formatPackageDetails() }</td>
            <g:if test="${i?.isNet}">
            	<td class="center">X</td>
          	</g:if>
          	<g:else>
            	<td class="center"></td>
          	</g:else>
        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
          <td class="right"><g:if test="${i?.price==0}">(FREE)</g:if><g:else>${i?.formatPrice()}</g:else></td>
          <td class="right">
          <g:if test="${i?.finalPrice==0}">(FREE)</g:if>
          <g:else>
	          <g:if test="${i?.finalPrice != i?.price}">
	            <strong>${i?.formatFinalPrice()}</strong>
	          </g:if>
	          <g:else>
	            ${i?.formatFinalPrice()}
	          </g:else>
	      </g:else>
          </td>
        </g:ifAnyGranted>
	        <g:each in="${warehouseList}" var="warehouse">
	        		<g:set var="positive" value="${''}"/>
	        	<g:if test="${i?.product?.getStock(warehouse).qty>0}">
	        		<g:set var="positive" value="${'positive'}"/>
	        	</g:if> 
	        	<g:if test="${i?.product?.getStock(warehouse).qty<0}">
	        		<g:set var="positive" value="${'negative'}"/>
	        	</g:if> 
	        	
            <td class="right warehouse ${positive} warehouse${warehouse.id}">${i?.product?.formatSOH(warehouse)}</td>
	        </g:each>        
            <td class="right">${i?.formatQty()}</td>
            <td class="right">${i?.formatDeliveredQty()}</td>
            <td class="right">${i?.formatRemainingBalance()}</td>
        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <td class="right">${i?.formatAmount()}</td>
        </g:ifAnyGranted>
            </tr>
          </g:each>
          </tbody>
		<g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
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
	        <g:each in="${warehouseList}" var="warehouse">
	          <td class="warehouse warehouse${warehouse.id}"></td>
	        </g:each>        
              <td></td>
              <td></td>
              <td></td>
              <td class="right"><strong>${salesOrderInstance.formatNetItemsTotal()}</strong></td>
            </tr>
            <tr>
            <td><strong>Discount</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
	        <g:each in="${warehouseList}" var="warehouse">
	          <td class="warehouse warehouse${warehouse.id}"></td>
	        </g:each>        
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${salesOrderInstance?.netDiscountGroup?.formatRate()}</strong></td>
            <td class="right"><strong>${salesOrderInstance?.formatNetDiscount()}</strong></td>
          </tr>
          <tr>
            <td><strong>Discounted Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
	        <g:each in="${warehouseList}" var="warehouse">
	          <td class="warehouse warehouse${warehouse.id}"></td>
	        </g:each>        
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${salesOrderInstance?.formatNetTotal()}</strong></td>
          </tr>
          </tfoot>
          </g:ifAnyGranted>
        </table>
      </div>

	<g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
      <table>
        <h2>Total</h2>
          <tfoot class="total">
              <tr>
                  <td><strong>Discounted Items Total</strong></td>
                  <td></td>
                  <td></td>
                  <td class="right">
                      <strong></strong>
                  </td>
                  <td class="right">
                      <strong></strong>
                  </td>
                  <td class="discountedItemsTotal right">
                      <strong>${salesOrderInstance?.formatDiscountedTotal()}</strong>
                  </td>

              </tr>
          <tr>
              <td><strong>Net Items Total</strong></td>
              <td></td>
              <td></td>
              <td class="right">
                  <strong></strong>
              </td>
              <td class="right">
                  <strong></strong>
              </td>
              <td class="netItemsTotal right">
                  <strong>${salesOrderInstance?.formatNetTotal()}</strong>
              </td>

          </tr>
          <tr>
              <td><strong>Grand Total</strong></td>
              <td></td>
              <td></td>
              <td class="right">
                  <strong></strong>
              </td>
              <td class="right">
                  <strong></strong>
              </td>
              <td class="grandTotal right">
                  <strong>${salesOrderInstance?.formatGrandTotal()}</strong>
              </td>

          </tr>
          </tfoot>
      </table>
	</g:ifAnyGranted>
	
    <div class="subTable">
      <table>
        <h2>Deliveries</h2>
        <thead>
          <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Status</th>
            <th>Delivery</th>
            <th>Trip Ticket</th>
        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
            <th class="right">Amount</th>
        </g:ifAnyGranted>
          </tr>
        </thead>
        <tbody class="editable">
        <g:each in="${salesOrderInstance.deliveries.sort{it.toString()}}" var="delivery" status="colors">
          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="goTo('${createLink(controller : "salesDelivery", action : "show", id : delivery?.id)}')">
            <td>${delivery}</td>
            <td><g:formatDate date="${delivery?.date}" format="MM/dd/yyyy" /></td>
          <td>${delivery?.status}</td>
          <g:if test="${delivery?.directDelivery != null}">
            <td><g:link controller="directDelivery" action="show" id="${delivery?.directDelivery?.id}">${delivery?.directDelivery}</g:link></td>
          </g:if>
          <g:elseif test="${delivery?.waybill != null}">
            <td><g:link controller="waybill" action="show" id="${delivery?.waybill?.id}">${delivery?.waybill}</g:link></td>
          </g:elseif>
          <g:else>
            <td></td>
          </g:else>

          <g:if test="${delivery?.waybill != null}">
            <td><g:link controller="tripTicket" action="show" id="${delivery?.waybill?.tripTicket?.tripTicket?.id}">${delivery?.waybill?.tripTicket?.tripTicket}</g:link></td>
          </g:if>
          <g:elseif test="${delivery?.directDelivery != null}">
            <td><g:link controller="tripTicket" action="show" id="${delivery?.directDelivery?.tripTicket?.tripTicket?.id}">${delivery?.directDelivery?.tripTicket?.tripTicket}</g:link></td>
          </g:elseif>
          <g:else>
            <td></td>
          </g:else>

        <g:ifAnyGranted role = "ROLE_SALES,ROLE_ACCOUNTING">
          <td class="right">PHP <g:formatNumber number="${delivery?.computeTotalAmount()}" format="###,##0.00" /></td>
        </g:ifAnyGranted>
          </tr>
        </g:each>
        </tbody>
		<g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
        <tfoot class="total">
          <tr>
            <td><strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right"><strong>${salesOrderInstance?.formatDeliveryTotal()}</strong></td>
          </tr>
        </tfoot>
        </g:ifAnyGranted>
      </table>
    </div>
	<div class="subTable">
	<table>
	  <h2>Print History</h2>
	  <thead>
		<tr>
		  <th>Printed By</th>
		  <th>With Price?</th>
		  <th>Date</th>
		</tr>
	  </thead>
	  <tbody class="editable">
	  <g:set var="counterPrice" value="${0}" />
	  <g:set var="counterNoPrice" value="${0}" />
	  <g:each in="${salesOrderInstance?.printLogs?.sort{it?.date}}" var="i" status="colors">
		<tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
		  <td>${i.user.userRealName}</td>
		  <td>
		  	<g:if test="${i.printType==PrintLogSalesOrder.PrintType.PRICE}">
		  		Yes
				<g:set var="counterPrice" value="${counterPrice+1}" />
		  	</g:if>
			<g:else>
				No
				<g:set var="counterNoPrice" value="${counterNoPrice+1}" />
			</g:else>
		  </td>
		  <td><g:formatDate date="${i?.date}" format="MMM. dd, yyyy - hh:mm:ss a" /></td>
		</tr>
	  </g:each>
	  	 <tfoot class="total">
          <tr>
             <td>Print Count (with Price)</td>
			 <td>${counterPrice}</td>
			 <td></td>
          </tr>
		  <tr>
			  <td>Print Count (no Price)</td>
			  <td>${counterNoPrice}</td>
			  <td></td>
		   </tr>
        </tfoot>
	  	 
	  </tbody>
	</table>
  </div>
  
    <div id="uploadDiv" class="dialog" style="display:none">
	  <g:form name="frmUpload" method="post" action="uploadFile" enctype="multipart/form-data">
          <g:hiddenField name="id" value="${salesOrderInstance?.id}" />
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">Please select file to upload.</td>
	        <td valign="top" class="value"><input id="fileUpload" type="file" name="file"><input type="submit" value="Submit"></td>
          </tr>
      	</tbody>
      </table>
      </g:form>
    </div>
    <div class="buttons">
      <g:form>
        <g:if test="${!salesOrderInstance?.isComplete()}">
          <g:hiddenField name="id" value="${salesOrderInstance?.id}" />
          <g:ifAnyGranted role="ROLE_SALES, ROLE_MANAGER_SALES">
            <g:if test="${salesOrderInstance?.isUnapproved()}">
              <span class="button"><g:link class="approve" controller="salesOrder" action="approve" id="${salesOrderInstance?.id}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');">Approve</g:link></span>
            </g:if>
          </g:ifAnyGranted>

          <g:ifAnyGranted role="ROLE_SALES">
            <g:if test="${salesOrderInstance?.isUnapproved()}">
              <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
              <span class="button"><input type="button" class="edit" value="${message(code: 'default.button.upload.label', default: 'Upload')}" onclick="uploadForm()"/></span>
            </g:if>
          </g:ifAnyGranted>

          <g:ifAnyGranted role="ROLE_ACCOUNTING">
            <g:if test="${salesOrderInstance?.isSecondApproval()}">
              <span class="button"><g:link class="approve" controller="salesOrder" action="approveTwo" id="${salesOrderInstance?.id}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');">Approve</g:link></span>
            </g:if>
          </g:ifAnyGranted>

          <g:ifAnyGranted role="ROLE_MANAGER_SALES,ROLE_ACCOUNTING,ROLE_SALES">
            <g:if test="${!salesOrderInstance.deliveries||salesOrderInstance.hasNoActiveDeliveries()}">
                <g:if test="${salesOrderInstance?.isApproved()||salesOrderInstance?.isSecondApproval()}">
                  <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="Unapprove" onclick="return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:if>
            </g:if>
          </g:ifAnyGranted>
          <g:if test="${salesOrderInstance?.isApproved()}">
            <span class="button"><g:link class="deliver" controller="salesDelivery" action="create" id="${salesOrderInstance?.id}">Deliver</g:link></span>
          </g:if>
          <g:if test="${!salesOrderInstance?.isUnapproved() && !salesOrderInstance?.isCancelled() && !salesOrderInstance?.isSecondApproval()}">
            <span class="button"><g:link class="print" controller="salesOrder" action="print" id="${salesOrderInstance?.id}">Price</g:link></span>
            <span class="button"><g:link class="print" target="_blank" controller="salesOrder" action="printNoPrice" id="${salesOrderInstance?.id}" onclick="javascript:setTimeout('location.reload(true)',500)">No Price</g:link></span>
          </g:if>
          <g:ifAnyGranted role="ROLE_SALES">
            <g:if test="${salesOrderInstance?.isApproved()}">
              <span class="button"><g:actionSubmit class="markAsComplete" action="markAsComplete" value="Mark As Complete" onclick="return confirm('${message(code: 'default.button.markAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
            </g:if>
            <g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
              <g:if test="${salesOrderInstance?.isUnapproved()}">
                <span class="button"><g:actionSubmit class="cancel" action="cancel" value="Cancel" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
              </g:if>
            </g:ifAnyGranted>
          </g:ifAnyGranted>
        </g:if>
       <g:ifAnyGranted role="ROLE_SUPER">
        <span class="button"><g:link controller="salesOrder" action="viewPriceMargin" id="${salesOrderInstance?.id}">View Margin</g:link></span>
        </g:ifAnyGranted>
        <span class="button"><g:link class="balance" controller="salesOrder" action="balance" id="${salesOrderInstance?.id}">Balance vs. Quantity</g:link></span>
      </g:form>
    </div>
  </div>
</body>
</html>
