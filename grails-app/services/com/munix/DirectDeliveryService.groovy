package com.munix

import java.util.Map;

class DirectDeliveryService {
    def generalMethodService
    static transactional = true

	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["dateBeforeText"] = generalMethodService.createCalendarStructDate(params.dateBeforeText_value)
		dateMap["dateAfterText"] = generalMethodService.createCalendarStructDate(params.dateAfterText_value)
		
		return dateMap
	}

    def getDirectDeliveryListWithCriteria(params) {
        def searchIdentifier = params.searchIdentifier
        def searchCustomerName = params.searchCustomerName
		def searchCustomerId = params.searchCustomerId
        def searchStatus = params.searchStatus
        def searchWithTripTicket = params.searchWithTripTicket
        def searchTripTicketId = params.searchTripTicketId
        def searchDateBefore
		if(params?.dateBeforeText){
			searchDateBefore = generalMethodService.createDate(params?.dateBeforeText_value)
		}
		def searchDateAfter
		if(params?.dateAfterText){
			def dateAfter = generalMethodService.createDate(params?.dateAfterText_value)
			searchDateAfter = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateAfter, 1)
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
                    eq('status', com.munix.DirectDelivery.Status.getStatusByName(searchStatus))
                }else{
                	not{
                		eq('status', com.munix.DirectDelivery.Status.getStatusByName("Complete"))
                		eq('status', com.munix.DirectDelivery.Status.getStatusByName("Cancelled"))
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
				if(searchWithTripTicket=="Yes"){
					isNotNull('tripTicket')
				}
				if(searchWithTripTicket=="No"){
					isNull('tripTicket')
				}
				if(searchTripTicketId){
					tripTicket{
						tripTicket{
							eq('id', Long.parseLong(searchTripTicketId))
						}
					}
				}
            }
        }
		return DirectDelivery.createCriteria().list(params,query)
    }
}
