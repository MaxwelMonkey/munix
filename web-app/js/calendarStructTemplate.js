var setCalendarStruct = function(dateStruct, dateValue){
    if(dateStruct.val() == "" && dateValue.val() != "") {
    	dateStruct.val("struct")
    }
}