package com.munix

class Province {
    String identifier
    String description

    static belongsTo = [region:Region]
    static hasMany = [cities:City]

    static constraints = {
        description(nullable:false)
        identifier(nullable:false, blank:false, unique:true)
        region(nullable:true)
    }
    
    String toString(){
        identifier
    }
}
