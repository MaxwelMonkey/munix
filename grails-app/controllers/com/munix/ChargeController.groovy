package com.munix

class ChargeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [chargeInstanceList: Charge.list(params), chargeInstanceTotal: Charge.count()]
    }

    def create = {
        def chargeInstance = new Charge()
        chargeInstance.properties = params
        return [chargeInstance: chargeInstance]
    }

    def save = {
        def chargeInstance = new Charge(params)
        if (chargeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'charge.label', default: 'Charge'), chargeInstance.id])}"
            redirect(action: "show", id: chargeInstance.id)
        }
        else {
            render(view: "create", model: [chargeInstance: chargeInstance])
        }
    }

    def show = {
        def chargeInstance = Charge.get(params.id)
        if (chargeInstance) {
            [chargeInstance: chargeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'charge.label', default: 'Charge'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def chargeInstance = Charge.get(params.id)
        if (chargeInstance) {
            return [chargeInstance: chargeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'charge.label', default: 'Charge'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def chargeInstance = Charge.get(params.id)
        if (chargeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (chargeInstance.version > version) {
                    
                    chargeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'charge.label', default: 'Charge')] as Object[], "Another user has updated this Charge while you were editing")
                    render(view: "edit", model: [chargeInstance: chargeInstance])
                    return
                }
            }
            chargeInstance.properties = params
            if (!chargeInstance.hasErrors() && chargeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'charge.label', default: 'Charge'), chargeInstance.id])}"
                redirect(action: "show", id: chargeInstance.id)
            }
            else {
                render(view: "edit", model: [chargeInstance: chargeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'charge.label', default: 'Charge'), params.id])}"
            redirect(action: "list")
        }
    }

}
