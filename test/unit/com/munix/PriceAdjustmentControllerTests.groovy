package com.munix

import grails.test.*
import java.text.SimpleDateFormat;
import grails.converters.JSON

class PriceAdjustmentControllerTests extends ControllerUnitTestCase {
	def sampleUser
	def sampleProduct1
	def sampleProduct2
	def sampleItemType
	def sdf
	def samplePriceAdjustment
	def samplePriceAdjustmentItem
	
    protected void setUp() {
        super.setUp()
		controller.metaClass.message = {Map map-> map}
		
		sdf = new SimpleDateFormat("MM/dd/yyyy")
		
		sampleUser = new User(username:"user")
		mockDomain(User, [sampleUser])

        samplePriceAdjustment = new PriceAdjustment()
        mockDomain(PriceAdjustment, [samplePriceAdjustment])

		samplePriceAdjustmentItem = new PriceAdjustmentItem()
		mockDomain(PriceAdjustmentItem, [samplePriceAdjustmentItem])
		
		sampleProduct1 = new Product(
			addedDescription: "1", 
			identifier: "product1",
			wholeSalePrice: new BigDecimal("10"),
			retailPrice: new BigDecimal("20"),
			isNet: false
		)
		sampleProduct2 = new Product(
			addedDescription: "2", 
			identifier: "product2",
			wholeSalePrice: new BigDecimal("15"),
			retailPrice: new BigDecimal("30")
		)
		mockDomain(Product, [sampleProduct1, sampleProduct2])
		sampleProduct1.metaClass.'static'.formatDescription = {-> return "description"}
		
		sampleItemType = new ItemType(identifier: "itemType", description: "desc")
		mockDomain(ItemType, [sampleItemType])
		
		mockDomain(Warehouse)
    }

    protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove Product
    }
	
    void testList() {
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.generateList(1..1){x->
			return [totalCount:1]
		}
		controller.priceAdjustmentService= mockPriceAdjustmentService.createMock()
        def result = controller.list()
        assertEquals "Should have 1 result", 1, result.priceAdjustmentInstanceTotal
    }
    void testCreate() {
		def result = controller.create()
		
		assertNotNull "Should return an object", result.priceAdjustmentInstance 
		assertTrue "Should return a priceAdjustment object", result.priceAdjustmentInstance instanceof PriceAdjustment
	}
	
	void testSaveWithoutErrors() {
		def cal = Calendar.getInstance()
		cal.add( Calendar.DATE, 1 )
		mockParams.effectiveDate = sdf.format(cal.getTime())
		mockParams.priceType = "Retail"
		mockParams.itemType = sampleItemType
		mockParams.items = [
				["product.id": sampleProduct1.id, newPrice: new BigDecimal("10.00")],
				["product.id": sampleProduct2.id, newPrice: new BigDecimal("20.00")]
		]

		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticate.createMock()
		
		controller.save()
		
		assertEquals("Price Adjustment should have 2 record", 2, PriceAdjustment.list().size())
		PriceAdjustment.list().each {
			assertEquals("Newly created Price Adjustment status should be Unapproved", PriceAdjustment.Status.UNAPPROVED, it.status)
		}
		assertEquals("Should redirect to show", "show", controller.redirectArgs.action)
	}
	
	void testSaveWithErrors() {
		mockParams.priceType = "Retail"
		
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticate.createMock()
		
		def result = controller.save()

		assertEquals("Should render create page", "create", renderArgs.view)
		assertEquals("Price Adjustment object should have picked params", PriceType.RETAIL, result.priceAdjustmentInstance.priceType)
	}
	
    void testShowWithId() {
		mockParams.id = samplePriceAdjustment.id

		def result = controller.show()

		assertEquals("Price Adjustment object should have been returned", samplePriceAdjustment, result.priceAdjustmentInstance)
	}
	
    void testShowWithoutId() {
		controller.show()

        assertEquals("Should render list page", "list", controller.redirectArgs.action)
		assertEquals("Price Adjustment not found.", controller.flash.message)
		assertEquals("Price Adjustment not found with id null.", controller.flash.defaultMessage)
	}
	
    void testEditWithId() {
		mockParams.id = samplePriceAdjustment.id

		def result = controller.edit()

		assertEquals("Price Adjustment object should have been returned", samplePriceAdjustment, result.priceAdjustmentInstance)
	}
	
    void testEditWithoutId() {
		controller.edit()

        assertEquals("Should render list page", "list", controller.redirectArgs.action)
		assertEquals("Price Adjustment not found.", controller.flash.message)
		assertEquals("Price Adjustment not found with id null.", controller.flash.defaultMessage)
	}
	
	void testUpdateSuccess() {
		samplePriceAdjustment.preparedBy = "me"
		samplePriceAdjustment.save()
		
		mockParams.id = samplePriceAdjustment.id
		mockParams.itemType = sampleItemType
		mockParams.priceType = "Retail"
		mockParams.effectiveDate = new Date()
		mockParams.items = [samplePriceAdjustmentItem]

		def result = controller.update()

		assertEquals("Should render show page", "show", controller.redirectArgs.action)
		assertEquals("Price Adjustment updated.", controller.flash.message)
		assertEquals("Price Adjustment 1 updated.", controller.flash.defaultMessage)
	}
	
	void testUpdateWithErrors() {
        mockParams.id = samplePriceAdjustment.id

		mockParams.priceType = "Retail"

		def result = controller.update()

		assertEquals("Should render edit page", "edit", renderArgs.view)
		assertEquals("Price Adjustment object should have picked params", PriceType.RETAIL, result.priceAdjustmentInstance.priceType)
	}
	
	void testUpdateWithNoPriceAdjustment() {
		mockParams.id = 100L

		def result = controller.update()

		assertEquals("Should render edit page", "edit", controller.redirectArgs.action)
		assertEquals("Price Adjustment not found", controller.flash.message)
		assertEquals("Price Adjustment not found with id 100.", controller.flash.defaultMessage)
	}
	
	void testApproveApprovable() {
		mockParams.id = samplePriceAdjustment.id
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.checkIfApprovable(1..1) { x ->
			return "Approvable"
		}
		controller.priceAdjustmentService = mockPriceAdjustmentService.createMock()
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticate.createMock()
		
		controller.approve()

		assertEquals "Sample price adjustment status should be approved", PriceAdjustment.Status.APPROVED, samplePriceAdjustment.status
		assertNotNull "Sample price adjustment approvedBy should not be empty", samplePriceAdjustment.approvedBy
		assertEquals "Should redirect to show page", "show", controller.redirectArgs.action
	}
	
	void testApproveDateError() {
		mockParams.id = samplePriceAdjustment.id
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.checkIfApprovable(1..1) { x ->
			return "DateError"
		}
		controller.priceAdjustmentService = mockPriceAdjustmentService.createMock()
		
		controller.approve()

		assertEquals "Sample price adjustment status should be unapproved", PriceAdjustment.Status.UNAPPROVED, samplePriceAdjustment.status
		assertNull "Sample price adjustment approvedBy should be empty", samplePriceAdjustment.approvedBy
		assertEquals "Should redirect to show page", "show", controller.redirectArgs.action
	}
	
	void testApprovePriceTypeAndItemTypeError() {
		mockParams.id = samplePriceAdjustment.id
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.checkIfApprovable(1..1) { x ->
			return "PriceTypeAndItemTypeError"
		}
		controller.priceAdjustmentService = mockPriceAdjustmentService.createMock()
		
		controller.approve()

		assertEquals "Sample price adjustment status should be unapproved", PriceAdjustment.Status.UNAPPROVED, samplePriceAdjustment.status
		assertNull "Sample price adjustment approvedBy should be empty", samplePriceAdjustment.approvedBy
		assertEquals "Should redirect to show page", "show", controller.redirectArgs.action
	}
	
	void testUnapproveUnapprovable() {
		samplePriceAdjustment.status = PriceAdjustment.Status.APPROVED
		samplePriceAdjustment.approvedBy = "me"
		samplePriceAdjustment.save()
		assertEquals "[GUARD] Sample price adjustment status should be approved", PriceAdjustment.Status.APPROVED, samplePriceAdjustment.status
		assertFalse "Sample price adjustment approvedBy should be empty", samplePriceAdjustment.approvedBy.isEmpty()
		
		mockParams.id = samplePriceAdjustment.id
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.checkIfUnapprovable(1..1) { x ->
			return true
		}
		controller.priceAdjustmentService = mockPriceAdjustmentService.createMock()
		
		controller.unapprove()
		
		assertEquals "Sample price adjustment status should be unapproved", PriceAdjustment.Status.UNAPPROVED, samplePriceAdjustment.status
		assertTrue "Sample price adjustment approvedBy should be empty", samplePriceAdjustment.approvedBy.isEmpty()
		assertEquals "Should redirect to show page", "show", controller.redirectArgs.action
	}
	
	void testUnapproveNotUnapprovable() {
		samplePriceAdjustment.status = PriceAdjustment.Status.APPROVED
		samplePriceAdjustment.approvedBy = "me"
		samplePriceAdjustment.save()
		assertEquals "[GUARD] Sample price adjustment status should be approved", PriceAdjustment.Status.APPROVED, samplePriceAdjustment.status
		assertFalse "Sample price adjustment approvedBy should be empty", samplePriceAdjustment.approvedBy.isEmpty()
		
		
		mockParams.id = samplePriceAdjustment.id
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.checkIfUnapprovable(1..1) { x ->
			return false
		}
		controller.priceAdjustmentService = mockPriceAdjustmentService.createMock()
		
		controller.unapprove()
		
		assertEquals "Sample price adjustment status should be approved", PriceAdjustment.Status.APPROVED, samplePriceAdjustment.status
		assertFalse "Sample price adjustment approvedBy should be empty", samplePriceAdjustment.approvedBy.isEmpty()
		assertEquals "Should redirect to show page", "show", controller.redirectArgs.action
	}
	
	void testCancel() {
		mockParams.id = samplePriceAdjustment.id
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.isCancelable(1..1) { x ->
			return true
		}
		controller.priceAdjustmentService = mockPriceAdjustmentService.createMock()
		controller.cancel()

		assertEquals "Sample price adjustment status should be cancelled", PriceAdjustment.Status.CANCELLED, samplePriceAdjustment.status
		assertEquals "Should redirect to show page", "show", controller.redirectArgs.action
	}
	
	void testRetrievePurchaseOrderItems() {
		mockParams.itemType = sampleItemType.id
		mockParams.sSearch = "product1"
		mockParams.priceType = "Retail"
		def mockPriceAdjustmentService = mockFor(PriceAdjustmentService)
		mockPriceAdjustmentService.demand.queryAvailableProducts(1..1){x, y ->
			return new HashSet([sampleProduct1])
		}
		controller.priceAdjustmentService = mockPriceAdjustmentService.createMock()
		
		def data = [[sampleProduct1.id, sampleProduct1.identifier, "description", false, new BigDecimal("20"), new BigDecimal("10"), new BigDecimal("20")]]
		def expectedJsonResponse = [iTotalRecords: 1, iTotalDisplayRecords: 1, aaData: data]
		
		controller.retrieveProductsForSale()
		
		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)
		
		assertEquals "json total Records should be correct", expectedJsonResponse.iTotalRecords, jsonResult.iTotalRecords
		assertEquals "json total display Records should be correct", expectedJsonResponse.iTotalDisplayRecords, jsonResult.iTotalDisplayRecords
		assertEquals "json data should be correct", expectedJsonResponse.aaData, jsonResult.aaData
	}
	
	void testRetrievePurchaseOrderItemsNoItemType() {
		controller.retrieveProductsForSale()
		
		assertEquals "should redirect to create", "create", controller.redirectArgs.action
	}
}
