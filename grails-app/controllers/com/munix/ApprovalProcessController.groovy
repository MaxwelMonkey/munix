package com.munix

class ApprovalProcessController {

	def authenticateService
	def customerService
	def customerDiscountService
	
    def approve = {
    	def a = ApprovalProcess.get(Long.parseLong(params.id))
    	a.status = "Approved"
    	a.approvedBy = authenticateService.userDomain()
    	a.approvedDate = new Date()
    	a.save(flush:true)
    	if(a.type=="Customer") {
    		approveCustomer(a)
    		flash.message = "The request has been approved. <a href='${request.contextPath}/customer/show/"+a.referenceNumber+"'>View Customer</a>"
    	}
    	if(a.type=="Customer Discount") {
    		approveCustomerDiscount(a)
    		flash.message = "The request has been approved. <a href='${request.contextPath}/customer/show/"+a.referenceNumber+"'>View Customer</a>"
    	}
    	if(a.type=="Sales Delivery"){
    		flash.message = "The request has been approved. <a href='${request.contextPath}/salesDelivery/show/"+a.referenceNumber+"'>View Sales Delivery</a>"
    	}
    	if(a.type=="Sales Order"){
    		flash.message = "The request has been approved. <a href='${request.contextPath}/salesOrder/show/"+a.referenceNumber+"'>View Sales Order</a>"
    	}
    	redirect controller:"home", action:params.view
    }
	
	def approveCustomer(a) {
		def approvalCustomer = ApprovalCustomerUpdate.findByApprovalProcess(a)
		def customer = approvalCustomer.customer
    	if(approvalCustomer.creditLimit!=null) customer.creditLimit = approvalCustomer.creditLimit
    	if(approvalCustomer.enableCreditLimit!=null) customer.enableCreditLimit = approvalCustomer.enableCreditLimit
    	if(approvalCustomer.term!=null) customer.term = approvalCustomer.term
    	if(approvalCustomer.autoApprove!=null) customer.autoApprove = approvalCustomer.autoApprove
    	if(approvalCustomer.autoSecondApprove!=null) customer.autoSecondApprove = approvalCustomer.autoSecondApprove
    	if(approvalCustomer.status!=null) {
    		def s = Customer.Status.getStatusByName(approvalCustomer.status)
    		customer.status = s
    	}
		customerService.generateAuditTrails(customer, a.requestedBy)
    	customer.save(flush:true)
	}
    
    def approveCustomerDiscount(a) {
		def approvalCustomerDiscount = ApprovalCustomerDiscount.findByApprovalProcess(a)
		def customerDiscount = approvalCustomerDiscount.customerDiscount
		if(!customerDiscount) customerDiscount = new CustomerDiscount()
		customerDiscount.customer = approvalCustomerDiscount.customer
		customerDiscount.discountType = approvalCustomerDiscount.discountType
		customerDiscount.discountGroup = approvalCustomerDiscount.discountGroup
		customerDiscount.type = CustomerDiscount.Type.getTypeByName(approvalCustomerDiscount.type)
    	customerDiscount.save(flush:true)
		customerDiscountService.logChanges(customerDiscount)
	}
            
    def reject = {
    	def a = ApprovalProcess.get(Long.parseLong(params.id))
    	a.status = "Rejected"
    	a.save(flush:true)
    	if(a.type=="Customer") {
    		flash.message = "The request has been approved. <a href='${request.contextPath}/customer/show/"+a.referenceNumber+"'>View Customer</a>"
    	}
    	if(a.type=="Sales Delivery"){
    		flash.message = "The request has been rejected. <a href='${request.contextPath}/salesDelivery/show/"+a.referenceNumber+"'>View Sales Delivery</a>"
    	}
    	redirect controller:"home", action:params.view
    }
}