<%@ page import="com.munix.PriceType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />

  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <link rel="stylesheet" href="${resource(dir:'js/table/css',file:'demo_table.css')}" />
  <g:javascript src="generalmethods.js" />
  <g:javascript src="jquery.calculation.js" />
  <g:javascript src="table/jquery.dataTables.js" />
  <g:javascript src="jquery.blockUI.js" />

  <g:javascript>
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
        var searchProductsTable = $("#searchProductsTable").dataTable({
            "bProcessing": true,
            "bServerSide": true,
            "bJQueryUI": true,
            "bPaginate": false,
            "bScrollCollapse": true,
            "sScrollYInner": "150%",
            "sScrollY": "200px",
            "bFilter": false,
            "sAjaxSource": "retrieveProductsForSale",
            "fnServerData": function ( sSource, aoData, fnCallback ) {
                aoData.push( { "name": "discountType", "value": $(".discountType").val() } )
                aoData.push( { "name": "priceType", "value": $(".priceType").val() } )
                aoData.push( { "name": "identifier", "value": $("#searchIdentifier").val() } )
                aoData.push( { "name": "category", "value": $("#searchCategory").val() } )
                aoData.push( { "name": "subcategory", "value": $("#searchSubcategory").val() } )
                aoData.push( { "name": "brand", "value": $("#searchBrand").val() } )
                aoData.push( { "name": "model", "value": $("#searchModel").val() } )
                aoData.push( { "name": "size", "value": $("#searchSize").val() } )
                aoData.push( { "name": "color", "value": $("#searchColor").val() } )
                $.ajax( {
                    "dataType": 'json',
                    "type": "POST",
                    "url": sSource,
                    "data": aoData,
                    "success": fnCallback
                } );
    		 },
            "aoColumns":columnsSettings,
            "fnDrawCallback" :function(){

                $("tbody tr", searchProductsTable).click(function () {
                       var data = searchProductsTable.fnGetData(this)
                       var isAdded = function(table){
                           var added = false
                           $.each($("tbody tr:not('.removed') .productId", table),function (idx, value) {
                               if($(this).val() == data[itemsTableConfig.id]){
                                   added = true
                                   return
                               }
                           })
                           return added
                       }

                       var getTable = function(){
                            if(data[itemsTableConfig.netPrice] == "true"){
                                return netTable
                            }else{
                                return discountedTable
                            }
                       }

                       var getNetTable = function(){
                       		return netTable
                       }

                       var getDiscountTable = function(){
                       		return discountedTable
                       }

                       if(!isAdded(getNetTable()) && !isAdded(getDiscountTable())){
                       	   var rowCount = $(".soItems tbody tr .remove").size()
                           var row = itemsTableConfig.createRow(data,rowCount)

                           $("tbody", getTable()).append(row)
                           $("img.remove",row).click(function (){
                                $("input:hidden.deleted",row).val(true)
                                $(row).addClass("removed")
                                $(row).hide()
                                computeTotal(getTable(), data[itemsTableConfig.netPrice] == "true")
                           })
                           computeTotal(getTable(), data[itemsTableConfig.netPrice] == "true")
                           $.each($("tbody tr:not('.removed')", getTable()), function(idx, tr) {
                                $("td input:text",tr).change(function (){
                                   recalc(tr,"qty * finalPrice")
                                   computeTotal(getTable(), data[itemsTableConfig.netPrice] == "true")
                                })
                           })

                           showPopup(data)

                       }else{
                           alert("Item: "+data[itemsTableConfig.identifier]+" already added!")
                       }
                })
            }
        })//end of data table


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
          searchProductsTable.fnDraw()
      })
      $(".customerName").change(function () {
          setCustomerValue($(".customerName"),$(".customerId"))
          setCustomerRemainingCreditLimit()
          discountedHandler = new DiscountHandler({
              discountTypeNode : $(".discountType"), discountGroupNode : $(".discountGroup"), customerNode : $(this)})
          resetSelectedTables()
          searchProductsTable.fnDraw()
      })

      $('#popupYes').click(function() {
       	   	var rowCount = $(".soItems tbody tr .remove").size()-1
       	   	var pFinalPrice = $("#pFinalPrice").val()
      		var pQty = $("#pQty").val()
      		$.unblockUI()
      		document.getElementById("orderItemList["+rowCount+"].finalPrice").value = pFinalPrice
      		document.getElementById("orderItemList["+rowCount+"].qty").value = pQty

      		var mockChangeFinalPrice = document.getElementById("orderItemList["+rowCount+"].finalPrice")
      		var changeEvent = document.createEvent("Event")
      		changeEvent.initEvent("change", true, false)
      		mockChangeFinalPrice.dispatchEvent(changeEvent)
            $("#productImage").remove()
      		$.unblockUI()
            return false
      })

      $('#popupShowHistory').click(function() {
       	   	$('#popupHistory').show()
       	   	$('#popupHideHistory').show()
       	   	$('#popupShowHistory').hide()
            setSalesOrderPriceHistory()
            var imageLink = "${createLink(uri:'/')}product/viewImage/"+$("#popupProductId").val()
            $("#popupHistory").append("<img style=\"float:left;padding:10px\" id=\"productImage\" src=\""+imageLink+"\" height=\"100\" width=\"150\"/>")
            return true
      })
      $('#popupHideHistory').click(function() {
       	   	$('#popupHistory').hide()
       	   	$('#popupHideHistory').hide()
       	   	$('#popupShowHistory').show()
       	   	$("#productImage").remove()
            return true
      })

      $('#popupCancel').click(function() {
       	   	var rowCount = $(".soItems tbody tr .remove").size()-1
       	   	var row = document.getElementById("orderItemList["+rowCount+"].finalPrice")
       	   	$(row).closest("tr").find("img.remove").click()
            $("#productImage").remove()
            $.unblockUI()
            return false
      })

      $('#pFinalPrice').bind('keypress', function(e) {
      		if(e.keyCode==13){
      			$("#popupYes").click()
      		}

      		if(e.keyCode==27){
      			$("#popupCancel").click()
      		}
      })

      $('#pQty').bind('keypress', function(e) {
      		if(e.keyCode==13){
      			$("#popupYes").click()
      		}

      		if(e.keyCode==27){
      			$("#popupCancel").click()
      		}
      });

      $("input[name^=search]").keypress(function(event) {
        if (event.keyCode == 13) {
            searchProductsTable.fnDraw()
            event.preventDefault();
        }
      });
      $("#searchButton").click(function(event) {
        searchProductsTable.fnDraw()
        event.preventDefault();
      });
      $("#clearButton").click(function(event) {
        $("#searchIdentifier").val("")
        $("#searchCategory").val("")
        $("#searchSubcategory").val("")
        $("#searchBrand").val("")
        $("#searchModel").val("")
        $("#searchSize").val("")
        $("#searchColor").val("")
        searchProductsTable.fnDraw()
        event.preventDefault();
      });
    })

  </g:javascript>


  </head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="upload">Upload</g:link></span>
    <span class="menuButton"><g:link class="create" action="excelForm">Download</g:link></span>
  </div>
 <g:form action="uploadFile" method="post" enctype="multipart/form-data">
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
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
          </tbody>
        </table>
      </div>

      <div class="subTable">
        <div style="clear:both" ></div>
           <table  id="discountedItems" class="soItems">
             <h2>Discounted Items</h2>
              <thead>
                  <tr>
                      <th>Identifier</th>
                      <th>Description</th>
                      <th>Part Number</th>
                      <th>Package Details</th>
                      <th width="15%">Price</th>
                      <th width="15%">Final Price</th>
                      <th width="15%">Quantity</th>
                      <th width="15%">Amount</th>
                  </tr>
              </thead>
              <tbody>
                  <g:each in="${items}" status="i" var="orderItem">
                    <g:if test="${!orderItem?.isNet}">
                      <tr>
                        <td class="identifier">${orderItem?.product?.identifier}</td>
                        <td class="identifier">${orderItem?.product?.partNumber}</td>
                        <td class="description">${orderItem?.product?.description}</td>
                        <td class="packageDetails">${orderItem?.product?.getPackageDetails()}</td>
                        <td class="right price">${orderItem?.formatPrice()}</td>
                        <td class="right price">${orderItem?.formatFinalPrice()}</td>
                        <td class="right price">${orderItem?.qty}</td>
                        <td class="right amount">${orderItem?.finalPrice * orderItem?.qty}</td>
                      </tr>
                    </g:if>
                  </g:each>
              </tbody>
              <tfoot>
                <tr class="total">
                    <td><strong>Total</strong></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="unDiscountedTotal right">
                        <strong>${salesOrderInstance?.formatDiscountedItemsTotal()}</strong>
                    </td>
                </tr>
                <tr class="total">
                    <td><strong>Discounted</strong></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="discountRate right">
                        <strong>${salesOrderInstance?.discountGroup?.formatRate()}</strong>
                    </td>
                    <td class="discountedTotal right">
                        <strong>${salesOrderInstance?.formatDiscountedTotal()}</strong>
                    </td>
                </tr>

            </table>
           </table>

        <div style="clear:both" ></div>
           <table id="netItems" class="soItems">
             <h2>Net Price Items</h2>
              <thead>
                  <tr>
                      <th>Identifier</th>
                      <th>Description</th>
                      <th>Part Number</th>
                      <th>Package Details</th>
                      <th width="15%">Price</th>
                      <th width="15%">Final Price</th>
                      <th width="15%">Quantity</th>
                      <th width="15%">Amount</th>
                  </tr>
              </thead>
              <tbody>
                  <g:each in="${salesOrderItems}" status="i" var="orderItem">
                    <g:if test="${orderItem?.isNet}">
                      <tr>
                        <td class="cancel existing">
                            <img src="../images/cancel.png" class="remove">
                            <input type="hidden" name="orderItemList[${orderItem.idx}].isDeleted" value="false" class="deleted" name="isDeleted">
							<input class="productId" value="${orderItem.productId}" type="hidden" name="orderItemList[${orderItem.idx}].product.id" id="orderItemList[${orderItem.idx}].product.id"/>
                        </td>
                        <td class="identifier">${orderItem?.product?.identifier}</td>
                        <td class="identifier">${orderItem?.product?.partNumber}</td>
                        <td class="description">${orderItem?.product?.description}</td>
                        <td class="packageDetails">${orderItem?.product?.getPackageDetails()}</td>
                        <td class="right price">${orderItem?.price}</td>
                        <td class="right price">${orderItem?.finalPrice}</td>
                        <td class="right price">${orderItem?.qty}</td>
                        <td class="right amount">${orderItem?.finalPrice * orderItem?.qty}</td>
                      </tr>
                    </g:if>
                  </g:each>
              </tbody>
              <tfoot>
                <tr class="total">
                    <td><strong>Total</strong></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="netTotal right">
                        <strong>${salesOrderInstance?.formatNetItemsTotal()}</strong>
                    </td>
                </tr>
                
                <tr class="total">
                    <td><strong>Discounted</strong></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="netDiscountRate right">
                        <strong>${salesOrderInstance?.netDiscountGroup?.formatRate()}</strong>
                    </td>
                    <td class="netDiscountedTotal right">
                        <strong>${salesOrderInstance?.formatNetTotal()}</strong>
                    </td>
                </tr>
              </tfoot>
            </table>
      </div>

      <div class="buttons">
        <input type="hidden" id="counter" name="counter" value="0"/>
        <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
      </div>


    </g:form>

</body>
</html>
