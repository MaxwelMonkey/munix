package com.munix

import grails.test.*

class CustomerTypeTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	public static CustomerType createSampleCustomerType() {
		return new CustomerType(identifier:"customerType", description:"descCustomerType")
	}

    void testSomething() {

    }
}
