package com.munix

class CustomerDiscountService {

    static transactional = true
    def authenticateService

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
	
	def logChanges(CustomerDiscount customerDiscountInstance) {
		if(!customerDiscountInstance.log){
			customerDiscountInstance.log = new CustomerDiscountLog(customer: customerDiscountInstance.customer, discount: customerDiscountInstance)
			createCustomerDiscountLogItem(customerDiscountInstance.log)
		}
		if (hasCustomerDiscountChanged(customerDiscountInstance)){
			createCustomerDiscountLogItem(customerDiscountInstance.log)
		}
	}

	def hasCustomerDiscountChanged(CustomerDiscount customerDiscountInstance) {
		return customerDiscountInstance.discountGroup != customerDiscountInstance.log.currentLog.discountGroup || 
			customerDiscountInstance.discountType != customerDiscountInstance.log.currentLog.discountType || 
			customerDiscountInstance.type != customerDiscountInstance.log.currentLog.type
	}


    def createCustomerDiscountLogItem(CustomerDiscountLog discountLog) {
        def log = new CustomerDiscountLogItem(user: authenticateService.userDomain(), 
			date: new Date(), 
			discountGroup: discountLog.discount.discountGroup, 
			discountType: discountLog.discount.discountType,
			type: discountLog.discount.type)
        discountLog.addToItems(log)
		discountLog.save()
    }
}
