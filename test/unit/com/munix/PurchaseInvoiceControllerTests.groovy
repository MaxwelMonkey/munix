package com.munix

import grails.test.*
import grails.converters.JSON

import org.grails.plugins.springsecurity.service.AuthenticateService

class PurchaseInvoiceControllerTests extends ControllerUnitTestCase {
	def generalMethodService = new GeneralMethodService() 
	def mockPurchaseInvoiceService
	def mockStockCardService
	
	def sampleSupplier
	def sampleWarehouse
	def sampleStock
	def samplePurchaseOrderItem
	def sampleProduct
	def samplePurchaseInvoice
	def samplePurchaseInvoiceItem
	
	protected void setUp() {
		super.setUp()
		controller.metaClass.message = {Map map ->
			return map.code
		}
		
		sampleSupplier = new Supplier(identifier: "supp")
		mockDomain(Supplier,[sampleSupplier])

		def samplePurchaseOrder = new PurchaseOrder(
			year: "2012",
			counterId: 1,
			supplier: sampleSupplier,
			currency : new CurrencyType()
		)
		mockDomain(PurchaseOrder, [samplePurchaseOrder])
	
		sampleProduct = new Product(
			id: 1,
			identifier: "product"
		)
		mockDomain(Product, [sampleProduct])
		sampleProduct.metaClass.'static'.formatDescription = {-> return "description"}
		
		sampleWarehouse = new Warehouse(
			identifier : "BULL",
			description : "bull"
		)
		mockDomain(Warehouse, [sampleWarehouse])

		sampleStock = new Stock(
			product: sampleProduct,
			warehouse: sampleWarehouse
		)
		mockDomain(Stock, [sampleStock])
		
		sampleProduct.stocks = [sampleStock]
				
		samplePurchaseOrderItem = new PurchaseOrderItem(
			id: 1,
			qty : new BigDecimal("3"), 
			product : sampleProduct,
			price: BigDecimal.ONE,
			finalPrice: BigDecimal.ONE,
			receivedQty: BigDecimal.ZERO,
			po: samplePurchaseOrder
		)
		mockDomain(PurchaseOrderItem, [samplePurchaseOrderItem])
		
		samplePurchaseOrder.items = [samplePurchaseOrderItem]
	
		samplePurchaseInvoice = new PurchaseInvoice(
			warehouse: sampleWarehouse,
			deliveryDate: generalMethodService.createDate("01/01/2010"),
			reference: "1",
			supplier: sampleSupplier,
			type: "bike"
		)
		mockDomain(PurchaseInvoice, [samplePurchaseInvoice])
		
		samplePurchaseInvoiceItem = new PurchaseInvoiceItem(
			qty: new BigDecimal("3"),
			purchaseInvoice: samplePurchaseInvoice,
			purchaseOrderItem: samplePurchaseOrderItem
		)
		mockDomain(PurchaseInvoiceItem, [samplePurchaseInvoiceItem])
		
		samplePurchaseInvoice.items = [samplePurchaseInvoiceItem] 
		
		mockPurchaseInvoiceService = mockFor(PurchaseInvoiceService)
	}

	protected void tearDown() {
		super.tearDown()
		
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove Product
	}

	void testFilter() {
		mockDomain(Supplier)

		controller.filter()
	}

	void testSavePurchaseInvoiceSuccess(){
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1){->
			return new User(username : "user")
		}
		controller.authenticateService = mockAuthenticateService.createMock()
		mockPurchaseInvoiceService.demand.processInvoiceItems(1..1){->
			return null
		}
		controller.purchaseInvoiceService = mockPurchaseInvoiceService.createMock()		
		
		mockParams."supplier.id" = sampleSupplier.id
		mockParams.reference = "123"
		mockParams.supplierReference = "123"
		mockParams.invoiceDate = "01/01/2001"
		mockParams.exchangeRate = "1"
		mockParams.deliveryDate = "01/01/2001"
		mockParams.remark = "remarkable"
		mockParams."warehouse.id" = sampleWarehouse.id
		mockParams."invoiceItemList[0].qty" = "10"
		mockParams."invoiceItemList[0].finalPrice" = "10"
		mockParams."invoiceItemList[0].purchaseOrderItem.id" = samplePurchaseOrderItem.id
		mockParams."productId" = sampleProduct.id

		controller.save()

