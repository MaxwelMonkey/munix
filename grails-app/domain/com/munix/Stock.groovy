package com.munix

class Stock {
    Warehouse warehouse
    BigDecimal qty = 0

    static belongsTo = [product:Product]

    static constraints = {
        warehouse(nullable:false)
        product(nullable:false)
        qty(nullable:false)
    }

    String toString() {
        qty
    }

    String formatQty(){
        "${String.format('%,.2f',qty)}"
    }
 
}
