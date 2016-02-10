package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.*

class PrintControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
    
//    void testCreditMemo() {
//    	def mockAuthenticateService = mockFor(AuthenticateService)
//		mockAuthenticateService.demand.userDomain(1..1) { ->
//			new User(userRealName: "realName")
//		}
//    	controller.authenticateService = mockAuthenticateService.createMock()
//    	mockDomain(CreditMemo)
//    	mockDomain(PrintLogCreditMemo)
//        mockParams.id = 1
//    	controller.creditMemo()
//    }

    
    void testJobOrder() {
    	mockDomain(JobOrder)
        mockParams.id = 1
        controller.formatDate = { return "" }
    	controller.jobOrder()
    }

}
