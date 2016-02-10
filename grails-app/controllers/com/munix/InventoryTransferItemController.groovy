package com.munix

class InventoryTransferItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [inventoryTransferItemInstanceList: InventoryTransferItem.list(params), inventoryTransferItemInstanceTotal: InventoryTransferItem.count()]
    }

    def create = {
        def inventoryTransferItemInstance = new InventoryTransferItem()
        inventoryTransferItemInstance.properties = params
        return [inventoryTransferItemInstance: inventoryTransferItemInstance]
    }

    def save = {
        def inventoryTransferItemInstance = new InventoryTransferItem(params)
        inventoryTransferItemInstance.transfer = InventoryTransfer.get(params.id)
        inventoryTransferItemInstance.product = Product.get(params."product.id")
        inventoryTransferItemInstance.qty = new BigDecimal(params.qty)
        def itemList = InventoryTransferItem.findAllWhere(transfer:inventoryTransferItemInstance.transfer).collect{it.product}
        if(inventoryTransferItemInstance.product in itemList){
            flash.message = "Duplicate product already exists!"
            redirect(controller: "inventoryTransfer", action: "show", id: inventoryTransferItemInstance.transfer.id)
        }else{
            if (inventoryTransferItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), inventoryTransferItemInstance.id])}"
                redirect(action: "show", id: inventoryTransferItemInstance.transfer.id, controller:"inventoryTransfer")
            }
            else {
                render(view: "create", model: [inventoryTransferItemInstance: inventoryTransferItemInstance])
            }
        }

        
    }

    def show = {
        def inventoryTransferItemInstance = InventoryTransferItem.get(params.id)
        if (inventoryTransferItemInstance) {
            [inventoryTransferItemInstance: inventoryTransferItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def inventoryTransferItemInstance = InventoryTransferItem.get(params.id)
        if (inventoryTransferItemInstance) {
            return [inventoryTransferItemInstance: inventoryTransferItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def inventoryTransferItemInstance = InventoryTransferItem.get(params.id)
        if (inventoryTransferItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (inventoryTransferItemInstance.version > version) {
                    
                    inventoryTransferItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem')] as Object[], "Another user has updated this InventoryTransferItem while you were editing")
                    render(view: "edit", model: [inventoryTransferItemInstance: inventoryTransferItemInstance])
                    return
                }
            }
            inventoryTransferItemInstance.properties = params
            if (!inventoryTransferItemInstance.hasErrors() && inventoryTransferItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), inventoryTransferItemInstance.id])}"
                redirect(action: "show", id:inventoryTransferItemInstance.transfer.id, controller:"inventoryTransfer")
            }
            else {
                render(view: "edit", model: [inventoryTransferItemInstance: inventoryTransferItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def inventoryTransferItemInstance = InventoryTransferItem.get(params.id)
        if (inventoryTransferItemInstance) {
            try {
                inventoryTransferItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), params.id])}"
                redirect(action: "show", id: inventoryTransferItemInstance.transfer.id, controller:"inventoryTransfer")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
