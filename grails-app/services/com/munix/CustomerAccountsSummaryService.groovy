package com.munix

class CustomerAccountsSummaryService {

    static transactional = true
//	def salesDeliveryService
//	def customerChargeService
//	def creditMemoService
//	def bouncedCheckService
//	def checkPaymentService

//    def getCustomerAccountList() {
//		def customers = []
//
//		Customer.list().each{
//			def totalUnpaidSalesDeliveries = salesDeliveryService.computeTotalUnpaidSalesDeliveries(it)
//			def totalUnpaidCustomerCharges = customerChargeService.computeTotalUnpaidCustomerCharges(it)
//			def totalUnpaidDebitMemos = creditMemoService.computeTotalUnpaidDebitMemos(it)
//			def totalUnpaidBouncedCheck = bouncedCheckService.computeTotalUnpaidBouncedCheck(it)
//			def totalUnpaidCheckPayment = checkPaymentService.computeTotalUnpaidCheckPayment(it)
//			def balance = totalUnpaidSalesDeliveries + totalUnpaidCustomerCharges + totalUnpaidDebitMemos+ totalUnpaidBouncedCheck + totalUnpaidCheckPayment
//			def isOverLimit = false
//            if(it.creditLimit<balance){
//                isOverLimit = true
//            }
//			customers.add([id:it.id,
//                         identifier: it.identifier,
//						 name: it.name,
//						 creditLimit: it.creditLimit,
//                         isOverLimit:isOverLimit,
//						 terms: (it.term != null ? it.term.description : ""),
//						 salesDeliveries: totalUnpaidSalesDeliveries,
//					 	 customerCharges: totalUnpaidCustomerCharges,
//                         debitMemos: totalUnpaidDebitMemos,
//					 	 bouncedChecks: totalUnpaidBouncedCheck,
//						 checkPayments: totalUnpaidCheckPayment,
//						 balance: balance])
//		}
//		return customers
//    }
	
	def generateFilteredCustomerAccountList(Map params) {
		def customerIdenfitier = params.customerIdentifier 
		def customerName = params.customerName
		def salesAgentParam = params.salesAgent?.id
		def busAddrCityParam = params.busAddrCity?.id
		def termsParam = params.term?.id
		def hasBalanceSD = params.hasBalanceSD
		def hasBalanceCC = params.hasBalanceCC
		def hasBalanceDM = params.hasBalanceDM
		def hasBalanceBC = params.hasBalanceBC
		def hasBalancePC = params.hasBalancePC
		def overCreditLimit = params.overCreditLimit
		def isValid = overCreditLimit != null && overCreditLimit != "" ? true : false 
		
		def query = {
			and {
				if(customerIdenfitier) {
					customer {
						like('identifier', "%${customerIdenfitier}%")
					}
				}
				
				if(customerName) {
					customer {
						like('name', "%${customerName}%")
					}
				}
				
				if(salesAgentParam) {
					if(salesAgentParam instanceof String){
						customer {
							salesAgent {
								eq('id', Long.valueOf(salesAgentParam))
							}
						}
					}else{
						def sas = []
						salesAgentParam.each{
							sas.add(Long.valueOf(it))
						}
						customer {
							salesAgent {
								"in"('id', sas)
							}
						}
					}
				}
				
				if(busAddrCityParam) {
					if(busAddrCityParam instanceof String){
						customer {
							busAddrCity {
								eq('id', Long.valueOf(busAddrCityParam))
							}
						}
					}else{
						def bcs = []
						busAddrCityParam.each{
							bcs.add(Long.valueOf(it))
						}
						customer {
							busAddrCity {
								"in"('id', bcs)
							}
						}
					}
				}
				
				if(termsParam) {
					if(termsParam instanceof String){
						customer {
							term {
								eq('id', Long.valueOf(termsParam))
							}
						}
					}else{
						def ts = []
						termsParam.each{
							ts.add(Long.valueOf(it))
						}
						customer {
							term {
								"in"('id', ts)
							}
						}
					}
				}
				
				if(hasBalanceSD) {
					gt('totalUnpaidSalesDeliveries', BigDecimal.ZERO)
				}
				
				if(hasBalanceCC) {
					gt('totalUnpaidCustomerCharges', BigDecimal.ZERO)
				}
				
				if(hasBalanceDM) {
					gt('totalUnpaidDebitMemos', BigDecimal.ZERO)
				}
				
				if(hasBalanceBC) {
					gt('totalUnpaidBouncedChecks', BigDecimal.ZERO)
				}
				
				if(hasBalancePC) {
					gt('totalUnpaidCheckPayments', BigDecimal.ZERO)
				}
			}
		}
		
		def customerAccountList = CustomerAccount.createCriteria().list(params, query).findAll { isValid ? (overCreditLimit == "False" ? it.isOverLimit() == false : it.isOverLimit() == true ) : it}
		return customerAccountList.sort{it.customer.identifier}
	}

    def addCustomerAccountCustomerCharge(CustomerCharge customerCharge){
        def customerAccount = customerCharge.customer.customerAccount
        customerAccount.addCCAmount(customerCharge.computeActualDue())
        customerAccount.save()
    }
    def removeCustomerAccountCustomerCharge(CustomerCharge customerCharge){
        def customerAccount = customerCharge.customer.customerAccount
        customerAccount.removeCCAmount(customerCharge.computeTotalAmount())
    }
	def addCustomerAccountCheckPayment(CheckPayment checkPayment){
		def customerAccount = checkPayment.customer.customerAccount
		customerAccount.addCPAmount(checkPayment.amount)
	}
	def removeCustomerAccountCheckPayment(CheckPayment checkPayment){
		def customerAccount = checkPayment.customer.customerAccount
		customerAccount.removeCPAmount(checkPayment.amount)
	}
	def updateCustomerAccountCheckPayment(CheckPayment checkPayment, BigDecimal previousAmount){
		def customerAccount = checkPayment.customer.customerAccount
		customerAccount.removeCPAmount(previousAmount)
		customerAccount.addCPAmount(checkPayment.amount)
	}
    def addCustomerAccountDebitMemo(CreditMemo creditMemo){
        def customerAccount = creditMemo.customer.customerAccount
        customerAccount.addDMAmount(creditMemo.computeTotalAmount().abs())
    }
    def removeCustomerAccountDebitMemo(CreditMemo creditMemo){
        def customerAccount = creditMemo.customer.customerAccount
        customerAccount.removeDMAmount(creditMemo.computeTotalAmount().abs())
    }
}
