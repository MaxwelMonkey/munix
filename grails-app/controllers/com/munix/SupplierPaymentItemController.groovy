package com.munix

class SupplierPaymentItemController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def create = {
        def supplierPaymentItemInstance = new SupplierPaymentItem()
        supplierPaymentItemInstance.properties = params
        return [supplierPaymentItemInstance: supplierPaymentItemInstance]
    }

    def save = {
        def supplierPaymentItemInstance = new SupplierPaymentItem(params)
		deleteCheckFieldsForNonCheck(supplierPaymentItemInstance)
        supplierPaymentItemInstance.payment = SupplierPayment.get(params.id)
		
        if (supplierPaymentItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem'), supplierPaymentItemInstance.id])}"
            redirect(action: "show", id: params.id, controller:"supplierPayment")
        }
        else {
            render(view: "create", model: [supplierPaymentItemInstance: supplierPaymentItemInstance])
        }
    }

    def edit = {
        def supplierPaymentItemInstance = SupplierPaymentItem.get(params.id)
        if (!supplierPaymentItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [supplierPaymentItemInstance: supplierPaymentItemInstance]
        }
    }

    def update = {
        def supplierPaymentItemInstance = SupplierPaymentItem.get(params.id)
        if (supplierPaymentItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (supplierPaymentItemInstance.version > version) {
                    
                    supplierPaymentItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem')] as Object[], "Another user has updated this SupplierPaymentItem while you were editing")
                    render(view: "edit", model: [supplierPaymentItemInstance: supplierPaymentItemInstance])
                    return
                }
            }
            supplierPaymentItemInstance.properties = params
			deleteCheckFieldsForNonCheck(supplierPaymentItemInstance)
            if (!supplierPaymentItemInstance.hasErrors() && supplierPaymentItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem'), supplierPaymentItemInstance.id])}"
                redirect(action: "show", controller:"supplierPayment", id: supplierPaymentItemInstance.payment.id)
            }
            else {
                render(view: "edit", model: [supplierPaymentItemInstance: supplierPaymentItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def supplierPaymentItemInstance = SupplierPaymentItem.get(params.id)
        if (supplierPaymentItemInstance) {
            try {
                supplierPaymentItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem'), params.id])}"
                redirect(action: "show", controller:"supplierPayment", id:supplierPaymentItemInstance.payment.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierPaymentItem.label', default: 'SupplierPaymentItem'), params.id])}"
            redirect(action: "list")
        }
    }
	
	private void deleteCheckFieldsForNonCheck(SupplierPaymentItem supplierPaymentItemInstance) {
		if (!supplierPaymentItemInstance?.type?.isCheck) {
			supplierPaymentItemInstance.checkBank = null
			supplierPaymentItemInstance.checkType = null
			supplierPaymentItemInstance.checkNumber = null
			supplierPaymentItemInstance.checkBranch = ""
		}
	}
}
