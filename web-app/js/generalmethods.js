function addCommasToInteger(nStr){
	var number = parseInt(nStr)
	number += '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(number)) {
		number = number.replace(rgx, '$1' + ',' + '$2');
	}
	return number;
}

function addCommas(nStr){
	var decimal=parseFloat(nStr).toFixed(2)
	//decimal=Math.round((decimal)*100)/100
	decimal += '';
	var x = decimal.split('.');
	var x1 = x[0];
	var x2 = x.length > 1 ? "." + x[1] : '.00';
	x2= x2.length == 2 ? x2 + "0" : x2;
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}

	return x1 + x2;
}

function replaceAll(Source,stringToFind,stringToReplace){
  var temp = Source;
  var index = temp.indexOf(stringToFind);
     while(index != -1){
         temp = temp.replace(stringToFind,stringToReplace);
         index = temp.indexOf(stringToFind);
     }
     return temp;
}

function changeToFloat(text){
	text=text.replace("PHP","")
	text=replaceAll(text,",","")
	return parseFloat(text)
}

function addAmount(total,text){
	return total+=changeToFloat(text)
}
function changeToString(text){
    text=text.replace("PHP","")
	text=replaceAll(text,",","")
    return text
}
function formatString(text){
	text+=""
	text=addCommas(text)
	return "PHP "+text
}

jQuery.fn.ForceNumericOnly =
	function(isDecimal){
	    return this.each(function()
	    {
	    	
	        $(this).keydown(function(e)
	        {
	        	
	            var key = e.charCode || e.keyCode || 0;
	            if(!e.shiftKey){
		            if (isDecimal) {
		            	if ($(this).val().indexOf(".") >= 0 && (key == 190 || key==110)) {
		            		return false;
		            	}
	
		            	if ((key == 190) || (key==110)){
		            		return true;
		            	}
		            }
	    	        return (
		            	key == 8 || 
		                key == 9 ||
		                key == 46 ||
		                (key >= 37 && key <= 40) ||
		                (key >= 48 && key <= 57) ||
		                (key >= 96 && key <= 105));
	            }else{
		        	return false
		        }
	        })
	        
	    })
	    
	};
    jQuery.fn.ForceNumericOnlyEnterAllowed =
	function(){
	    return this.each(function()
	    {
	        $(this).keydown(function(e)
	        {
	            var key = e.charCode || e.keyCode || 0;
	            if(!e.shiftKey){
	    	        return (
		            	key == 8 ||
		                key == 9 ||
		                key == 13 ||
		                key == 46 ||
		                (key >= 37 && key <= 40) ||
		                (key >= 48 && key <= 57) ||
		                (key >= 96 && key <= 105));
	            }else{
		        	return false
		        }
	        })

	    })

	};
