package com.munix

import grails.test.*

class CheckTypeTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	public static CheckType createSampleCheckType() {
		return new CheckType( 
				routingNumber:"1234",
				description:"idontknow",
				branch:"1345")
	}

    void testSomething() {

    }
}
