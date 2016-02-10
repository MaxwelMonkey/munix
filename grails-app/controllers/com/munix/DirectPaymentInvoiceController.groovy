package com.munix

class DirectPaymentInvoiceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [directPaymentInvoiceInstanceList: DirectPaymentInvoice.list(params), directPaymentInvoiceInstanceTotal: DirectPaymentInvoice.count()]
    }

    def create = {
        def directPaymentInvoiceInstance = new DirectPaymentInvoice()
        directPaymentInvoiceInstance.properties = params
        return [directPaymentInvoiceInstance: directPaymentInvoiceInstance]
    }

    def save = {
        def directPaymentInvoiceInstance = new DirectPaymentInvoice(params)
        if (directPaymentInvoiceInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), directPaymentInvoiceInstance.id])}"
            redirect(action: "show", id: directPaymentInvoiceInstance.id)
        }
        else {
            render(view: "create", model: [directPaymentInvoiceInstance: directPaymentInvoiceInstance])
        }
    }

    def show = {
        def directPaymentInvoiceInstance = DirectPaymentInvoice.get(params.id)
        if (directPaymentInvoiceInstance) {
            [directPaymentInvoiceInstance: directPaymentInvoiceInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def directPaymentInvoiceInstance = DirectPaymentInvoice.get(params.id)
        if (directPaymentInvoiceInstance) {
            return [directPaymentInvoiceInstance: directPaymentInvoiceInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def directPaymentInvoiceInstance = DirectPaymentInvoice.get(params.id)
        if (directPaymentInvoiceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (directPaymentInvoiceInstance.version > version) {
                    
                    directPaymentInvoiceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice')] as Object[], "Another user has updated this DirectPaymentInvoice while you were editing")
                    render(view: "edit", model: [directPaymentInvoiceInstance: directPaymentInvoiceInstance])
                    return
                }
            }
            directPaymentInvoiceInstance.properties = params
            if (!directPaymentInvoiceInstance.hasErrors() && directPaymentInvoiceInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), directPaymentInvoiceInstance.id])}"
                redirect(action: "show", id: directPaymentInvoiceInstance?.directPayment?.id, controller: "directPayment")
            }
            else {
                render(view: "edit", model: [directPaymentInvoiceInstance: directPaymentInvoiceInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def directPaymentInvoiceInstance = DirectPaymentInvoice.get(params.id)
        def directPaymentId = directPaymentInvoiceInstance?.directPayment?.id
        if (directPaymentInvoiceInstance) {
            try {
                directPaymentInvoiceInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), params.id])}"
                redirect(action: "show", id: directPaymentId, controller: "directPayment")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentInvoice.label', default: 'DirectPaymentInvoice'), params.id])}"
            redirect(action: "list")
        }
    }
}
