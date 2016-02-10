package com.munix

class PurchaseInvoiceService {
	def generalMethodService
	def authenticateService
	def stockCardService
	def productService
	
    static transactional = false

	public Set queryAvailablePurchaseOrderItems(Supplier supplier, String productSearchCriterion) {
		def purchaseOrderItemsList = PurchaseOrderItem.withCriteria {
			po {
				and {
					eq('supplier', supplier)
					eq('status', "Approved")
				}				
			}
		}
		purchaseOrderItemsList = purchaseOrderItemsList?.findAll{it.product.description.toLowerCase() =~ productSearchCriterion.toLowerCase() || it.product.identifier.toLowerCase() =~ productSearchCriterion.toLowerCase()}
		return new HashSet(purchaseOrderItemsList)
	}

    public void processInvoiceItems(PurchaseInvoice purchaseInvoiceInstance) {
		removeReceivedItemsFromPoItem(purchaseInvoiceInstance)
		addReceivedItemsToPoItem(purchaseInvoiceInstance)
	}
	
    public String getPurchaseInvoiceItemsWithExceedingQuantityMessage(PurchaseInvoice purchaseInvoiceInstance) {
    	def purchaseInvoiceItemsWithExceedingQuantity = getPurchaseInvoiceItemsWithExceedingQuantity(purchaseInvoiceInstance)
    	return getExceedingQuantityMessage(purchaseInvoiceItemsWithExceedingQuantity)
    }
	
	private List getPurchaseInvoiceItemsWithExceedingQuantity(PurchaseInvoice purchaseInvoiceInstance) {
		def purchaseInvoiceItemsWithExceedingQuantity = []
		purchaseInvoiceInstance.items.each {
			if (it.qty > it.purchaseOrderItem.computeRemainingBalance()) {
				purchaseInvoiceItemsWithExceedingQuantity.add(it)
			}
		}
		return purchaseInvoiceItemsWithExceedingQuantity
	}
    
	private addReceivedItemsToPoItem(purchaseInvoiceInstance){
		purchaseInvoiceInstance.items.each {
			if(!it.id){
				it.purchaseOrderItem.addToReceivedItems(it)
			}
		}
	}
	
	private removeReceivedItemsFromPoItem(PurchaseInvoice purchaseInvoiceInstance) {
		def toBeDeleted = purchaseInvoiceInstance.invoiceItemList?.findAll { it.isDeleted }
		if (toBeDeleted) {
			toBeDeleted.each {
				purchaseInvoiceInstance.removeFromItems(it)
				if (it?.purchaseOrderItem?.receivedItems?.contains(it)) {
					it?.purchaseOrderItem?.removeFromReceivedItems(it)
				}
				it.delete()
			}
		}
	}
	
	private String getExceedingQuantityMessage(List purchaseInvoiceItemsWithExceedingQuantity) {
		def exceedMessage = ""
		if (purchaseInvoiceItemsWithExceedingQuantity) {
			purchaseInvoiceItemsWithExceedingQuantity.each {
				exceedMessage += "Invoice Item " + it.purchaseOrderItem.po.formatId() + " - " + it.purchaseOrderItem + " exceeds ordered quantity.<br/>"
			}
		}
		return exceedMessage
	}
	
	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["searchInvoiceDateFrom"] = generalMethodService.createCalendarStructDate(params.searchInvoiceDateFrom_value)
		dateMap["searchInvoiceDateTo"] = generalMethodService.createCalendarStructDate(params.searchInvoiceDateTo_value)
		dateMap["searchDeliveryDateFrom"] = generalMethodService.createCalendarStructDate(params.searchDeliveryDateFrom_value)
		dateMap["searchDeliveryDateTo"] = generalMethodService.createCalendarStructDate(params.searchDeliveryDateTo_value)
		
