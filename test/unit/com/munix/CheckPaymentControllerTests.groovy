package com.munix


import grails.test.ControllerUnitTestCase

class CheckPaymentControllerTests extends ControllerUnitTestCase {

    void testList() {
        def mockCheckPaymentService = mockFor(CheckPaymentService)
		mockCheckPaymentService.demand.getCheckPaymentListWithCriteria(1..1){x->
			return [checkPaymentInstanceList:"1",totalCount:"2"]
		}
		mockCheckPaymentService.demand.generateDateStructList(1..1){x->
			return [:]
		}
		controller.checkPaymentService= mockCheckPaymentService.createMock()
		def result = controller.list()
		assertEquals "The checkPaymentInstanceList must be 1",'1',result.checkPaymentInstanceList.checkPaymentInstanceList
		assertEquals "The totalCount must be 2",'2',result.checkPaymentInstanceList.totalCount
    }

}
