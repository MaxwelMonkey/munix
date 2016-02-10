package com.munix

class TruckController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [truckInstanceList: Truck.list(params), truckInstanceTotal: Truck.count()]
    }

    def create = {
        def truckInstance = new Truck()
        truckInstance.properties = params
        return [truckInstance: truckInstance]
    }

    def save = {
        def truckInstance = new Truck(params)
        if (truckInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'truck.label', default: 'Truck'), truckInstance.id])}"
            redirect(action: "show", id: truckInstance.id)
        }
        else {
            render(view: "create", model: [truckInstance: truckInstance])
        }
    }

    def show = {
        def truckInstance = Truck.get(params.id)
        if (!truckInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'truck.label', default: 'Truck'), params.id])}"
            redirect(action: "list")
        }
        else {
            [truckInstance: truckInstance]
        }
    }

    def edit = {
        def truckInstance = Truck.get(params.id)
        if (!truckInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'truck.label', default: 'Truck'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [truckInstance: truckInstance]
        }
    }

    def update = {
        def truckInstance = Truck.get(params.id)
        if (truckInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (truckInstance.version > version) {
                    
                    truckInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'truck.label', default: 'Truck')] as Object[], "Another user has updated this Truck while you were editing")
                    render(view: "edit", model: [truckInstance: truckInstance])
                    return
                }
            }
            truckInstance.properties = params
            if (!truckInstance.hasErrors() && truckInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'truck.label', default: 'Truck'), truckInstance.id])}"
                redirect(action: "show", id: truckInstance.id)
            }
            else {
                render(view: "edit", model: [truckInstance: truckInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'truck.label', default: 'Truck'), params.id])}"
            redirect(action: "list")
        }
    }

}
