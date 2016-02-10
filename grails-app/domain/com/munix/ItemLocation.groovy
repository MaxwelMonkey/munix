package com.munix

class ItemLocation {
	String description
	
	static belongsTo = [warehouse:Warehouse]
	
    static constraints = {
		description(nullable:false, maxSize:250)
    }
	
	String toString() {
		description
	}
}
