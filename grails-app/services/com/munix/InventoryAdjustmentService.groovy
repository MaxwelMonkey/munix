package com.munix

class InventoryAdjustmentService {

    static transactional = true

    def generateInventoryAdjustmentItems(InventoryAdjustment inventoryAdjustmentInstance) {
        def items = []
		def isApproved = InventoryAdjustment.Status.APPROVED == inventoryAdjustmentInstance.status ? true : false
		def warehouse = inventoryAdjustmentInstance.warehouse
        inventoryAdjustmentInstance.items.eachWithIndex { item, idx->
            def itemsMap = [:]
			def oldStock = warehouse ? item.product.getStock(warehouse).qty : BigDecimal.ZERO
			
            itemsMap["productId"] = item.product.id
            itemsMap["identifier"] = item.product?.identifier
            itemsMap["description"] = item.product?.description
			itemsMap["oldStock"] = isApproved ? item.oldStock : oldStock
			itemsMap["newStock"] = item.newStock
			itemsMap["isDeleted"] = item.isDeleted
			itemsMap["stockDifference"] = item.newStock - item.oldStock
			itemsMap["index"] = idx
            items.add(itemsMap)
        }
        return items.sort{it.description}
    }

	def updateProductStock(InventoryAdjustment inventoryAdjustmentInstance) {
		inventoryAdjustmentInstance.items.sort{it.id}.each { item ->
			def product = item.product
			product.stocks.sort{it.id}.each { stock ->
				if(stock.warehouse.identifier == inventoryAdjustmentInstance.warehouse.identifier) {
					item.oldStock = stock.qty
					stock.qty = item.newStock
					stock.save(flush: true)
					product.save(flush: true)
				}
			}
		}
	}
	
}
