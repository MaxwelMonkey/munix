package com.munix

class TripTicketItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
   

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [tripTicketItemInstanceList: TripTicketItem.list(params), tripTicketItemInstanceTotal: TripTicketItem.count()]
    }

    def create = {
        def tripTicketItemInstance = new TripTicketItem()
        tripTicketItemInstance.properties = params
        return [tripTicketItemInstance: tripTicketItemInstance]
    }

    def save = {
        def tripTicketItemInstance = new TripTicketItem(params)
    
        if (tripTicketItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), tripTicketItemInstance.id])}"
            redirect(action: "show", id: tripTicketItemInstance.id)
        }
        else {
            render(view: "create", model: [tripTicketItemInstance: tripTicketItemInstance])
        }
    }

    def show = {
        def tripTicketItemInstance = TripTicketItem.get(params.id)
        if (!tripTicketItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [tripTicketItemInstance: tripTicketItemInstance]
        }
    }

    def edit = {
        def tripTicketItemInstance = TripTicketItem.get(params.id)
        if (!tripTicketItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [tripTicketItemInstance: tripTicketItemInstance]
        }
    }

    def update = {
        def tripTicketItemInstance = TripTicketItem.get(params.id)
        if (tripTicketItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (tripTicketItemInstance.version > version) {
                    
                    tripTicketItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'tripTicketItem.label', default: 'TripTicketItem')] as Object[], "Another user has updated this TripTicketItem while you were editing")
                    render(view: "edit", model: [tripTicketItemInstance: tripTicketItemInstance])
                    return
                }
            }
            tripTicketItemInstance.properties = params
            if (!tripTicketItemInstance.hasErrors() && tripTicketItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), tripTicketItemInstance.id])}"
                redirect(action: "show", controller: "tripTicket", id: tripTicketItemInstance?.tripTicket?.id)
            }
            else {
                render(view: "edit", model: [tripTicketItemInstance: tripTicketItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def tripTicketItemInstance = TripTicketItem.get(params.id)
        if (tripTicketItemInstance) {
            try {
                tripTicketItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tripTicketItem.label', default: 'TripTicketItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
