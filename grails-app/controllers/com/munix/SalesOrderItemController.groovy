package com.munix

class SalesOrderItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def create = {
        def salesOrderItemInstance = new SalesOrderItem()
        salesOrderItemInstance.properties = params
        return [salesOrderItemInstance: salesOrderItemInstance]
    }

    def save = {
        def salesOrderItemInstance = new SalesOrderItem(params)
        salesOrderItemInstance.invoice = SalesOrder.get(params.id)
        salesOrderItemInstance.product = Product.get(params."product.id")
        salesOrderItemInstance.price = new BigDecimal(params.price)
        salesOrderItemInstance.qty = new BigDecimal(params.qty)
        salesOrderItemInstance.finalPrice = new BigDecimal(params.finalPrice)

        if(salesOrderItemInstance.product.isNet){
            salesOrderItemInstance.invoice.discountGroup = null
        }


        def itemList = SalesOrderItem.findAllWhere(invoice:salesOrderItemInstance.invoice).collect{it.product}

        if(salesOrderItemInstance.product in itemList){
            flash.message = "Duplicate product already exists!"
            redirect(action: "show", id: params.id, controller:"salesOrder")
        }
        else{
            if (salesOrderItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'salesOrderItem.label', default: 'SalesOrderItem'), salesOrderItemInstance.id])}"
                redirect(action: "show", id: params.id, controller:"salesOrder")
            }
            else {
                render(view: "create", model: [salesOrderItemInstance: salesOrderItemInstance])
            }
        }
    }

    def edit = {
        def salesOrderItemInstance = SalesOrderItem.get(params.id)
        if (!salesOrderItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrderItem.label', default: 'SalesOrderItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [salesOrderItemInstance: salesOrderItemInstance]
        }
    }

    def update = {
        def salesOrderItemInstance = SalesOrderItem.get(params.id)
        if (salesOrderItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (salesOrderItemInstance.version > version) {
                    
                    salesOrderItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'salesOrderItem.label', default: 'SalesOrderItem')] as Object[], "Another user has updated this SalesOrderItem while you were editing")
                    render(view: "edit", model: [salesOrderItemInstance: salesOrderItemInstance])
                    return
                }
            }
            salesOrderItemInstance.properties = params
            if (!salesOrderItemInstance.hasErrors() && salesOrderItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'salesOrderItem.label', default: 'SalesOrderItem'), salesOrderItemInstance.id])}"
                redirect(action: "show", controller:"salesOrder", id: salesOrderItemInstance.invoice.id)
            }
            else {
                render(view: "edit", model: [salesOrderItemInstance: salesOrderItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrderItem.label', default: 'SalesOrderItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def salesOrderItemInstance = SalesOrderItem.get(params.id)
        def salesOrderId = salesOrderItemInstance.invoice.id
        if (salesOrderItemInstance) {
            try {
                salesOrderItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'salesOrderItem.label', default: 'SalesOrderItem'), params.id])}"
                redirect(action: "show", controller:"salesOrder",id:salesOrderId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'salesOrderItem.label', default: 'SalesOrderItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrderItem.label', default: 'SalesOrderItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
