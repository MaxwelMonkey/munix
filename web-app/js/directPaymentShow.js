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

function formatString(text){
	text+=""
	text=addCommas(text)
	return "PHP "+text
}

