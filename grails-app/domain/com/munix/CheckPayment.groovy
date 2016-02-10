package com.munix

class CheckPayment {
    static enum Status {
		PENDING("Pending"), BOUNCED("Bounced"), DEPOSITED("Deposited"), FOR_REDEPOSIT("For redeposit"), CANCELLED("Cancelled")

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
    Customer customer
    Date date
    String checkNumber
    BigDecimal amount
	Bank bank
    Status status = Status.PENDING
	String remark
    CheckType type
	String branch
    DirectPaymentItemCheck directPaymentItem
    
    static belongsTo = [warehouse:CheckWarehouse]
	static transients = ['bouncedCheck', 'checkDeposit']
	static hasMany = [bouncedChecks:BouncedCheck, checkDeposits:CheckDeposit]

    static constraints = {
        customer(nullable:false)
        date(nullable:false)
        checkNumber(nullable:false)
        amount(nullable:false)
        bank(nullable:false)
        type(nullable:false)
        status(nullable:false)
		remark(nullable:true)
		branch(nullable:true)
        directPaymentItem(nullable:true)
    }

    String toString(){
        checkNumber
    }

    String formatAmount(){
        "PHP ${String.format('%,.2f',amount)}"
    }
	
	Boolean isDeposited() {
		this.status == Status.DEPOSITED
	}
	Boolean isCancelled() {
		this.status == Status.CANCELLED
	}
    Boolean isForRedeposit() {
		this.status == Status.FOR_REDEPOSIT
	}
	
	void cancel() {
		status = Status.CANCELLED
	}
	
	CheckDeposit getCheckDeposit() {
		return retrieveCurrentCheckDeposit()
	}
	
	CheckDeposit retrieveCurrentCheckDeposit() {
		def currentCheckDeposit
		def dateComparator = [compare: {a, b -> a.depositDate <=> b.depositDate }] as Comparator
		if (this.checkDeposits) {
			currentCheckDeposit = Collections.max( this.checkDeposits, dateComparator )
		}
		return currentCheckDeposit
	}
	
	BouncedCheck getBouncedCheck() {
		return retrieveCurrentBouncedCheck()
	}
	
	BouncedCheck retrieveCurrentBouncedCheck() {
		def currentBouncedCheck
		def dateComparator = [compare: {a, b -> a.date <=> b.date }] as Comparator
		if (this.bouncedChecks) {
			currentBouncedCheck = Collections.max( this.bouncedChecks, dateComparator )
		}
		return currentBouncedCheck
	}
    String formatBank(){
        return bank.identifier + " - " +branch
    }
}
