package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class JobOutControllerTests extends ControllerUnitTestCase {
    def mockAuthenticateService
	def sampleJobOrder
    def sampleJobOut
	def sampleMaterialRequisition
	def sampleWarehouse
	def sampleLaborCost
	def sampleProduct1
	def sampleProduct2
	def sampleMaterialRequisitionItem1
	def sampleMaterialRequisitionItem2
	def sampleMaterialRelease
	def sampleMaterialReleaseItem1
	
    protected void setUp() {
        super.setUp()
		controller.metaClass.message = {Map map ->
			return map.code
		}

        mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1){ ->
			return new User(username : "Tester")
		}
		
		sampleProduct1 = new Product(identifier: "product1", runningCost: new BigDecimal("100"))
		sampleProduct2 = new Product(identifier: "product2", runningCost: new BigDecimal("400"))
		mockDomain(Product, [sampleProduct1, sampleProduct2])
		
		sampleJobOrder = new JobOrder(qty: 20)
		mockDomain(JobOrder, [sampleJobOrder])
		
		sampleWarehouse = new Warehouse()
		mockDomain(Warehouse, [sampleWarehouse])
		
		sampleLaborCost = new LaborCost(
			amount: new BigDecimal("100.00")	
		)
		mockDomain(LaborCost, [sampleLaborCost])

		sampleMaterialRequisition = new MaterialRequisition()
		mockDomain(MaterialRequisition, [sampleMaterialRequisition])
		sampleJobOrder.requisition = sampleMaterialRequisition
		
		sampleMaterialRequisitionItem1 = new MaterialRequisitionItem(component: sampleProduct1, unitsRequired: 10)
		sampleMaterialRequisitionItem2 = new MaterialRequisitionItem(component: sampleProduct2, unitsRequired: 20)
		mockDomain(MaterialRequisitionItem, [sampleMaterialRequisitionItem1, sampleMaterialRequisitionItem2])
		
		sampleMaterialRequisition.addToItems(sampleMaterialRequisitionItem1)
		sampleMaterialRequisition.addToItems(sampleMaterialRequisitionItem2)
		
		sampleMaterialRelease = new MaterialRelease(status: MaterialRelease.Status.APPROVED)
		mockDomain(MaterialRelease,[sampleMaterialRelease])
		
		sampleMaterialReleaseItem1 = new MaterialReleaseItem(materialRequisitionItem: sampleMaterialRequisitionItem1, qty: 90, cost: 1000)
		mockDomain(MaterialReleaseItem, [sampleMaterialReleaseItem1])
		
		sampleMaterialRelease.addToItems(sampleMaterialReleaseItem1)
		MaterialRequisitionItem.metaClass.static.getMaterialReleaseItems = { -> return [sampleMaterialReleaseItem1]}
		
		sampleJobOut = new JobOut(
			warehouse: sampleWarehouse,
			qty: 10,
			laborCost: sampleLaborCost,
			preparedBy: "preparer",
			jobOrder: sampleJobOrder
		)
		mockDomain(JobOut, [sampleJobOut])

    }
	
    protected void tearDown() {
        super.tearDown()
    }
	
	void testSave() {
		mockParams.id = sampleJobOrder.id
		mockParams."warehouse.id" = sampleWarehouse.id
		mockParams.qty = 1
		mockParams."laborCost.id" = sampleLaborCost.id
		controller.authenticateService = mockAuthenticateService.createMock()
		def mockJobOutService = mockFor(JobOutService)
		mockJobOutService.demand.validateQuantity(1..1) {x, y ->
			return true
		}
		controller.jobOutService = mockJobOutService.createMock()
		
		controller.save()
		
		assertEquals "should redirect to correct page", "show", redirectArgs.action
	}
	
	void testSaveFailDueToQuantity() {
		mockParams.id = sampleJobOrder.id
		mockParams."warehouse.id" = sampleWarehouse.id
		mockParams.qty = 1
		mockParams."laborCost.id" = sampleLaborCost.id
		controller.authenticateService = mockAuthenticateService.createMock()
		def mockJobOutService = mockFor(JobOutService)
		mockJobOutService.demand.validateQuantity(1..1) {x, y ->
			return false
		}
		controller.jobOutService = mockJobOutService.createMock()
		
		controller.save()
		
		assertEquals "should redirect to correct page", "create", renderArgs.view
	}
	
	void testSaveFail() {
		mockParams.id = sampleJobOrder.id
		mockParams."warehouse.id" = sampleWarehouse.id
		mockParams."laborCost.id" = sampleLaborCost.id
		mockParams.qty = 0
		controller.authenticateService = mockAuthenticateService.createMock()
		def mockJobOutService = mockFor(JobOutService)
		mockJobOutService.demand.validateQuantity(1..1) {x, y ->
			return true
		}
		controller.jobOutService = mockJobOutService.createMock()
		
		controller.save()
		
		assertEquals "should redirect to correct page", "create", renderArgs.view
	}
	
	void testUpdate() {
		mockParams.id = sampleJobOut.id
		mockParams.qty = 2
		def mockJobOutService = mockFor(JobOutService)
		mockJobOutService.demand.validateQuantity(1..1) {x, y ->
			return true
		}
		controller.jobOutService = mockJobOutService.createMock()

		controller.update()
		
		assertEquals "quantity should be updated", 2, sampleJobOut.qty
		assertEquals "should redirect to correct page", "show", redirectArgs.action
	}
	
	void testUpdateNoId() {
		controller.update()
		
		assertEquals "wrong controller redirection", "list", controller.redirectArgs.action
	}
	
	void testUpdateFail() {
		mockParams.id = sampleJobOut.id
		mockParams.qty = 0
		def mockJobOutService = mockFor(JobOutService)
		mockJobOutService.demand.validateQuantity(1..1) {x, y ->
			return true
		}
		controller.jobOutService = mockJobOutService.createMock()

		controller.update()
		
		assertEquals "wrong controller redirection", "edit", renderArgs.view
	}
	
	void testShow(){
		mockParams.id = sampleJobOut.id
		
		def result = controller.show()
		
		assertEquals "Wrong computation in Job Out Components Cost", new BigDecimal("2000.00"), result.componentsCost
		assertEquals "Wrong computation in Job Out Total Cost per Assembly", new BigDecimal("2100.00"), result.totalCostPerAssembly
		assertEquals "Wrong computation in Job Out Total Cost", new BigDecimal("21000.00"), result.jobOutTotalCost
	}
	
	void testShowFail(){
		
		controller.show()
		
		assertEquals "Wrong redirection", "list", redirectArgs.action
	}
	
    void testApprove() {
        mockParams.id = sampleJobOut.id
        def mockJobOutService = mockFor(JobOutService)
		mockJobOutService.demand.approve(1){ x-> return true}
        controller.jobOutService = mockJobOutService.createMock()
		
        controller.approve()
		
        assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "Job Out has been successfully approved!", controller.flash.message
    }
	
    void testUnapprove() {
        mockParams.id = sampleJobOut.id
        controller.authenticateService = mockAuthenticateService.createMock()
        def mockJobOutService = mockFor(JobOutService)
        mockJobOutService.demand.isUnapprovable(1){ x-> return true}
        mockJobOutService.demand.removeStocksInJobOut(1){ x-> }
        controller.jobOutService = mockJobOutService.createMock()
		def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createUnapprovedJobOut(1..1){ x-> }
		controller.stockCardService = mockStockCardService.createMock()
		
        controller.unapprove()
		
        assertTrue "Job out must be unapprove", sampleJobOut.isUnapproved()
        assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "Job Out has been unapproved!", controller.flash.message
    }
	
    void testUnapproveUnapprovable() {
        mockParams.id = sampleJobOut.id
        controller.authenticateService = mockAuthenticateService.createMock()
        def mockJobOutService = mockFor(JobOutService)
        mockJobOutService.demand.isUnapprovable(1){ x-> return false}
        controller.jobOutService = mockJobOutService.createMock()
        controller.unapprove()
        assertEquals "wrong controller redirection", "show", controller.redirectArgs.action
        assertEquals "Job Out cannot be unapproved!", controller.flash.message
    }

	void testCancel() {
		mockParams.id = sampleJobOut.id
		
		controller.cancel()

		assertEquals "Sample job out status should be cancelled", JobOut.Status.CANCELLED, sampleJobOut.status
		assertEquals "Should redirect to show page", "show", controller.redirectArgs.action
	}

}
