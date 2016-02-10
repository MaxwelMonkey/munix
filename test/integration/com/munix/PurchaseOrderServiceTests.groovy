package com.munix

import static org.junit.Assert.*;
import grails.test.*

class PurchaseOrderServiceTests extends GrailsUnitTestCase {
	def purchaseOrderService
	def sampleDiscountType
	def sampleProduct1
	def sampleProduct2
	def sampleCountry
	def sampleCurrency
	def sampleSupplier
	def samplePurchaseOrder
	def sampleSupplierItem1
	def sampleSupplierItem2
	def samplePurchaseOrderId
	def samplePurchaseOrderItem
	def sampleWarehouse
	def samplePurchaseInvoice
	def samplePurchaseInvoiceItem

    protected void setUp() {
        super.setUp()
		setupDiscountType()
		setUpProducts()
		setUpCountries()
		setUpCurrencies()
		setUpSupplierItems()		
		setUpSuppliers()
		setUpSupplierAndSupplierItemRelationship()
		setUpPurchaseOrders()
		setUpPurchaseOrderItem()
        setUpWarehouses()
        setUpPurchaseInvoices()
        setUpPurchaseInvoiceItems()
    }
    private void setUpPurchaseInvoiceItems() {
		samplePurchaseInvoiceItem = new PurchaseInvoiceItem(
			qty: new BigDecimal("10"),
			purchaseOrderItem: samplePurchaseOrderItem
		)
		samplePurchaseInvoiceItem.save(flush: true)
        samplePurchaseOrderItem.addToReceivedItems(samplePurchaseInvoiceItem)
        samplePurchaseOrder.save()
        samplePurchaseInvoice.addToItems(samplePurchaseInvoiceItem)
        samplePurchaseInvoice.save()
	}
    private void setUpPurchaseInvoices() {
		samplePurchaseInvoice = new PurchaseInvoice(
			supplier: sampleSupplier,
			reference: "1",
			warehouse: sampleWarehouse,
            deliveryDate: new Date(),
            type:"PI"
		)
		samplePurchaseInvoice.save(flush: true)
	}
    private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(
			identifier: "identifier",
			description: "desc"
		)
		sampleWarehouse.save(flush: true)
	}
	
	private void setupDiscountType() {
		sampleDiscountType = new DiscountType(
			identifier: "discount type",
			description: "desc"
		)
		sampleDiscountType.save()
	}
	
	private void setUpProducts() {
		sampleProduct1 = new Product(
			identifier: "product1",
			type: sampleDiscountType
		)
		sampleProduct1.save()
		
		sampleProduct2 = new Product(
			identifier: "product2",
			type: sampleDiscountType
		)
		sampleProduct2.save()
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
	
	private setUpSupplierItems() {
		sampleSupplierItem1 = new SupplierItem(
				product: sampleProduct1,
				productCode: "1234",
				cost: new BigDecimal("1.23"),
                status:SupplierItem.Status.ACTIVE)

		sampleSupplierItem2 = new SupplierItem(
				product: sampleProduct2,
				productCode: "2345",
				cost: new BigDecimal("2.34"),
                status:SupplierItem.Status.ACTIVE)
	}
	
	private setUpSuppliers() {
		sampleSupplier = new Supplier(
				identifier: "supplier",
				name: "Mr. Merchant Boy",
				currency: sampleCurrency,
				country: sampleCountry)
		sampleSupplier.save()
	}
	
	private setUpSupplierAndSupplierItemRelationship() {
		sampleSupplier.supplierItemList[0] = sampleSupplierItem1
		sampleSupplier.supplierItemList[1] = sampleSupplierItem2
		sampleSupplier.save()
		
		sampleSupplierItem1.supplier = sampleSupplier
		sampleSupplierItem1.save()

		sampleSupplierItem2.supplier = sampleSupplier
		sampleSupplierItem2.save()
	}
	
	private setUpPurchaseOrders() {
		samplePurchaseOrder = new PurchaseOrder(
			date: createDate(2010, 1, 1),
			supplier: sampleSupplier,
			currency: sampleCurrency,
			year: 2010,
			counterId: 1,
			items: [samplePurchaseOrderItem]
		)
		samplePurchaseOrder.save()
	}
	private setUpPurchaseOrderItem(){
        samplePurchaseOrderItem = new PurchaseOrderItem(
                product:sampleProduct1,
                qty:BigDecimal.ONE,
                price:BigDecimal.ONE,
                finalPrice:BigDecimal.ONE,
                isComplete:false,
				po: samplePurchaseOrder
        )
        samplePurchaseOrder.addToItems(samplePurchaseOrderItem)
        samplePurchaseOrder.save()
    }
	private Date createDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance()
		calendar.set(year, month - 1, day)
		return calendar.getTime()
	}
	
    protected void tearDown() {
        super.tearDown()
        tearDownPurchaseOrders()
        tearDownSuppliers()
        tearDownSupplierItems()
        tearDownCurrencies()
        tearDownCountries()
		tearDownProducts()
    }
    	
    private void tearDownPurchaseOrders() {
    	samplePurchaseOrder.delete()
    }
    
    private void tearDownSuppliers() {
    	sampleSupplier.delete()
    }

	private void tearDownSupplierItems() {
		sampleSupplierItem1.delete()
		sampleSupplierItem2.delete()
	}
	
	private void tearDownCurrencies() {
		sampleCurrency.delete()
	}
	
	private void tearDownCountries() {
		sampleCountry.delete()
	}	
	
	private void tearDownProducts() {
		sampleProduct1.delete()
		sampleProduct2.delete()
	}
	
	void testGetPurchaseOrderList() {
		def object=[:]
		
		def purchaseOrderList = purchaseOrderService.getPurchaseOrderList(object)
		
		assertEquals 1,purchaseOrderList.purchaseOrders.size()
		assertTrue purchaseOrderList.purchaseOrders.contains(samplePurchaseOrder)
	}
	
	void testGetPurchaseOrderListPoIdExists() {
		def object=["poId": "1"]

		def purchaseOrderList = purchaseOrderService.getPurchaseOrderList(object)
		
		assertEquals 1,purchaseOrderList.purchaseOrders.size()
		assertTrue purchaseOrderList.purchaseOrders.contains(samplePurchaseOrder)
	}
	
	void testGetPurchaseOrderListPoIdDoesNotExist() {
		def object=["poId": "tests"]

		def purchaseOrderList = purchaseOrderService.getPurchaseOrderList(object)
		
		assertEquals 0,purchaseOrderList.purchaseOrders.size()
		assertTrue purchaseOrderList.purchaseOrders.isEmpty()
	}
	
	void testGetPurchaseOrderListStartDateExistsWithNoEndDate() {
		def object=["startDate_value": "01/01/2000"]

		def purchaseOrderList = purchaseOrderService.getPurchaseOrderList(object)
		
		assertEquals 1,purchaseOrderList.purchaseOrders.size()
		assertTrue purchaseOrderList.purchaseOrders.contains(samplePurchaseOrder)
	}
	
	void testGetPurchaseOrderListEndDateExistsWithNoStartDate() {
		def object=["endDate_value": "01/01/2011"]
		
		def purchaseOrderList = purchaseOrderService.getPurchaseOrderList(object)
		
		assertEquals 1,purchaseOrderList.purchaseOrders.size()
		assertTrue purchaseOrderList.purchaseOrders.contains(samplePurchaseOrder)
	}

	void testGetPurchaseOrderListStartDateAndEndDateBothExist() {
		def object= [
			"startDate_value": "01/01/2000",
			"endDate_value": "01/01/2011"]
		
		def purchaseOrderList = purchaseOrderService.getPurchaseOrderList(object)
		
		assertEquals 1,purchaseOrderList.purchaseOrders.size()
		assertTrue purchaseOrderList.purchaseOrders.contains(samplePurchaseOrder)
	}

    void testIsUnapprovableWithPI(){
        def results = purchaseOrderService.isUnapprovable(samplePurchaseOrder)
        assertFalse "the result must be false",results
    }

    void testIsUnapprovableWithoutPI(){
        samplePurchaseOrder.items = []
        assertTrue "GUARD items must be empty", samplePurchaseOrder.items.isEmpty()

        def results = purchaseOrderService.isUnapprovable(samplePurchaseOrder)
        assertTrue "the result must be false",results
    }
}