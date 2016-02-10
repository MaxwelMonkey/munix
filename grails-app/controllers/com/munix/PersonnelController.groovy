package com.munix

class PersonnelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [personnelInstanceList: Personnel.list(params), personnelInstanceTotal: Personnel.count()]
    }

    def create = {
        def personnelInstance = new Personnel()
        personnelInstance.properties = params
        return [personnelInstance: personnelInstance]
    }

    def save = {
        def personnelInstance = new Personnel(params)
        if (personnelInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'personnel.label', default: 'Personnel'), personnelInstance.id])}"
            redirect(action: "show", id: personnelInstance.id)
        }
        else {
            render(view: "create", model: [personnelInstance: personnelInstance])
        }
    }

    def show = {
        def personnelInstance = Personnel.get(params.id)
        if (personnelInstance) {
            [personnelInstance: personnelInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'personnel.label', default: 'Personnel'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def personnelInstance = Personnel.get(params.id)
        if (personnelInstance) {
            return [personnelInstance: personnelInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'personnel.label', default: 'Personnel'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def personnelInstance = Personnel.get(params.id)
        if (personnelInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (personnelInstance.version > version) {
                    
                    personnelInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'personnel.label', default: 'Personnel')] as Object[], "Another user has updated this Personnel while you were editing")
                    render(view: "edit", model: [personnelInstance: personnelInstance])
                    return
                }
            }
            personnelInstance.properties = params
            if (!personnelInstance.hasErrors() && personnelInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'personnel.label', default: 'Personnel'), personnelInstance.id])}"
                redirect(action: "show", id: personnelInstance.id)
            }
            else {
                render(view: "edit", model: [personnelInstance: personnelInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'personnel.label', default: 'Personnel'), params.id])}"
            redirect(action: "list")
        }
    }

}
