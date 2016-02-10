package com.munix

class ReasonController {

        static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [reasonInstanceList: Reason.list(params), reasonInstanceTotal: Reason.count()]
    }

    def create = {
        def reasonInstance = new Reason()
        reasonInstance.properties = params
        return [reasonInstance: reasonInstance]
    }

    def save = {
        def reasonInstance = new Reason(params)
        if (reasonInstance.save()) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'reason.label', default: 'Reason'), reasonInstance.id])}"
            redirect(action: "show", id: reasonInstance.id)
        }
        else {
            render(view: "create", model: [reasonInstance: reasonInstance])
        }
    }

    def show = {
        def reasonInstance = Reason.get(params.id)
        if (reasonInstance) {
            [reasonInstance: reasonInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reason.label', default: 'Reason'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def reasonInstance = Reason.get(params.id)
        if (reasonInstance) {
            return [reasonInstance: reasonInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reason.label', default: 'Reason'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def reasonInstance = Reason.get(params.id)
        if (reasonInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (reasonInstance.version > version) {
                    
                    reasonInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'reason.label', default: 'Reason')] as Object[], "Another user has updated this Reason while you were editing")
                    render(view: "edit", model: [reasonInstance: reasonInstance])
                    return
                }
            }
            reasonInstance.properties = params
            if (!reasonInstance.hasErrors() && reasonInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'reason.label', default: 'Reason'), reasonInstance.id])}"
                redirect(action: "show", id: reasonInstance.id)
            }
            else {
                render(view: "edit", model: [reasonInstance: reasonInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'reason.label', default: 'Reason'), params.id])}"
            redirect(action: "list")
        }
    }
}
