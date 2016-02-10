package com.munix

import grails.test.*

class CityTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	public static City createSampleCity() {
		return new City(identifier:"city", description:"cityDesc")
	}
	
    void testSomething() {

    }
}
