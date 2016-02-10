package com.munix

class CollectorController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [collectorInstanceList: Collector.list(params), collectorInstanceTotal: Collector.count()]
    }

    def create = {
        def collectorInstance = new Collector()
        collectorInstance.properties = params
        return [collectorInstance: collectorInstance]
    }

    def save = {
        def collectorInstance = new Collector(params)
        if (collectorInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'collector.label', default: 'Collector'), collectorInstance.id])}"
            redirect(action: "show", id: collectorInstance.id)
        }
        else {
            render(view: "create", model: [collectorInstance: collectorInstance])
        }
    }

    def show = {
        def collectorInstance = Collector.get(params.id)
        if (collectorInstance) {
            [collectorInstance: collectorInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collector.label', default: 'Collector'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def collectorInstance = Collector.get(params.id)
        if (collectorInstance) {
            return [collectorInstance: collectorInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collector.label', default: 'Collector'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def collectorInstance = Collector.get(params.id)
        if (collectorInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (collectorInstance.version > version) {
                    
                    collectorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'collector.label', default: 'Collector')] as Object[], "Another user has updated this Collector while you were editing")
                    render(view: "edit", model: [collectorInstance: collectorInstance])
                    return
                }
            }
            collectorInstance.properties = params
            if (!collectorInstance.hasErrors() && collectorInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'collector.label', default: 'Collector'), collectorInstance.id])}"
                redirect(action: "show", id: collectorInstance.id)
            }
            else {
                render(view: "edit", model: [collectorInstance: collectorInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collector.label', default: 'Collector'), params.id])}"
            redirect(action: "list")
        }
    }

}
