package com.munix

import grails.test.*

class SalesDeliveryItemTests extends GrailsUnitTestCase {
	def generalMethodService = new GeneralMethodService()
	def sampleDiscountProduct
	def sampleNetProduct
	def sampleSalesOrder
	def sampleDiscountSalesOrderItem
	def sampleNetSalesOrderItem
	def sampleSalesDelivery
	def sampleDiscountSalesDeliveryItem
	def sampleNetSalesDeliveryItem
	def sampleSalesDeliveryItemWithAttachedCreditMemoItem
	def sampleSalesDeliveryItemWithAttachedCreditMemoItems
	def sampleCreditMemo
	def sampleCreditMemoItem1
	def sampleCreditMemoItem2
	
    protected void setUp() {
        super.setUp()
		setUpProducts()
		setUpSampleSalesOrders()
		setUpSampleSalesOrderItems()
		setUpSalesDelivery()
		setUpSalesDeliveryItems()
		setUpCreditMemos()
		setUpCreditMemoItems()
		setUpSalesDeliveryItemAndCreditMemoItemRelationship()
		
    }
	
    protected void tearDown() {
        super.tearDown()
    }
	
	void testComputeDiscountedPriceForDiscountItem() {
		assertEquals "wrong discountedPrice", new BigDecimal(".50"), sampleDiscountSalesDeliveryItem.discountedPrice
	}
	
	void testComputeDiscountedPriceForNetItem() {
		assertEquals "wrong discountedPrice", new BigDecimal(".80"), sampleNetSalesDeliveryItem.discountedPrice
	}
	
	void testGetPriceMargin() {
		assertEquals "wrong margin percentage", new BigDecimal("-90"), sampleDiscountSalesDeliveryItem.getPriceMargin()
	}
	
	private void setUpProducts() {
		sampleDiscountProduct = new Product()
		sampleNetProduct = new Product(isNet: true)
		mockDomain(Product, [sampleDiscountProduct, sampleNetProduct])
	}
	
	private void setUpSampleSalesOrders() {
		sampleSalesOrder = new SalesOrder(
			netDiscount: new BigDecimal("20"),
			discount: new BigDecimal("50")
		)	
		mockDomain(SalesOrder,[sampleSalesOrder])
	}
	
	private void setUpSampleSalesOrderItems() {
		sampleDiscountSalesOrderItem = new SalesOrderItem(
			isNet: false
		)
		sampleNetSalesOrderItem = new SalesOrderItem(
			isNet: true
		)
		mockDomain(SalesOrderItem,[sampleDiscountSalesOrderItem, sampleNetSalesDeliveryItem])
	}
	
	private void setUpSalesDelivery() {
		sampleSalesDelivery = new SalesDelivery(
			invoice: sampleSalesOrder,
			status: "Approved"
		)
		mockDomain(SalesDelivery, [sampleSalesDelivery])
	}
	
	private void setUpSalesDeliveryItems() {
		sampleDiscountSalesDeliveryItem = new SalesDeliveryItem(
			product: sampleDiscountProduct,
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE,
			delivery: sampleSalesDelivery,
			cost: new BigDecimal("5"),
			orderItem: sampleDiscountSalesOrderItem
		)
		
		sampleNetSalesDeliveryItem = new SalesDeliveryItem(
			product: sampleNetProduct,
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE,
			delivery: sampleSalesDelivery,
			cost: new BigDecimal("5"),
			orderItem: sampleNetSalesOrderItem
		)
		
		sampleSalesDeliveryItemWithAttachedCreditMemoItem = new SalesDeliveryItem(
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE
		)
		
		sampleSalesDeliveryItemWithAttachedCreditMemoItems = new SalesDeliveryItem(
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE
		)
		
		mockDomain(SalesDeliveryItem, [sampleDiscountSalesDeliveryItem, sampleNetSalesDeliveryItem, sampleSalesDeliveryItemWithAttachedCreditMemoItem, sampleSalesDeliveryItemWithAttachedCreditMemoItems])
	}
	
	private void setUpCreditMemos() {
		sampleCreditMemo = new CreditMemo()
		mockDomain(CreditMemo, [sampleCreditMemo])
	}
	
	private void setUpCreditMemoItems() {
		sampleCreditMemoItem1 = new CreditMemoItem(
			deliveryItem: sampleSalesDeliveryItemWithAttachedCreditMemoItems,
			date: generalMethodService.createDate("01/01/2001"),
			oldQty: BigDecimal.ONE,
			oldPrice: BigDecimal.ONE,
			newQty: new BigDecimal("5"),
			newPrice: new BigDecimal("5")
		)
		sampleCreditMemoItem1.creditMemo = sampleCreditMemo
		
		sampleCreditMemoItem2 = new CreditMemoItem(
			deliveryItem: sampleSalesDeliveryItemWithAttachedCreditMemoItems,
			date: generalMethodService.createDate("01/01/2002"),
			oldQty: new BigDecimal("5"),
			oldPrice: new BigDecimal("5"),
			newQty: new BigDecimal("10"),
			newPrice: new BigDecimal("10")
		)
		sampleCreditMemoItem2.creditMemo = sampleCreditMemo
		
		mockDomain(CreditMemoItem, [sampleCreditMemoItem1, sampleCreditMemoItem2])
	}
	
	private void setUpSalesDeliveryItemAndCreditMemoItemRelationship() {
		sampleSalesDeliveryItemWithAttachedCreditMemoItem.creditMemoItems = [sampleCreditMemoItem1]
		sampleSalesDeliveryItemWithAttachedCreditMemoItem.save()
		
		sampleSalesDeliveryItemWithAttachedCreditMemoItems.creditMemoItems = [sampleCreditMemoItem1, sampleCreditMemoItem2]
		sampleSalesDeliveryItemWithAttachedCreditMemoItems.save()
	}
}
