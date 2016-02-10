package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class JobOrderControllerTests extends ControllerUnitTestCase {
	def mockAuthenticateService
	
	def sampleAssembler
	def sampleProduct
	def sampleJobOrder
	def sampleMaterialRelease
	
    protected void setUp() {
        super.setUp()
        controller.metaClass.message = {Map map ->
            return map.code
        }
		
		sampleProduct = new Product(identifier : "XXX11")
		mockDomain(Product, [sampleProduct])
		
		sampleJobOrder = new JobOrder(
			startDate : new Date(),
			targetDate : new Date(),
			assignedTo : new Assembler(),
			qty : new BigDecimal(100),
			preparedBy : "test", 
			product : sampleProduct,
		)
		mockDomain(JobOrder, [sampleJobOrder])
		
		sampleMaterialRelease = new MaterialRelease(
			jobOrder: sampleJobOrder
		)
		mockDomain(MaterialRelease, [sampleMaterialRelease])
		
		sampleJobOrder.releases = [sampleMaterialRelease]
		
		sampleAssembler = new Assembler()
		mockDomain(Assembler, [sampleAssembler])
		
		mockDomain(MaterialRequisition)
		
		mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1){ ->
			return new User(username : "Tester")
		}
    }

    protected void tearDown() {
        super.tearDown()
    }
    
    void testSaveSuccess() { 
        controller.authenticateService = mockAuthenticateService.createMock()
		
        mockParams.startDate = new Date()
        mockParams.targetDate = new Date()
        mockParams."product.id" = sampleProduct.id
        mockParams.qty = 300
        mockParams."assignedTo.id" = 300
		
        controller.save()

        assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "wrong message code", "default.created.message", controller.flash.message
    }

    void testSaveFail() { 
        controller.authenticateService = mockAuthenticateService.createMock()
		
        controller.save()
		
        assertEquals "wrong controller redirection", "create", controller.renderArgs.view
    }

    void testUpdateSuccess(){
        mockParams.materialList = [[qty : 100, productComponent : 1, id : 1]]
        mockParams.id = sampleJobOrder.id
        controller.authenticateService = mockAuthenticateService.createMock()
		
        controller.update()
		
        assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
    }
	
	void testApproveWithId(){
		controller.params.id = sampleJobOrder.id
		controller.authenticateService = mockAuthenticateService.createMock()
		
		controller.approve()
		
		assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
	}
	
	void testApproveWithoutId(){
		mockParams.id = sampleJobOrder.id
		controller.authenticateService = mockAuthenticateService.createMock()
		
		controller.approve()
		
		assertEquals "flash error is wrong", null, controller.flash.error
		assertEquals "show", redirectArgs.action
	}
	
	void testMarkAsComplete(){
		mockParams.id = sampleJobOrder.id
        controller.authenticateService = mockAuthenticateService.createMock()
		def mockJobOrderService = mockFor(JobOrderService)
		mockJobOrderService.demand.isCompletable(1..1) { x ->
			return true
		}
		controller.jobOrderService = mockJobOrderService.createMock()

		controller.markAsComplete()
		
		assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "flash message is wrong", "The job order has been successfully completed!", controller.flash.message
	}
    void testMarkAsCompleteCannotBeCompleted(){
		mockParams.id = sampleJobOrder.id
		def mockJobOrderService = mockFor(JobOrderService)
		mockJobOrderService.demand.isCompletable(1..1) { x ->
			return false
		}
		controller.jobOrderService = mockJobOrderService.createMock()

		controller.markAsComplete()

		assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "flash message is wrong", "The job order cannot be completed!", controller.flash.message
	}
    void testUnmarkAsComplete(){
		mockParams.id = sampleJobOrder.id
        controller.authenticateService = mockAuthenticateService.createMock()
		def mockJobOrderService = mockFor(JobOrderService)
		mockJobOrderService.demand.unmarkableAsComplete(1..1) { x ->
			return true
		}
		controller.jobOrderService = mockJobOrderService.createMock()

		controller.unmarkAsComplete()

		assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "flash message is wrong", "The job order has been unmarked as complete!", controller.flash.message
	}
    void testUnmarkAsCompleteCannotBeCompleted(){
		mockParams.id = sampleJobOrder.id
		def mockJobOrderService = mockFor(JobOrderService)
		mockJobOrderService.demand.unmarkableAsComplete(1..1) { x ->
			return false
		}
		controller.jobOrderService = mockJobOrderService.createMock()

		controller.unmarkAsComplete()

		assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "flash message is wrong", "The job order cannot be unmarked as complete!", controller.flash.message
	}
    void testUnapproveWithId(){
		mockParams.id = sampleJobOrder.id
		def mockJobOrderService = mockFor(JobOrderService)
		mockJobOrderService.demand.checkIfUnapprovable(1..1) { x ->
			return true
		}
		controller.jobOrderService = mockJobOrderService.createMock()

		controller.unapprove()

		assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
	}
	
	void testUnapproveWithoutId(){
		mockParams.id = sampleJobOrder.id
		def mockJobOrderService = mockFor(JobOrderService)
		mockJobOrderService.demand.checkIfUnapprovable(1..1) { x ->
			return false
		}
		controller.jobOrderService = mockJobOrderService.createMock()
		
		controller.unapprove()
		
		assertEquals "flash error is wrong", "Job order cannot be unapproved! Active Material Release existing.", controller.flash.error
		assertEquals "show", redirectArgs.action
	}
	
	void testApproveMaterialReleases() {
		sampleMaterialRelease.approve()
		mockParams.id = sampleJobOrder.id
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1){ ->
			return new User(username : "Tester")
		}
        controller.authenticateService = mockAuthenticateService.createMock()
		controller.approveMaterialReleases()
		
		assertTrue "job order status should be changed to material releases approved", sampleJobOrder.isMaterialReleasesApproved()
		assertEquals "should redirect to correct page", "show", redirectArgs.action
	}	
	
	void testUnapproveMaterialReleases() {
		sampleJobOrder.approveMaterialReleases()
		sampleJobOrder.materialReleasesApprovedBy = "me"
		mockParams.id = sampleJobOrder.id
		
		controller.unapproveMaterialReleases()
		
		assertTrue "job order status should be reverted to approved for item releases", sampleJobOrder.isJobOrderApproved()
		assertTrue "job order materials release approved by should be empty", sampleJobOrder.materialReleasesApprovedBy.isEmpty()
		assertEquals "should redirect to correct page", "show", redirectArgs.action
	}
	
	void testAddJobOut() {
		mockParams.id = sampleJobOrder.id
		sampleJobOrder.approveMaterialReleases()
		
		controller.addJobOut()
		
		assertEquals "wrong redirect", "create", redirectArgs.action
		assertEquals "wrong redirect controller", "jobOut", redirectArgs.controller
	}
	
	void testAddJobOutNotMaterialReleasesApproved() {
		mockParams.id = sampleJobOrder.id
		
		controller.addJobOut()
		
		assertEquals "wrong redirect", "show", redirectArgs.action
	}
	
	void testAddJobOutZeroBalance() {
		mockParams.id = sampleJobOrder.id
		sampleJobOrder.approveMaterialReleases()
		def jobOut = new JobOut(qty: 100)
		sampleJobOrder.addToJobOuts(jobOut)
		
		controller.addJobOut()
		
		assertEquals "wrong redirect", "show", redirectArgs.action
	}
}
