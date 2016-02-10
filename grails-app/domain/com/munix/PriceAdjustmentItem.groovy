package com.munix

class PriceAdjustmentItem {

	Product product
	BigDecimal newPrice
    BigDecimal oldPrice
	boolean isDeleted
		
	static transients = ['isDeleted', 'margin']
	
    static constraints = {
		newPrice(nullable: false, scale:4)
		oldPrice(nullable: false, scale:4)
    }

	static belongsTo = [priceAdjustment: PriceAdjustment]
	
	BigDecimal getMargin() {
		return computeMargin()
	}
	
	BigDecimal computeMargin() {
		def margin = 0
		if((!oldPrice) || oldPrice == 0) {
			margin = 0			
		} else {
			margin = (((newPrice / oldPrice) - 1) * 100)
		}
		return margin
	}
}
