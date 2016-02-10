package com.munix

class DirectPaymentInvoice {
    static mapping = {
        tablePerHierarchy false
    }
	
	CustomerPaymentType type
    BigDecimal amount = 0
	
	DirectPaymentInvoice() {}
	
	DirectPaymentInvoice(CustomerPaymentType type) {
		this.type = type
	}

    static belongsTo = [directPayment: DirectPayment]

    static constraints = {
        type(nullable:false)
        amount(nullable:false,min:new BigDecimal("0"))
    }
	
    String formatAmount(){
        "PHP ${String.format( '%,.2f',amount )}"
    }
	String formatDue(due){
		"PHP ${String.format( '%,.2f',due)}"
	}
	
	def getRelatedCustomerPayment() {
		switch (this.type) {
			case CustomerPaymentType.CREDIT_MEMO:
                def creditMemo = CreditMemo.find("from CreditMemo as cm left join cm.invoices as inv where inv.id = :dpiId", [dpiId: this.id])
				return ((List) creditMemo).get(0)
			case CustomerPaymentType.BOUNCED_CHECK:
                def bouncedCheck = BouncedCheck.find("from BouncedCheck as bc left join bc.invoices as inv where inv.id = :dpiId", [dpiId: this.id])
				return ((List) bouncedCheck).get(0)
			case CustomerPaymentType.SALES_DELIVERY:
                def salesDelivery = SalesDelivery.find("from SalesDelivery as sd left join sd.invoices as inv where inv.id = :dpiId", [dpiId: this.id])
				return ((List) salesDelivery).get(0)
			case CustomerPaymentType.CUSTOMER_CHARGE:
                def charge = CustomerCharge.find("from CustomerCharge as cc left join cc.invoices as inv where inv.id = :dpiId", [dpiId: this.id])
				return ((List) charge).get(0)
		}
	}
}
