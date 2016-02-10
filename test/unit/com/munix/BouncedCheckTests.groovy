package com.munix

import grails.test.*

class BouncedCheckTests extends GrailsUnitTestCase {
    def sampleCheckPayment1
	def sampleCheckPayment2
	def sampleCheckDeposit
	def sampleCheckDeposit2
	def sampleBouncedCheck
	def sampleCounterReceipt
	def sampleCounterReceiptItem
	
	protected void setUp() {
        super.setUp()
		setUpCheckPayments()
		setUpCheckDeposits()
		setUpCheckPaymentAndCheckPaymentRelationship()
		setUpBouncedCheck()
		setUpBouncedCheckAndCheckPaymentRelationship()
		setUpCounterReceipts()
		setUpCounterReceiptItems()
    }

	public static BouncedCheck createSampleBouncedCheck() {
		return new BouncedCheck(
			date:new Date(),
			status:"Approved",
			customer:CustomerTests.createSampleCustomer(),
			preparedBy:"me",
			bouncedCheckStatus: CheckStatusTests.createSampleCheckStatus(),
			forRedeposit: false)
	}
	
    protected void tearDown() {
        super.tearDown()
    }

    void testComputeTotalAmount() {
		def totalAmount = sampleBouncedCheck.computeTotalAmount()
		
		assertEquals "Should have total amount of 44.67", 44.67, totalAmount
    }
	
	void testComputeProjectedDue() {
		def totalDue = sampleBouncedCheck.computeProjectedDue()
		
		assertEquals "Should have total due of 44.67", 44.67, totalDue
	}
	
	void testGetCounterReceipts() {
		assertEquals "wrong list of counter receipts", [sampleCounterReceipt], sampleBouncedCheck.counterReceipts
	}
	
	void testIsUnapprovable() {
		assertTrue "bounced check should be unapprovable", sampleBouncedCheck.isUnapprovable()
	}

	void testIsUnapprovableTakenByDirectPayment() {
		sampleBouncedCheck.isTakenByDirectPayment = true
		
		assertFalse "bounced check should not be unapprovable", sampleBouncedCheck.isUnapprovable()
	}
	
	void testIsUnapprovableTakenByCounterReceipt() {
		sampleBouncedCheck.isTakenByCounterReceipt = true
		
		assertFalse "bounced check should not be unapprovable", sampleBouncedCheck.isUnapprovable()
	}

	void testIsUnapprovableCheckPaymentTakenByNewCheckDeposit() {
		sampleBouncedCheck.forRedeposit = true
		sampleCheckDeposit.addToChecks(sampleCheckPayment2)
		sampleCheckPayment2.addToCheckDeposits(sampleCheckDeposit)
		
		assertFalse "bounced check should not be unapprovable", sampleBouncedCheck.isUnapprovable()
	}

	private void setUpBouncedCheckAndCheckPaymentRelationship() {
		sampleBouncedCheck.checks = [sampleCheckPayment1, sampleCheckPayment2]
		sampleCheckPayment1.addToBouncedChecks(sampleBouncedCheck)
		sampleCheckPayment2.addToBouncedChecks(sampleBouncedCheck)
	}

	private void setUpBouncedCheck() {
		sampleBouncedCheck = createSampleBouncedCheck()
		mockDomain(BouncedCheck, [sampleBouncedCheck])
	}

	private void setUpCheckPayments() {
		sampleCheckPayment1 = new CheckPayment(
			amount: new BigDecimal("10.52")
		)
		
		sampleCheckPayment2 = new CheckPayment(
			amount: new BigDecimal("34.15")
		)
		mockDomain(CheckPayment, [sampleCheckPayment1, sampleCheckPayment2])
	}
	
	private void setUpCheckDeposits() {
		sampleCheckDeposit = new CheckDeposit()
		sampleCheckDeposit2 = new CheckDeposit()
		mockDomain(CheckDeposit, [sampleCheckDeposit, sampleCheckDeposit2])
	}
	
	private void setUpCheckPaymentAndCheckPaymentRelationship() {
		sampleCheckDeposit.checks = [sampleCheckPayment1]
        sampleCheckPayment1.addToCheckDeposits(sampleCheckDeposit)
		sampleCheckDeposit2.checks = [sampleCheckPayment2]
		sampleCheckPayment2.addToCheckDeposits(sampleCheckDeposit2)
	}
	
	private void setUpCounterReceipts() {
		sampleCounterReceipt = new CounterReceipt()
		mockDomain(CounterReceipt, [sampleCounterReceipt])
	}
	
	private void setUpCounterReceiptItems() {
		sampleCounterReceiptItem = new CounterReceiptItem(
			counterReceipt: sampleCounterReceipt,
			invoice: sampleBouncedCheck
		)
		mockDomain(CounterReceiptItem, [sampleCounterReceiptItem])
	}
}
