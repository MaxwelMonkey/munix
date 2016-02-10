

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
