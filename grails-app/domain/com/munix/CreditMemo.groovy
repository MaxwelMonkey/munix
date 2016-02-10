package com.munix

class CreditMemo extends CustomerPayment {
    DiscountType discountType
	DirectPaymentItem directPaymentItem
    BigDecimal discount = 0
    BigDecimal commissionRate = 0
	Reason reason
    String remark
    String approvedTwoBy
    Warehouse warehouse
    BigDecimal additionalDiscount = 0
    Boolean applyFourMonthRule = true

	static transients = ['counterReceipts', 'amountPaid']
    static hasMany = [items: CreditMemoItem, printLogs:PrintLogCreditMemo]
    
    static constraints = {
        approvedTwoBy(nullable: true)
		directPaymentItem(nullable: true)
        discountType(nullable: false)
        discount(nullable: false)
        status(nullable: false, inList : ["Approved", "Unapproved", "Paid", "Taken","Cancelled","Second Approval Pending"])
        remark(nullable: true)
        commissionRate(nullable: false)
		reason(nullable: false)
		warehouse(nullable: true)
		additionalDiscount(nullable: true)
        applyFourMonthRule(nullable: true)
    }
	
    String toString(){
        "CM-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    String formatCommissionRate(){
        "${String.format( '%,.2f',commissionRate )}%"
    }

    String formatDiscountTotal(){
        "PHP ${String.format( '%,.2f',computeDiscountTotal() )}"
    }
    
    String formatDiscountedAmountTotal(){
        "PHP ${String.format( '%,.2f',computeTotalAmount() )}"
    }

    String formatCommissionAmount(){
        "PHP ${String.format( '%,.2f',computeCommissionAmount() )}"
    }

    String formatAmountTotal(){
        "PHP ${String.format( '%,.2f',computeTotal() )}"
    }

	
	void approve(){
		status = "Approved"
	}
    void secondApproval(){
		status = "Second Approval Pending"
	}
	
	void cancelled(){
		status = "Cancelled"
	}

	void unapprove(){
		status = "Unapproved"
	}
	
	void paid(){
		status = "Paid"
	}
	
    def isUnapproved(){
        return status == "Unapproved"
    }
    def isFirstApproved(){
        return status == "Second Approval Pending"
    }

    def isApproved(){
		return status == "Approved"
	}
    def isPaid(){
		return status == "Paid"
	}

    def isCancelled(){
		return status == "Cancelled"
	}
	
	def isADebitMemo() {
		return computeTotalAmount() < 0
	}

    public void setDirectPaymentItem(DirectPaymentItem directPaymentItem){
		this.directPaymentItem = directPaymentItem
		takenByDirectPayment()
	}
	
    BigDecimal computeTotal() {
        def total = 0
        items.each {
            total += it.computeFinalAmount()
        }
        return total
    }
	
    @Override
    void finishTransaction(BigDecimal amount){
        paid()
        notTakenByDirectPayment()
        if(isADebitMemo()){
            removeFromCustomerAccountDMAmount(amount)
        }
    }
	
    @Override
    void rollbackTransaction(BigDecimal amount){
        approve()
        takenByDirectPayment()
        if(isADebitMemo()){
            addToCustomerAccountDMAmount(amount)
        }
    }
    void addToCustomerAccountDMAmount(BigDecimal amount) {
		this.customer.customerAccount.addDMAmount(amount)
	}
    void removeFromCustomerAccountDMAmount(BigDecimal amount) {
		this.customer.customerAccount.removeDMAmount(amount)
	}
    BigDecimal computeDiscountTotal() {
		def total = 0
        items.each {
            total += it.computeDiscountAmount()
        }
        return total
    }
	
    @Override
    BigDecimal computeTotalAmount(){
        computeTotal() - computeDiscountTotal()
    }
    @Override
    Map createLink(){
        [controller:"creditMemo", id:this.id, action:"show"]
    }
	@Override
	BigDecimal computeProjectedDue() {
		def due = 0
		if (this.isUnapproved()) {
			due = computeCreditMemoTotalAmount()
		}
		return due
	}
	
	BigDecimal computeCreditMemoTotalAmount() {
		return computeTotalAmount().multiply(new BigDecimal ("-1"))
	}

    BigDecimal computeCommissionAmount(){
        computeTotalAmount() * (commissionRate/100)
    }

	List<CounterReceipt> getCounterReceipts() {
		def counterReceiptItems = CounterReceiptItem.findAllByInvoiceIdAndInvoiceType(id, CustomerPaymentType.CREDIT_MEMO)
		def counterReceipts = []
		counterReceiptItems.each {
			counterReceipts.add(it.counterReceipt)
		}
		return counterReceipts
	}
	
	BigDecimal getAmountPaid() {
		def amountPaid = 0
		if (this.isPaid()) {
			amountPaid = computeCreditMemoTotalAmount()
		}
		return amountPaid
	}
	
	def formatAdditionalDiscount(){
		if(additionalDiscount)
			return "${String.format('%,.2f',additionalDiscount)}%"
		return ""
	}
}


