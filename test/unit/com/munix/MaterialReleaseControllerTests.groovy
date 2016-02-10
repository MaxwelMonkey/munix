package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class MaterialReleaseControllerTests extends ControllerUnitTestCase {
	def mockMaterialReleaseService
	
	def sampleJobOrder
	def sampleMaterialRequisitionItem
	def sampleMaterialRelease
	def sampleMaterialReleaseItem
	def sampleStock
	def sampleWarehouse
	def sampleProduct
	
	void setUp() {
		super.setUp()
		controller.metaClass.message = {Map map -> }
		mockMaterialReleaseService = mockFor(MaterialReleaseService)
		
		sampleProduct = new Product(
			id: 1,
			identifier: "product"
		)
		mockDomain(Product, [sampleProduct])
		
		sampleWarehouse = new Warehouse(
			identifier : "BULL",
			description : "bull"
		)
		mockDomain(Warehouse, [sampleWarehouse])

		sampleStock = new Stock(
			product: sampleProduct,
			warehouse: sampleWarehouse,
			qty: new BigDecimal("20")
		)
		mockDomain(Stock, [sampleStock])
		
		sampleProduct.stocks = [sampleStock]

		sampleJobOrder = new JobOrder()
		mockDomain (JobOrder, [sampleJobOrder])
		
		sampleMaterialRequisitionItem = new MaterialRequisitionItem(
			component: sampleProduct 
		)
		mockDomain(MaterialRequisitionItem, [sampleMaterialRequisitionItem])
		
		sampleMaterialRelease = new MaterialRelease(
			warehouse: sampleWarehouse,
			preparedBy: "me",
			jobOrder: sampleJobOrder
		)
		mockDomain(MaterialRelease, [sampleMaterialRelease])
		
		sampleMaterialReleaseItem = new MaterialReleaseItem(
			materialRequisitionItem: sampleMaterialRequisitionItem,
			qty: 10,
			materialRelease: sampleMaterialRelease
		) 
		mockDomain(MaterialReleaseItem, [sampleMaterialReleaseItem])
		
		sampleMaterialRelease.items = [sampleMaterialReleaseItem]
	}
	
	void tearDown() {
		super.tearDown()
	}

	void testApprove() {
		mockParams.id = sampleMaterialRelease.id
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1) {-> return new User(username: "user")}
		controller.authenticateService = mockAuthenticateService.createMock()

        def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createApprovedMaterialReleaseStockCards(1..1) { x ->
			return []
		}
		controller.stockCardService = mockStockCardService.createMock()

        mockMaterialReleaseService.demand.isApprovable(1..1) {x -> true}
		mockMaterialReleaseService.demand.updateItemsCost(1..1) {x -> }
		mockMaterialReleaseService.demand.releaseStocksInWarehouse(1..1) {x -> }
		controller.materialReleaseService = mockMaterialReleaseService.createMock()
		
		controller.approve()
		
		assertEquals "sample material release should be approved", MaterialRelease.Status.APPROVED, sampleMaterialRelease.status
		assertEquals "should redirect to correct page", "show", controller.redirectArgs.action
	}
	
	void testApproveFail() {
		sampleMaterialRelease.jobOrder = null
		mockParams.id = sampleMaterialRelease.id
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1) {-> return new User(username: "user")}
		controller.authenticateService = mockAuthenticateService.createMock()

        mockMaterialReleaseService.demand.isApprovable(1..1) {x -> true}
		mockMaterialReleaseService.demand.updateItemsCost(1..1) {x -> }
		
		controller.materialReleaseService = mockMaterialReleaseService.createMock()

		controller.approve()
		
		assertEquals "should redirect to correct page", "show", controller.redirectArgs.action
	}	

	void testApproveNoId() {
		controller.approve()
		
		assertEquals "should redirect to correct page", "list", controller.redirectArgs.action
		assertEquals "should redirect to correct controller", "jobOrder", controller.redirectArgs.controller
	}
			
	void testCancel() {
		mockParams.id = sampleMaterialRelease.id
		
		controller.cancel()
		
		assertEquals "Material Release status should be cancelled", MaterialRelease.Status.CANCELLED, sampleMaterialRelease.status
	}
}
