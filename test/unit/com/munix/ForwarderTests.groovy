package com.munix

import grails.test.*

class ForwarderTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	public static Forwarder createSampleForwarder() {
		return new Forwarder(identifier:"forward",
				description:"descForw",
				street:"nothing",
				city:CityTests.createSampleCity(),
				zip:"1234",
				landline:"1234",
				contact:"1234",
				contactPosition:"you")
	}
	
    void testSomething() {

    }
}
