
<%@ page import="com.munix.CreditMemoItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemoItem.label', default: 'Credit Memo Item')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="numbervalidation.js" />
  <g:javascript src="generalmethods.js" />
  <g:javascript src="jquery.calculation.js" />
  <g:javascript>
      $=jQuery.noConflict()

      function computeDiscountedNewPrice() {
   		$('#discountedNewPrice').calc("price - (price * discount)", {
   			price : $("#newPrice").val(),
			discount: $("#discount").val()
		 },
         	function(s) {
            	return addCommas(s)
         })
         $('#discountedNewPriceText').text($('#discountedNewPrice').val())
      }
  
      $(document).ready(function(){
        $('#newQty').ForceNumericOnly(false)
        $('#newPrice').ForceNumericOnly(true)
        $('#salesDeliverySelect').change(function(){
            $.ajax({ url: '<g:createLink action="updateSelect"/>',
                data: "selectedValue="+$('#salesDeliverySelect').val()+"&creditMemoId="+${creditMemoInstance?.id},
                success: function(resp){
                    $('#salesDeliveryItems')
                        .find('option')
                        .remove()
                        .end()
                        .append('<option value="">Select One..</option>')
                    $('#oldQty').val("")
                    $('#oldPrice').val("")
                    $.each(resp.items, function(key, value)
                    {
                        $('#salesDeliveryItems')
                            .append('<option value="'+value.id+'">'+value.product+'</option>')
                    });
                }
            });
        });
        $('#salesDeliveryItems').change(function(){
            $.ajax({ url: '<g:createLink action="updateOldPriceAndQtyFields"/>',
                data: "id=${creditMemoInstance?.id}&selectedValue="+$('#salesDeliveryItems').val(),
                success: function(resp){
                    $('#oldQty').val(resp.qty)
                    $('#oldPrice').val(resp.price)
                    $('#newPrice').val(resp.price)
                    $('#discountedNewPrice').val(resp.discountedNewPrice)
                    $('#discount').val(resp.discount)
            		computeDiscountedNewPrice()
                }
            });
        });
        
        $('#newPrice').bind("keyup", function(){
        	computeDiscountedNewPrice()
        });
        
        computeDiscountedNewPrice()
      });

  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="['Credit Memo Item']" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${creditMemoItemInstance}">
      <div class="errors">
        <g:renderErrors bean="${creditMemoItemInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" >
    	<g:hiddenField name="discountedNewPrice" />
    	<g:hiddenField class="discount" name="discount" id="discount" value="" />
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="creditMemo"><g:message code="creditMemoItem.creditMemo.label" default="Credit Memo" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'creditMemo', 'errors')}">
          <g:link controller="creditMemo" action="show" id="${creditMemoInstance?.id}">${creditMemoInstance}</g:link>
          </td>
          </tr>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="delivery"><g:message code="creditMemoItem.delivery.label" default="Delivery" /></label>
            </td>

            <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'delivery', 'errors')}">
          <g:select name="salesDelivery" id="salesDeliverySelect" noSelection="${['':'Select One...']}" from="${salesDeliveryList}" optionKey="id" value="${creditMemoItemInstance?.deliveryItem?.delivery?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="product"><g:message code="creditMemoItem.product.label" default="Product" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'product', 'errors')}">
          <g:select name="deliveryItem.id" id="salesDeliveryItems" noSelection="${['':'Select One...']}" from="${creditMemoItemInstance?.deliveryItem?.delivery?.items}" optionKey="id" optionValue="product" value="${creditMemoItemInstance?.deliveryItem?.id}"  />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="oldQty"><g:message code="creditMemoItem.oldQty.label" default="Old Quantity" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'oldQty', 'errors')}">
          	  <g:textField name="oldQty" id="oldQty" readonly="true" value="${fieldValue(bean: creditMemoItemInstance, field: 'oldQty')}" />
          	</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="oldPrice"><g:message code="creditMemoItem.oldPrice.label" default="Old Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'oldPrice', 'errors')}">
          <g:textField name="oldPrice" id="oldPrice" readonly="true" value="${fieldValue(bean: creditMemoItemInstance, field: 'oldPrice')}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="newQty"><g:message code="creditMemoItem.newQty.label" default="New Quantity" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'newQty', 'errors')}">
          <g:textField name="newQty" id="newQty" value="${fieldValue(bean: creditMemoItemInstance, field: 'newQty')}" maxlength="17"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="newPrice"><g:message code="creditMemoItem.newPrice.label" default="New Price" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'newPrice', 'errors')}">
          <g:textField name="newPrice" id="newPrice" value="${fieldValue(bean: creditMemoItemInstance, field: 'newPrice')}" maxlength="17"/>
          </td>
          </tr>

		  <tr class="prop">
            <td valign="top" class="name">
              <label for="discountedNewPrice"><g:message code="creditMemoItem.discountedNewPrice.label" default="Discounted New Price" /></label>
            </td>
            <td valign="top" class="value">
        	  <span id="discountedNewPriceText"></span>
          	</td>
          </tr>
        
          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="creditMemoItem.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: creditMemoItemInstance, field: 'remark', 'errors')}">
          <g:textArea name="remark" value="${creditMemoItemInstance?.remark}" />
          </td>
          </tr>

          </tbody>
        </table>
      </div>
      <div class="buttons">
        <input type="hidden" name="id" value="${params?.id}" />
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
