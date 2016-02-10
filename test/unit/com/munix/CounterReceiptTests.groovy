package com.munix

import grails.test.*

class CounterReceiptTests extends GrailsUnitTestCase {
	def sampleCounterReceipt
	def sampleCounterReceiptItemForSalesDelivery1
	def sampleCounterReceiptItemForSalesDelivery2
	def sampleCounterReceiptItemForCustomerCharge1
	def sampleCounterReceiptItemForCustomerCharge2
	def sampleCounterReceiptItemForCreditMemo1
	def sampleCounterReceiptItemForCreditMemo2
	def sampleCounterReceiptItemForBouncedCheck1
	def sampleCounterReceiptItemForBouncedCheck2
	def sampleSalesDelivery1
	def sampleSalesDelivery2
	def sampleCustomerCharge1
	def sampleCustomerCharge2
	def sampleCreditMemo1
	def sampleCreditMemo2
	def sampleBouncedCheck1
	def sampleBouncedCheck2

    protected void setUp() {
        super.setUp()
		setUpCounterReceipts()
		setUpSalesDeliveries()
		setUpCustomerCharges()
		setUpCreditMemos()
		setUpBouncedChecks()
		setUpCounterReceiptItems()
		setUpCounterReceiptAndCounterReceiptItems()
    }
	
	private void setUpCounterReceipts() {
		sampleCounterReceipt = new CounterReceipt(
			date: new Date(),
			status: CounterReceipt.Status.APPROVED,
			customer: new Customer(),
			preparedBy: "me")
		mockDomain(CounterReceipt, [sampleCounterReceipt])
	}
	
	private void setUpSalesDeliveries() {
		sampleSalesDelivery1 = new SalesDelivery(amountPaid: new BigDecimal("100"))
		sampleSalesDelivery2 = new SalesDelivery(amountPaid: new BigDecimal("100"))
		mockDomain(SalesDelivery, [sampleSalesDelivery1, sampleSalesDelivery2])
		
		SalesDelivery.metaClass.static.computeTotalAmount = { -> return new BigDecimal("200")}
		SalesDelivery.metaClass.static.computeAmountDue = { -> return new BigDecimal("100")}
		SalesDelivery.metaClass.static.computeProjectedDue = { -> return new BigDecimal("100")}
	}
	
	private void setUpCustomerCharges() {
		sampleCustomerCharge1 = new CustomerCharge(amountPaid: new BigDecimal("100"))
		sampleCustomerCharge2 = new CustomerCharge(amountPaid: new BigDecimal("100"))
		mockDomain(CustomerCharge, [sampleCustomerCharge1, sampleCustomerCharge2])
		
		CustomerCharge.metaClass.static.computeTotalAmount = { -> return new BigDecimal("200")}
		CustomerCharge.metaClass.static.computeProjectedDue = { -> return new BigDecimal("100")}
	}
	
	private void setUpCreditMemos() {
		sampleCreditMemo1 = new CreditMemo()
		sampleCreditMemo2 = new CreditMemo()
		mockDomain(CreditMemo, [sampleCreditMemo1, sampleCreditMemo2])
		
		CreditMemo.metaClass.static.computeTotalAmount = { -> return new BigDecimal("-200")}
	}
	
	private void setUpBouncedChecks() {
		sampleBouncedCheck1 = new BouncedCheck(amountPaid: new BigDecimal("100"))
		sampleBouncedCheck2 = new BouncedCheck(amountPaid: new BigDecimal("100"))
		mockDomain(BouncedCheck, [sampleBouncedCheck1, sampleBouncedCheck2])
		
		BouncedCheck.metaClass.static.computeTotalAmount = { -> return new BigDecimal("200")}
		BouncedCheck.metaClass.static.computeProjectedDue = { -> return new BigDecimal("100")}
	}
	
	private void setUpCounterReceiptItems() {
		sampleCounterReceiptItemForSalesDelivery1 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleSalesDelivery1,
			counterReceipt: sampleCounterReceipt
		) 
		
