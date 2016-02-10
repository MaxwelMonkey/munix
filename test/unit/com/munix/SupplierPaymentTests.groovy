package com.munix

import grails.test.*

class SupplierPaymentTests extends GrailsUnitTestCase {
	def sampleSupplier
	def sampleSupplierPayment
	def sampleSupplierPaymentItem
	def sampleSupplierPaymentItem2
	def samplePurchaseInvoice
	def samplePurchaseInvoice2
	
    protected void setUp() {
        super.setUp()
		sampleSupplier = new Supplier(name: "supplier")
		mockDomain(Supplier, [sampleSupplier])
		
		sampleSupplierPaymentItem = new SupplierPaymentItem(
			amount: new BigDecimal("100")
		)
		sampleSupplierPaymentItem2 = new SupplierPaymentItem(
			amount: new BigDecimal("50")
		)
		mockDomain(SupplierPaymentItem, [sampleSupplierPaymentItem, sampleSupplierPaymentItem2])
		
		samplePurchaseInvoice = new PurchaseInvoice()
		samplePurchaseInvoice2 = new PurchaseInvoice()
		mockDomain(PurchaseInvoice, [samplePurchaseInvoice])
		samplePurchaseInvoice.metaClass.'static'.computePurchaseInvoiceDiscountedForeignTotal = { -> new BigDecimal("75") }
		samplePurchaseInvoice2.metaClass.'static'.computePurchaseInvoiceDiscountedForeignTotal = { -> new BigDecimal("75") }
		
		sampleSupplierPayment = new SupplierPayment(
				supplier: sampleSupplier,
				preparedBy: "me"
		)
		sampleSupplierPayment.items = [sampleSupplierPaymentItem, sampleSupplierPaymentItem2]
		sampleSupplierPayment.purchaseInvoices = [samplePurchaseInvoice, samplePurchaseInvoice2]
		mockDomain(SupplierPayment, [sampleSupplierPayment])
    }

    protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove PurchaseInvoice
    }

    void testComputePaymentTotal() {
		assertEquals "should be 150", new BigDecimal("150"), sampleSupplierPayment.computePaymentTotal()
    }
	
	void testComputeInvoiceTotal() {
		assertEquals "should be 150", new BigDecimal("150"), sampleSupplierPayment.computeInvoiceTotal()
	}
	
	void testComputeRemainingBalance() {
		assertEquals "should be 0", BigDecimal.ZERO, sampleSupplierPayment.computeRemainingBalance()
	}
	
	void testIsFullyPaid() {
		assertTrue "should be fully paid", sampleSupplierPayment.isFullyPaid()
	}
}
