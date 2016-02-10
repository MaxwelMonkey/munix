package com.munix

import grails.test.*

class SupplierPaymentServiceTests extends GroovyTestCase {
	def sampleSupplierPayment
    def sampleSupplier
    def sampleCountry
    def sampleCurrency
    def samplePurchaseOrder
    def samplePurchaseInvoice
	def samplePurchaseInvoice2
	def sampleDiscountType
    def sampleProduct1
    def samplePurchaseOrderItem
    def samplePurchaseInvoiceItem
	def sampleWarehouse

	def supplierPaymentService

    protected void setUp() {
        super.setUp()
		setUpSupplierPayments()
        setUpCountries()
        setUpCurrencies()
        setUpSuppliers()
		setUpWarehouses()
        setUpPurchaseOrders()
		setUpDiscountTypes()
        setUpProducts()
        setUpPurchaseOrderItem()
        setUpPurchaseInvoiceItem()
        setUpPurchaseInvoice()
        setUpPurchaseInvoiceAndPurchaseInvoiceItemRelationship()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testAvailablePurchaseInvoice() {
		def purchaseInvoiceList = supplierPaymentService.availablePurchaseInvoices(sampleSupplier)
		assertTrue "PurchaseInvoiceList must contain samplePurchaseInvoice", purchaseInvoiceList.contains(samplePurchaseInvoice)
	}
	
	void testUpdatePurchaseInvoiceFromSupplierPaymentRemoveAll() {
		assertNull "The purchase invoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices
		sampleSupplierPayment.addToPurchaseInvoices(samplePurchaseInvoice)
		assertFalse "The purchase invoices for supplier payment is empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
		
		sampleSupplierPayment = supplierPaymentService.updatePurchaseInvoiceFromSupplierPayment(sampleSupplierPayment, [])
		
		assertTrue "The purchase invoices list is not empty", sampleSupplierPayment.purchaseInvoices.isEmpty()
	}
	
	void testUpdatePurchaseInvoiceFromSupplierPaymentChangeInvoice() {
		assertNull "The purchase invoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices
		sampleSupplierPayment.addToPurchaseInvoices(samplePurchaseInvoice)
		
		sampleSupplierPayment = supplierPaymentService.updatePurchaseInvoiceFromSupplierPayment(sampleSupplierPayment, [samplePurchaseInvoice.id])
		
		assertTrue "The invoice list does not contain the expected result", sampleSupplierPayment.purchaseInvoices.contains(samplePurchaseInvoice)
		assertEquals "The invoice list contains more than the expected result", 1, sampleSupplierPayment.purchaseInvoices.size()
	}
	
	void testUpdatePurchaseInvoiceFromSupplierPaymentAddInvoice() {
		assertNull "The purchase invoices for supplier payment is not empty", sampleSupplierPayment.purchaseInvoices
		sampleSupplierPayment.addToPurchaseInvoices(samplePurchaseInvoice)

		sampleSupplierPayment = supplierPaymentService.updatePurchaseInvoiceFromSupplierPayment(sampleSupplierPayment, [samplePurchaseInvoice.id, samplePurchaseInvoice2.id])
	
		assertTrue "The invoice list does not contain the expected result", sampleSupplierPayment.purchaseInvoices.contains(samplePurchaseInvoice)
		assertTrue "The invoice list does not contain the expected result", sampleSupplierPayment.purchaseInvoices.contains(samplePurchaseInvoice2)
		assertEquals "The invoice list contains more than the expected result", 2, sampleSupplierPayment.purchaseInvoices.size()
	}

	private void setUpSupplierPayments() {
		sampleSupplierPayment = new SupplierPayment(
			supplier: sampleSupplier,
			preparedBy: "me"
		)
		sampleSupplierPayment.save(flush: true)
	}
	
	private void setUpDiscountTypes() {
		sampleDiscountType=new DiscountType(identifier:"discountType",
				description:"discount type", margin: BigDecimal.ONE)
		sampleDiscountType.save()
	}
	
    private void setUpProducts() {
		sampleProduct1 = new Product(
			identifier: "product1",
			type: sampleDiscountType
		)
		sampleProduct1.save()
    }
	
    private setUpSuppliers() {
		sampleSupplier = new Supplier(
				identifier: "supplier",
				name: "Mr. Merchant Boy",
				currency: sampleCurrency,
				country: sampleCountry)
		sampleSupplier.save()
	}
	
    private setUpCountries() {
		sampleCountry = new Country(
				identifier: "USA",
				description: "United States of America")
		sampleCountry.save()
	}

	private setUpCurrencies() {
		sampleCurrency = new CurrencyType(
				identifier: "USD",
				description: "dollars")
		sampleCurrency.save()
	}
	
	private setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier: "warehouse", description: "sample")
		sampleWarehouse.save()
	}
	
    private setUpPurchaseOrders() {
		samplePurchaseOrder = new PurchaseOrder(
				supplier: sampleSupplier,
				currency: sampleCurrency,
				year: 2010,
				counterId: 1,
				items: [samplePurchaseOrderItem])
		samplePurchaseOrder.save()
	}

	private setUpPurchaseInvoiceItem(){
		samplePurchaseInvoiceItem = new PurchaseInvoiceItem(
			purchaseOrderItem: samplePurchaseOrderItem,
			qty:BigDecimal.ONE
		)
		samplePurchaseOrderItem.receivedItems[0]=samplePurchaseInvoiceItem
		samplePurchaseInvoiceItem.save()
		samplePurchaseOrderItem.save()
	}
	
    private setUpPurchaseInvoice(){
        samplePurchaseInvoice = new PurchaseInvoice(
            supplier: sampleSupplier,
            reference: "hello",
            status: PurchaseInvoice.Status.APPROVED,
            type:"Local",
            deliveryDate: new Date(),
			warehouse: sampleWarehouse,
			items: [samplePurchaseInvoiceItem]
        )
        samplePurchaseInvoice.save()
		
		samplePurchaseInvoice2 = new PurchaseInvoice(
			supplier: sampleSupplier,
			reference: "hello2",
			status: PurchaseInvoice.Status.APPROVED,
			type:"Local",
			deliveryDate: new Date(),
			warehouse: sampleWarehouse,
			items: [samplePurchaseInvoiceItem]
		)
		samplePurchaseInvoice2.save()
    }

    private setUpPurchaseOrderItem(){
        samplePurchaseOrderItem = new PurchaseOrderItem(
            product:sampleProduct1,
            qty:BigDecimal.ONE,
            price:BigDecimal.ZERO,
            finalPrice:BigDecimal.ZERO,
			po: samplePurchaseOrder
        )
		samplePurchaseOrder.addToItems(samplePurchaseOrderItem)
        samplePurchaseOrderItem.save()
    }
	
   
    private setUpPurchaseInvoiceAndPurchaseInvoiceItemRelationship() {
		samplePurchaseInvoice.items[0]=samplePurchaseInvoiceItem
        samplePurchaseInvoice.save()
        println samplePurchaseInvoice.errors
	}
}
