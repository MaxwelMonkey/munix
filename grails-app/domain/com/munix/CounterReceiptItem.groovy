package com.munix

class CounterReceiptItem {
	Long invoiceId
	CustomerPaymentType invoiceType
	BigDecimal amount = BigDecimal.ZERO
	
	static transients = ['invoice']
	
    static constraints = {
		invoiceId(nullable: false)
		invoiceType(nullable: false)
		amount(nullable: false)
		counterReceipt(nullable: false)
    }
	
	static belongsTo = [counterReceipt: CounterReceipt]
	
	String toString() {
		invoice?.id
	}
	
	CustomerPayment getInvoice() {
		switch (this.invoiceType) {
			case CustomerPaymentType.CREDIT_MEMO:
				return CreditMemo.get(this.invoiceId)
			case CustomerPaymentType.BOUNCED_CHECK:
				return BouncedCheck.get(this.invoiceId)
			case CustomerPaymentType.SALES_DELIVERY:
				return SalesDelivery.get(this.invoiceId)
			case CustomerPaymentType.CUSTOMER_CHARGE:
				return CustomerCharge.get(this.invoiceId)
		}
	}
	
	void setInvoice(CustomerPayment customerPayment) {
		this.invoiceId = customerPayment.id
		switch (customerPayment.class) {
			case CreditMemo.class:
				this.invoiceType = CustomerPaymentType.CREDIT_MEMO
				return
			case BouncedCheck.class:
				this.invoiceType = CustomerPaymentType.BOUNCED_CHECK
				return
			case SalesDelivery.class:
				this.invoiceType = CustomerPaymentType.SALES_DELIVERY
				return
			case CustomerCharge.class:
				this.invoiceType = CustomerPaymentType.CUSTOMER_CHARGE
		}
	}
	
	static CounterReceiptItem retrieveCounterReceiptItem(CounterReceipt counterReceipt, Long invoiceId, CustomerPaymentType invoiceType) {
		return CounterReceiptItem.find("from CounterReceiptItem as c where c.counterReceipt=:counterReceipt and c.invoiceId=:invoiceId and c.invoiceType=:invoiceType", [counterReceipt: counterReceipt, invoiceType: invoiceType, invoiceId: invoiceId])
	}
}
