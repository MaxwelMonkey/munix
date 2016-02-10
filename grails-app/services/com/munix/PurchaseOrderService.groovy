package com.munix

import java.util.Map;

class PurchaseOrderService {

	boolean transactional = true
	def generalMethodService
	
	def updateComplete(purchaseOrderInstance,params){
		purchaseOrderInstance.items.each{
			if(params["completeElement${it.id}"]){
				it.isComplete=true
			}else{
				it.isComplete=false
			}
			it.save()
		}
	}

	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["startDate"] = generalMethodService.createCalendarStructDate(params.startDate_value)
		dateMap["endDate"] = generalMethodService.createCalendarStructDate(params.endDate_value)
		
		return dateMap
	}

	def getPurchaseOrderList(params) {
		def searchId = params.poId
		def searchStartDate = params.startDate
		def searchEndDate = params.endDate
		def searchSupplier = params.supplier
		def searchStatus = params.status
		def startDate
		def endDate

		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "poId"
		if (!params.order) params.order = "asc"
		
		if(params.startDate){
			startDate = generalMethodService.createDate(params?.startDate_value)
		}
		
		if(params.endDate){
			def dateTo = generalMethodService.createDate(params?.endDate_value)
			endDate = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}

		def query = {
			and{
				//Search
				if(startDate&&endDate){
					ge('date',startDate)
					lt('date',endDate)
				}else if(startDate){
					ge('date',startDate)
				}else if(endDate){
					lt('date',endDate)
				}

				if(searchSupplier){
					supplier{
						like('name', "%${searchSupplier}%")
					}
				}

				if(searchStatus == null) {
					ne('status', 'Complete')
				} else if(searchStatus){
					eq('status', searchStatus)
				}

				//Sort
				if (params.sort == 'poId') {
					poId {
						order('purchaseOrderId', params.order)
					}
				}
			}
		}
		
		def purchaseOrders = PurchaseOrder.createCriteria().list(params,query).sort{it.formatId()}.sort{it.supplier.identifier}
		if(searchId){
			purchaseOrders = purchaseOrders.findAll{it.formatId() =~ searchId}
		}
		
		def purchaseOrdersTotal = purchaseOrders.size()
		return [purchaseOrders: generalMethodService.paginateList(purchaseOrders, purchaseOrdersTotal, params), purchaseOrdersTotal: purchaseOrdersTotal]
	}
	
    def isUnapprovable(PurchaseOrder purchaseOrderInstance){
        def unapprovable = true
        def onUsedPurchaseInvoiceItems = PurchaseInvoiceItem.findAllByPurchaseOrderItemInList(purchaseOrderInstance.items)
        onUsedPurchaseInvoiceItems.each{

            if(!it.purchaseInvoice.isCancelled()){
                unapprovable = false
            }
        }
        return unapprovable
    }
}
