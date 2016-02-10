package com.munix

import grails.test.*

class BankTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	public static Bank createSampleBank() {
		return new Bank(identifier: "bank", description: "description")
	}
	
    void testSomething() {

    }
}
