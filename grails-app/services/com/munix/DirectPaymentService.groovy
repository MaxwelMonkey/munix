package com.munix

import java.text.SimpleDateFormat;
import java.util.Map;

class DirectPaymentService {

    static transactional = true
    def generalMethodService
	def customerPaymentService
    def listOfIdObjectGetterService
	def customerAccountsSummaryService
    private static final APPROVED = "Approved"
	private static final UNPAID = "Unpaid"
	private static final CLOSED = "Closed"
	private static final APPROVABLE = "Approvable"
	private static final NOAPPLIED = "No amount applied"
	private static final NOINVOICE = "No invoice"
	private static final NOENTRIES = "No entries"
	private static final MOREAPPLIED = "Applied more than amount"
	private static final TOTALPAYMENTLESSTHANAPPLIED = "Payment less than applied"

	def generateInvoices(directPaymentInstance, Map mapOfCustomerPaymentLists) {
        def creditMemoList = []
        if(mapOfCustomerPaymentLists.creditMemoList){
            creditMemoList = CreditMemo.getAll(mapOfCustomerPaymentLists.creditMemoList)
        }
        def bouncedCheckList = []
        if(mapOfCustomerPaymentLists.bouncedCheckList){
            bouncedCheckList = BouncedCheck.getAll(mapOfCustomerPaymentLists.bouncedCheckList)
        }
        def deliveryList = []
        if(mapOfCustomerPaymentLists.deliveryList){
            deliveryList = SalesDelivery.getAll(mapOfCustomerPaymentLists.deliveryList)
        }
        def chargeList = []
        if(mapOfCustomerPaymentLists.chargeList){
            chargeList = CustomerCharge.getAll(mapOfCustomerPaymentLists.chargeList)
        }
    	generateInvoicesForCreditMemo(directPaymentInstance, creditMemoList)
		generateInvoicesForBouncedCheck(directPaymentInstance, bouncedCheckList)
		generateInvoicesForSalesDelivery(directPaymentInstance, deliveryList)
    	generateInvoicesForCustomerCharge(directPaymentInstance, chargeList)
    }
	
	def checkAllInvoicesIfTakenByDirectPayment(DirectPayment directPaymentInstance, Map mapOfCustomerPaymentLists) {
		def availableCustomerPayments = getAvailableCustomerPaymentsForDirectPayment(directPaymentInstance.customer)
		def takenByDP = false
		
		def bouncedCheckList = []
		if(mapOfCustomerPaymentLists.bouncedCheckList){
            bouncedCheckList = BouncedCheck.getAll(mapOfCustomerPaymentLists.bouncedCheckList)
			bouncedCheckList.each{
				if(!availableCustomerPayments.bouncedChecks.contains(it)){
					takenByDP = true
				}
			}
		}
		def chargeList = []
		if(mapOfCustomerPaymentLists.chargeList){
            chargeList = CustomerCharge.getAll(mapOfCustomerPaymentLists.chargeList)
			chargeList.each{
				if(!availableCustomerPayments.charges.contains(it)){
					takenByDP = true
				}
			}
		}
		def creditMemoList = []
		if(mapOfCustomerPaymentLists.creditMemoList){
			creditMemoList = CreditMemo.getAll(mapOfCustomerPaymentLists.creditMemoList)
			creditMemoList.each{
				if(!availableCustomerPayments.creditMemos.contains(it)){
					takenByDP = true
				}
			}
		}
		def deliveryList = []
		if(mapOfCustomerPaymentLists.deliveryList){
			deliveryList = SalesDelivery.getAll(mapOfCustomerPaymentLists.deliveryList)
			deliveryList.each{
				if(!availableCustomerPayments.deliveries.contains(it)){
					takenByDP = true
				}
			}
		}
		
		return takenByDP
	}

