package com.munix

class CustomerPaymentService {

    static transactional = true
	private static final APPROVED = "Approved"
	private static final UNPAID = "Unpaid"
	private static final CLOSED = "Closed"
	private static final DIRECT_PAYMENT_AVAILABILITY_PROPERTY = "isTakenByDirectPayment"
	private static final COUNTER_RECEIPT_AVAILABILITY_PROPERTY = "isTakenByCounterReceipt"

    def getAvailableCustomerPaymentsForDirectPayment(Customer customerInstance) {
		return getAvailableCustomerPayments(customerInstance, DIRECT_PAYMENT_AVAILABILITY_PROPERTY)
	}
	
	def getAvailableCustomerPaymentsForCounterReceipt(Customer customerInstance) {
		return getAvailableCustomerPayments(customerInstance, COUNTER_RECEIPT_AVAILABILITY_PROPERTY)
	}
	
	private getAvailableCustomerPayments(Customer customerInstance, String availabilityProperty) {
		def deliveries = getAvailableSalesDeliveries(customerInstance, availabilityProperty)
		def charges = getAvailableCustomerCharges(customerInstance, availabilityProperty)
		def bouncedChecks = getAvailableBouncedChecks(customerInstance, availabilityProperty)
		def creditMemos = getAvailableCreditMemos(customerInstance, availabilityProperty)

		return [creditMemos: creditMemos, bouncedChecks: bouncedChecks, deliveries: deliveries, charges: charges]
	}
	
	private List getAvailableSalesDeliveries(Customer customerInstance, String availabilityProperty) {
		return SalesDelivery.withCriteria {
			eq("customer", customerInstance)
			'in'("status", [UNPAID, CLOSED])
			eq(availabilityProperty, false)
		}
	}
	
	private List getAvailableCustomerCharges(Customer customerInstance, String availabilityProperty) {
		return CustomerCharge.withCriteria {
			eq("customer", customerInstance)
			eq("status", UNPAID)
			eq(availabilityProperty, false)
		}
	}
	
	private List getAvailableBouncedChecks(Customer customerInstance, String availabilityProperty) {
		return BouncedCheck.withCriteria {
            eq("forRedeposit",false)
			eq("customer", customerInstance)
			eq("status", APPROVED)
			eq(availabilityProperty, false)
		}
	}
	
	private List getAvailableCreditMemos(Customer customerInstance, String availabilityProperty) {
		return CreditMemo.withCriteria {
			eq("customer", customerInstance)
			eq("status", APPROVED)
			eq(availabilityProperty, false)
		}
	}
}
