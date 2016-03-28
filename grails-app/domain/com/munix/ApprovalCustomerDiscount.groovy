package com.munix

class ApprovalCustomerDiscount {

	ApprovalProcess approvalProcess
	CustomerDiscount customerDiscount
	Customer customer
	DiscountType discountType
	DiscountGroup discountGroup
	String type
    
    static belongsTo = ApprovalProcess

    static constraints = {
        customerDiscount(nullable:true)
        customer(nullable:false)
        discountType(nullable:true)
        discountGroup(nullable:true)
        type(nullable:true)
    }
    
    public String toString() {
    	def result = ""
    	result+="Discount Type: "+discountType
    	result+=", Discount Group: "+discountGroup
    	result+=", Type: "+type
    	return result 
    }
}
