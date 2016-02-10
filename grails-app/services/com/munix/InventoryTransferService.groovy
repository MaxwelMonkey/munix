package com.munix

import java.util.Map;

class InventoryTransferService {

    static transactional = true
	def generalMethodService
	
    def generateInventoryTransferItems(InventoryTransfer inventoryTransferInstance) {
        def items = []
		
        inventoryTransferInstance.items.eachWithIndex { item, idx->
            def itemsMap = [:]
			def originWarehouseStock = inventoryTransferInstance.isReceived() ? item.originWarehouseQty : Stock.findByProductAndWarehouse(item.product, inventoryTransferInstance.originWarehouse).qty
			def destinationWarehouseStock = inventoryTransferInstance.isReceived() ? item.destinationWarehouseQty : Stock.findByProductAndWarehouse(item.product, inventoryTransferInstance.destinationWarehouse).qty
			
            itemsMap["productId"] = item.product.id
            itemsMap["identifier"] = item.product?.identifier
            itemsMap["description"] = item.product?.description
			itemsMap["originWarehouseStock"] = originWarehouseStock
			itemsMap["destinationWarehouseStock"] = destinationWarehouseStock
			itemsMap["qty"] = item.qty
			itemsMap["isDeleted"] = item.isDeleted
			itemsMap["index"] = idx
            items.add(itemsMap)
        }
        return items.sort{it.description}
    }
	
	def updateProductStock(InventoryTransfer inventoryTransferInstance) {
		inventoryTransferInstance?.items.each{ item ->
			def quantity = item.qty
			def originStock = Stock.findByProductAndWarehouse(item.product,inventoryTransferInstance.originWarehouse)
			def destinationStock = Stock.findByProductAndWarehouse(item.product,inventoryTransferInstance.destinationWarehouse)
			item.originWarehouseQty = originStock.qty
			item.destinationWarehouseQty = destinationStock.qty
			originStock.qty -= quantity
			destinationStock.qty += quantity
			
			originStock.save()
			destinationStock.save()
			item.save()
		}
	}
	
	def validateInventoryTransfer(InventoryTransfer inventoryTransferInstance) {
		def isValid = true
		inventoryTransferInstance?.items.each{ item ->
			def quantity = item.qty
			def originStock = Stock.findByProductAndWarehouse(item.product,inventoryTransferInstance.originWarehouse)
			
			if(originStock.qty < quantity) {
				isValid = false
			}
		}
		return isValid
	}
	
	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["searchDateCreatedFrom"] = generalMethodService.createCalendarStructDate(params.searchDateCreatedFrom_value)
		dateMap["searchDateCreatedTo"] = generalMethodService.createCalendarStructDate(params.searchDateCreatedTo_value)
		
		return dateMap
	}

	def generateList(params){
		def searchId = params.searchId
		def searchOriginWarehouse = params.searchOriginWarehouse
		def searchDestinationWarehouse = params.searchDestinationWarehouse
		def searchStatus = params.searchStatus
		def searchDateCreatedFrom
		if(params?.searchDateCreatedFrom){
			searchDateCreatedFrom = generalMethodService.createDate(params?.searchDateCreatedFrom_value)
		}
		def searchDateCreatedTo
		if(params?.searchDateCreatedTo){
			def dateTo = generalMethodService.createDate(params?.searchDateCreatedTo_value)
			searchDateCreatedTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}
		
		def query = {
			and {
				if(searchOriginWarehouse) {
					originWarehouse{
						eq('id', searchOriginWarehouse.toLong())
					}
				}

				if(searchDestinationWarehouse) {
					destinationWarehouse{
						eq('id', searchDestinationWarehouse.toLong())
					}
				}

				if(searchStatus){
					eq('status', com.munix.InventoryTransfer.Status.getStatusByName(searchStatus))
				}
				
				if(searchDateCreatedFrom && searchDateCreatedTo) {
					ge('date', searchDateCreatedFrom)
					lt('date', searchDateCreatedTo)
				} else if(searchDateCreatedFrom) {
					ge('date', searchDateCreatedFrom)
				} else if(searchDateCreatedTo) {
					lt('date', searchDateCreatedTo)
				}
			}
		}
		
		def inventoryTransfers = InventoryTransfer.createCriteria().list(query)
		if(searchId){
			inventoryTransfers = inventoryTransfers.findAll{it.toString() =~ searchId}
		}
		def inventoryTransfersTotal = inventoryTransfers.size()
		return [inventoryTransfers: generalMethodService.paginateList(inventoryTransfers, inventoryTransfersTotal, params), inventoryTransfersTotal: inventoryTransfersTotal]
	}
}
