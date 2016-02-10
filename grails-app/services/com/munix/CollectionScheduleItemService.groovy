package com.munix

class CollectionScheduleItemService {
	def generalMethodService

	def completeCounterCollectionDate(CollectionScheduleItem collectionScheduleItemInstance) {
		if (collectionScheduleItemInstance.type == "Collection") {
			collectionScheduleItemInstance.assignDateForCollection()
		} 
		else if (collectionScheduleItemInstance.type == "Counter") {
			collectionScheduleItemInstance.assignDateForCounter()
		}
		else if (collectionScheduleItemInstance.type == "Both") {
			collectionScheduleItemInstance.assignDateForBoth()
		}
		return collectionScheduleItemInstance
	}
	
	def generateCounterReceiptList(CollectionSchedule collectionSchedule) {
        def listOfCounterReceipts = CounterReceipt.withCriteria() {
			and {
				eq("status", CounterReceipt.Status.APPROVED)
				or {
					isNull("collectionDate")
					isNull("counterDate")
				}
                if(collectionSchedule.items){
                    not{
                        'in'('id',collectionSchedule.items?.counterReceipt?.id)
                    }
                }
			}
			order("id","asc")
		}

		return listOfCounterReceipts
	}

	def generateTypeList(String counterReceiptId ) {
		def collectionScheduleItemTypeList = []
		def counterReceiptInstance = CounterReceipt.get(counterReceiptId)
		
        if (!counterReceiptInstance.counterDate) {
			collectionScheduleItemTypeList.add("Counter")
		}
		if (!counterReceiptInstance.collectionDate){
			collectionScheduleItemTypeList.add("Collection")
		}
		if ((!counterReceiptInstance.counterDate)&&(!counterReceiptInstance.collectionDate)) {
			collectionScheduleItemTypeList.add("Both")
		}
		return collectionScheduleItemTypeList
	}
}
