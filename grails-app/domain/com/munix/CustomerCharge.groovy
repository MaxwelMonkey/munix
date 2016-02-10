package com.munix


class CustomerCharge extends CustomerPayment {
	String remark
	BigDecimal amountPaid = 0

	static transients = ['counterReceipts']
    static hasMany = [items: CustomerChargeItem]
	
    static constraints = {
        remark(nullable: true)
        status(nullable: false, inList: ["Unpaid", "Unapproved", "Paid", "Taken", "Cancelled"])
		amountPaid(nullable: true)
    }
	
    String toString() {
        "CC-${formatId()}"
    }

    String formatId() {
         "${id}".padLeft(8,'0')
    }
	
	void approve() {
		status = "Unpaid"
	}
	
	void unapprove() {
		status = "Unapproved"
	}
	
	void paid() {
		status = "Paid"
	}
	
	void cancel() {
		status = "Cancelled"
	}
	def isCancel(){
		return status == "Cancelled"
	}
	
	def isCancelable(){
		def cancelable = true
		if(invoices){
			invoices.each{
				if(!it.directPayment.isCancelled()){
					cancelable = false
				}
			}
		}
        if(!isUnapproved()){
            cancelable = false
        }
		return cancelable
	}

	def isUnapproved(){
		return status == "Unapproved"
	}
	
	def isUnapprovable() {
		return !invoices
	}

	def isPaid(){
		return status == "Paid"
	}

    def isApproved(){
		return status == "Unpaid"
	}

	def isDeliver(){
		return deliveryType == "Deliver"
	}
	
    BigDecimal computeReceiptsTotal() {
        def total = 0
        counterReceipts.each {
            total += it.computeTotal()
        }
        return total
    }
	
    BigDecimal computeReceiptsDueTotal() {
        def total = 0
        counterReceipts.each {
            total += it.computeAmountDueTotal()
        }
        return total
    }
    
	@Override
    BigDecimal computeTotalAmount() {
        def total = 0
        items.each {
			total += it.amount
        }
        return total
    }

	@Override
    BigDecimal computeProjectedDue() {
        def due = computeTotalAmount()
        invoices.each {
            due -= it.amount
        }
        return due
    }
	
	BigDecimal computeActualDue() {
		def due = computeTotalAmount()
		invoices.each {
			if(it.directPayment.isApproved()) {
				due -= it.amount
			}
		}
		return due
	}
    @Override
    Map createLink(){
        [controller:"customerCharge", id:this.id, action:"show"]
    }
    @Override
    void finishTransaction(BigDecimal amount) {
        this.amountPaid += amount
        if(this.amountPaid >= computeTotalAmount()) {
            paid()
        }
        notTakenByDirectPayment()
        removeFromCustomerAccountCCAmount(amount)
    }
	
    @Override
    void rollbackTransaction(BigDecimal amount) {
        this.amountPaid -= amount
        approve()
        takenByDirectPayment()
        addToCustomerAccountCCAmount(amount)
    }
	void addToCustomerAccountCCAmount(BigDecimal amount) {
		this.customer.customerAccount.addCCAmount(amount)
	}
    void removeFromCustomerAccountCCAmount(BigDecimal amount) {
		this.customer.customerAccount.removeCCAmount(amount)
	}
	List<CounterReceipt> getCounterReceipts() {
		def counterReceiptItems = CounterReceiptItem.findAllByInvoiceIdAndInvoiceType(id, CustomerPaymentType.CUSTOMER_CHARGE)
		def counterReceipts = []
		counterReceiptItems.each {
			counterReceipts.add(it.counterReceipt)
		}
		return counterReceipts
	}
}
