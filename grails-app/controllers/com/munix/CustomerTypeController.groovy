package com.munix

class CustomerTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [customerTypeInstanceList: CustomerType.list(params), customerTypeInstanceTotal: CustomerType.count()]
    }

    def create = {
        def customerTypeInstance = new CustomerType()
        customerTypeInstance.properties = params
        return [customerTypeInstance: customerTypeInstance]
    }

    def save = {
        def customerTypeInstance = new CustomerType(params)
        if (customerTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'customerType.label', default: 'CustomerType'), customerTypeInstance.id])}"
            redirect(action: "show", id: customerTypeInstance.id)
        }
        else {
            render(view: "create", model: [customerTypeInstance: customerTypeInstance])
        }
    }

    def show = {
        def customerTypeInstance = CustomerType.get(params.id)
        if (customerTypeInstance) {
            [customerTypeInstance: customerTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerType.label', default: 'CustomerType'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def customerTypeInstance = CustomerType.get(params.id)
        if (customerTypeInstance) {
            return [customerTypeInstance: customerTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerType.label', default: 'CustomerType'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def customerTypeInstance = CustomerType.get(params.id)
        if (customerTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (customerTypeInstance.version > version) {
                    
                    customerTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'customerType.label', default: 'CustomerType')] as Object[], "Another user has updated this CustomerType while you were editing")
                    render(view: "edit", model: [customerTypeInstance: customerTypeInstance])
                    return
                }
            }
            customerTypeInstance.properties = params
            if (!customerTypeInstance.hasErrors() && customerTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'customerType.label', default: 'CustomerType'), customerTypeInstance.id])}"
                redirect(action: "show", id: customerTypeInstance.id)
            }
            else {
                render(view: "edit", model: [customerTypeInstance: customerTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customerType.label', default: 'CustomerType'), params.id])}"
            redirect(action: "list")
        }
    }

}
