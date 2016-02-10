package com.munix

import java.util.Map;

class JobOrderService {
    static transactional = true

	def generalMethodService
	
	def generateDateStructList(Map params) {
		def dateMap = [:]
		dateMap["searchStartDateFrom"] = generalMethodService.createCalendarStructDate(params.searchStartDateFrom_value)
		dateMap["searchStartDateTo"] = generalMethodService.createCalendarStructDate(params.searchStartDateTo_value)
		dateMap["searchTargetDateFrom"] = generalMethodService.createCalendarStructDate(params.searchTargetDateFrom_value)
		dateMap["searchTargetDateTo"] = generalMethodService.createCalendarStructDate(params.searchTargetDateTo_value)
		dateMap["searchEndDateFrom"] = generalMethodService.createCalendarStructDate(params.searchEndDateFrom_value)
		dateMap["searchEndDateTo"] = generalMethodService.createCalendarStructDate(params.searchEndDateTo_value)
		
		return dateMap
	}
	
    def generateList(params){
		def searchId = params.searchId
		def searchIdentifier = params.searchIdentifier
		def searchDescription = params.searchDescription
		def searchAssignedTo = params.searchAssignedTo
		def searchRemarks = params.searchRemarks
		def searchStatus = params.searchStatus
		
		def searchStartDateFrom
		if(params?.searchStartDateFrom){
			searchStartDateFrom = generalMethodService.createDate(params?.searchStartDateFrom_value)
		}
		def searchStartDateTo
		if(params?.searchStartDateTo){
			def dateTo = generalMethodService.createDate(params?.searchStartDateTo_value)
			searchStartDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}
		
		def searchTargetDateFrom
		if(params?.searchTargetDateFrom){
			searchTargetDateFrom = generalMethodService.createDate(params?.searchTargetDateFrom_value)
		}
		def searchTargetDateTo
		if(params?.searchTargetDateTo){
			def dateTo = generalMethodService.createDate(params?.searchTargetDateTo_value)
			searchTargetDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}
		
		def searchEndDateFrom
		if(params?.searchEndDateFrom){
			searchEndDateFrom = generalMethodService.createDate(params?.searchEndDateFrom_value)
		}
		def searchEndDateTo
		if(params?.searchEndDateTo){
			def dateTo = generalMethodService.createDate(params?.searchEndDateTo_value)
			searchEndDateTo = generalMethodService.performDayOperationsOnDate(Operation.ADD, dateTo, 1)
		}

		def query = {
			and {
				
				if(searchIdentifier) {
					product {
						like('identifier', "%${searchIdentifier}%")
					}
				}

				if(searchAssignedTo) {
					assignedTo{
						eq('identifier', searchAssignedTo)
					}
				}

				if(searchRemarks) {
					like('remark', "%${searchRemarks}%")
				}
				
				if(searchStatus){
					eq('status', com.munix.JobOrder.Status.getStatusByName(searchStatus))
				}else{
					ne('status', com.munix.JobOrder.Status.getStatusByName("Completed"))
					ne('status', com.munix.JobOrder.Status.getStatusByName("Cancelled"))
				}
				
				if(searchStartDateFrom && searchStartDateTo) {
					ge('startDate', searchStartDateFrom)
					le('startDate', searchStartDateTo)
				} else if(searchStartDateFrom) {
					ge('startDate', searchStartDateFrom)
				} else if(searchStartDateTo) {
					le('startDate', searchStartDateTo)
				}
				
				if(searchTargetDateFrom && searchTargetDateTo) {
					ge('targetDate', searchTargetDateFrom)
					le('targetDate', searchTargetDateTo)
				} else if(searchTargetDateFrom) {
					ge('targetDate', searchTargetDateFrom)
				} else if(searchTargetDateTo) {
					le('targetDate', searchTargetDateTo)
				}

				if(searchEndDateFrom && searchEndDateTo) {
					ge('endDate', searchEndDateFrom)
					le('endDate', searchEndDateTo)
				} else if(searchEndDateFrom) {
					ge('endDate', searchEndDateFrom)
				} else if(searchEndDateTo) {
					le('endDate', searchEndDateTo)
				}

			}
		}
		
		def jobOrders = JobOrder.createCriteria().list(query)
		if(searchId){
			jobOrders = jobOrders.findAll{it.toString() =~ searchId}
		}
		if(searchDescription){
			jobOrders = jobOrders.findAll{it.product.description.toLowerCase() =~ searchDescription.toLowerCase()}
		}
		def jobOrdersTotal = jobOrders.size()
		return [jobOrders: generalMethodService.paginateList(jobOrders, jobOrdersTotal, params), jobOrdersTotal: jobOrdersTotal]
    }
    def getNotCompletedRequisitionItems(JobOrder jobOrder){
        def notCompletedRequisitionItems = []
        def balance = 0
        jobOrder.requisition.items.each{
            balance = jobOrder.computeRequisitionItemRemainingBalance(it)
                notCompletedRequisitionItems.add([item:it,remainingBalance:balance])
        }
        return notCompletedRequisitionItems
    }
	

	def checkIfUnapprovable(JobOrder jobOrderInstance) {
		def isUnapprovable = true
		jobOrderInstance.releases.each{
			if(!it.isCancelled()) {
				isUnapprovable = false
			}
		}

		return isUnapprovable
	}

    def generateJobOrderMaterials(JobOrder jobOrderInstance) {
        def materials = []
        jobOrderInstance.requisition.items.eachWithIndex { item, idx->
            def materialsMap = [:]
            materialsMap["productId"] = item.component.id
            materialsMap["identifier"] = item.component?.identifier
            materialsMap["description"] = item.component?.description
			materialsMap["unitsRequired"] = item.unitsRequired
			materialsMap["totalUnitsRequired"] = item.computeQuantity()
			materialsMap["totalUnits"] = jobOrderInstance.qty
			materialsMap["isDeleted"] = item.isDeleted
			materialsMap["index"] = idx
            materials.add(materialsMap)
        }
        return materials.sort{it.description}
    }

    def isCompletable(JobOrder jobOrderInstance){
        def canBeCompleted = false
        if(jobOrderInstance.computeRemainingBalance()==0&&jobOrderInstance.jobOuts.findAll{!(it?.isApproved()||it.isCancelled())}.isEmpty()){
            canBeCompleted = true
        }
        return canBeCompleted
    }
    def unmarkableAsComplete(JobOrder jobOrderInstance){
        def canBeUnMarkedAsCompleted = true
		if(jobOrderInstance.markAsCompleteBy) {
			def dateMarkedAsComplete = generalMethodService.getDateFromApprovedBy(jobOrderInstance.markAsCompleteBy)
			def dateToday = generalMethodService.dateToday()
			if(dateToday>generalMethodService.performDayOperationsOnDate(Operation.ADD, dateMarkedAsComplete, 1)){
				canBeUnMarkedAsCompleted = false
			}
		} else {
			canBeUnMarkedAsCompleted = false
		}
        return canBeUnMarkedAsCompleted
    }
}
