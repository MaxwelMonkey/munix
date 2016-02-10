package com.munix

class DirectPaymentItemCheck extends DirectPaymentItem{
    CheckPayment checkPayment

    static constraints = {
        checkPayment(nullable:false)
    }

    String toString(){
        directPayment
    }

	@Override	
	String isNotUnapprovable() {
		if (checkPayment?.checkDeposit && !checkPayment?.checkDeposit.isCancelled()) {
			"direct.payment.item.check.unapprovable"
		}
	}
}
