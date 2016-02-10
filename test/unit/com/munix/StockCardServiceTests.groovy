package com.munix

import grails.test.*

class StockCardServiceTests extends GrailsUnitTestCase {
	def sampleSalesOrder
	def sampleSalesOrderItem
    def sampleSalesDelivery
    def sampleProduct
    def sampleSalesDeliveryItem
    def sampleStockCardItem
    def sampleStockCard
    def sampleWarehouse
	def samplePurchaseInvoice
	def samplePurchaseOrderItem1
	def samplePurchaseInvoiceItem1
	def samplePurchaseOrderItem2
	def samplePurchaseInvoiceItem2
	def sampleCreditMemo
	def sampleCreditMemoItem
	def sampleMaterialRequisitionItem
	def sampleMaterialRelease
	def sampleMaterialReleaseItem
	def sampleJobOrder
	def sampleJobOut
	def sampleSupplier
	def sampleCustomer
	def sampleCity
	def sampleCustomerType
	def sampleTerm

    def stockCardService = new StockCardService()
    def generalMethodService = new GeneralMethodService()
	
    protected void setUp() {
        super.setUp()
		
		setupWarehouse()
		setupProduct()
		setupStockCard()
		setupRelationshipBetweenProductAndStockCard()
		setupCities()
		setupCustomerTypes()
		setupTerms()
		setupCustomers()
		setupSupplier()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateUnapprovedSalesDeliveryStockCardsWithQty() {
		setupSalesOrder()
		setupSalesOrderItem()
        setupSalesDelivery()
        setupSalesDeliveryItem()
        setupSalesDeliveryAndItemRelationship()
       
        stockCardService.createUnapprovedSalesDeliveryStockCards(sampleSalesDelivery)
		
        assertEquals "stock card item must only contain 1 item", 1, sampleProduct.stockCard.items.size()
        sampleProduct.stockCard.items.each{
            assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_SALES_DELIVERY, it.type
            assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), it.balance
            assertEquals "created Stock Card Item qty is wrong", BigDecimal.ONE, it.qtyIn
            assertEquals "created Stock Card Item warehouse is wrong", "123", it.warehouseIn
            assertNull "created Stock Card Item qty out must be empty", it.qtyOut
            assertNull "created Stock Card Item warehouse out must be empty", it.warehouseOut
        }
    }
    void testCreateUnapprovedSalesDeliveryStockCardsWithOutQty() {
		setupSalesOrder()
		setupSalesOrderItem()
        setupSalesDelivery()
        setupSalesDeliveryItem()
        setupSalesDeliveryAndItemRelationship()
        sampleSalesDeliveryItem.qty = BigDecimal.ZERO
        sampleSalesDeliveryItem.save()
        assertEquals "[GUARD]SDitem qty must be 0",BigDecimal.ZERO,sampleSalesDeliveryItem.qty
        stockCardService.createUnapprovedSalesDeliveryStockCards(sampleSalesDelivery)

        assertNull "stock card item must be null", sampleProduct.stockCard.items
    }

