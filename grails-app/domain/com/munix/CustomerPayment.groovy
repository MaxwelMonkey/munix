package com.munix

abstract class CustomerPayment {
	Customer customer	
	Date date = new Date()
	String preparedBy
	String approvedBy
	String cancelledBy
	String status="Unapproved"
    Boolean isTakenByDirectPayment = false
    Boolean isTakenByCounterReceipt = false

	static hasMany = [invoices: DirectPaymentInvoice]

	static constraints = {
		customer(nullable:false)
		preparedBy(nullable:false)
		approvedBy(nullable:true)
		cancelledBy(nullable:true)
        status(nullable:false)
    }

    void takenByDirectPayment() {
        this.isTakenByDirectPayment = true
    }
    void notTakenByDirectPayment() {
        this.isTakenByDirectPayment = false
    }
    void takenByCounterReceipt() {
        this.isTakenByCounterReceipt = true
    }
    void notTakenByCounterReceipt() {
        this.isTakenByCounterReceipt = false
    }
	
	void linkDirectPaymentInvoice(DirectPaymentInvoice directPaymentInvoice){
		this.addToInvoices(directPaymentInvoice)
		takenByDirectPayment()
	}
	
	abstract BigDecimal computeTotalAmount()
	abstract BigDecimal computeProjectedDue()
	abstract void finishTransaction(BigDecimal amount)
	abstract void rollbackTransaction(BigDecimal amount)
	abstract Map createLink()

    DirectPaymentInvoice searchRelatedInvoice(DirectPayment directPaymentInstance) {
		return this.invoices.find {
			it.directPayment == directPaymentInstance
		}
	}
	
	boolean hasInvoiceRelatedTo(DirectPayment directPaymentInstance) {
		return null != searchRelatedInvoice(directPaymentInstance)
	}	
}
