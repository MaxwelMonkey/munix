package com.munix

class Overpayment {
	
	static enum Status {
		UNAPPROVED("Unapproved"), APPROVED("Approved")
		
		String name
		
		Status(String name) {
			this.name = name
		}
		
		@Override
		public String toString() {
			return name
		}
	}

	BigDecimal amount = BigDecimal.ZERO
	Status status = Status.APPROVED

    static constraints = {
		status(nullable:false)
		amount(nullable:false)
    }
	
	static belongsTo = [directPayment:DirectPayment]
	
	void approved() {
		status = Status.APPROVED
	}
	
	void unapproved() { 
		status = Status.UNAPPROVED
	}
	
	def isApproved() {
		return status == Status.APPROVED
	}
	
	def isUnapproved() {
		return status == Status.UNAPPROVED
	}
	
}
