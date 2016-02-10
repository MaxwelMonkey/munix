package com.munix

import grails.test.*

class SalesDeliveryTests extends GrailsUnitTestCase {
    def salesDelivery
	def sampleSalesDelivery
	def sampleCounterReceipt
	def sampleCounterReceiptItem
	def sampleSalesOrder
	def sampleCustomer
	def sampleCustomerAccount
	def sampleDirectPaymentInvoice
	
    protected void setUp() {
        super.setUp()
	
		def sampleDiscountType
		sampleSalesOrder = new SalesOrder(
			date: new Date(),
			status: "Approved",
			discountType:sampleDiscountType,
			priceType:PriceType.RETAIL
			)
		mockDomain(SalesOrder, [sampleSalesOrder])

		sampleCustomerAccount = new CustomerAccount()
		mockDomain(CustomerAccount, [sampleCustomerAccount])
		
		sampleCustomer = new Customer(
			customerAccount: sampleCustomerAccount
		)
		mockDomain(Customer, [sampleCustomer])
		
		salesDelivery = new SalesDelivery()
		def sampleWarehouse
		sampleSalesDelivery = new SalesDelivery(
			customer: sampleCustomer,
			termDay: new BigDecimal("1"),
			warehouse: sampleWarehouse,
			deliveryType: "Type1",
			commissionRate: new BigDecimal("0"),
			invoice: sampleSalesOrder
			)
        mockDomain(SalesDelivery, [salesDelivery, sampleSalesDelivery])
        salesDelivery.metaClass.'static'.computeDiscountedItemsTotal = { -> return new BigDecimal("1") }
		salesDelivery.metaClass.'static'.computeNetItemsTotal = { -> return new BigDecimal("1") }
		
		def sampleProduct1 = new Product(
			identifier:"SampleProduct1", 
			status:"Active", 
			isNet:true
			)
		def sampleProduct2 = new Product(
			identifier:"SampleProduct2",
			status:"Active",
			isNet:true
			)
		def sampleProduct3 = new Product(
			identifier:"SampleProduct3",
			status:"Active",
			isNet:false
			)
		def sampleProduct4 = new Product(
			identifier:"SampleProduct4",
			status:"Active",
			isNet:false
			)
		mockDomain(Product, [sampleProduct1, sampleProduct2, sampleProduct3, sampleProduct4])
		
		//assemble sales order
		def sampleSalesOrderItem = new SalesOrderItem(
			qty: new BigDecimal("6"),
			finalPrice: new BigDecimal("11.11"),
			product: sampleProduct1,
			isNet: true
			)
		def sampleSalesOrderItem2 = new SalesOrderItem(
			qty: new BigDecimal("6"),
			finalPrice: new BigDecimal("11.11"),
			product: sampleProduct3,
			isNet: false
			)
		mockDomain(SalesOrderItem, [sampleSalesOrderItem])

		def sampleSalesDeliveryItem1 = new SalesDeliveryItem(
			qty: new BigDecimal("6"),
			price: new BigDecimal("11.11"),
			product: sampleProduct1,
			orderItem: sampleSalesOrderItem
			)
		def sampleSalesDeliveryItem2 = new SalesDeliveryItem(
			qty: new BigDecimal("3"),
			price: new BigDecimal("11.11"),
			product: sampleProduct2,
			orderItem: sampleSalesOrderItem
			)
		def sampleSalesDeliveryItem3 = new SalesDeliveryItem(
			qty: new BigDecimal("6"),
			price: new BigDecimal("11.11"),
			product: sampleProduct3,
			orderItem: sampleSalesOrderItem2
			)
		def sampleSalesDeliveryItem4 = new SalesDeliveryItem(
			qty: new BigDecimal("3"),
			price: new BigDecimal("11.11"),
			product: sampleProduct4,
			orderItem: sampleSalesOrderItem2
			)
		mockDomain(SalesDeliveryItem, [sampleSalesDeliveryItem1, sampleSalesDeliveryItem2, sampleSalesDeliveryItem3, sampleSalesDeliveryItem4])
		sampleSalesDelivery.items = [sampleSalesDeliveryItem1, sampleSalesDeliveryItem2, sampleSalesDeliveryItem3, sampleSalesDeliveryItem4]
		
		sampleCounterReceipt = new CounterReceipt()
		mockDomain(CounterReceipt, [sampleCounterReceipt])
		
		sampleCounterReceiptItem = new CounterReceiptItem(
			counterReceipt: sampleCounterReceipt,
			invoice: sampleSalesDelivery
		)
		mockDomain(CounterReceiptItem, [sampleCounterReceiptItem])
		
		sampleDirectPaymentInvoice = new DirectPaymentInvoice()
		mockDomain(DirectPaymentInvoice, [sampleDirectPaymentInvoice])
    }

    protected void tearDown() {
        super.tearDown()
        def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
        remove SalesDelivery
    }

    void testComputeDiscountedDiscountLessThan5() {
		sampleSalesDelivery.invoice.discount = new BigDecimal("3.00")
		sampleSalesDelivery.invoice.discountGroup = new DiscountGroup()
        def amount = sampleSalesDelivery.computeDiscountedDiscount()
        assertEquals "The value is not expected", new BigDecimal("2.99"), amount
    }
	
