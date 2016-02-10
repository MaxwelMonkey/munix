package com.munix

import grails.test.*
import java.math.RoundingMode;

class StockCostHistoryServiceTests extends GrailsUnitTestCase {
	def stockCostHistoryService
	def generalMethodService

	def sampleDiscountType
	def sampleProduct
	def sampleWarehouse
	def sampleCurrencyType
	def sampleSupplier
	def samplePurchaseOrder
	def samplePurchaseOrderItem1
	def samplePurchaseOrderItem2
	def samplePurchaseInvoice
	def samplePurchaseInvoiceItem1
	def samplePurchaseInvoiceItem2
	def sampleAssembler
	def sampleJobOrder
	def sampleJobOut1
	def sampleJobOut2
	
    protected void setUp() {
        super.setUp()
	
		setUpDiscountTypes()	
		setUpProducts()
		setUpWarehouses()
		setUpCurrencyTypes()
		setUpSuppliers()
		setUpPurchaseOrders()
		setUpPurhaseOrderItems()
		setUpPurchaseInvoiceItems()
		setUpPurhaseOrderAndPurchaseInvoiceRelationship()
		setUpPurchaseInvoices()
		setUpPurchaseInvoiceAndPurchaseInvoiceItemRelationship()
		
		setUpAssemblers()
		setUpJobOrders() 
		setUpJobOuts()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerateStockCostItems() {
		def result = stockCostHistoryService.generateStockCostItems(sampleProduct)
		def expectedDate = [samplePurchaseInvoice.date, samplePurchaseInvoice.date, sampleJobOut1.date, sampleJobOut2.date]
		def expectedSupplier = [samplePurchaseInvoice.supplier, samplePurchaseInvoice.supplier, null, null]
		def expectedReference = [samplePurchaseInvoice.reference, samplePurchaseInvoice.reference, sampleJobOut1.toString(), sampleJobOut2.toString()]
		def expectedSupplierReference = [samplePurchaseInvoice.supplierReference, samplePurchaseInvoice.supplierReference, null, null]
		def expectedCostForeign = ["USD " + samplePurchaseInvoiceItem1.discountedPrice.setScale(4,RoundingMode.DOWN), "USD " + samplePurchaseInvoiceItem2.discountedPrice.setScale(4,RoundingMode.DOWN), null, null]
		def expectedExchangeRate = [samplePurchaseInvoice.exchangeRate, samplePurchaseInvoice.exchangeRate, null, null]
		def expectedCostLocal = [samplePurchaseInvoiceItem1.localDiscountedPrice, samplePurchaseInvoiceItem1.localDiscountedPrice, BigDecimal.ZERO, BigDecimal.ZERO]
		def expectedQty = [samplePurchaseInvoiceItem1.qty, samplePurchaseInvoiceItem2.qty, sampleJobOut1.qty, sampleJobOut2.qty]
		
		assertNotNull "result should not be null", result
		assertFalse "should have results", result.isEmpty()
		assertEquals "should have correct date", expectedDate, result.date
		assertEquals "should have correct supplier", expectedSupplier, result.supplier

		assertEquals "should have correct reference", expectedReference, result.reference
		assertEquals "should have correct supplier reference", expectedSupplierReference, result.supplierReference
		assertEquals "should have correct foreign cost", expectedCostForeign, result.costForeign
		assertEquals "should have correct exchange rate", expectedExchangeRate, result.exchangeRate
		assertEquals "should have correct local cost", expectedCostLocal, result.costLocal
		assertEquals "should have correct quantity", expectedQty, result.qty
    }
	
	private void setUpDiscountTypes() {
		sampleDiscountType=new DiscountType(identifier:"discountType",
				description:"discount type", margin: BigDecimal.ONE)
		sampleDiscountType.save()
	}
	
	private void setUpProducts() {
		sampleProduct = new Product(
			identifier: "product",
			description: "pro-duct",
			type:sampleDiscountType
		)
		sampleProduct.save(flush: true)
	}
	
	private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(
			identifier: "warehouse",
			description: "BUL"
		)
		sampleWarehouse.save(flush: true)
	}
	
	private void setUpCurrencyTypes() {
		sampleCurrencyType = new CurrencyType(
			identifier: "USD",
			description: "US dollars"
		)
		sampleCurrencyType.save(flush: true)
	}
	
	private void setUpSuppliers() {
		sampleSupplier = new Supplier(
			identifier: "supply",
            name:"supplier",
			currency: sampleCurrencyType
		)
		sampleSupplier.save(flush: true)
	}
	
	private void setUpPurchaseOrders() {
		samplePurchaseOrder = new PurchaseOrder(
			date: generalMethodService.createDate("01/01/2010"),
			supplier: sampleSupplier,
			currency: sampleCurrencyType,
			year: 2010,
			counterId: 1,
			items: [samplePurchaseOrderItem1, samplePurchaseOrderItem2]
		)
		samplePurchaseOrder.validate()
		println samplePurchaseOrder.errors
		samplePurchaseOrder.save()
	}
	
