package com.munix

class CollectionScheduleItem {
    CounterReceipt counterReceipt
    String type
    String remark
    BigDecimal amount
    Boolean isComplete = false//if the task is completed

    static belongsTo = [schedule:CollectionSchedule]

    static constraints = {
        counterReceipt(nullable:false)
        type(nullable:false)
        remark(nullable:true)
        isComplete(nullable:false)
        amount(nullable:false)
        schedule(nullable:true)
    }
	
	void assignDateForBoth(){
		counterReceipt.collectionDate = determineDateToday()
		counterReceipt.counterDate = determineDateToday()
	}
	
	void assignDateForCollection(){
		counterReceipt.collectionDate = determineDateToday()
	}
	
	void assignDateForCounter(){
		counterReceipt.counterDate = determineDateToday()
	}
	
	def determineDateToday() {
		return isComplete ? new Date() : null;
	}
	
	def determineAmountForCounter(){
		def amount = 0
		if (type == "Counter") {
			amount = this.amount
		}
		return amount;
	}
	
	def determineAmountForCollection(){
		def amount = 0
		if (type == "Collection") {
			amount = this.amount
		}
		return amount;
	}
	
	def determineAmountForBoth(){
		def amount = 0
		if (type == "Both") {
			amount = this.amount
		}
		return amount;
	}
}
