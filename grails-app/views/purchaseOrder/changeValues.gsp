
<%@ page import="com.munix.PurchaseOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'purchaseOrder.label', default: 'PurchaseOrder')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <g:javascript src="generalmethods.js" />
    <g:javascript src="jquery.dataTables.js" />
    <g:javascript src="purchaseorder.js" />
    <g:javascript src="numbervalidation.js" />
    <g:javascript src="jquery.calculation.js" />
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
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="purchaseOrder.id.label" default="Id" /></td>
	        <td valign="top" class="value">${purchaseOrderInstance}</td>
          </tr>

          <tr class="prop">
	        <td valign="top" class="name"><g:message code="purchaseOrder.date.label" default="Date" /></td>
	        <td valign="top" class="value"><g:formatDate date="${purchaseOrderInstance?.date}" format="MMM. dd, yyyy" /></td>
          </tr>

          <tr class="prop">
          	<td valign="top" class="name"><g:message code="purchaseOrder.supplier.label" default="Supplier" /></td>
        	<td valign="top" class="value"><g:link controller="supplier" action="show" id="${purchaseOrderInstance?.supplier?.id}">${purchaseOrderInstance?.supplier?.name.encodeAsHTML()}</g:link></td>
          </tr>
          
        </tbody>
      </table>
    </div>

   <script type="text/javascript">
	   	isDirty = false;
	   	var maxLength = 16;
	   	var msg = 'You haven\'t saved your changes.';
	   	var $=jQuery.noConflict();
	   	table=$(".scrollContent").dataTable()
	   	
        function solvingAmount(){
        	var sum = 0
    		$.each($('total'),function(index,value){
        		var num=parseFloat($(value).text())
				sum+=num
        	});
        	sum = decimalFixer(sum)
        	$('#amountTotalText').text(productData.currency + " " + addCommas(sum))
        	$('#amountTotal').val(sum)
        }
        
        function addProductsAvailableTable(value){
	        if(value.productCode==null){
	        	value.productCode=""
		    }
			var productRow = $('<tr></tr>')
                .append('<td class="name">'+value.name+'</td>')
                .append('<td>'+value.productCode+'</td>')
                .append('<td>'+value.description+'</td>')
                .append('<td class="right">'+value.cost+'</td>')
			
			$(productRow).click( function(){
				addProductsToOrderTable(value)
				var row = $(this).get(0);
				$(".scrolltable").dataTable().fnDeleteRow($(".scrolltable").dataTable().fnGetPosition(row));
				isDirty=true;
			});
			$(".scrollContent").append(productRow)
			$(".scrolltable").dataTable({"bDestroy":true,"bAutoWidth": false,"bPaginate": false,"aaSorting": [[ 2, "asc" ]]})
			$(".scrolltable").dataTable()
        }

		function addProductsToOrderTable(value){
			var total
			var finalPrice
			var qty
			
			if(value.finalPrice==null){
				finalPrice=value.cost;
			}else{
				finalPrice=value.finalPrice
			}
			if(value.qty==null){
				qty=0;
				total=0;
			}else{
				qty=value.qty
				total=solvingTotal(finalPrice,qty)
			}
			if(value.productCode==null){
	        	value.productCode=""
		    }
		    
            var tr = $("<tr></tr>")
            var removeLink = $("<img src='${createLink(uri:'/')}images/cancel.png'/>")
            var removeCol = $("<td style='text-align: center'></td>").append(removeLink)
            var finalPriceField = $("<input type=\"text\" maxlength=\""+maxLength+"\" style='width:80px' MAXLENGTH=\"18\" name=\"finalPrice"+value.itemId+"\" id=\"finalPrice"+value.itemId+"\" value="+finalPrice+">").ForceNumericOnly(true)
            var finalPriceCol = $("<td class=\"right\"></td>").append(finalPriceField)
            var qtyField = $("<input type=\"text\" maxlength=\""+maxLength+"\" style='width:80px' MAXLENGTH=\"18\" name=\"finalQty"+value.itemId+"\" id=\"finalQty"+value.itemId+"\" value="+qty+">").ForceNumericOnly(false)
            var qtyCol = $("<td class=\"right\"></td>").append(qtyField)
            
            $(removeLink).click(function(){
			    $(tr).remove();
                   solvingAmount();
                   addProductsAvailableTable(value)
                   isDirty=true						
		    });
            
            $(tr).append(removeCol)
                .append("<td>"+value.name+"</td>")
                .append("<td>"+value.productCode+"</td>")
                .append("<td style='width:300px'>"+value.description+"</td>")
                .append("<td class=\"right\">"+value.cost+"</td>")
                .append(finalPriceCol)
                .append(qtyCol)
                .append("<td class=\"right\"><total>"+total+"</total></td>")
                .append("<input type=\"hidden\" name='itemId' id=\"itemId\" MAXLENGTH=\"18\" value=\""+value.itemId+"\"/>")
			
           	$(".editable").append(tr);
            solvingAmount();
		}

		function computeDiscountedTotal() {
			$("#grandTotal").calc("undiscountedTotal - (undiscountedTotal * rate / 100)",
                    {
                        undiscountedTotal : $("#amountTotal").val(),
                        rate : $("#discountRate").val()
                    },
                    function(s){
                        return $("#currency").text() + " " + addCommas(s)
                    }
                )
		}
		
        $(document).ready(function(){
	        productData = "";
	        $.ajax({ url: "${createLink(uri:'/')}purchaseOrder/getItems",
                   data: "selectedValue="+$('#purchaseId').val(),
                   success: function(resp){
                   	productData=resp
                   	$(".scrolltable").dataTable({
                   		"aoColumns": [
              						{ "sWidth": "150px" },
              						{ "sWidth": "150px" },
              						{ "sWidth": "500px" },
              						{ "sWidth": "100px" }
              			],
              			"bAutoWidth": false,
              			"bPaginate": false,
						"aaSorting": [[ 2, "asc" ]]
					});
					$.each(resp.availableItems, function(index,value){
						value.cost=decimalFixer(value.cost)
						addProductsAvailableTable(value);
					});
					$.each(resp.inventory, function(index,value){
						value.cost=decimalFixer(value.cost)
						value.finalPrice=decimalFixer(value.finalPrice)
						addProductsToOrderTable(value);
					});
					$("#currency").text(resp.currency)
					computeDiscountedTotal()
	        	}
	        });
            $("#discountRate").change(function() {
            	$("#discount").text($("#discountRate").val() + " %")
            	computeDiscountedTotal()
            });
        	$( "input[ id ^= 'final' ]" ).live("change",function(){
				$(this).closest("tr").find("total").text(solvingTotal($(this).closest("tr").find('input[id^="finalQty"]').val(),$(this).closest("tr").find('input[id^="finalPrice"]').val()))
				solvingAmount();
				computeDiscountedTotal()
				isDirty=true;
			});
        	window.onbeforeunload = function(){
 	           if(isDirty){
 	        	   return msg;
 	           }
 	        };
   		});
        </script>
   	  
          <g:form>
            <g:hiddenField name="amountTotal" />
            <g:hiddenField name="currency" />
    <div class="dialog">
      <table>
        <tbody>
          
          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="purchaseOrder.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderInstance, field: 'remark', 'errors')}">
              <g:textArea name="remark" value="${purchaseOrderInstance?.remark}" maxlength="255"/>
            </td>
          </tr>
          
          <tr class="prop">
            <td valign="top" class="name">
              <label for="discountRate"><g:message code="purchaseOrder.discountRate.label" default="Discount Rate" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderInstance, field: 'discountRate', 'errors')}">
              <g:textField name="discountRate" value="${purchaseOrderInstance?.discountRate}"/>%
            </td>
          </tr>
         </tbody>
      </table>
    </div>
          
  	<g:if test="${purchaseOrderInstance?.status == 'Unapproved'}">
	   	<div id="tableContainer" class="tableContainer">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" class="scrollTable">
		<thead class="fixedHeader">
      	  <tr>
      	  	<th>Name</th>
      	  	<th>ID</th>
      	  	<th>Description</th>
      	  	<th>Unit Price</th>
      	  </tr>
      	</thead>
      	<tbody class="scrollContent">
      	</tbody>
      </table>
   </div>
      </g:if>
   
   <div class="subTable">
      <table>
        <thead>
          <tr>
         	<th>Cancel</th>
            <th>Identifier</th>
            <th>Supplier Code</th>
            <th>Description</th>
            <th>Price</th>
            <th>Final Price</th>
            <th>Quantity</th>
            <th>Amount</th>
          </tr>
        </thead>
          <tbody class="editable">
          </tbody>
        <tfoot class="total">
          <tr>
          	<g:if test="${purchaseOrderInstance?.status == 'Unapproved'}">
	            <td> <strong>Total</strong></td>
	            <td></td>
	            <td></td>
	            <td></td>
	            <td></td>
	            <td></td>
				<td></td>
	            <td class="right"><span id="amountTotalText">${purchaseOrderInstance.formatTotal()}</span></td>
            </g:if>
          </tr>
          <tr>
           	<td> <strong>Discount Rate</strong></td>
            <td></td>
	            <td></td>
	            <td></td>
	            <td></td>
	            <td></td>
				<td></td>
			<g:if test="${purchaseOrderInstance?.status != 'Unapproved' && purchaseOrderInstance?.status != 'Complete'}">
				<td></td>
			</g:if>
            <td class="right"><span id="discount">${purchaseOrderInstance?.discountRate}%</span></td>
          </tr>
          <tr>
           	<td> <strong>Grand Total</strong></td>
            <td></td>
	            <td></td>
	            <td></td>
	            <td></td>
	            <td></td>
				<td></td>
			<g:if test="${purchaseOrderInstance?.status != 'Unapproved' && purchaseOrderInstance?.status != 'Complete'}">
				<td></td>
			</g:if>
            <td class="right"><span id="grandTotal">${purchaseOrderInstance?.formatGrandTotal()}</span></td>
          </tr>
        </tfoot>
      </table>
    </div>

    <div class="buttons">
        <g:hiddenField id="purchaseId" name="id" value="${purchaseOrderInstance?.id}"/>
        <g:ifAnyGranted role="ROLE_MANAGER_PURCHASING,ROLE_PURCHASING">
        	<span class="button"><g:actionSubmit class="save" action="update" value="Update" onclick="isDirty=false;return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');" /></span>
    	</g:ifAnyGranted>
    </div>
   </g:form>
    
  </div>
</body>
</html>
