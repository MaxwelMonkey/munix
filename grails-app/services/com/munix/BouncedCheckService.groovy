package com.munix

class BouncedCheckService {

    static transactional = true
    def generalMethodService
    def editBouncedCheckService

    def updateChecksFromBouncedCheck(BouncedCheck bouncedCheckInstance, List<Long> checkList){
        def currentList = []
        if(bouncedCheckInstance.checks){
            currentList = bouncedCheckInstance.checks.id
        }
        def toBeRemoved = generalMethodService.removeSameItems(currentList, checkList)
        editBouncedCheckService.removeChecksFromBouncedCheck(bouncedCheckInstance,toBeRemoved)

        def toBeAdded = generalMethodService.removeSameItems(checkList, currentList)
        editBouncedCheckService.addChecksToBouncedCheck(bouncedCheckInstance,toBeAdded)

        return bouncedCheckInstance
    }
	
	def getUnpaidBouncedCheckList(Map params, Customer customer) {
		def query = {
			and {
				eq("customer",customer)
				eq("status", "Approved")
			}
		}
		return BouncedCheck.createCriteria().list(params, query)
	}
	
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
		def searchDateFrom
		if(params?.searchDateFrom){
			searchDateFrom = generalMethodService.createDate(params?.searchDateFrom_value)
		}
		def searchDateTo
		if(params?.searchDateTo){
			def dateTo = generalMethodService.createDate(params?.searchDateTo_value)
			searchDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}
		def searchBouncedCheckStatus = params.searchBouncedCheckStatus
		def searchForRedeposit = params.searchForRedeposit

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
				
				if(searchDateFrom&&searchDateTo){
					ge('date',searchDateFrom)
					lt('date',searchDateTo)
				}else if(searchDateFrom){
					ge('date',searchDateFrom)
				}else if(searchDateTo){
					lt('date',searchDateTo)
				}
				
				if(searchBouncedCheckStatus){
					bouncedCheckStatus {
						or {
							like('identifier', "%${searchBouncedCheckStatus}%")
							like('description', "%${searchBouncedCheckStatus}%")
						}
					}
				}

				if(searchForRedeposit){
					eq('forRedeposit', new Boolean(searchForRedeposit))
				}
			}
		}

		return BouncedCheck.createCriteria().list(params, query)
	}
	
	private def getAllUnpaidBouncedCheckList(Customer customer){
		return BouncedCheck.findAllByCustomerAndStatus(customer, "Approved")
	}

	def computeTotalUnpaidBouncedCheck(Customer customer){
		def bouncedChecks = getAllUnpaidBouncedCheckList(customer)
		BigDecimal totalUnpaidBouncedChecks = BigDecimal.ZERO
		bouncedChecks.each{
			totalUnpaidBouncedChecks += it.computeProjectedDue()
		}
		return totalUnpaidBouncedChecks
	}
}
