package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class DirectDeliveryControllerTests extends ControllerUnitTestCase {

	def sampleDirectDelivery
	def sampleCustomer
	def sampleTripTicketDeliveryItem
	
	def mockAuthenticateService
	
	protected void setUp() {
		super.setUp()
		controller.metaClass.message={Map map-> map}
		
		sampleDirectDelivery = new DirectDelivery()
		mockDomain(DirectDelivery, [sampleDirectDelivery])
		
		sampleCustomer = new Customer()
		mockDomain(Customer, [sampleCustomer])
		
		sampleTripTicketDeliveryItem = new TripTicketDeliveryItem()
		mockDomain(TripTicketDeliveryItem, [sampleTripTicketDeliveryItem])
		
		mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1) {->
			return new User(userRealName: "user")
		}
	}

	protected void tearDown() {
		super.tearDown()
	}
		
	void testSaveWithoutErrors() {
		mockParams."customer.id" = sampleCustomer.id
		controller.authenticateService = mockAuthenticateService.createMock()
		def oldSize = DirectDelivery.count()
		
		controller.save()
		
		assertEquals "New direct delivery should have been created", oldSize + 1, DirectDelivery.count()
		assertEquals "Should redirect to show", "show", controller.redirectArgs.action
	}
	
	void testSaveWithErrors() {
		controller.authenticateService = mockAuthenticateService.createMock()
		def oldSize = DirectDelivery.count()

		controller.save()
		
		assertEquals "New direct delivery should not be created", oldSize, DirectDelivery.count()
		assertEquals "Should return to create", "create", controller.renderArgs.view
	}
	
	void testCancel() {
		mockParams.id = sampleDirectDelivery.id
		
		controller.cancel()
		
		assertEquals "Direct Delivery status should be cancelled", DirectDelivery.Status.CANCELLED, sampleDirectDelivery.status
	}
	
	void testIsCancelable() {
		mockParams.id = sampleDirectDelivery.id
		
		controller.cancel()
		
		assertTrue "Sample direct delivery should be cancelable", sampleDirectDelivery.isCancelable()
	}
	
	void testIsCancelableNotCancelable() {
		mockParams.id = sampleDirectDelivery.id
		
		sampleDirectDelivery.tripTicket = sampleTripTicketDeliveryItem
		sampleDirectDelivery.save()
		
		controller.cancel()
		
		assertFalse "Sample direct delivery should not be cancelable", sampleDirectDelivery.isCancelable()
	}
}
