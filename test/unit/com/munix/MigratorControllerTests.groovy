package com.munix

import grails.test.*

class MigratorControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCustomerLedger() {
		def mockMigratorService = mockFor(MigratorService)
		mockMigratorService.demand.generateCustomerLedgers(1..1) { ->
			return null
		}
		controller.migratorService = mockMigratorService.createMock()
		
		controller.customerLedger()
    }
}
