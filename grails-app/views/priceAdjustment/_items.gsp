<%@ page import="com.munix.Product; com.munix.PriceType"%>
<link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
<g:javascript src="table/jquery.dataTables.js" />
<g:javascript src="table/jquery.dataTables.forceReload.js" />
<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
<g:javascript src="numbervalidation.js" />

<script type="text/javascript">
	var LAZY_LIST = {
		id: 0,
        identifier: 1,
        description: 2,
        isNet: 3,
        wholesalePrice: 5,
        retailPrice: 6,
        createRow: function(data, count) {
	        var row = $(
	    	    "<tr>" +
	    	      "<td><img src=\"../images/cancel.png\" class=\"remove\"/></td>" +
		          "<td>" + data[this.identifier] + "</td>" +
		          "<td>" + data[this.description] + "</td>" +
		          "<td>" + data[this.isNet] + "</td>" +
		          "<td class=\"wholesale\">" + data[this.wholesalePrice] + "</td>" +
		          "<td class=\"retail\">" + data[this.retailPrice] + "</td>" +
		          "<td><input type=\"text\" class=\"newPrice\" maxLength=\"17\" name=\"itemList[" + count + "].newPrice\" id=\"itemList[" + count + "].newPrice\" value=\"\"/></td>" +
		          "<input type=\"hidden\" class=\"productId\" name='itemList[" + count + "].product.id' value='" + data[this.id] + "'/>" +
		          "<input type=\"hidden\" class=\"oldPricewholesale\" name='itemList[" + count + "].oldPrice' value='" + data[this.wholesalePrice] + "'/>" +
		          "<input type=\"hidden\" class=\"oldPriceretail\" name='itemList[" + count + "].oldPrice' value='" + data[this.retailPrice] + "'/>" +
		          "<input type=\"hidden\" class=\"deleted\" name=\"itemList["+ count +"].isDeleted\" value=\"false\">" +
		        "</tr>")
            $(".newPrice", row).ForceNumericOnly(true)
	        return row
	    },
	    initSelectedProducts: function() {
	    	$(".newPrice").ForceNumericOnly(true)
	        $.each($("tbody tr", "#selectedProducts"), function(idx, tr) {
				$("#cancelExisting", this).click(function() {
	                $("input:hidden.deleted", this).val(true)
	                $(tr).addClass("removed")
	                $(tr).hide()
				})
		    })
	    },
	    showSelectedPriceType: function() {
		    this.hidePrices()
		    this.disableOldPrices()
		    var selectedPriceType = $('#priceType').val()
		    $("." + selectedPriceType.toLowerCase()).show()
		    $(".oldPrice" + selectedPriceType.toLowerCase()).removeAttr("disabled")
		},
		hidePrices: function() {
		    $(".wholesale").hide()
		    $(".retail").hide()
		},
        disableOldPrices: function() {
		    $(".oldPriceWholesale").attr("disabled","disabled")
		    $(".oldPriceRetail").attr("disabled","disabled")
		}
	}

	$(document).ready(function () {
         var columnsSettings = new Array()

         columnsSettings.push({"bSearchable": false, "bVisible": false }, {"bSearchable": true}, {"bSearchable": true}, null, null, {"bVisible": false}, {"bVisible": false})
         for(var i=0; i<${warehouses.size()}; i++){
            columnsSettings.push("null")
         }
          
         var searchProductsTable = $("#searchProductsTable").dataTable({
            "bJQueryUI": true,
            "bServerSide": true,
            "bPaginate": false,
            "bScrollCollapse": true,
            "sScrollYInner": "150%",
            "sScrollY": "200px",
            "sAjaxSource": "retrieveProductsForSale",
            "aoColumns": columnsSettings,
            "fnServerData": function ( sSource, aoData, fnCallback ) {
                aoData.push( { "name": "itemType", "value": $("#itemType").val() } )
                aoData.push( { "name": "priceType", "value": $("#priceType").val() } )
                $.ajax( {
                    "dataType": 'json',
                    "type": "POST",
                    "url": sSource,
                    "data": aoData,
                    "success": fnCallback
                } );
    		 },

            "fnDrawCallback" :function(){
                $("tbody tr", searchProductsTable).click(function () {
                   var data = searchProductsTable.fnGetData(this)
                   var isAdded = function(table){
                       var added = false
                       $.each($("tbody tr:not('.removed') .productId", table),function (idx, value) {
                           if($(this).val() == data[LAZY_LIST.id]){
                               added = true
                           }
                       })
                       return added
                   }

                   if(!isAdded($('#selectedProducts'))){
                       var row = LAZY_LIST.createRow(data, $("#selectedProducts tbody tr .remove").size())
                       $("tbody", $('#selectedProducts')).append(row)
                       LAZY_LIST.showSelectedPriceType()
                       $("img.remove", row).click(function () {
                            $("input:hidden.deleted", row).val(true)
                            $(row).addClass("removed")
                            $(row).hide()
                       })
                   } else {
                       alert("Item: "+data[LAZY_LIST.identifier]+" already added!")
                   }
                })

            }
        }).fnSetFilteringDelay()//end of data table

        LAZY_LIST.initSelectedProducts()
        LAZY_LIST.hidePrices()
        LAZY_LIST.showSelectedPriceType()
        $("#priceType").change(function() {
			LAZY_LIST.showSelectedPriceType()
			searchProductsTable.fnReloadAjax()
        })
        $("#itemType").change(function (){
            $("tbody tr",$('#selectedProducts')).empty()
            searchProductsTable.fnReloadAjax()
		})
     })

</script>
 
<div class="list">
    <table id="searchProductsTable">
        <thead>
          <th>Id</th>
          <th width="5%">Identifier</th>
          <th width="25%">Description</th>
          <th width="10px">Net</th>
          <th width="10px">Current Price</th>
          <g:each in="${warehouses}" var="warehouse">
            <th width="10px">${warehouse.identifier}</th>
          </g:each>
        </thead>
        <tbody class="salesOrderItems">
        </tbody>
    </table>
</div>
<div class="subTable">
  <table id="selectedProducts">
  <thead>
    <th width="3%">&nbsp</th>
    <th>Identifier</th>
    <th>Description</th>
    <th width="15%">Net</th>
    <th width="15%" id="wholesalePriceHeader" class="wholesale">Wholesale Price</th>
    <th width="15%" id="retailPriceHeader" class="retail">Retail Price</th>
    <th width="15%">New Price</th>
  </thead>
  <tbody>
    <g:each in="${priceAdjustmentInstance.items}" status="i" var="item">
        <tr>
          <td id="cancelExisting">
          	<img src="../images/cancel.png" class="remove">
          	<g:hiddenField class="deleted" name="itemList[${i}].isDeleted" value="false" />
          </td>
          <td>${item?.product?.identifier}</td>
          <td>${item?.product?.description}</td>
          <td>${item?.product?.isNet}</td>
          <td class="wholesale">${item?.product?.getProductPrice(PriceType.WHOLESALE)}</td>
		  <td class="retail">${item?.product?.getProductPrice(PriceType.RETAIL)}</td>
		  <td class="newPrice"><g:textField name="itemList[${i}].newPrice" value="${item?.newPrice}" /></td>
		  <g:hiddenField class="productId" name="itemList[${i}].product.id" value="${item.product.id}" />
		  <g:hiddenField class="oldPricewholesale" name="itemList[${i}].oldPrice" value="${item.product.wholeSalePrice}"/>
		  <g:hiddenField class="oldPriceretail" name="itemList[${i}].oldPrice" value="${item.product.retailPrice}"/>
        </tr>
    </g:each>
  </tbody>
</table>
</div>