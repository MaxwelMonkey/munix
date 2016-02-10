package com.munix

class ProductComponent {
	Product component
	BigDecimal qty
	boolean isDeleted
	
	static transients = [ 'isDeleted' ]

	static belongsTo = [product:Product]

	static constraints = {
		component(nullable: false)
		qty(nullable: false, min: 0.01)
		product(nullable: false)
	}

	String toString() {
		id
	}

	String formatNeededQty(BigDecimal neededQty) {
		"${String.format( '%,.2f',computeNeededQty(neededQty) )}"
	}

	BigDecimal computeNeededQty(BigDecimal neededQty){
		(neededQty? neededQty : 0) * (qty? qty : 0)
	}
}
