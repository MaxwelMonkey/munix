package com.munix

class DirectPaymentItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [directPaymentItemInstanceList: DirectPaymentItem.list(params), directPaymentItemInstanceTotal: DirectPaymentItem.count()]
    }

    def create = {
        def directPaymentItemInstance = new DirectPaymentItem()
        directPaymentItemInstance.properties = params
        return [directPaymentItemInstance: directPaymentItemInstance]
    }

    def save = {
        def directPaymentItemInstance = new DirectPaymentItem(params)
        directPaymentItemInstance.directPayment = DirectPayment.get(params.id)

        if(directPaymentItemInstance.paymentType.isCheck){
            def itemCheck = new DirectPaymentItemCheck(params)
            itemCheck.directPayment = DirectPayment.get(params.id)
            def checkPayment = new CheckPayment()
            checkPayment.amount = directPaymentItemInstance.amount
            checkPayment.checkNumber = params.checkNumber
            checkPayment.bank = Bank.get(params."checkBank.id")
            checkPayment.type = CheckType.get(params."checkType.id")
            checkPayment.date = params.checkDate
            checkPayment.customer = directPaymentItemInstance?.directPayment?.customer
            checkPayment.warehouse = CheckWarehouse.findWhere(isDefault:true)
            checkPayment.save(flush:true)
            itemCheck.checkPayment = checkPayment
            itemCheck.checkPayment.directPaymentItem = itemCheck
            itemCheck.save(flush:true)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), directPaymentItemInstance.id])}"
            redirect(action: "show", id: params.id, controller:"directPayment")
        }

        else{
            if (directPaymentItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), directPaymentItemInstance.id])}"
                redirect(action: "show", controller:"directPayment", id: params.id)
            }
            else {
                render(view: "create", model: [directPaymentItemInstance: directPaymentItemInstance])
            }
        }
    }

    def show = {
        def directPaymentItemInstance = DirectPaymentItem.get(params.id)
        if (directPaymentItemInstance) {
            [directPaymentItemInstance: directPaymentItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def directPaymentItemInstance = DirectPaymentItem.get(params.id)
        if (directPaymentItemInstance) {
            return [directPaymentItemInstance: directPaymentItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def directPaymentItemInstance = DirectPaymentItem.get(params.id)
        if (directPaymentItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (directPaymentItemInstance.version > version) {
                    
                    directPaymentItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem')] as Object[], "Another user has updated this DirectPaymentItem while you were editing")
                    render(view: "edit", model: [directPaymentItemInstance: directPaymentItemInstance])
                    return
                }
            }
            directPaymentItemInstance.properties = params
            if (!directPaymentItemInstance.hasErrors() && directPaymentItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), directPaymentItemInstance.id])}"
                redirect(action: "show", controller:"directPayment", id: directPaymentItemInstance.directPayment.id)
            }
            else {
                render(view: "edit", model: [directPaymentItemInstance: directPaymentItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def directPaymentItemInstance = DirectPaymentItem.get(params.id)
        if (directPaymentItemInstance) {
            try {
                directPaymentItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), params.id])}"
                redirect(action:  "show", controller:"directPayment", id: directPaymentItemInstance.directPayment.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPaymentItem.label', default: 'DirectPaymentItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
