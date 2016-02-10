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
            if (customerDiscountInstance.save(flush: true)) {
				customerDiscountInstance.log = new CustomerDiscountLog(customer: customerDiscountInstance.customer, discount: customerDiscountInstance)
                createCustomerDiscountLogItem(customerDiscountInstance.log)

                flash.message = "${message(code: 'default.created.message', args: [message(code: 'customerDiscount.label', default: 'CustomerDiscount'), customerDiscountInstance.id])}"
                redirect(action: "show", id: params.id, controller:"customer")
            } else {
                render(view: "create", model: [customerDiscountInstance: customerDiscountInstance])
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
					customerDiscountInstance.properties = params
				} else {
					render(view: "edit", model: [customerDiscountInstance: customerDiscountInstance])
				}
	            if (!customerDiscountInstance.hasErrors() && customerDiscountInstance.save(flush: true)) {
					logChanges(customerDiscountInstance)
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
	
	private void logChanges(CustomerDiscount customerDiscountInstance) {
		if(!customerDiscountInstance.log){
			customerDiscountInstance.log = new CustomerDiscountLog(customer: customerDiscountInstance.customer, discount: customerDiscountInstance)
			createCustomerDiscountLogItem(customerDiscountInstance.log)
		}
		if (hasCustomerDiscountChanged(customerDiscountInstance)){
			createCustomerDiscountLogItem(customerDiscountInstance.log)
		}
	}
	
	private boolean hasCustomerDiscountChanged(CustomerDiscount customerDiscountInstance) {
		return customerDiscountInstance.discountGroup != customerDiscountInstance.log.currentLog.discountGroup || 
			customerDiscountInstance.discountType != customerDiscountInstance.log.currentLog.discountType || 
			customerDiscountInstance.type != customerDiscountInstance.log.currentLog.type
	}

    private def createCustomerDiscountLogItem(CustomerDiscountLog discountLog) {
        def log = new CustomerDiscountLogItem(user: authenticateService.userDomain(), 
			date: new Date(), 
			discountGroup: discountLog.discount.discountGroup, 
			discountType: discountLog.discount.discountType,
			type: discountLog.discount.type)
        discountLog.addToItems(log)
		discountLog.save()
    }

    def delete = {
        def customerDiscountInstance = CustomerDiscount.get(params.id)
        if (customerDiscountInstance) {
            try {
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
