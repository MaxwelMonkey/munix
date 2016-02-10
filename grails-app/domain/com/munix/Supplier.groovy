package com.munix

import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.list.LazyList

class Supplier {
    String identifier
    String name
    String address
    Term term
    String email
    String contact
    String landline
    String fax
    String skype
    String yahoo
    String website
    String tin
    CurrencyType currency
    Country country
    String localeType = "Local"
    List items = new ArrayList()

    static hasMany = [items:SupplierItem]

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        name(nullable:false)
        tin(nullable:true)
        address(nullable:true)
        term(nullable:true)
        email(nullable:true, email:true)
        contact(nullable:true)
        landline(nullable:true)
        fax(nullable:true)
        skype(nullable:true)
        yahoo(nullable:true)
        website(nullable:true)
        currency(nullable:true)
        country(nullable:true)
        localeType(inList : ["Foreign", "Local"])
    }

    def getLocality(){
        def locality = "N/A"
        if(country){
            if(country.identifier == "Philippines"){
                locality = "Local"
            }else{
                locality = "Foreign"
            }
        }
        return locality
    }

    String toString(){
        identifier
    }

    String formatId(){
         "${id}".padLeft(4,'0')
    }
    def getSupplierItemList(){
		return LazyList.decorate(items,FactoryUtils.instantiateFactory(SupplierItem.class))
	}
}
