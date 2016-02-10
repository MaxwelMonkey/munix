package com.munix

class BankAccountController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [bankAccountInstanceList: BankAccount.list(params), bankAccountInstanceTotal: BankAccount.count()]
    }

    def create = {
        def bankAccountInstance = new BankAccount()
        bankAccountInstance.properties = params
        return [bankAccountInstance: bankAccountInstance]
    }

    def save = {
        def bankAccountInstance = new BankAccount(params)
        if (bankAccountInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), bankAccountInstance.id])}"
            redirect(action: "show", id: bankAccountInstance.id)
        }
        else {
            render(view: "create", model: [bankAccountInstance: bankAccountInstance])
        }
    }

    def show = {
        def bankAccountInstance = BankAccount.get(params.id)
        if (bankAccountInstance) {
            [bankAccountInstance: bankAccountInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def bankAccountInstance = BankAccount.get(params.id)
        if (bankAccountInstance) {
            return [bankAccountInstance: bankAccountInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def bankAccountInstance = BankAccount.get(params.id)
        if (bankAccountInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (bankAccountInstance.version > version) {
                    
                    bankAccountInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'bankAccount.label', default: 'BankAccount')] as Object[], "Another user has updated this BankAccount while you were editing")
                    render(view: "edit", model: [bankAccountInstance: bankAccountInstance])
                    return
                }
            }
            bankAccountInstance.properties = params
            if (!bankAccountInstance.hasErrors() && bankAccountInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), bankAccountInstance.id])}"
                redirect(action: "show", id: bankAccountInstance.id)
            }
            else {
                render(view: "edit", model: [bankAccountInstance: bankAccountInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bankAccount.label', default: 'BankAccount'), params.id])}"
            redirect(action: "list")
        }
    }

}
