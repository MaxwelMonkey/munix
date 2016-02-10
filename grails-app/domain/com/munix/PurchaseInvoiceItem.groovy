package com.munix

class PurchaseInvoiceItem {

	PurchaseOrderItem purchaseOrderItem
	BigDecimal finalPrice = BigDecimal.ZERO
	BigDecimal qty = BigDecimal.ZERO
	boolean isDeleted
	
	static transients = ['isDeleted', 'discountedPrice', 'localPrice', 'localDiscountedPrice']
    static constraints = {
		purchaseOrderItem(nullable:false)
		finalPrice(nullable:false, scale:4)
		qty(nullable:false, min:new BigDecimal("1.00"))
    }
	
	static belongsTo = [purchaseInvoice: PurchaseInvoice]
	
	BigDecimal getDiscountedPrice() {
		this.finalPrice * (1 - (purchaseInvoice.discountRate / 100))
	}
	
	BigDecimal getLocalPrice() {
		this.finalPrice?.multiply(this.purchaseInvoice?.exchangeRate?:1)
	}
	
	BigDecimal getLocalDiscountedPrice() {
		this.localPrice * (1 - (purchaseInvoice.discountRate / 100))
	}
}
