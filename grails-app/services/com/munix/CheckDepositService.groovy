package com.munix

import java.util.Map;

class CheckDepositService {

    static transactional = true

    def generalMethodService
    def editCheckDepositService

    def updateChecksInCheckDeposit(List<Long> checkIdList, CheckDeposit checkDepositInstance){
        def currentCheckIdList = []
        if(checkDepositInstance.checks){
            currentCheckIdList = checkDepositInstance.checks?.id
        }

        def toBeRemoved = generalMethodService.removeSameItems(currentCheckIdList, checkIdList)
        checkDepositInstance = editCheckDepositService.removeChecksFromCheckDeposit(toBeRemoved, checkDepositInstance)

        def toBeAdded = generalMethodService.removeSameItems(checkIdList, currentCheckIdList)
        checkDepositInstance = editCheckDepositService.addChecksToCheckDeposit(toBeAdded, checkDepositInstance)

        return checkDepositInstance
    }
	
	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["searchDepositDateFrom"] = generalMethodService.createCalendarStructDate(params.searchDepositDateFrom_value)
		dateMap["searchDepositDateTo"] = generalMethodService.createCalendarStructDate(params.searchDepositDateTo_value)
		
		return dateMap
	}
	
	def generateList(Map params) {
		def searchIdentifier = params.searchIdentifier
		def searchAccount = params.searchAccount
		def searchBillsPurchase = params.searchBillsPurchase
		def searchStatus = params.searchStatus
		def searchDepositDateFrom
		if(params?.searchDepositDateFrom){
			searchDepositDateFrom = generalMethodService.createDate(params?.searchDepositDateFrom_value)
		}
		def searchDepositDateTo
		if(params?.searchDepositDateTo){
			def dateTo = generalMethodService.createDate(params?.searchDepositDateTo_value)
			searchDepositDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}

		def query = {
			and{
				if(searchIdentifier){
					like('id', Long.parseLong(searchIdentifier))
					
				}
				if(searchAccount){
					account{
						or {
							like('accountName', "%${searchAccount}%")
							like('accountNumber', "%${searchAccount}%")
							bank {
								like('identifier', "%${searchAccount}%")
							}
						}
					}
				}
				if(searchBillsPurchase){
					eq('billsPurchase', new Boolean(searchBillsPurchase))
				}

				if(searchStatus){
					eq('status', searchStatus)
				}

				if(searchDepositDateFrom&&searchDepositDateTo){
					ge('depositDate',searchDepositDateFrom)
					lt('depositDate',searchDepositDateTo)
				}else if(searchDepositDateFrom){
					ge('depositDate',searchDepositDateFrom)
				}else if(searchDepositDateTo){
					lt('depositDate',searchDepositDateTo)
				}
			}
		}
		return CheckDeposit.createCriteria().list(params, query)
	}
    Boolean cancelCheckDeposit(CheckDeposit checkDeposit){
        def cancellable = true
        if(checkDeposit.isCleared()){
            cancellable = false
        }else{
            checkDeposit.cancel()
            checkDeposit.save()
        }
        return cancellable
    }
}
