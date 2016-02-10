package com.munix

class City {
    String identifier
    String description

    static belongsTo = [province:Province]

    static constraints = {
        description(nullable:false)
        identifier(nullable:false,blank:false)
        province(nullable:true)
    }
    
    String toString(){
        "${identifier}, ${province}"
    }
}
