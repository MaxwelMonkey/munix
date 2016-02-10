package com.munix

class CounterReceiptService {

    static transactional = true
    def editCounterReceiptService
	def customerPaymentService
    def generalMethodService
    def listOfIdObjectGetterService
    def authenticateService

    private final String CANBEUNAPPROVED = "Unapprovable"
    private final String COLLECTIONEXIST = "Collection Exist"
    private final String TIMEPASSED = "Time passed"
    def isUnapprovable(CounterReceipt counterReceipt){
        def result = CANBEUNAPPROVED
        if(hasCollection(counterReceipt)){
            result = COLLECTIONEXIST
        }
        def dateApproved = generalMethodService.getDateFromApprovedBy(counterReceipt.approvedBy)
        def dateTwoDaysAfterApproved = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateApproved, 2)
        def dateToday = generalMethodService.dateToday()

        if(dateToday > dateTwoDaysAfterApproved){
            result = TIMEPASSED
        }
        return result
    }
	
	private boolean hasCollection(CounterReceipt counterReceipt) {
		def collectionSchedules = getCollectionSchedulesForCounterReceipt(counterReceipt)
		return (collectionSchedules.collectionScheduleForCollection || collectionSchedules.collectionScheduleForCounter)
	}
	
	def isCancellable(CounterReceipt counterReceipt) {
        def cancellable = true
        if(counterReceipt.isApproved()){
            cancellable = false
        }
		return cancellable
	}
		
    def getCollectionSchedulesForCounterReceipt(CounterReceipt counterReceiptInstance){
        def collectionScheduleForCollection = getCollectionScheduleContainingTheCounterReceiptForCollection(counterReceiptInstance)
        def collectionScheduleForCounter = getCollectionScheduleContainingTheCounterReceiptForCounter(counterReceiptInstance)
        return [collectionScheduleForCollection:collectionScheduleForCollection, collectionScheduleForCounter:collectionScheduleForCounter]
    }
    private getCollectionScheduleContainingTheCounterReceiptForCounter(CounterReceipt counterReceiptInstance){
        def collectionScheduleInstanceList = CollectionSchedule.withCriteria(){
            items{
                and{
                    eq('counterReceipt', counterReceiptInstance)
                    or{
                        eq("type", "Counter")
                        eq("type", "Both")
                    }
                }
            }
        }
		return collectionScheduleInstanceList 
    }
    private getCollectionScheduleContainingTheCounterReceiptForCollection(CounterReceipt counterReceiptInstance){
        def collectionScheduleInstanceList = CollectionSchedule.withCriteria(){
            items{
                and{
                    eq('counterReceipt', counterReceiptInstance)
                    or{
                        eq("type", "Collection")
                        eq("type", "Both")
                    }
                }
            }
        }
		return collectionScheduleInstanceList
    }
    def getAvailableCustomerPaymentsForCounterReceipt(CounterReceipt counterReceiptInstance) {
    	return customerPaymentService.getAvailableCustomerPaymentsForCounterReceipt(counterReceiptInstance?.customer)
    }

	def getCustomerPaymentsForCounterReceipt(CounterReceipt counterReceiptInstance) {
		def parameters = getAvailableCustomerPaymentsForCounterReceipt(counterReceiptInstance)
		parameters.charges?.addAll(counterReceiptInstance?.charges)
		parameters.deliveries?.addAll(counterReceiptInstance?.deliveries)
		parameters.creditMemos?.addAll(counterReceiptInstance?.creditMemos)
		parameters.bouncedChecks?.addAll(counterReceiptInstance?.bouncedChecks)
		return parameters
	}
		
    def updateCounterReceipt(CounterReceipt counterReceiptInstance, Map mapOfCustomerPayments){
    	def items = generateItemsFromCustomerPayments(counterReceiptInstance, mapOfCustomerPayments)
		counterReceiptInstance = updateItemsOfCounterReceipt(counterReceiptInstance, items)
		
        if (!counterReceiptInstance.hasErrors() && counterReceiptInstance.save(flush: true)) {
            return true
        }
        else {
            return false
        }
    }

    def saveCounterReceipt(CounterReceipt counterReceiptInstance, Map mapOfCustomerPayments){
		def items = generateItemsFromCustomerPayments(counterReceiptInstance, mapOfCustomerPayments)
        counterReceiptInstance = editCounterReceiptService.addItemsToCounterReceipt(counterReceiptInstance, items)

        if (counterReceiptInstance.save(flush: true) && !counterReceiptInstance.hasErrors()) {
            return true
        } else {
            return false
        }
    }


    def validateAndApproveCounterReceipt(CounterReceipt counterReceiptInstance){
        if(!checkForInvoiceExistence(counterReceiptInstance)){
            return [result:false,message:"Counter Receipt cannot be approved because no invoices has been selected!"]
        }else{
            counterReceiptInstance?.approve()
            counterReceiptInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
            counterReceiptInstance?.save()
            return [result:true,message:"Counter Receipt has been successfully approved!"]
        }
    }
	
	private def generateItemsFromCustomerPayments(CounterReceipt counterReceiptInstance, Map mapOfCustomerPayments) {
		def items = []
        mapOfCustomerPayments.deliveries.each {
			def item = CounterReceiptItem.retrieveCounterReceiptItem(counterReceiptInstance, it, CustomerPaymentType.SALES_DELIVERY)
			if (item) {
				items.add(item)
			} else {
				def delivery = SalesDelivery.get(it)
				items.add(new CounterReceiptItem(counterReceipt: counterReceiptInstance, invoice: delivery, amount: delivery.computeProjectedDue()))
			}
        }
        mapOfCustomerPayments.charges.each {
            def item = CounterReceiptItem.retrieveCounterReceiptItem(counterReceiptInstance, it, CustomerPaymentType.CUSTOMER_CHARGE)
			if (item) {
				items.add(item)
			} else {
				def charge = CustomerCharge.get(it)
				items.add(new CounterReceiptItem(counterReceipt: counterReceiptInstance, invoice: charge, amount: charge.computeProjectedDue()))
			}
        }
		mapOfCustomerPayments.creditMemos.each {
			def item = CounterReceiptItem.retrieveCounterReceiptItem(counterReceiptInstance, it, CustomerPaymentType.CREDIT_MEMO)
			if (item) {
				items.add(item)
			} else {
				def creditMemo = CreditMemo.get(it)
				items.add(new CounterReceiptItem(counterReceipt: counterReceiptInstance, invoice: creditMemo, amount: creditMemo.computeCreditMemoTotalAmount()))
			}
		}
		mapOfCustomerPayments.bouncedChecks.each {
			def item = CounterReceiptItem.retrieveCounterReceiptItem(counterReceiptInstance, it, CustomerPaymentType.BOUNCED_CHECK)
			if (item) {
				items.add(item)				
			} else {
				def bouncedCheck = BouncedCheck.get(it)
				items.add(new CounterReceiptItem(counterReceipt: counterReceiptInstance, invoice: bouncedCheck, amount: bouncedCheck.computeTotalAmount()))
			}
		}
		return items
	}
	
    private def checkForInvoiceExistence(CounterReceipt counterReceiptInstance){
        if(counterReceiptInstance.getDeliveries() || counterReceiptInstance.getCharges() || counterReceiptInstance.getCreditMemos() || counterReceiptInstance.getBouncedChecks()){
            return true
        }else{
            return false
        }
    }
	
    private def checkForNonZeroBalance(CounterReceipt counterReceiptInstance){
        def status = true
        counterReceiptInstance.getDeliveries().each{
            if(it.computeAmountDue()==BigDecimal.ZERO) { 
                status = false
            }
        }
        counterReceiptInstance.getCharges().each{
            if(it.computeProjectedDue()==BigDecimal.ZERO){
                status = false
            }
        }
        counterReceiptInstance.getCreditMemos().each{
            if(it.isPaid()){
                status = false
            }
        }
        counterReceiptInstance.getBouncedChecks().each{
            if(it.isPaid()){
                status = false
            }
        }
        return status
    }

    private def updateItemsOfCounterReceipt(CounterReceipt counterReceiptInstance, List<CounterReceiptItem> updatedItems){
        def currentItems = []
        if(counterReceiptInstance.items){
            currentItems.addAll(counterReceiptInstance.items)
        }
        def toBeRemoved = generalMethodService.removeSameItems(currentItems, updatedItems)
		counterReceiptInstance = editCounterReceiptService.removeItemsFromCounterReceipt(counterReceiptInstance, toBeRemoved)

        def toBeAdded = generalMethodService.removeSameItems(updatedItems, currentItems)
        counterReceiptInstance = editCounterReceiptService.addItemsToCounterReceipt(counterReceiptInstance, toBeAdded)

        return counterReceiptInstance
    }

	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["collectionDateFrom"] = generalMethodService.createCalendarStructDate(params.collectionDateFrom_value)
		dateMap["collectionDateTo"] = generalMethodService.createCalendarStructDate(params.collectionDateTo_value)
		dateMap["counterDateFrom"] = generalMethodService.createCalendarStructDate(params.counterDateFrom_value)
		dateMap["counterDateTo"] = generalMethodService.createCalendarStructDate(params.counterDateTo_value)
		dateMap["dateFrom"] = generalMethodService.createCalendarStructDate(params.dateFrom_value)
		dateMap["dateTo"] = generalMethodService.createCalendarStructDate(params.dateTo_value)

		return dateMap
	}
	
	def generateList(Map params) {
		def searchID = params.id
		def searchCustomerName = params.customerName
		def searchCustomerId = params.customerId
		def searchStatus = params.status
		def searchDate
		def dateFrom = params.dateFrom
		def dateTo = params.dateTo
		def collectionDateFrom = params.collectionDateFrom
		def collectionDateTo = params.collectionDateTo
		def counterDateFrom = params.counterDateFrom
		def counterDateTo = params.counterDateTo
		def collectionDate = params.collectionDate
		def counterDate = params.counterDate
		
		if(params.dateFrom) {
			dateFrom = generalMethodService.createDate(params?.dateFrom_value)
	    }
		if(params.dateTo) {
			dateTo = generalMethodService.createDate(params?.dateTo_value)
			dateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}
		
		if(params.collectionDateFrom) {
			collectionDateFrom = generalMethodService.createDate(params?.collectionDateFrom_value)
		}
		if(params.collectionDateTo) {
			collectionDateTo = generalMethodService.createDate(params?.collectionDateTo_value)
			collectionDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, collectionDateTo, 1)
		}
		
		if(params.counterDateFrom) {
			counterDateFrom = generalMethodService.createDate(params?.counterDateFrom_value)
		}
		if(params.counterDateTo) {
			counterDateTo = generalMethodService.createDate(params?.counterDateTo_value)
			counterDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, counterDateTo, 1)
		}
		
		def query = {
			and{
				if(searchID) {
					like('id', searchID.toLong())
				}
				
				if(searchCustomerName) {
					customer{
						ilike("name","%${searchCustomerName}%")
						if(params.order == 'asc'){
							order("name","des")
						}else{
							order("name", "asc")
						}
					}
				}
				
				if(searchCustomerId) {
					customer{
						ilike("identifier","%${searchCustomerId}%")
						if(params.order == 'asc'){
							order("identifier","des")
						}else{
							order("identifier", "asc")
						}
					}
				}

				if(searchStatus) {
					eq("status", searchStatus == "Approved" ? CounterReceipt.Status.APPROVED : searchStatus == "Unapproved" ? CounterReceipt.Status.UNAPPROVED : CounterReceipt.Status.CANCELLED)
				}
				
				if(collectionDate == "Yes") {
					isNotNull("collectionDate")	
				} 
				
				if(collectionDate == "No") {
					isNull("collectionDate")
				}
				
				if(counterDate == "Yes") {
					isNotNull("counterDate")
				}
				if(counterDate == "No") {
					isNull("counterDate")
				}
				
				if(dateFrom && dateTo) {
					ge('date',dateFrom)
					lt('date',dateTo)
				} else if(dateFrom) {
					ge('date',dateFrom)
				} else if(dateTo) {
					lt('date',dateTo)
				}
				
				if(collectionDateFrom && collectionDateTo) {
					ge('collectionDate',collectionDateFrom)
					lt('collectionDate',collectionDateTo)
				} else if(collectionDateFrom) {
					ge('collectionDate',collectionDateFrom)
				} else if(collectionDateTo) {
					lt('collectionDate',collectionDateTo)
				}
				
				if(counterDateFrom && counterDateTo) {
					ge('counterDate',counterDateFrom)
					lt('counterDate',counterDateTo)
				} else if(counterDateFrom) {
					ge('counterDate',counterDateFrom)
				} else if(counterDateTo) {
					lt('counterDate',counterDateTo)
				}
			}
		}
		
		return CounterReceipt.createCriteria().list(params, query)
	}
}
