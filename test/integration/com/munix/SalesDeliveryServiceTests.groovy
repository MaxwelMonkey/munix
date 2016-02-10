package com.munix

import grails.test.*

class SalesDeliveryServiceTests extends GrailsUnitTestCase {
    def sampleTerm
    def sampleCustomer
    def sampleCity
    def sampleCustomerType
    def sampleDiscountType
    def sampleDiscountType2
	def sampleDiscountGroup
    def salesDeliveryService
    def sampleSalesDelivery
    def sampleSalesDelivery2
    def sampleSalesDelivery3
    def sampleSalesDelivery4
	def sampleSalesDelivery5
    def sampleWarehouse
	def sampleWarehouse2
	def sampleSalesOrderItem
	def sampleSalesOrderItem2
	def sampleSalesOrderItem3
    def sampleSalesOrder
    def sampleSalesOrder2
	def sampleSalesOrder3
    def sampleProduct
	def sampleProduct2
    def sampleStock
	def sampleStock2
	def sampleSalesDeliveryItem
	def sampleSalesDeliveryItem2
	def sampleCustomerAccount
	
    protected void setUp() {
        super.setUp()
        setUpTerms()
        setUpWarehouses()
		setUpDiscountTypes()
        setUpProducts()
        setUpStocks()
        setUpProductAndStockRelationship()
        setUpCities()
        setUpCustomerTypes()
        setUpCustomerAccounts()
        setUpCustomers()
        setUpSalesOrders()
		setUpSalesOrderItem()
		setUpDiscountGroup()
		setUpSalesDeliveryItems()
        setUpSalesDeliveries()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testListOfSalesDeliveryForCreditMemoNotNullCustomerAndDiscountType() {
        def salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(sampleCustomer,sampleDiscountType)
        assertNotNull "The list is empty",salesDeliveryList
        assertEquals "The list contains more items than the expected value", 2, salesDeliveryList.size()
    }
	
    void testListOfSalesDeliveryForCreditMemoNotNullCustomerNullDiscountType(){
        def salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(sampleCustomer,null)
        assertTrue "The list is not empty",salesDeliveryList.isEmpty()
    }
	
    void testListOfSalesDeliveryForCreditMemoNullCustomerNotNullDiscountType(){
        def salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(null,sampleDiscountType)
        assertTrue "The list is not empty",salesDeliveryList.isEmpty()
    }
	
    void testListOfSalesDeliveryForCreditMemoNullCustomerNullDiscountType(){
        def salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(null,null)
        assertTrue "The list is not empty",salesDeliveryList.isEmpty()
    }
	
    void testListOfSalesDeliveryForCreditMemoMoreThanOneResult() {
        def salesDeliveryList = salesDeliveryService.listOfSalesDeliveryForCreditMemo(sampleCustomer,sampleDiscountType2)
        assertNotNull "The list is empty",salesDeliveryList
        assertEquals "The list contains more items than the expected value", 2, salesDeliveryList.size()
        assertTrue "The object is not an instance of the expected object", salesDeliveryList.contains(sampleSalesDelivery2)
        assertTrue "The object is not an instance of the expected object", salesDeliveryList.contains(sampleSalesDelivery3)
    }
	
    void testApproveSalesDeliveryWithOutError(){
		def oldCustomerAccountSalesDeliveryTotalUnpaidBalance = sampleCustomerAccount.totalUnpaidSalesDeliveries
		def mockCustomerLedgerService = mockFor(CustomerLedgerService)
		mockCustomerLedgerService.demand.createApprovedSalesDelivery(1..1) {x ->
			return true
		}
		salesDeliveryService.customerLedgerService = mockCustomerLedgerService.createMock()
		def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createApprovedSalesDeliveryStockCards(1..1) {x ->
			return null
		}
		salesDeliveryService.stockCardService = mockStockCardService.createMock()

        def result = salesDeliveryService.approveSalesDelivery(sampleSalesDelivery)
		
        assertTrue "The result should be true", result
        assertEquals 0, sampleStock.qty
        assertTrue "Sales Delivery must be approved", sampleSalesDelivery.isApproved()
		assertEquals "should update customer account sales delivery total unpaid balance", oldCustomerAccountSalesDeliveryTotalUnpaidBalance + sampleSalesDelivery.computeTotalAmount(), sampleCustomerAccount.totalUnpaidSalesDeliveries
    }
	
    void testApproveSalesDeliveryWithError(){
        sampleSalesDelivery.warehouse = null
        
		def result = salesDeliveryService.approveSalesDelivery(sampleSalesDelivery)
		
        sampleSalesDelivery.refresh()
        assertFalse "The result should be false", result
        assertEquals 1, sampleStock.qty
        assertTrue "Sales Delivery must be unapproved", sampleSalesDelivery.isUnapproved()
    }
	
    void testUnapproveSalesDeliveryWithOutError(){
		def oldCustomerAccountSalesDeliveryTotalUnpaidBalance = sampleCustomerAccount.totalUnpaidSalesDeliveries
		def mockCustomerLedgerService = mockFor(CustomerLedgerService)
		mockCustomerLedgerService.demand.createUnapprovedSalesDelivery(1..1) {x ->
			return true
		}
		salesDeliveryService.customerLedgerService = mockCustomerLedgerService.createMock()
		def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createUnapprovedSalesDeliveryStockCards(1..1) { x ->
			return null
		}
		salesDeliveryService.stockCardService = mockStockCardService.createMock()

        def result = salesDeliveryService.unapproveSalesDelivery(sampleSalesDelivery)
		
        assertTrue "The result should be true", result
        assertEquals 2, sampleStock.qty
        assertTrue "Sales Delivery must be unapproved", sampleSalesDelivery.isUnapproved()
		assertEquals "should update customer account sales delivery total unpaid balance", oldCustomerAccountSalesDeliveryTotalUnpaidBalance - sampleSalesDelivery.computeTotalAmount(), sampleCustomerAccount.totalUnpaidSalesDeliveries
    }
	
    void testUnapproveSalesDeliveryWithError() {
        sampleSalesDelivery.warehouse= null
        def result = salesDeliveryService.unapproveSalesDelivery(sampleSalesDelivery)
        sampleSalesDelivery.refresh()
        assertFalse "The result should be false", result
        assertEquals 1, sampleStock.qty
    }
	
	void testGenerateWarehouseList() {
		assertEquals "Warehouse list should be alphabetically sorted by identifier", ["BUL", "WH"].toString(), salesDeliveryService.generateWarehouseList().toString()
	}
	
	void testGenerateUndeliveredItems() {
		// error in persisting salesOrderItem renders this method untestable :(
	}
	
	void testGenerateOrderItems() {
		def map1 = createMapForOrderItems(new SalesOrderItem(
				product: sampleProduct,
				qty: new BigDecimal("2"),
				price: new BigDecimal("10"),
				finalPrice: new BigDecimal("8"),
				isNet: false
		))
		def map2 = createMapForOrderItems(new SalesOrderItem(
				product: sampleProduct,
				qty: new BigDecimal("5"),
				price: new BigDecimal("20"),
				finalPrice: new BigDecimal("15"),
				isNet: true
		))
		
		def expected = [map1, map2]
		def actual = salesDeliveryService.generateOrderItems(sampleSalesOrder3)
		
		assertEquals expected.toString(), actual.toString()
	}
	
	void testGetAllUnpaidSalesDeliveryListOneUnpaid(){
		sampleSalesDelivery.approve()
		sampleSalesDelivery2.unapprove()
		
		assertNotNull "[Guard] Sales Delivery must not be null", sampleSalesDelivery
		assertTrue "[Guard] Sales Delivery status must be unpaid", sampleSalesDelivery.isApproved()
		assertEquals "[Guard] Customer must be the same", sampleCustomer, sampleSalesDelivery.customer
		
		def counter = 0
		SalesDelivery.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Gurad] Sales Delivery must only have one unpaid status", 1, counter
		
		def result = salesDeliveryService.getAllUnpaidSalesDeliveryList(sampleCustomer)
		assertEquals "Sales Delivery list must have a content", 1, result.size()
	}
	
