package com.munix

class TripTicketWaybillItem extends TripTicketItem{
    Waybill item

    static constraints = {
        item(nullable:false)
    }

    String toString(){
        tripTicket
    }
}
