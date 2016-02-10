package com.munix

import java.util.Map;

class WaybillService {

    static transactional = true
	def generalMethodService
    private static final String UNAPPROVED = "Unapproved"
    private static final String CANCELLED = "Cancelled"

    def getDeliveryListForWaybill(Customer customerInstance) {

        def deliveryList = SalesDelivery.withCriteria {
            order("salesDeliveryId","asc")
            and{
                eq('customer',customerInstance)
                not{
                    'in'('status',[UNAPPROVED,CANCELLED])
                }
                eq('deliveryType',"Deliver")
                isNull('waybill')
                isNull('directDelivery')
            }
        }
        return deliveryList
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
		def searchForwarder = params.searchForwarder
		def searchStatus = params.searchStatus
		def searchTripTicketId = params.searchTripTicketId
		def searchWithTripTicket = params.searchWithTripTicket
		def searchDateFrom
		if(params.searchDateFrom) {
			 searchDateFrom = generalMethodService.createDate(params.searchDateFrom_value)
		}
		def searchDateTo
		if(params.searchDateTo) {
			def dateTo = generalMethodService.createDate(params.searchDateTo_value)
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
				if(searchForwarder){
					forwarder{
						like('identifier', "%${searchForwarder}%")
					}
				}
				
				if(searchStatus){
					eq('status', searchStatus)
				}else{
					not{
						eq('status', "Complete")
						eq('status', "Cancelled")
					}
				}
				
				if(searchDateFrom&&searchDateTo){
					ge('date',searchDateFrom)
					lt('date',searchDateTo)
				}else if(searchDateFrom){
					ge('date',searchDateFrom)
				}else if(searchDateTo){
					lt('date',searchDateTo)
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

		return Waybill.createCriteria().list(params,query)
	}
}
