package com.munix

import com.munix.CustomerLedgerEntry.Type

class CustomerLedgerEntry {

	static enum Type {
		APPROVED_SALES_DELIVERY("Sales Delivery", "salesDelivery"),
		UNAPPROVED_SALES_DELIVERY("Sales Delivery - Unapproved", "salesDelivery"),
		APPROVED_CUSTOMER_CHARGE("Customer Charge", "customerCharge"),
		UNAPPROVED_CUSTOMER_CHARGE("Customer Charge - Unapproved", "customerCharge"),
		APPROVED_DEBIT_MEMO("Debit Memo", "creditMemo"),
		UNAPPROVED_DEBIT_MEMO("Debit Memo - Unapproved", "creditMemo"),
		APPROVED_CREDIT_MEMO("Credit Memo", "creditMemo"),
		UNAPPROVED_CREDIT_MEMO("Credit Memo - Unapproved", "creditMemo"),
		APPROVED_DIRECT_PAYMENT("Direct Payment", "directPayment"),
		UNAPPROVED_DIRECT_PAYMENT("Direct Payment - Unapproved", "directPayment"),
		DIRECT_PAYMENT_ITEM("Direct Payment Item", "directPayment"),
		OVERPAYMENT("Unapplied Overpayment Adjustment", "directPayment"),
		APPROVED_BOUNCED_CHECK("Bounced Check", "bouncedCheck"),
		UNAPPROVED_BOUNCED_CHECK("Bounced Check - Unapproved", "bouncedCheck"),
        BOUNCED_CHECK_ITEM("Bounced Check", "bouncedCheck"),
		APPROVED_CHECK_DEPOSIT_ITEM("Deposit Check", "checkDeposit"),
		CUSTOMER_INITIAL_BALANCE_ENTRY("Initial Balance", "customer")
		
		String type
		String controllerType
		
		Type(String type, String controllerType) {
			this.type = type
			this.controllerType = controllerType
		}
		
		@Override
		public String toString() {
			return type
		}
	}

	Date dateOpened
	Date datePosted
	Type type
    String details
	String remark
	String referenceId
	BigDecimal amount
	BigDecimal debitAmount
	BigDecimal creditAmount
	BigDecimal runningBalance
	Long linkId
    Boolean isChild

	static belongsTo = [customerLedger : CustomerLedger]
    static hasMany = [paymentBreakdown : CustomerLedgerEntry]

    static constraints = {
		dateOpened(nullable:false)
        details(nullable:true)
		remark(nullable:true)
		type(nullable:false)
		referenceId(nullable:false)
		amount(nullable:true)
		debitAmount(nullable:true)
		creditAmount(nullable:true)
		runningBalance(nullable:false)
		linkId(nullable:false)
        isChild(nullable:false)
        paymentBreakdown(nullable:true)
    }

    Map generateLink(){
        return [id: this.linkId, controller: type.controllerType, action: 'show']
    }
	
	def computeDebit() {
		this.debitAmount = this.amount
	}
	
	def computeCredit() {
		this.creditAmount = this.amount
	}

    def addPaymentBreakdown(CustomerLedgerEntry ledgerEntry){
        this.addToPaymentBreakdown(ledgerEntry)
    }
	
}
