package com.munix

class SalesAgent {
    String identifier
    String lastName
    String firstName
    String street
    City city
    String zip
    String mobile
    String landline
    String skype
    String yahoo
    String email
	BigDecimal commission

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        lastName(nullable:false, blank:false)
        firstName(nullable:false, blank:false)
        street(nullable:true)
        city(nullable:true)
        zip(nullable:true)
        mobile(nullable:true)
        landline(nullable:true)
        skype(nullable:true)
        yahoo(nullable:true)
        email(nullable:true,email:true)
		commission(nullable: true, min: 0.00)
    }

    String toString(){
        "${identifier}"
    }

    String formatName(){
        "${lastName}, ${firstName}"
    }

    String formatAddress(){
        def address = ""

        if(street){
            address = "${street}, "
        }

        if(city){
            address = "${address}${city}, ${city.province}, ${city.province.region}"
        }

        if(zip){
            address = "${address}, ${zip}"
        }

        return address
    }

}
