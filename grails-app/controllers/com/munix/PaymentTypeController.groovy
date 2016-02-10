package com.munix

class PaymentTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [paymentTypeInstanceList: PaymentType.list(params), paymentTypeInstanceTotal: PaymentType.count()]
    }

    def create = {
        def paymentTypeInstance = new PaymentType()
        paymentTypeInstance.properties = params
        return [paymentTypeInstance: paymentTypeInstance]
    }

    def save = {
        def paymentTypeInstance = new PaymentType(params)

        if (paymentTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'paymentType.label', default: 'PaymentType'), paymentTypeInstance.id])}"
            redirect(action: "show", id: paymentTypeInstance.id)
        }
        else {
            render(view: "create", model: [paymentTypeInstance: paymentTypeInstance])
        }
    }

    def show = {
        def paymentTypeInstance = PaymentType.get(params.id)
        if (paymentTypeInstance) {
            [paymentTypeInstance: paymentTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'paymentType.label', default: 'PaymentType'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def paymentTypeInstance = PaymentType.get(params.id)
        if (paymentTypeInstance) {
            return [paymentTypeInstance: paymentTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'paymentType.label', default: 'PaymentType'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def paymentTypeInstance = PaymentType.get(params.id)
        if (paymentTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (paymentTypeInstance.version > version) {
                    
                    paymentTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'paymentType.label', default: 'PaymentType')] as Object[], "Another user has updated this PaymentType while you were editing")
                    render(view: "edit", model: [paymentTypeInstance: paymentTypeInstance])
                    return
                }
            }
            paymentTypeInstance.properties = params
            if (!paymentTypeInstance.hasErrors() && paymentTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'paymentType.label', default: 'PaymentType'), paymentTypeInstance.id])}"
                redirect(action: "show", id: paymentTypeInstance.id)
            }
            else {
                render(view: "edit", model: [paymentTypeInstance: paymentTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'paymentType.label', default: 'PaymentType'), params.id])}"
            redirect(action: "list")
        }
    }

}
