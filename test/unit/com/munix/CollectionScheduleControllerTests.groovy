package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class CollectionScheduleControllerTests extends ControllerUnitTestCase {
	def sampleCollectionSchedule
	def mockAuthenticateService
	
    protected void setUp() {
        super.setUp()
		controller.metaClass.message = {Map map ->
			return map.code
		}

		sampleCollectionSchedule = new CollectionSchedule()
		mockDomain(CollectionSchedule, [sampleCollectionSchedule])
		
		mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1){ ->
			return new User(username : "Tester")
		}
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testMarkAsComplete() {
		mockParams.id = sampleCollectionSchedule.id
		controller.authenticateService = mockAuthenticateService.createMock()
		
		controller.markAsComplete()
		
		assertEquals "should have correct status", CollectionSchedule.Status.COMPLETE, sampleCollectionSchedule.status 
		assertTrue "should have closed By", !sampleCollectionSchedule.closedBy.isEmpty()
	}
	
	void testMarkAsCompleteFail() {
		mockParams.id = sampleCollectionSchedule.id
		controller.authenticateService = mockAuthenticateService.createMock()
		sampleCollectionSchedule.preparedBy = ""
		
		controller.markAsComplete()
		
		assertEquals "should redirect to correct page", "show", redirectArgs.action 
	}

	void testMarkAsCompleteNoId() {
		controller.markAsComplete()
		
		assertEquals "should redirect to correct page", "list", redirectArgs.action 
	}

	void testUnmarkAsComplete() {
		mockParams.id = sampleCollectionSchedule.id
		sampleCollectionSchedule.status = CollectionSchedule.Status.COMPLETE
		sampleCollectionSchedule.closedBy = "me"
		
		controller.unmarkAsComplete()
		
		assertEquals "should have correct status", CollectionSchedule.Status.PROCESSING, sampleCollectionSchedule.status 
		assertTrue "should have no closed By", sampleCollectionSchedule.closedBy.isEmpty()
	}
	
	void testUnmarkAsCompleteFail() {
		mockParams.id = sampleCollectionSchedule.id
		sampleCollectionSchedule.preparedBy = ""
		
		controller.unmarkAsComplete()
		
		assertEquals "should redirect to correct page", "show", redirectArgs.action 
	}

	void testUnmarkAsCompleteNoId() {
		controller.unmarkAsComplete()
		
		assertEquals "should redirect to correct page", "list", redirectArgs.action 
	}

    void testPrint() {
        controller.doPrint()
        assertEquals "show",redirectArgs.action
    }
}
