package com.munix

import java.util.Map;

class CheckPaymentService {

    static transactional = true
    def generalMethodService

	def generatePendingAndForRedepositBouncedCheck() {
        def query = {
			and{
                directPaymentItem{
                    isNotNull("directPayment")
                }
                or {
                    and {
                    	eq("status", CheckPayment.Status.PENDING)
                    	directPaymentItem {
                            directPayment {
                                eq("status", "Approved")
                            }
                        }
                        or {
                            isEmpty("checkDeposits")
                            checkDeposits {
                                eq("status", "Cancelled")
                            }
                        }
                    }
					and {
						eq("status", CheckPayment.Status.FOR_REDEPOSIT)
						bouncedChecks {
							or {
								eq("status", "Approved")
								eq("status", "Paid")
							}
						}
                        sizeLt("checkDeposits",2)
					}
                }
            }
		}
        def checkPayments = CheckPayment.createCriteria().listDistinct (query)
		def toBeRemoved = []
		checkPayments.each {
			if(it.status == CheckPayment.Status.FOR_REDEPOSIT) {
				def isNotTaken = true
				it.checkDeposits.each {
					if(isNotTaken && it.status == "Unapproved") {
						isNotTaken = false
						toBeRemoved.add(it)
					}
				}
			}
		}
		
		checkPayments.removeAll(toBeRemoved)

		return checkPayments
	}

	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["dateBeforeText"] = generalMethodService.createCalendarStructDate(params.dateBeforeText_value)
		dateMap["dateAfterText"] = generalMethodService.createCalendarStructDate(params.dateAfterText_value)
		
		return dateMap
	}
	
    def getCheckPaymentListWithCriteria(Map params) {
        def searchCustomerName = params.searchCustomerName
		def searchCustomerId = params.searchCustomerId
        def searchCheckNumber = params.searchCheckNumber
        def searchBank = params.searchBank
        def searchBranch = params.searchBranch
        def searchWarehouse = params.searchWarehouse
        def searchStatus = params.searchStatus
        def searchDateBefore
		if(params?.dateBeforeText){
			searchDateBefore = generalMethodService.createDate(params?.dateBeforeText_value)
		}
		def searchDateAfter
		if(params?.dateAfterText){
			def dateTo = generalMethodService.createDate(params?.dateAfterText_value)
			searchDateAfter = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}

        def queryParams = params.clone()

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
                if(searchStatus==null){
              		eq('status', com.munix.CheckPayment.Status.getStatusByName("Pending"))
                }else if(searchStatus!=""){
                    eq('status', com.munix.CheckPayment.Status.getStatusByName(searchStatus))
                }
                if(searchCheckNumber){
                    like('checkNumber',  "%${searchCheckNumber}%")
                }
                if(searchBank){
                    bank{
                        eq('id', Long.parseLong(searchBank))
                    }
                }
                if(searchBranch){
                   like('branch', "%${searchBranch}%")
                }
                if(searchWarehouse){
                    warehouse{
                        eq('id', Long.parseLong(searchWarehouse))
                    }
                }
                if(searchDateBefore&&searchDateAfter){
                    ge('date',searchDateBefore)
                    lt('date',searchDateAfter)
                }else if(searchDateBefore){
                    ge('date',searchDateBefore)
                }else if(searchDateAfter){
                    lt('date',searchDateAfter)
                }

                directPaymentItem {
                    directPayment {
                        eq("status", "Approved")
                        if(queryParams.sort=="directPaymentItem.directPayment"){
                            queryParams.sort=null
                            order("id", params.order)
                        }
                    }
                }
            }
        }
        return CheckPayment.createCriteria().list(queryParams,query)
    }

	private def getAllUnpaidCheckPayment(Customer customer){
		return CheckPayment.findAllByCustomerAndStatusInList(customer,[CheckPayment.Status.PENDING, CheckPayment.Status.FOR_REDEPOSIT])
	}

	def computeTotalUnpaidCheckPayment(Customer customer){
		def checkPayments = getAllUnpaidCheckPayment(customer)
		BigDecimal totalUnpaidCheckPayment = BigDecimal.ZERO
		checkPayments.each{ cp ->
			if(cp.directPaymentItem) {
				totalUnpaidCheckPayment += cp.amount
			}
		}
		return totalUnpaidCheckPayment
	}

	def getUnpaidCheckPaymentList(Map params, Customer customer) {
		def query = {
			and {
				eq("customer",customer)
				
				directPaymentItem {
					directPayment{
                        eq('status','Approved')
                    }
				}
				
				or {
					eq("status", CheckPayment.Status.PENDING)
					eq("status", CheckPayment.Status.FOR_REDEPOSIT)
				}
			}
		}
		return CheckPayment.createCriteria().list(params, query)
	}
}
