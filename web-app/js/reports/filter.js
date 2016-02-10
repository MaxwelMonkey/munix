	function checkAll(type, check){
		$("."+type+"Checkbox").each(function(){
			this.checked = check;
		});
	}  	
