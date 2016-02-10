package com.munix

import grails.converters.JSON

class InventoryTransferController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", receive: "POST"]
    def authenticateService
    def constantService
	def inventoryTransferService
	def stockCardService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

		def inventoryTransfers = inventoryTransferService.generateList(params)
		def dateMap = inventoryTransferService.generateDateStructList(params)
		return [inventoryTransferList: inventoryTransfers.inventoryTransfers, inventoryTransferInstanceTotal: inventoryTransfers.inventoryTransfersTotal, statuses: InventoryTransfer.Status, warehouses: Warehouse.list().sort{it.identifier}, dateMap: dateMap]
    }

    def create = {
        def inventoryTransferInstance = new InventoryTransfer()
        inventoryTransferInstance.properties = params
        return [inventoryTransferInstance: inventoryTransferInstance]
    }

    def save = {
		def inventoryTransferInstance = new InventoryTransfer(params)
		if(params.destination == null) {
			flash.message = "Destination warehouse cannot be blank."
			render(view: "create", model: [inventoryTransferInstance: inventoryTransferInstance, items : inventoryTransferService.generateinventoryTransferItems(inventoryTransferInstance)])
		} else {
			def destinationWarehouse = Warehouse.findByIdentifier(params.destination)
			inventoryTransferInstance.destinationWarehouse = destinationWarehouse
			def user = authenticateService.userDomain()
			inventoryTransferInstance.preparedBy = FormatUtil.createTimeStamp(user)

			def toBeDeleted = inventoryTransferInstance.itemList?.findAll { it.isDeleted }
			inventoryTransferInstance.items.removeAll(toBeDeleted)

			if (!inventoryTransferInstance.hasErrors() && inventoryTransferInstance.save()) {
				flash.message = "${message(code: 'default.created.message', args: [message(code: 'inventoryTransfer.label', default: 'Inventory Transfer'), inventoryTransferInstance.id])}"
				redirect(action: "show", id: inventoryTransferInstance.id)
			}
			else {
				render(view: "create", model: [inventoryTransferInstance: inventoryTransferInstance, items : inventoryTransferService.generateInventoryTransferItems(inventoryTransferInstance)])
			}
		}
    }

    def show = {
        def inventoryTransferInstance = InventoryTransfer.get(params.id)
        if (inventoryTransferInstance) {
            [inventoryTransferInstance: inventoryTransferInstance, items : inventoryTransferService.generateInventoryTransferItems(inventoryTransferInstance)]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryTransfer.label', default: 'Inventory Transfer'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def inventoryTransferInstance = InventoryTransfer.get(params.id)
        if (inventoryTransferInstance) {
            return [inventoryTransferInstance: inventoryTransferInstance, items : inventoryTransferService.generateInventoryTransferItems(inventoryTransferInstance)]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryTransfer.label', default: 'Inventory Transfer'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
		def isProductsEmpty = false
		def inventoryTransferInstance = InventoryTransfer.get(params.id)
		if (inventoryTransferInstance) {
			def inventoryTransferItems = inventoryTransferService.generateInventoryTransferItems(inventoryTransferInstance)
			inventoryTransferInstance.properties = params
			def destinationWarehouse = Warehouse.get(Long.valueOf(params.destination))
			inventoryTransferInstance.destinationWarehouse = destinationWarehouse
			
			def products = inventoryTransferInstance?.items
			def toBeDeleted = inventoryTransferInstance?.items.findAll {it.isDeleted}
			if (toBeDeleted) {
				if(toBeDeleted.size() >= products.size()) {
					isProductsEmpty = true
				}
				else {
					toBeDeleted.each { deleted->
						inventoryTransferInstance.removeFromItems(deleted)
						deleted.delete()
					}
					inventoryTransferInstance?.items.removeAll(toBeDeleted)
				}
			}

			if (!inventoryTransferInstance.hasErrors() && inventoryTransferInstance.save() && !isProductsEmpty) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'inventoryTransfer.label', default: 'Inventory Transfer'), inventoryTransferInstance.id])}"
				redirect(action: "show", id: inventoryTransferInstance.id)
			}
			else {
				flash.message = "Error in updating inventory transfer."
				render(view: "edit", model: [inventoryTransferInstance: inventoryTransferInstance, items: inventoryTransferItems])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'inventoryTransfer.label', default: 'Inventory Transfer'), params.id])}"
			redirect(action: "list")
		}
    }
	
	def receive = {
		def inventoryTransferInstance = InventoryTransfer.get(params.id)
		def isInventoryTransferValid = inventoryTransferService.validateInventoryTransfer(inventoryTransferInstance)
		if(isInventoryTransferValid) {
			stockCardService.createApprovedInventoryTransfer(inventoryTransferInstance)
			inventoryTransferInstance?.received()
			inventoryTransferInstance.receivedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			inventoryTransferService.updateProductStock(inventoryTransferInstance)
			inventoryTransferInstance?.save()
			flash.message = "Inventory transfer has been successfully received!"
		} else {
			flash.message = "One or more item's stock is already outdated."
		}
		
		redirect(action:'show',id:inventoryTransferInstance?.id)
	}
	
	def retrieveInventoryTransferItems = {
		def searchField = params.sSearch
		def products = Product.list().findAll {it.identifier.toLowerCase() =~ searchField.toLowerCase() || it.description.toLowerCase() =~ searchField.toLowerCase()}
		def data = params.originWarehouseId == "null" ? [] : createInventoryTransferItemsDataMap(products)

		def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
		render jsonResponse as JSON
	}

	private List createInventoryTransferItemsDataMap(List products) {
		def data = []
		def originWarehouseId = params.originWarehouseId
		def destinationWarehouseField = params.destination
		def warehouseNotNull = originWarehouseId != "null"
		def warehouse = warehouseNotNull ? Warehouse.get(originWarehouseId.toLong()) : null
		products?.sort{it.description}.each {
				def originStock = Stock.findByProductAndWarehouse(it, warehouse)
				def destinationWarehouse = Warehouse.findByIdentifier(destinationWarehouseField)
				def destinationStock = Stock.findByProductAndWarehouse(it, destinationWarehouse)
				if(originStock && originStock.qty > BigDecimal.ZERO) {
					def dataMap = []
					dataMap.addAll(
						it.id,
						it.identifier,
						it?.formatDescription(),
						originStock.qty,
						destinationStock ? destinationStock.qty : BigDecimal.ZERO
					)
					data.add(dataMap)
				}
		}
		return data
	}
	
	def updateDestinationWarehouseSelectType = {
		def warehouses = []
		def originWarehouseId = params.selectedValue
		def warehouseNotNull = originWarehouseId != "null"
		if(warehouseNotNull){
			def warehouse = warehouseNotNull ? Warehouse.get(originWarehouseId.toLong()) : null

			Warehouse.list().sort{it.identifier}.each { 
				if(warehouse.identifier != it.identifier) {
					warehouses.add(it.identifier)
				}	
			}
		}
		
		def jsonMap=[warehouses:warehouses]
		render jsonMap as JSON
	}
	
	def cancel = {
		def inventoryTransferInstance = InventoryTransfer.get(params.id)
		inventoryTransferInstance?.cancel()
		inventoryTransferInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		inventoryTransferInstance.receivedBy = ""

		flash.message = "Inventory transfer has been cancelled!"
		redirect(action:'show',id:inventoryTransferInstance?.id)
	}

	def print = {
	}
}
