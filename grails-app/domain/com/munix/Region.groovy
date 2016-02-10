package com.munix

class Region {
    String identifier
    String description

    static belongsTo = [country:Country]
    static hasMany = [provinces:Province]

    static constraints = {
        description(nullable:false)
        identifier(nullable:false, blank:false, unique:true)
        country(nullable:true)
    }
    
    String toString(){
        identifier
    }
}

