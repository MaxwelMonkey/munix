package com.munix

class LaborCost {

	String type
	BigDecimal amount
	
    static constraints = {
		type(nullable: false, blank:false)
		amount(nullable: false)
    }
	
	static belongsTo = [product: Product]
	
	String toString() {
		type
	}
}
