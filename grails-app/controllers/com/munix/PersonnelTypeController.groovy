package com.munix

class PersonnelTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [personnelTypeInstanceList: PersonnelType.list(params), personnelTypeInstanceTotal: PersonnelType.count()]
    }

    def create = {
        def personnelTypeInstance = new PersonnelType()
        personnelTypeInstance.properties = params
        return [personnelTypeInstance: personnelTypeInstance]
    }

    def save = {
        def personnelTypeInstance = new PersonnelType(params)
        if (personnelTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'personnelType.label', default: 'PersonnelType'), personnelTypeInstance.id])}"
            redirect(action: "show", id: personnelTypeInstance.id)
        }
        else {
            render(view: "create", model: [personnelTypeInstance: personnelTypeInstance])
        }
    }

    def show = {
        def personnelTypeInstance = PersonnelType.get(params.id)
        if (personnelTypeInstance) {
            [personnelTypeInstance: personnelTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'personnelType.label', default: 'PersonnelType'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def personnelTypeInstance = PersonnelType.get(params.id)
        if (personnelTypeInstance) {
            return [personnelTypeInstance: personnelTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'personnelType.label', default: 'PersonnelType'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def personnelTypeInstance = PersonnelType.get(params.id)
        if (personnelTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (personnelTypeInstance.version > version) {
                    
                    personnelTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'personnelType.label', default: 'PersonnelType')] as Object[], "Another user has updated this PersonnelType while you were editing")
                    render(view: "edit", model: [personnelTypeInstance: personnelTypeInstance])
                    return
                }
            }
            personnelTypeInstance.properties = params
            if (!personnelTypeInstance.hasErrors() && personnelTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'personnelType.label', default: 'PersonnelType'), personnelTypeInstance.id])}"
                redirect(action: "show", id: personnelTypeInstance.id)
            }
            else {
                render(view: "edit", model: [personnelTypeInstance: personnelTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'personnelType.label', default: 'PersonnelType'), params.id])}"
            redirect(action: "list")
        }
    }

}
