package com.munix

import grails.converters.*

class PurchaseInvoiceController {

    static allowedMethods = [save: "POST", update: "POST", validate: "POST",authorize: "POST", delete: "POST", approve: "POST", unapprove: "POST", retrievePOs : "POST"]
    
	def authenticateService
    def constantService
    def userAuthenticateService
	def purchaseInvoiceService
	def stockCardService
	
    private static final String APPROVED = "Approved"
    
	def index = {
        redirect(action: "list", params: params)
    }

    def filter = {
		["supplier.id": params."supplier.id", "supplierName": params."supplierNname"]
    }

    def list = {
        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "reference"
        if (!params.order) params.order = "asc"

        def purchaseInvoiceInstanceList = purchaseInvoiceService.generateList(params)
		def dateMap = purchaseInvoiceService.generateDateStructList(params)
        [purchaseInvoiceInstanceList: purchaseInvoiceInstanceList, purchaseInvoiceInstanceTotal: purchaseInvoiceInstanceList.totalCount, dateMap : dateMap]
    }

    def create = {
        def purchaseInvoiceInstance = new PurchaseInvoice(params)

        def orderList = PurchaseOrder.findAllBySupplierAndStatus(purchaseInvoiceInstance.supplier, APPROVED)?.findAll {order -> !order.items?.isEmpty()}
        if(orderList?.isEmpty()) {
        	flash.message = "Supplier chosen doesn't have any approved POs!"
			redirect(action: "filter", params: ["supplier.id": params."supplier.id"])
        }

        return [purchaseInvoiceInstance: purchaseInvoiceInstance, orderList: orderList, warehouses : Warehouse.list()]
    }