	List<Map> retrieveDetailsOfItems(DirectPayment directPaymentInstance) {
		def items = []
		
		def latestInvoiceDate = new Date(0);
		directPaymentInstance.invoices.each{
			def cp = it.getRelatedCustomerPayment();
			if(latestInvoiceDate<cp?.date) latestInvoiceDate = cp?.date
			if(it.type == CustomerPaymentType.SALES_DELIVERY){
				def deliveryDate = cp?.directDelivery?.tripTicket?.tripTicket?.date
				if(latestInvoiceDate<deliveryDate) latestInvoiceDate = deliveryDate
			}
		}
		
		directPaymentInstance.items?.sort{it.id}.each {
			def item = [:]
			item.paymentType = it.paymentType
			item.referenceNumber = it?.relatedCreditMemo()?.toString()
			item.deductibleFromSales = it.paymentType.deductibleFromSales
			item.date = it.date
			item.term = ""
			if (it instanceof DirectPaymentItemCheck) {
				item.checkNumber = it?.checkPayment?.checkNumber
				item.checkDate = it?.checkPayment?.date
				item.term = String.format('%,.0f',(item.checkDate.getTime() - latestInvoiceDate.getTime())/1000/60/60/24)
				item.checkBank = it?.checkPayment?.bank?.identifier + " - " + it?.checkPayment?.branch
				item.checkType = it?.checkPayment?.type.routingNumber + " - " + it?.checkPayment?.type.description
			}
			item.debitMemo = it?.relatedCreditMemo()
			item.remark = it?.remark
			item.amount = it?.amount
			items.add(item)
		}
		return items
	}
		
	List<Map> retrieveDetailsOfInvoices(DirectPayment directPaymentInstance) {
		def invoices = []
		
		directPaymentInstance?.invoices?.sort{it.getRelatedCustomerPayment().toString()}?.each {
			def customerPayment = it.getRelatedCustomerPayment()
			def invoice = [:]
			def discountType = ""
			invoice.id = it.id
			invoice.customerPayment = customerPayment
			invoice.type = it.type
			invoice.date = customerPayment?.date
			invoice.amount = customerPayment?.computeTotalAmount().abs()
			invoice.net = customerPayment?.computeProjectedDue()
			invoice.applied = it.amount
			invoice.due = invoice.net + invoice.applied			
			invoice.isInstallment = checkIfPaymentIsInstallment(it)
			if(it.type == CustomerPaymentType.SALES_DELIVERY){
				invoice.deliveryDate = customerPayment?.directDelivery?.tripTicket?.tripTicket?.date
				invoice.discountedDiscount = customerPayment.invoice?.discountGroup
				invoice.netDiscount = customerPayment.invoice?.netDiscountGroup
				invoice.daysDiff = ""
				if(invoice.deliveryDate)
					invoice.daysDiff = String.format('%,.0f',(invoice.deliveryDate.getTime() - invoice.date.getTime())/1000/60/60/24)   
			}
			
			if(it.type == CustomerPaymentType.CREDIT_MEMO) {
				discountType = customerPayment.discountType
			} else if (it.type == CustomerPaymentType.SALES_DELIVERY) {
				discountType = customerPayment.invoice.discountType
			}
			
			invoice.discountType = discountType
			invoices.add(invoice)
		}
		return invoices
	}
	
	Map accumulateTotals(List<Map> invoices) {
		BigDecimal totalAmount = BigDecimal.ZERO
		BigDecimal totalDue = BigDecimal.ZERO
		BigDecimal totalApplied = BigDecimal.ZERO
		BigDecimal totalNet = BigDecimal.ZERO
		
		invoices.each {
			totalAmount += it.amount
			totalDue += it.due
			totalApplied += it.applied
			totalNet += it.net 
		}
		
		return [amount: totalAmount, due: totalDue, applied: totalApplied, net: totalNet]
	}

