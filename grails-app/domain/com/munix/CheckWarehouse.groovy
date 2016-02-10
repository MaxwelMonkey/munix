package com.munix

class CheckWarehouse {
    String identifier
    String description
    Boolean isDefault

    static hasMany = [checks:CheckPayment]

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
        isDefault(nullable:true)
    }

    String toString(){
        identifier
    }

    String formatTotal(){
        "PHP ${String.format( '%,.2f',computeTotal() )}"
    }

    BigDecimal computeTotal(){
        def total = 0
        checks.each{
            total += it.amount
        }
        return total
    }
}
