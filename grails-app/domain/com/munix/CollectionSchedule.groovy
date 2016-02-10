package com.munix

class CollectionSchedule {
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
    String identifier
    String description
    String remarks
    Date startDate
    Date endDate
    Status status = Status.PROCESSING

    String preparedBy
    String closedBy
	String cancelledBy
    Collector collector

    static hasMany = [items:CollectionScheduleItem]

    static constraints = {
        identifier(nullable:false, unique:true, blank:false)
        description(nullable:true)
        remarks(nullable:true)
        startDate(nullable:false)
        endDate(nullable:false)
        status(nullable:false)
        preparedBy(nullable:false)
        closedBy(nullable:true)
		cancelledBy(nullable:true)
        collector(nullable:false)
    }

    String formatPeriod(){
        "${startDate} - ${endDate}"
    }

    String toString() {
        "CS-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }
	
	void cancel() {
		status = Status.CANCELLED
	}
    void complete() {
		status = Status.COMPLETE
	}
    void processing() {
		status = Status.PROCESSING
	}
    def isCancel() {
		return status == Status.CANCELLED
	}
    def isProcessing() {
		return status == Status.PROCESSING
	}
    def isComplete() {
		return status == Status.COMPLETE
	}
	
	def isUnapproved() {
		return status == Status.PROCESSING
	}

	BigDecimal computeAmountCounterTotal() {
		def total = 0
		items.each {
			total += it.determineAmountForCounter()
		}
		return total
	}
	
	BigDecimal computeAmountCollectionTotal() {
		def total = 0
		items.each {
			total += it.determineAmountForCollection()
		}
		return total
	}
	
	BigDecimal computeAmountBothTotal() {
		def total = 0
		items.each {
			total += it.determineAmountForBoth()
		}
		return total
	}

	BigDecimal computeTotalAmount() {
		def total = 0
		items.each {
			total += it.amount
		}
		return total
	}
	
}