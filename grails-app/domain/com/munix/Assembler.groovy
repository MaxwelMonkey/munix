package com.munix

class Assembler {
    String identifier
    String description

    static hasMany = [jobOrders:JobOrder]

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
    }
    
    String toString() {
        identifier
    }
}
