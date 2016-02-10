package com.munix


class PriceAdjustmentJob {
	static triggers = {
		simple name: 'mySimpleTrigger', startDelay: 30000, repeatInterval: 10000, repeatCount: 0
		cron name: 'myTrigger', cronExpression: "0 0 8 * * ?"
	}
	def priceAdjustmentUpdateService
	
	def execute() {
		priceAdjustmentUpdateService.updatePrices()
	}
}
