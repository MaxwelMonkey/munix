package com.munix

class TripTicketItem {
    static mapping = {
        tablePerHierarchy false
    }

    BigDecimal priority = 1
    String type

    static belongsTo = [tripTicket:TripTicket]

    static constraints = {
        priority(nullable:false)
        type(nullable:false)
    }
    
    String toString(){
        tripTicket
    }

    String formatPriority(){
        "${String.format( '%,.0f',priority )}"
    }
    
}
