package com.munix

import java.util.Map;

class TripTicketService {

	def generalMethodService
    static transactional = true
	
	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["dateFrom"] = generalMethodService.createCalendarStructDate(params.dateFrom_value)
		dateMap["dateTo"] = generalMethodService.createCalendarStructDate(params.dateTo_value)
		
		return dateMap
	}
	
	def generateList(Map params) {
		def searchId = params.id
		def searchDriver = params.driver
		def searchHelper = params.helper
		def searchStatus = params.status
		def searchTruck = params.truck
		def searchDateFrom
		def searchDateTo
		
		if(params?.dateFrom) {
			 searchDateFrom = generalMethodService.createDate(params.dateFrom_value)
		}
		if(params?.dateTo) {
			def dateTo = generalMethodService.createDate(params.dateTo_value)
			searchDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}
		
		def query = {
			and{
				if(searchId){
					like('id', Long.parseLong(searchId))
				}
				if(searchDateFrom&&searchDateTo){
					ge('date',searchDateFrom)
					lt('date',searchDateTo)
				}else if(searchDateFrom){
					ge('date',searchDateFrom)
				}else if(searchDateTo){
					lt('date',searchDateTo)
				}

				if(searchDriver) {
					driver{
						ilike("identifier","%${searchDriver}%")
						if(params.order == 'asc'){
							order("identifier","des")
						}else{
							order("identifier", "asc")
						}
					}
				}
				
				if(searchHelper) {
					helpers{
						ilike("identifier","%${searchHelper}%")
						if(params.order == 'asc'){
							order("identifier","des")
						}else{
							order("identifier", "asc")
						}
					}
				}
	
				if(searchStatus) {
					ilike('status', "%${searchStatus}%")
				}else{
					not{
						eq("status","Complete")
						eq("status","Cancelled")
					}
				}
				
				if(searchTruck) {
					truck{
						ilike("identifier","%${searchTruck}%")
						if(params.order == 'asc'){
							order("identifier","des")
						}else{
							order("identifier", "asc")
						}
					}
				}
			}
		}

		return TripTicket.createCriteria().list(params, query)
	}
}
