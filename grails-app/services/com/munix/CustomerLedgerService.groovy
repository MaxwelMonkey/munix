package com.munix

class CustomerLedgerService {

    static transactional = true
    def generalMethodService

	def getCustomerLedgerEntries(CustomerLedger customerLedgerInstance, params) {
		if (!params.offset) params.offset = 0
		if(!params.max)  params.max = 100
		def searchPostDateBefore
		if(params?.postDateBeforeText){
			searchPostDateBefore = generalMethodService.createDate(params?.postDateBeforeText)
		}
		def searchPostDateAfter
		if(params?.postDateAfterText){
			def dateAfter = generalMethodService.createDate(params?.postDateAfterText)
			searchPostDateAfter = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateAfter, 1)
		}
		def query = {
			and{
				//Search
				if(searchPostDateBefore&&searchPostDateAfter){
					and{
						ge('datePosted',searchPostDateBefore)
						le('datePosted',searchPostDateAfter)
					}
				}else if(searchPostDateBefore){
					ge('datePosted',searchPostDateBefore)
				}else if(searchPostDateAfter){
					le('datePosted',searchPostDateAfter)
				}
				eq('customerLedger',customerLedgerInstance)
				//Sort
				order('datePosted','asc')
			}
		}
		def entries= CustomerLedgerEntry.createCriteria().list(params, query)
		return [customerLedgerInstance: customerLedgerInstance,entries:entries,entriesTotal:entries.getTotalCount(), params:params]
	}
	
    def createApprovedSalesDelivery(SalesDelivery salesDelivery) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedSalesDelivery(salesDelivery)
		return updateLedger(customerLedgerEntry, salesDelivery)
    }
	
	def createUnapprovedSalesDelivery(SalesDelivery salesDelivery) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedSalesDelivery(salesDelivery)
		return updateLedger(customerLedgerEntry, salesDelivery)
	}
	
	def createApprovedBouncedCheck(BouncedCheck bouncedCheck) {
		def result = true
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedBouncedCheck(bouncedCheck)
		result = updateLedger(customerLedgerEntry, bouncedCheck)
		def customerLedger = bouncedCheck.customer.customerLedger
		if (result && customerLedger) {
			bouncedCheck.checks.each {
				def entry = CustomerLedgerEntryFactory.createApprovedBouncedCheckItem(bouncedCheck, it)
				customerLedgerEntry.addPaymentBreakdown(entry)
				customerLedger.addToEntries(entry)
			}
			customerLedgerEntry.save(flush: true)
			customerLedger.save(flush: true)
		}
		return result
	}
    def createUnapprovedBouncedCheck(BouncedCheck bouncedCheck) {
		def result = true
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedBouncedCheck(bouncedCheck)
		result = updateLedger(customerLedgerEntry, bouncedCheck)
		def customerLedger = bouncedCheck.customer.customerLedger
		if (result && customerLedger) {
			bouncedCheck.checks.each {
				def entry = CustomerLedgerEntryFactory.createUnapprovedBouncedCheckItem(bouncedCheck, it)
				customerLedgerEntry.addPaymentBreakdown(entry)
				customerLedger.addToEntries(entry)
			}
			customerLedgerEntry.save(flush: true)
			customerLedger.save(flush: true)
		}
		return result
	}
		
	def createApprovedCheckDeposit(CheckDeposit checkDeposit) {
		def result = true
		checkDeposit.checks.each {
			def entry = CustomerLedgerEntryFactory.createApprovedCheckDepositItem(checkDeposit, it)
			if (!createCheckPayment(it, entry)) {
				result = false
			}
		}
		return result
	}
	
	def createApprovedCustomerCharge(CustomerCharge customerCharge) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedCustomerCharge(customerCharge)
		return updateLedger(customerLedgerEntry, customerCharge)
	}
	
	def createUnapprovedCustomerCharge(CustomerCharge customerCharge) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedCustomerCharge(customerCharge)
		return updateLedger(customerLedgerEntry, customerCharge)
	}
	
	def createApprovedDebitMemo(CreditMemo debitMemo) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedDebitMemo(debitMemo)
		return updateLedger(customerLedgerEntry, debitMemo)
	}
	
	def createUnapprovedDebitMemo(CreditMemo debitMemo) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedDebitMemo(debitMemo)
		return updateLedger(customerLedgerEntry, debitMemo)
	}
	
	def createApprovedCreditMemo(CreditMemo creditMemo) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedCreditMemo(creditMemo)
		return updateLedger(customerLedgerEntry, creditMemo)
	}
	
	def createUnapprovedCreditMemo(CreditMemo creditMemo) {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedCreditMemo(creditMemo)
		return updateLedger(customerLedgerEntry, creditMemo)
	}
	
    def approveDirectPayment(DirectPayment directPayment){
        if(directPayment){
            def customerLedger = directPayment.customer.customerLedger
            def ledgerEntry = createApprovedDirectPayment(directPayment, customerLedger)
            if(ledgerEntry){
                directPayment.items.each{
                    if(it.paymentType.isCheck){
                        createApprovedDirectPaymentItemCheck(it, customerLedger,ledgerEntry)
                    } else{
                        createApprovedDirectPaymentItem(it, customerLedger,ledgerEntry)
                    }
                }
				if (directPayment.overpayment) {
					createApprovedOverpayment(directPayment.overpayment, customerLedger, ledgerEntry)
				}
            }
        }
    }
	
    def unapproveDirectPayment(DirectPayment directPayment){
        if(directPayment){
            def customerLedger = directPayment.customer.customerLedger
            def ledgerEntry = createUnapprovedDirectPayment(directPayment, customerLedger)
            if(ledgerEntry){
                directPayment.items.each{
                    if(it.paymentType.isCheck){
                        createUnapprovedDirectPaymentItemCheck(it,customerLedger,ledgerEntry)
                    }else{
                        createUnapprovedDirectPaymentItem(it,customerLedger,ledgerEntry)
                    }
                }
				if (directPayment.overpayment) {
					createUnapprovedOverpayment(directPayment.overpayment, customerLedger, ledgerEntry)
				}
            }
        }
    }
	
	private def createApprovedOverpayment(Overpayment overpayment, CustomerLedger customerLedger, CustomerLedgerEntry ledgerEntry) {
		def result = false
		if(customerLedger) {
			def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedOverpayment(overpayment)
			result = updateCustomerLedgerDebit(customerLedger, customerLedgerEntry, ledgerEntry)
		}
		return result
	}
	
	private def createUnapprovedOverpayment(Overpayment overpayment, CustomerLedger customerLedger, CustomerLedgerEntry ledgerEntry) {
		def result = false
		if(customerLedger) {
			def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedOverpayment(overpayment)
			result = updateCustomerLedgerCredit(customerLedger, customerLedgerEntry, ledgerEntry)
		}
		return result
	}
	
    private def createApprovedDirectPayment(DirectPayment directPayment,CustomerLedger customerLedger){
        def customerLedgerEntry
        if(customerLedger){
            customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedDirectPayment(directPayment)
            customerLedgerEntry.runningBalance = customerLedger.balance
            customerLedger.addToEntries(customerLedgerEntry)
            customerLedger?.save(flush:true)
        }
		return customerLedgerEntry
    }
	
    private def createUnapprovedDirectPayment(DirectPayment directPayment,CustomerLedger customerLedger){
        def customerLedgerEntry
        if(customerLedger){
            customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedDirectPayment(directPayment)
            customerLedgerEntry.runningBalance = customerLedger.balance
            customerLedger.addToEntries(customerLedgerEntry)
            customerLedger?.save(flush:true)
        }
        return customerLedgerEntry
    }
	
    private def createApprovedDirectPaymentItem(DirectPaymentItem directPaymentItem, CustomerLedger customerLedger, CustomerLedgerEntry ledgerEntry){
        def result = false
        if(customerLedger){
            def customerLedgerEntry = CustomerLedgerEntryFactory.createDirectPaymentItem(directPaymentItem)
            result = updateCustomerLedgerCredit(customerLedger,customerLedgerEntry,ledgerEntry)
        }
		return result
    }
	
    private def createUnapprovedDirectPaymentItem(DirectPaymentItem directPaymentItem, CustomerLedger customerLedger,CustomerLedgerEntry ledgerEntry){
        def result = false
        if(customerLedger){
            def customerLedgerEntry = CustomerLedgerEntryFactory.createDirectPaymentItem(directPaymentItem)
            result = updateCustomerLedgerDebit(customerLedger,customerLedgerEntry,ledgerEntry)
        }
		return result
    }
	
    private def createApprovedDirectPaymentItemCheck(DirectPaymentItem directPaymentItem, CustomerLedger customerLedger, CustomerLedgerEntry ledgerEntry){
        def result = false
        if(customerLedger){
            def customerLedgerEntry = CustomerLedgerEntryFactory.createDirectPaymentItem(directPaymentItem)
            customerLedgerEntry.runningBalance = customerLedger.balance
            ledgerEntry.addPaymentBreakdown(customerLedgerEntry)
            customerLedger.addToEntries(customerLedgerEntry)
            if (customerLedger?.save(flush:true) && !customerLedger.hasErrors()) {
                result = true
            }
        }
		return result
    }
	
    private def createUnapprovedDirectPaymentItemCheck(DirectPaymentItem directPaymentItem, CustomerLedger customerLedger, CustomerLedgerEntry ledgerEntry){
        def result = false
        if(customerLedger){
            def customerLedgerEntry = CustomerLedgerEntryFactory.createDirectPaymentItem(directPaymentItem)
            customerLedgerEntry.runningBalance = customerLedger.balance
            ledgerEntry.addPaymentBreakdown(customerLedgerEntry)
            customerLedger.addToEntries(customerLedgerEntry)
            if (customerLedger?.save(flush:true) && !customerLedger.hasErrors()) {
                result = true
            }
        }
		return result
    }
	
    private updateCustomerLedgerCredit(CustomerLedger customerLedger, CustomerLedgerEntry customerLedgerEntry, CustomerLedgerEntry ledgerEntry){
        def result = false
        customerLedgerEntry.creditAmount= customerLedgerEntry.amount
        customerLedger.updateBalanceAddCredit(customerLedgerEntry.creditAmount)
        customerLedgerEntry.runningBalance = customerLedger.balance
        ledgerEntry.runningBalance = customerLedger.balance
        ledgerEntry.addPaymentBreakdown(customerLedgerEntry)
        customerLedger.addToEntries(customerLedgerEntry)
        if (customerLedger?.save(flush:true) && !customerLedger.hasErrors()) {
            result = true
        }
        return result
    }
	
    private updateCustomerLedgerDebit(CustomerLedger customerLedger, CustomerLedgerEntry childLedgerEntry, CustomerLedgerEntry parentLedgerEntry){
        def result = false
        childLedgerEntry.debitAmount = childLedgerEntry.amount
        customerLedger.updateBalanceAddDebit(childLedgerEntry.debitAmount)
        childLedgerEntry.runningBalance = customerLedger.balance
        parentLedgerEntry.runningBalance = customerLedger.balance
        parentLedgerEntry.addPaymentBreakdown(childLedgerEntry)
        customerLedger.addToEntries(childLedgerEntry)
        if (customerLedger?.save(flush:true) && !customerLedger.hasErrors()) {
            result = true
        }
        return result
    }
	
	private boolean updateLedger(CustomerLedgerEntry entry, CustomerPayment customerPayment) {
		def result = false
		def customerLedger = customerPayment.customer.customerLedger
		if (customerLedger) {
			if (entry.debitAmount) {
				customerLedger.updateBalanceAddDebit(entry.debitAmount)
			} else if (entry.creditAmount) {
				customerLedger.updateBalanceAddCredit(entry.creditAmount)
			}
			entry.runningBalance = customerLedger.balance
			customerLedger.addToEntries(entry)
			if (customerLedger?.save() && !customerLedger.hasErrors()) {
				result = true
			}
		}
		return result
	}
	
	private boolean createCheckPayment(CheckPayment checkPayment, CustomerLedgerEntry entry) {
		def result = false
		def customerLedger = checkPayment.customer.customerLedger
		if (customerLedger) {
			if (entry.creditAmount) {
				customerLedger.updateBalanceAddCredit(entry.creditAmount)
			} else if (entry.debitAmount) {
				customerLedger.updateBalanceAddDebit(entry.debitAmount)
			}
			entry.runningBalance = customerLedger.balance
			customerLedger.addToEntries(entry)
			if (customerLedger?.save() && !customerLedger.hasErrors()) {
				result = true
			}
		}
		return result
	}
}
