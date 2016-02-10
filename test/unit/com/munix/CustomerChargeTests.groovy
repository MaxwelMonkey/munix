package com.munix

import grails.test.*

class CustomerChargeTests extends GrailsUnitTestCase {
	def sampleCustomerCharge
	def sampleCustomerChargeItem1
	def sampleCustomerChargeItem2
	def sampleDirectPaymentInvoice1
	def sampleDirectPaymentInvoice2
	def sampleDirectPayment
	def sampleCounterReceipt1
	def sampleCounterReceipt2
	def sampleCounterReceiptItem1
	def sampleCounterReceiptItem2
	def sampleCustomerAccount
	def sampleCustomer

	def mockCounterReceipt
		
    protected void setUp() {
        super.setUp()
		setUpCustomerCharges()
		setUpCustomerChargeItems()
		setUpCustomerChargeAndCustomerChargeRelationship()

		setUpDirectPayment()
		setUpDirectPaymentInvoices()
		setUpCustomerChargeAndDirectPaymentInvoiceRelationship()
		
		setUpCounterReceipts()
		setUpCounterReceiptItems()

        setupCustomer()
        setupCustomerAccount()
        setupLinkBetweenCustomerAccountAndCustomer()

        setupLinkBetweenCustomerAndCustomerCharge()
    }

	protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove CounterReceipt
    }

	void testToString() {
		def id = sampleCustomerCharge.id
		
		def customerChargeString = sampleCustomerCharge.toString()
		
		assertEquals "String should be CC-0000000" + id, "CC-0000000" + id, customerChargeString  
	}
	
	void testComputeTotalAmount() {
		def computedAmount = sampleCustomerCharge.computeTotalAmount()
		
		assertEquals "Should have a value of 1100.10", new BigDecimal("1100.10"), computedAmount
	}	
	
	void testComputeProjectedDue(){
		def computedDue = sampleCustomerCharge.computeProjectedDue()
		
		assertEquals "Should have a value of 700.00", new BigDecimal("700.00"), computedDue
	}
	
	void testComputeReceiptsTotal() {
		assertEquals "[GUARD] Customer Charge should have 2 receipts", 2, sampleCustomerCharge.counterReceipts.size()

		def receiptsTotal = sampleCustomerCharge.computeReceiptsTotal()
		
		assertEquals "Receipts total should be 200", new BigDecimal("200"), receiptsTotal
	}
	
	void testComputeReceiptsDueTotal() {
		assertEquals "[GUARD] Customer Charge should have 2 receipts", 2, sampleCustomerCharge.counterReceipts.size()
		
		def receiptsDueTotal = sampleCustomerCharge.computeReceiptsDueTotal()
		
		assertEquals "Receipts Due total should be 100", new BigDecimal("100"), receiptsDueTotal
	}
	
	void testFinishTransactionFullyPaid() {
		sampleCustomerCharge.isTakenByDirectPayment = true
		assertEquals "[GUARD] Amount Paid should be 0", BigDecimal.ZERO, sampleCustomerCharge.amountPaid
		assertEquals "[GUARD] Status should be Unpaid", "Unpaid", sampleCustomerCharge.status
		assertEquals "[GUARD] Customer Charge should be takenByDirectPayment", true, sampleCustomerCharge.isTakenByDirectPayment
		
		sampleCustomerCharge.finishTransaction(new BigDecimal("1100.10"))
		
		assertEquals "Amount Paid should be 1100.10", new BigDecimal("1100.10"), sampleCustomerCharge.amountPaid
		assertEquals "Status should be paid", "Paid", sampleCustomerCharge.status
		assertEquals "Customer Charge should be not takenByDirectPayment", false, sampleCustomerCharge.isTakenByDirectPayment
	}
	
	void testFinishTransactionPartiallyPaid() {
		sampleCustomerCharge.finishTransaction(new BigDecimal("100.10"))
		
		assertEquals "Amount Paid should be 100.10", new BigDecimal("100.10"), sampleCustomerCharge.amountPaid
		assertEquals "Status should still be unpaid", "Unpaid", sampleCustomerCharge.status
		assertEquals "Customer Charge should still be not takenByDirectPayment", false, sampleCustomerCharge.isTakenByDirectPayment
	}
	
	void testRollbackTransaction() {
		sampleCustomerCharge.amountPaid = new BigDecimal("100")
		sampleCustomerCharge.status = "Paid"
		assertEquals "[GUARD] Amount Paid should be 100", new BigDecimal("100"), sampleCustomerCharge.amountPaid
		assertEquals "[GUARD] Status should be Paid", "Paid", sampleCustomerCharge.status
		assertEquals "[GUARD] Customer Charge should be not takenByDirectPayment", false, sampleCustomerCharge.isTakenByDirectPayment
		
		sampleCustomerCharge.rollbackTransaction(new BigDecimal("10.10"))
		
		assertEquals "[GUARD] Amount Paid should be 89.90", new BigDecimal("89.90"), sampleCustomerCharge.amountPaid
		assertEquals "[GUARD] Status should be unpaid", "Unpaid", sampleCustomerCharge.status
		assertEquals "[GUARD] Customer Charge should be takenByDirectPayment", true, sampleCustomerCharge.isTakenByDirectPayment
	}
	
	void testUnapprove() {
		assertEquals "[GUARD] Status should be Unpaid", "Unpaid", sampleCustomerCharge.status
		
		sampleCustomerCharge.unapprove()
		
		assertEquals "Status should be Unapproved", "Unapproved", sampleCustomerCharge.status
	}
	
	void testCancel() {
		assertEquals "[GUARD] Status should be Unpaid", "Unpaid", sampleCustomerCharge.status
		
		sampleCustomerCharge.cancel()
		
		assertEquals "Status should be Cancelled", "Cancelled", sampleCustomerCharge.status
	}
	
	void testGetCounterReceipts() {
		assertTrue "should contain sample counter receipt 1", sampleCustomerCharge.counterReceipts.contains(sampleCounterReceipt1)
		assertTrue "should contain sample counter receipt 2", sampleCustomerCharge.counterReceipts.contains(sampleCounterReceipt2)
		assertEquals "should only have 2 counter receipts", 2, sampleCustomerCharge.counterReceipts.size()
	}
	
	void testIsUnapprovable() {
		setUpCustomerCharges()
		
		assertTrue "Customer Charge should be unapprovable", sampleCustomerCharge.isUnapprovable()
	}
	
	void testIsUnapprovableNotUnapprovable() {
		assertFalse "[GUARD] Customer Charge should have invoices", sampleCustomerCharge.invoices.isEmpty()
		
		assertFalse "Customer Charge should not be unapprovable", sampleCustomerCharge.isUnapprovable()
	}
	
	void testIsCancelable() {
		setUpCustomerCharges()
		sampleCustomerCharge.unapprove()
        sampleCustomerCharge.save(flush:true)
        assertTrue "[GUARD] Customer Charge should be unapproved", sampleCustomerCharge.isUnapproved()
		assertTrue "Customer Charge should be cancelable", sampleCustomerCharge.isCancelable()
	}
	
	void testIsCancelableNotCancelable() {
		assertFalse "[GUARD] Customer Charge should have invoices", sampleCustomerCharge.invoices.isEmpty()
		sampleCustomerCharge.unapprove()
        sampleCustomerCharge.save(flush:true)
        assertTrue "[GUARD] Customer Charge should be unapproved", sampleCustomerCharge.isUnapproved()
		assertFalse "Customer Charge should not be cancelable", sampleCustomerCharge.isCancelable()
	}
	
	void testIsCancelableInvoicesCancelled() {
		assertFalse "[GUARD] Customer Charge should have invoices", sampleCustomerCharge.invoices.isEmpty()
		
		sampleDirectPayment.cancelled()
		sampleCustomerCharge.unapprove()
        sampleCustomerCharge.save(flush:true)
        assertTrue "[GUARD] Customer Charge should be unapproved", sampleCustomerCharge.isUnapproved()
		assertTrue "Customer Charge should be cancelable", sampleCustomerCharge.isCancelable()
	}
	
	private void setUpCustomerCharges() {
		sampleCustomerCharge = new CustomerCharge(
				date: new Date(),
				status: "Unpaid",
				customer: new Customer(),
				preparedBy: "me")
		mockDomain(CustomerCharge, [sampleCustomerCharge])
	}
	
	private void setUpCustomerChargeItems() {
		sampleCustomerChargeItem1 = new CustomerChargeItem(
				charge: new Charge(),
				amount: new BigDecimal("100.00")
			)
		sampleCustomerChargeItem2 = new CustomerChargeItem(
				charge: new Charge(),
				amount: new BigDecimal("1000.10")
			)
		mockDomain(CustomerChargeItem, [sampleCustomerChargeItem1, sampleCustomerChargeItem2])
	}
	
	private void setUpCustomerChargeAndCustomerChargeRelationship() {
		sampleCustomerCharge.items = [sampleCustomerChargeItem1, sampleCustomerChargeItem2]
		sampleCustomerCharge.save()
	}
	private void setUpDirectPayment(){
		sampleDirectPayment = new DirectPayment(
				status:"Approved"
		)
		mockDomain(DirectPayment, [sampleDirectPayment])
	}
	private void setUpDirectPaymentInvoices() {
		sampleDirectPaymentInvoice1 = new DirectPaymentInvoice(
				type: CustomerPaymentType.CUSTOMER_CHARGE,
				amount: new BigDecimal("100.00"),
				directPayment: sampleDirectPayment
				)
		sampleDirectPaymentInvoice2 = new DirectPaymentInvoice(
				type: CustomerPaymentType.CUSTOMER_CHARGE,
				amount: new BigDecimal("300.10"),
				directPayment: sampleDirectPayment
				)
		mockDomain(DirectPaymentInvoice, [sampleDirectPaymentInvoice1, sampleDirectPaymentInvoice2])
	}
	
	private void setUpCustomerChargeAndDirectPaymentInvoiceRelationship() {
		sampleCustomerCharge.invoices = [sampleDirectPaymentInvoice1, sampleDirectPaymentInvoice2]
		sampleCustomerCharge.save()
	}
	
	private void setUpCounterReceipts() {
		sampleCounterReceipt1 = new CounterReceipt(
			date: new Date(),
			status: CounterReceipt.Status.APPROVED,
			customer: new Customer(),
			preparedBy: "me")
		
		sampleCounterReceipt2 = new CounterReceipt(
			date: new Date(),
			status: CounterReceipt.Status.APPROVED,
			customer: new Customer(),
			preparedBy: "me")
		mockDomain(CounterReceipt, [sampleCounterReceipt1, sampleCounterReceipt2])

		CounterReceipt.metaClass.static.computeTotal = { -> return new BigDecimal("100")}
		CounterReceipt.metaClass.static.computeAmountDueTotal = { -> return new BigDecimal("50")}
	}
	
	private void setUpCounterReceiptItems() {
		sampleCounterReceiptItem1 = new CounterReceiptItem(
			counterReceipt: sampleCounterReceipt1,
			invoice: sampleCustomerCharge
		)
		sampleCounterReceiptItem2 = new CounterReceiptItem(
			counterReceipt: sampleCounterReceipt2,
			invoice: sampleCustomerCharge
		)
		mockDomain(CounterReceiptItem, [sampleCounterReceiptItem1, sampleCounterReceiptItem2])
	}
    private setupCustomerAccount(){
        sampleCustomerAccount = new CustomerAccount()
        mockDomain(CustomerAccount, [sampleCustomerAccount])
    }
    private setupLinkBetweenCustomerAccountAndCustomer(){
        sampleCustomer.customerAccount = sampleCustomerAccount
        sampleCustomer.save()
    }
     private setupCustomer(){
        sampleCustomer = new Customer()
        mockDomain(Customer, [sampleCustomer])
    }
    private setupLinkBetweenCustomerAndCustomerCharge(){
        sampleCustomerCharge.customer = sampleCustomer
        sampleCustomerCharge.save()
    }
}
