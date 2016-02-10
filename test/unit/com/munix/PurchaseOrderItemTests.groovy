package com.munix

import grails.test.*

class PurchaseOrderItemTests extends GrailsUnitTestCase {
	PurchaseOrder samplePurchaseOrder
	PurchaseOrderItem samplePurchaseOrderItem
	
    protected void setUp() {
        super.setUp()
		setUpPurchaseOrder()
		setUpPurchaseOrderItem()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testHasInvoiceNoInvoice() {
		assertFalse "sample purchase order item should not have an invoice", samplePurchaseOrderItem.hasInvoice()
    }
	
	void testHasInvoice() {
		PurchaseInvoice samplePurchaseInvoice = new PurchaseInvoice()
		mockDomain(PurchaseInvoice, [samplePurchaseInvoice])
		
		PurchaseInvoiceItem samplePurchaseInvoiceItem = new PurchaseInvoiceItem()
		mockDomain(PurchaseInvoiceItem, [samplePurchaseInvoiceItem])
		
		samplePurchaseOrderItem.addToReceivedItems(samplePurchaseInvoiceItem)
		
		assertTrue "sample purchase order item should have an invoice", samplePurchaseOrderItem.hasInvoice()
	}

	private void setUpPurchaseOrderItem() {
		samplePurchaseOrderItem = new PurchaseOrderItem()
		samplePurchaseOrderItem.po = samplePurchaseOrder
		mockDomain(PurchaseOrderItem, [samplePurchaseOrderItem])
	}

	private void setUpPurchaseOrder() {
		samplePurchaseOrder = new PurchaseOrder()
		mockDomain(PurchaseOrder, [samplePurchaseOrder])
	}
}
