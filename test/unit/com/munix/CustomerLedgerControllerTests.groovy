package com.munix

import grails.test.*

class CustomerLedgerControllerTests extends ControllerUnitTestCase {
	def sampleCustomerLedger
	
    protected void setUp() {
        super.setUp()
		
		sampleCustomerLedger = new CustomerLedger()
		mockDomain(CustomerLedger, [sampleCustomerLedger])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testShowWithId() {
		mockParams.id = sampleCustomerLedger.id
		def mockCustomerLedgerService = mockFor(CustomerLedgerService)
		mockCustomerLedgerService.demand.getCustomerLedgerEntries(1..1) { x,y ->
			println "here"
			return true
		}
		controller.customerLedgerService = mockCustomerLedgerService.createMock()

		def result = controller.show()
		
		assertEquals("Controller should return something.", true, result)
	}

    void testShowWithoutId() {
		mockParams.id = 2
		
		controller.show()

        assertEquals("Should render list page", "list", controller.redirectArgs.action)
		assertEquals("customerLedger.not.found", controller.flash.message)
		assertEquals("CustomerLedger not found with id 2", controller.flash.defaultMessage)
	}
}
