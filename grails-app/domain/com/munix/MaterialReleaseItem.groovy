package com.munix

class MaterialReleaseItem {
    MaterialRequisitionItem materialRequisitionItem
    BigDecimal qty = 0
	BigDecimal cost = 0

	static transients = ['totalCost']
	static belongsTo = [materialRelease:MaterialRelease]
	
	static constraints = {
        materialRequisitionItem(nullable: false)
        qty(nullable: false)
		cost(nullable: false, scale:4)
    }
	
	BigDecimal getTotalCost() {
		return computeTotalCost()
	}
	
	BigDecimal computeTotalCost() {
		return qty * cost
	}
}
