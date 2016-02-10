package com.munix

class TripTicketDeliveryItem extends TripTicketItem{
    DirectDelivery item
    
    static constraints = {
        item(nullable:false)
    }

    String toString(){
        tripTicket
    }

}
