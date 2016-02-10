
<%@ page import="com.munix.PurchaseOrder" %>
<html>  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'purchaseOrder.label', default: 'PurchaseOrder')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <g:javascript src="jquery.dataTables.js" />
	<g:set var="warehouses" value="${com.munix.Warehouse.list(sort:'identifier')}"/>
	<script>
        function addWarehousesCheckboxes(){
        	<g:each in="${warehouses}" var="wh">
        	$(".warehouseFilters").append($("<span style='margin-left:30px'>${wh.identifier}</span>&nbsp;<input type='checkbox' class='warehouseFilter' style='height:auto;' value='${wh.id}'/>"));
        	</g:each>
        	$(".warehouseFilter").click(function(){
        		if(this.checked)
        			$(".warehouseColumn_"+$(this).val()).show();
        		else
        			$(".warehouseColumn_"+$(this).val()).hide();
        	});
        }
        
        $(document).ready(function(){
        	addWarehousesCheckboxes();
        });
	</script>
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
            <td valign="top" class="name"><g:message code="purchaseOrder.id.label" default="Id" /></td>
	        <td valign="top" class="value">${purchaseOrderInstance.formatId()}</td>
	        <td valign="top" class="name"><g:message code="purchaseOrder.preparedBy.label" default="Prepared By" /></td>
	        <td valign="top" class="value">${purchaseOrderInstance?.preparedBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
	        <td valign="top" class="name"><g:message code="purchaseOrder.date.label" default="Date" /></td>
	        <td valign="top" class="value"><g:formatDate date="${purchaseOrderInstance?.date}" format="MMM. dd, yyyy" /></td>
	        <td valign="top" class="name"><g:message code="purchaseOrder.approvedBy.label" default="Approved By" /></td>
	        <td valign="top" class="value">${purchaseOrderInstance?.approvedBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
          	<td valign="top" class="name"><g:message code="purchaseOrder.supplier.label" default="Supplier" /></td>
          	<td valign="top" class="value"><g:link elementId="supplierShowLink" controller="supplier" action="show" id="${purchaseOrderInstance?.supplier?.id}">${purchaseOrderInstance?.supplier?.name.encodeAsHTML()}</g:link></td>
          	<td valign="top" class="name">Closed By</td>
          	<td valign="top" class="value">${purchaseOrderInstance?.closedBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
          	<td valign="top" class="name"><g:message code="purchaseOrder.status.label" default="Status" /></td>
      	  	<td valign="top" class="value">${fieldValue(bean: purchaseOrderInstance, field: "status")}</td>
	        <td valign="top" class="name"><g:message code="purchaseOrder.cancelledBy.label" default="Cancelled By" /></td>
	        <td valign="top" class="value">${purchaseOrderInstance?.cancelledBy?.encodeAsHTML()}</td>
          </tr>
          
          <tr class="prop">
          <td valign="top" class="name"><g:message code="purchaseOrder.shipBy.label" default="Ship By" /></td>
          <td valign="top" class="value"><g:formatDate date="${purchaseOrderInstance?.shipBy}" format="MM/dd/yyyy" /></td>
          	<td valign="top" class="name"><g:message code="purchaseOrder.country.label" default="Country" /></td>
          	<td valign="top" class="value">${purchaseOrderInstance?.supplier?.country}</td>
          </tr>
          <tr class="prop">
          <td valign="top" class="name"><g:message code="purchaseOrder.remark.label" default="Remarks" /></td>
          <td valign="top" class="value">${purchaseOrderInstance?.remark?.encodeAsHTML()}</td>
          <td valign="top" class="name"></td>
          <td valign="top" class="value"></td>
          </tr>
        </tbody>
      </table>
    </div>

   <g:form name="purchaseOrderForm">
   <div class="subTable">
   	<div class="warehouseFilters">Warehouses: </div>
      <table>
        <thead>
          <tr>
            <th>Identifier</th>
            <th>Part Number</th>
            <th>Supplier Code</th>
            <th>Description</th>
        	<th>Is Net</th>
	        <g:each in="${warehouses}" status="i" var="warehouse">
	        <th style="display:none;" class="warehouseColumn_${warehouse?.id}">${warehouse?.identifier}</th>
	        </g:each>
            <g:ifAnyGranted role="ROLE_SUPER">
		        <th class="right">Retail Price</th>
		        <th class="right">Wholesale Price</th>
	        </g:ifAnyGranted>
            <th class="right">Price</th>
            <th class="right">Final Price</th>
            <g:if test="${purchaseOrderInstance?.status != 'Unapproved' && purchaseOrderInstance?.status != 'Complete'}">
            	<th class="right">Complete</th>
            </g:if>
            <th class="right">Purchase Invoice</th>
            <th class="right">Quantity</th>
            <th class="right">Received</th>
            <th class="right">Remaining</th>
            <th class="right">Amount</th>
          </tr>
        </thead>
        <tbody class="uneditable">
          <g:each in="${purchaseOrderInstance.items.sort{it.id}}" var="i" status="colors">
	          <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
	          <td><g:link controller="product" action="show" id="${i?.product?.id}">${i?.product}</g:link></td>
	          <td>${i?.product?.partNumber}</td>
	          <td>${i?.productCode}</td>
	          <td>${i?.product?.description}</td>
	          <td>${i?.product?.isNet?"Yes":""}</td>
    		<g:set var="stocks" value="${com.munix.Stock.findAll('from Stock s where s.product.id = :productId',['productId':i?.product?.id])}"/>
    		<g:set var="stockMap" value="${[:]}"/>
    		<g:each in="${stocks}" var="stock">
    			<% stockMap[stock.warehouse?.id] = stock.qty; %>
    		</g:each>
	        <g:each in="${warehouses}" var="warehouse">
        		<g:set var="qty" value="${stockMap[warehouse?.id]}"/>
        		<g:set var="negative" value="${''}"/>
        		<g:if test="${qty&&qty<0}">
        			<g:set var="negative" value="${'negative'}"/>
        		</g:if>
        		<g:set var="positive" value="${''}"/>
        		<g:if test="${qty&&qty>0}">
        			<g:set var="positive" value="${'positive'}"/>
        		</g:if>
        	<td style="display:none" class="right ${negative} ${positive} warehouseColumn_${warehouse?.id}">
        		<g:if test="${qty}">
    				${String.format('%,.0f',qty)}
    			</g:if>
    			<g:else>
    				0
    			</g:else>
        	</td>
        	</g:each>
	          
            <g:ifAnyGranted role="ROLE_SUPER">
            	<td class="right">${i?.product?.formatRetailPrice()}</td>
            	<td class="right">${i?.product?.formatWholeSalePrice()}</td>
            </g:ifAnyGranted>
	          <td class="right">
          	  <g:if test="${purchaseOrderInstance?.status == 'Approved' || purchaseOrderInstance?.status == 'Complete'}">
          	  	${i?.formatPrice()}
          	  </g:if>
          	  <g:else>
          	  	<g:set var="price" value="${null}"/>
          	  	<g:each in="${service.generateStockCostItems(i?.product)?.reverse()}" var="s">
          	  		<g:if test="${price==null && s.currency == purchaseOrderInstance.currency?.toString()}">
          	  			<g:set var="price" value="${s.costForeign}"/>
          	  		</g:if> 
          	  	</g:each> 
          	  	${price}
          	  </g:else>
          	  </td>
	          	
	          <td class="right">
		          <g:if test="${i?.finalPrice!=i.price}">
		          	<strong>${i?.formatFinalPrice()}</strong>
		          </g:if>
		          <g:else>
		          	${i?.formatFinalPrice()}
		          </g:else>
	          </td>
	          <g:if test="${purchaseOrderInstance?.status != 'Unapproved' && purchaseOrderInstance?.status != 'Complete'}">
	          	<td class="center">
	          	  <g:if test="${i?.hasInvoice() == true}">
			          <g:if test="${i?.isComplete == true}">
			            <g:checkBox name="completeElement${i.id}" value="${true}"/>
			          </g:if> <g:else>
			            <g:checkBox name="completeElement${i.id}" value="${false}" />
			          </g:else>
	          	  </g:if>
		        </td>
	          </g:if>
	          <td class="right">
	          	<g:each in="${i?.receivedItems}" var="j">
	          		<g:link action="show" id="${j.purchaseInvoice.id}" controller="purchaseInvoice">${j.purchaseInvoice}</g:link><br/>
	          	</g:each>
	          </td>
	          <td class="right">${i?.formatQty()}</td>
	          <td class="right">${i?.formatReceivedQty()}</td>
	          <td class="right">${i?.formatRemainingBalance()}</td>
	          <td class="right">${i?.formatAmount()}</td>
	          </tr>
        	</g:each>
        </tbody>
        <tfoot class="total">
          <tr>
           	<td> <strong>Total</strong></td>
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
	        <g:each in="${warehouses}" var="warehouse">
			<td style="display:none;" class="warehouseColumn_${warehouse?.id}"></td>
			</g:each>
            <g:ifAnyGranted role="ROLE_SUPER">
			<td></td>
			<td></td>
            </g:ifAnyGranted>
			<g:if test="${purchaseOrderInstance?.status != 'Unapproved' && purchaseOrderInstance?.status != 'Complete'}">
				<td></td>
			</g:if>
            <td class="right">${purchaseOrderInstance.formatTotal()}</td>
          </tr>
          <tr>
           	<td> <strong>Discount</strong></td>
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
	        <g:each in="${warehouses}" var="warehouse">
			<td style="display:none;" class="warehouseColumn_${warehouse?.id}"></td>
			</g:each>
            <g:ifAnyGranted role="ROLE_SUPER">
			<td></td>
			<td></td>
            </g:ifAnyGranted>
			<g:if test="${purchaseOrderInstance?.status != 'Unapproved' && purchaseOrderInstance?.status != 'Complete'}">
				<td></td>
			</g:if>
            <td class="right">${purchaseOrderInstance?.discountRate}%</td>
          </tr>
          <tr>
           	<td> <strong>Grand Total</strong></td>
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
	        <g:each in="${warehouses}" var="warehouse">
			<td style="display:none;" class="warehouseColumn_${warehouse?.id}"></td>
			</g:each>
            <g:ifAnyGranted role="ROLE_SUPER">
			<td></td>
			<td></td>
            </g:ifAnyGranted>
			<g:if test="${purchaseOrderInstance?.status != 'Unapproved' && purchaseOrderInstance?.status != 'Complete'}">
				<td></td>
			</g:if>
            <td class="right">${purchaseOrderInstance?.formatGrandTotal()}</td>
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
		  <th>Type</th>
		  <th>Date</th>
		</tr>
	  </thead>
	  <tbody class="editable">
	  <g:each in="${purchaseOrderInstance?.printLogs?.sort{it?.date}}" var="i" status="colors">
		<tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
		  <td>${i.user.userRealName}</td>
		  <td>${i.type}</td>
		  <td><g:formatDate date="${i?.date}" format="MMM. dd, yyyy - hh:mm a" /></td>
		</tr>
	  </g:each>
	  	 <tfoot class="total">
          <tr>
             <td>Print Count</td>
             <td></td>
			 <td>${purchaseOrderInstance.printLogs.size()}</td>
          </tr>
        </tfoot>
	  </tbody>
	</table>
  </div>
    <g:if test="${purchaseOrderInstance?.status != 'Complete'}">
      <div class="buttons">
          <g:hiddenField id="purchaseId" name="id" value="${purchaseOrderInstance?.id}"/>
          <g:if test="${purchaseOrderInstance?.status == 'Unapproved'}">
            <g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
            	<span class="button"><g:actionSubmit class="cancel" value="Cancel PO" action="cancel" id="${purchaseOrderInstance?.id}" onclick="isDirty=false;return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');"/></span>
            </g:ifAnyGranted>
            <g:ifAnyGranted role="ROLE_MANAGER_PURCHASING,ROLE_PURCHASING">
            	<span class="button"><g:actionSubmit class="edit" action="edit" value="Edit Orders" /></span>
            </g:ifAnyGranted>
            <g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
              <span class="button"><g:actionSubmit class="approve" action="approve" value="${message(code: 'default.button.approve.label', default: 'Approve')}" onclick="isDirty=false;return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
            </g:ifAnyGranted>
          </g:if>
          <g:if test="${purchaseOrderInstance?.status == 'Approved'}">
            <g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
              <g:if test="${disapprove}">
	              <span class="button"><g:actionSubmit class="unapprove" action="unapprove" value="${message(code: 'default.button.unapprove.label', default: 'Unapprove')}" onclick="isDirty=false;return confirm('${message(code: 'default.button.unapprove.confirm.message', default: 'Are you sure?')}');" /></span>
              </g:if>
              <span class="button"><g:actionSubmit class="save" action="updateCompleteStatus" value="Save" onclick="isDirty=false;return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
              <g:if test="${1==1 || purchaseOrderInstance.checkForCompletion()}">
                  <span class="button"><g:actionSubmit class="markAsComplete" action="markAsComplete" value="${message(code: 'default.button.markAsComplete.label', default: 'Mark As Complete')}" onclick="isDirty=false;return confirm('${message(code: 'default.button.markAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
              </g:if>
              <span class="button"><g:actionSubmit class="print" action="doPrint" value="Print (w/picture)" onclick="document.purchaseOrderForm.target='_blank'; window.location.reload();"/></span>
              <span class="button"><g:actionSubmit class="print" action="doPrintNoPrice" value="Print (No Price, w/picture)" onclick="document.purchaseOrderForm.target='_blank'; window.location.reload();"/></span>
              <span class="button"><g:actionSubmit class="print" action="doPrintNoPicture" value="Print (w/o picture)" onclick="document.purchaseOrderForm.target='_blank'; window.location.reload();"/></span>
              <span class="button"><g:actionSubmit class="print" action="doPrintNoPriceNoPicture" value="Print (No Price, w/o picture)" onclick="document.purchaseOrderForm.target='_blank'; window.location.reload();"/></span>
            </g:ifAnyGranted>
          </g:if>
          <g:if test="${purchaseOrderInstance?.status == 'Cancelled'}">
          	<g:ifAnyGranted role="ROLE_MANAGER_PURCHASING">
              <span class="button"><g:link action="unCancel" id="${purchaseOrderInstance?.id}" onclick="return confirm('Are you sure?');isDirty=false;">Revert</g:link></span>
             </g:ifAnyGranted>
          </g:if>
      </div>
    </g:if>
   </g:form>
    
  </div>
</body>
</html>
