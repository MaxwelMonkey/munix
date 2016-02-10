package com.munix

import grails.converters.JSON

class InventoryAdjustmentController {

	def inventoryAdjustmentService
	def authenticateService
	def stockCardService
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 100,  100)
        [inventoryAdjustmentInstanceList: InventoryAdjustment.list(params), inventoryAdjustmentInstanceTotal: InventoryAdjustment.count()]
    }

    def create = {
        def inventoryAdjustmentInstance = new InventoryAdjustment()
        inventoryAdjustmentInstance.properties = params
        return [inventoryAdjustmentInstance: inventoryAdjustmentInstance, items : inventoryAdjustmentService.generateInventoryAdjustmentItems(inventoryAdjustmentInstance)]
    }

    def save = {
        def inventoryAdjustmentInstance = new InventoryAdjustment(params)
		def user = authenticateService.userDomain()
		inventoryAdjustmentInstance.preparedBy = FormatUtil.createTimeStamp(user)

		def toBeDeleted = inventoryAdjustmentInstance.itemList?.findAll { it.isDeleted }
		inventoryAdjustmentInstance.items.removeAll(toBeDeleted)

        if (!inventoryAdjustmentInstance.hasErrors() && inventoryAdjustmentInstance.save()) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'inventoryAdjustment.label', default: 'InventoryAdjustment'), inventoryAdjustmentInstance.id])}"
			redirect(action: "show", id: inventoryAdjustmentInstance.id)
        }
        else {
            render(view: "create", model: [inventoryAdjustmentInstance: inventoryAdjustmentInstance, items : inventoryAdjustmentService.generateInventoryAdjustmentItems(inventoryAdjustmentInstance)])
        }
    }

    def show = {
        def inventoryAdjustmentInstance = InventoryAdjustment.get(params.id)
        if (inventoryAdjustmentInstance) {
			return [inventoryAdjustmentInstance: inventoryAdjustmentInstance, items : inventoryAdjustmentService.generateInventoryAdjustmentItems(inventoryAdjustmentInstance)]
        }
        else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryAdjustment.label', default: 'InventoryAdjustment'), params.id])}"
			redirect(action: "list")
        }
    }

    def edit = {
        def inventoryAdjustmentInstance = InventoryAdjustment.get(params.id)
        if (inventoryAdjustmentInstance) {
            return [inventoryAdjustmentInstance: inventoryAdjustmentInstance, items : inventoryAdjustmentService.generateInventoryAdjustmentItems(inventoryAdjustmentInstance)]
        }
        else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryAdjustment.label', default: 'InventoryAdjustment'), params.id])}"
			redirect(action: "list")
        }
    }

    def update = {
		def isProductsEmpty = false
        def inventoryAdjustmentInstance = InventoryAdjustment.get(params.id)
        if (inventoryAdjustmentInstance) {
			def inventoryAdjustmentItems = inventoryAdjustmentService.generateInventoryAdjustmentItems(inventoryAdjustmentInstance)
            inventoryAdjustmentInstance.properties = params
			
			def products = inventoryAdjustmentInstance?.items
			def toBeDeleted = inventoryAdjustmentInstance?.items.findAll {it.isDeleted}
			if (toBeDeleted) {
				if(toBeDeleted.size() >= products.size()) {
					isProductsEmpty = true
				}
				else {
					toBeDeleted.each { deleted->
						inventoryAdjustmentInstance.removeFromItems(deleted)
						deleted.delete()
					}
					inventoryAdjustmentInstance?.items.removeAll(toBeDeleted)
				}
			}

            if (!inventoryAdjustmentInstance.hasErrors() && inventoryAdjustmentInstance.save() && !isProductsEmpty) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'inventoryAdjustment.label', default: 'InventoryAdjustment'), inventoryAdjustmentInstance.id])}"
                redirect(action: "show", id: inventoryAdjustmentInstance.id)
            }
            else {
				flash.message = "Inventory adjustment must have at least one product."
                render(view: "edit", model: [inventoryAdjustmentInstance: inventoryAdjustmentInstance, items: inventoryAdjustmentItems])
            }
        }
        else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryAdjustment.label', default: 'InventoryAdjustment'), params.id])}"
			redirect(action: "list")
		}
    }

	def cancel = {
		def inventoryAdjustmentInstance = InventoryAdjustment.get(params.id)
		inventoryAdjustmentInstance?.cancel()
		inventoryAdjustmentInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		inventoryAdjustmentInstance.approvedBy = ""

		flash.message = "Inventory Adjustment has been cancelled!"
		redirect(action:'show',id:inventoryAdjustmentInstance?.id)
	}
	
	def retrieveInventoryAdjustmentItems = {
		def searchField = params.sSearch
		def inventoryStatus = params.inventoryStatus
		def products = Product.list().findAll {it.status == inventoryStatus && (it.identifier.toLowerCase() =~ searchField.toLowerCase() || it.description.toLowerCase() =~ searchField.toLowerCase())}
		def data = createInventoryAdjustmentItemsDataMap(products)

		def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
		render jsonResponse as JSON
	}

	private List createInventoryAdjustmentItemsDataMap(List products) {
		def data = []
		def itemTypeId = params.itemTypeId
		def warehouseId = params.warehouseId
		def itemTypeNotNull = itemTypeId != "null"
		def warehouseNotNull = warehouseId != "null"
		def itemType = itemTypeNotNull ? ItemType.get(itemTypeId.toLong()).identifier : ""
		def warehouse = warehouseNotNull ? Warehouse.get(warehouseId.toLong()) : null
		products?.sort{it?.description}.each {
			if(it?.itemType?.identifier == itemType) {
				def dataMap = []
				def stock = Stock.findByProductAndWarehouse(it, warehouse)
				dataMap.addAll(
						it.id,
						it.identifier,
						it?.formatDescription(),
						stock ? stock.qty : BigDecimal.ZERO
						)
				data.add(dataMap)
			}
		}
		return data
	}
	
	def approve = {
		def inventoryAdjustmentInstance = InventoryAdjustment.get(params.id)
		if(!inventoryAdjustmentInstance?.isApproved()){
			inventoryAdjustmentInstance?.approved()
			inventoryAdjustmentInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			if (inventoryAdjustmentInstance?.save()) {
				inventoryAdjustmentInstance?.items?.each{
					def stock = Stock.findByProductAndWarehouse(it.product, inventoryAdjustmentInstance?.warehouse)
					if(stock){
						it.oldStock = stock.qty
						it.save()
					}
				}
				inventoryAdjustmentService.updateProductStock(inventoryAdjustmentInstance)
				stockCardService.createApprovedInventoryAdjustment(inventoryAdjustmentInstance)
				flash.message = "Inventory adjustment has been successfully approved!"
			} else {
				flash.message = "Inventory adjustment cannot be approved!"
			}
		}
		redirect(action:'show',id:inventoryAdjustmentInstance?.id)
	}
	
	def print = {
	}
}
