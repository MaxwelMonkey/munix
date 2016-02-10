package com.munix

class DirectDelivery {
    static enum Status {
		PROCESSING("Processing"), CANCELLED("Cancelled"), COMPLETE("Complete")

		String name

		Status(String name) {
			this.name = name
		}

		@Override
		public String toString() {
			return name
		}

        public static Status getStatusByName(String name) {
            return Status.values().find { it.toString().equalsIgnoreCase name}
        }
	}
    Date date = new Date()
    Customer customer
    String preparedBy
	String cancelledBy
    Status status = Status.PROCESSING
    TripTicketDeliveryItem tripTicket
    
    static hasMany = [deliveries:SalesDelivery,packages:DirectDeliveryPackage]

    static constraints = {
        date(nullable:false)
        tripTicket(nullable:true)
        customer(nullable:false)
        preparedBy(nullable:false)
		cancelledBy(nullable:true)
        status(nullable:false)
    }

	def isCancelled(){
		return status == Status.CANCELLED
	}
	
	def isCancelable(){
		return !tripTicket
	}

    void cancel(){
    	status = Status.CANCELLED
    }
    
	void complete(){
		status = Status.COMPLETE
	}
	
	void processing(){
		status = Status.PROCESSING
	}
	
    Boolean isProcessing() {
    	return this.status.toString() == "Processing"
    }
	
	Boolean isUnapproved() {
		return this.status.toString() == "Processing"
	}

    String toString(){
        "DD-${formatId()}"
    }
    
    String formatId(){
         "${id}".padLeft(8,'0')
    }

    String formatTotal(){
        "PHP ${String.format( '%,.2f',computeTotal() )}"
    }

    BigDecimal computeTotal(){
        def total = 0
        deliveries.each{
            total += it.computeTotalAmount()
        }
        return total
    }
}
