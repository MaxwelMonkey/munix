
package com.munix

class DirectPaymentItem {
    Date date = new Date()
    PaymentType paymentType
    BigDecimal amount
	String remark

    static belongsTo = [directPayment:DirectPayment]

    static constraints = {
        date(nullable: false)
        paymentType(nullable: false)
        amount(nullable: false, blank: false, min: BigDecimal.ZERO)
        directPayment(nullable: true)
		remark(nullable: true)
    }

    def relatedCreditMemo(){
        return CreditMemo.findByDirectPaymentItem(this)
    }
	
	def String isNotUnapprovable() {
		// not yet implemented for non-check items
		def result;
		return result;
	}

}