    void testComputeDiscountedDiscountGreaterThan5() {
		sampleSalesDelivery.invoice.discount = new BigDecimal("10.00")
		sampleSalesDelivery.invoice.discountGroup = new DiscountGroup()
        def amount = sampleSalesDelivery.computeDiscountedDiscount()
        assertEquals "The value is not expected", new BigDecimal("9.99"), amount
    }
	
    void testComputeDiscountedDiscountEqual5() {
		sampleSalesDelivery.invoice.discount = new BigDecimal("5.00")
		sampleSalesDelivery.invoice.discountGroup = new DiscountGroup()
		
        def amount = sampleSalesDelivery.computeDiscountedDiscount()
        assertEquals "The value is not expected", new BigDecimal("4.99"), amount
    }
	void testComputeNetDiscountLessThan5() {
		sampleSalesDelivery.invoice.netDiscount = new BigDecimal("3.00")
		sampleSalesDelivery.invoice.netDiscountGroup = new DiscountGroup()
		
		def amount = sampleSalesDelivery.computeNetDiscount()
		assertEquals "The value is not expected", new BigDecimal("2.99"), amount
	}
	
	void testComputeNetDiscountGreaterThan5() {
		sampleSalesDelivery.invoice.netDiscount = new BigDecimal("10.00")
		sampleSalesDelivery.invoice.netDiscountGroup = new DiscountGroup()
		
		def amount = sampleSalesDelivery.computeNetDiscount()
		assertEquals "The value is not expected", new BigDecimal("9.99"), amount
	}
	
	void testComputeNetDiscountEqual5() {
		sampleSalesDelivery.invoice.netDiscount = new BigDecimal("5.00")
		sampleSalesDelivery.invoice.netDiscountGroup = new DiscountGroup()
		def amount = sampleSalesDelivery.computeNetDiscount()
		assertEquals "The value is not expected", new BigDecimal("4.99"), amount
	}
	
	void testComputeNetItemsTotal(){	
		def computedTotal = sampleSalesDelivery.computeNetItemsTotal()
		
		assertEquals "Should have a value of 99.99", 99.99, computedTotal
	}
	
	void testComputeDiscountedItemsTotal(){
		def computedTotal = sampleSalesDelivery.computeDiscountedItemsTotal()
		
		assertEquals "Should have a value of 99.99", 99.99, computedTotal
	}
	
	void testComputeGrossTotal(){
		sampleSalesDelivery.computeNetItemsTotal()
		sampleSalesDelivery.computeDiscountedItemsTotal()
		
		def computedTotal = sampleSalesDelivery.computeGrossTotal()
		assertEquals "Should have a value of 199.98", 199.98, computedTotal
	}

	void testGetCounterReceipts() {
		assertEquals "wrong list of counter receipts", [sampleCounterReceipt], sampleSalesDelivery.counterReceipts 
	}
	
	void testFinishTransactionPartialPayment() {
		def amountApplied = BigDecimal.ONE
		sampleSalesDelivery.isTakenByDirectPayment = true
		
		sampleSalesDelivery.finishTransaction(amountApplied)
		
		assertEquals "must update amount paid", amountApplied, sampleSalesDelivery.amountPaid
		assertFalse "must not be taken by direct payment", sampleSalesDelivery.isTakenByDirectPayment
		assertEquals "must update customer account sd amount", -1 * amountApplied, sampleCustomerAccount.totalUnpaidSalesDeliveries
	}
	
	void testFinishTransaction() {
		def amountApplied = sampleSalesDelivery.computeTotalAmount()
		sampleSalesDelivery.isTakenByDirectPayment = true
		
		sampleSalesDelivery.finishTransaction(amountApplied)
		
		assertEquals "must update amount paid", amountApplied, sampleSalesDelivery.amountPaid
		assertFalse "must not be taken by direct payment", sampleSalesDelivery.isTakenByDirectPayment
		assertTrue "must be paid", sampleSalesDelivery.isPaid()
		assertEquals "must update customer account sd amount", -1 * amountApplied, sampleCustomerAccount.totalUnpaidSalesDeliveries
	}
	
	void testRollbackTransaction() {
		def amountApplied = BigDecimal.ONE
		
		sampleSalesDelivery.rollbackTransaction(amountApplied)
		
		assertEquals "must update amount paid", -1 * amountApplied, sampleSalesDelivery.amountPaid
		assertTrue "must be taken by direct payment", sampleSalesDelivery.isTakenByDirectPayment
		assertEquals "must update customer account sd amount", amountApplied, sampleCustomerAccount.totalUnpaidSalesDeliveries
	}
	
	void testIsUnapprovable() {
		
		assertTrue "Sales Delivery should be unapprovable", sampleSalesDelivery.isUnapprovable()
	}
	
	void testIsUnapprovableNotUnapprovable() {
		sampleSalesDelivery.addToInvoices(sampleDirectPaymentInvoice)
		
		assertFalse "Sales Delivery should not be unapprovable", sampleSalesDelivery.isUnapprovable()
	}
}
