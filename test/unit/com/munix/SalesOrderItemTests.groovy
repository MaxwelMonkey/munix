package com.munix

import grails.test.*

class SalesOrderItemTests extends GrailsUnitTestCase {
	def sampleSalesOrder
	def sampleProduct
	def sampleSalesOrderItem
	
    protected void setUp() {
        super.setUp()
			
		def sampleDiscountType

		sampleProduct = new Product(
			identifier:"SampleProduct1",
			status:"Active",
			isNet:true,
			runningCost: new BigDecimal("100")
			)
		mockDomain(Product, [sampleProduct])
						
		sampleSalesOrder = new SalesOrder(
			date: new Date(),
			status: "Approved",
			discountType:sampleDiscountType,
			priceType:PriceType.RETAIL
			)
		mockDomain(SalesOrder, [sampleSalesOrder])
		sampleSalesOrder.metaClass.'static'.computeDiscountedDiscount = { -> return new BigDecimal("9.99") }
		sampleSalesOrder.metaClass.'static'.computeNetDiscount = { -> return new BigDecimal("5.55") }
		
		sampleSalesOrderItem = new SalesOrderItem(
			qty: new BigDecimal("6"),
			finalPrice: new BigDecimal("11.11"),
			product: sampleProduct,
			isNet: true
			)
		mockDomain(SalesOrderItem, [sampleSalesOrderItem])
		sampleSalesOrder.items = [sampleSalesOrderItem]
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetRemainingBalance(){
		sampleSalesOrderItem.deliveredQty = 2
		assertEquals "sample sales order remaining balance should is wrong", 4, sampleSalesOrderItem.remainingBalance
    }
}
