package com.munix

class Country {
    String identifier
    String description

    static hasMany = [regions:Region]

    static constraints = {
        identifier(nullable:false,blank:false,unique:true)
        description(nullable:false)
    }

    String toString(){
        identifier
    }
}
