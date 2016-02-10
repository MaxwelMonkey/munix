package com.munix

class DiscountGroupController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def addCustomer = {
        def customerInstance = Customer.get(params.customer.id)
        def discountGroupInstance = DiscountGroup.get(params.id)
        if(customerInstance) {
            if(customerInstance in discountGroupInstance.customers) {
                flash.message= "${customerInstance} is already on the list!"
            }
            else {
                discountGroupInstance.addToCustomers(customerInstance)
                discountGroupInstance.save()
                flash.message= "${customerInstance} added successfully!"
            }
            redirect (action:"show", id:discountGroupInstance.id)
        }
    }

    def removeCustomer = {
        def customer = Customer.get(params.id)
        def discountGroup = DiscountGroup.get(params.discountGroupId)
        
        discountGroup.removeFromCustomers(customer)
        discountGroup.save()
        flash.message= "${customer} removed from the group!"
        redirect (action:"show", id:discountGroup.id)
    }

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [discountGroupInstanceList: DiscountGroup.list(params), discountGroupInstanceTotal: DiscountGroup.count()]
    }

    def create = {
        def discountGroupInstance = new DiscountGroup()
        discountGroupInstance.properties = params
        return [discountGroupInstance: discountGroupInstance]
    }

    def save = {
        def discountGroupInstance = new DiscountGroup(params)

        if (discountGroupInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'discountGroup.label', default: 'DiscountGroup'), discountGroupInstance.id])}"
            redirect(action: "show", id: discountGroupInstance.id)
        }
        else {
            render(view: "create", model: [discountGroupInstance: discountGroupInstance])
        }
    }

    def show = {
        def discountGroupInstance = DiscountGroup.get(params.id)
        if (discountGroupInstance) {
            [discountGroupInstance: discountGroupInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'discountGroup.label', default: 'DiscountGroup'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def discountGroupInstance = DiscountGroup.get(params.id)
        if (discountGroupInstance) {
            return [discountGroupInstance: discountGroupInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'discountGroup.label', default: 'DiscountGroup'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def discountGroupInstance = DiscountGroup.get(params.id)
        if (discountGroupInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (discountGroupInstance.version > version) {
                    
                    discountGroupInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'discountGroup.label', default: 'DiscountGroup')] as Object[], "Another user has updated this DiscountGroup while you were editing")
                    render(view: "edit", model: [discountGroupInstance: discountGroupInstance])
                    return
                }
            }
            discountGroupInstance.properties = params
            if (!discountGroupInstance.hasErrors() && discountGroupInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'discountGroup.label', default: 'DiscountGroup'), discountGroupInstance.id])}"
                redirect(action: "show", id: discountGroupInstance.id)
            }
            else {
                render(view: "edit", model: [discountGroupInstance: discountGroupInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'discountGroup.label', default: 'DiscountGroup'), params.id])}"
            redirect(action: "list")
        }
    }
}