		assertEquals "should redirect to show", "show", controller.redirectArgs.action
		assertEquals "wrong flash message", "default.created.message", controller.flash.message
		assertNotNull controller.redirectArgs.id
	}

	void testSavePurchaseInvoiceFail(){
		controller.params."supplier.id" = sampleSupplier.id
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1){->
			mockDomain(User)
			def user = new User(username : "user")
			return user
		}
		controller.authenticateService = mockAuthenticateService.createMock()

		controller.save()

		assertEquals "should redirect to create", "create", controller.renderArgs.view
		assertNotNull "should have purchaseInvoiceInstance object", controller.renderArgs.model.purchaseInvoiceInstance
	}

	void testUpdateSuccess(){
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1){->
			return new User(username : "user")
		}
		controller.authenticateService = mockAuthenticateService.createMock()
		mockPurchaseInvoiceService.demand.processInvoiceItems(1..1){->
			return null
		}
		controller.purchaseInvoiceService = mockPurchaseInvoiceService.createMock()		
		
		mockParams.id = samplePurchaseInvoice.id
		mockParams."supplier.id" = sampleSupplier.id
		mockParams.type = "type"
		mockParams.reference = "123"
		mockParams.supplierReference = "123"
		mockParams.invoiceDate = "01/01/2001"
		mockParams.exchangeRate = "1"
		mockParams.deliveryDate = "01/01/2001"
		mockParams.remark = "remarkable"
		mockParams."warehouse.id" = sampleWarehouse.id
		mockParams."invoiceItemList[0].qty" = "10"
		mockParams."invoiceItemList[0].finalPrice" = "10"
		mockParams."invoiceItemList[0].purchaseOrderItem.id" = samplePurchaseOrderItem.id
		mockParams."productId" = sampleProduct.id
		
		controller.update()

		assertEquals "show", controller.redirectArgs.action
		assertEquals "default.updated.message", controller.flash.message
	}

	void testUpdateFail(){
		controller.update()
		
		assertEquals "list", controller.redirectArgs.action
	}

	void testCreateWithSupplierFail(){
		controller.params.id = 1

		def purchaseInvoiceInstance = controller.create()
		
		assertEquals "Wrong message", "Supplier chosen doesn't have any approved POs!", controller.flash.message
		assertNotNull "there should a new instance of purchase invoice returned", purchaseInvoiceInstance
	}

	void testCreateNoSupplier(){
		def purchaseInvoiceInstance = controller.create()
		
		assertNotNull "there should a new instance of purchase invoice returned", purchaseInvoiceInstance
	}
	
	void testApprove() {
		mockParams.id = samplePurchaseInvoice.id
		mockPurchaseInvoiceService.demand.approve(1..1){x ->
			return true
		}
		controller.purchaseInvoiceService = mockPurchaseInvoiceService.createMock()
		
		controller.approve()
		
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
	}
	
	void testUnapprove() {
		mockParams.id = samplePurchaseInvoice.id
		mockPurchaseInvoiceService.demand.unapprove(1..1){x ->
			return true
		}
		controller.purchaseInvoiceService = mockPurchaseInvoiceService.createMock()
		
		controller.unapprove()
		
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
	}
	
	void testRetrievePurchaseOrderItems() {
		mockParams.supplierId = sampleSupplier.id
		mockParams.sSearch = "product"
		mockPurchaseInvoiceService.demand.queryAvailablePurchaseOrderItems(1..1){x, y ->
			return new HashSet([samplePurchaseOrderItem])
		}
		controller.purchaseInvoiceService = mockPurchaseInvoiceService.createMock()
		
		def data = [["supp-2012-00001", "product-description", new BigDecimal("3"), new BigDecimal("3"), BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO, 1, 1]]
		def expectedJsonResponse = [iTotalRecords: 1, iTotalDisplayRecords: 1, aaData: data]
		
		controller.retrievePurchaseOrderItems()
		
		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)
		
		assertEquals "json total Records should be correct", expectedJsonResponse.iTotalRecords, jsonResult.iTotalRecords
		assertEquals "json total display Records should be correct", expectedJsonResponse.iTotalDisplayRecords, jsonResult.iTotalDisplayRecords
		assertEquals "json data should be correct", expectedJsonResponse.aaData, jsonResult.aaData
	}
	
	void testRetrievePurchaseOrderItemsNoSupplierId() {
		controller.retrievePurchaseOrderItems()
		
		assertEquals "should redirect to filter", "filter", controller.redirectArgs.action
	}
}
