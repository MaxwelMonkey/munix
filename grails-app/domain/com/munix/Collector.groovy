package com.munix

class Collector {
    String identifier
    String description

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
    }
    
    String toString() {
        identifier
    }
}
