package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService;

class PurchaseOrderControllerTests extends ControllerUnitTestCase {
	def purchaseOrderService
	def sampleProduct1
	def sampleProduct2
	def sampleCountry
	def sampleCurrency
	def sampleSupplier
	def samplePo
	def sampleSupplierItem1
	def sampleSupplierItem2
	def samplePoId
	def sampleUser
	def constantService
	def poIdService

    protected void setUp() {
        super.setUp()
		controller.metaClass.message={Map map-> map}
		sampleProduct1=new Product(
			addedDescription:"1",
			identifier:"product1",
			runningCost: new BigDecimal("1.23"),
			runningCostInForeignCurrency: new BigDecimal("9.87"))
		sampleProduct2=new Product(
			addedDescription:"2",
			identifier:"product2",
			runningCost: new BigDecimal("2.34"),
			runningCostInForeignCurrency: new BigDecimal("8.76"))
		mockDomain(Product,[sampleProduct1,sampleProduct2])
		
		sampleUser=new User(id:1,
			userRealName:"test")
		mockDomain(User, [sampleUser])
		
		sampleCountry=new Country(
			identifier:"USA",
			description:"us")
		mockDomain(Country,[sampleCountry])
		
		sampleCurrency= new CurrencyType(
			identifier:"USD",
			description:"dollars")
		mockDomain(CurrencyType,[sampleCurrency])
		
		sampleSupplierItem1=new SupplierItem(
			product:sampleProduct1,
			productCode:"1234")
		sampleSupplierItem2=new SupplierItem(
			product:sampleProduct2,
			productCode:"2345")
		mockDomain(SupplierItem,[sampleSupplierItem1,sampleSupplierItem2])

		
		sampleSupplier= new Supplier(
			identifier:"supplier",
			name:"supp",
			currency:sampleCurrency,
			country: sampleCountry,
			items:[sampleSupplierItem1,sampleSupplierItem2])
		mockDomain(Supplier,[sampleSupplier])

		samplePo=new PurchaseOrder(
			supplier: sampleSupplier,
			currency: sampleCurrency,
			year: 2010,
			counterId: 1,
			items:[new PurchaseOrderItem(product:sampleProduct1,qty:new BigDecimal("1"),price:new BigDecimal("2.34"),finalPrice:new BigDecimal("3.45")),new PurchaseOrderItem(product:sampleProduct2,qty:new BigDecimal("2"),price:new BigDecimal("1.23"),finalPrice:new BigDecimal("2.34"))])
		mockDomain(PurchaseOrder,[samplePo])	
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testSaveSuccess(){
		def controller = new PurchaseOrderController()
		controller.metaClass.message={Map map-> map}
		mockParams."supplier.id"=sampleSupplier.id
		mockRequest.addParameter "itemId", sampleSupplierItem1.id.toString()
		mockRequest.addParameter "itemId", sampleSupplierItem2.id.toString()
		mockParams."finalPrice${sampleSupplierItem1.id}"=2.00
		mockParams."finalPrice${sampleSupplierItem2.id}"=4.00
		mockParams."finalQty${sampleSupplierItem1.id}"=1
		mockParams."finalQty${sampleSupplierItem2.id}"=2
		mockParams.discountRate = new BigDecimal("0")
		mockParams.supplier=sampleSupplier
		mockParams.currency=sampleCurrency
		mockParams."itemList[0]" = new PurchaseOrderItem(product: sampleProduct1)
		
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.save()
		assertEquals "show", redirectArgs.action
	}
	void testSaveFail(){
		def controller = new PurchaseOrderController()
		controller.metaClass.message={Map map-> map}
		mockParams."supplier.id"=sampleSupplier.id
		mockRequest.addParameter "itemId", sampleSupplierItem1.id.toString()
		mockRequest.addParameter "itemId", sampleSupplierItem2.id.toString()
		mockParams."finalPrice${sampleSupplierItem1.id}"=2.00
		mockParams."finalPrice${sampleSupplierItem2.id}"=4.00
		mockParams."finalQty${sampleSupplierItem1.id}"=1
		mockParams."finalQty${sampleSupplierItem2.id}"=2
		mockParams.supplier=null
		mockParams.currency=sampleCurrency
		mockParams.discountRate="10"
		
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.save()
		assertEquals "create", renderArgs.view
	}
	void testUpdateCreate(){
		mockParams.selectedValue=1
		def mockAuthenticate = mockFor(AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){-> 
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.updateCreate()
		assertEquals '{"supplier":"supp","preparedBy":"'+FormatUtil.createTimeStamp(sampleUser)+'","supplierId":1,"country":"us","currency":"USD","inventory":[{"description":"1","itemId":1,"productCode":"1234","name":"product1","cost":9.87},{"description":"2","itemId":2,"productCode":"2345","name":"product2","cost":8.76}]}', controller.response.contentAsString
	}
	void testGetItems(){
		mockParams.selectedValue=1
		controller.getItems()
		assertEquals '{"inventory":[{"description":"1","itemId":1,"productCode":"1234","name":"product1","cost":9.87,"qty":1,"finalPrice":3.45},{"description":"2","itemId":2,"productCode":"2345","name":"product2","cost":8.76,"qty":2,"finalPrice":2.34}],"availableItems":[],"allItems":[{"description":"1","itemId":1,"productCode":"1234","name":"product1","cost":9.87},{"description":"2","itemId":2,"productCode":"2345","name":"product2","cost":8.76}],"currency":"USD"}', controller.response.contentAsString
	}
	void testUpdateSave(){
		mockParams.id=1
		mockRequest.addParameter "itemId", sampleSupplierItem1.id.toString()
		mockRequest.addParameter "itemId", sampleSupplierItem2.id.toString()
		mockParams.discountRate = new BigDecimal("0")
		def mockPoService = mockFor(PurchaseOrderService)
		mockPoService.demand.updateValues(1..1){->
			return "purchase order instance"
		}
		controller.purchaseOrderService= mockPoService.createMock()
		controller.update()
		assertEquals "show", redirectArgs.action
	}
	void testUpdateWithError(){
		mockParams.id=1
		mockRequest.addParameter "itemId", sampleSupplierItem1.id.toString()
		mockRequest.addParameter "itemId", sampleSupplierItem2.id.toString()
		mockParams.discountRate = new BigDecimal("0")
		def mockPoService = mockFor(PurchaseOrderService)
		mockPoService.demand.updateValues(1..1){->
			return "purchase order instance"
		}
		controller.purchaseOrderService= mockPoService.createMock()
		samplePo.supplier=null
		controller.update()
		assertEquals "edit", renderArgs.view
	}
	void testUpdateNoneExist(){
		mockParams.id=2
		controller.update()
		assertEquals "list", redirectArgs.action
	}
	void testUnCancel(){
		mockParams.id=1
		def mockConsService = mockFor(ConstantService)
		controller.constantService= mockConsService.createMock()
		controller.unCancel()
		assertEquals "Unapproved", samplePo.status
	}
	void testCancelUnapprovedPO(){
		mockParams.id=1
		def mockConsService = mockFor(ConstantService)
		controller.constantService= mockConsService.createMock()
		controller.cancel()
		assertEquals "Cancelled", samplePo.status
	}
	void testCancelApprovedPO(){
		mockParams.id=1
		samplePo.approve()
		def mockConsService = mockFor(ConstantService)
		controller.constantService= mockConsService.createMock()
		controller.cancel()
		assertEquals "Sample purchase order should not be cancelled", "Approved", samplePo.status
	}
	void testApproved(){
		mockParams.id=1
		def mockPoService = mockFor(PurchaseOrderService)
		mockPoService.demand.updateValues(1..1){->
			return "purchase order instance"
		}
		controller.purchaseOrderService= mockPoService.createMock()
		def mockConsService = mockFor(ConstantService)
		controller.constantService= mockConsService.createMock()
		def mockAuthenticate = mockFor(AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){-> 
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.approve()
		assertEquals "Approved", samplePo.status
	}
	void testMarkAsComplete(){
		mockParams.id=1
		def mockConsService = mockFor(ConstantService)
		controller.constantService= mockConsService.createMock()
		def mockAuthenticate = mockFor(AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.markAsComplete()
		assertEquals "Complete", samplePo.status
	}
	void testUnapprovedSuccessful(){
		mockParams.id=1
		def mockConsService = mockFor(ConstantService)
		controller.constantService= mockConsService.createMock()
        def mockPurchaseOrderService = mockFor(PurchaseOrderService)
		mockPurchaseOrderService.demand.isUnapprovable(1..1){params->
			return true
		}
		controller.purchaseOrderService = mockPurchaseOrderService.createMock()
		controller.unapprove()
		assertEquals "Unapproved", samplePo.status
        assertEquals "Purchase Order has been successfully unapproved!", controller.flash.message
	}
	void testUnapprovedUnsuccessful(){
		mockParams.id=1
        def mockPurchaseOrderService = mockFor(PurchaseOrderService)
		mockPurchaseOrderService.demand.isUnapprovable(1..1){params->
			return false
		}
		controller.purchaseOrderService = mockPurchaseOrderService.createMock()
		controller.unapprove()
        assertEquals "Purchase Order cannot be unapproved since there is a purchase invoice associated!", controller.flash.error
	}

	void testList() {
		def purchaseOrder = new PurchaseOrder()
		def mockPurchaseOrderService = mockFor(PurchaseOrderService)
		mockPurchaseOrderService.demand.getPurchaseOrderList(1..1){params->
			return [purchaseOrders:[purchaseOrder],purchaseOrdersTotal:1]
		}
		mockPurchaseOrderService.demand.generateDateStructList(1..1){x->
			return [:]
		}
		controller.purchaseOrderService = mockPurchaseOrderService.createMock()
		
		def model = controller.list()
		
		assertNotNull "Should have purchaseOrderInstanceList attribute", model["purchaseOrderInstanceList"]
		assertTrue "Should contain purchaseOrder returned by mock", model["purchaseOrderInstanceList"].contains(purchaseOrder)
		assertNotNull "Should have purchaseOrderInstanceTotal attribute", model["purchaseOrderInstanceTotal"]
		assertEquals "Should return 1 purchase order", 1, model["purchaseOrderInstanceTotal"]
	}
	
	void testUpdateCompleteWithPo(){
		mockParams.id=1
		def mockPoService = mockFor(PurchaseOrderService)
		mockPoService.demand.updateComplete(1..1){->
			return "hi"
		}
		controller.purchaseOrderService= mockPoService.createMock()
		controller.updateCompleteStatus()
		assertEquals "show", redirectArgs.action
	}
	void testUpdateCompleteWithoutPo(){
		mockParams.id=3
		controller.updateCompleteStatus()
		assertEquals "list", redirectArgs.action
	}
	void testDoPrint() {
		mockParams.id=1
		def mockAuthenticate = mockFor(AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.doPrint()
		assertEquals "purchaseOrder", redirectArgs.action
	}
	void testDoPrintNoPrice() {
		mockParams.id=1
		def mockAuthenticate = mockFor(AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.doPrintNoPrice()
		assertEquals "purchaseOrderNoPrice", redirectArgs.action
	}
}