    def isApprovable(DirectPayment directPaymentInstance){
        def status = APPROVABLE

		def appliedTotal = generateApplied(directPaymentInstance)
        if(checkIfAppliedValueMoreThanAmount(directPaymentInstance.invoices)){
            status = MOREAPPLIED
        }else if(generateComputedBalance(directPaymentInstance, appliedTotal)<BigDecimal.ZERO){
            status = TOTALPAYMENTLESSTHANAPPLIED
        }else if(!directPaymentInstance.invoices){
			status = NOINVOICE
        }else if(!directPaymentInstance.items || directPaymentInstance.items.size()==0){
			status = NOENTRIES
        }else if(appliedTotal==BigDecimal.ZERO){
            status = NOAPPLIED
		}
        return status
    }
    private checkIfAppliedValueMoreThanAmount(directPaymentInvoices){
        def hasMoreAppliedThanAmount = false

        directPaymentInvoices.each{
            if( it.getRelatedCustomerPayment().computeProjectedDue() < BigDecimal.ZERO){
                hasMoreAppliedThanAmount = true
            }
        }
        return hasMoreAppliedThanAmount
    }
    def updateDirectPaymentInvoice(DirectPayment directPaymentInstance, Map mapOfCustomerPaymentLists) {
        def customerPayments = extractCustomerPaymentsFromDirectPayment(directPaymentInstance)

        def creditMemoList = []
        if(mapOfCustomerPaymentLists.creditMemoList){
            creditMemoList = CreditMemo.getAll(mapOfCustomerPaymentLists.creditMemoList)
        }
        def bouncedCheckList = []
        if(mapOfCustomerPaymentLists.bouncedCheckList){
            bouncedCheckList = BouncedCheck.getAll(mapOfCustomerPaymentLists.bouncedCheckList)
        }
        def deliveryList = []
        if(mapOfCustomerPaymentLists.deliveryList){
            deliveryList = SalesDelivery.getAll(mapOfCustomerPaymentLists.deliveryList)
        }
        def chargeList = []
        if(mapOfCustomerPaymentLists.chargeList){
            chargeList = CustomerCharge.getAll(mapOfCustomerPaymentLists.chargeList)
        }
        generateInvoicesForSalesDelivery(directPaymentInstance, generalMethodService.removeSameItems(deliveryList, customerPayments.deliveries))//items to be added
        removeSalesDeliveryInvoiceFromDirectPayment(directPaymentInstance, generalMethodService.removeSameItems(customerPayments.deliveries, deliveryList))//items to be removed
		
        generateInvoicesForCustomerCharge(directPaymentInstance, generalMethodService.removeSameItems(chargeList, customerPayments.charges))
        removeChargeInvoiceFromDirectPayment(directPaymentInstance, generalMethodService.removeSameItems(customerPayments.charges, chargeList))

        generateInvoicesForCreditMemo(directPaymentInstance, generalMethodService.removeSameItems(creditMemoList, customerPayments.creditMemos))
        removeCreditMemoInvoiceFromDirectPayment(directPaymentInstance, generalMethodService.removeSameItems(customerPayments.creditMemos, creditMemoList))
		
        generateInvoicesForBouncedCheck(directPaymentInstance, generalMethodService.removeSameItems(bouncedCheckList, customerPayments.bouncedChecks))
        removeBouncedCheckInvoiceFromDirectPayment(directPaymentInstance, generalMethodService.removeSameItems(customerPayments.bouncedChecks, bouncedCheckList))
    }

    def updateDirectPayment(directPaymentInstance, params, itemId, createdItemId){
        def items=directPaymentInstance.items
        def idList=[]
        items.each {
            idList.add(it.id)
        }
        def existingItems=itemId
        removeItems(directPaymentInstance, generalMethodService.removeSameItems(idList, existingItems))
        if(params.counter!="0"){
            addItems(directPaymentInstance, params,createdItemId)
        }
        updateItems(directPaymentInstance, params, itemId)		
		updateInvoices(directPaymentInstance, params)
    }

    def getAvailableCustomerPaymentsForDirectPayment(Customer customerInstance) {
		return customerPaymentService.getAvailableCustomerPaymentsForDirectPayment(customerInstance)
    }

    def getCustomerPaymentsForDirectPayment(DirectPayment directPaymentInstance){
        def availableCustomerPayments = getAvailableCustomerPaymentsForDirectPayment(directPaymentInstance.customer)
        def existingCustomerPayments = extractCustomerPaymentsFromDirectPayment(directPaymentInstance)

        def deliveries = []
        def charges = []
        def bouncedChecks = []
        def creditMemos = []

        deliveries.addAll(availableCustomerPayments.deliveries)
        charges.addAll(availableCustomerPayments.charges)
        bouncedChecks.addAll(availableCustomerPayments.bouncedChecks)
        creditMemos.addAll(availableCustomerPayments.creditMemos)

        deliveries.addAll(existingCustomerPayments.deliveries)
        charges.addAll(existingCustomerPayments.charges)
        bouncedChecks.addAll(existingCustomerPayments.bouncedChecks)
        creditMemos.addAll(existingCustomerPayments.creditMemos)
		
        return [creditMemos: creditMemos, bouncedChecks: bouncedChecks, deliveries: deliveries, charges: charges]
    }

