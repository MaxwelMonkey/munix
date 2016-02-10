package com.munix

class CustomerDiscountService {

    static transactional = true

    def validateCustomerDiscount(CustomerDiscount customerDiscountInstance) {
		def valid = true
		def itemList = CustomerDiscount.findWhere(
			customer:customerDiscountInstance.customer,
			discountType: customerDiscountInstance.discountType,
			type: customerDiscountInstance.type
		)
		if (itemList) {
			valid = false
		}
		return valid
    }
	
	def validateUpdatedCustomerDiscount(CustomerDiscount newCustomerDiscount, CustomerDiscount oldCustomerDiscount) {
			newCustomerDiscount.type == oldCustomerDiscount.type
		if (newCustomerDiscount.discountType == oldCustomerDiscount.discountType &&
			newCustomerDiscount.type == oldCustomerDiscount.type) {
			return true
		} else {
			return validateCustomerDiscount(newCustomerDiscount)
		}
	}
}