		sampleCounterReceiptItemForSalesDelivery2 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleSalesDelivery2,
			counterReceipt: sampleCounterReceipt
		)
		
		sampleCounterReceiptItemForCustomerCharge1 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleCustomerCharge1,
			counterReceipt: sampleCounterReceipt
		)
		
		sampleCounterReceiptItemForCustomerCharge2 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleCustomerCharge2,
			counterReceipt: sampleCounterReceipt
		)
		
		sampleCounterReceiptItemForCreditMemo1 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleCreditMemo1,
			counterReceipt: sampleCounterReceipt
		)
		
		sampleCounterReceiptItemForCreditMemo2 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleCreditMemo2,
			counterReceipt: sampleCounterReceipt
		)
		
		sampleCounterReceiptItemForBouncedCheck1 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleBouncedCheck1,
			counterReceipt: sampleCounterReceipt
		)
		
		sampleCounterReceiptItemForBouncedCheck2 = new CounterReceiptItem(
			amount: new BigDecimal("100"),
			invoice: sampleBouncedCheck2,
			counterReceipt: sampleCounterReceipt
		)
		
		mockDomain(CounterReceiptItem, [
			sampleCounterReceiptItemForSalesDelivery1, sampleCounterReceiptItemForSalesDelivery2,
			sampleCounterReceiptItemForCustomerCharge1, sampleCounterReceiptItemForCustomerCharge2,
			sampleCounterReceiptItemForCreditMemo1, sampleCounterReceiptItemForCreditMemo2,
			sampleCounterReceiptItemForBouncedCheck1, sampleCounterReceiptItemForBouncedCheck2
		])
	}
	
	private void setUpCounterReceiptAndCounterReceiptItems() {
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForSalesDelivery1)
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForSalesDelivery2)
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForCustomerCharge1)
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForCustomerCharge2)
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForCreditMemo1)
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForCreditMemo2)
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForBouncedCheck1)
		sampleCounterReceipt.addToItems(sampleCounterReceiptItemForBouncedCheck2)
	}
	
	protected void tearDown() {
		super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove SalesDelivery
		remove CustomerCharge
		remove CreditMemo
		remove BouncedCheck
	}
	
	void testToString() {
		def id = sampleCounterReceipt.id
		
		def counterReceiptString = sampleCounterReceipt.toString()
		
		assertEquals "String should be CR-0000000" + id, "CR-0000000" + id, counterReceiptString
	}

    void testComputeDeliveryTotal() {
		assertCounterReceiptDeliveries()
		
		def total = sampleCounterReceipt.computeDeliveryTotal()
		
		assertEquals "Delivery Total should be 400", new BigDecimal("400"), total
    }
	
	void testComputeChargeTotal() {
		assertCounterReceiptCharges()
		
		def total = sampleCounterReceipt.computeChargeTotal()
		
		assertEquals "Charge Total should be 400", new BigDecimal("400"), total
	}
	
	void testComputeCreditMemoTotal() {
		assertCounterReceiptCreditMemos()
		
		def total = sampleCounterReceipt.computeCreditMemoTotal()
		
		assertEquals "Charge Total should be 400", new BigDecimal("400"), total
	}
	
	void testComputeBouncedCheckTotal() {
		assertCounterReceiptBouncedChecks()
		
		def total = sampleCounterReceipt.computeBouncedCheckTotal()
		
		assertEquals "Charge Total should be 400", new BigDecimal("400"), total
	}
	
	void testComputeDeliveryAmountPaidTotal() {
		def deliveryAmountPaidTotal = sampleCounterReceipt.computeDeliveryAmountPaidTotal()
		
		assertEquals "Delivery Amount Paid Total should be 200", new BigDecimal("200"), deliveryAmountPaidTotal
	}
	
	void testComputeChargeAmountPaidTotal() {
		def chargeAmountPaidTotal = sampleCounterReceipt.computeChargeAmountPaidTotal()
		
		assertEquals "Charge Amount Paid Total should be 200", new BigDecimal("200"), chargeAmountPaidTotal
	}
	
	void testComputeBouncedCheckAmountPaidTotal() {
		def bouncedCheckAmountPaidTotal = sampleCounterReceipt.computeBouncedCheckAmountPaidTotal()
		
		assertEquals "Bounced Check Amount Paid Total should be 200", new BigDecimal("200"), bouncedCheckAmountPaidTotal
	}

	void testComputeDeliveryAmountDueTotal() {
		assertCounterReceiptDeliveries()
		
		def deliveryAmountDueTotal = sampleCounterReceipt.computeDeliveryAmountDueTotal()
		
		assertEquals "Delivery Amount Due Total should be 200", new BigDecimal("200"), deliveryAmountDueTotal
	}
	
	void testComputeChargeAmountDueTotal() {
		assertCounterReceiptCharges()
				
		def chargeAmountDueTotal = sampleCounterReceipt.computeChargeAmountDueTotal()
		
		assertEquals "Charge Amount Due Total should be 200", new BigDecimal("200"), chargeAmountDueTotal
	}

	void testComputeCreditMemoAmountDueTotal() {
		assertCounterReceiptCreditMemos()
				
		def total = sampleCounterReceipt.computeCreditMemoAmountDueTotal()
		
		assertEquals "Charge Amount Due Total should be 400", new BigDecimal("400"), total
	}
	
	void testComputeBouncedCheckAmountDueTotal() {
		assertCounterReceiptBouncedChecks()
				
		def total = sampleCounterReceipt.computeBouncedCheckAmountDueTotal()
		
		assertEquals "Bounced Check Amount Due Total should be 200", new BigDecimal("200"), total
	}

	void testComputeTotal() {
		assertCounterReceiptDeliveries()
		assertCounterReceiptCharges()
		
		def total = sampleCounterReceipt.computeTotal()
		
		assertEquals "Total should be 800", new BigDecimal("800"), total
	}
	
	void testComputeAmountDueTotal() {
		assertCounterReceiptDeliveries()
		assertCounterReceiptCharges()
		
		def amountDueTotal = sampleCounterReceipt.computeAmountDueTotal()
		
		assertEquals "Amount Due Total should be 400", new BigDecimal("400"), amountDueTotal
	}
	
	void testComputeAmountPaidTotal() {
		assertCounterReceiptDeliveries()
		assertCounterReceiptCharges()
		assertCounterReceiptCreditMemos()
		assertCounterReceiptBouncedChecks()
		
		def amountPaidTotal = sampleCounterReceipt.computeAmountPaidTotal()
		
		assertEquals "Amount Paid Total should be 600", new BigDecimal("600"), amountPaidTotal
	}
	
	void testComputeInvoicesTotal() {
		assertCounterReceiptDeliveries()
		assertCounterReceiptCharges()
		assertCounterReceiptCreditMemos()
		assertCounterReceiptBouncedChecks()
		
		def total = sampleCounterReceipt.computeInvoicesTotal()
		
		assertEquals "Amount Paid Total should be 800", new BigDecimal("800"), total
	}
	
	void testComputeInvoicesAmountDueTotal() {
		assertCounterReceiptDeliveries()
		assertCounterReceiptCharges()
		assertCounterReceiptCreditMemos()
		assertCounterReceiptBouncedChecks()

		def total = sampleCounterReceipt.computeInvoicesAmountDueTotal()
		
		assertEquals "Amount Paid Total should be 1000", new BigDecimal("1000"), total
	}
	
	void testComputeOriginalAmountTotal() {
		assertCounterReceiptDeliveries()
		assertCounterReceiptCharges()
		assertCounterReceiptCreditMemos()
		assertCounterReceiptBouncedChecks()

		def total = sampleCounterReceipt.computeOriginalAmountTotal()
		
		assertEquals "Amount Total should be 1600", new BigDecimal("1600"), total
	}
	
	void testUnapprove() {
		assertEquals "[GUARD] Status should be Approved", "Approved", sampleCounterReceipt.status.toString()
		
		sampleCounterReceipt.unapprove()
		
		assertEquals "Status should be Unapproved", "Unapproved", sampleCounterReceipt.status.toString()
		assertTrue "Attached customer charge should be taken up", sampleCustomerCharge1.isTakenByCounterReceipt
	}
	
	void testCancel() {
		sampleCustomerCharge1.takenByCounterReceipt()
		assertEquals "[GUARD] Status should be Approved", "Approved", sampleCounterReceipt.status.toString()
		assertTrue "[GUARD] Attached customer charge should be taken by counter receipt", sampleCustomerCharge1.isTakenByCounterReceipt
		
		sampleCounterReceipt.cancel()
		
		assertEquals "Status should be Cancelled", "Cancelled", sampleCounterReceipt.status.toString()
		assertFalse "Attached customer charge should be freed up", sampleCustomerCharge1.isTakenByCounterReceipt
	}
	
	void testApprove() {
		sampleCounterReceipt.status = CounterReceipt.Status.UNAPPROVED
		sampleCustomerCharge1.takenByCounterReceipt()
		assertEquals "[GUARD] Status should be Unapproved", "Unapproved", sampleCounterReceipt.status.toString()
		assertTrue "[GUARD] Attached customer charge should be taken by counter receipt", sampleCustomerCharge1.isTakenByCounterReceipt
		
		sampleCounterReceipt.approve()
		
		assertEquals "Status should be Approved", "Approved", sampleCounterReceipt.status.toString()
		assertFalse "Attached customer charge should be freed up", sampleCustomerCharge1.isTakenByCounterReceipt
	}
	
	void testIsCancelledTrue() {
		sampleCounterReceipt.status = CounterReceipt.Status.CANCELLED
		assertEquals "[GUARD] Status should be Cancelled", "Cancelled", sampleCounterReceipt.status.toString()
		
		def isCancelled = sampleCounterReceipt.isCancelled()
		
		assertTrue "isCancelled should be true", isCancelled
	}

	void testIsCancelledFalse() {
		assertEquals "[GUARD] Status should be Approved", "Approved", sampleCounterReceipt.status.toString()
		
		def isCancelled = sampleCounterReceipt.isCancelled()
		
		assertFalse "isCancelled should be false", isCancelled
	}

	void testIsApprovedTrue() {
		assertEquals "[GUARD] Status should be Approved", "Approved", sampleCounterReceipt.status.toString()
		
		def isApproved = sampleCounterReceipt.isApproved()
		
		assertTrue "isApproved should be true", isApproved
	}
	
	void testIsApprovedFalse() {
		sampleCounterReceipt.status = CounterReceipt.Status.CANCELLED
		assertEquals "[GUARD] Status should be Cancelled", "Cancelled", sampleCounterReceipt.status.toString()
		
		def isApproved = sampleCounterReceipt.isApproved()
		
		assertFalse "isApproved should be false", isApproved
	}
	
	void testIsUnapprovedTrue() {
		sampleCounterReceipt.status = CounterReceipt.Status.UNAPPROVED
		assertEquals "[GUARD] Status should be Unapproved", "Unapproved", sampleCounterReceipt.status.toString()
		
		def isUnapproved = sampleCounterReceipt.isUnapproved()
		
		assertTrue "isUnapproved should be true", isUnapproved
	}
	
	void testIsUnapprovedFalse() {
		assertEquals "[GUARD] Status should be Approved", "Approved", sampleCounterReceipt.status.toString()
		
		def isUnapproved = sampleCounterReceipt.isUnapproved()
		
		assertFalse "isUnapproved should be false", isUnapproved
	}
	
	void testGetDeliveries() {
		assertTrue "should have sample delivery 1", sampleCounterReceipt.deliveries.contains(sampleSalesDelivery1)
		assertTrue "should have sample delivery 2", sampleCounterReceipt.deliveries.contains(sampleSalesDelivery2)
		assertEquals "should have 2 entries", 2, sampleCounterReceipt.deliveries.size()
	}
	
	void testGetDeliveriesNoItems() {
		sampleCounterReceipt.items = null
		
		assertEquals "should have no entries", 0, sampleCounterReceipt.deliveries.size()
	}

	void testGetCharges() {
		assertTrue "Counter Receipt should have sample customer charge 1", sampleCounterReceipt.charges.contains(sampleCustomerCharge1)
		assertTrue "Counter Receipt should have sample customer charge 2", sampleCounterReceipt.charges.contains(sampleCustomerCharge2)
		assertEquals "should have 2 entries", 2, sampleCounterReceipt.charges.size()
	}
	
	void testGetCreditMemos() {
		assertTrue "Counter Receipt should have sample credit memo 1", sampleCounterReceipt.creditMemos.contains(sampleCreditMemo1)
		assertTrue "Counter Receipt should have sample credit memo 2", sampleCounterReceipt.creditMemos.contains(sampleCreditMemo2)
		assertEquals "should have 2 entries", 2, sampleCounterReceipt.creditMemos.size()
	}
	
	void testGetBouncedChecks() {
		assertTrue "Counter Receipt should have sample bounced check 1", sampleCounterReceipt.bouncedChecks.contains(sampleBouncedCheck1)
		assertTrue "Counter Receipt should have sample bounced check 2", sampleCounterReceipt.bouncedChecks.contains(sampleBouncedCheck2)
		assertEquals "should have 2 entries", 2, sampleCounterReceipt.bouncedChecks.size()
	}

	private void assertCounterReceiptCharges() {
		assertEquals "[GUARD] Counter Receipt should have 2 items with customer charge", 2, sampleCounterReceipt.deliveries.size()
	}
	
	private void assertCounterReceiptDeliveries() {
		assertEquals "[GUARD] Counter Receipt should have 2 items with delivery", 2, sampleCounterReceipt.charges.size()
	}
	
	private void assertCounterReceiptCreditMemos() {
		assertEquals "[GUARD] Counter Receipt should have 2 items with credit memo", 2, sampleCounterReceipt.creditMemos.size()
	}
	
	private void assertCounterReceiptBouncedChecks() {
		assertEquals "[GUARD] Counter Receipt should have 2 items with credit memo", 2, sampleCounterReceipt.bouncedChecks.size()
	}
}
