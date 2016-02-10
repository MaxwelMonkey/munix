
<%@ page import="com.munix.SalesOrderItem; com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <g:javascript src="jquery.calculation.js"/>
  <g:javascript src="generalmethods.js" />
  <g:javascript>
    var $=jQuery.noConflict();
    var computeDiscountedItemsTotal = function(){
        return $('.discountedAmount').sum(
        {
            bind: "bind",
            selector:"discountedItemsTotal",
            oncalc: function (value, settings){
		        $(settings.selector).html(formatString(value));
	        }
        })
    }
    var computeDiscountGroupTotal = function(){
        $('discountedAmountTotal').calc(
            "total * discount",
            {
                total:$('.discountedAmount').sum(),
                discount:(1-($("#discountRate").val()/100))
            },
            function(s){
                return formatString(s);
            }
        )
    }
    var computeNetTotal = function(){
        return $('.netAmount').sum()
    }
    var computeNetDiscountGroupTotal = function(){
        $('netAmountTotal').calc(
            "total * discount",
            {
                total:$('.netAmount').sum(),
                discount:(1-($("#netDiscountRate").val()/100))
            },
            function(s){
                return formatString(s);
            }
        )
    }    
    var computeNetAndDiscountedTotal = function(){
        $('grandTotal').calc("netTotal+discountTotal",{netTotal:$('netAmountTotal'),
            discountTotal:$('discountedAmountTotal')},
            function(s){
                return formatString(s);
            }
        )
    }
    var recalc = function(){
        computeDiscountedItemsTotal()
        computeDiscountGroupTotal()
        $('netItemsTotal').text(formatString(computeNetTotal()))
        computeNetDiscountGroupTotal()
        computeNetAndDiscountedTotal()
    }
    var checkRemainingBalance = function(product, status){
      var remainingField  = $("#"+product+"remaining")
      var qtyField = $("#"+product)
      var remainingBalance = parseFloat(remainingField.val());
      var qty = parseFloat(qtyField.val());
      var tr = $(qtyField).closest("tr")

      $("td."+status,tr).calc("qty * price", {qty : $("td input.qty", tr),
        price: $("td.price",tr)}, function (s){
		return formatString(s);
	  })
      return true;
    }
    var checkRemainingBalanceForDiscounted = function(product){
      checkRemainingBalance(product, "discountedAmount")
    }
    var checkRemainingBalanceForNet = function(product){
      checkRemainingBalance(product, "netAmount")
    }
    $(document).ready(function(){
      if($("#salesDeliveryNumber").val()==""){
      	$("#salesDeliveryNumber").hide()
      }else{
      	$("#salesInvoiceBox").attr("checked","checked")
      	$("#salesDeliveryNumber").show()
      }
      $("#salesInvoiceBox").click(function(){
    	$("#salesDeliveryNumber").toggle()
    	$("#salesDeliveryNumber").val("")
      })
      $("input.qty").ForceNumericOnly(false)
      recalc()
    })
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${salesDeliveryInstance}">
      <div class="errors">
        <g:renderErrors bean="${salesDeliveryInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:hiddenField name="discountRate" id='discountRate' value="${salesDeliveryInstance.invoice?.discountGroup?.rate?:0}"/>
    <g:hiddenField name="netDiscountRate" id='netDiscountRate' value="${salesDeliveryInstance.invoice?.netDiscountGroup?.rate?:0}"/>
    <g:form method="post" >
      <g:hiddenField name="id" value="${salesDeliveryInstance?.id}" />
      <g:hiddenField name="version" value="${salesDeliveryInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>
            
            <tr class="prop">
              <td valign="top" class="name">
                <label for="deliveryType"><g:message code="salesDelivery.deliveryType.label" default="Delivery Type" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: salesDeliveryInstance, field: 'deliveryType', 'errors')}">
                <g:select name="deliveryType" from="${['Deliver','Pickup']}" value="${salesDeliveryInstance?.deliveryType}" />
              </td>
            </tr>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="customer"><g:message code="salesDelivery.warehouse.label" default="Warehouse" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: salesDeliveryInstance, field: 'warehouse', 'errors')}">
                <g:select name="warehouse.id" class="warehouse" from="${com.munix.Warehouse.list().sort{it.toString()}}" optionValue="description" optionKey="id" value="${salesDeliveryInstance?.warehouse?.id}" />
              </td>
            </tr>
        	<tr class="prop">
          		<td valign="top" class="name">
            		<label for="receipt"><g:message code="salesDelivery.deliveryReceiptNumber.label" default="Delivery Receipt #" /></label>
          		</td>
				<td valign="top" class="value ${hasErrors(bean: salesDeliveryInstance, field: 'deliveryReceiptNumber', 'errors')}">
       	  			<g:textField name="deliveryReceiptNumber" value="${salesDeliveryInstance?.deliveryReceiptNumber}"></g:textField>
          		</td>
       		</tr>
			<tr class="prop">
	          <td valign="top" class="name">
	            <label for="deliveryType">Sales Invoice #</label>
	          </td>
	          <td valign="top" class="value ${hasErrors(bean: salesDeliveryInstance, field: 'deliveryType', 'errors')}">
	            <g:checkBox name="" id = "salesInvoiceBox"></g:checkBox>
	       	  	<g:textField name="salesDeliveryNumber" maxlength = "30" id="salesDeliveryNumber" value="${salesDeliveryInstance?.salesDeliveryNumber}"></g:textField>
	          </td>
	        </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="salesDelivery.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesDeliveryInstance, field: 'remark', 'errors')}">
          <g:textArea id="remark" maxlength="250" name="remark" value="${salesDeliveryInstance?.remark}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
    <g:set var="undeliveredIdx" value="${0}"/>
    <g:set var="deliveredIdx" value="${0}"/>
      <div class="subTable">
    <h2>Discounted Items</h2>
      <table >
        <thead>
          <tr>
            <th>Identifier</th>
            <th>Description</th>
            <th class="right">Remaining</th>
            <th class="right">Quantity</th>
            <th class="right">Price</th>
            <th class="right">Amount</th>
            <g:each in="${warehouses}" var="warehouse">            
            	<th class="right">${warehouse}</th>
            </g:each>
          </tr>
        </thead>
        <tbody>
          <g:each in="${productList}" var="productItem" status = "idx">
       	    <g:if test="${!productItem.isNet}">
               <tr>
                 <td>
                     ${productItem.productName}
                     <g:if test="${!productItem.isDeliver}">
                         <g:hiddenField name="deliveryItemList[${productItem.idx}].product.id" value="${productItem.productId}"/>
                         <g:hiddenField name="deliveryItemList[${productItem.idx}].price" value="${productItem.price}"/>
                         <g:hiddenField name="deliveryItemList[${productItem.idx}].orderItem.id" value="${productItem.orderItemId}"/>
                     </g:if>
                     <g:hiddenField name="${productItem.productId}remaining" value="${productItem.remainingBalance}"/>
                 </td>
                 <td>${productItem.description}</td>
                 <td class="right">${productItem.remainingBalance}</td>
                 <td class="right"><input onchange="recalc();" onblur="checkRemainingBalanceForDiscounted(${productItem.productId});" onkeyup="checkRemainingBalanceForDiscounted(${productItem.productId});" onkeypress="checkRemainingBalanceForDiscounted(${productItem.productId});" id="${productItem.productId}" class="right qty" type="text" name="deliveryItemList[${productItem.idx}].qty" value="${productItem.qty}" /></td>
                 <td class="right price">${productItem.price}</td>
                 <td class="right discountedAmount">PHP <g:formatNumber number="${productItem.amount}" format="###,##0.00" /></td>
                 <g:each in="${warehouses}" var="warehouses">
					<td class="right">${productItem.stocks[warehouses.identifier]}</td>
                 </g:each> 
               </tr>

             </g:if>
          </g:each>
        </tbody>
        <tfoot class="total">
          <tr>
            <td>Total</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <g:each in="${warehouses}">
            	<td></td>
            </g:each>
            <td class="right"><discountedItemsTotal></discountedItemsTotal></td>
          </tr>
          <tr>
            <td>Discounted Total</td>
            <td></td>
            <td></td>
            <td></td>	
            <g:each in="${warehouses}">
            	<td></td>
            </g:each>
            <td>${salesDeliveryInstance.invoice?.discountGroup}</td>
            <td class="right"><discountedAmountTotal></discountedAmountTotal></td>
          </tr>
        </tfoot>
      </table>
    </div>
	
	<div class="subTable">
	<h2>Net Items</h2>
      <table >
        <thead>
          <tr>
            <th>Identifier</th>
            <th>Description</th>
            <th class="right">Remaining</th>
            <th class="right">Quantity</th>
            <th class="right">Price</th>
            <th class="right">Amount</th>
            <g:each in="${warehouses}" var="warehouse">            
            	<th class="right">${warehouse}</th>
            </g:each>
          </tr>
        </thead>
        <tbody>
          <g:each in="${productList}" var="productItem" status="idx">
      	    <g:if test="${productItem.isNet}">
               <tr>
                 <td>
                     ${productItem.productName}
                     <g:if test="${!productItem.isDeliver}">
                         <g:hiddenField name="deliveryItemList[${productItem.idx}].product.id" value="${productItem.productId}"/>
                         <g:hiddenField name="deliveryItemList[${productItem.idx}].price" value="${productItem.price}"/>
                         <g:hiddenField name="deliveryItemList[${productItem.idx}].orderItem.id" value="${productItem.orderItemId}"/>
                     </g:if>
                     <g:hiddenField name="${productItem.productId}remaining" value="${productItem.remainingBalance}"/>
                 </td>
                 <td>${productItem.description}</td>
                 <td class="right">${productItem.remainingBalance}</td>
                 <td class="right"><input onchange="recalc();" onblur="checkRemainingBalanceForDiscounted(${productItem.productId});" onkeyup="checkRemainingBalanceForDiscounted(${productItem.productId});" onkeypress="checkRemainingBalanceForDiscounted(${productItem.productId});" id="${productItem.productId}" class="right qty" type="text" name="deliveryItemList[${productItem.idx}].qty" value="${productItem.qty}" /></td>
                 <td class="right price">${productItem.price}</td>
                 <td class="right netAmount">PHP <g:formatNumber number="${productItem.amount}" format="###,##0.00" /></td>
                 <g:each in="${warehouses}" var="warehouses">
				   <td class="right">${productItem.stocks[warehouses.identifier]}</td>
	             </g:each> 
               </tr>
            </g:if>
          </g:each>
        </tbody>
        <tfoot class="total">
          <tr>
            <td>Total</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <g:each in="${warehouses}">
            	<td></td>
            </g:each>
            <td class="right"><netItemsTotal></netItemsTotal></td>
          </tr>
          <tr>
            <td>Discounted Total</td>
            <td></td>
            <td></td>
            <td></td>
            <g:each in="${warehouses}">            
	           	<td></td>
            </g:each>            
            <td>${salesDeliveryInstance?.invoice?.netDiscountGroup}</td>
            <td class="right"><netAmountTotal></netAmountTotal></td>
          </tr>          
        </tfoot>
      </table>
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
                      <discountedAmountTotal></discountedAmountTotal>
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
                  <netAmountTotal></netAmountTotal>
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
                  <grandTotal></grandTotal>
              </td>

          </tr>
          </tfoot>
      </table>
    </div>
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
