package com.munix

class Truck {
    String identifier
    String model
    String plateNumber

    static constraints = {
        identifier(nullable:false,blank:false,unique:true)
        model(nullable:false,blank:false)
        plateNumber(nullable:false,blank:false,unique:true)
    }

    String toString(){
       identifier
    }
}
