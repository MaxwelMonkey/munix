package com.munix

class CustomerDiscountController {
    def authenticateService
	def customerDiscountService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = 100
		params.sort = "customer.identifier"
        [customerDiscountInstanceList: CustomerDiscount.list(params), customerDiscountInstanceTotal: CustomerDiscount.count()]
    }

    def create = {
        def customerDiscountInstance = new CustomerDiscount()
        customerDiscountInstance.properties = params
        return [customerDiscountInstance: customerDiscountInstance]
    }

    def save = {
    	params.type = CustomerDiscount.Type.getTypeByName(params.type)
        def customerDiscountInstance = new CustomerDiscount(params)
        customerDiscountInstance.customer = Customer.get(params.id)

        if (customerDiscountService.validateCustomerDiscount(customerDiscountInstance)) {
        	if(needsApproval()){
        		def approvalCustomerDiscount = createApproval(customerDiscountInstance, null)
                flash.message = "Customer Discount needs to be approved before it takes effect: " + approvalCustomerDiscount.toString()
	            redirect(action: "show", id: params.id, controller:"customer")
        	}else{
	            if (customerDiscountInstance.save(flush: true)) {
					customerDiscountService.logChanges(customerDiscountInstance) 
	
	                flash.message = "${message(code: 'default.created.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), customerDiscountInstance.id])}"
	                redirect(action: "show", id: params.id, controller:"customer")
	            } else {
	                render(view: "create", model: [customerDiscountInstance: customerDiscountInstance])
	            }
            }
        } else {
        	flash.message = "Duplicate discount type already exists!"
    		redirect(action: "show", id: params.id, controller:"customer")
        }
    }
    


    def edit = {
        def customerDiscountInstance = CustomerDiscount.get(params.id)
        if (customerDiscountInstance) {
            return [customerDiscountInstance: customerDiscountInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), params.id])}"
            redirect(action: "list")
        }
    }


    def update = {
        def customerDiscountInstance = CustomerDiscount.get(params.id)		
        if (customerDiscountInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (customerDiscountInstance.version > version) {
                    customerDiscountInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'customerDiscount.label', default: 'CustomerDiscount')] as Object[], "Another user has updated this CustomerDiscount while you were editing")
                    render(view: "edit", model: [customerDiscountInstance: customerDiscountInstance])
                    return
                }
            }
            params.type = CustomerDiscount.Type.getTypeByName(params.type)
			CustomerDiscount tempCustomerDiscount = new CustomerDiscount(customer: customerDiscountInstance.customer)
			tempCustomerDiscount.properties = params
			
			if (customerDiscountService.validateUpdatedCustomerDiscount(tempCustomerDiscount, customerDiscountInstance)) {
				if (tempCustomerDiscount.validate()) {
		        	if(needsApproval()){
		        		def approvalCustomerDiscount = createApproval(tempCustomerDiscount, customerDiscountInstance)
		                flash.message = "Customer Discount needs to be approved before it takes effect: " + approvalCustomerDiscount.toString()
			            redirect(action: "show", id: customerDiscountInstance.customer?.id, controller:"customer")
			            return
		        	}else{
						customerDiscountInstance.properties = params
					}
				} else {
					render(view: "edit", model: [customerDiscountInstance: customerDiscountInstance])
				}
	            if (!customerDiscountInstance.hasErrors() && customerDiscountInstance.save(flush: true)) {
					customerDiscountService.logChanges(customerDiscountInstance)
	                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), customerDiscountInstance.id])}"
	                redirect(action: "show", controller:"customer", id: customerDiscountInstance.customer.id)
	            } else {
	                render(view: "edit", model: [customerDiscountInstance: customerDiscountInstance])
	            }
			} else {
				flash.message = "Duplicate discount type already exists!"
				redirect(action: "show", id: customerDiscountInstance.customer.id, controller:"customer")
			}
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), params.id])}"
            redirect(action: "list")
        }
    }
    
    def needsApproval = {
    	authenticateService.ifNotGranted("ROLE_SUPER")
    }

    def createApproval(customerDiscountInstance, existingCustomerDiscount){
		def user = authenticateService.userDomain()
    	def customerInstance = customerDiscountInstance.customer
        def approvalCustomerDiscount = new ApprovalCustomerDiscount()
        approvalCustomerDiscount.customerDiscount = existingCustomerDiscount
    	approvalCustomerDiscount.customer = customerInstance
    	approvalCustomerDiscount.discountType = customerDiscountInstance.discountType
    	approvalCustomerDiscount.discountGroup = customerDiscountInstance.discountGroup
    	approvalCustomerDiscount.type = customerDiscountInstance.type
	    def msg = "${user.userRealName} wants to add Discount for Customer ${customerInstance.identifier} - ${customerInstance.name}:"
	    msg+=approvalCustomerDiscount
    	def approval = new ApprovalProcess(description:msg, requestedBy:user, date:new Date(), type:"Customer Discount", referenceNumber:customerInstance.id)
        approval.save(flush:true)
    	approvalCustomerDiscount.approvalProcess = approval
    	approvalCustomerDiscount.save()
    	approvalCustomerDiscount
    }
	
	
    def delete = {
        def customerDiscountInstance = CustomerDiscount.get(params.id)
        if (customerDiscountInstance) {
            try {
            	def acd = ApprovalCustomerDiscount.findByCustomerDiscount(customerDiscountInstance)
            	acd?.delete()
                customerDiscountInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), params.id])}"
                redirect(action: "show", controller:"customer", id:customerDiscountInstance.customer.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), params.id])}"
            redirect(action: "list")
        }
    }
}
