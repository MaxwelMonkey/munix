package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class SalesDeliveryControllerTests extends ControllerUnitTestCase {
	def sampleNetProduct
	def sampleSalesOrder
	def sampleSalesDelivery
	def sampleSalesDeliveryItem
	def sampleSalesOrderItem
	def sampleWarehouse
	def sampleTerm
	def sampleCustomer
	def sampleUser
	def sampleDirectPaymentInvoice
	
	protected void setUp() {
		super.setUp()
		controller.metaClass.message = {Map map-> map}
		
		sampleUser = new User()
        mockDomain(User,[sampleUser])
		sampleSalesDelivery = new SalesDelivery()
		mockDomain(SalesDelivery, [sampleSalesDelivery])
		
		setupProducts()
		setupWarehouses()
		setupTerms()
		setupCustomers()
		setupSalesOrders()
		setupSalesOrderItem()
		setupSalesDeliveryItems()
		setupSalesDeliveryAndSalesOrderRelationShip()
		setupDirectPaymentInvoice()
	}

	protected void tearDown() {
		super.tearDown()
	}
	
	void testApprove() {
		mockParams.id = sampleSalesDelivery.id
		def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.approveSalesDelivery(1..1) { ->
		return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()

		controller.approve()
		
		assertEquals "Should redirect to show view", "show", redirectArgs.action
		assertEquals "should have correct spiel", "Delivery has been successfully approved!", controller.flash.message
	}

	void testUnapprove() {
		mockParams.id = sampleSalesDelivery.id
		def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.unapproveSalesDelivery(1..1) { ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
				
		controller.unapprove()
		
		assertEquals "Should redirect to show view", "show", redirectArgs.action
		assertTrue controller.flash.message.startsWith("Delivery")
	}

	void testCancel() {
		mockParams.id = sampleSalesDelivery.id
		def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.cancelSalesDelivery(1..1) { ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
		
		controller.cancel()
		
		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}

    void testPrint(){
        mockParams.id = sampleSalesDelivery.id
        mockParams.noPl = "Y"
        assertNull "Print logs must be empty",sampleSalesDelivery.printLogs
        def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.approveSalesDelivery(1..1) { ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
        def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..2) { ->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticateService.createMock()
        controller.print.call()

        assertEquals "Print logs must have 1 item",1,sampleSalesDelivery.printLogs.size()
        sampleSalesDelivery.printLogs.each{
            assertEquals "Print logs type must be Packing List",PrintLogSalesDelivery.Type.INVOICE,it.type
        }
    }
    void testPrintNoPl(){
        mockParams.id = sampleSalesDelivery.id
        assertNull "Print logs must be empty",sampleSalesDelivery.printLogs
        def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.approveSalesDelivery(1..1) { ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
        def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..2) { ->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticateService.createMock()
        controller.print.call()

        assertEquals "Print logs must have 1 item",1,sampleSalesDelivery.printLogs.size()
        sampleSalesDelivery.printLogs.each{
            assertEquals "Print logs type must be Packing List",PrintLogSalesDelivery.Type.PACKINGLIST,it.type
        }
    }
	
	void testSave() {
		mockParams.id = sampleSalesOrder.id
		mockParams.deliveryType = "delivery type"
		mockParams."warehouse.id" = sampleWarehouse.id
		mockParams."deliveryItemList[0]" = sampleSalesDeliveryItem
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticate.createMock()
		def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.saveSalesDelivery(1..1) { x ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
		
		controller.save()
		
		assertEquals "wrong redirect", "show", redirectArgs.action
	}
	
	void testUpdate() {
		mockParams.id = sampleSalesOrder.id
		def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.saveSalesDelivery(1..1) { x ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
		
		controller.update()
		
		assertEquals "wrong redirect", "show", redirectArgs.action
	}
	
	void testIsUnapprovable() {
		mockParams.id = sampleSalesDelivery.id
		def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.unapproveSalesDelivery(1..1) { ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
				
		controller.unapprove()
		
		assertTrue "Sales Delivery should be approvable", sampleSalesDelivery.isUnapprovable()
	}
	
	void testIsUnapprovableNotApprovable() {
		mockParams.id = sampleSalesDelivery.id
		sampleSalesDelivery.addToInvoices(sampleDirectPaymentInvoice)
		
		def mockSalesDeliveryService = mockFor(SalesDeliveryService)
		mockSalesDeliveryService.demand.unapproveSalesDelivery(1..1) { ->
			return true
		}
		controller.salesDeliveryService = mockSalesDeliveryService.createMock()
				
		controller.unapprove()
		
		assertFalse "Sales Delivery should be not approvable", sampleSalesDelivery.isUnapprovable()
	}
	
	private void setupSalesOrders() {
		sampleSalesOrder = new SalesOrder(
			customer: sampleCustomer,
			discount: BigDecimal.ONE
		)
		mockDomain(SalesOrder, [sampleSalesOrder]) 
	}
	
	private void setupCustomers() {
		sampleCustomer = new Customer(
			term: sampleTerm,
			commissionRate: BigDecimal.ZERO
		)
		mockDomain(Customer, [sampleCustomer])
	}
	
	private void setupTerms() {
		sampleTerm = new Term(
			dayValue: BigDecimal.ONE
		)
		mockDomain(Term, [sampleTerm])
	}
	
	private void setupWarehouses() {
		sampleWarehouse = new Warehouse()
		mockDomain(Warehouse, [sampleWarehouse])
	}
	
	private void setupSalesDeliveryItems() {
		sampleSalesDeliveryItem = new SalesDeliveryItem(
			product: sampleNetProduct,
			orderItem: sampleSalesOrderItem,
			qty: new BigDecimal("5"),
			price: new BigDecimal("3")
		)
		mockDomain(SalesDeliveryItem, [sampleSalesDeliveryItem])
	}
	
	private void setupProducts() {
		sampleNetProduct = new Product(
			isNet: true
		)
		mockDomain(Product, [sampleNetProduct])
	}
	
	private void setupSalesOrderItem() {
		sampleSalesOrderItem = new SalesOrderItem(
			isNet: true,
			invoice: sampleSalesOrder
		)
		mockDomain(SalesOrderItem, [sampleSalesOrderItem])
	}
	
	private void setupDirectPaymentInvoice() {
		sampleDirectPaymentInvoice = new DirectPaymentInvoice()
		mockDomain(DirectPaymentInvoice, [sampleDirectPaymentInvoice]) 
	}
    private void setupSalesDeliveryAndSalesOrderRelationShip(){
        sampleSalesDelivery.invoice = sampleSalesOrder
    }
}
