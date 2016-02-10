package com.munix

class Waybill {
    Date date = new Date()
    Customer customer
    Forwarder forwarder
    BigDecimal declaredValue
    String preparedBy
	String cancelledBy
    String status = "Processing"
    TripTicketWaybillItem tripTicket
    
    static hasMany = [deliveries:SalesDelivery,packages:WaybillPackage]
    
    static constraints = {
        date(nullable:false)
        tripTicket(nullable:true)
        customer(nullable:false)
        forwarder(nullable:false)
        preparedBy(nullable:false)
		cancelledBy(nullable:true)
        status(nullable:false)
    }
	
	def isCancelled(){
		return status == "Cancelled"
	}
	
	void cancel(){
		status = "Cancelled"
	}
	
	void complete(){
		status = "Complete"
	}
	
	void processing(){
		status = "Processing"
	}
	
	Boolean isProcessing() {
		return this.status.toString() == "Processing"
	}
	
	Boolean isUnapproved() {
		return this.status.toString() == "Processing"
	}

    String toString(){
        "WB-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    String formatDeclaredValue(){
        "${String.format( '%,.2f', declaredValue )}%"
    }

    String formatDeclaredValueTotal(){
        "PHP ${String.format( '%,.2f', computeDeclaredValueTotal() )}"
    }

    BigDecimal computeDeclaredValueTotal(){
        def total = 0
        deliveries.each{
            total += (it.computeTotalAmount() * (declaredValue/100))
        }
        return total
    }
}
