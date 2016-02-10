package com.munix

import grails.test.*

class WaybillControllerTests extends ControllerUnitTestCase {

	def sampleWaybill
	
	protected void setUp() {
		super.setUp()
		
		sampleWaybill = new Waybill()
		mockDomain(Waybill, [sampleWaybill])
	}

	protected void tearDown() {
		super.tearDown()
	}
		
	void testCancel() {
		mockParams.id = sampleWaybill.id
		
		controller.cancel()
		
		assertEquals "Waybill status should be cancelled", "Cancelled", sampleWaybill.status
	}
	
	void testList() {
		def mockWaybillService = mockFor(WaybillService)
		mockWaybillService.demand.generateList(1..1){x->
			return [waybillInstanceList:"1",totalCount:"1"]
		}
		mockWaybillService.demand.generateDateStructList(1..1){x->
			return [:]
		}
		controller.waybillService= mockWaybillService.createMock()
		def result = controller.list()
		assertEquals "Should have 1 result", '1', result.waybillInstanceTotal
		assertEquals "Should contain a waybill", '1', result.waybillInstanceList.waybillInstanceList
	}
	
}
