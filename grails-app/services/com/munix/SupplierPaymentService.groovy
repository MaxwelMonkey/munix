package com.munix

class SupplierPaymentService {

    static transactional = true
    def generalMethodService
    def editSupplierPaymentService

    def availableSuppliers() {
		def supplierList = Supplier.withCriteria {
			eq('localeType', 'Local')
		}

		def supplierFilteredList = []
		supplierList.each { 
			def purchaseInvoices = PurchaseInvoice.findAllBySupplierAndStatus(it, PurchaseInvoice.Status.APPROVED)

			if(purchaseInvoices) {
				supplierFilteredList.add(it)
			}
		}
		
        def sortIdentifier = supplierFilteredList.sort{it.identifier}.clone()
        return [sortedByIdentifier:sortIdentifier, sortedByName:supplierFilteredList.sort{it.name}]
    }
    def availablePurchaseInvoices(Supplier supplierInstance){
        return PurchaseInvoice.withCriteria {
            order('reference', 'asc')
            and{
                eq('supplier', supplierInstance)
                eq('type', 'Local')
                eq('status', PurchaseInvoice.Status.APPROVED)
				or {
				  isNull('payment')
				  payment {
					  eq ('status', SupplierPayment.Status.CANCELLED)
				  }
                }
            }
        }
    }
    def updatePurchaseInvoiceFromSupplierPayment(SupplierPayment supplierPaymentInstance, List<Long> purchaseInvoiceList){
        def currentList = []
        if(supplierPaymentInstance.purchaseInvoices){
            currentList = supplierPaymentInstance.purchaseInvoices.id
        }
        def toBeRemoved = generalMethodService.removeSameItems(currentList, purchaseInvoiceList)
        editSupplierPaymentService.removePurchaseInvoiceToSupplierPayment(supplierPaymentInstance,toBeRemoved)

        def toBeAdded = generalMethodService.removeSameItems(purchaseInvoiceList, currentList)
        editSupplierPaymentService.addPurchaseInvoiceToSupplierPayment(supplierPaymentInstance,toBeAdded)

        return supplierPaymentInstance
    }
    def checkIfAPurchaseInvoiceHasAlreadyBeenTaken(Supplier supplierInstance, List<Long> purchaseInvoiceList){
        def listOfAvailablePurchaseInvoice = availablePurchaseInvoices(supplierInstance)
        def purchaseInvoiceAlreadyTaken = false
        purchaseInvoiceList.each{
            def purchaseInvoice = PurchaseInvoice.get(it)
            if(!listOfAvailablePurchaseInvoice.contains(purchaseInvoice)){
                purchaseInvoiceAlreadyTaken = true
            }
        }
        return purchaseInvoiceAlreadyTaken
    }
}
