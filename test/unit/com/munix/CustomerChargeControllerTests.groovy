package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class CustomerChargeControllerTests extends ControllerUnitTestCase {

	def sampleCustomerCharge
	def sampleCustomerChargeItem
	def sampleDirectPaymentInvoice
	
	protected void setUp() {
		super.setUp()
		
		sampleCustomerCharge = new CustomerCharge()
		mockDomain(CustomerCharge, [sampleCustomerCharge])

		sampleCustomerChargeItem = new CustomerChargeItem()
		mockDomain(CustomerChargeItem, [sampleCustomerChargeItem])
				
		sampleCustomerCharge.items = [sampleCustomerChargeItem]
		
		sampleDirectPaymentInvoice = new DirectPaymentInvoice()
		mockDomain(DirectPaymentInvoice, [sampleDirectPaymentInvoice])
	}

	protected void tearDown() {
		super.tearDown()
	}
	
	void testApprove() {
		mockParams.id = sampleCustomerCharge.id
		def mockCustomerChargeService = mockFor(CustomerChargeService)
		mockCustomerChargeService.demand.approveCustomerCharge(1..1) {x ->
			return true
		}
		controller.customerChargeService = mockCustomerChargeService.createMock()
		controller.approve()
		
		assertEquals "Should redirect to show view", "show", redirectArgs.action
        assertEquals "Flash message must be Customer Charge has been successfully approved!","Customer Charge has been successfully approved!",controller.flash.message
	}
	
	void testApproveFailure() {
		mockParams.id = sampleCustomerCharge.id
		sampleCustomerCharge.items = []
		
		controller.approve()
		
		assertEquals "Sample customer charge status should be Unapproved", "Unapproved", sampleCustomerCharge.status
		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}
	
	void testUnapprove() {
		mockParams.id = sampleCustomerCharge.id
		def mockCustomerChargeService = mockFor(CustomerChargeService)
		mockCustomerChargeService.demand.unapproveCustomerCharge(1..1) {x ->
			return true
		}
		controller.customerChargeService = mockCustomerChargeService.createMock()

		controller.unapprove()

		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}
	
	void testIsUnapprovable() {
		mockParams.id = sampleCustomerCharge.id
		def mockCustomerChargeService = mockFor(CustomerChargeService)
		mockCustomerChargeService.demand.unapproveCustomerCharge(1..1) {x ->
			return true
		}
		controller.customerChargeService = mockCustomerChargeService.createMock()

		controller.unapprove()
		assertTrue "Customer Charge should be unapprovable", sampleCustomerCharge.isUnapprovable()
	}
	
	void testIsUnapprovableNotUnapprovable() {
		mockParams.id = sampleCustomerCharge.id
		sampleCustomerCharge.addToInvoices(sampleDirectPaymentInvoice)
		def mockCustomerChargeService = mockFor(CustomerChargeService)
		mockCustomerChargeService.demand.unapproveCustomerCharge(1..1) {x ->
			return true
		}
		controller.customerChargeService = mockCustomerChargeService.createMock()
		
		controller.unapprove()
		assertFalse "Customer Charge should not be unapprovable", sampleCustomerCharge.isUnapprovable()
	}

}
