package com.munix

import grails.test.*

class CreditMemoItemTests extends GrailsUnitTestCase {
	def generalMethodService = new GeneralMethodService()
	def sampleProduct
	def sampleProductIsNet
	def sampleDiscountType
	def sampleSalesOrderNullDiscountGroup
	def sampleSalesOrderNullNetDiscountGroup
	def sampleSalesDeliveryNullDiscountGroup
	def sampleSalesDeliveryNetDiscountGroup
	def sampleSalesDeliveryItemProductIsNet
	def sampleSalesDeliveryItemDeliveryNullDiscountGroup
	def sampleSalesDeliveryItem
	def sampleCreditMemo
	def sampleCreditMemoItem1
	def sampleCreditMemoItem2
	def sampleSalesOrderItem1
	def sampleSalesOrderItem2
	
    protected void setUp() {
        super.setUp()
		setUpProducts()
		setUpCreditMemos()
		setUpDiscountTypes()
		setUpSalesOrders()
		setUpSalesOrderItems()
		setUpSalesDeliveries()
		setUpSalesDeliveryItem()
		setUpCreditMemoItems()
    }
	
	protected void tearDown() {
		super.tearDown()
	}

	void testComputeDiscountAmountGivenDeliveryItemProductIsNet() {
		assertTrue "[GUARD] sample Credit Memo Item's product should be net", sampleCreditMemoItem1.deliveryItem.orderItem.isNet
		
		def result = sampleCreditMemoItem1.computeDiscountAmount()

		assertEquals "Compute Discount Amount should be 0", BigDecimal.ZERO, result		
	}
	
	void testComputeDiscountAmountGivenDeliveryItemDeliveryNullDiscountGroup() {
		assertNull "[GUARD] sample Credit Memo Item's delivery should have null discount group", sampleCreditMemoItem2.deliveryItem.delivery.invoice.discountGroup
		
		def result = sampleCreditMemoItem2.computeDiscountAmount()

		assertEquals "Compute Discount Amount should be 0", BigDecimal.ZERO, result
	}
	
	void testIsUnapprovedReturnTrue() {
		sampleCreditMemo.unapprove()
		def result = sampleCreditMemoItem1.isUnapproved()

		assertTrue "Return results should be true ", result
	}
	
	void testIsUnapprovedReturnFalse() {
		sampleCreditMemo.approve()
		def result = sampleCreditMemoItem1.isUnapproved()

		assertFalse "Return results should be false ", result
	}

	private void setUpProducts() {
		sampleProductIsNet = new Product(
			isNet: true
		)
		sampleProduct= new Product(
			isNet: false
		)
		mockDomain(Product, [sampleProduct, sampleProductIsNet])
	}
	
	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(identifier: "discountType", description: "discount type")
		mockDomain(DiscountType, [sampleDiscountType])
	}
	
	private void setUpSalesDeliveries() {
		sampleSalesDeliveryNullDiscountGroup = new SalesDelivery(
			invoice: sampleSalesOrderNullDiscountGroup
		)
		sampleSalesDeliveryNetDiscountGroup = new SalesDelivery(
			invoice: sampleSalesOrderNullNetDiscountGroup
		)
		mockDomain(SalesDelivery, [sampleSalesDeliveryNullDiscountGroup, sampleSalesDeliveryNetDiscountGroup])
	}
	
	private void setUpSalesDeliveryItem() {
		sampleSalesDeliveryItemProductIsNet = new SalesDeliveryItem(
			product: sampleProductIsNet,
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE,
			orderItem: sampleSalesOrderItem1,
			delivery: sampleSalesDeliveryNetDiscountGroup
		)
		sampleSalesDeliveryItemDeliveryNullDiscountGroup = new SalesDeliveryItem(
			product: sampleProduct,
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE,
			orderItem: sampleSalesOrderItem2,
			delivery: sampleSalesDeliveryNullDiscountGroup
		)
		sampleSalesDeliveryItem = new SalesDeliveryItem(
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE
		)
		mockDomain(SalesDeliveryItem, [sampleSalesDeliveryItemDeliveryNullDiscountGroup, sampleSalesDeliveryItemProductIsNet, sampleSalesDeliveryItem])
	}
	
	private void setUpCreditMemos() {
		sampleCreditMemo = new CreditMemo()
		mockDomain(CreditMemo, [sampleCreditMemo])
	}
	
	private void setUpCreditMemoItems() {
		sampleCreditMemoItem1 = new CreditMemoItem(
			deliveryItem: sampleSalesDeliveryItemProductIsNet,
			date: generalMethodService.createDate("01/01/2001"),
			oldQty: new BigDecimal("5"),
			oldPrice: new BigDecimal("5"),
			newQty: BigDecimal.ONE,
			newPrice: BigDecimal.ONE
		)
		sampleCreditMemoItem1.creditMemo = sampleCreditMemo
		
		sampleCreditMemoItem2 = new CreditMemoItem(
			deliveryItem: sampleSalesDeliveryItemDeliveryNullDiscountGroup,
			date: generalMethodService.createDate("01/01/2002"),
			oldQty: new BigDecimal("5"),
			oldPrice: new BigDecimal("5"),
			newQty: new BigDecimal("10"),
			newPrice: new BigDecimal("10")
		)
		sampleCreditMemoItem2.creditMemo = sampleCreditMemo
		mockDomain(CreditMemoItem, [sampleCreditMemoItem1, sampleCreditMemoItem2])
	}
	
	private void setUpSalesOrders() {
		sampleSalesOrderNullDiscountGroup = new SalesOrder(
			discountGroup: null,
			discountType:sampleDiscountType,
			priceType:PriceType.RETAIL,
			items:[new SalesOrderItem(product:sampleProduct,
				qty:new BigDecimal("1"),
				price:new BigDecimal("0"),
				finalPrice:new BigDecimal("0"))])
		
		sampleSalesOrderNullNetDiscountGroup = new SalesOrder(
			netDiscountGroup: null,
			discountType:sampleDiscountType,
			priceType:PriceType.RETAIL,
			items:[new SalesOrderItem(product:sampleProduct,
				qty:new BigDecimal("1"),
				price:new BigDecimal("0"),
				finalPrice:new BigDecimal("0"))])
		mockDomain(SalesOrder, [sampleSalesOrderNullDiscountGroup, sampleSalesOrderNullNetDiscountGroup])
	}
	
	private void setUpSalesOrderItems() {
		sampleSalesOrderItem1 = new SalesOrderItem(isNet:true)
		sampleSalesOrderItem2 = new SalesOrderItem()
		mockDomain(SalesOrderItem, [sampleSalesOrderItem1, sampleSalesOrderItem2])
	}
}
