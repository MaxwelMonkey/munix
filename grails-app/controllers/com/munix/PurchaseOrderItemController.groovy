package com.munix

class PurchaseOrderItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [purchaseOrderItemInstanceList: PurchaseOrderItem.list(params), purchaseOrderItemInstanceTotal: PurchaseOrderItem.count()]
    }

    def create = {
        def purchaseOrderItemInstance = new PurchaseOrderItem()
        purchaseOrderItemInstance.properties = params
        return [purchaseOrderItemInstance: purchaseOrderItemInstance]
    }

    def save = {
        def purchaseOrderItemInstance = new PurchaseOrderItem(params)
        purchaseOrderItemInstance.po = PurchaseOrder.get(params.id)
        purchaseOrderItemInstance.product = Product.get(params."product.id")
        purchaseOrderItemInstance.price = new BigDecimal(params.price)
        purchaseOrderItemInstance.finalPrice = new BigDecimal(params.finalPrice)
        purchaseOrderItemInstance.qty = new BigDecimal(params.qty)
        purchaseOrderItemInstance.isComplete = false
        
        def itemList = PurchaseOrderItem.findAllWhere(po:purchaseOrderItemInstance.po).collect{it.product}
        
        if(purchaseOrderItemInstance.product in itemList){
            flash.message = "Duplicate product already exists!"
            redirect(action: "show", id: params.id, controller:"purchaseOrder")
        }else{
            if (purchaseOrderItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), purchaseOrderItemInstance.id])}"
                redirect(action: "show", id: params.id, controller:"purchaseOrder")
            }
            else {
                render(view: "create", model: [purchaseOrderItemInstance: purchaseOrderItemInstance])
            }
        }
    }

    def show = {
        def purchaseOrderItemInstance = PurchaseOrderItem.get(params.id)
        if (!purchaseOrderItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [purchaseOrderItemInstance: purchaseOrderItemInstance]
        }
    }

    def edit = {
        def purchaseOrderItemInstance = PurchaseOrderItem.get(params.id)
        if (!purchaseOrderItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [purchaseOrderItemInstance: purchaseOrderItemInstance]
        }
    }

    def update = {
        def purchaseOrderItemInstance = PurchaseOrderItem.get(params.id)
        if (purchaseOrderItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (purchaseOrderItemInstance.version > version) {
                    
                    purchaseOrderItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem')] as Object[], "Another user has updated this PurchaseOrderItem while you were editing")
                    render(view: "edit", model: [purchaseOrderItemInstance: purchaseOrderItemInstance])
                    return
                }
            }
            purchaseOrderItemInstance.properties = params
            if (!purchaseOrderItemInstance.hasErrors() && purchaseOrderItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), purchaseOrderItemInstance.id])}"
                redirect(action: "show", controller:"purchaseOrder", id: purchaseOrderItemInstance.po.id)
            }
            else {
                render(view: "edit", model: [purchaseOrderItemInstance: purchaseOrderItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def purchaseOrderItemInstance = PurchaseOrderItem.get(params.id)
        if (purchaseOrderItemInstance) {
        	def purchaseOrderId = purchaseOrderItemInstance.po.id 
            try {
                purchaseOrderItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), params.id])}"
                redirect(action: "show", controller:"purchaseOrder", id:purchaseOrderId )
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
