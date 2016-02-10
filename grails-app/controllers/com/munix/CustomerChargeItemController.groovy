package com.munix

class CustomerChargeItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def create = {
        def customerChargeItemInstance = new CustomerChargeItem()
        customerChargeItemInstance.properties = params
        return [customerChargeItemInstance: customerChargeItemInstance]
    }

    def save = {
        def customerChargeItemInstance = new CustomerChargeItem(params)
        customerChargeItemInstance.customerCharge = CustomerCharge.get(params.id)
        customerChargeItemInstance.charge = Charge.get(params."charge.id")
        customerChargeItemInstance.amount = params.amount == "" ? null : new BigDecimal(params.amount)

        if (customerChargeItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), customerChargeItemInstance.id])}"
            redirect(action: "show", id: params.id, controller:"customerCharge")
        }
        else {
            render(view: "create", model: [customerChargeItemInstance: customerChargeItemInstance])
        }
    }

    def show = {
        def customerChargeItemInstance = CustomerChargeItem.get(params.id)
        if (customerChargeItemInstance) {
            [customerChargeItemInstance: customerChargeItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def customerChargeItemInstance = CustomerChargeItem.get(params.id)
        if (customerChargeItemInstance) {
            return [customerChargeItemInstance: customerChargeItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def customerChargeItemInstance = CustomerChargeItem.get(params.id)
        if (customerChargeItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (customerChargeItemInstance.version > version) {
                    
                    customerChargeItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem')] as Object[], "Another user has updated this CustomerChargeItem while you were editing")
                    render(view: "edit", model: [customerChargeItemInstance: customerChargeItemInstance])
                    return
                }
            }
            customerChargeItemInstance.properties = params
            if (!customerChargeItemInstance.hasErrors() && customerChargeItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), customerChargeItemInstance.id])}"
                redirect(action: "show", controller:"customerCharge", id:customerChargeItemInstance.customerCharge.id)
            }
            else {
                render(view: "edit", model: [customerChargeItemInstance: customerChargeItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def customerChargeItemInstance = CustomerChargeItem.get(params.id)
        def customerChargeId = customerChargeItemInstance.customerCharge.id
        if (customerChargeItemInstance) {
            try {
                customerChargeItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), params.id])}"
                redirect(action: "show", controller:"customerCharge", id:customerChargeId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerChargeItem.label', default: 'CustomerChargeItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
