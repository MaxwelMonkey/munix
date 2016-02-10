package com.munix

import java.util.Map;

class SalesDeliveryService {
    private static final String CANCELLED= "Cancelled"
    static transactional = true
    def authenticateService
	def customerLedgerService
	def stockCardService
	def generalMethodService

    def listOfSalesDeliveryForCreditMemo(Customer customerInstance, DiscountType discountTypeInstance, CreditMemo creditMemo) {
    	def cal = Calendar.getInstance()
   		cal.add(Calendar.MONTH, -4)
        def salesDeliveryList = SalesDelivery.withCriteria {
            order ("salesDeliveryId","asc")
            and{
                eq("customer",customerInstance)
                invoice{
                    eq("discountType",discountTypeInstance)
                }
                ne("status",CANCELLED)
                if(authenticateService.ifNotGranted("ROLE_MANAGER_ACCOUNTING") && creditMemo.applyFourMonthRule )
					gt("date", cal.getTime())
            }
        }
        return salesDeliveryList
    }
	
    def listSalesDeliveryItemForCreditMemo(SalesDelivery salesDeliveryInstance, List<SalesDeliveryItem> deliveryItemList){
        def salesDeliveryItemList
        if(!deliveryItemList){
            salesDeliveryItemList=salesDeliveryInstance.items
        }
        else{
            salesDeliveryItemList = SalesDeliveryItem.withCriteria{
                order('product','asc')
                and{
                    eq("delivery",salesDeliveryInstance)
                    not{
                        "in"('id',deliveryItemList)
                    }
                }
            }
        }
        return salesDeliveryItemList
    }
	
    def showUnapproveButton(SalesDelivery salesDeliveryInstance){
        def showUnapprove = true
        if(salesDeliveryInstance.invoices || salesDeliveryInstance.counterReceipts || salesDeliveryInstance.directDelivery){
            showUnapprove = false
        }
        if(showUnapprove){
            salesDeliveryInstance.items.creditMemoItems.each{
                if(!it.isEmpty()){
                    showUnapprove = false
                }
            }
        }
        return showUnapprove
    }
	
	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["searchDateFrom"] = generalMethodService.createCalendarStructDate(params.searchDateFrom_value)
		dateMap["searchDateTo"] = generalMethodService.createCalendarStructDate(params.searchDateTo_value)
		
