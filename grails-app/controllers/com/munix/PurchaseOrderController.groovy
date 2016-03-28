package com.munix

import grails.converters.*
import com.munix.SupplierItem.Status;

class PurchaseOrderController {

    def searchableService
    def authenticateService
    def constantService
	def purchaseOrderService
	def poIdService
    def generalMethodService
    def stockCostHistoryService
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST", approve:"POST", markAsComplete: "POST", unapprove: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def filter = {
        redirect(action: "list", params: params)
    }

    def list = {
		def generatedList = purchaseOrderService.getPurchaseOrderList(params)
		def dateMap = purchaseOrderService.generateDateStructList(params)
		[purchaseOrderInstanceList: generatedList.purchaseOrders, purchaseOrderInstanceTotal: generatedList.purchaseOrdersTotal, dateMap: dateMap]
    }

    def create = {
        def purchaseOrderInstance = new PurchaseOrder()
        purchaseOrderInstance.properties = params
        return [purchaseOrderInstance: purchaseOrderInstance]
    }
	
	def retrievePurchaseOrderItems = {
		def searchField = params.sSearch
		def supplier
		if(params.supplierId) {
			supplier = Supplier.get(params.supplierId.toLong())
		} 
		def supplierInventory = supplier ? supplier.items.findAll {it.status == SupplierItem.Status.ACTIVE && (it.product.identifier.toLowerCase() =~ searchField.toLowerCase() || it.product.description.toLowerCase() =~ searchField.toLowerCase())} : []
		def data = createPurchaseOrderItemsDataMap(supplierInventory)
		
		def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
		render jsonResponse as JSON
	}
	
	private List createPurchaseOrderItemsDataMap(List<SupplierItem> supplierItems) {
		def data = []
		supplierItems.each{
			def dataMap = []
			               
			def product = it.product
			dataMap.addAll(
				it?.product?.id,
				it?.product?.identifier,
				it?.product?.partNumber,
				it?.productCode,
				it?.product?.description,
				it?.product?.runningCostInForeignCurrency?it?.product?.runningCostInForeignCurrency:"",
				//it?.product?.runningCost?:0)
				it?.product?.runningCostInForeignCurrency?it?.product?.formatRunningCostInForeignCurrency():"",
				it?.supplier?.currency?.identifier
				)
				//it?.product?.currency?.identifier,
				//it?.product?.runningCostInForeignCurrency?:0
				//it?.product?.runningCostInForeignCurrency?it?.product?.formatRunningCostInForeignCurrency():"",
				//)
			Warehouse.list().sort{it.identifier}.each{
				def warehouse = it
				def stock = product.stocks?.find{it.warehouse?.id == warehouse.id}
				if(stock.qty>0)  
					dataMap.add("<span class='positive'>"+stock?.formatQty()+"</span>")
				else if(stock.qty<0)
					dataMap.add("<span class='negative'>"+stock?.formatQty()+"</span>")
				else
					dataMap.add(stock?.formatQty())
			}
			data.add(dataMap)
		}
		return data
	}
	

	def updateCreate={
		params.counter=0
		def purchaseOrderInstance=new PurchaseOrder(params)
		purchaseOrderInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		purchaseOrderInstance.supplier=Supplier.get(params.selectedValue)
		purchaseOrderInstance.currency = purchaseOrderInstance.supplier.currency
		def supplierInventory=purchaseOrderInstance.supplier.items.findAll{it.status == SupplierItem.Status.ACTIVE}

		def inventory=[]
		supplierInventory.each{
			inventory.add([description:it?.product?.description?: "",
				itemId:it?.id?: "", 
				productCode:it?.productCode?: "", 
				name:it?.product?.identifier?: "",
				cost:it?.product?.runningCostInForeignCurrency?: 0])
			
		}
		inventory=inventory.sort{it?.itemId}
		def jsonMap = [supplier:purchaseOrderInstance?.supplier?.name?: "",
			preparedBy:purchaseOrderInstance?.preparedBy?: "",
			supplierId:purchaseOrderInstance?.supplier.id?: "",
			country:purchaseOrderInstance?.supplier?.country?.description?: "",
			currency:purchaseOrderInstance?.supplier?.currency?.identifier?: "",
			inventory:inventory?.sort{it.description}?: ""]
		
		render jsonMap as JSON
	}

	def save = {
		def purchaseOrderInstance = new PurchaseOrder(params)
		purchaseOrderInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		purchaseOrderInstance.currency = Supplier.get(params."supplier.id").currency
		purchaseOrderInstance.counterId = purchaseOrderInstance.generateCounterId()
		purchaseOrderInstance.discountRate = new BigDecimal(params.discountRate)

		def toBeDeleted = purchaseOrderInstance.itemList?.findAll { it.isDeleted }
		purchaseOrderInstance.items.removeAll(toBeDeleted)
		purchaseOrderInstance.itemList.each { 
            it.price = it.product.runningCostInForeignCurrency?:0
        }
		if (!purchaseOrderInstance.hasErrors() && purchaseOrderInstance.save()) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), purchaseOrderInstance.id])}"
			redirect(action: "show", id: purchaseOrderInstance.id)
		}
		else {
			render(view: "create", model: [purchaseOrderInstance: purchaseOrderInstance, items : generatePurchaseOrderItems(purchaseOrderInstance)])
		}
	}
	
	def getItems={
		
	    def purchaseOrderInstance = PurchaseOrder.findById(params.selectedValue)
		def supplierInventory=purchaseOrderInstance.supplier.items.findAll{it.status == SupplierItem.Status.ACTIVE}
		def orderedItems=purchaseOrderInstance.items
		def availableItems=[]
		def tempList=[]
		def selectItems=[]
		def inventory=[]
		def allItems=[]
		def tempItems=supplierInventory.product-orderedItems.product
		supplierInventory.each{item->
			tempItems.each{tempItem->
				if(tempItem.id==item.product.id){
					selectItems.add(item)
				}
			}
		}
		supplierInventory.each{item->
			orderedItems.each{tempItem->
				if(tempItem.product.id==item.product.id){
					tempList.add(item:item,qty:tempItem.qty,finalPrice:tempItem.finalPrice)
				}
			}
		}
		tempList.each{
			inventory.add([description:it.item.product.description,
				itemId:it.item.id,
				productCode:it.item.productCode,
				name:it.item.product.identifier,
				cost:it.item.product.runningCostInForeignCurrency?: 0,
				qty:it.qty,
				finalPrice:it.finalPrice])
		}
		selectItems.each{
			availableItems.add([description:it.product.description,
				itemId:it.id,
				productCode:it.productCode,
				name:it.product.identifier,
				cost:it.product.runningCostInForeignCurrency?: 0])
		}
		supplierInventory.each{
			allItems.add([description:it.product.description,
				itemId:it.id,
				productCode:it.productCode,
				name:it.product.identifier,
				cost:it.product.runningCostInForeignCurrency?: 0])
		}
		availableItems=availableItems.sort{it.description}
		inventory=inventory.sort{it.description}
		allItems=allItems.sort{it.description}
		
		def jsonMap = [inventory:inventory,availableItems:availableItems, allItems:allItems,currency:purchaseOrderInstance.supplier.currency.identifier]
		
		render jsonMap as JSON
	}
    def show = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        if (!purchaseOrderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), params.id])}"
            redirect(action: "list")
        }
        else {
			def disapprove=true
			purchaseOrderInstance.items.each{
				if(it.receivedQty!=0){
					disapprove=false
				}
			}
            [purchaseOrderInstance: purchaseOrderInstance,disapprove:disapprove, service:stockCostHistoryService]
        }
    }
	def changeValues = {
		def purchaseOrderInstance = PurchaseOrder.get(params.id)
		if (!purchaseOrderInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), params.id])}"
			redirect(action: "list")
		}
		else {
			def disapprove=true
			purchaseOrderInstance.items.each{
				if(it.receivedQty!=0){
					disapprove=false
				}
			}
			[purchaseOrderInstance: purchaseOrderInstance,disapprove:disapprove]
		}
	}
	def updateCompleteStatus={
		def purchaseOrderInstance=PurchaseOrder.get(params.id)
		if(purchaseOrderInstance){
			purchaseOrderService.updateComplete(purchaseOrderInstance,params)
			redirect (action:"show",id:params.id)
		}
		else{
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), params.id])}"
            redirect(action: "list")
			return
		}
		
	}
    def edit = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        if (!purchaseOrderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [purchaseOrderInstance: purchaseOrderInstance, items: generatePurchaseOrderItems(purchaseOrderInstance)]
        }
    }

    def update = {

        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        if (purchaseOrderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (purchaseOrderInstance.version > version) {
                    purchaseOrderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'purchaseOrder.label', default: 'PurchaseOrder')] as Object[], "Another user has updated this PurchaseOrder while you were editing")
                    render(view: "edit", model: [purchaseOrderInstance: purchaseOrderInstance])
                    return
                }
            }
			purchaseOrderInstance.properties = params
			def toBeDeleted = purchaseOrderInstance.itemList?.findAll { it.isDeleted == true }
			purchaseOrderInstance.items.removeAll(toBeDeleted)
			toBeDeleted.each{
				it.delete()
			}
            if (!purchaseOrderInstance.hasErrors() && purchaseOrderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), purchaseOrderInstance.id])}"
                redirect(action: "show", id: purchaseOrderInstance.id)
            }
            else {
                render(view: "edit", model: [purchaseOrderInstance: purchaseOrderInstance, items: generatePurchaseOrderItems(purchaseOrderInstance)])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseOrder.label', default: 'PurchaseOrder'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def unCancel={
		def purchaseOrderInstance = PurchaseOrder.get(params.id)
		purchaseOrderInstance?.status=constantService.PURCHASE_ORDER_UNAPPROVED
		purchaseOrderInstance?.save()
		flash.message = "Purchase Order status has been reverted!"
		redirect(action:"show",id:purchaseOrderInstance?.id)
	}
	def cancel={
		def purchaseOrderInstance = PurchaseOrder.get(params.id)
		if(purchaseOrderInstance.isUnapproved()){
			purchaseOrderInstance?.status=constantService.PURCHASE_ORDER_CANCEL
			purchaseOrderInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			purchaseOrderInstance?.save()
			flash.message = "Purchase Order has been cancelled!"
		}else {
			flash.error = "Purchase Order cannot be cancelled in this status!"
		}
		redirect(action:"show",id:purchaseOrderInstance?.id)
	}
    def approve = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        purchaseOrderInstance?.approve()
        purchaseOrderInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		purchaseOrderInstance.items.each{
			it.price = it.product.runningCostInForeignCurrency?:0
		}
		purchaseOrderInstance?.save()
        flash.message = "Purchase Order has been successfully approved!"
		
        redirect(action:"show",id:purchaseOrderInstance?.id)
    }

    def markAsComplete = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        purchaseOrderInstance?.complete()
		purchaseOrderInstance?.closedBy=FormatUtil.createTimeStamp(authenticateService.userDomain())
        purchaseOrderInstance?.save()
        flash.message = "Purchase Order has been successfully completed!"
        redirect(action:'show',id:purchaseOrderInstance?.id)
    }

    def unapprove = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        def isUnapprovable = purchaseOrderService.isUnapprovable(purchaseOrderInstance)
        if(isUnapprovable){
            purchaseOrderInstance?.status = constantService.PURCHASE_ORDER_UNAPPROVED
            purchaseOrderInstance.approvedBy = ""
			purchaseOrderInstance.items.each{
				it.price = BigDecimal.ZERO
			}
            purchaseOrderInstance?.save()
            flash.message = "Purchase Order has been successfully unapproved!"
        }else{
            flash.error = "Purchase Order cannot be unapproved since there is a purchase invoice associated!"
        }

        redirect(action:'show',id:purchaseOrderInstance?.id)
    }
    def doPrint = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        purchaseOrderInstance.addToPrintLogs(new PrintLogPurchaseOrder(purchaseOrder:purchaseOrderInstance,user:authenticateService.userDomain(),type:PrintLogPurchaseOrder.Type.WITHPRICE))
        purchaseOrderInstance.save()
        redirect(controller:"print", action: "purchaseOrder", id: purchaseOrderInstance?.id)
    }

    def doPrintNoPrice = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        purchaseOrderInstance.addToPrintLogs(new PrintLogPurchaseOrder(purchaseOrder:purchaseOrderInstance,user:authenticateService.userDomain(),type:PrintLogPurchaseOrder.Type.NOPRICE))
        purchaseOrderInstance.save()
        redirect(controller:"print", action: "purchaseOrderNoPrice", id: purchaseOrderInstance?.id)
    }
	
    def doPrintNoPicture = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        purchaseOrderInstance.addToPrintLogs(new PrintLogPurchaseOrder(purchaseOrder:purchaseOrderInstance,user:authenticateService.userDomain(),type:PrintLogPurchaseOrder.Type.WITHPRICE))
        purchaseOrderInstance.save()
        redirect(controller:"print", action: "purchaseOrderNoPicture", id: purchaseOrderInstance?.id)
    }

    def doPrintNoPriceNoPicture = {
        def purchaseOrderInstance = PurchaseOrder.get(params.id)
        purchaseOrderInstance.addToPrintLogs(new PrintLogPurchaseOrder(purchaseOrder:purchaseOrderInstance,user:authenticateService.userDomain(),type:PrintLogPurchaseOrder.Type.NOPRICE))
        purchaseOrderInstance.save()
        redirect(controller:"print", action: "purchaseOrderNoPriceNoPicture", id: purchaseOrderInstance?.id)
    }
		
	private def generatePurchaseOrderItems(PurchaseOrder purchaseOrderInstance) {
		def items = []
		purchaseOrderInstance.items.eachWithIndex { item, idx->
			def itemsMap = [:]
			itemsMap["productId"] = item?.product.id
			itemsMap["identifier"] = item?.product?.identifier
			itemsMap["partNumber"] = item?.product?.partNumber
			itemsMap["productCode"] = item?.productCode
			itemsMap["description"] = item?.product?.description
			itemsMap["price"] = item?.price
			itemsMap["finalPrice"] = item?.finalPrice
			itemsMap["qty"] = item?.qty
			itemsMap["amount"] = item.computeAmount()
			itemsMap["isDeleted"] = item.isDeleted
			itemsMap["index"] = idx
			items.add(itemsMap)
		}
		return items.sort{it.description}
	}
}
