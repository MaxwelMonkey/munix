package com.munix

class SalesDeliveryItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [salesDeliveryItemInstanceList: SalesDeliveryItem.list(params), salesDeliveryItemInstanceTotal: SalesDeliveryItem.count()]
    }

    def create = {
        def salesDeliveryItemInstance = new SalesDeliveryItem()
        salesDeliveryItemInstance.properties = params
        return [salesDeliveryItemInstance: salesDeliveryItemInstance]
    }

    def save = {
        def salesDeliveryItemInstance = new SalesDeliveryItem(params)

        salesDeliveryItemInstance.delivery = SalesDelivery.get(params.id)

        def itemList = SalesDeliveryItem.findAllWhere(delivery:salesDeliveryItemInstance.delivery).collect{it.product}

        if(salesDeliveryItemInstance.product in itemList){
            flash.message = "Duplicate product already exists!"
            redirect(action: "show", id: params.id, controller:"salesDelivery")
        }
        
        else{
            if(salesDeliveryItemInstance.qty > 0){
                if (salesDeliveryItemInstance.save(flush: true)) {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), salesDeliveryItemInstance.id])}"
                    redirect(action: "show", id: params.id, controller:"salesDelivery")
                }
                else {
                    render(view: "create", model: [salesDeliveryItemInstance: salesDeliveryItemInstance])
                }
            }
            else{
                flash.message = "Qty should be greater than 0!"
                redirect(action: "show", id: params.id, controller:"salesDelivery")
            }
        }
    }

    def show = {
        def salesDeliveryItemInstance = SalesDeliveryItem.get(params.id)
        if (!salesDeliveryItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [salesDeliveryItemInstance: salesDeliveryItemInstance]
        }
    }

    def edit = {
        def salesDeliveryItemInstance = SalesDeliveryItem.get(params.id)
        if (!salesDeliveryItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [salesDeliveryItemInstance: salesDeliveryItemInstance]
        }
    }

    def update = {
        def salesDeliveryItemInstance = SalesDeliveryItem.get(params.id)
        if (salesDeliveryItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (salesDeliveryItemInstance.version > version) {
                    
                    salesDeliveryItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem')] as Object[], "Another user has updated this SalesDeliveryItem while you were editing")
                    render(view: "edit", model: [salesDeliveryItemInstance: salesDeliveryItemInstance])
                    return
                }
            }
            salesDeliveryItemInstance.properties = params
            if (!salesDeliveryItemInstance.hasErrors() && salesDeliveryItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), salesDeliveryItemInstance.id])}"
                redirect(action: "show", id: salesDeliveryItemInstance.delivery?.id, controller:"salesDelivery")
            }
            else {
                render(view: "edit", model: [salesDeliveryItemInstance: salesDeliveryItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def salesDeliveryItemInstance = SalesDeliveryItem.get(params.id)
        def salesDeliveryId = salesDeliveryItemInstance.delivery.id
        if (salesDeliveryItemInstance) {
            try {
                salesDeliveryItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), params.id])}"
                redirect(action: "show",, id: salesDeliveryId, controller:"salesDelivery")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
