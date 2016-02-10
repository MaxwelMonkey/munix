package com.munix

class CurrencyTypeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [currencyTypeInstanceList: CurrencyType.list(params), currencyTypeInstanceTotal: CurrencyType.count()]
    }

    def create = {
        def currencyTypeInstance = new CurrencyType()
        currencyTypeInstance.properties = params
        return [currencyTypeInstance: currencyTypeInstance]
    }

    def save = {
        def currencyTypeInstance = new CurrencyType(params)
        if (currencyTypeInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'currencyType.label', default: 'CurrencyType'), currencyTypeInstance.id])}"
            redirect(action: "show", id: currencyTypeInstance.id)
        }
        else {
            render(view: "create", model: [currencyTypeInstance: currencyTypeInstance])
        }
    }

    def show = {
        def currencyTypeInstance = CurrencyType.get(params.id)
        if (currencyTypeInstance) {
            [currencyTypeInstance: currencyTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'currencyType.label', default: 'CurrencyType'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def currencyTypeInstance = CurrencyType.get(params.id)
        if (currencyTypeInstance) {
            return [currencyTypeInstance: currencyTypeInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'currencyType.label', default: 'CurrencyType'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def currencyTypeInstance = CurrencyType.get(params.id)
        if (currencyTypeInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (currencyTypeInstance.version > version) {
                    
                    currencyTypeInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'currencyType.label', default: 'CurrencyType')] as Object[], "Another user has updated this CurrencyType while you were editing")
                    render(view: "edit", model: [currencyTypeInstance: currencyTypeInstance])
                    return
                }
            }
            currencyTypeInstance.properties = params
            if (!currencyTypeInstance.hasErrors() && currencyTypeInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'currencyType.label', default: 'CurrencyType'), currencyTypeInstance.id])}"
                redirect(action: "show", id: currencyTypeInstance.id)
            }
            else {
                render(view: "edit", model: [currencyTypeInstance: currencyTypeInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'currencyType.label', default: 'CurrencyType'), params.id])}"
            redirect(action: "list")
        }
    }

}
