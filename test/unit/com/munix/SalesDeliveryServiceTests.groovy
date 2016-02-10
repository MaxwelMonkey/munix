package com.munix

import grails.test.*

class SalesDeliveryServiceTests extends GrailsUnitTestCase {
    def salesDeliveryService = new SalesDeliveryService()
    def sampleSalesDelivery
    protected void setUp() {
        super.setUp()
        sampleSalesDelivery = new SalesDelivery()
        mockDomain(SalesDelivery, [sampleSalesDelivery])
    }

    protected void tearDown() {
        super.tearDown()
        mockDomain(CounterReceiptItem)
    }

    void testShowUnapproveButtonTestIfPaymentExist() {
        def sampleDirectPaymentInvoice = new DirectPaymentInvoice(type: CustomerPaymentType.SALES_DELIVERY)
        mockDomain(DirectPaymentInvoice, [sampleDirectPaymentInvoice])
        sampleSalesDelivery.addToInvoices(sampleDirectPaymentInvoice)
        assertFalse "Payment exist", salesDeliveryService.showUnapproveButton(sampleSalesDelivery)
    }
	
    void testShowUnapproveButtonTestIfDirectDeliveryExist() {
        def sampleDirectDelivery = new DirectDelivery()
        mockDomain(DirectDelivery, [sampleDirectDelivery])
        sampleSalesDelivery.directDelivery = sampleDirectDelivery
        assertFalse "Payment exist", salesDeliveryService.showUnapproveButton(sampleSalesDelivery)
    }
	
    void testShowUnapproveButtonTestIfCounterReceiptsExist() {
        def sampleCounterReceipt = new CounterReceipt()
        mockDomain(CounterReceipt, [sampleCounterReceipt])
        def sampleCounterReceiptItem = new CounterReceiptItem(invoiceId: sampleCounterReceipt.id, invoiceType: CustomerPaymentType.SALES_DELIVERY)
        mockDomain(CounterReceiptItem, [sampleCounterReceiptItem])
        sampleSalesDelivery.addToCounterReceipts(sampleCounterReceipt)
        assertFalse "Payment exist", salesDeliveryService.showUnapproveButton(sampleSalesDelivery)
    }
	
    void testShowUnapproveButtonTestIfCreditMemoItemExist() {
        def sampleCreditMemoItem = new CreditMemoItem()
        mockDomain(CreditMemoItem, [sampleCreditMemoItem])
        def sampleSalesDeliveryItem = new SalesDeliveryItem()
        mockDomain(SalesDeliveryItem, [sampleSalesDeliveryItem])
        sampleSalesDeliveryItem.addToCreditMemoItems(sampleCreditMemoItem)
        sampleSalesDelivery.addToItems(sampleSalesDeliveryItem)
        assertFalse "Payment exist", salesDeliveryService.showUnapproveButton(sampleSalesDelivery)
    }
	
    void testShowUnapproveButtonNoTransactionExist(){
        assertTrue "Payment exist",salesDeliveryService.showUnapproveButton(sampleSalesDelivery)
    }
}
