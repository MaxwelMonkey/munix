package com.munix;

import grails.test.*;

class CheckStatusTests extends GrailsUnitTestCase {
	protected void setUp() {
		super.setUp()
	}

	public static CheckStatus createSampleCheckStatus() {
		return new CheckStatus(
			identifier: "identifier",
			description: "description")
	}
	
	protected void tearDown() {
		super.tearDown()
	}

	void testSomething() {

	}
}
