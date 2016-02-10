package com.munix

class CustomerDiscountLog {
    Customer customer

    static hasMany = [items : CustomerDiscountLogItem]
	static belongsTo = [discount: CustomerDiscount]
	static transients = ['currentLog'] 
	
    static constraints = {
        customer(nullable:true)
        discount(nullable:true)
    }
	
	CustomerDiscountLogItem getCurrentLog() {
		def currentLogItem
		def dateComparator = [compare: {a, b -> a.date <=> b.date }] as Comparator
		if (this.items) {
			currentLogItem = Collections.max( this.items, dateComparator )
		}
		return currentLogItem
	}
}