	def checkAndGenerateOverpayment(balance, directPaymentInstance){
		if (balance>BigDecimal.ZERO){
			if (!directPaymentInstance.overpayment){
				directPaymentInstance.overpayment = new Overpayment(directPayment: directPaymentInstance)
			}
			directPaymentInstance.overpayment.amount = balance
			directPaymentInstance.overpayment.approved()
		}
	}
	def addPendingChecksToCustomerAccountService(DirectPayment directPaymentInstance){
        directPaymentInstance.items.each{
            if(it.paymentType.isCheck){
                customerAccountsSummaryService.addCustomerAccountCheckPayment(it.checkPayment)
            }
        }
    }
    def removePendingChecksFromCustomerAccountService(DirectPayment directPaymentInstance){
        directPaymentInstance.items.each{
            if(it.paymentType.isCheck){
                customerAccountsSummaryService.removeCustomerAccountCheckPayment(it.checkPayment)
            }
        }
    }
    
    def generateApplied(directPaymentInstance){
		def invoices = retrieveDetailsOfInvoices(directPaymentInstance)
		Map invoiceTotals = accumulateTotals(invoices)
		invoiceTotals?.applied ? invoiceTotals?.applied : 0
    }
    
    def generateComputedBalance(directPaymentInstance){
		def appliedTotal = generateApplied(directPaymentInstance)
		generateComputedBalance(directPaymentInstance, appliedTotal)
    }
    
	def generateComputedBalance(directPaymentInstance, appliedTotal){
		BigDecimal paymentTotal = directPaymentInstance?.computePaymentTotal()
		
		BigDecimal balance = paymentTotal - appliedTotal
		return balance
	}

	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["searchDateFrom"] = generalMethodService.createCalendarStructDate(params.searchDateFrom_value)
		dateMap["searchDateTo"] = generalMethodService.createCalendarStructDate(params.searchDateTo_value)
		
