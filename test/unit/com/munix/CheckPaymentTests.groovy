package com.munix

import grails.test.*

class CheckPaymentTests extends GrailsUnitTestCase {
	def generalMethodService = new GeneralMethodService()
	
	def sampleCheckPayment
	def sampleCheckDeposit1
	def sampleCheckDeposit2
	def sampleCheckDeposit3
	def sampleBouncedCheck1
	def sampleBouncedCheck2
	def sampleBouncedCheck3

	
    protected void setUp() {
        super.setUp()
		
		sampleCheckPayment = new CheckPayment()
		mockDomain(CheckPayment, [sampleCheckPayment])
		
		sampleCheckDeposit1 = new CheckDeposit(
			depositDate: generalMethodService.createDate("01/30/2001") 
		)
		sampleCheckDeposit2 = new CheckDeposit(
			depositDate: generalMethodService.createDate("02/11/2001")
		)
		sampleCheckDeposit3 = new CheckDeposit(
			depositDate: generalMethodService.createDate("04/23/2001")
		)
		mockDomain(CheckDeposit, [sampleCheckDeposit1, sampleCheckDeposit2, sampleCheckDeposit3])
		
		sampleBouncedCheck1 = new BouncedCheck(
			date: generalMethodService.createDate("02/21/2001")
		)
		sampleBouncedCheck2 = new BouncedCheck(
			date: generalMethodService.createDate("04/12/2001")
		)
		sampleBouncedCheck3 = new BouncedCheck(
			date: generalMethodService.createDate("01/15/2002")
		)
		mockDomain(BouncedCheck, [sampleBouncedCheck1, sampleBouncedCheck2, sampleBouncedCheck3])		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetCurrentCheckDeposit() {
    	sampleCheckPayment.checkDeposits = [sampleCheckDeposit1, sampleCheckDeposit2, sampleCheckDeposit3]

		def result = sampleCheckPayment.retrieveCurrentCheckDeposit()
		
		assertEquals "wrong current check deposit", sampleCheckDeposit3, result 
    }

    void testGetCurrentCheckDepositNoCheckDeposits() {
		def result = sampleCheckPayment.retrieveCurrentCheckDeposit()
		
		assertNull "wrong current check deposit", null 
	}
	
	void testRetrieveCurrentBouncedCheck() {
		sampleCheckPayment.bouncedChecks = [sampleBouncedCheck1, sampleBouncedCheck2, sampleBouncedCheck3]

		def result = sampleCheckPayment.retrieveCurrentBouncedCheck()
		
		assertEquals "wrong current bounced check", sampleBouncedCheck3, result
	}

	void testRetrieveCurrentBouncedCheckNoBouncedChecks() {
		def result = sampleCheckPayment.retrieveCurrentBouncedCheck()
		
		assertNull "wrong current bounced check", null
	}
	
	void testIsDeposited() {
		sampleCheckPayment.status = CheckPayment.Status.DEPOSITED
		
		assertTrue "check should be deposited", sampleCheckPayment.isDeposited()
	}

}
