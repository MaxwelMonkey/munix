package com.munix

import grails.test.*

class SalesAgentControllerTests extends ControllerUnitTestCase {

	def sampleSalesAgent
	
	protected void setUp() {
		super.setUp()
		
		sampleSalesAgent = new SalesAgent()
		mockDomain(SalesAgent, [sampleSalesAgent])
	}

	protected void tearDown() {
		super.tearDown()
	}
	
	void testList() {
		def mockSalesAgentService = mockFor(SalesAgentService)
		mockSalesAgentService.demand.generateList(1..1){x->
			return [salesAgentInstanceList:"1",totalCount:"1"]
		}
		controller.salesAgentService= mockSalesAgentService.createMock()
		def result = controller.list()
		assertEquals "Should have 1 result", '1', result.salesAgentInstanceTotal
		assertEquals "Should contain a Sales Agent", '1', result.salesAgentInstanceList.salesAgentInstanceList
	}
}
