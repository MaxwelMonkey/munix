package com.munix

import grails.test.*
import grails.converters.JSON
import org.grails.plugins.springsecurity.service.AuthenticateService

class InventoryAdjustmentControllerTests extends ControllerUnitTestCase {
	def sampleItemType
	def sampleInventoryAdjustment
	def sampleInventoryAdjustmentItem
	def sampleProduct1
	def sampleProduct2
	def sampleWarehouse
	def sampleStock1
	def sampleStock2
	
    protected void setUp() {
        super.setUp()
		
		setupItemType()
		setupInventoryAdjustments()
		setupInventoryAdjustmentItems()
		setupProducts()
		setupWarehouses()
		setupStocks()
		setupProductAndStockRelationship()
    }

    protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove Product
    }

    void testApprove() {
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1){->
			return new User(username : "user")
		}
		controller.authenticateService = mockAuthenticateService.createMock()
		def mockInventoryAdjustmentService = mockFor(InventoryAdjustmentService)
		mockInventoryAdjustmentService.demand.updateProductStock(1..1){x -> }
		controller.inventoryAdjustmentService = mockInventoryAdjustmentService.createMock()
		def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createApprovedInventoryAdjustment(1..1){x -> }
		controller.stockCardService = mockStockCardService.createMock()
		
		mockParams.id = sampleInventoryAdjustment.id
		
		controller.approve()
		
		assertEquals "should be approved", InventoryAdjustment.Status.APPROVED, sampleInventoryAdjustment.status
    }
	
	void testRetrieveInventoryAdjustmentItems() {
		mockParams.warehouseId = sampleWarehouse.id
		mockParams.itemTypeId = sampleItemType.id
		mockParams.sSearch = "product1"
		mockParams.inventoryStatus = "Active"

		def data = [[sampleProduct1.id, sampleProduct1.identifier, "description1", new BigDecimal("100")]]
		def expectedJsonResponse = [iTotalRecords: 1, iTotalDisplayRecords: 1, aaData: data]
		
		controller.retrieveInventoryAdjustmentItems()
		
		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)
		
		assertEquals "json total Records should be correct", expectedJsonResponse.iTotalRecords, jsonResult.iTotalRecords
		assertEquals "json total display Records should be correct", expectedJsonResponse.iTotalDisplayRecords, jsonResult.iTotalDisplayRecords
		assertEquals "json data should be correct", expectedJsonResponse.aaData, jsonResult.aaData
	}
	
	void testRetrieveInventoryAdjustmentItemsNotActive() {
		mockParams.warehouseId = sampleWarehouse.id
		mockParams.itemTypeId = sampleItemType.id
		mockParams.sSearch = "product2"
		mockParams.inventoryStatus = "Inactive"

		def data = [[sampleProduct2.id, sampleProduct2.identifier, "description2", new BigDecimal("50")]]
		def expectedJsonResponse = [iTotalRecords: 1, iTotalDisplayRecords: 1, aaData: data]
		
		controller.retrieveInventoryAdjustmentItems()
		
		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)
		
		assertEquals "json total Records should be correct", expectedJsonResponse.iTotalRecords, jsonResult.iTotalRecords
		assertEquals "json total display Records should be correct", expectedJsonResponse.iTotalDisplayRecords, jsonResult.iTotalDisplayRecords
		assertEquals "json data should be correct", expectedJsonResponse.aaData, jsonResult.aaData
	}
	
	private void setupItemType() {
		sampleItemType = new ItemType()
		mockDomain(ItemType, [sampleItemType])
	}
	
	private void setupInventoryAdjustments() {
		sampleInventoryAdjustment = new InventoryAdjustment(
			preparedBy: "me",
			itemType: sampleItemType,
			dateGenerated: new Date(),
			items: [sampleInventoryAdjustmentItem]
		)
		mockDomain(InventoryAdjustment, [sampleInventoryAdjustment])
	}
	
	private void setupInventoryAdjustmentItems() {
		sampleInventoryAdjustmentItem = new InventoryAdjustmentItem()
		mockDomain(InventoryAdjustmentItem, [sampleInventoryAdjustmentItem])
	}
	
	private void setupProducts() {
		sampleProduct1 = new Product(
			addedDescription: "1",
			identifier: "product1",
			wholeSalePrice: new BigDecimal("10"),
			retailPrice: new BigDecimal("20"),
			isNet: false,
			status: "Active"
		)
		sampleProduct2 = new Product(
			addedDescription: "2",
			identifier: "product2",
			wholeSalePrice: new BigDecimal("15"),
			retailPrice: new BigDecimal("30"),
			status: "Inactive"
		)
		mockDomain(Product, [sampleProduct1, sampleProduct2])
		sampleProduct1.metaClass.'static'.formatDescription = {-> return "description1"}
		sampleProduct2.metaClass.'static'.formatDescription = {-> return "description2"}
	}
	
	private void setupWarehouses() {
		sampleWarehouse = new Warehouse()
		mockDomain(Warehouse, [sampleWarehouse])
	}
	
	private void setupStocks() {
		sampleStock1 = new Stock(
			product: sampleProduct1,
			warehouse: sampleWarehouse,
			qty: new BigDecimal("100")
		)
		mockDomain(Stock, [sampleStock1])
		
		sampleStock2 = new Stock(
			product: sampleProduct2,
			warehouse: sampleWarehouse,
			qty: new BigDecimal("50")
		)
		mockDomain(Stock, [sampleStock1, sampleStock2])
	}
	
	private void setupProductAndStockRelationship() {
		sampleProduct1.addToStocks(sampleStock1)
		sampleProduct2.addToStocks(sampleStock2)
	}
}