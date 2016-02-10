package com.munix

import grails.test.*
import grails.converters.JSON

class ProductControllerTests extends ControllerUnitTestCase {
	def sampleDiscountType
	def sampleProduct
	def sampleProductForComponent
	def sampleProductUnit
	def sampleItemLocation
	
    protected void setUp() {
        super.setUp()
		controller.metaClass.message={Map map-> map}
		
        setUpProductUnits()
		setUpDiscountTypes()
		setUpProducts()
		mockDomain(ProductComponent)
		setUpItemLocations()
    }
	
    protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove Product
    }
	
	void testViewPriceAdjustmentHistory() {
		mockParams.id = sampleProduct.id
		def mockProductService = mockFor(ProductService)
		mockProductService.demand.generateAppliedPriceAdjustmentItemsForProduct(1..1) { ->
			return true
		}
		controller.productService = mockProductService.createMock()

		def result = controller.viewPriceAdjustmentHistory()
		
		assertEquals("Product in params should be equal to sampleProduct", sampleProduct, result.productInstance)
	}
	
	void testSave() {
		mockParams.id = sampleProduct.id
		mockParams.identifier = "123123"
		mockParams.type = sampleDiscountType.id
		
		def mockProductService = mockFor(ProductService)
		mockProductService.demand.constructItemLocationList(1..1) {x ->
		return [sampleItemLocation]
		}
		mockProductService.demand.createStockCardForNewProduct(1..1) {x ->
			return null
		}
		mockProductService.demand.createStocksForNewProduct(1..1) {x ->
			return null
		}
		controller.productService = mockProductService.createMock()
		
		controller.save()
		
		assertEquals "wrong redirection", "show", redirectArgs.action
	}
	
	void testUpdate() {
		mockParams.id = sampleProduct.id
		mockParams.modelNumber = "new model number"

		def mockProductService = mockFor(ProductService)
		mockProductService.demand.constructItemLocationList(1..1) {x ->
			return [sampleItemLocation]
		}
        mockProductService.demand.generateAuditTrails(1..1) {x ->
			return []
		}
		controller.productService = mockProductService.createMock()
		
		controller.update()
		
		assertEquals "model Number should be updated", "new model number", sampleProduct.modelNumber
		assertEquals "wrong redirection", "show", redirectArgs.action
	}
	
	void testUpdateWithIsComponentParam() {
		mockParams.id = sampleProduct.id
		mockParams.isComponent = "false"
		mockParams.modelNumber = "new model number"
		
		def mockProductService = mockFor(ProductService)
		mockProductService.demand.checkIfProductComponentExistForProduct(1..1) {x,y ->
			return false
		}
		mockProductService.demand.constructItemLocationList(1..1) {x ->
			return [sampleItemLocation]
		}
        mockProductService.demand.generateAuditTrails(1..1) {x ->
			return []
		}
		controller.productService = mockProductService.createMock()
		
		controller.update()
		
		assertEquals "model Number should be updated", "new model number", sampleProduct.modelNumber
		assertEquals "wrong redirection", "show", redirectArgs.action
	}
	
	void testUpdateWithIsComponentParamAlreadyUsedAsComponent() {
		mockParams.id = sampleProduct.id
		mockParams.isComponent = "false"
		mockParams.modelNumber = "new model number"
		
		def mockProductService = mockFor(ProductService)
		mockProductService.demand.checkIfProductComponentExistForProduct(1..1) {x,y ->
			return true
		}
		controller.productService = mockProductService.createMock()
		
		controller.update()
		
		assertEquals "model Number should not be updated", "123", sampleProduct.modelNumber
		assertEquals "wrong redirection", "edit", renderArgs.view
	}
	
	void testEditProductComponent() {
		mockParams.id = sampleProduct.id
		
		def result = controller.editProductComponent()
		
		assertNotNull "should contain object", result.productInstance
	}
	
	void testGenerateProductComponentTable() {
		mockParams.productId = sampleProduct.id
		mockParams.sSearch = "product"
		def mockProductService = mockFor(ProductService)
		mockProductService.demand.getAllComponents(1..1){x ->
			return [sampleProductForComponent]
		}
		controller.productService = mockProductService.createMock()
		
		def data = [[sampleProductForComponent.id, sampleProductForComponent.identifier, "description", BigDecimal.ONE, sampleProductUnit.identifier, sampleProduct.id]]
		def expectedJsonResponse = [iTotalRecords: 1, iTotalDisplayRecords: 1, aaData: data]
		
		controller.generateProductComponentTable()
		
		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)
		
		assertEquals "json total Records should be correct", expectedJsonResponse.iTotalRecords, jsonResult.iTotalRecords
		assertEquals "json total display Records should be correct", expectedJsonResponse.iTotalDisplayRecords, jsonResult.iTotalDisplayRecords
		assertEquals "json data should be correct", expectedJsonResponse.aaData, jsonResult.aaData
	}
	
	void testUpdateProductComponent() {
		mockParams.id = sampleProduct.id
		mockParams."componentList[0].product.id" = sampleProduct.id 
		mockParams."componentList[0].component.id" = sampleProductForComponent.id
		mockParams."componentList[0].qty" = 10
		
		controller.updateProductComponent()
		
		assertEquals "should redirect to correct page", "show", redirectArgs.action
	}
	
	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(identifier: "id", description: "desc")
		mockDomain(DiscountType, [sampleDiscountType])
	}
	
	private void setUpProducts() {
		sampleProduct = new Product(identifier: "product1", modelNumber: "123", type: sampleDiscountType)
		sampleProductForComponent = new Product(identifier: "product component", unit: sampleProductUnit, type: sampleDiscountType)
		mockDomain(Product, [sampleProduct, sampleProductForComponent])
		sampleProductForComponent.metaClass.'static'.formatDescription = {-> return "description"}
	}
	
	private void setUpProductUnits() {
		sampleProductUnit = new ProductUnit(
			identifier: "pc",
			description: "desc"
		)
		mockDomain(ProductUnit, [sampleProductUnit])
	}
	
	private void setUpItemLocations() {
		sampleItemLocation = new ItemLocation(description: "item location")
		mockDomain(ItemLocation, [sampleItemLocation])
	}
}
