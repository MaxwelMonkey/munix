package com.munix

class Personnel {
    String identifier
    String lastName
    String firstName
    PersonnelType type

    static constraints = {
        identifier(nullable:false,blank:false,unique:true)
        lastName(nullable:false,blank:false)
        firstName(nullable:false,blank:false)
        type(nullable:false)
    }

    String toString() {
        identifier
    }
}