	void testGetAllUnpaidSalesDeliveryListNoResult(){
		sampleSalesDelivery.unapprove()
		sampleSalesDelivery2.unapprove()
		
		assertNotNull "[Guard] Sales Delivery must not be null", sampleSalesDelivery
		assertEquals "[Guard] Customer must be the same", sampleCustomer, sampleSalesDelivery.customer
		assertTrue "[Guard] Sales Delivery must have a content", SalesDelivery.list().size() > 0
		
		def counter = 0
		SalesDelivery.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] Sales Delivery must not unpaid status", 0, counter
		
		def result = salesDeliveryService.getAllUnpaidSalesDeliveryList(sampleCustomer)
		assertEquals "Sales Delivery must not have a content", 0, result.size()
	}
	
	void testGetUnpaidSalesDeliveryListOneUnpaid(){
		sampleSalesDelivery.approve()
		sampleSalesDelivery2.unapprove()
		
		assertNotNull "[Guard] Sales Delivery must not be null", sampleSalesDelivery
		assertTrue "[Guard] Sales Delivery status must be unpaid", sampleSalesDelivery.isApproved()
		assertEquals "[Guard] Customer must be the same", sampleCustomer, sampleSalesDelivery.customer
		
		def counter = 0
		SalesDelivery.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Gurad] Sales Delivery must only have one unpaid status", 1, counter
		
		def result = salesDeliveryService.getUnpaidSalesDeliveryList(sampleCustomer, [:])
		assertEquals "Sales Delivery list must have a content", 1, result.size()
	}
	
	void testGetUnpaidSalesDeliveryListNoResult(){
		sampleSalesDelivery.unapprove()
		sampleSalesDelivery2.unapprove()
		
		assertNotNull "[Guard] Sales Delivery must not be null", sampleSalesDelivery
		assertEquals "[Guard] Customer must be the same", sampleCustomer, sampleSalesDelivery.customer
		assertTrue "[Guard] Sales Delivery must have a content", SalesDelivery.list().size() > 0
		
		def counter = 0
		SalesDelivery.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] Sales Delivery must not have unpaid status", 0, counter
		
		def result = salesDeliveryService.getUnpaidSalesDeliveryList(sampleCustomer, [:])
		assertEquals "Sales Delivery must not have a content", 0, result.size()
	}
	
	void testGetUnpaidSalesDeliveryListTwoResult(){
		sampleSalesDelivery.approve()
		sampleSalesDelivery2.approve()
		
		assertNotNull "[Guard] Sales Delivery must not be null", sampleSalesDelivery
		assertEquals "[Guard] Customer must be the same", sampleCustomer, sampleSalesDelivery.customer
		assertTrue "[Guard] Sales Delivery must have a content", SalesDelivery.list().size() > 0
		
		def counter = 0
		SalesDelivery.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] Sales Delivery must have two unpaid status", 2, counter
		
		def result = salesDeliveryService.getUnpaidSalesDeliveryList(sampleCustomer, [max:1])
		assertEquals "Sales Delivery must not have a content", 2, result.totalCount
	}
	
	void testComputeTotalUnpaidSalesDeliveries(){
		sampleSalesDelivery.items.each{
			it.price = new BigDecimal("10.00")
		}
		sampleSalesDelivery2.items.each{
			it.price = new BigDecimal("20.00")
		}
		sampleSalesDelivery.approve()
		sampleSalesDelivery2.approve()
		sampleSalesDelivery.amountPaid = new BigDecimal("5.00")
		sampleSalesDelivery2.amountPaid = new BigDecimal("10.00")
		
		assertNotNull "[Guard] Sales Delivery must not be null", sampleSalesDelivery
		assertEquals "[Guard] Customer must be the same", sampleCustomer, sampleSalesDelivery.customer
		assertTrue "[Guard] Sales Delivery must have a content", SalesDelivery.list().size() > 0 
		assertEquals "[Guard] Amount paid of Sales Delivery  must be 5.00", new BigDecimal("5.00"), sampleSalesDelivery.amountPaid
		assertEquals "[Guard] Amount paid of Sales Delivery 2  must be 10.00", new BigDecimal("10.00"), sampleSalesDelivery2.amountPaid
		
		def counter = 0
		SalesDelivery.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] Sales Delivery must not unpaid status", 2, counter
		
		BigDecimal result = salesDeliveryService.computeTotalUnpaidSalesDeliveries(sampleCustomer)
		assertEquals "Sales Delivery must have a result", new BigDecimal("15.00"), result
	}
	
	void testSaveSalesDelivery() {
		def oldSalesDeliveryCount = SalesDelivery.count()
		def salesDelivery = new SalesDelivery(sampleSalesOrder)
		salesDelivery.warehouse = sampleWarehouse
		salesDelivery.preparedBy = "me"
		salesDelivery.deliveryType= "nothing"
		salesDelivery.addToItems(new SalesDeliveryItem(product: sampleProduct, price: 1, qty: 1, orderItem: sampleSalesOrderItem))
		
		salesDeliveryService.saveSalesDelivery(salesDelivery)
		
		assertEquals "sales delivery should be saved", 1 + oldSalesDeliveryCount, SalesDelivery.count()
	}
		
	void testCancelSalesDelivery() {
		salesDeliveryService.cancelSalesDelivery(sampleSalesDelivery)
		
		assertEquals "sales delivery should be cancelled", "Cancelled", sampleSalesDelivery.status
	}
	
	private Map createMapForOrderItems(SalesOrderItem salesOrderItemInstance) {
		def map = [:]
		           
       map["item"] = salesOrderItemInstance
	   map["stocks"] = [(sampleWarehouse.identifier): 1, (sampleWarehouse2.identifier): 2]
	   
	   return map
	}
	
	private Map createMapForDeliveryItems(SalesDeliveryItem salesDeliveryItemInstance) {
		def map = [:]
		
		map["item"] = salesDeliveryItemInstance
		map["stocks"] = [(sampleWarehouse.identifier): 1, (sampleWarehouse2.identifier): 2]
		
		return map
	}
	
	private void setUpCustomerAccounts() {
		sampleCustomerAccount = new CustomerAccount()
		sampleCustomerAccount.save()
	}
	
	private setUpSalesDeliveries() {
		sampleSalesDelivery = new SalesDelivery(sampleSalesOrder)
		sampleSalesDelivery.warehouse = sampleWarehouse
		sampleSalesDelivery.preparedBy = "me"
		sampleSalesDelivery.deliveryType= "nothing"
		sampleSalesDelivery.addToItems(new SalesDeliveryItem(product: sampleProduct, price: 1, qty: 1, orderItem: sampleSalesOrderItem))
		sampleSalesDelivery.save()
		
		sampleSalesDelivery2 = new SalesDelivery(sampleSalesOrder2)
		sampleSalesDelivery2.warehouse = sampleWarehouse
		sampleSalesDelivery2.preparedBy = "you"
		sampleSalesDelivery2.deliveryType= "nothing"
		sampleSalesDelivery2.addToItems(sampleSalesDeliveryItem)
		sampleSalesDelivery2.save()
		
		sampleSalesDelivery3 = new SalesDelivery(sampleSalesOrder2)
		sampleSalesDelivery3.warehouse = sampleWarehouse
		sampleSalesDelivery3.preparedBy = "everyone"
		sampleSalesDelivery3.deliveryType= "nothing"
		sampleSalesDelivery3.addToItems(sampleSalesDeliveryItem)
		sampleSalesDelivery3.save()
		
		sampleSalesDelivery4 = new SalesDelivery(sampleSalesOrder2)
		sampleSalesDelivery4.warehouse = sampleWarehouse
		sampleSalesDelivery4.preparedBy = "else"
		sampleSalesDelivery4.deliveryType= "nothing"
		sampleSalesDelivery4.addToItems(sampleSalesDeliveryItem)
		sampleSalesDelivery4.cancel()
		sampleSalesDelivery4.save()

		sampleSalesDelivery5 = new SalesDelivery(sampleSalesOrder)
		sampleSalesDelivery5.warehouse = sampleWarehouse
		sampleSalesDelivery5.preparedBy = "me"
		sampleSalesDelivery5.deliveryType= "nothing"
		sampleSalesDelivery5.addToItems(sampleSalesDeliveryItem)
		sampleSalesDelivery5.addToItems(sampleSalesDeliveryItem2)
		sampleSalesDelivery5.save()
	}

	private setUpSalesDeliveryItems() {
		sampleSalesDeliveryItem = new SalesDeliveryItem(product: sampleProduct, price: 1, qty: 1)
		sampleSalesDeliveryItem.save()
		
		sampleSalesDeliveryItem2 = new SalesDeliveryItem(product: sampleProduct2, price: 1, qty: 1)
		sampleSalesDeliveryItem2.save()
	}
	
	private setUpSalesOrders() {
		sampleSalesOrder = new SalesOrder(
			discountType: sampleDiscountType,
			priceType: PriceType.RETAIL,
			customer: sampleCustomer,
			items: [sampleSalesOrderItem]
		)
		sampleSalesOrder.save()
		
		sampleSalesOrder2 = new SalesOrder(
			discountType: sampleDiscountType2,
			priceType: PriceType.RETAIL,
			customer: sampleCustomer,
			items: [new SalesOrderItem(
				product: sampleProduct,
				qty: new BigDecimal("1"),
				price: new BigDecimal("0"),
				finalPrice: new BigDecimal("0")
			), ]
		)
		sampleSalesOrder2.save()

		sampleSalesOrder3 = new SalesOrder(
				discountType: sampleDiscountType,
				priceType: PriceType.RETAIL,
				customer: sampleCustomer,
				items: [new SalesOrderItem(
						product: sampleProduct,
						qty: new BigDecimal("2"),
						price: new BigDecimal("10"),
						finalPrice: new BigDecimal("8"),
						isNet: false
				), new SalesOrderItem(
						product: sampleProduct,
						qty: new BigDecimal("5"),
						price: new BigDecimal("20"),
						finalPrice: new BigDecimal("15"),
						isNet: true
				), new SalesOrderItem(
						product: sampleProduct,
						qty: new BigDecimal("5"),
						deliveredQty: new BigDecimal("5"),
						price: new BigDecimal("20"),
						finalPrice: new BigDecimal("15"),
						isNet: true
				)]
		)
		sampleSalesOrder3.save()
		
	}

	private setUpSalesOrderItem() {
		sampleSalesOrderItem = new SalesOrderItem(
				product: sampleProduct,
				qty: new BigDecimal("1"),
				price: new BigDecimal("0"),
				finalPrice: new BigDecimal("0"),
				invoice: sampleSalesOrder
			)
		sampleSalesOrder.addToItems(sampleSalesOrderItem)
		sampleSalesOrderItem.save()
	}
	private setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(description:"prod", identifier:"P", margin: BigDecimal.ONE)
		sampleDiscountType.save()
		
		sampleDiscountType2 = new DiscountType(description:"prod2",	identifier:"P2", margin: BigDecimal.ONE)
		sampleDiscountType2.save()
	}

	private setUpDiscountGroup() {
		sampleDiscountGroup = new DiscountGroup(identifier: "Net", description: "Net", rate: new BigDecimal(0.00))
		sampleDiscountGroup.save()
	}

	private setUpCustomers() {
		sampleCustomer = new Customer(identifier: "customer1",
				name:"cust",
				bilAddrCity:sampleCity,
				bilAddrStreet:"Accuier",
				status:Customer.Status.ACTIVE,
				type:sampleCustomerType,
				fax:"12345",
				term:sampleTerm,
				autoApprove:true,
				customerAccount: sampleCustomerAccount)
		sampleCustomer.save()
	}

	private setUpCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier:"H",
				description:"happy")
		sampleCustomerType.save()
	}

	private setUpCities() {
		sampleCity = new City( description:"quezon city",
				identifier:"QC")
		sampleCity.save()
	}

	private setUpProductAndStockRelationship() {
		sampleProduct.addToStocks(sampleStock)
		sampleProduct.save()
		
		sampleProduct.addToStocks(sampleStock2)
		sampleProduct.save()
		
		sampleProduct2.addToStocks(sampleStock2)
		sampleProduct2.save()
		
		sampleProduct2.addToStocks(sampleStock)
		sampleProduct2.save()
	}

	private setUpStocks() {
		sampleStock = new Stock(warehouse: sampleWarehouse,
				qty:1,
				product:sampleProduct)
		sampleStock.save()
		
		sampleStock2 = new Stock(warehouse: sampleWarehouse2,
				qty:2,
				product:sampleProduct2)
		sampleStock2.save()
	}

	private setUpProducts() {
		sampleProduct = new Product(
			identifier:"product1",
			type: sampleDiscountType)
		sampleProduct.save()
		
		sampleProduct2 = new Product(
			identifier:"product2",
			type: sampleDiscountType)
		sampleProduct2.save()
	}

	private setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier:"WH", description:"White House")
		sampleWarehouse.save()
		
		sampleWarehouse2 = new Warehouse(identifier:"BUL", description:"Bulacan")
		sampleWarehouse2.save()
	}

	private setUpTerms() {
		sampleTerm = new Term(identifier:"term",
				description:"termDesc",
				dayValue:new BigDecimal("1234"))
		sampleTerm.save()
	}
	
	void testCheckIfDefaultQuantityHasBeenChangedNotChanged() {
		def result = salesDeliveryService.checkIfDefaultQuantityHasBeenChanged(sampleSalesDelivery)
		
		assertFalse "sample sales delivery is not changed", result
	}
	
	void testCheckIfDefaultQuantityHasBeenChangedAndWasChanged() {
		sampleSalesOrderItem.qty = new BigDecimal("10")
		sampleSalesOrderItem.save(flush: true)
		
		def result = salesDeliveryService.checkIfDefaultQuantityHasBeenChanged(sampleSalesDelivery)
		
		assertTrue "sample sales delivery is not changed", result
	}
}