	void testCreateApprovedSalesDeliveryStockCardsWithQty() {
		setupSalesOrder()
		setupSalesOrderItem()
        setupSalesDelivery()
        setupSalesDeliveryItem()
        setupSalesDeliveryAndItemRelationship()
		
        stockCardService.createApprovedSalesDeliveryStockCards(sampleSalesDelivery)
		
        assertEquals "stock card item must only contain 1 item", 1, sampleProduct.stockCard.items.size()
        sampleProduct.stockCard.items.each{
            assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_SALES_DELIVERY, it.type
            assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), it.balance
            assertEquals "created Stock Card Item qty out is wrong", BigDecimal.ONE, it.qtyOut
            assertEquals "created Stock Card Item warehouse is wrong", "123", it.warehouseOut
            assertNull "created Stock Card Item qty in must be empty", it.qtyIn
            assertNull "created Stock Card Item warehouse out must be empty", it.warehouseIn
        }
    }
    void testCreateApprovedSalesDeliveryStockCardsWithoutQty() {
		setupSalesOrder()
		setupSalesOrderItem()
        setupSalesDelivery()
        setupSalesDeliveryItem()
        setupSalesDeliveryAndItemRelationship()
		sampleSalesDeliveryItem.qty = BigDecimal.ZERO
        sampleSalesDeliveryItem.save()
        assertEquals "[GUARD]SDitem qty must be 0",BigDecimal.ZERO,sampleSalesDeliveryItem.qty
        stockCardService.createApprovedSalesDeliveryStockCards(sampleSalesDelivery)

        assertNull "stock card item must be null", sampleProduct.stockCard.items

    }
    void testCreateUnapprovedMaterialReleaseStockCards() {
        setupMaterialRequisitionItem()
        setupMaterialRelease()
        setupMaterialReleaseItem()
        setupMaterialReleaseAndMaterialReleaseItem()
        
        stockCardService.createUnapprovedMaterialReleaseStockCards(sampleMaterialRelease)
		
        assertEquals "stock card item must only contain 1 item", 1, sampleProduct.stockCard.items.size()
        sampleProduct.stockCard.items.each{
            assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_MATERIAL_RELEASE, it.type
            assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), it.balance
            assertEquals "created Stock Card Item qty in is wrong", BigDecimal.ONE, it.qtyIn
            assertEquals "created Stock Card Item warehouse in is wrong", "123", it.warehouseIn
            assertEquals "created Stock Card Item local cost is wrong", new BigDecimal("456"), it.costLocal
            assertNull "created Stock Card Item qty out must be empty", it.qtyOut
            assertNull "created Stock Card Item warehouse out must be empty", it.warehouseOut
        }
    }
	void testCreateApprovedMaterialReleaseStockCards() {
        setupMaterialRequisitionItem()
        setupMaterialRelease()
        setupMaterialReleaseItem()
        setupMaterialReleaseAndMaterialReleaseItem()

        stockCardService.createApprovedMaterialReleaseStockCards(sampleMaterialRelease)

        assertEquals "stock card item must only contain 1 item", 1, sampleProduct.stockCard.items.size()
        sampleProduct.stockCard.items.each{
            assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_MATERIAL_RELEASE, it.type
            assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), it.balance
            assertEquals "created Stock Card Item qty in is wrong", BigDecimal.ONE, it.qtyOut
            assertEquals "created Stock Card Item warehouse in is wrong", "123", it.warehouseOut
            assertEquals "created Stock Card Item local cost is wrong", new BigDecimal("456"), it.costLocal
            assertNull "created Stock Card Item qty out must be empty", it.qtyIn
            assertNull "created Stock Card Item warehouse out must be empty", it.warehouseIn
        }
    }
	
	void testCreateApprovedPurchaseInvoice() {
		setUpPurchaseInvoices()
		setUpPurchaseOrderItems()
		setUpPurchaseInvoiceItems()
		setUpPurchaseInvoiceAndItemRelationship()
		
		stockCardService.createApprovedPurchaseInvoice(samplePurchaseInvoice)
		
		assertEquals "product must have 2 stock card items", 2, sampleProduct.stockCard.items.size()
	}
	
	void testCreateUnapprovedPurchaseInvoice() {
		setUpPurchaseInvoices()
		setUpPurchaseOrderItems()
		setUpPurchaseInvoiceItems()
		setUpPurchaseInvoiceAndItemRelationship()
		
		stockCardService.createUnapprovedPurchaseInvoice(samplePurchaseInvoice)
		
		assertEquals "product must have 2 stock card items", 2, sampleProduct.stockCard.items.size()
	}
	
	void testCreateApprovedJobOut() {
		setUpJobOrders()
		setUpJobOuts()
		setUpJobOrderAndJobOutsRelationship()
		
		stockCardService.createApprovedJobOut(sampleJobOut)
		
		assertEquals "product must have 1 stock card item", 1, sampleProduct.stockCard.items.size()
	}
	
	void testCreateUnapprovedJobOut() {
		setUpJobOrders()
		setUpJobOuts()
		setUpJobOrderAndJobOutsRelationship()
		
		stockCardService.createUnapprovedJobOut(sampleJobOut)
		
		assertEquals "product must have 1 stock card item", 1, sampleProduct.stockCard.items.size()
	}
	
	void testCreateApprovedCreditMemo() {
		setupSalesOrder()
		setupSalesOrderItem()
        setupSalesDelivery()
        setupSalesDeliveryItem()
        setupSalesDeliveryAndItemRelationship()
		setUpCreditMemos()
		setUpCreditMemoItems()
		setUpCreditMemoAndCreditMemoItemsRelationship()
		
		stockCardService.createApprovedCreditMemo(sampleCreditMemo)
		
		assertEquals "product must have 1 stock card items", 1, sampleProduct.stockCard.items.size()
	}
	
	void testCreateUnapprovedCreditMemo() {
		setupSalesOrder()
		setupSalesOrderItem()
		setupSalesDelivery()
		setupSalesDeliveryItem()
		setupSalesDeliveryAndItemRelationship()
		setUpCreditMemos()
		setUpCreditMemoItems()
		setUpCreditMemoAndCreditMemoItemsRelationship()
		
		stockCardService.createUnapprovedCreditMemo(sampleCreditMemo)
		
		assertEquals "product must have 1 stock card items", 1, sampleProduct.stockCard.items.size()
	}
	
	private void setupSupplier() {
		sampleSupplier = new Supplier(identifier:"supplierIdentifier", name:"supplierName")
		mockDomain(Supplier, [sampleSupplier])
	}
	
	private setupCustomers() {
		sampleCustomer = new Customer(identifier: "customerIdentifier",
				name:"customerName",
				bilAddrCity:sampleCity,
				bilAddrStreet:"Accuier",
				status:Customer.Status.ACTIVE,
				type:sampleCustomerType,
				fax:"12345",
				term:sampleTerm,
				autoApprove:true)
		mockDomain(Customer, [sampleCustomer])
	}
	
	private setupCities() {
		sampleCity = new City( description:"quezon city",
				identifier:"QC")
		mockDomain(City, [sampleCity])
	}
	
	private setupCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier:"H",
				description:"happy")
		mockDomain(CustomerType, [sampleCustomerType])
	}

	private setupTerms() {
		sampleTerm = new Term(identifier:"term",
				description:"termDesc",
				dayValue:new BigDecimal("1234"))
		mockDomain(Term, [sampleTerm])
	}
	
	private void setUpPurchaseInvoices() {
		samplePurchaseInvoice = new PurchaseInvoice(
			date: generalMethodService.createDate("01/01/2010"),
			reference: "1",
			exchangeRate: new BigDecimal("0.5"),
			supplier: sampleSupplier,
			warehouse: sampleWarehouse
		)
		mockDomain(PurchaseInvoice, [samplePurchaseInvoice])
	}
	
	private void setUpPurchaseOrderItems() {
		samplePurchaseOrderItem1 = new PurchaseOrderItem(
			product: sampleProduct
		)
		samplePurchaseOrderItem2 = new PurchaseOrderItem(
			product: sampleProduct
		)
		mockDomain(PurchaseOrderItem, [samplePurchaseOrderItem1, samplePurchaseOrderItem2])
	}
	
	private void setUpPurchaseInvoiceItems() {
		samplePurchaseInvoiceItem1 = new PurchaseInvoiceItem(
			purchaseInvoice: samplePurchaseInvoice,
			purchaseOrderItem: samplePurchaseOrderItem1,
			finalPrice: new BigDecimal("30"),
			qty: new BigDecimal("20")
		)
		samplePurchaseInvoiceItem2 = new PurchaseInvoiceItem(
			purchaseInvoice: samplePurchaseInvoice,
			purchaseOrderItem: samplePurchaseOrderItem2,
			finalPrice: new BigDecimal("30"),
			qty: new BigDecimal("20")
		)
		mockDomain(PurchaseInvoiceItem, [samplePurchaseInvoiceItem1, samplePurchaseInvoiceItem2])
	}
	
	private void setUpPurchaseInvoiceAndItemRelationship(){
		samplePurchaseInvoice.addToItems(samplePurchaseInvoiceItem1)
		samplePurchaseInvoice.addToItems(samplePurchaseInvoiceItem2)
	}
	
	private void setupProduct(){
		sampleProduct = new Product()
		mockDomain(Product, [sampleProduct])
		sampleProduct.metaClass.'static'.getTotalStock = { -> new BigDecimal("100") }
	}
	
	private void setupWarehouse(){
		sampleWarehouse = new Warehouse(identifier: "123")
		mockDomain(Warehouse,[sampleWarehouse])
	}
	
	private void setupSalesOrder() {
		sampleSalesOrder = new SalesOrder(
			discountType: new DiscountType(identifier: "discountType", description: "discount type"),
			priceType:PriceType.RETAIL,
			customer: sampleCustomer,
			items:[new SalesOrderItem(product:sampleProduct,
				qty:new BigDecimal("1"),
				price:new BigDecimal("0"),
				finalPrice:new BigDecimal("0"))])
		mockDomain(SalesOrder,[sampleSalesOrder])
	}
	
	private void setupSalesOrderItem() {
		sampleSalesOrderItem = new SalesOrderItem(
			isNet: false
		)
		mockDomain(SalesOrderItem,[sampleSalesOrderItem])
	}
	
	private void setupSalesDelivery(){
		def sampleDate = generalMethodService.createDate("10/10/2000")
		sampleSalesDelivery = new SalesDelivery(date: sampleDate,
				salesDeliveryId: "123",
				customer: sampleCustomer,
				invoice: sampleSalesOrder,
				warehouse: sampleWarehouse)
		mockDomain(SalesDelivery,[sampleSalesDelivery])

	}
	
	private void setupSalesDeliveryItem(){
		sampleSalesDeliveryItem = new SalesDeliveryItem(product: sampleProduct,
				qty: BigDecimal.ONE,
				price: BigDecimal.ONE,
				orderItem: sampleSalesOrderItem)
		mockDomain(SalesDeliveryItem,[sampleSalesDeliveryItem])
	}
	
	private void setupSalesDeliveryAndItemRelationship(){
		sampleSalesDelivery.addToItems(sampleSalesDeliveryItem)
		sampleSalesDeliveryItem.delivery = sampleSalesDelivery
		sampleSalesDelivery.save()
	}
	
	private void setUpCreditMemos() {
		sampleCreditMemo = new CreditMemo(
			discountType: new DiscountType(),
			discount: new BigDecimal("0"),
			status: "Approved",
			preparedBy: "me",
			customer: sampleCustomer,
			commissionRate: new BigDecimal("0"),
			date: new Date()
		)
		mockDomain(CreditMemo, [sampleCreditMemo])
	}
	
	private void setUpCreditMemoItems() {
		sampleCreditMemoItem = new CreditMemoItem(
			date:generalMethodService.createDate("02/10/2010"),
			deliveryItem: sampleSalesDeliveryItem,
			oldQty:new BigDecimal("9"),
			oldPrice:new BigDecimal("11.11"),
			newQty:new BigDecimal("3"),
			newPrice:new BigDecimal("11.11")
			)
		mockDomain(CreditMemoItem, [sampleCreditMemoItem])
	}
	
	private void setUpCreditMemoAndCreditMemoItemsRelationship() {
		sampleCreditMemo.addToItems(sampleCreditMemoItem)
		sampleCreditMemo.save()
	}
	
	private void setupRelationshipBetweenProductAndStockCard(){
		sampleProduct.stockCard = sampleStockCard
		sampleProduct.save()
	}
	
	private void setupStockCardItem(){
		sampleStockCardItem = new StockCardItem()
		mockDomain(StockCardItem, [sampleStockCardItem])
	}
	
	private void setupStockCard(){
		sampleStockCard = new StockCard()
		mockDomain(StockCard, [sampleStockCard])
	}
    private void setupMaterialRequisitionItem(){
		sampleMaterialRequisitionItem = new MaterialRequisitionItem(component: sampleProduct)
		mockDomain(MaterialRequisitionItem,[sampleMaterialRequisitionItem])
        sampleMaterialRequisitionItem.metaClass.'static'.computeCostPerUnit = { -> new BigDecimal("456") }

	}
    private void setupMaterialRelease(){
		def sampleDate = generalMethodService.createDate("10/10/2000")
		sampleMaterialRelease = new MaterialRelease(date: sampleDate,
				warehouse: sampleWarehouse)
		mockDomain(MaterialRelease,[sampleMaterialRelease])

	}

	private void setupMaterialReleaseItem(){
		sampleMaterialReleaseItem = new MaterialReleaseItem(
                materialRequisitionItem: sampleMaterialRequisitionItem,
				qty: BigDecimal.ONE)
		mockDomain(MaterialReleaseItem,[sampleMaterialReleaseItem])

	}

	private void setupMaterialReleaseAndMaterialReleaseItem(){
		sampleMaterialRelease.addToItems(sampleMaterialReleaseItem)
		sampleMaterialReleaseItem.materialRelease = sampleMaterialRelease
	}
	
	private void setUpJobOrders() {
		sampleJobOrder = new JobOrder(
			product: sampleProduct
		)
		mockDomain(JobOrder, [sampleJobOrder])
	}
	
	private void setUpJobOuts() {
		sampleJobOut = new JobOut(
			jobOrder: sampleJobOrder,
			date: generalMethodService.createDate("01/01/2010"),
			warehouse: sampleWarehouse,
			qty: new BigDecimal("30")
		)
		mockDomain(JobOut, [sampleJobOut])
	}

	private void setUpJobOrderAndJobOutsRelationship() {
		sampleJobOrder.addToJobOuts(sampleJobOut)
	}
}
