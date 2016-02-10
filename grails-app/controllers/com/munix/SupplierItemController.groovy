package com.munix

class SupplierItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [supplierItemInstanceList: SupplierItem.list(params), supplierItemInstanceTotal: SupplierItem.count()]
    }

    def create = {
        def supplierItemInstance = new SupplierItem()
        supplierItemInstance.properties = params
        return [supplierItemInstance: supplierItemInstance]
    }

    def save = {
        params.status = SupplierItem.Status.getTypeByName(params.status)
        def supplierItemInstance = new SupplierItem(params)
        supplierItemInstance.supplier = Supplier.get(params.id)
        
        if (supplierItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), supplierItemInstance.id])}"
            redirect(action: "show", id: supplierItemInstance.supplier.id, controller: "supplier")
        }
        else {
            render(view: "create", model: [supplierItemInstance: supplierItemInstance], params: params)
        }
    }

    def show = {
        def supplierItemInstance = SupplierItem.get(params.id)
        if (!supplierItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [supplierItemInstance: supplierItemInstance]
        }
    }

    def edit = {

        def supplierItemInstance = SupplierItem.get(params.id)
        if (!supplierItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [supplierItemInstance: supplierItemInstance]
        }
    }

    def update = {
        def supplierItemInstance = SupplierItem.get(params.id)
        if (supplierItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (supplierItemInstance.version > version) {
                    
                    supplierItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'supplierItem.label', default: 'SupplierItem')] as Object[], "Another user has updated this SupplierItem while you were editing")
                    render(view: "edit", model: [supplierItemInstance: supplierItemInstance])
                    return
                }
            }
            params.status = SupplierItem.Status.getTypeByName(params.status)
            supplierItemInstance.properties = params
            if (!supplierItemInstance.hasErrors() && supplierItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), supplierItemInstance.id])}"
                redirect(action: "show", id: supplierItemInstance.supplier.id, controller: "supplier")
            }
            else {
                render(view: "edit", model: [supplierItemInstance: supplierItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def supplierItemInstance = SupplierItem.get(params.id)
        if (supplierItemInstance) {
            try {
                supplierItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), params.id])}"
                redirect(action: "show", id: supplierItemInstance.supplier.id, controller: "supplier")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), params.id])}"
                redirect(action: "show", id: supplierItemInstance.supplier.id, controller:"supplier")
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplierItem.label', default: 'SupplierItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
