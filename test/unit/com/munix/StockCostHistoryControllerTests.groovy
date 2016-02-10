package com.munix

import grails.test.*

class StockCostHistoryControllerTests extends ControllerUnitTestCase {
	def sampleProduct
	
    protected void setUp() {
        super.setUp()
		
		sampleProduct = new Product()
		mockDomain(Product, [sampleProduct])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testShow() {
		mockParams.id = sampleProduct.id
		def mockStockCostHistoryService = mockFor(StockCostHistoryService)
		mockStockCostHistoryService.demand.generateStockCostItems(1..1) {
			return null
		}
		controller.stockCostHistoryService = mockStockCostHistoryService.createMock()
		
		controller.show()
    }
	
	void testShowFail() {
		controller.show()
		
		assertEquals "wrong page redirect", "list", redirectArgs.action
		assertEquals "wrong controller redirect", "product", redirectArgs.controller
	}
}
