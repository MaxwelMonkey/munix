
<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
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
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${salesDeliveryInstance}">
    <div class="errors">
      <g:renderErrors bean="${salesDeliveryInstance}" as="list" />
    </div>
  </g:hasErrors>

  <g:form action="save" method="post" >
    <g:hiddenField name="discountRate" id='discountRate' value="${salesOrderInstance.discountGroup?.rate?:0}"/>
    <g:hiddenField name="netDiscountRate" id='netDiscountRate' value="${salesOrderInstance.netDiscountGroup?.rate?:0}"/>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="invoice"><g:message code="salesDelivery.invoice.label" default="Invoice" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesDeliveryInstance, field: 'invoice', 'errors')}">
        	  <g:link controller="salesOrder" action="show" id="${salesOrderInstance?.id}" >${salesOrderInstance}</g:link>
          	</td>
        </tr>

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
            <label for="receiptNo"><g:message code="salesDelivery.deliveryReceiptNumber.label" default="Delivery Receipt #" /></label>
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
          <g:textArea name="remark" maxlength= "250" value="${salesDeliveryInstance?.remark}" />
         </td>
        </tr>
        
        </tbody>
      </table>
    </div>
    <g:set var="idx" value="${0}"/>
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
        <g:each in="${salesOrderItems}" var="item" >
       	    <g:if test="${!item.item?.isNet}">
                  <tr>
                    <td>
                        ${item?.item?.product}
                        <g:hiddenField name="deliveryItemList[${idx}].product.id" value="${item?.item?.product?.id}"/>
                        <g:hiddenField name="deliveryItemList[${idx}].price" value="${item?.item?.finalPrice}"/>
                        <g:hiddenField name="deliveryItemList[${idx}].orderItem.id" value="${item?.item?.id}"/>
                        <g:hiddenField name="${item?.item?.product?.id}remaining" value="${item?.item?.computeRemainingBalance()?.intValue()}"/>
                    </td>
                    <td>${item?.item?.product?.description}</td>
                    <td class="right">${item?.item?.computeRemainingBalance()?.intValue()}</td>
                    <td class="right"><input onchange="recalc();" onblur="checkRemainingBalanceForDiscounted(${item?.item?.product?.id});" onkeyup="checkRemainingBalanceForDiscounted(${item?.item?.product?.id});" onkeypress="checkRemainingBalanceForDiscounted(${item?.item?.product?.id});" id="${item?.item?.product?.id}" class="right qty" type="text" name="deliveryItemList[${idx}].qty" value="${item?.item?.computeRemainingBalance()?.intValue()}" /></td>
                    <td class="right price">${item?.item?.finalPrice}</td>
                    <td class="right discountedAmount">PHP <g:formatNumber number="${item?.item?.computeAmount()}" format="###,##0.00" /></td>
	                <g:each in="${warehouses}" var="warehouses">
						<td class="right">${item.stocks[warehouses.identifier]}</td>                
	                </g:each> 
                  </tr>
                  <g:set var="idx" value="${idx+1}"/>
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
            <td>${salesOrderInstance?.discountGroup}</td>
            <td class="right"><discountedAmountTotal></discountedAmountTotal></td>
          </tr>
        </tfoot>
      </table>
    </div>
	
	<div class="subTable">
	<h2>Net Items</h2>
      <table class="deliveryItems">
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
        <g:each in="${salesOrderItems}" var="item" >
      	    <g:if test="${item.item?.isNet}">
              <tr>
                <td>
                    ${item?.item?.product}
                    <g:hiddenField name="deliveryItemList[${idx}].product.id" value="${item?.item?.product?.id}"/>
                    <g:hiddenField name="deliveryItemList[${idx}].price" value="${item?.item?.finalPrice}"/>
                    <g:hiddenField name="deliveryItemList[${idx}].orderItem.id" value="${item?.item?.id}"/>
                    <g:hiddenField name="${item?.item?.product?.id}remaining" value="${item?.item?.computeRemainingBalance()?.intValue()}"/>
                </td>
                <td>${item?.item?.product?.description}</td>
                <td class="right">${item?.item?.computeRemainingBalance()?.intValue()}</td>
                <td class="right"><input onchange="recalc();" onblur="checkRemainingBalanceForNet(${item?.item?.product?.id});" onkeyup="checkRemainingBalanceForNet(${item?.item?.product?.id});" onkeypress="checkRemainingBalanceForNet(${item?.item?.product?.id});" id="${item?.item?.product?.id}" class="right qty" type="text" name="deliveryItemList[${idx}].qty" value="${item?.item?.computeRemainingBalance()?.intValue()}" /></td>
                <td class="right price">${item?.item?.finalPrice}</td>
                <td class="right netAmount">PHP <g:formatNumber number="${item?.item?.computeAmount()}" format="###,##0.00" /></td>
                <g:each in="${warehouses}" var="warehouses">
					<td class="right">${item.stocks[warehouses.identifier]}</td>
	            </g:each>                 
              </tr>
              <g:set var="idx" value="${idx+1}"/>
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
            <td>${salesOrderInstance?.netDiscountGroup}</td>
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
      <input type="hidden" name="id" value="${params.id}"/>
      <input type="hidden" name="version" value="${salesOrderInstance.version}"/>
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
