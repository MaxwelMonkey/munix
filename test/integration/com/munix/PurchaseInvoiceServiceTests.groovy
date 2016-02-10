package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class PurchaseInvoiceServiceTests extends GrailsUnitTestCase {
	def purchaseInvoiceService
	def generalMethodService

	def sampleSupplier
	def sampleCurrencyType
	def samplePurchaseOrder
	def sampleDiscountType
	def sampleProduct
	def sampleProduct2
	def samplePurchaseOrderItem
	def samplePurchaseOrderItem2
	def sampleWarehouse
	def sampleStock
	def sampleStock2
	def sampleSupplierPayment
	def samplePurchaseInvoice
	def samplePurchaseInvoiceItem
	def samplePurchaseInvoiceItemToBeDeleted

	protected void setUp() {
		super.setUp()

		setUpSuppliers()

		setUpCurrencyTypes()
		setUpPurchaseOrders()
		setUpDiscountTypes()
		setUpProducts()
		setUpPurchaseOrderItems()

		setUpWarehouses()
		setUpStocks()
		setUpSupplierPayments()
		setUpPurchaseInvoices()
		setUpPurchaseInvoiceItems()
		setUpPurhaseInvoiceAndPurchaseInvoiceItemsRelationship()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testQueryAvailablePurchaseOrderItems() {
		samplePurchaseOrder.save(flush: true)
		samplePurchaseOrder.approve()
		samplePurchaseOrder.save(flush: true)
		assertNotNull "SamplePurchaseOrderItem's purchase order should not be null", samplePurchaseOrderItem.po
		assertTrue "SamplePurchaseOrderItem's purchase order should be approved", samplePurchaseOrderItem.po.isApproved()
		assertNotNull "The supplier of SamplePurchaseOrderItem's purchase order should not be null", samplePurchaseOrderItem.po.supplier

		def result = purchaseInvoiceService.queryAvailablePurchaseOrderItems(samplePurchaseOrderItem.po.supplier, samplePurchaseOrderItem.product.identifier)

		assertTrue "Result should contain samplePurchaseOrderItem", result.contains(samplePurchaseOrderItem)
	}

	void testProcessInvoiceItems() {
		purchaseInvoiceService.processInvoiceItems(samplePurchaseInvoice)

		assertTrue "Sample purchase invoice item should be added to received items of sample purchase order item", samplePurchaseOrderItem.receivedItems.contains(samplePurchaseInvoiceItem)
		assertFalse "Deleted sample purchase invoice item should not be added to received items of sample purchase order item 2", samplePurchaseOrderItem2.receivedItems.contains(samplePurchaseInvoiceItemToBeDeleted)
	}

	void testGetPurchaseInvoiceItemsWithExceedingQuantityMessage() {
		def result = purchaseInvoiceService.getPurchaseInvoiceItemsWithExceedingQuantityMessage(samplePurchaseInvoice)

		assertEquals "message should be correct", "Invoice Item identifier-2010-00123 - prod1 exceeds ordered quantity.<br/>", result
	}

	void testApprove() {
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1){->
			return new User(username : "user")
		}
		purchaseInvoiceService.authenticateService = mockAuthenticateService.createMock()
		def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createApprovedPurchaseInvoice(1..1){x ->
			return null
		}
		purchaseInvoiceService.stockCardService = mockStockCardService.createMock()
		def originalSamplePurchaseOrderItemReceivedQty = samplePurchaseOrderItem.receivedQty

		purchaseInvoiceService.approve(samplePurchaseInvoice)

		assertEquals "sample purchase invoice should be approved", PurchaseInvoice.Status.APPROVED, samplePurchaseInvoice.status
		assertTrue "sample purchase order item should be complete", samplePurchaseOrderItem.isComplete
		assertEquals "sample purchase invoice item's received quantity should increase", samplePurchaseInvoiceItem.qty + originalSamplePurchaseOrderItemReceivedQty, samplePurchaseOrderItem.receivedQty
		assertEquals "sample stock's quantity should increase", samplePurchaseInvoiceItem.qty, sampleStock.qty
		assertEquals "sample product's running cost should update", new BigDecimal("200"), sampleProduct.runningCost
	}

	void testUnapprove() {
		sampleSupplierPayment.status = SupplierPayment.Status.CANCELLED
		samplePurchaseInvoice.status = PurchaseInvoice.Status.APPROVED
		samplePurchaseInvoice.save(flush: true)
		samplePurchaseOrderItem.isComplete = true
		samplePurchaseOrderItem.save(flush: true)
		sampleStock.qty = samplePurchaseInvoiceItem.qty
		sampleStock.save(flush: true)
		def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createUnapprovedPurchaseInvoice(1..1){x ->
			return null
		}
		purchaseInvoiceService.stockCardService = mockStockCardService.createMock()
		def mockStockCostHistoryService = mockFor(StockCostHistoryService)
		mockStockCostHistoryService.demand.generateStockCostItems(1..100) {x ->
			def stockCostItems = []
			def stockCostItem = [:]
			stockCostItem.costLocal = new BigDecimal("50")
			stockCostItems.add(stockCostItem)
			stockCostItem.costLocal = new BigDecimal("100")
			stockCostItems.add(stockCostItem)
			return stockCostItems
		}
		purchaseInvoiceService.productService.stockCostHistoryService = mockStockCostHistoryService.createMock()

		purchaseInvoiceService.unapprove(samplePurchaseInvoice)

		assertEquals "sample purchase invoice should be unapproved", PurchaseInvoice.Status.UNAPPROVED, samplePurchaseInvoice.status
		assertFalse "sample purchase order item should be incomplete", samplePurchaseOrderItem.isComplete
		assertEquals "sample purchase invoice item's received quantity should decrease", BigDecimal.ZERO, samplePurchaseOrderItem.receivedQty
		assertEquals "sample stock's quantity should decrease", BigDecimal.ZERO, sampleStock.qty
		assertEquals "sample product's running cost should update", new BigDecimal("100"), sampleProduct.runningCost
	}

    void testCancel(){
        assertTrue "Purchase Invoice must be unapproved", samplePurchaseInvoice.isUnapproved()
        def result = purchaseInvoiceService.isCancelable(samplePurchaseInvoice)
        assertTrue "Return value must be true", result
    }
    void testCancelNot(){
        samplePurchaseInvoice.approved()
        samplePurchaseInvoice.save()
        assertTrue "Purchase Invoice must be approved", samplePurchaseInvoice.isApproved()
        def result = purchaseInvoiceService.isCancelable(samplePurchaseInvoice)
        assertFalse "Return value must be false", result
    }
	private void setUpSuppliers() {
		sampleSupplier = new Supplier(identifier: "identifier",
            name:"supplier")
		sampleSupplier.save(flush:true)
	}

	private void setUpCurrencyTypes() {
		sampleCurrencyType = new CurrencyType(
				identifier: "identifier",
				description: "description"
				)
		sampleCurrencyType.save(flush: true)
	}

	private void setUpPurchaseOrders() {
		samplePurchaseOrder = new PurchaseOrder(
			supplier: sampleSupplier,
			currency: sampleCurrencyType,
			year: "2010",
			counterId: 123,
			items: [samplePurchaseOrderItem, samplePurchaseOrderItem2]
		)
		samplePurchaseOrder.save(flush: true)
	}

	private void setUpDiscountTypes() {
		sampleDiscountType=new DiscountType(identifier:"discountType",
				description:"discount type", margin: BigDecimal.ONE)
		sampleDiscountType.save()
	}
	
	private void setUpProducts() {
		sampleProduct = new Product(
			identifier: "prod1",
			type: sampleDiscountType
		)
		sampleProduct.save(flush: true)

		sampleProduct2 = new Product(
			identifier: "prod2",
			type: sampleDiscountType
		)
		sampleProduct2.save(flush: true)
	}

	private void setUpPurchaseOrderItems() {
		samplePurchaseOrderItem = new PurchaseOrderItem(
			product: sampleProduct,
			qty: new BigDecimal("10"),
			price: new BigDecimal("8"),
			finalPrice: new BigDecimal("8"),
			receivedQty: new BigDecimal("10"),
			po: samplePurchaseOrder
		)
		samplePurchaseOrder.addToItems(samplePurchaseOrderItem)
		samplePurchaseOrderItem.save()

		samplePurchaseOrderItem2 = new PurchaseOrderItem(
			product: sampleProduct2,
			qty: new BigDecimal("20"),
			price: new BigDecimal("4"),
			finalPrice: new BigDecimal("8"),
			po: samplePurchaseOrder
		)
		samplePurchaseOrder.addToItems(samplePurchaseOrderItem2)
		samplePurchaseOrderItem2.save()
	}

	private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(
				identifier: "identifier",
				description: "desc"
				)
		sampleWarehouse.save(flush: true)
	}

	private void setUpStocks() {
		sampleStock = new Stock(
				product: sampleProduct,
				warehouse: sampleWarehouse
				)
		sampleStock.save(flush: true)
		sampleProduct.addToStocks(sampleStock)

		sampleStock2 = new Stock(
				product: sampleProduct2,
				warehouse: sampleWarehouse
				)
		sampleStock2.save(flush: true)
		sampleProduct2.addToStocks(sampleStock2)
	}

	private void setUpSupplierPayments() {
		sampleSupplierPayment = new SupplierPayment(
			supplier: sampleSupplier,
			date: new Date(),
			preparedBy: "me"
		)	
		sampleSupplierPayment.save()
	}
	
	private void setUpPurchaseInvoices() {
		samplePurchaseInvoice = new PurchaseInvoice(
			supplier: sampleSupplier,
			reference: "2",
			warehouse: sampleWarehouse,
			deliveryDate: generalMethodService.createDate("01/01/2012"),
			type: "type",
			exchangeRate: new BigDecimal("10"),
			payment: sampleSupplierPayment
		)
		samplePurchaseInvoice.save()
	}

	private void setUpPurchaseInvoiceItems() {
		samplePurchaseInvoiceItem = new PurchaseInvoiceItem(
				qty: new BigDecimal("10"),
				finalPrice: new BigDecimal("20"),
				purchaseOrderItem: samplePurchaseOrderItem
				)
		samplePurchaseInvoiceItem.save()
		samplePurchaseOrderItem.addToReceivedItems(samplePurchaseInvoiceItem)

		samplePurchaseInvoiceItemToBeDeleted = new PurchaseInvoiceItem(
				qty: new BigDecimal("1"),
				purchaseOrderItem: samplePurchaseOrderItem2,
				isDeleted: true
				)
		samplePurchaseInvoiceItemToBeDeleted.save()
		samplePurchaseOrderItem2.addToReceivedItems(samplePurchaseInvoiceItemToBeDeleted)
	}

	private void setUpPurhaseInvoiceAndPurchaseInvoiceItemsRelationship() {
		samplePurchaseInvoice.addToItems(samplePurchaseInvoiceItem)
		samplePurchaseInvoice.addToItems(samplePurchaseInvoiceItemToBeDeleted)
		samplePurchaseInvoice.save(flush: true)
		samplePurchaseInvoiceItem.purchaseInvoice = samplePurchaseInvoice
		samplePurchaseInvoiceItem.save(flush: true)
	}
}
