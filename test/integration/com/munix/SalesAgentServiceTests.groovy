package com.munix


import grails.test.*

class SalesAgentServiceTests extends GroovyTestCase {
    def salesAgentService
    def sampleSalesAgent
	
    private setupSalesAgent(){
        sampleSalesAgent = new SalesAgent(
            identifier: "Sample SalesAgent",
            lastName: "Sample",
   		    firstName: "SalesAgent"
        )
        sampleSalesAgent.save(flush:true)
    }
	
    protected void setUp() {
        super.setUp()
		
		setupSalesAgent()

    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testGenerateList() {
		def params = [:]
		params.searchIdentifier = "Sample SalesAgent"
		
		def result = salesAgentService.generateList(params)
		
		assertTrue "Result should contain sampleSalesAgent", result.contains(sampleSalesAgent)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListNoResults() {
		def params = [:]
		params.searchIdentifier = "blank"
		
		def result = salesAgentService.generateList(params)
		
		assertTrue "Result must be empty", result.isEmpty()
	}
}
