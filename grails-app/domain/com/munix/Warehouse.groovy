package com.munix

class Warehouse {
    String identifier
    String description

    static hasMany = [stocks:Stock, itemLocations:ItemLocation]

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
    }

    String toString(){
        identifier
    }

    String formatId(){
         "${id}".padLeft(4,'0')
    }
}
