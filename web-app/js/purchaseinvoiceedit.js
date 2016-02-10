var $ = jQuery.noConflict()
var invoiceConfig = {
    cols : [
        {identifier : "product"},
        {identifier : "qty"},
        {identifier : "poPrice"},
        {identifier : "poFinalPrice"},
        {identifier : "qtyReceived", editable : true},
        {identifier : "finalPrice", editable : true}
    ]
}

var createRow = function(item, config) {
        var td = ""
        var count = $(".invoiceItems tbody tr").size()
        $.each(config.cols, function(colIdx, col){
            var itemData = item[col.identifier]
            var content = itemData
            if(col.link){
                td += "<td><a href=\""+col.link+item.id+"\">"+content+"</a></td>"
            }else if(col.identifier == "qtyReceived"){
                td += "<td class=\"right\"><input maxlength=\"16\" width=\"20%\" class=\"qty\" type=\"text\" id=\"received"+item.id+
                            "\" name=\"invoiceItemList["+count+"].qty\" value=\"0\"/><input class=\"deleted\" type=\"hidden\""+
                            " id=\"itemId\" name=\"invoiceItemList["+count+"].isDeleted\" value=\"false"
                            +"\"/><input type=\"hidden\""+
                            " id=\"itemId\" name=\"invoiceItemList["+count+"].poItem.id\" value=\""+item.id
                            +"\"/><input type=\"hidden\""+
                            " id=\"itemId\" name=\"invoiceItemList["+count+"].product.id\" value=\""+item.productId
                            +"\"/></td>"
            }else if(col.identifier == "finalPrice"){
	            td += "<td class=\"right\"><input maxlength=\"16\" width=\"20%\" class=\"finalPrice\" type=\"text\" id=\"finalPrice"+item.id+
                            "\" name=\"invoiceItemList["+count+"].finalPrice\" value=\""+item.finalPrice+"\"/></td>"
            }else if(col.identifier == "remaining" || col.identifier == "qty" || col.identifier == "poPrice" || col.identifier == "poFinalPrice"){
	            td += "<td class=\"right\">"+content+"</td>"
            }else {
	            td += "<td >"+content+"</td>"
            }
        })
        var tr = "<tr>"+td+"</tr>"
        return tr			
    }
var createRemoveCol = function(item, row){
      var td = $("<td><img src=\""+base_url+"images/cancel.png\" /></td>")
      $(td).click(function () {
           $(row).toggle(true)
           $(this).parent().hide()
           $(".deleted",$(td).parent()).val(true)
      })
      return td
}

var removeItemHandler = function(pOrow){
       $(pOrow).toggle(true)
       //addItemHandler(row)
}

var addItemHandler = function(row){
        var item = {
            "product" : $(".product",row).val() +"-"+$(".desc",row).val(),
            "qty" : $(".qty",row).val(),
            "remaining" : $(".remaining",row).val(),
            "productId" : $(".productId",row).val(),
            "id" : $(".poItem",row).val(),
            "poPrice" : $(".poItemCurrency",row).val()+" "+$(".poItemPrice",row).val(),
            "poFinalPrice" : $(".poItemCurrency",row).val()+" "+$(".poItemFinalPrice",row).val(),
            "finalPrice" : $(".poItemPrice",row).val()
        }
        var tr = $(createRow(item,invoiceConfig))
        $(tr).append(createRemoveCol(item, row))
        $(".qty",tr).ForceNumericOnly(false)
        $(".finalPrice",tr).ForceNumericOnly(true)
        $("#piItems").append(tr)
}

var addInvoiceItem = function(){
    $(".poTables tbody tr").click(function() {
        var row = $(this)
        addItemHandler(row)
        $(row).toggle(false)
    })
}

var validateNumber = function(input){
    input.value = input.value.replace(/[^0-9]/g,'');
}

var removeFromInvoice = function(item, id){
    $(item).children(".deleted").val(true)
    $(item).parent().hide()
    $(id).show()
    removeItemHandler($(id))
}
