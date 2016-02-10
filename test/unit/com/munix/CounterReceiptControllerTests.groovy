package com.munix

import grails.test.*

class CounterReceiptControllerTests extends ControllerUnitTestCase {
    def sampleCounterReceipt
    def sampleSalesDelivery
    def sampleCustomerCharge
    def sampleUser
    def sampleCustomer
    protected void setUp() {
        super.setUp()
        sampleCustomer= new Customer(id:1L)
        sampleUser = new User(username:"user")
		mockDomain(User, [sampleUser])
        sampleCounterReceipt = new CounterReceipt()
        mockDomain(CounterReceipt, [sampleCounterReceipt])
        sampleSalesDelivery = new SalesDelivery()
        sampleCustomerCharge = new CustomerCharge()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDoPrint() {
        def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
        controller.params.id = sampleCounterReceipt.id
		
        controller.doPrint()
		
        assertEquals "counterReceipt",redirectArgs.action
    }
	
    void testApproveWithId(){
        def mockService = mockFor(CounterReceiptService)
		mockService.demand.validateAndApproveCounterReceipt(1..1){ x->
			return [result:true,message:"Counter Receipt has been successfully approved!"]
		}
		controller.counterReceiptService= mockService.createMock()
        controller.params.id = sampleCounterReceipt.id
        controller.approve()
        assertEquals "flash message is wrong", "Counter Receipt has been successfully approved!", controller.flash.message
        assertEquals "show", redirectArgs.action
    }
    void testApproveWithIdNotValid(){
        def mockService = mockFor(CounterReceiptService)
		mockService.demand.validateAndApproveCounterReceipt(1..1){ x->
			return [result:false,message:"Error"]
		}
		controller.counterReceiptService= mockService.createMock()
        controller.params.id = sampleCounterReceipt.id
        controller.approve()
        assertEquals "flash error is wrong", "Error", controller.flash.error
        assertEquals "show", redirectArgs.action
    }
    void testCreateWithParamsId(){
        mockDomain(Customer, [sampleCustomer])
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.getAvailableCustomerPaymentsForCounterReceipt(1..1){x->
			return [deliveries:[1],charges:[2]]
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.params.id = sampleCustomer.id
        def results = controller.create()
        assertEquals "Deliveries must contain 1",1, results.deliveries[0]
        assertEquals "Charges must contain 2",2, results.charges[0]
    }
    void testCreateWithOutParamsId(){
        controller.params.preparedBy = "me"
        def results = controller.create()
        assertEquals "The counter receipt instance must have the field preparedby 'me'","me", results.counterReceiptInstance.preparedBy
    }
    void testSaveWithoutErrors(){
        def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.saveCounterReceipt(1..1){x,y->
			return true
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.metaClass.message = {Map map ->
            return map.code
        }
        controller.save()

        assertEquals "default.created.message",controller.flash.message
        assertEquals "show", redirectArgs.action
    }
    void testSaveWithErrors(){
        def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.saveCounterReceipt(1..1){x,y->
			return false
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.save()
        assertEquals "wrong action redirection", "create", controller.renderArgs.view
    }
    void testShowWithId(){
        controller.params.id=sampleCounterReceipt.id
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.getCollectionSchedulesForCounterReceipt(1..1){x->
			return "This is a service"
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        def result = controller.show()
        assertEquals "The return value must be sampleCounterReceipt", sampleCounterReceipt, result.counterReceiptInstance
        assertEquals "The return value of the collection schedule must be This is a service ", "This is a service", result.collectionSchedules
    }
    void testShowWithoutId(){
        controller.metaClass.message = {Map map ->
            return map.code
        }
        controller.show()
        assertEquals "list", redirectArgs.action
        assertEquals "default.not.found.message",controller.flash.message
    }
    void testEditWithId(){
        controller.params.id=sampleCounterReceipt.id
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.getCustomerPaymentsForCounterReceipt(1..1){x->
			return [deliveries:[1],charges:[2]]
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        def results = controller.edit()
        assertEquals "Deliveries must contain 1",1, results.deliveries[0]
        assertEquals "Charges must contain 2",2, results.charges[0]
    }
    void testEditWithoutId(){
        controller.metaClass.message = {Map map ->
            return map.code
        }
        controller.edit()
        assertEquals "list", redirectArgs.action
        assertEquals "default.not.found.message",controller.flash.message
    }
    void testUpdateWithoutError(){
        controller.params.id=sampleCounterReceipt.id
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.updateCounterReceipt(1..1){x,y->
			return true
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.metaClass.message = {Map map ->
            return map.code
        }
        controller.update()
        assertEquals "show", redirectArgs.action
        assertEquals "default.updated.message",controller.flash.message
    }
    void testUpdateWithError(){
        controller.params.id=sampleCounterReceipt.id
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.updateCounterReceipt(1..1){x,y->
			return false
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.update()
        assertEquals "wrong action redirection", "edit", controller.renderArgs.view
    }
    void testUpdateWithoutId(){
        controller.metaClass.message = {Map map ->
            return map.code
        }
        controller.update()
        assertEquals "list", redirectArgs.action
        assertEquals "default.not.found.message",controller.flash.message
    }
	
    void testCancel(){
        mockParams.id = sampleCounterReceipt.id
        controller.metaClass.message = {x->
            return x
        }
		def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.isCancellable(1..1){x ->
			return true
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
		
        controller.cancel()
		
        assertEquals "show", redirectArgs.action
        assertEquals "Counter Receipt has been successfully cancelled!",controller.flash.message
        assertEquals "Counter Receipt status must be cancelled","Cancelled", sampleCounterReceipt.status.toString()
    }
	
    void testUnapprovedWithoutError(){
        controller.params.id=sampleCounterReceipt.id
        sampleCounterReceipt.approve()
        sampleCounterReceipt.save()
        assertTrue "[Guard] counter receipt must be initially be approved", sampleCounterReceipt.isApproved()
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.isUnapprovable(1..1){x->
			return "Unapprovable"
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.unapprove()
        assertTrue "counter receipt must be unapproved", sampleCounterReceipt.isUnapproved()
        assertEquals "show", redirectArgs.action
    }
    void testUnapprovedWithoutId(){
        controller.metaClass.message = {Map map ->
            return map.code
        }
        controller.unapprove()

        assertEquals "default.not.found.message",controller.flash.message
        assertEquals "list", redirectArgs.action
    }
    void testUnapprovedCannotBeUnapproved(){
        controller.params.id=sampleCounterReceipt.id
        sampleCounterReceipt.approve()
        sampleCounterReceipt.save()
        assertTrue "[Guard] counter receipt must be initially be approved", sampleCounterReceipt.isApproved()
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.isUnapprovable(1..1){x->
			return "Collection Exist"
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.unapprove()

        assertEquals "Cannot unapprove since it is assigned to a collection already",controller.flash.error
        assertTrue "counter receipt must still be approved", sampleCounterReceipt.isApproved()
        assertEquals "show", redirectArgs.action
    }
    void testUnapprovedCannotBeUnapprovedDaysPassed(){
        controller.params.id=sampleCounterReceipt.id
        sampleCounterReceipt.approve()
        sampleCounterReceipt.save()
        assertTrue "[Guard] counter receipt must be initially be approved", sampleCounterReceipt.isApproved()
        def mockCounterReceiptService = mockFor(CounterReceiptService)
		mockCounterReceiptService.demand.isUnapprovable(1..1){x->
			return "Time passed"
		}
		controller.counterReceiptService= mockCounterReceiptService.createMock()
        controller.unapprove()

        assertEquals "Cannot unapprove for 2 days have already passed since the approved date",controller.flash.error
        assertTrue "counter receipt must still be approved", sampleCounterReceipt.isApproved()
        assertEquals "show", redirectArgs.action
    }
}
