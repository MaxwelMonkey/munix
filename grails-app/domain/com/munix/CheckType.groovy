package com.munix

class CheckType {
    String routingNumber
    String description
    String branch

    static constraints = {
        routingNumber(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
        branch(nullable:false, blank:false)
    }

    String toString(){
        routingNumber
    }
}
