package com.munix

import grails.test.*
import grails.converters.JSON

class SupplierControllerTests extends ControllerUnitTestCase {
    def sampleSupplier
    def sampleProduct
    protected void setUp() {
        super.setUp()
        sampleSupplier = new Supplier(id:1L)
        mockDomain(Supplier, [sampleSupplier])
        sampleProduct = new Product(id:1L, identifier: "product")
        mockDomain(Product, [sampleProduct])
        sampleProduct.metaClass.'static'.formatDescription = {-> return "description"}

    }

    protected void tearDown() {
        super.tearDown()
    }

    void testShowWithError() {
        mockParams.id = 0
        controller.metaClass.message = {args->
            return args.code
        }
        controller.show()
        assertEquals "Should redirect to list", "list", redirectArgs.action
        assertEquals "The message did not match", "default.not.found.message", controller.flash.message
    }
    void testShowWithoutError() {
        mockParams.id = 1
        def mockSupplierService = mockFor(SupplierService)
        mockSupplierService.demand.rolePurchasingIsLoggedIn(1..1){->
            return true
        }
        controller.supplierService= mockSupplierService.createMock()

        def result = controller.show()
        assertEquals "Supplier must be sampleSupplier", sampleSupplier, result.supplierInstance
        assertTrue "Result must be true", result.showEditButton
    }
    void testEditWithoutError() {
        mockParams.id = 1
        def mockSupplierService = mockFor(SupplierService)
        mockSupplierService.demand.rolePurchasingIsLoggedIn(1..1){->
            return true
        }
        controller.supplierService= mockSupplierService.createMock()

        def result = controller.edit()
        assertEquals "Supplier must be sampleSupplier", sampleSupplier, result.supplierInstance
    }
    void testEditWithoutSupplierId() {
        mockParams.id = 0
        controller.metaClass.message = {args->
            return args.code
        }
        def mockSupplierService = mockFor(SupplierService)
        mockSupplierService.demand.rolePurchasingIsLoggedIn(1..1){->
            return true
        }
        controller.supplierService= mockSupplierService.createMock()

        controller.edit()
        assertEquals "Should redirect to list", "list", redirectArgs.action
        assertEquals "The message did not match", "default.not.found.message", controller.flash.message
    }
    void testEditRolePurchasingNotLoggedIn() {
        def mockSupplierService = mockFor(SupplierService)
        mockSupplierService.demand.rolePurchasingIsLoggedIn(1..1){->
            return false
        }
        controller.supplierService= mockSupplierService.createMock()

        controller.edit()
        assertEquals "Should redirect to list", "list", redirectArgs.action
        assertEquals "The message did not match", "Sorry you are not allowed to access this feature", controller.flash.message
    }
    void testEditSupplierItems() {
		mockParams.id = sampleSupplier.id
        def mockSupplierService = mockFor(SupplierService)
        mockSupplierService.demand.rolePurchasingIsLoggedIn(1..1){->
            return true
        }
        controller.supplierService= mockSupplierService.createMock()
		def result = controller.editSupplierItems()

		assertNotNull "should contain object", result.supplierInstance
	}

	void testGenerateAvailableProductsForSupplierItem() {
		def data = [[sampleProduct.id, sampleProduct.identifier, "description"]]
		def expectedJsonResponse = [iTotalRecords: 1, iTotalDisplayRecords: 1, aaData: data]
		def mockSupplierService = mockFor(SupplierService)
		mockSupplierService.demand.queryAvailableProducts(1..1) {x ->
			return new HashSet([sampleProduct])
		}
		controller.supplierService = mockSupplierService.createMock()

		controller.generateAvailableProductsForSupplierItem()

		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)

		assertEquals "json total Records should be correct", expectedJsonResponse.iTotalRecords, jsonResult.iTotalRecords
		assertEquals "json total display Records should be correct", expectedJsonResponse.iTotalDisplayRecords, jsonResult.iTotalDisplayRecords
		assertEquals "json data should be correct", expectedJsonResponse.aaData, jsonResult.aaData
	}

	void testUpdateSupplierItems() {
		mockParams.id = sampleSupplier.id
		mockParams."supplierItemList[0].product.id" = sampleProduct.id

		controller.updateSupplierItems()

		assertEquals "should redirect to correct page", "show", redirectArgs.action
	}

}