	private void setUpPurhaseOrderItems() {
		samplePurchaseOrderItem1 = new PurchaseOrderItem(
			product: sampleProduct,
			qty: new BigDecimal("10"),
			price: new BigDecimal("10"),
			finalPrice: new BigDecimal("10"),
			po: samplePurchaseOrder
		)
		samplePurchaseOrder.addToItems(samplePurchaseOrderItem1)
		samplePurchaseOrderItem1.save()
		
		samplePurchaseOrderItem2 = new PurchaseOrderItem(
			product: sampleProduct,
			qty: new BigDecimal("10"),
			price: new BigDecimal("10"),
			finalPrice: new BigDecimal("10"),
			po: samplePurchaseOrder
		)
		samplePurchaseOrder.addToItems(samplePurchaseOrderItem2)
		samplePurchaseOrderItem2.save()
	}
	
	
	private void setUpPurchaseInvoiceItems() {
		samplePurchaseInvoiceItem1 = new PurchaseInvoiceItem(
			purchaseOrderItem: samplePurchaseOrderItem1,
			finalPrice: new BigDecimal("10"),
			qty: new BigDecimal("10")
		)
		samplePurchaseInvoiceItem1.save()
		
		samplePurchaseInvoiceItem2 = new PurchaseInvoiceItem(
			purchaseOrderItem: samplePurchaseOrderItem2,
			finalPrice: new BigDecimal("10"),
			qty: new BigDecimal("10")
		)
		samplePurchaseInvoiceItem2.save(flush: true)

	}
	
	private void setUpPurhaseOrderAndPurchaseInvoiceRelationship() {
		samplePurchaseOrderItem1.addToReceivedItems(samplePurchaseInvoiceItem1)
		samplePurchaseInvoiceItem1.save(flush: true)
		samplePurchaseOrderItem2.addToReceivedItems(samplePurchaseInvoiceItem2)
		samplePurchaseInvoiceItem2.save(flush: true)
	}
	
	private void setUpPurchaseInvoices() {
		samplePurchaseInvoice = new PurchaseInvoice(
			date: generalMethodService.createDate("01/01/2010"),
			status: PurchaseInvoice.Status.APPROVED,
			approvedBy: "Stephanie Sioco, Jan. 01, 2010 - 00:00 AM",
			discountRate: new BigDecimal("50"),
			supplier: sampleSupplier,
            supplierReference:"asd",
			reference: "1234",
			warehouse: sampleWarehouse,
			type: "type",
			deliveryDate: generalMethodService.createDate("01/01/2010"),
			exchangeRate: new BigDecimal("42"),
			items: [samplePurchaseInvoiceItem1, samplePurchaseInvoiceItem2]
		)
		samplePurchaseInvoice.save(flush: true)
	}
	
	private void setUpPurchaseInvoiceAndPurchaseInvoiceItemRelationship() {
		samplePurchaseInvoiceItem1.purchaseInvoice = samplePurchaseInvoice
		samplePurchaseInvoiceItem1.save(flush: true)
		samplePurchaseInvoiceItem2.purchaseInvoice = samplePurchaseInvoice
		samplePurchaseInvoiceItem2.save(flush: true)
	}
	
	private void setUpAssemblers() {
		sampleAssembler = new Assembler(
			identifier: "assembler",
			description: "Mang Builder"
		)
		sampleAssembler.save(flush: true)
	}
	
	private void setUpJobOrders() {
		sampleJobOrder = new JobOrder(
			product: sampleProduct,
			startDate: generalMethodService.createDate("01/03/2010"),
			targetDate: generalMethodService.createDate("11/03/2010"),
			qty: new BigDecimal("20"),
			assignedTo: sampleAssembler,
			preparedBy: "me",
			status: JobOrder.Status.JOB_ORDER_APPROVED
		)
		sampleJobOrder.save(flush: true)
	}
	
	private void setUpJobOuts() {
		sampleJobOut1 = new JobOut(
			date: generalMethodService.createDate("01/03/2010"),
			warehouse: sampleWarehouse,
			qty: new BigDecimal("20"),
			preparedBy: "me",
			status: JobOut.Status.APPROVED,
			approvedBy: "Stephanie Sioco, Jan. 03, 2010 - 00:00 AM",
			jobOrder: sampleJobOrder
		)
		sampleJobOut1.save(flush: true)
		
		sampleJobOut2 = new JobOut(
			date: generalMethodService.createDate("01/04/2010"),
			warehouse: sampleWarehouse,
			qty: new BigDecimal("20"),
			preparedBy: "me",
			status: JobOut.Status.APPROVED,
			approvedBy: "Stephanie Sioco, Jan. 04, 2010 - 00:00 AM",
			jobOrder: sampleJobOrder
		)
		sampleJobOut2.save(flush: true)
	}
}