    def save = {
        def purchaseInvoiceInstance = new PurchaseInvoice(params)
        purchaseInvoiceInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        purchaseInvoiceInstance.type = purchaseInvoiceInstance.supplier.getLocality()
		
		def toBeDeleted = purchaseInvoiceInstance.invoiceItemList?.findAll { it.isDeleted }
		purchaseInvoiceInstance.items.removeAll(toBeDeleted)
		if (purchaseInvoiceInstance.validate()) {
			purchaseInvoiceService.processInvoiceItems(purchaseInvoiceInstance)		
		}
		else {
			render(view: "create", model: [purchaseInvoiceInstance: purchaseInvoiceInstance, warehouses: Warehouse.list()])
			return
		}
		if (purchaseInvoiceInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), purchaseInvoiceInstance.id])}"
            redirect(action: "show", id: purchaseInvoiceInstance.id)
        } else {
            render(view: "create", model: [purchaseInvoiceInstance: purchaseInvoiceInstance, warehouses : Warehouse.list()])
        }
    }

    def show = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (purchaseInvoiceInstance) {
        	def orderList = PurchaseOrder.findAllBySupplierAndStatus(purchaseInvoiceInstance.supplier, "Approved")
        	def supplerPaymentList = SupplierPayment.findAllBySupplier(purchaseInvoiceInstance.supplier)
        	def supplierPayment = supplerPaymentList.find{payment -> payment.purchaseInvoices?.find{ it == purchaseInvoiceInstance} }
        	def poItems = purchaseInvoiceInstance.items?.collect { it?.purchaseOrderItem }
			def exceedMessage
			if (purchaseInvoiceInstance.status == PurchaseInvoice.Status.UNAPPROVED) {
				exceedMessage = purchaseInvoiceService.getPurchaseInvoiceItemsWithExceedingQuantityMessage(purchaseInvoiceInstance)
			}
			
        	[purchaseInvoiceInstance: purchaseInvoiceInstance, orderList:orderList, poItems : poItems, supplierPayment : supplierPayment, exceedMessage: exceedMessage]
        } else {
        	flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
    		redirect(action: "list")
        }
    }
	
    def edit = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (purchaseInvoiceInstance) {
        	[purchaseInvoiceInstance: purchaseInvoiceInstance, warehouses: Warehouse.list()]
        } else {
        	flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
    		redirect(action: "list")
        }
    }
	
    def update = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (purchaseInvoiceInstance) {
            purchaseInvoiceInstance.properties = params
			purchaseInvoiceInstance.errors = validatePurchaseInvoice(purchaseInvoiceInstance)
			if (purchaseInvoiceInstance.hasErrors()) {
                render(view: "edit", model: [purchaseInvoiceInstance: purchaseInvoiceInstance, warehouses: Warehouse.list()])
            }
			else {
				purchaseInvoiceService.processInvoiceItems(purchaseInvoiceInstance)
				purchaseInvoiceInstance.save(flush: true)
			    flash.message = "${message(code: 'default.updated.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), purchaseInvoiceInstance.id])}"
                redirect(action: "show", id: purchaseInvoiceInstance.id)
			}
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
            redirect(action: "list")
        }
    }


    def approve = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if(!purchaseInvoiceInstance.isApproved()){
	        if (purchaseInvoiceService.approve(purchaseInvoiceInstance)) {
		        flash.message = "Purchase Invoice has been successfully approved!"
	        } else {
				flash.message = "Purchase Invoice cannot be approved!"
			}
        }
        redirect(action:'show', id:purchaseInvoiceInstance?.id)
    }

    def unapprove = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if(!purchaseInvoiceInstance.isUnapproved()){
        	if (purchaseInvoiceService.unapprove(purchaseInvoiceInstance)) {
	        	flash.message = "Purchase Invoice has been successfully unapproved!"
        	} else {
        		flash.error = "Purchase Invoice cannot be unapproved because the supplier payment has already taken it or one of the purchase order has already been marked as complete!"
        	}
        }
        redirect(action:'show', id:purchaseInvoiceInstance?.id)
    }

    def cancel = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        def isCancelable = purchaseInvoiceService.isCancelable(purchaseInvoiceInstance)
        if(isCancelable){
    		purchaseInvoiceInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
            purchaseInvoiceInstance?.cancel()
            purchaseInvoiceInstance.approvedBy = ""
            purchaseInvoiceInstance?.save()
            flash.message = "Purchase Invoice has been cancelled!"
        }else{
            flash.error = "Purchase Invoice can't be cancelled because it is currently approved!"
        }
        redirect(action:'show',id:purchaseInvoiceInstance?.id)
    }
    
	def retrievePurchaseOrderItems = {
		def supplier = Supplier.get(params.supplierId)
		if (supplier) {
			def purchaseOrderItems = purchaseInvoiceService.queryAvailablePurchaseOrderItems(supplier, params.sSearch)
			def data = createPurchaseOrderItemDataMap(purchaseOrderItems)
	
			def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
			render jsonResponse as JSON
    	} else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.supplerId])}"
            redirect(action: "filter")
        } 
	}
		
	private List createPurchaseOrderItemDataMap(Set purchaseOrderItems) {
		def data = []
		purchaseOrderItems?.each {item ->
			if (!item.isComplete && item.computeRemainingBalance() > 0) {
				def dataMap = []
				dataMap.addAll(
					item.po.formatId(),
					item.product.identifier + "-" +  item?.product?.description,
					item.qty,
					item.computeRemainingBalance(),
					item.price,
					item.finalPrice,
					item.receivedQty,
					item.product.id,
					item.id
				)
				data.add(dataMap)
			}
		}
		return data
	}
	
	private def validatePurchaseInvoice(PurchaseInvoice purchaseInvoiceInstance) {
		def toBeDeleted = purchaseInvoiceInstance.invoiceItemList?.findAll { it.isDeleted }
		
		// reset qty property of deleted items that have 0 qty to bypass qty constraint for these items
		toBeDeleted.each {
			if (it.qty == 0) {
				it.qty = 1
			}
		}		
		purchaseInvoiceInstance.validate()
		
		if (toBeDeleted) {
			// validate items size
			def itemsSize = purchaseInvoiceInstance.invoiceItemList?.size() - toBeDeleted.size()
			if (itemsSize == 0) {
				purchaseInvoiceInstance.errors.reject('purchaseInvoice.empty.items.error',
				"Purchase Invoice must have at least 1 item")
			}
		}
		
		return purchaseInvoiceInstance.errors
	}
}
