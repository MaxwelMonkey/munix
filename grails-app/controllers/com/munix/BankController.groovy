package com.munix

class BankController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [bankInstanceList: Bank.list(params), bankInstanceTotal: Bank.count()]
    }

    def create = {
        def bankInstance = new Bank()
        bankInstance.properties = params
        return [bankInstance: bankInstance]
    }

    def save = {
        def bankInstance = new Bank(params)
        if (bankInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'bank.label', default: 'Bank'), bankInstance.id])}"
            redirect(action: "show", id: bankInstance.id)
        }
        else {
            render(view: "create", model: [bankInstance: bankInstance])
        }
    }

    def show = {
        def bankInstance = Bank.get(params.id)
        if (bankInstance) {
            [bankInstance: bankInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bank.label', default: 'Bank'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def bankInstance = Bank.get(params.id)
        if (bankInstance) {
            return [bankInstance: bankInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bank.label', default: 'Bank'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def bankInstance = Bank.get(params.id)
        if (bankInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (bankInstance.version > version) {
                    
                    bankInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'bank.label', default: 'Bank')] as Object[], "Another user has updated this Bank while you were editing")
                    render(view: "edit", model: [bankInstance: bankInstance])
                    return
                }
            }
            bankInstance.properties = params
            if (!bankInstance.hasErrors() && bankInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'bank.label', default: 'Bank'), bankInstance.id])}"
                redirect(action: "show", id: bankInstance.id)
            }
            else {
                render(view: "edit", model: [bankInstance: bankInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bank.label', default: 'Bank'), params.id])}"
            redirect(action: "list")
        }
    }

}