		return dateMap
	}
	
	def generateList(Map params) {
		def searchReference = params.reference
		def searchSupplierReference = params.supplierReference
		def searchSupplier = params.supplier
		def searchStatus = params.status
		def searchType = params.type
		def searchInvoiceStatus = params.searchInvoiceStatus
		def searchInvoiceDateFrom
		def searchInvoiceDateTo
		def searchDeliveryDateFrom
		def searchDeliveryDateTo
		
		if(params?.searchInvoiceDateFrom){
			searchInvoiceDateFrom = generalMethodService.createDate(params?.searchInvoiceDateFrom_value)
		}
		
		if(params?.searchInvoiceDateTo){
			def dateTo = generalMethodService.createDate(params?.searchInvoiceDateTo_value)
			searchInvoiceDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}

		if(params?.searchDeliveryDateFrom){
			searchDeliveryDateFrom = generalMethodService.createDate(params?.searchDeliveryDateFrom_value)
		}
		if(params?.searchDeliveryDateTo){
			def dateTo = generalMethodService.createDate(params?.searchDeliveryDateTo_value)
			searchDeliveryDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}

		def query = {
			and{
				if(searchReference) {
					like('reference', "%${searchReference}%")
				}
				
				if(searchSupplierReference) {
					like('supplierReference', "%${searchSupplierReference}%")
				}
				
				if(searchSupplier) {
					supplier{
						like('name', "%${searchSupplier}%")
					}
				}
				
				if(searchStatus) {
					eq("status", searchStatus == "Approved" ? PurchaseInvoice.Status.APPROVED : searchStatus == "Unapproved" ? PurchaseInvoice.Status.UNAPPROVED : searchStatus == "Paid" ? PurchaseInvoice.Status.PAID : PurchaseInvoice.Status.CANCELLED)
				}
				
				if(searchDeliveryDateFrom&&searchDeliveryDateTo){
					ge('deliveryDate',searchDeliveryDateFrom)
					lt('deliveryDate',searchDeliveryDateTo)
				}else if(searchDeliveryDateFrom){
					ge('deliveryDate',searchDeliveryDateFrom)
				}else if(searchDeliveryDateTo){
					lt('deliveryDate',searchDeliveryDateTo)
				}
				
				if(searchInvoiceDateFrom&&searchInvoiceDateTo){
					ge('invoiceDate',searchInvoiceDateFrom)
					lt('invoiceDate',searchInvoiceDateTo)
				}else if(searchInvoiceDateFrom){
					ge('invoiceDate',searchInvoiceDateFrom)
				}else if(searchInvoiceDateTo){
					lt('invoiceDate',searchInvoiceDateTo)
				}
				
				if(searchType) {
					eq("type", searchType)
				}
			}
		}
		
		return PurchaseInvoice.createCriteria().list(params, query)
	}
	
	def boolean approve(PurchaseInvoice purchaseInvoice) {
		def result = false
		purchaseInvoice?.approved()
		purchaseInvoice.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		updatePurchaseOrderItemsOfApprovedPurchaseInvoice(purchaseInvoice)
		
		if (purchaseInvoice?.save()) {
			stockCardService.createApprovedPurchaseInvoice(purchaseInvoice)
			result = true
		}
		return result
	}
	
	private void updatePurchaseOrderItemsOfApprovedPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		purchaseInvoice.items?.each {
			def purchaseOrderItem = it.purchaseOrderItem
			if(purchaseOrderItem.computeRemaining() <= 0){
				purchaseOrderItem.isComplete = true
			}
			purchaseOrderItem.receivedQty += it.qty
			purchaseOrderItem.product.getStock(purchaseInvoice.warehouse).qty += it.qty
			productService.updateRunningCost(purchaseOrderItem.product, it.localDiscountedPrice)
			productService.updateRunningCostInForeignCurrency(purchaseOrderItem.product, it.discountedPrice, it.purchaseInvoice?.supplier?.currency, it.purchaseInvoice?.exchangeRate)
		}
	}
	
	boolean unapprove(PurchaseInvoice purchaseInvoice) {
		def result = true
        purchaseInvoice.items.each{
            if(it.purchaseOrderItem?.po?.isComplete()){
                result = false
            }
        }
        if(purchaseInvoice.payment && !purchaseInvoice.payment.isCancelled()){
            result = false
        }
        if(result){
            purchaseInvoice?.unapproved()
            purchaseInvoice.approvedBy = ""
            updatePurchaseOrderItemsOfUnapprovedPurchaseInvoice(purchaseInvoice)
            if (purchaseInvoice?.save()) {
                stockCardService.createUnapprovedPurchaseInvoice(purchaseInvoice)
                result = true
            }
        }
		return result
	}
	
	private void updatePurchaseOrderItemsOfUnapprovedPurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		purchaseInvoice.items?.each {
			def purchaseOrderItem = it.purchaseOrderItem
			purchaseOrderItem.isComplete = false
			purchaseOrderItem.receivedQty -= it.qty
			purchaseOrderItem.product.getStock(purchaseInvoice.warehouse).qty -= it.qty
			productService.updateRunningCost(purchaseOrderItem.product)
	   }
	}
    def isCancelable(PurchaseInvoice purchaseInvoice){
        def cancelable = false
        if(purchaseInvoice.isUnapproved()){
            cancelable = true
        }
        return cancelable
    }
}
