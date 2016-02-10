package com.munix

class Term {
    String identifier
    String description
    BigDecimal dayValue

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
        dayValue(nullable:false, blank:false, min:new BigDecimal("0"))
    }

    String toString() {
        identifier
    }
}
