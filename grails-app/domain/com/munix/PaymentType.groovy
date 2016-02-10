package com.munix

class PaymentType {
    String identifier
    String description
    Boolean isCheck
    Boolean deductibleFromSales

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
        isCheck(nullable:true)
        deductibleFromSales(nullable:false)
    }

    String toString(){
        identifier
    }
}
