package com.munix

class TripTicket {

    Date date = new Date()
    Truck truck
    Personnel driver
    String preparedBy
	String markAsCompleteBy
	String cancelledBy
    String status = "Processing"
    String remark

    static hasMany = [helpers:Personnel,items:TripTicketItem]
    
    static constraints = {
        date(nullable:false)
        truck(nullable:false)
        driver(nullable:false)
        preparedBy(nullable:false)
		markAsCompleteBy(nullable:true)
		cancelledBy(nullable:true)
		status(nullable:false)
        remark(nullable:true)
    }
	
	String toString(){
		"TT-${formatId()}"
	}

	String formatId(){
		"${id}".padLeft(8,'0')
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
	
}
