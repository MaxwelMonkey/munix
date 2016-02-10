package com.munix

import grails.test.*

class EditSupplierPaymentServiceTests extends GrailsUnitTestCase {
    def editSupplierPaymentService
    def samplePurchaseInvoice
    def samplePurchaseInvoice2
    def sampleSupplierPayment
    protected void setUp() {
        super.setUp()
        editSupplierPaymentService = new EditSupplierPaymentService()
        samplePurchaseInvoice = new PurchaseInvoice()
        samplePurchaseInvoice2 = new PurchaseInvoice()
        mockDomain(PurchaseInvoice,[samplePurchaseInvoice, samplePurchaseInvoice2])
        sampleSupplierPayment = new SupplierPayment(purchaseInvoices:[])
        mockDomain(SupplierPayment, [sampleSupplierPayment])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testaddPurchaseInvoiceToSupplierPaymentAddOne() {
        assertTrue "The purchaseInvoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
        sampleSupplierPayment = editSupplierPaymentService.addPurchaseInvoiceToSupplierPayment(sampleSupplierPayment, [samplePurchaseInvoice.id])
        assertEquals "The purchaseInvoices list contains more than expected", 1, sampleSupplierPayment.purchaseInvoices.size()
        assertTrue "The purchaseInvoices list does not contain the added item", sampleSupplierPayment.purchaseInvoices.contains(samplePurchaseInvoice)
    }
    void testaddPurchaseInvoiceToSupplierPaymentAddTwoMore() {
        assertTrue "The purchaseInvoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
        sampleSupplierPayment = editSupplierPaymentService.addPurchaseInvoiceToSupplierPayment(sampleSupplierPayment, [samplePurchaseInvoice.id,samplePurchaseInvoice2.id])
        assertEquals "The purchaseInvoices list contains more than expected", 2, sampleSupplierPayment.purchaseInvoices.size()
        assertTrue "The purchaseInvoices list does not contain the added item", sampleSupplierPayment.purchaseInvoices.contains(samplePurchaseInvoice)
        assertTrue "The purchaseInvoices list does not contain the added item", sampleSupplierPayment.purchaseInvoices.contains(samplePurchaseInvoice2)
    }
    void testaddPurchaseInvoiceToSupplierPaymentAddNone() {
        assertTrue "The purchaseInvoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
        sampleSupplierPayment = editSupplierPaymentService.addPurchaseInvoiceToSupplierPayment(sampleSupplierPayment, [])
        assertTrue "The purchaseInvoices list is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
    }
    void testremovePurchaseInvoiceToSupplierPaymentRemoveDoesNotExist() {
        assertTrue "The purchaseInvoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
        sampleSupplierPayment = editSupplierPaymentService.removePurchaseInvoiceToSupplierPayment(sampleSupplierPayment, [samplePurchaseInvoice.id])
        assertTrue "The purchaseInvoices list is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
    }
    void testremovePurchaseInvoiceToSupplierPaymentRemoveOneInvoice() {
        assertTrue "The purchaseInvoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
        sampleSupplierPayment.addToPurchaseInvoices(samplePurchaseInvoice)
        sampleSupplierPayment = editSupplierPaymentService.removePurchaseInvoiceToSupplierPayment(sampleSupplierPayment, [samplePurchaseInvoice.id])
        assertTrue "The purchaseInvoices list is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
    }
    void testremovePurchaseInvoiceToSupplierPaymentRemoveNone() {
        assertTrue "The purchaseInvoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
        sampleSupplierPayment.addToPurchaseInvoices(samplePurchaseInvoice)
        sampleSupplierPayment = editSupplierPaymentService.removePurchaseInvoiceToSupplierPayment(sampleSupplierPayment, [])
        assertTrue "The purchaseInvoices list does not contain the expected result", sampleSupplierPayment.purchaseInvoices.contains(samplePurchaseInvoice)
        assertEquals "The purchaseInvoices list contains more than the expected result", 1, sampleSupplierPayment.purchaseInvoices.size()
    }
}
