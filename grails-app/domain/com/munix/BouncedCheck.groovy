package com.munix

import java.math.BigDecimal;

class BouncedCheck extends CustomerPayment {
    String remark
    Boolean forRedeposit
    CheckStatus bouncedCheckStatus
    BigDecimal amountPaid = 0

    static belongsTo = [CheckPayment]
    static transients = ['counterReceipts']
	static hasMany = [checks:CheckPayment]
	
    static constraints = {
        status(nullable:false,inList : ["Approved", "Unapproved", "Cancelled", "Paid", "Taken"])
        remark(nullable:true)
        forRedeposit(nullable:false)
        amountPaid(nullable:false)
    }

    String toString(){
        "BC-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    String formatTotal(){
        "PHP ${String.format( '%,.2f',computeTotalAmount() )}"
    }

	void approve(){
		status = "Approved"
	}

	void unapprove(){
		status = "Unapproved"
	}
	
	void cancel(){
		status = "Cancelled"
	}

	void paid(){
		status = "Paid"
	}
    def isApproved(){
        return status == "Approved"
    }
    def isPaid(){
        return status == "Paid"
    }
    def isUnapproved(){
        return status == "Unapproved"
    }
    def isCancelled(){
        return status == "Cancelled"
    }
    def isForRedeposit(){
        return forRedeposit
    }
    def isUnpaid(){
        return !forRedeposit
    }
	
	Boolean isUnapprovable() {
		def isUnapprovable = this.isApproved() && !(this.isTakenByDirectPayment || this.isTakenByCounterReceipt)
		if (this.forRedeposit && isUnapprovable) {
			isUnapprovable = !checkHasBeenDeposited()
		}
		return isUnapprovable
	}
	
	private boolean checkHasBeenDeposited() {
		def checkHasBeenDeposited = false
		checks.each {
			if (it.checkDeposits.size() > it.bouncedChecks.size()) {
				checkHasBeenDeposited = true
			}
		}
		return checkHasBeenDeposited
	}

	@Override
    BigDecimal computeTotalAmount() {
        def total = 0
        checks.each {
            total += it.amount
        }
        return total
    }
	
	@Override
	BigDecimal computeProjectedDue() {
	    def due = computeTotalAmount()
        invoices.each{
             if(!it.directPayment.isCancelled()){
                due -= it.amount
            }
        }
        return due
	}
	@Override
    Map createLink(){
        [controller:"bouncedCheck", id:this.id, action:"show"]
    }
    @Override
    void finishTransaction(BigDecimal amount){
		this.amountPaid += amount
		removeToCustomerAccountBCAmount(amount)
		if(this.amountPaid >= computeTotalAmount()){
			paid()
		}
		notTakenByDirectPayment()
    }
	
	void addToCustomerAccountBCAmount(BigDecimal amount) {
		this.customer.customerAccount.addBCAmount(amount)
	}
	
	void removeToCustomerAccountBCAmount(BigDecimal amount) {
		this.customer.customerAccount.removeBCAmount(amount)
	}

    @Override
    void rollbackTransaction(BigDecimal amount){
		this.amountPaid -= amount
		addToCustomerAccountBCAmount(amount)
        approve()
        takenByDirectPayment()    
	}

    BigDecimal computeNotClearedChecks() {
        def total = 0
        checks.each {
            if(it.isForRedeposit()){
                total += it.amount
            }
        }
        return total
    }

	List<CounterReceipt> getCounterReceipts() {
		def counterReceiptItems = CounterReceiptItem.findAllByInvoiceIdAndInvoiceType(id, CustomerPaymentType.BOUNCED_CHECK)
		def counterReceipts = []
		counterReceiptItems.each {
			counterReceipts.add(it.counterReceipt)
		}
		return counterReceipts
	}
	
}
