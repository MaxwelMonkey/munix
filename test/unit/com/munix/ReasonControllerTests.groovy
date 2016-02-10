package com.munix

import grails.test.*

class ReasonControllerTests extends ControllerUnitTestCase {
	def sampleReason
	
    protected void setUp() {
        super.setUp()
		controller.metaClass.message = {Map map ->
			return map.code
		}
		sampleReason = new Reason(
			identifier: "reason",
			description: "reason"
			)
		mockDomain(Reason,[sampleReason])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSaveSuccess() {
		mockDomain(Reason)
		mockParams.identifier = "reason"
		mockParams.description = "reason"
		mockParams.id = sampleReason.id
		controller.save()
		assertEquals "message must be create", "default.created.message", controller.flash.message
		assertEquals "redirect must be show", "show", redirectArgs.action
    }
	
	void testSaveNotSuccessful() {
		mockDomain(Reason)
		controller.save()
		assertEquals "render must be create", "create", controller.renderArgs.view
	}
	
	void testShowWithoutId(){
		mockDomain(Reason)
		controller.show()
		assertEquals "wrong redirection", "list", controller.redirectArgs.action
	}
	
	void testEditWithoutId(){
		mockDomain(Reason)
		controller.show()
		assertEquals "wrong redirection", "list", controller.redirectArgs.action
	}
}
