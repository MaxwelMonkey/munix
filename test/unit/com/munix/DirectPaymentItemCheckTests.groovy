package com.munix

import grails.test.*

class DirectPaymentItemCheckTests extends GrailsUnitTestCase {
	def sampleDirectPaymentItemCheck
	def sampleCheckPayment
	def sampleCheckDeposit
	
    protected void setUp() {
        super.setUp()
		
		sampleCheckPayment = new CheckPayment()
		mockDomain(CheckPayment, [sampleCheckPayment])
		
		sampleDirectPaymentItemCheck = new DirectPaymentItemCheck(
			checkPayment: sampleCheckPayment
		)
		mockDomain(DirectPaymentItemCheck, [sampleDirectPaymentItemCheck])
		
		sampleCheckDeposit = new CheckDeposit()
		mockDomain(CheckDeposit, [sampleCheckDeposit])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIsNotUnapprovable() {
		def result = sampleDirectPaymentItemCheck.isNotUnapprovable()
		
		assertNull "should be unapprovable", result
    }
	
	void testIsNotUnapprovableNotApprovable() {
		sampleCheckPayment.addToCheckDeposits(sampleCheckDeposit)
		def result = sampleDirectPaymentItemCheck.isNotUnapprovable()
		
		assertNotNull "should not be unapprovable", result
	}
	
	void testIsNotUnapprovableApprovableBecauseCancelled() {
		sampleCheckDeposit.status = "Cancelled"
		sampleCheckPayment.addToCheckDeposits(sampleCheckDeposit)
		def result = sampleDirectPaymentItemCheck.isNotUnapprovable()
		
		assertNull "should be unapprovable", result
	}
}
