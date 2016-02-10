package com.munix

import grails.test.*

class ForwarderServiceTests extends GrailsUnitTestCase {
	def forwarderService
	
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerateList() {
		def forwarder = new Forwarder(
			identifier: "forwarder",
			description: "forwarder desc",
			street: "super street"
		)
		forwarder.save(flush: true)
		
		def params = [:]
		params.searchIdentifier = "forwarder"
												
		def result = forwarderService.generateList(params)
		
		assertTrue "Result should contain forwarder", result.contains(forwarder)
		assertEquals "Result total should be 1", 1, result.size()
    }
}
