package com.munix

class InventoryAdjustmentItem {

	Product product
	BigDecimal newStock
    BigDecimal oldStock
	boolean isDeleted
	
	static transients = ['isDeleted']
	static belongsTo = [adjustment:InventoryAdjustment]
	
    static constraints = {
		newStock(nullable: false)
		oldStock(nullable: true)
		adjustment(nullable: true)
    }
}
