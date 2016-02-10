package com.munix

import grails.test.*

class SalesOrderTests extends GrailsUnitTestCase {
	def sampleSalesOrder
	def sampleProduct1
	def sampleProduct2
	def sampleSalesOrderItem1
	def sampleSalesOrderItem2
	def sampleSalesDelivery
	
    protected void setUp() {
        super.setUp()
			
		def sampleDiscountType

		sampleProduct1 = new Product(
			identifier:"SampleProduct1",
			status:"Active",
			isNet:true,
			runningCost: new BigDecimal("100")
			)
		sampleProduct2 = new Product(
			identifier:"SampleProduct2",
			status:"Active",
			isNet:false,
			runningCost: new BigDecimal("50")
			)
		mockDomain(Product, [sampleProduct1, sampleProduct2])
						
		sampleSalesOrder = new SalesOrder(
			date: new Date(),
			status: "Approved",
			discountType:sampleDiscountType,
			priceType:PriceType.RETAIL
			)
		mockDomain(SalesOrder, [sampleSalesOrder])
		sampleSalesOrder.metaClass.'static'.computeDiscountedDiscount = { -> return new BigDecimal("9.99") }
		sampleSalesOrder.metaClass.'static'.computeNetDiscount = { -> return new BigDecimal("5.55") }
		
		
		sampleSalesOrderItem1 = new SalesOrderItem(
			qty: new BigDecimal("6"),
			finalPrice: new BigDecimal("11.11"),
			product: sampleProduct1,
			isNet: true
			)
		sampleSalesOrderItem2 = new SalesOrderItem(
			qty: new BigDecimal("3"),
			finalPrice: new BigDecimal("11.11"),
			product: sampleProduct2,
			isNet: false
			)
		mockDomain(SalesOrderItem, [sampleSalesOrderItem1, sampleSalesOrderItem2])
		sampleSalesOrder.items = [sampleSalesOrderItem1, sampleSalesOrderItem2]
		
		sampleSalesDelivery = new SalesDelivery()
		mockDomain(SalesDelivery, [sampleSalesDelivery])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testComputeTotal(){
		def computedTotal = sampleSalesOrder.computeTotal()			
		assertEquals "Should have a value of 99.99", 99.99, computedTotal
    }
	
	void testComputeNetItemsTotal(){
		def computedTotal = sampleSalesOrder.computeNetItemsTotal()
		assertEquals "Should have a value of 66.66", 66.66, computedTotal
	}
	
	void testComputeDiscountedItemsTotal(){
		def computedTotal = sampleSalesOrder.computeDiscountedItemsTotal()
		assertEquals "Should have a value of 33.33", 33.33, computedTotal
	}
	
	void testComputeDiscountedTotal(){
		def computedTotal = sampleSalesOrder.computeDiscountedTotal()
		assertEquals "Should have a value of 23.34", 23.34, computedTotal
	}
	
	void testComputeNetTotal(){
		def computedTotal = sampleSalesOrder.computeNetTotal()
		assertEquals "Should have a value of 61.11", 61.11, computedTotal
	}
	
	void testComputeGrandTotal(){
		def computedTotal = sampleSalesOrder.computeGrandTotal()
		assertEquals "Should have a value of 84.45", 84.45, computedTotal
	}
	
	void testUpdateItemCosts() {
		sampleSalesOrder.updateItemCosts()
		
		assertEquals "sample sales order item 1 should have updated cost", sampleProduct1.runningCost, sampleSalesOrderItem1.cost
		assertEquals "sample sales order item 2 should have updated cost", sampleProduct2.runningCost, sampleSalesOrderItem2.cost
	}
	
	void testRemoveItemCosts() {
		sampleSalesOrderItem1.cost = BigDecimal.ONE
		sampleSalesOrderItem2.cost = BigDecimal.ONE
		
		sampleSalesOrder.removeItemCosts()
		
		assertNull "sample sales order item 1 cost should be null", sampleSalesOrderItem1.cost
		assertNull "sample sales order item 2 cost should be null", sampleSalesOrderItem2.cost
	}
	
	void testIsUnapprovable() {
		assertTrue "sample sales order should be unapprovable", sampleSalesOrder.isUnapprovable
	}
	
	void testIsUnapprovableNotUnapprovable() {
		sampleSalesOrder.addToDeliveries(sampleSalesDelivery)
		
		assertFalse "sample sales order should not be unapprovable", sampleSalesOrder.isUnapprovable
	}
	
	void testIsUnapprovableApprovableCancelledSD() {
		sampleSalesDelivery.cancel()
		sampleSalesOrder.addToDeliveries(sampleSalesDelivery)
		
		assertTrue "sample sales order should be unapprovable", sampleSalesOrder.isUnapprovable
	}
	
	void testIsUnapprovableUnapprovableNotAllSDsAreCancelled() {
		sampleSalesDelivery.cancel()
		sampleSalesOrder.addToDeliveries(sampleSalesDelivery)
		def salesDelivery = new SalesDelivery()
		sampleSalesOrder.addToDeliveries(salesDelivery)
		
		assertFalse "sample sales order should not be unapprovable", sampleSalesOrder.isUnapprovable
	}
	
	void testIsCancelable(){
        sampleSalesOrder.unapproved()
        sampleSalesOrder.save()
        assertTrue "[GUARD] Sales Order should be unapproved", sampleSalesOrder.isUnapproved()
		assertTrue "sample sales order should be cancelable", sampleSalesOrder.isCancelable()
	}
	
	void testIsCancelableNotCancelable(){
		sampleSalesOrder.addToDeliveries(sampleSalesDelivery)
		assertFalse "sample sales order should not be cancelable", sampleSalesOrder.isCancelable()
	}
	
	void testIsCancelableApprovableCancelledSD() {
		sampleSalesDelivery.cancel()
		sampleSalesOrder.addToDeliveries(sampleSalesDelivery)
		sampleSalesOrder.unapproved()
        sampleSalesOrder.save()
        assertTrue "[GUARD] Sales Order should be unapproved", sampleSalesOrder.isUnapproved()
		assertTrue "sample sales order should be cancelable", sampleSalesOrder.isCancelable()
	}
	
	void testIsCancelableUnapprovableNotAllSDsAreCancelled() {
		sampleSalesDelivery.cancel()
		sampleSalesOrder.addToDeliveries(sampleSalesDelivery)
		def salesDelivery = new SalesDelivery()
		sampleSalesOrder.addToDeliveries(salesDelivery)
		
		assertFalse "sample sales order should not be cancelable", sampleSalesOrder.isCancelable()
	}
}
