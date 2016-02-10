package com.munix

class StockCard {
	Product product
    static hasMany = [items: StockCardItem]
        
    static constraints = {
		product(nullable: false)
    }
}
