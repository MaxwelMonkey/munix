package com.munix

class CustomerChargeService {

    static transactional = true
	
	def generalMethodService
    def customerLedgerService
    def customerAccountsSummaryService
    def authenticateService

	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["searchDateFrom"] = generalMethodService.createCalendarStructDate(params.searchDateFrom_value)
		dateMap["searchDateTo"] = generalMethodService.createCalendarStructDate(params.searchDateTo_value)
		
		return dateMap
	}
	
    def generateList(Map params) {
		def searchIdentifier = params.searchIdentifier
		def searchCustomerName = params.searchCustomerName
		def searchCustomerId = params.searchCustomerId
		def searchStatus = params.searchStatus
		def searchRemarks = params.searchRemarks
		def searchDateFrom
		if(params?.searchDateFrom){
			searchDateFrom = generalMethodService.createDate(params?.searchDateFrom_value)
		}
		def searchDateTo
		if(params?.searchDateTo){
			def dateTo = generalMethodService.createDate(params?.searchDateTo_value)
			searchDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}

		def query = {
			and{
				if(searchIdentifier){
					like('id', Long.parseLong(searchIdentifier))
				}
				
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

				if(searchRemarks){
					like('remark', "%${searchRemarks}%")
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

		return CustomerCharge.createCriteria().list(params, query)
    }
	
	def getUnpaidCustomerCharges(Customer customer, Map params){
		def query = {
			and {
				eq("customer",customer)
				eq("status", "Unpaid")
			}
		}
		return CustomerCharge.createCriteria().list(params, query)
	}
	
	private def getAllUnpaidSalesDeliveryList(Customer customer){
		return CustomerCharge.findAllByCustomerAndStatus(customer,"Unpaid")
	}
	
	def computeTotalUnpaidCustomerCharges(Customer customer){
		def customerCharges = getAllUnpaidSalesDeliveryList(customer)
		BigDecimal totalUnpaidCustomerCharges = BigDecimal.ZERO 
		customerCharges.each {
			totalUnpaidCustomerCharges += it.computeProjectedDue()
		}
		return totalUnpaidCustomerCharges
	}
    def approveCustomerCharge(CustomerCharge customerCharge){
    	if(!customerCharge?.isApproved()){
	        customerCharge?.approve()
	        customerCharge.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
	        customerLedgerService.createApprovedCustomerCharge(customerCharge)
	        customerAccountsSummaryService.addCustomerAccountCustomerCharge(customerCharge)
	        if(customerCharge?.save()){
	            return true
	        }else{
	            return false
	        }
    	}else{
    		return true
    	}
    }
    def unapproveCustomerCharge(CustomerCharge customerCharge){
    	if(!customerCharge?.isUnapproved()){
	        customerCharge?.unapprove()
	        customerCharge.approvedBy = ""
	        customerLedgerService.createUnapprovedCustomerCharge(customerCharge)
	        customerAccountsSummaryService.removeCustomerAccountCustomerCharge(customerCharge)
	        if(customerCharge?.save()){
	            return true
	        }else{
	            return false
	        }
        }else{
            return true
        }
    }
}
