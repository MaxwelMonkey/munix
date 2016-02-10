package com.munix

class CheckTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [checkTypeInstanceList: CheckType.list(params), checkTypeInstanceTotal: CheckType.count()]
    }

    def create = {
        def checkTypeInstance = new CheckType()
        checkTypeInstance.properties = params
        return [checkTypeInstance: checkTypeInstance]
    }

    def save = {
        def checkTypeInstance = new CheckType(params)
        if (checkTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'checkType.label', default: 'CheckType'), checkTypeInstance.id])}"
            redirect(action: "show", id: checkTypeInstance.id)
        }
        else {
            render(view: "create", model: [checkTypeInstance: checkTypeInstance])
        }
    }

    def show = {
        def checkTypeInstance = CheckType.get(params.id)
        if (checkTypeInstance) {
            [checkTypeInstance: checkTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkType.label', default: 'CheckType'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def checkTypeInstance = CheckType.get(params.id)
        if (checkTypeInstance) {
            return [checkTypeInstance: checkTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkType.label', default: 'CheckType'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def checkTypeInstance = CheckType.get(params.id)
        if (checkTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (checkTypeInstance.version > version) {
                    
                    checkTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'checkType.label', default: 'CheckType')] as Object[], "Another user has updated this CheckType while you were editing")
                    render(view: "edit", model: [checkTypeInstance: checkTypeInstance])
                    return
                }
            }
            checkTypeInstance.properties = params
            if (!checkTypeInstance.hasErrors() && checkTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'checkType.label', default: 'CheckType'), checkTypeInstance.id])}"
                redirect(action: "show", id: checkTypeInstance.id)
            }
            else {
                render(view: "edit", model: [checkTypeInstance: checkTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkType.label', default: 'CheckType'), params.id])}"
            redirect(action: "list")
        }
    }

}
