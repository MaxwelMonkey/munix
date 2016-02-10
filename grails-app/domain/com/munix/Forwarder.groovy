package com.munix

class Forwarder {
    String identifier
    String description
    String street
    City city
    String zip
    String landline
    String contact
    String contactPosition
    
    static constraints = {
        identifier(nullable:false, blank:false)
        description(nullable:false, blank:false)
        city(nullable:true)
        zip(nullable:true)
        landline(nullable:true)
        contact(nullable:true)
        contactPosition(nullable:true)
    }

    String toString(){
        "${identifier}"
    }

    String formatAddress(){
        if(street && city && zip){
            return "${street}, ${city.identifier}, ${city.province}, ${city.province.region}, ${city.province.region.country}, ${zip}"
        }
        else if(street && city){
            return "${street}, ${city.identifier}, ${city.province}, ${city.province.region}, ${city.province.region.country}"
        }
        else{
            return ""
        }
    }
}
