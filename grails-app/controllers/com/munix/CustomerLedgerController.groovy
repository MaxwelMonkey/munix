package com.munix

class CustomerLedgerController {
	def customerLedgerService

	def show = {
		def customerLedgerInstance = CustomerLedger.get(params.id)
		if (customerLedgerInstance) {
			return customerLedgerService.getCustomerLedgerEntries(customerLedgerInstance, params)
		}
		else {
			flash.message = "customerLedger.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "CustomerLedger not found with id ${params.id}"
			redirect(action: "list")
		}
	}
}
