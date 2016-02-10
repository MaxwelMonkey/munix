package com.munix

class StockCostHistoryController {
	def stockCostHistoryService
	
    def show = {
		def product = Product.get(params.id)
		if (product) {
			def stockCostItems = stockCostHistoryService.generateStockCostItems(product)
			[stockCostItems: stockCostItems, product: product]
		} else {
			flash.message = "No product selected for stock cost history"
			redirect(action: "list", controller: "product")
		}
	}
}
