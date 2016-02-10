package com.munix

import grails.test.*

class CreditMemoTests extends GrailsUnitTestCase {
	def generalMethodService = new GeneralMethodService()
	def sampleCreditMemo
	def sampleCreditMemo2
	def sampleCreditMemoItem1
	def sampleCreditMemoItem2
	def sampleCreditMemoItem3
	def sampleCustomer
	def sampleDiscountType
	def sampleCounterReceipt
	def sampleCounterReceiptItem
	
    protected void setUp() {
        super.setUp()
		sampleCustomer= new Customer(identifier:"customer",
			name:"customer name")
		
		sampleDiscountType=new DiscountType(identifier:"discountType",
			description:"discount type")
	
		setUpCreditMemos()
		setUpCreditMemoItems()
		setUpCreditMemoAndCreditMemoItemsRelationship()		
		setUpCounterReceipts()
		setUpCounterReceiptItems()
    }

    protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove CreditMemoItem
    }
	
	void testComputeTotalAmount() {		
		def computedAmount = sampleCreditMemo.computeTotalAmount()
		
		assertEquals "Should have a value of 99.99", 99.99, computedAmount
	}
	
	void testComputeCreditMemoTotalAmount() {
		def computedAmount = sampleCreditMemo.computeCreditMemoTotalAmount()
		assertEquals "Should have a value of -99.99", new BigDecimal("-99.99"), computedAmount
	}
	
	void testComputeProjectedDue() {
		sampleCreditMemo.status = "Unapproved"
		def computedAmount = sampleCreditMemo.computeProjectedDue()
		
		assertEquals "Should have a value of 99.99", -99.99, computedAmount
	}
	
	void testIsADebitMemo() {
		assertTrue "Should be a Debit Memo", sampleCreditMemo2.isADebitMemo() 
		assertFalse "Should not be a Debit Memo", sampleCreditMemo.isADebitMemo()
	}
	
	void testGetCounterReceipts() {
		assertEquals "wrong list of counter receipts", [sampleCounterReceipt], sampleCreditMemo.counterReceipts
	}

	void testGetAmountPaid() {
		sampleCreditMemo.status = "Paid"
		def computedAmount = sampleCreditMemo.amountPaid
		
		assertEquals "Should have a value of -99.99", -99.99, computedAmount
	}
	
	private void setUpCreditMemos() {
		sampleCreditMemo = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discount: new BigDecimal("0"),
			status: "Approved",
			preparedBy: "me",
			date: new Date()
		)
		sampleCreditMemo2 = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discount: new BigDecimal("0"),
			status: "Approved",
			preparedBy: "me",
			date: new Date()
		)
		mockDomain(CreditMemo, [sampleCreditMemo, sampleCreditMemo2])
	}
	
	private void setUpCreditMemoItems() {
		sampleCreditMemoItem1 = new CreditMemoItem(
			date:generalMethodService.createDate("02/10/2010"),
			oldQty:new BigDecimal("9"),
			oldPrice:new BigDecimal("11.11"),
			newQty:new BigDecimal("3"),
			newPrice:new BigDecimal("11.11")
			)
		assertEquals "[GUARD]Should have a value of 66.66", 66.66, sampleCreditMemoItem1.computeFinalAmount()
		
		sampleCreditMemoItem2 = new CreditMemoItem(
			date:generalMethodService.createDate("02/11/2010"),
			oldQty:new BigDecimal("6"),
			oldPrice:new BigDecimal("11.11"),
			newQty:new BigDecimal("3"),
			newPrice:new BigDecimal("11.11")
			)
		assertEquals "[GUARD]Should have a value of 33.33", 33.33, sampleCreditMemoItem2.computeFinalAmount()
		
		sampleCreditMemoItem3 = new CreditMemoItem(
			date:generalMethodService.createDate("02/11/2010"),
			oldQty:new BigDecimal("3"),
			oldPrice:new BigDecimal("11.11"),
			newQty:new BigDecimal("6"),
			newPrice:new BigDecimal("11.11")
			)
		assertEquals "[GUARD]Should have a value of -33.33", -33.33, sampleCreditMemoItem3.computeFinalAmount()
		
		mockDomain(CreditMemoItem, [sampleCreditMemoItem1, sampleCreditMemoItem2, sampleCreditMemoItem3])
		sampleCreditMemoItem1.metaClass.'static'.computeDiscountAmount = { -> return BigDecimal.ZERO }
		sampleCreditMemoItem2.metaClass.'static'.computeDiscountAmount = { -> return BigDecimal.ZERO }
		sampleCreditMemoItem3.metaClass.'static'.computeDiscountAmount = { -> return BigDecimal.ZERO }
	}
	
	private void setUpCreditMemoAndCreditMemoItemsRelationship() {
		sampleCreditMemo.addToItems(sampleCreditMemoItem1)
		sampleCreditMemo.addToItems(sampleCreditMemoItem2)
		sampleCreditMemo.save()
		
		sampleCreditMemo2.addToItems(sampleCreditMemoItem3)
		sampleCreditMemo2.save()
	}
	
	private void setUpCounterReceipts() {
		sampleCounterReceipt = new CounterReceipt()
		mockDomain(CounterReceipt, [sampleCounterReceipt])
	}
	
	private void setUpCounterReceiptItems() {
		sampleCounterReceiptItem = new CounterReceiptItem(
			counterReceipt: sampleCounterReceipt,
			invoice: sampleCreditMemo
		)
		mockDomain(CounterReceiptItem, [sampleCounterReceiptItem])
	}
}
