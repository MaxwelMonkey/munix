package com.munix

import grails.test.*

class StockCardControllerTests extends ControllerUnitTestCase {
	def sampleStockCard
	def sampleStockCardItem
	def sampleStock
	def sampleProduct
	def sampleWarehouse
	
    protected void setUp() {
        super.setUp()
		
		sampleProduct = new Product()
		mockDomain(Product, [sampleProduct])
		
		sampleWarehouse = new Warehouse()
		mockDomain(Warehouse, [sampleWarehouse])
		
		sampleStockCard = new StockCard(
			product: sampleProduct
		)
		mockDomain(StockCard, [sampleStockCard])
		
		sampleStockCardItem = new StockCardItem(
			stockCard: sampleStockCard
		)
		mockDomain(StockCardItem, [sampleStockCardItem])
		sampleStockCard.items = [sampleStockCardItem]
		
		sampleStock = new Stock(
			warehouse: sampleWarehouse,
			product: sampleProduct,
			qty: BigDecimal.ONE
		)
		mockDomain(Stock, [sampleStock])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testShow() {
		mockParams.id = sampleStockCard.id
		
		def result = controller.show()
		
		assertTrue "model should contain sample stock", result.stocks.contains(sampleStock)
    }
	
	void testShowNoId() {
		def result = controller.show()
		
		assertEquals "should redirect to correct page", "list", redirectArgs.action
		assertEquals "should redirect to correct controller", "product", redirectArgs.controller
	}
}
