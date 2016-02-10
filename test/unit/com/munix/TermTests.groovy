package com.munix

import grails.test.*

class TermTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

	public static Term createSampleTerm() {
		return new Term(
			identifier: "term",
			description: "termDesc",
			dayValue: new BigDecimal("1234"))
	}
	
    void testSomething() {

    }
}
