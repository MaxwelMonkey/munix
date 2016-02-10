package com.munix

import org.apache.commons.lang.time.DateUtils

class CollectionScheduleService {

    static transactional = true
	
	def generalMethodService

    def getCollectionScheduleListWithCriteria(params) {
        def searchId = params.searchId
        def searchIdentifier = params.searchIdentifier
        def searchDescription = params.searchDescription
        def searchStatus = params.searchStatus
        def searchCollector = params.searchCollector
        def query = {
            and{
				if(searchId){
					like('id', Long.parseLong(searchId))
				}
                if(searchIdentifier){
                    like('identifier', "%${searchIdentifier}%")
                }
                if(searchDescription){
                    like('description', "%${searchDescription}%")
                }
                if(searchCollector){
                    collector{
                        like('identifier',"%${searchCollector}%")
                    }
                }
                if(searchStatus){
                    eq('status', com.munix.CollectionSchedule.Status.getStatusByName(searchStatus))
                }
            }
        }
		
        return CollectionSchedule.createCriteria().list(params,query)
    }
	
	def checkIfUnmarkableAsComplete(CollectionSchedule collectionScheduleInstance){
		def canBeUnMarkedAsCompleted = false
		if (collectionScheduleInstance.status == CollectionSchedule.Status.COMPLETE) {
			def endDate = DateUtils.truncate(collectionScheduleInstance.endDate, Calendar.DATE);
			def dateToday = generalMethodService.dateToday()
			if(dateToday <= generalMethodService.performDayOperationsOnDate(Operation.ADD, endDate, 1)){
				canBeUnMarkedAsCompleted = true
			}
		}
		return canBeUnMarkedAsCompleted
	}
}
