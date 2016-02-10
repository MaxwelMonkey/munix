package com.munix

import grails.test.*

class LaborCostControllerTests extends ControllerUnitTestCase {
	def sampleProduct
	def sampleLaborCost
	
    protected void setUp() {
        super.setUp()
        controller.metaClass.message={Map map-> map}

		sampleProduct = new Product()
		mockDomain(Product, [sampleProduct])
				
		sampleLaborCost = new LaborCost(
			product: sampleProduct,
			type: "type",
			amount: BigDecimal.ONE
		)
		mockDomain(LaborCost, [sampleLaborCost])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testList() {
		controller.list()
		
		assertEquals "should redirect to list", "list", controller.redirectArgs.action
		assertEquals "should redirect to controller", "product", controller.redirectArgs.controller
    }
	
	void testShow() {
		mockParams.id = sampleLaborCost.id
		
		controller.show()
		
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
		assertEquals "should redirect to controller", "product", controller.redirectArgs.controller
		assertEquals "should redirect to sample product", sampleProduct.id, controller.redirectArgs.id
	}
	
	void testShowWithoutId() {
		controller.show()
		
		assertEquals "should redirect to list", "list", controller.redirectArgs.action
		assertEquals "should redirect to controller", "product", controller.redirectArgs.controller
	}
	
	void testCreate() {
		def result = controller.create()
		
		assertNotNull "should have a laborCostInstance", result.laborCostInstance
	}
	
	void testSave() {
		mockParams.id = sampleProduct.id
		mockParams.type = "type"
		mockParams.amount = "100"
		def oldSize = Product.count()
		
		def result = controller.save()
		
		assertEquals "should redirect to show page", "show", controller.redirectArgs.action
	}
	
	void testSaveFail() {
		mockParams.id = sampleProduct.id
		mockParams.type = "type"
		mockParams.amount = "aoeu"
		
		def result = controller.save()
		
		assertEquals "should redirect to create page", "create", controller.renderArgs.view
		assertNotNull "should have a laborCostInstance", result.laborCostInstance
	}
	
	void testEdit() {
		mockParams.id = sampleLaborCost.id
		
		def result = controller.edit()
		
		assertNotNull "should have a laborCostInstance", result.laborCostInstance
		assertEquals "laborCostInstance should be sampleLaborCost", sampleLaborCost.id, result.laborCostInstance.id 
	}
	
	void testUpdate() {
		mockParams.id = sampleLaborCost.id
		mockParams.type = "edit"
		mockParams.amount = "100"
		
		controller.update()
		
		assertEquals "type should be edit", "edit", sampleLaborCost.type
		assertEquals "amount should be 100", new BigDecimal("100"), sampleLaborCost.amount
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
		assertEquals "should redirect to product controller", "product", controller.redirectArgs.controller
	}
	
	void testUpdateFail() {
		mockParams.id = sampleLaborCost.id
		mockParams.type = "type"
		mockParams.amount = "aoeu"
		
		def result = controller.save()
		
		assertEquals "should redirect to create page", "create", controller.renderArgs.view
		assertNotNull "should have a laborCostInstance", result.laborCostInstance
	}

	void testDelete() {
		mockParams.id = sampleLaborCost.id
		
		controller.delete()
		
		assertEquals "should redirect to show page", "show", controller.redirectArgs.action
		assertEquals "should redirect to product controller", "product", controller.redirectArgs.controller
		assertEquals "should have product id", sampleProduct.id, controller.redirectArgs.id
	}
	
	void testDeleteFail() {
		controller.delete()
		
		assertEquals "should redirect to list page", "list", controller.redirectArgs.action
	}
}
