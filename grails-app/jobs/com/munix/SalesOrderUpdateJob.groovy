package com.munix


class SalesOrderUpdateJob {
    
	def cronExpression = "0 0 0 ? * MON-SUN"
		
	def salesOrderService 

	def execute() {
		println "SALES ORDER UPDATE JOB"
		salesOrderService.autocancelSalesOrders()
		println "Autocancel done."
		salesOrderService.autocompleteSalesOrders()
		println "Autocomplete done."
	}
}
