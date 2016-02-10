 function updateRemaining() {
	var remaining = parseInt($("#remaining").val())
	var inputtedQty = $("#qty").val()
	if (inputtedQty == '') {
		inputtedQty = '0'
	} 
	var qty = parseInt(inputtedQty)
	
	var updatedBalance = remaining - qty
	if (updatedBalance < 0) {
		updatedBalance = 0
	}
	$("#remainingText").text(addCommasToInteger(updatedBalance))
}

$(document).ready( function() {
	$("#qty").ForceNumericOnlyEnterAllowed(true)
	$("#qty").change(function() {
		updateRemaining()
	})
	updateRemaining()
})