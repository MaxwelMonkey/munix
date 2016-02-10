package com.munix

import grails.test.*

class SalesAgentTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	public static SalesAgent createSampleSalesAgent() {
		return new SalesAgent(identifier:"salesAgent",
			lastName:"last",
			firstName:"first",
			street:"street",
			city:CityTests.createSampleCity(),
			zip:"1234",
			mobile:"1233",
			landline:"12345",
			skype:"nothing.com",
			yahoo:"1@yahoo.com",
			email:"something@samthing.com")
	}
	
    void testSomething() {

    }
}
