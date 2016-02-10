package com.munix

class CustomerChargeController {

    static allowedMethods = [save: "POST", update: "POST", approve: "POST", unapprove: "POST"]
	def customerChargeService
    def authenticateService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

        def customerChargeInstanceList = customerChargeService.generateList(params)
		def dateMap = customerChargeService.generateDateStructList(params)
        [customerChargeInstanceList:customerChargeInstanceList, customerChargeInstanceTotal: customerChargeInstanceList.totalCount, dateMap: dateMap]
    }

	def unpaidList = {
		Customer customerInstance = Customer.get(params.customerId)
		if(customerInstance){
			if (!params.max) params.max = 100
			if (!params.offset) params.offset = 0
			if (!params.sort) params.sort = "id"
			if (!params.order) params.order = "asc"
			
			def unpaidCustomerCharges = customerChargeService.getUnpaidCustomerCharges(customerInstance, params)
			def totalUnpaidCustomerCharges = customerChargeService.computeTotalUnpaidCustomerCharges(customerInstance)
			
			return [customerChargeInstanceList:unpaidCustomerCharges, totalUnpaidCustomerCharges: totalUnpaidCustomerCharges, customerChargeInstanceTotal: unpaidCustomerCharges.totalCount, customerInstance: customerInstance]
		}else{
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])}"
			redirect(action: "accountsSummary" ,controller: "customer")
		}
	}
	
    def create = {
        def customerChargeInstance = new CustomerCharge()
        customerChargeInstance.properties = params
        return [customerChargeInstance: customerChargeInstance]
    }

    def save = {
        def customerChargeInstance = new CustomerCharge(params)
		customerChargeInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        if (customerChargeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'customerCharge.label', default: 'CustomerCharge'), customerChargeInstance.id])}"
            redirect(action: "show", id: customerChargeInstance.id)
        }
        else {
            render(view: "create", model: [customerChargeInstance: customerChargeInstance])
        }
    }

    def show = {
        def customerChargeInstance = CustomerCharge.get(params.id)
        if (customerChargeInstance) {
            [customerChargeInstance: customerChargeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerCharge.label', default: 'CustomerCharge'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def customerChargeInstance = CustomerCharge.get(params.id)
        if (customerChargeInstance) {
            return [customerChargeInstance: customerChargeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerCharge.label', default: 'CustomerCharge'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def customerChargeInstance = CustomerCharge.get(params.id)
        if (customerChargeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (customerChargeInstance.version > version) {
                    
                    customerChargeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'customerCharge.label', default: 'CustomerCharge')] as Object[], "Another user has updated this CustomerCharge while you were editing")
                    render(view: "edit", model: [customerChargeInstance: customerChargeInstance])
                    return
                }
            }
            customerChargeInstance.properties = params
            if (!customerChargeInstance.hasErrors() && customerChargeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'customerCharge.label', default: 'CustomerCharge'), customerChargeInstance.id])}"
                redirect(action: "show", id: customerChargeInstance.id)
            }
            else {
                render(view: "edit", model: [customerChargeInstance: customerChargeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerCharge.label', default: 'CustomerCharge'), params.id])}"
            redirect(action: "list")
        }
    }

    def approve = {
        def customerChargeInstance = CustomerCharge.get(params.id)
		if (customerChargeInstance?.items.isEmpty()) {
			flash.error = "items cannot be empty!"
		} else {
            if (customerChargeService.approveCustomerCharge(customerChargeInstance)) {
				flash.message = "Customer Charge has been successfully approved!"
            }else{
                flash.message = "An error occurred while customer charge is being approved!"
            }
		}
        redirect(action:'show',id:customerChargeInstance?.id)
    }

    def unapprove = {
        def customerChargeInstance = CustomerCharge.get(params.id)
		if(customerChargeInstance.isUnapprovable()) {
			if (customerChargeService.unapproveCustomerCharge(customerChargeInstance)) {
				flash.message = "Customer Charge has been successfully unapproved!"
			}else{
				flash.error = "An error occurred while customer charge is being unapproved!"
			}
		}else{
			flash.error = "Customer Charge can't be unapproved because it is already taken by direct payment!"
		}
        redirect(action:'show',id:customerChargeInstance?.id)
    }
	
	def cancel = {
		def customerChargeInstance = CustomerCharge.get(params.id)
		if(customerChargeInstance?.isCancelable()) {
			customerChargeInstance?.cancel()
			customerChargeInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			customerChargeInstance?.save()
			flash.message = "Customer charge has now been cancelled!"
		}else {
			flash.error = "Customer Charge can't be cancelled because it has at least one Direct Payment!"
		}
		redirect(action:'show',id:customerChargeInstance?.id)
	}
	
}
