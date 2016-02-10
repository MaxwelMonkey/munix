package com.munix

import grails.test.*

class PurchaseOrderTests extends GrailsUnitTestCase {
	def sampleSupplier
	def samplePurchaseOrder
	def samplePurchaseOrder2
	
    protected void setUp() {
        super.setUp()
		
		sampleSupplier = new Supplier(identifier:"sampleSupplier")
		mockDomain(Supplier,[sampleSupplier])
		samplePurchaseOrder = new PurchaseOrder(supplier:sampleSupplier,year:"2012", counterId: 1)
        mockDomain(PurchaseOrder, [samplePurchaseOrder])
		samplePurchaseOrder2 = new PurchaseOrder(supplier:sampleSupplier,year:"2012", counterId: 2)
		mockDomain(PurchaseOrder, [samplePurchaseOrder2])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCheckForCompletion() {
        mockDomain(PurchaseOrderItem)
        def inCompleteItems = [
            new PurchaseOrderItem(isComplete : true),
            new PurchaseOrderItem(isComplete : false),
            new PurchaseOrderItem(isComplete : true),
        ]
        def completeItems = [
            new PurchaseOrderItem(isComplete : true),
            new PurchaseOrderItem(isComplete : true),
            new PurchaseOrderItem(isComplete : true),
        ]

        def poIncomplete = new PurchaseOrder(items : inCompleteItems)
        assertFalse "should be incomplete", poIncomplete.checkForCompletion()

        def poComplete = new PurchaseOrder(items : completeItems)
        assertTrue "should be complete", poComplete.checkForCompletion()
    }
	
	void testApprove() {
		samplePurchaseOrder.approve()
		
		assertEquals "sample purchase order's status should be approved", "Approved", samplePurchaseOrder.status
	}
	
	void testIsApprovedCorrectStatus() {
		samplePurchaseOrder.status = "Approved"
		
		assertTrue "should return true", samplePurchaseOrder.isApproved()
	}
	
	void testIsApprovedWrongStatus() {
		samplePurchaseOrder.status = "Unapproved"
		
		assertFalse "should return false", samplePurchaseOrder.isApproved()
	}
	
	void testIsUnapprovedCorrectStatus() {
		samplePurchaseOrder.status = "Unapproved"
		
		assertTrue "should return true", samplePurchaseOrder.isUnapproved()
	}
	
	void testIsUnapprovedWrongStatus() {
		samplePurchaseOrder.status = "Approved"
		
		assertFalse "should return false", samplePurchaseOrder.isUnapproved()
	}
	
	void testNewSupplierGenerateCounterId() {
		def sup = new Supplier(identifier:"sup")
		def po = new PurchaseOrder(supplier:sup,year:"2012")
		def counterId = po.generateCounterId()
		assertEquals "wrong counter id", 1, counterId
	}
	
	void testNewYearGenerateCounterId() {
		def po = new PurchaseOrder(supplier:sampleSupplier,year:"2013")
		def counterId = po.generateCounterId()
		assertEquals "wrong counter id", 1, counterId
	}
	
	void testSameSupplierSameYearGenerateCounterId() {
		def po = new PurchaseOrder(supplier:sampleSupplier,year:"2012")
		def counterId = po.generateCounterId()
		assertEquals "wrong counter id", 3, counterId
	}
	
	void testFormatId() {
		assertEquals "wrong formatID()", "sampleSupplier-2012-00002", samplePurchaseOrder2.formatId()
	}
	
	void testAnotherFormatId() {
		samplePurchaseOrder2.counterId = 20
		assertEquals "wrong toString()", "sampleSupplier-2012-00020", samplePurchaseOrder2.formatId()
	}
}
