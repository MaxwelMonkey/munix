package com.munix

class LedgerEntryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [ledgerEntryInstanceList: LedgerEntry.list(params), ledgerEntryInstanceTotal: LedgerEntry.count()]
    }

    def create = {
        def ledgerEntryInstance = new LedgerEntry()
        ledgerEntryInstance.properties = params
        return [ledgerEntryInstance: ledgerEntryInstance]
    }

    def save = {
        def ledgerEntryInstance = new LedgerEntry(params)
        if (ledgerEntryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), ledgerEntryInstance.id])}"
            redirect(action: "show", id: ledgerEntryInstance.id)
        }
        else {
            render(view: "create", model: [ledgerEntryInstance: ledgerEntryInstance])
        }
    }

    def show = {
        def ledgerEntryInstance = LedgerEntry.get(params.id)
        if (ledgerEntryInstance) {
            [ledgerEntryInstance: ledgerEntryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def ledgerEntryInstance = LedgerEntry.get(params.id)
        if (ledgerEntryInstance) {
            return [ledgerEntryInstance: ledgerEntryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def ledgerEntryInstance = LedgerEntry.get(params.id)
        if (ledgerEntryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (ledgerEntryInstance.version > version) {
                    
                    ledgerEntryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'ledgerEntry.label', default: 'LedgerEntry')] as Object[], "Another user has updated this LedgerEntry while you were editing")
                    render(view: "edit", model: [ledgerEntryInstance: ledgerEntryInstance])
                    return
                }
            }
            ledgerEntryInstance.properties = params
            if (!ledgerEntryInstance.hasErrors() && ledgerEntryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), ledgerEntryInstance.id])}"
                redirect(action: "show", id: ledgerEntryInstance.id)
            }
            else {
                render(view: "edit", model: [ledgerEntryInstance: ledgerEntryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def ledgerEntryInstance = LedgerEntry.get(params.id)
        if (ledgerEntryInstance) {
            try {
                ledgerEntryInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'ledgerEntry.label', default: 'LedgerEntry'), params.id])}"
            redirect(action: "list")
        }
    }
}
