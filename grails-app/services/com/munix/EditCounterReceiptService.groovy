package com.munix

class EditCounterReceiptService {

    static transactional = false

	def addItemsToCounterReceipt(CounterReceipt counterReceiptInstance, items) {
		items.each {
			counterReceiptInstance.addToItems(it)
			it.invoice.takenByCounterReceipt()
		}
		return counterReceiptInstance
	}
	
    def removeItemsFromCounterReceipt(CounterReceipt counterReceiptInstance, items){
		items.each {
            counterReceiptInstance.removeFromItems(it)
			it.delete()
			it.invoice.notTakenByCounterReceipt()
        }
        return counterReceiptInstance
    }
}