		return dateMap
	}

	def generateList(Map params) {
		def searchIdentifier = params.searchIdentifier
		def searchInvoice = params.searchInvoice
		def searchCustomerId = params.searchCustomerId
		def searchCustomerName = params.searchCustomerName
		def searchCustomerType = params.searchCustomerType
		def searchDiscountType = params.searchDiscountType
		def searchDeliveryType = params.searchDeliveryType
		def searchStatus = params.searchStatus
		def searchDateFrom
		if(params?.searchDateFrom){
			searchDateFrom = generalMethodService.createDate(params?.searchDateFrom_value)
		}
		def searchDateTo
		if(params?.searchDateTo){
			def dateTo = generalMethodService.createDate(params?.searchDateTo_value)
			searchDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}
		
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "salesDeliveryId"
		if (!params.order) params.order = "asc"
		def query = {
			and{
				//Search
				if(searchIdentifier){
					like('salesDeliveryId', "%${searchIdentifier}%")
				}

				if(searchInvoice){
					invoice{
						eq('id', Long.parseLong(searchInvoice))
					}
				}

				if(searchCustomerId){
					customer{
						like('identifier', "%${searchCustomerId}%")
					}
				}
				if(searchCustomerName){
					customer{
						like('name', "%${searchCustomerName}%")
					}
				}
				
				if(searchCustomerType){
					customer{
						type{
							eq('description', searchCustomerType)
						}
					}
				}

				if(searchDiscountType){
					invoice{
						discountType{
							eq('description', searchDiscountType)
						}
					}
				}

				if(searchDeliveryType){
					eq('deliveryType', searchDeliveryType)
				}

				if(searchDateFrom&&searchDateTo){
					ge('date',searchDateFrom)
					lt('date',searchDateTo)
				}else if(searchDateFrom){
					ge('date',searchDateFrom)
				}else if(searchDateTo){
					lt('date',searchDateTo)
				}

				if(searchStatus){
					if(searchStatus!="All statuses"){
						eq('status', searchStatus)
					}
				}else{
					not{
						eq('status', "Cancelled")
						eq('status', "Paid")
					}
				}

				//Sort
				if (params.sort == 'type') {
					type {
						order('identifier', params.order)
					}
				}

				else if (params.sort == 'customer') {
					customer {
						order('identifier', params.order)
					}
				}

				else if (params.sort == 'discountType') {
					discountType {
						order('identifier', params.order)
					}
				}
			}
		}

		def salesDeliveryInstanceList = SalesDelivery.createCriteria().list(params, query)
		def salesDeliveryInstanceTotal = SalesDelivery.createCriteria().count(query)
		return [salesDeliveryInstanceList: salesDeliveryInstanceList, salesDeliveryInstanceTotal: salesDeliveryInstanceTotal]
	}
	
    def approveSalesDelivery(SalesDelivery salesDeliveryInstance){
    	if(!salesDeliveryInstance.isApproved()){
	        salesDeliveryInstance?.approve()
	        salesDeliveryInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
	        salesDeliveryInstance?.items.each { item ->
				def salesInvoiceItem = item.orderItem
	            salesInvoiceItem.deliveredQty += item.qty
	
	            //update stocks
	            def stock = item.product?.getStock(item?.delivery?.warehouse)
	            if(stock){
	                stock.qty -= item.qty
	            }
	        }
	        if(!salesDeliveryInstance.hasErrors() && salesDeliveryInstance?.save(flush:true)){
	        	salesDeliveryInstance.addToCustomerAccountSDAmount(salesDeliveryInstance.computeTotalAmount())
	        	salesDeliveryInstance.updateItemCosts()
	        	customerLedgerService.createApprovedSalesDelivery(salesDeliveryInstance)
	        	stockCardService.createApprovedSalesDeliveryStockCards(salesDeliveryInstance)
	            return true
	        } else {
	            return false
	        }
    	}else{
    		return true
    	}
    }
	
    def unapproveSalesDelivery(SalesDelivery salesDeliveryInstance){
        salesDeliveryInstance?.unapprove()
        salesDeliveryInstance.approvedBy = ""
        salesDeliveryInstance.autoApprovedBy = ""
        salesDeliveryInstance?.items.each { item ->
            def salesInvoiceItem = item.orderItem
            salesInvoiceItem.deliveredQty -= item.qty

            //update stocks
            def stock = item.product?.getStock(item?.delivery?.warehouse)
            if(stock){
                stock.qty += item.qty
            }
        }
        salesDeliveryInstance.validate()
        if(!salesDeliveryInstance.hasErrors() && salesDeliveryInstance?.save(flush:true)){
			salesDeliveryInstance.removeFromCustomerAccountSDAmount(salesDeliveryInstance.computeAmountDue())
        	salesDeliveryInstance.removeItemCosts()
			customerLedgerService.createUnapprovedSalesDelivery(salesDeliveryInstance)
			stockCardService.createUnapprovedSalesDeliveryStockCards(salesDeliveryInstance)
            return true
        } else {
        	salesDeliveryInstance.errors?.allErrors?.each { println it } 
            return false
        }
    }
	
	def generateWarehouseList() {
		return Warehouse.list().sort{it.identifier}
	}
	
	def generateOrderItems(SalesOrder salesOrderInstance) {
		def productList = []
		
		salesOrderInstance?.items.sort{it.product.getDescription()}.each {
			if (it.computeRemainingBalance() > 0) {
				def salesOrderItemMap = [:]
				salesOrderItemMap["item"] = it
				salesOrderItemMap["stocks"] = inputStocksValue(it.product.stocks)
				
				productList.add(salesOrderItemMap)
			}
		}
		
		return productList
	}
	
    def generateListOfSalesDeliveryItems(SalesDelivery salesDeliveryInstance){
        def productList = []
        def deliveryItemList = generateDeliveryItems(salesDeliveryInstance)
        def undeliveredItemList = generateUndeliveredItems(salesDeliveryInstance)
        def idx = deliveryItemList.size()
        undeliveredItemList.each{
            def itemMap = [:]
            itemMap["productName"]= it.item.toString()
            itemMap["productId"]= it.item.product.id
            itemMap["price"]= it.item.finalPrice
            itemMap["orderItemId"] = it.item.id
            itemMap["remainingBalance"] = it.item.computeRemainingBalance()?.intValue()
            itemMap["description"] = it.item?.product?.description
            itemMap["qty"] = it.item.qty.intValue()
            itemMap["amount"] = it.item.computeAmount()
            itemMap["stocks"] = it.stocks
            itemMap["isNet"]= it.item.isNet
            itemMap["isDeliver"] = false
            itemMap["idx"] = idx
            productList.add(itemMap)
            idx++
        }
        deliveryItemList.each{
            def itemMap = [:]
            itemMap["productName"]= it.item?.orderItem?.product.toString()
            itemMap["productId"]= it.item.product.id
            itemMap["price"]= it.item.price
            itemMap["orderItemId"] = 0L
            itemMap["remainingBalance"] = it.item?.orderItem?.computeRemainingBalance()?.intValue()
            itemMap["description"] = it.item?.orderItem?.product?.description
            itemMap["qty"] = it.item.qty.intValue()
            itemMap["amount"] = it.item.computeAmount()
            itemMap["stocks"] = it.stocks
            itemMap["isNet"]= it.item.orderItem.isNet
            itemMap["isDeliver"] = true
            itemMap["idx"] = it.idx
            productList.add(itemMap)
        }
        return productList.sort{it.description}
    }
    private generateUndeliveredItems(SalesDelivery salesDeliveryInstance) {
		def deliveredOrderIds = salesDeliveryInstance.items.collect { it.orderItem.id }
		def undeliveredItems = salesDeliveryInstance.invoice.items.findAll { !deliveredOrderIds.contains(it.id)}

		def productList = []
		undeliveredItems.each {
			if (it.computeRemainingBalance() > 0) {
				def salesDeliveryItemMap = [:]
				salesDeliveryItemMap["item"] = it
				salesDeliveryItemMap["stocks"] = inputStocksValue(it.product.stocks)

				productList.add(salesDeliveryItemMap)
			}
		}
		return productList
	}

	private generateDeliveryItems(SalesDelivery salesDeliveryInstance) {
		def productList = []
        def idx = 0
		salesDeliveryInstance?.items.each {
			def salesDeliveryItemMap = [:]
			salesDeliveryItemMap["item"] = it
			salesDeliveryItemMap["idx"] = idx
			salesDeliveryItemMap["stocks"] = inputStocksValue(it.product.stocks)
			productList.add(salesDeliveryItemMap)
            idx++
		}

		return productList
	}
	private Map initializeWarehouseStockMap() {
		def warehouseStockMap = [:]
		Warehouse.list().each {
			warehouseStockMap[it.identifier] = BigDecimal.ZERO
		}
		return warehouseStockMap
	}
	
	private Map inputStocksValue(Set<Stock> stocks) {
		def warehouseStockMap = initializeWarehouseStockMap()
		stocks.each {
			warehouseStockMap[it.warehouse.identifier] = it.qty
		}
		return warehouseStockMap
	}

	def getUnpaidSalesDeliveryList(Customer customer, Map params){
		def query = {
			and {
				eq("customer",customer)
				eq("status", "Unpaid")
			}
		}
		return SalesDelivery.createCriteria().list(params, query)
	}
	
	private def getAllUnpaidSalesDeliveryList(Customer customer){
		return SalesDelivery.findAllByCustomerAndStatus(customer,"Unpaid")
	}
	
	def computeTotalUnpaidSalesDeliveries(Customer customer){
		def salesDeliveries = getAllUnpaidSalesDeliveryList(customer)
		BigDecimal totalUnpaidSalesDeliveries = BigDecimal.ZERO
		salesDeliveries.each{
			totalUnpaidSalesDeliveries += it.computeAmountDue()
		}
		return totalUnpaidSalesDeliveries
	}
	
	boolean saveSalesDelivery(SalesDelivery salesDelivery) {
		if (!salesDelivery.hasErrors() && salesDelivery.save(flush: true, cascade : true)) {
			return true
		} else {
			return false
		}
	}
		
	boolean cancelSalesDelivery(SalesDelivery salesDelivery) {
		def isCancellable = true
        def onUsedDirectPayment = salesDelivery.invoices.find{it.directPayment.isApproved()||it.directPayment.isUnapproved()}
        if(onUsedDirectPayment){
            isCancellable = false
        }
        if(isCancellable){
            salesDelivery?.cancel()
            salesDelivery?.save()
        }
		return isCancellable
	}
	
	boolean checkIfDefaultQuantityHasBeenChanged(SalesDelivery salesDelivery) {
		def result = false
		salesDelivery.items.each {
			if (it.qty != it.orderItem.remainingBalance) {
				result = true
			}
		}
		return result
	}
}
