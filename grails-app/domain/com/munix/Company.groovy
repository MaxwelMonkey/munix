package com.munix

class Company {
    String name
    String code
    String registeredAddress
    String billingAddress
    String fax
    String landline
    String email
    String website

    static constraints = {
        name(nullable:false)
        code(nullable:true)
        registeredAddress(nullable:false)
        billingAddress(nullable:false)
        fax(nullable:true)
        landline(nullable:true)
        email(nullable:true,email:true)
        website(nullable:true)
    }
}
