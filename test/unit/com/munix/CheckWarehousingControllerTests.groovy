package com.munix

import grails.test.ControllerUnitTestCase
import org.grails.plugins.springsecurity.service.AuthenticateService

class CheckWarehousingControllerTests extends ControllerUnitTestCase {
    def sampleUser
    def sampleCheckWarehousing
    def sampleCheckWarehouse
	def sampleCheckPayment
	
    protected void setUp() {
		super.setUp()
        mockDomain(CheckWarehousing)
        controller.metaClass.message = {Map map ->
            return map.code
        }
        sampleUser = new User(userRealName:"haha")
        mockDomain(User,[sampleUser])
        sampleCheckWarehouse = new CheckWarehouse()
        mockDomain(CheckWarehouse, [sampleCheckWarehouse])
		
		sampleCheckPayment = new CheckPayment()
		mockDomain(CheckPayment, [sampleCheckPayment])
	}
	
    protected void tearDown() {
        super.tearDown()
    }

    void testSaveSuccess() {
        def authenticateService = mockFor(AuthenticateService)
		authenticateService.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = authenticateService.createMock()
        mockParams.originWarehouse=sampleCheckWarehouse
        mockParams.destinationWarehouse=sampleCheckWarehouse
		mockParams.checks = [sampleCheckPayment.id]
		
        controller.save()
		
        assertEquals "message must be create", "default.created.message", controller.flash.message
        assertEquals "redirect must be show", "show", redirectArgs.action
    }
    void testSavePreparedByString() {
        def authenticateService = mockFor(AuthenticateService)
		authenticateService.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = authenticateService.createMock()

        mockParams.originWarehouse=sampleCheckWarehouse
        mockParams.destinationWarehouse=sampleCheckWarehouse
        controller.save()
        def checkWarehousingList = CheckWarehousing.list()
        assertEquals "Checkwarehousing list must only contain 1 object", 1, checkWarehousingList.size()
        checkWarehousingList.each{
            assertEquals "preparedby must be the string specified", "haha, "+new Date().format("MMM. dd, yyyy - hh:mm a"), it.preparedBy
        }

    }

    void testSaveNotSuccessful() {
        def authenticateService = mockFor(AuthenticateService)
		authenticateService.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = authenticateService.createMock()
        controller.save()
        assertEquals "render must be create", "create", controller.renderArgs.view
    }

    void testCreate(){
        def mockCheckWarehousingService = mockFor(CheckWarehousingService)
		mockCheckWarehousingService.demand.getCheckPaymentsWithCriteria(1..1) { x,y->
			return []
		}
		controller.checkWarehousingService = mockCheckWarehousingService.createMock()
        mockParams."originWarehouse.id" = sampleCheckWarehouse.id
        mockParams.originWarehouse = sampleCheckWarehouse
        def result = controller.create()
        assertTrue "checkPayments must be empty", result.checkPayments.isEmpty()
        assertEquals "origin warehouse must be the originWarehouse",sampleCheckWarehouse, result.originWarehouse
    }
    void testEditNoId(){
        controller.metaClass.message={x-> x.code}
        controller.edit()
        assertEquals "must redirect to list", "list", redirectArgs.action
        assertEquals "code must be default.not.found.message", "default.not.found.message", controller.flash.message
    }
    void testEditWithId(){
        sampleCheckWarehousing = new CheckWarehousing()
        mockDomain(CheckWarehousing, [sampleCheckWarehousing])
        mockParams.id = sampleCheckWarehousing.id
        def result = controller.edit()
        assertEquals "must be sampleCheckWarehousing", sampleCheckWarehousing, result.checkWarehousingInstance
        assertTrue "checkPayments must be empty", result.checkPayments.contains(sampleCheckPayment)
    }
}
