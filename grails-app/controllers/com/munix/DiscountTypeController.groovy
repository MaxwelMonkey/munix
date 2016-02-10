package com.munix

class DiscountTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [discountTypeInstanceList: DiscountType.list(params), discountTypeInstanceTotal: DiscountType.count()]
    }

    def create = {
        def discountTypeInstance = new DiscountType()
        discountTypeInstance.properties = params
        return [discountTypeInstance: discountTypeInstance]
    }

    def save = {
        def discountTypeInstance = new DiscountType(params)
        if (discountTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'discountType.label', default: 'DiscountType'), discountTypeInstance.id])}"
            redirect(action: "show", id: discountTypeInstance.id)
        }
        else {
            render(view: "create", model: [discountTypeInstance: discountTypeInstance])
        }
    }

    def show = {
        def discountTypeInstance = DiscountType.get(params.id)
        if (discountTypeInstance) {
            [discountTypeInstance: discountTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'discountType.label', default: 'DiscountType'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def discountTypeInstance = DiscountType.get(params.id)
        if (discountTypeInstance) {
            return [discountTypeInstance: discountTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'discountType.label', default: 'DiscountType'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def discountTypeInstance = DiscountType.get(params.id)
        if (discountTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (discountTypeInstance.version > version) {
                    
                    discountTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'discountType.label', default: 'DiscountType')] as Object[], "Another user has updated this DiscountType while you were editing")
                    render(view: "edit", model: [discountTypeInstance: discountTypeInstance])
                    return
                }
            }
            discountTypeInstance.properties = params
            if (!discountTypeInstance.hasErrors() && discountTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'discountType.label', default: 'DiscountType'), discountTypeInstance.id])}"
                redirect(action: "show", id: discountTypeInstance.id)
            }
            else {
                render(view: "edit", model: [discountTypeInstance: discountTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'discountType.label', default: 'DiscountType'), params.id])}"
            redirect(action: "list")
        }
    }

}