		return dateMap
	}

	def generateList(Map params) {
		def searchIdentifier = params.searchIdentifier
		def searchCustomerId = params.searchCustomerId
		def searchCustomerName = params.searchCustomerName
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
		def searchBalance = params.searchBalance

		def query = {
			and{
				if(searchCustomerName){
					customer{
						like('name', "%${searchCustomerName}%")
					}
				}
				
				if(searchCustomerId){
					customer{
						like('identifier', "%${searchCustomerId}%")
					}
				}

				if(searchStatus){
					eq('status', searchStatus)
				}
				
				if(searchDateFrom&&searchDateTo){
					ge('date',searchDateFrom)
					lt('date',searchDateTo)
				}else if(searchDateFrom){
					ge('date',searchDateFrom)
				}else if(searchDateTo){
					lt('date',searchDateTo)
				}
			}
		}

		def directPayments = DirectPayment.createCriteria().list(query)
		if(searchIdentifier){
			directPayments = directPayments.findAll{it.toString() =~ searchIdentifier}
		}
		if (searchBalance=='Yes') {
			directPayments = directPayments.findAll{it.computeBalance() > 0}
		}else if (searchBalance=='No') {
			directPayments = directPayments.findAll{it.computeBalance() <= 0}
		}		
		def directPaymentsTotal = directPayments.size()
		return [directPayments: generalMethodService.paginateList(directPayments, directPaymentsTotal, params), directPaymentsTotal: directPaymentsTotal]
	}
	
	private void generateInvoicesForCreditMemo(DirectPayment directPaymentInstance, creditMemos) {
		creditMemos.each {
			BigDecimal creditMemoAmount = it.computeTotalAmount()
			if(creditMemoAmount < BigDecimal.ZERO){
				generateInvoiceForCreditMemo(directPaymentInstance, it)
			} else if(creditMemoAmount >= BigDecimal.ZERO) {
			   generateItemForCreditMemo(directPaymentInstance, creditMemoAmount, it)
		   }
		}
	}

	private generateItemForCreditMemo(DirectPayment directPaymentInstance, BigDecimal creditMemoAmount, CreditMemo creditMemoInstance) {
		def directPaymentItemInstance = new DirectPaymentItem(
				directPayment:directPaymentInstance,
				paymentType: PaymentType.findByDescription("Credit Memo"),
				amount: creditMemoAmount)
		directPaymentInstance.addToItems(directPaymentItemInstance)
		creditMemoInstance.setDirectPaymentItem(directPaymentItemInstance)
		creditMemoInstance.takenByDirectPayment()
	}

	private generateInvoiceForCreditMemo(DirectPayment directPaymentInstance, CreditMemo creditMemoInstance) {
		def directPaymentInvoice = new DirectPaymentInvoice(type: CustomerPaymentType.CREDIT_MEMO, 
			amount: creditMemoInstance.computeCreditMemoTotalAmount())
		directPaymentInstance.addToInvoices(directPaymentInvoice)
		creditMemoInstance.linkDirectPaymentInvoice(directPaymentInvoice)
		directPaymentInvoice.save()
	}

	private void generateInvoicesForBouncedCheck(DirectPayment directPaymentInstance, bouncedChecks) {
		bouncedChecks.each {
			def directPaymentInvoice = new DirectPaymentInvoice(CustomerPaymentType.BOUNCED_CHECK)
			directPaymentInstance.addToInvoices(directPaymentInvoice)
			it.linkDirectPaymentInvoice(directPaymentInvoice)
			directPaymentInvoice.save()
		}
	}

	private void generateInvoicesForSalesDelivery(DirectPayment directPaymentInstance, salesDeliveries) {
		salesDeliveries.each {
			def directPaymentInvoice = new DirectPaymentInvoice(CustomerPaymentType.SALES_DELIVERY)
			directPaymentInstance.addToInvoices(directPaymentInvoice)
			it.linkDirectPaymentInvoice(directPaymentInvoice)
			directPaymentInvoice.save()
		}
	}

	private void generateInvoicesForCustomerCharge(DirectPayment directPaymentInstance, charges) {
		charges.each {
			def directPaymentInvoice = new DirectPaymentInvoice(CustomerPaymentType.CUSTOMER_CHARGE)
			directPaymentInstance.addToInvoices(directPaymentInvoice)
			it.linkDirectPaymentInvoice(directPaymentInvoice)
			directPaymentInvoice.save()
		}
	}

    private void removeSalesDeliveryInvoiceFromDirectPayment(DirectPayment directPaymentInstance, salesDeliveries) {
        salesDeliveries.each {
            def directPaymentInvoice = it.searchRelatedInvoice(directPaymentInstance)
            it.removeFromInvoices(directPaymentInvoice)
            it.notTakenByDirectPayment()
            directPaymentInstance.removeFromInvoices(directPaymentInvoice)
            directPaymentInvoice.delete()
        }
    }

    private void removeChargeInvoiceFromDirectPayment(DirectPayment directPaymentInstance, charges){
        charges.each {
            def directPaymentInvoice = it.searchRelatedInvoice(directPaymentInstance)
            it.removeFromInvoices(directPaymentInvoice)
            it.notTakenByDirectPayment()
            directPaymentInstance.removeFromInvoices(directPaymentInvoice)
            directPaymentInvoice.delete()
        }
    }

    private void removeBouncedCheckInvoiceFromDirectPayment(DirectPayment directPaymentInstance, bouncedChecks){
        bouncedChecks.each {
            def directPaymentInvoice = it.searchRelatedInvoice(directPaymentInstance)
            it.removeFromInvoices(directPaymentInvoice)
            it.notTakenByDirectPayment()
            directPaymentInstance.removeFromInvoices(directPaymentInvoice)
            directPaymentInvoice.delete()
        }
    }

    private void removeCreditMemoInvoiceFromDirectPayment(DirectPayment directPaymentInstance, creditMemos) {
        creditMemos.each {
            if(it.computeTotalAmount() < BigDecimal.ZERO) {
                def directPaymentInvoice = it.searchRelatedInvoice(directPaymentInstance)
                it.removeFromInvoices(directPaymentInvoice)
                it.notTakenByDirectPayment()
                directPaymentInstance.removeFromInvoices(directPaymentInvoice)
                directPaymentInvoice.delete()
            } else {
                directPaymentInstance.removeDirectPaymentItem(it.directPaymentItem)
                it.directPaymentItem = null
                it.approve()
                it.notTakenByDirectPayment()
            }
        }
    }

	private createDate(date){
		def df = new SimpleDateFormat("MM/dd/yyyy");
		return df.parse(date);
	}
	
	private removeItems(directPaymentInstance, toBeRemoved){
		toBeRemoved.each{
			def directPaymentItem = DirectPaymentItem.get(it)
			if(directPaymentItem.paymentType.isCheck){
				def checkPayment = directPaymentItem.checkPayment

				directPaymentItem.checkPayment.cancel()
			}
			directPaymentInstance.removeFromItems(directPaymentItem)
		}
		directPaymentInstance.save()
	}

	private addItems(directPaymentInstance, params, createdItemId){
		createdItemId.each{ctr->
			def directPaymentItemInstance = new DirectPaymentItem(directPayment:directPaymentInstance,
                paymentType: PaymentType.findById(params["paymentType${ctr}"]),
                amount: new BigDecimal(params["textAmount${ctr}"]?:0),
			    remark: params["remark${ctr}"])
			def dt = params["dateUpdate${ctr}"]
			if(dt=="")
				directPaymentItemInstance.setDate(new Date())
			else
				directPaymentItemInstance.setDate(createDate(dt))

            if(directPaymentItemInstance.paymentType.isCheck){
				def day = createDate(params["textCheckDate${ctr}"])
				def checkPayment = new CheckPayment(amount:directPaymentItemInstance.amount,
                    checkNumber: params["textCheckNum${ctr}"],
					remark: params["remark${ctr}"],
                    bank: Bank.findById(params["bankType${ctr}"]),
                    type: CheckType.findById(params["checkType${ctr}"]),
                    branch: params["textBranch${ctr}"],
                    date: day,
                    customer: directPaymentItemInstance?.directPayment?.customer,
                    warehouse: CheckWarehouse.findWhere(isDefault:true))
				checkPayment.save()
				
				def itemCheck = new DirectPaymentItemCheck(
                        checkPayment:checkPayment,
                        directPayment:directPaymentInstance,
                        amount:directPaymentItemInstance.amount,
                        paymentType:directPaymentItemInstance.paymentType,
				        remark:directPaymentItemInstance.remark)
                checkPayment.directPaymentItem = itemCheck
				itemCheck.save(flush:true)
			}
			else{
				directPaymentItemInstance.save(flush: true)
			}
		}
	}

	private updateItems(directPaymentInstance, params, itemId) {
		itemId.each {
			def directPaymentItemInstance=DirectPaymentItem.findById(it)
			if(directPaymentItemInstance.paymentType == PaymentType.findByIdentifier("CM")) {
				directPaymentItemInstance.setDate(createDate(params["dateUpdate${it}"]))
				directPaymentItemInstance.setRemark(params["remarkUpdate${it}"])
            }else{
				directPaymentItemInstance.setPaymentType(PaymentType.findById(params["paymentTypeUpdate${it}"]))
				directPaymentItemInstance.setDate(createDate(params["dateUpdate${it}"]))
				directPaymentItemInstance.setRemark(params["remarkUpdate${it}"])
                directPaymentItemInstance.setAmount(new BigDecimal(params["textAmountUpdate${it}"]?:0))

				if(directPaymentItemInstance.paymentType.isCheck){
					if(directPaymentItemInstance instanceof DirectPaymentItemCheck){
						def previousAmount = directPaymentItemInstance.checkPayment.amount
						
						directPaymentItemInstance.checkPayment.setAmount(directPaymentItemInstance.amount)
                        directPaymentItemInstance.checkPayment.setCheckNumber(params["textCheckNumUpdate${it}"])
                        directPaymentItemInstance.checkPayment.setBank(Bank.findById(params["bankTypeUpdate${it}"]))
                        directPaymentItemInstance.checkPayment.setType(CheckType.findById(params["checkTypeUpdate${it}"]))
                        directPaymentItemInstance.checkPayment.setBranch(params["textBranchUpdate${it}"])
                        directPaymentItemInstance.checkPayment.setDate(createDate(params["textCheckDateUpdate${it}"]))
                        directPaymentItemInstance.checkPayment.setCustomer(directPaymentItemInstance.checkPayment.customer)
						directPaymentItemInstance.checkPayment.setRemark(params["remarkUpdate${it}"])

						directPaymentItemInstance.checkPayment.save()
						
						def checkPayment = directPaymentItemInstance.checkPayment						

					} else {
                        def day = createDate(params["textCheckDateUpdate${it}"])
                        def checkPayment = new CheckPayment(
							amount:directPaymentItemInstance.amount,
                            checkNumber: params["textCheckNumUpdate${it}"],
                            bank: Bank.findById(params["bankTypeUpdate${it}"]),
                            type: CheckType.findById(params["checkTypeUpdate${it}"]),
                            branch: params["textBranchUpdate${it}"],
							remark: params["remarkUpdate${it}"],
                            date: day,
                            customer: directPaymentItemInstance?.directPayment?.customer,
                            warehouse: CheckWarehouse.findWhere(isDefault:true))
						checkPayment.save()
						


						def itemCheck = new DirectPaymentItemCheck(
                            checkPayment:checkPayment,
                            directPayment:directPaymentInstance,
                            amount:new BigDecimal(params["textAmountUpdate${it}"]),
                            paymentType:PaymentType.findById(params["paymentTypeUpdate${it}"]),
						    remark: params["remarkUpdate${it}"],
                            date:createDate(params["dateUpdate${it}"]))
                        checkPayment.directPaymentItem = itemCheck
						itemCheck.save()

						directPaymentInstance.removeFromItems(directPaymentItemInstance)
						directPaymentItemInstance.delete()
					}
				} else {
                    if(directPaymentItemInstance instanceof DirectPaymentItemCheck){
                        def directPaymentItem = new DirectPaymentItem(
                            paymentType: directPaymentItemInstance.paymentType,
                            date:directPaymentItemInstance.date,
                            amount:directPaymentItemInstance.amount,
                            remark: directPaymentItemInstance.remark
                        )
                        directPaymentInstance.removeFromItems(directPaymentItemInstance)
                        directPaymentItemInstance.delete()
                        directPaymentInstance.addToItems(directPaymentItem)
                    }
					directPaymentInstance.save(flush: true)
				}
			}
		}
	}
	private updateInvoices(directPaymentInstance, params) {
		directPaymentInstance.invoices.each {
			if(checkIfPaymentIsInstallment(it)){
				it.amount = new BigDecimal(params["textApplied${it.id}"])
				it.save()
			}
		}
	}

	private boolean checkIfPaymentIsInstallment(DirectPaymentInvoice invoice) {
		return invoice.type ==  CustomerPaymentType.CREDIT_MEMO ? false : true
	}
	
    private Map extractCustomerPaymentsFromDirectPayment(DirectPayment directPaymentInstance) {
        def creditMemos = []
        def bouncedChecks = []
        def deliveries = []
        def charges = []
		
        directPaymentInstance.invoices.each {
            switch (it.type) {
                case CustomerPaymentType.CREDIT_MEMO:
                    creditMemos.add(it.getRelatedCustomerPayment())
                    break
                case CustomerPaymentType.BOUNCED_CHECK:
                    bouncedChecks.add(it.getRelatedCustomerPayment())
                    break
                case CustomerPaymentType.SALES_DELIVERY:
                    deliveries.add(it.getRelatedCustomerPayment())
                    break
                case CustomerPaymentType.CUSTOMER_CHARGE:
                    charges.add(it.getRelatedCustomerPayment())
                    break
            }
        }
		
        directPaymentInstance.directPaymentItemsWithCreditMemo().each {
            creditMemos.add(it.relatedCreditMemo())
        }
		
        return [creditMemos: creditMemos, bouncedChecks: bouncedChecks, deliveries: deliveries, charges: charges]
    }
}
