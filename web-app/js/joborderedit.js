var $=jQuery.noConflict()
var availConfig = {
    cols : [
        {identifier : "identifier"},
        {identifier : "desc"}
    ]
}

var compConfig = {
    cols : [
        {identifier : "identifier", link : base_url+"product/show/"},
        {identifier : "desc"},
        {identifier : "qtyNeeded", editable : true},
    ]
}

var checkOddEven = function(num){
    if(num % 2 == 0){
        return "even"
    }else{
        return "odd"
    }
}

var renderBody = function(data, table){
        $.each(data, function(rowIdx, item) {
            var oddEven = checkOddEven(rowIdx)
            var row = $(createRow(item, availConfig)).click(
                function(){
                    if(!verifyItemToAdd(item.identifier)){
                        item["added"] = true
                        var compRow = $(createRow(item, compConfig))
                        $("input:text",compRow).ForceNumericOnly(false)
                        $(compRow).append(addRemoveHandler(item, this))
                        $(this).toggle()
                        $("#components tbody").append(compRow)
                    }else{
                        alert("Cannot add the same component!")
                    }
                }
            )

            $(row).addClass(oddEven)
            $(table).append(row)
        })
    }

var addRemoveHandler = function(item, row){
      var td = $("<td class=\"right\"><img src=\""+base_url+"images/cancel.png\" /></td>")
      $(td).click(function () {
           $(row).toggle()
//                   item["added"] = false
           $(this).parent().remove()
      })
      return td
}

var removeItem = function(item, id){
     $(item).parent().hide()
     $(".isDeleted",$(item).parent()).val(true)
}

var createRow = function(item, config) {
        var td = ""
        $.each(config.cols, function(colIdx, col){
            var itemData = item[col.identifier]
            var content = itemData
            if(col.link){
                td += "<td><a href=\""+col.link+item.id+"\">"+content+"</a></td>"
            }else if(col.editable){
                var count = $("#components tbody tr").size()
                td += "<td class=\"right\"><input type=\"text\" id=\"materialList["+count+
                            "].qty\" name=\"materialList["+count+"].qty\"value=\""+item.qtyNeeded+"\"/>"+
                            "<input type=\"hidden\" id=\"materialList["+count+"].productComponent\""+
                            " name=\"materialList["+count+"].productComponent\" value=\""
                            +item.id+"\"/></td>"
            }else{
	            td += "<td>"+content+"</td>"
            }
        })
        var tr = "<tr>"+td+"</tr>"
        return tr			
    }

var queryProduct = function(query,qty,productId){
    $.ajax({ url: base_url+"jobOrder/retrieveComponents",
            data: "query="+query+"&qty="+qty+"&productId="+productId,
            type: "POST",
            success: function(resp){
                $("#products").empty()
                renderBody(resp, $("#products"))
            }
	})        
}

var verifyItemToAdd = function(code){
    var added = false
    $(".components tr a").each(function(idx, elem){

        if(code == $(elem).text()){
            added = true
            return
        }
    })
    return added
}

