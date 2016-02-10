<%@ page import="com.munix.PriceType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />

  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="generalmethods.js" />
  <g:javascript src="jquery.calculation.js" />
	<script>
    var $ = jQuery.noConflict()

    var itemsTableConfig = {
        id : 0,
        identifier : 1,
        description : 2,
        partNumber : 3,
        netPrice : 4,
        price : 5,
        packageDetails : 6,
        stock : 7,
        createRow : function(data, count){
            var ctx = this
            var row  = $("<tr><td><img src=\"../images/cancel.png\" class=\"remove\"/>"+
                            "<input type=\"hidden\" name=\"orderItemList["+count+"].isDeleted\" class=\"deleted\" value=\"false\">"+
                            "<input type=\"hidden\" name=\"orderItemList["+count+"].isNet\" class=\"isNet\" value=\""+data[itemsTableConfig.netPrice]+"\">"+
                            "<input type=\"hidden\" name=\"orderItemList["+count+"].price\" class=\"price\" value=\""+data[this.price]+"\">" +
                            "<input class=\"productId\" value=\""+data[this.id]+"\" type=\"hidden\" name=\"orderItemList["+count+"].product.id\" id=\"orderItemList["+count+"].product.id\"/></td>"+
                            "<td>"+data[this.identifier]+"</td>"+
                            "<td>"+data[this.description]+"</td>"+
                            "<td>"+data[this.partNumber]+"</td>"+
                            "<td>"+data[this.packageDetails]+"</td>"+
                            "<td class=\"right\">"+data[this.price]+"</td>"+
                            "<td class=\"right\"><input type=\"text\" maxlength= \"17\" class=\"finalPrice\" name=\"orderItemList["+count+"].finalPrice\" id=\"orderItemList["+count+"].finalPrice\" value=\""+data[this.price]+"\"/></td>"+
                            "<td class=\"right\"><input type=\"text\" maxlength= \"17\" class=\"qty\" name=\"orderItemList["+count+"].qty\" id=\"orderItemList["+count+"].qty\" value=\"1\"/></td>"+
                            "<td class=\"amount right\">"+data[this.price]+"</td></tr>")
            $(".qty",row).ForceNumericOnly(false)
            $(".finalPrice",row).ForceNumericOnly(true)

            return row

        }
    }

    var recalc = function(tr, formula){
       $("td.amount", tr).calc(formula, {qty : $("td input.qty", tr),
            finalPrice: $("td input.finalPrice",tr), discount : $("#discountRate")},
            function(s){
                return formatString(s)
            })
    }
    
    var recalcPopupAmount = function(){
        var qty = $("#pQty").val() 
        var price = $("#pFinalPrice").val().replace(/[^\d\.\-\ ]/g, '')
        var amount = qty * price
        document.getElementById('pAmount').innerHTML = formatString(amount)
    }

    var DiscountHandler = function (args) {
        var _currentDiscountItem = {}
        var _ctx = this

        var _updateDiscount = function(){
            var discountRateUpdate = false
            var netDiscountRateUpdate = false
            $.each(_ctx.discounts,function (idx, item){
              if(item.discountTypeId == _ctx.discountTypeNode.val()){
                _currentDiscountItem = item
                if (item.type == "Discount") {
	                $("#discountRate").val(1-(item.rate / 100))
	                $(".discountRate").val(item.rateFormat)
	                $("#discountGroup\\.id").val(item.discountGroupId)
                    $("#discount").val(item.rate)
	                discountRateUpdate= true
                } else if (item.type == "Net") {
                	$("#netDiscountRate").val(1-(item.rate / 100))
                	$(".netDiscountRate").val(item.rateFormat)
                	$("#netDiscountGroup\\.id").val(item.discountGroupId)
                	$("#netDiscount").val(item.rate)
                	netDiscountRateUpdate= true
                }
              }
            })
            if(!discountRateUpdate){
                $("#discountRate").val("")
                $(".discountRate").val("")
                $("#discountGroup\\.id").val("")
                $("#discount").val("")
            }
            if(!netDiscountRateUpdate){
                $("#netDiscountRate").val("")
                $(".netDiscountRate").val("")
                $("#netDiscountGroup\\.id").val("")
                $("#netDiscount").val("")
            }
        }

        this.discounts = {}
        this.discountGroupNode = args.discountGroupNode
        this.discountTypeNode = args.discountTypeNode
        this.customerNode = args.customerNode
        this.updateDiscount = _updateDiscount
        this.getDiscountItem = function () {
            return _currentDiscountItem
        }

        $.ajax({
            url : "retrieveCustomerDiscounts",
            data: "customer="+_ctx.customerNode.val(),
            async: false,
            type: "POST",
            success: function(resp){
              _ctx.discounts = resp
            }
        })
        _updateDiscount()
    }
    var computeTotal = function (table, isNet) {
       var total = $("tbody tr:not('.removed') td.amount", table).sum()
       if(isNet){
            $(".netItemsTotal strong").text(formatString(total))
            $(".netTotal strong").text(formatString(total))

            $(".netDiscountedTotal strong", table).calc("undiscountedTotal * rate",
                {
                    undiscountedTotal : total,
                    rate : $("#netDiscountRate").val()
                },
                function(s){
                    return formatString(s)
                }
            )

            $(".netDiscountRate strong").text($(".netDiscountRate").val())

            var rate = $("#netDiscountRate").val()

            if(rate){
              $(".netItemsTotal strong").text($(".netDiscountedTotal strong").text())
            } else {
              $(".netDiscountedTotal strong").text($(".netTotal strong").text())
              $(".netItemsTotal strong").text($(".netTotal strong").text())
            }

       }else{
            $(".unDiscountedTotal strong").text(formatString(total))

            $(".discountedTotal strong", table).calc("undiscountedTotal * rate",
                {
                    undiscountedTotal : total,
                    rate : $("#discountRate").val()
                },
                function(s){
                    return formatString(s)
                }
            )

            $(".discountRate strong").text( $(".discountRate").val())

            var rate = $("#discountRate").val()

            if(rate){
              $(".discountedItemsTotal strong").text($(".discountedTotal strong").text())
            }else{
              $(".discountedTotal strong").text($(".unDiscountedTotal strong").text())
              $(".discountedItemsTotal strong").text($(".unDiscountedTotal strong").text())
            }

        }
        $(".grandTotal strong").calc("netTotal + discountedTotal",
            { netTotal : $(".netItemsTotal strong")
           , discountedTotal : $(".discountedTotal strong")},
           function(s){
            return formatString(s)
           }
        )

    }
    var showPopup = function(data) {
            var initialPrice = data[itemsTableConfig.price]
            var IdDesc = data[itemsTableConfig.identifier] + " - " + data[itemsTableConfig.description]
            document.getElementById('pIdentifierDesc').innerHTML = IdDesc
            $("#pQty").ForceNumericOnly(false)
            $("#pFinalPrice").ForceNumericOnly(true)
            $("#pQty").val(1)
            $("#popupProductId").val(data[itemsTableConfig.id])

            $("#pFinalPrice").val(initialPrice)
            document.getElementById('pAmount').innerHTML = initialPrice
            $('#popupHistory').hide()
            $('#popupHideHistory').hide()
            $('#popupShowHistory').show()
            $.blockUI({ message: $('#popupFinalPriceQty'), css: { width: '900px', top:'20%',left:'20%',cursor: null} })
        }

      	var setCustomerValue = function(setValue, toBeSet){
            $(toBeSet).val($(setValue).val())
        }

      var setCustomerRemainingCreditLimit = function() {
        $.ajax({ url: "retrieveCustomerRemainingCredit",
			data: "customerId="+$(".customerId").val(),
	        success: function(resp) {
                if(resp.isNegative){
                    $("#remainingCredit").addClass("redText")
                }else{
                    $("#remainingCredit").removeClass("redText")
                }
				$("#remainingCredit").text(addCommas(resp.remainingCredit))
	       	}
	    });
      }

      var setSalesOrderPriceHistory = function(){
         $.ajax({ url: "retrieveProductHistory",
			data: "productId="+$("#popupProductId").val()+"&customerId="+$(".customerId").val(),
	        success: function(resp) {
	            var ctr=0
	            for(ctr=0;ctr<3;ctr++){
	                document.getElementById('productAmount'+ctr).innerHTML = ""
                    document.getElementById('salesDeliveryCreatedDate'+ctr).innerHTML = ""
                    document.getElementById('salesDeliveryId'+ctr).innerHTML = ""
                    document.getElementById('discountRate'+ctr).innerHTML = ""
                    document.getElementById('discountedPrice'+ctr).innerHTML = ""
	            }
	            ctr=0
	            $.each(resp, function(index,value){
                    document.getElementById('productAmount'+ctr).innerHTML = formatString(value.productAmount)
                    document.getElementById('salesDeliveryId'+ctr).innerHTML = value.salesDeliveryId
                    document.getElementById('salesDeliveryCreatedDate'+ctr).innerHTML = value.dateApproved
                    document.getElementById('discountRate'+ctr).innerHTML = value.discountRate
                    document.getElementById('discountedPrice'+ctr).innerHTML = formatString(value.discountedPrice)
                    ctr++
                });
	       	}
	    });

      }
      var resetSelectedTables = function(){
        var discountedTable = $("#discountedItems")
        var netTable = $("#netItems")
        $("td strong.number").text(0)
        $("tbody tr",discountedTable).empty()
        $("tbody tr",netTable).empty()
      }
    $(document).ready(function () {

        var discountedHandler = new DiscountHandler({
            discountTypeNode : $(".discountType"), discountGroupNode : $(".discountGroup"), customerNode : $(".customerName")})

        var discountedTable = $("#discountedItems")
        var netTable = $("#netItems")
        var columnsSettings = new Array()

        columnsSettings.push({"bSearchable": false, "bVisible": false })

        for(var i=0;i<${warehouseList.size()+6};i++){
           columnsSettings.push("null")
        }


      $(".priceType").change(function (){
           searchProductsTable.fnDraw()
      })
      $(".discountType").change(function () {
          discountedHandler.updateDiscount()
          resetSelectedTables()
          searchProductsTable.fnDraw()
      })
      $(".customerId").change(function () {
          setCustomerValue($(".customerId"),$(".customerName"))
          setCustomerRemainingCreditLimit()
          discountedHandler = new DiscountHandler({
              discountTypeNode : $(".discountType"), discountGroupNode : $(".discountGroup"), customerNode : $(this)})
          resetSelectedTables()
      })
      $(".customerName").change(function () {
          setCustomerValue($(".customerName"),$(".customerId"))
          setCustomerRemainingCreditLimit()
          discountedHandler = new DiscountHandler({
              discountTypeNode : $(".discountType"), discountGroupNode : $(".discountGroup"), customerNode : $(this)})
          resetSelectedTables()
      })


    })

	function fileSelected(){
		$("#processingFile").show();
		document.uploadForm.submit();		
	}

	function updateUploadedItems(iframe){
		$("#processingFile").hide();
	}
	
	function doCreate(){
		if(document.getElementById("fileUpload").value==""){
			alert("File has not been specified. Please select the file containing the Sales Order Form.");
		}else{
			document.uploadForm.target="";
			document.uploadForm.action="uploadSave";
			document.uploadForm.submit();
		}		
	}

	</script>

  </head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create">Create</g:link></span>
    <span class="menuButton"><g:link class="create" action="upload">Create (from SO Form)</g:link></span>
    <span class="menuButton"><g:link class="create" action="excelForm">Download SO Form</g:link></span>
  </div>
	<g:form name="uploadForm" action="uploadFile" method="post" enctype="multipart/form-data" target="uploadFrame">
   <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /> (from Sales Order Form)</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${salesOrderInstance}">
      <div class="errors">
        <g:renderErrors bean="${salesOrderInstance}" as="list" />
      </div>
    </g:hasErrors>

      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="deliveryDate"><g:message code="salesOrder.deliveryDate.label" default="Delivery Date" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: salesOrderInstance, field: 'deliveryDate', 'errors')}">
          <g:datePicker name="deliveryDate" precision="day" value="${salesOrderInstance?.deliveryDate}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="customer"><g:message code="salesOrder.customer.label" default="Customer" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderInstance, field: 'customer', 'errors')}">
            <g:set var="customersSortedByIdentifier" value="${customerList.sort{it.identifier}}" />
            <g:select name="customer.id" class="customerId" from="${customersSortedByIdentifier}" optionKey="id" optionValue="identifier" value="${salesOrderInstance?.customer?.id}"  noSelection="['':'select one..']"/>
            <g:set var="customersSortedByName" value="${customerList.sort{it.name}}" />
            <g:select name="customerName" class="customerName" from="${customersSortedByName}" optionKey="id" optionValue="name" value="${salesOrderInstance?.customer?.id}"  noSelection="['':'select one..']"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="customer"><g:message code="salesOrder.customer.remainingCredit.label" default="Customer Remaining Credit" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderInstance, field: 'customer', 'errors')}">
              <span id=remainingCredit><g:formatNumber number="${salesOrderInstance?.customer?.remainingCredit}" format="#,##0.00"/></span>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="discountType"><g:message code="salesOrder.discountType.label" default="Discount Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderInstance, field: 'discountType', 'errors')}">
            <g:select name="discountType.id" class="discountType" from="${com.munix.DiscountType.list().sort{it.id}}" optionKey="id" value="${salesOrderInstance?.discountType?.id}" />
            <g:hiddenField name="discount" value="" id="discount"/>
            <g:hiddenField name="netDiscount" value="" id="netDiscount"/>
            <g:hiddenField class="discountGroup" name="discountGroup.id" value="" />
            <g:hiddenField id="discountRate" name="discountRate" value="" />
            <g:hiddenField class="netDiscountGroup" name="netDiscountGroup.id" value="" />
            <g:hiddenField id="netDiscountRate" name="netDiscountRate" value="" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="salesOrder"><g:message code="salesOrder.priceType.label" default="Price Type" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderInstance, field: 'priceType', 'errors')}">
          	<g:select name="priceType" class="priceType" from="${PriceType.values()}" value="${PriceType.getTypeByName('wholesale')}"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="salesOrder.remark.label" default="Remarks" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderInstance, field: 'remark', 'errors')}">
            <g:textArea name="remark" maxlength="255" value="${salesOrderInstance?.remark}" />
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="remark"><g:message code="salesOrder.form.label" default="Upload Sales Order Form" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderInstance, field: 'form', 'errors')}">
            <input id="fileUpload" type="file" name="file" onchange="fileSelected()"><span style="display:none" class="processing" id="processingFile">Processing File...</span>
          </td>
          </tr>
          </tbody>
        </table>
      </div>
      <iframe name="uploadFrame" src="about:blank" style="display:none" onload="updateUploadedItems(this)"></iframe>
      <div id="uploadedItems">
      </div>
      <div class="buttons">
        <input type="hidden" id="counter" name="counter" value="0"/>
        <span class="button"><input type="button" name="create" value="${message(code: 'default.button.create.label', default: 'Create')}" onclick="doCreate()"/></span>
      </div>

    </g:form>

</body>
</html>
