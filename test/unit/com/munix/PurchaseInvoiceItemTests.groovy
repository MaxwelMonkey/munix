package com.munix

import grails.test.*

class PurchaseInvoiceItemTests extends GrailsUnitTestCase {
	def samplePurchaseInvoiceItem
	def samplePurchaseInvoice
	
    protected void setUp() {
        super.setUp()
		
		samplePurchaseInvoice = new PurchaseInvoice(
			exchangeRate: new BigDecimal("20"),
			discountRate: new BigDecimal("10")
		)
		mockDomain(PurchaseInvoice, [samplePurchaseInvoice])
		
		samplePurchaseInvoiceItem = new PurchaseInvoiceItem(
			finalPrice: new BigDecimal("10"),
			purchaseInvoice: samplePurchaseInvoice 
		)
		mockDomain(PurchaseInvoiceItem, [samplePurchaseInvoiceItem])
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testDiscountedPrice() {
		assertEquals "wrong discounted price", new BigDecimal("9"), samplePurchaseInvoiceItem.discountedPrice
	}
	
    void testLocalPrice() {
		assertEquals "wrong local price", new BigDecimal("200"), samplePurchaseInvoiceItem.localPrice
    }
	
	void testLocalDiscountedPrice() {
		assertEquals "wrong local discounted price", new BigDecimal("180"), samplePurchaseInvoiceItem.localDiscountedPrice
	}
}
