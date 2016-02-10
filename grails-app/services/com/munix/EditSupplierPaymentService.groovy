package com.munix

class EditSupplierPaymentService {

    static transactional = false

    def addPurchaseInvoiceToSupplierPayment(SupplierPayment supplierPaymentInstance, List<Long> purchaseInvoiceList) {
        purchaseInvoiceList.each{
            supplierPaymentInstance.addToPurchaseInvoices(PurchaseInvoice.get(it))
        }
        return supplierPaymentInstance
    }
    def removePurchaseInvoiceToSupplierPayment(SupplierPayment supplierPaymentInstance, List<Long> purchaseInvoiceList) {
        purchaseInvoiceList.each{
            supplierPaymentInstance.removeFromPurchaseInvoices(PurchaseInvoice.get(it))
        }
        return supplierPaymentInstance
    }
}
