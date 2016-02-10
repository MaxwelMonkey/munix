package com.munix

import grails.test.*

class CheckWarehouseTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	public static CheckWarehouse createSampleCheckWarehouse() {
		return new CheckWarehouse(
			identifier:"1234",
			description:"1234",
			isDefault:true)
	}
	
    void testSomething() {

    }
}
