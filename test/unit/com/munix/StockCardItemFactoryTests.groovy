package com.munix

import grails.test.*

class StockCardItemFactoryTests extends GrailsUnitTestCase {
    def generalMethodService = new GeneralMethodService()
    def sampleWarehouse
    def sampleProduct
	def sampleSalesOrder
    def sampleSalesDelivery
    def sampleSalesDeliveryItem
	def sampleSalesOrderItem
    def sampleMaterialRelease
    def sampleMaterialReleaseItem
    def sampleMaterialRequisitionItem
	def samplePurchaseInvoice
	def samplePurchaseOrderItem
	def samplePurchaseInvoiceItem
	def sampleDiscountType
	def sampleCreditMemo
	def sampleCreditMemoItem
	def sampleDebitMemo
	def sampleDebitMemoItem
	def sampleJobOrder
	def sampleJobOut
	def sampleSupplier
	def sampleCustomer
	def sampleCity
	def sampleCustomerType
	def sampleTerm
	
    protected void setUp() {
        super.setUp()
		setupCities()
		setupCustomerTypes()
		setupTerms()
		setupCustomers()
		setupSupplier()

		setUpDiscountTypes()
        setupWarehouse()
        setupProduct()
		setupSalesOrder()
        setupSalesOrderItem()
        setupSalesDelivery()
        setupSalesDeliveryItem()
        setupSalesDeliveryAndItemRelationship()
		
		setUpPurchaseInvoices()
		setUpPurchaseOrderItems()
		setUpPurchaseInvoiceItems()
		setUpPurchaseInvoiceAndItemRelationship()
		
		setUpCreditMemos()
		setUpCreditMemoItems()
		setUpCreditMemoAndCreditMemoItemsRelationship()
		
		setUpDebitMemos()
		setUpDebitMemoItems()
		setUpDebitMemoAndDebitMemoItemsRelationship()

        setupMaterialRequisitionItem()
        setupMaterialRelease()
        setupMaterialReleaseItem()
        setupMaterialReleaseAndMaterialReleaseItem()

		setUpJobOrders()
		setUpJobOuts()
		setUpJobOrderAndJobOutsRelationship()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateInitialBalanceEntry() {
		def stockCardItem = StockCardItemFactory.createInitialBalanceEntry()

		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.INITIAL_ENTRY, stockCardItem.type
		assertEquals "created Stock Card Item balance is wrong", BigDecimal.ZERO, stockCardItem.balance
	}
	
    void testCreateUnapprovedSalesDeliveryItem() {
		def stockCardItem = StockCardItemFactory.createUnapprovedSalesDeliveryItem(sampleSalesDeliveryItem)

		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_SALES_DELIVERY, stockCardItem.type
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item qty is wrong", BigDecimal.ONE, stockCardItem.qtyIn
		assertEquals "created Stock Card Item warehouse is wrong", "123", stockCardItem.warehouseIn
		assertNull "created Stock Card Item qty out must be empty", stockCardItem.qtyOut
		assertNull "created Stock Card Item warehouse out must be empty", stockCardItem.warehouseOut
	}

    void testCreateApprovedSalesDeliveryItem() {
		def stockCardItem = StockCardItemFactory.createApprovedSalesDeliveryItem(sampleSalesDeliveryItem)

		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_SALES_DELIVERY, stockCardItem.type
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item qty is wrong", BigDecimal.ONE, stockCardItem.qtyOut
		assertEquals "created Stock Card Item warehouse is wrong", "123", stockCardItem.warehouseOut
		assertNull "created Stock Card Item qty in must be empty", stockCardItem.qtyIn
		assertNull "created Stock Card Item warehouse in must be empty", stockCardItem.warehouseIn
	}
    void testCreateApprovedMaterialReleaseEntry() {
		def stockCardItem = StockCardItemFactory.createApprovedMaterialReleaseItem(sampleMaterialReleaseItem)

		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_MATERIAL_RELEASE, stockCardItem.type
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item qty is wrong", BigDecimal.ONE, stockCardItem.qtyOut
		assertEquals "created Stock Card Item warehouse is wrong", "123", stockCardItem.warehouseOut
		assertEquals "created Stock Card Item local cost is wrong", new BigDecimal("456"), stockCardItem.costLocal
		assertNull "created Stock Card Item qty in must be empty", stockCardItem.qtyIn
		assertNull "created Stock Card Item warehouse in must be empty", stockCardItem.warehouseIn
	}
    void testCreateUnapprovedMaterialReleaseEntry() {
		def stockCardItem = StockCardItemFactory.createUnapprovedMaterialReleaseItem(sampleMaterialReleaseItem)

		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_MATERIAL_RELEASE, stockCardItem.type
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item qty in is wrong", BigDecimal.ONE, stockCardItem.qtyIn
		assertEquals "created Stock Card Item warehouse in is wrong", "123", stockCardItem.warehouseIn
		assertEquals "created Stock Card Item local cost is wrong", new BigDecimal("456"), stockCardItem.costLocal
		assertNull "created Stock Card Item qty out must be empty", stockCardItem.qtyOut
		assertNull "created Stock Card Item warehouse out must be empty", stockCardItem.warehouseOut
	}
	
	void testCreateApprovedPurchaseInvoiceItem() {
		def stockCardItem = StockCardItemFactory.createApprovedPurchaseInvoiceItem(samplePurchaseInvoiceItem)
		
		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("01/01/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_PURCHASE_INVOICE, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", "1 / null", stockCardItem.referenceId
		assertEquals "created Stock Card Item cost (foreign) is wrong", new BigDecimal("27"), stockCardItem.costForeign
		assertEquals "created Stock Card Item cost (local) is wrong", new BigDecimal("13.5"), stockCardItem.costLocal
		assertEquals "created Stock Card Item link id is wrong", samplePurchaseInvoice.id, stockCardItem.linkId
		assertEquals "created Stock Card Item warehouse (in) is wrong", "123", stockCardItem.warehouseIn
		assertEquals "created Stock Card Item qty (in) is wrong", new BigDecimal("20"), stockCardItem.qtyIn
		assertNull "created Stock Card Item qty (out) must be empty", stockCardItem.qtyOut
		assertNull "created Stock Card Item warehouse (out) must be empty", stockCardItem.warehouseOut
	}
	
	void testCreateUnapprovedPurchaseInvoiceItem() {
		def stockCardItem = StockCardItemFactory.createUnapprovedPurchaseInvoiceItem(samplePurchaseInvoiceItem)
		
		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("01/01/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_PURCHASE_INVOICE, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", "1 / null", stockCardItem.referenceId
		assertEquals "created Stock Card Item cost (foreign) is wrong", new BigDecimal("27"), stockCardItem.costForeign
		assertEquals "created Stock Card Item cost (local) is wrong", new BigDecimal("13.5"), stockCardItem.costLocal
		assertEquals "created Stock Card Item link id is wrong", samplePurchaseInvoice.id, stockCardItem.linkId
		assertNull "created Stock Card Item qty (in) must be empty", stockCardItem.qtyIn
		assertNull "created Stock Card Item warehouse (in) must be empty", stockCardItem.warehouseIn
		assertEquals "created Stock Card Item warehouse (out) is wrong", "123", stockCardItem.warehouseOut
		assertEquals "created Stock Card Item qty (out) is wrong", new BigDecimal("20"), stockCardItem.qtyOut
	}
	
	void testCreateApprovedCreditMemoItem() {
		def stockCardItem = StockCardItemFactory.createApprovedCreditMemoItem(sampleCreditMemoItem)

		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("02/10/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_CREDIT_MEMO, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", "CM-00000001", stockCardItem.referenceId
		assertEquals "created Stock Card Item link id is wrong", sampleCreditMemo.id, stockCardItem.linkId
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item amount is wrong", new BigDecimal("53.33"), stockCardItem.sellingAmount
		assertEquals "created Stock Card Item qty is wrong", new BigDecimal("6"), stockCardItem.qtyIn
		assertEquals "created Stock Card Item warehouse is wrong", "123", stockCardItem.warehouseIn
		assertNull "created Stock Card Item qty in must be empty", stockCardItem.qtyOut
		assertNull "created Stock Card Item warehouse in must be empty", stockCardItem.warehouseOut
	}
	
	void testCreateUnapprovedCreditMemoItem() {
		def stockCardItem = StockCardItemFactory.createUnapprovedCreditMemoItem(sampleCreditMemoItem)

		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("02/10/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_CREDIT_MEMO, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", "CM-00000001", stockCardItem.referenceId
		assertEquals "created Stock Card Item link id is wrong", sampleCreditMemo.id, stockCardItem.linkId
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item amount is wrong", new BigDecimal("53.33"), stockCardItem.sellingAmount
		assertEquals "created Stock Card Item qty is wrong", new BigDecimal("6"), stockCardItem.qtyOut
		assertEquals "created Stock Card Item warehouse is wrong", "123", stockCardItem.warehouseOut
		assertNull "created Stock Card Item qty out must be empty", stockCardItem.qtyIn
		assertNull "created Stock Card Item warehouse out must be empty", stockCardItem.warehouseIn
	}
	
	void testCreateApprovedDebitMemoItem() {
		def stockCardItem = StockCardItemFactory.createApprovedCreditMemoItem(sampleDebitMemoItem)

		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("02/10/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_DEBIT_MEMO, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", "CM-00000002", stockCardItem.referenceId
		assertEquals "created Stock Card Item link id is wrong", sampleDebitMemo.id, stockCardItem.linkId
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item amount is wrong", new BigDecimal("-10.23"), stockCardItem.sellingAmount
		assertEquals "created Stock Card Item qty is wrong", new BigDecimal("2"), stockCardItem.qtyIn
		assertEquals "created Stock Card Item warehouse is wrong", "123", stockCardItem.warehouseIn
		assertNull "created Stock Card Item qty in must be empty", stockCardItem.qtyOut
		assertNull "created Stock Card Item warehouse in must be empty", stockCardItem.warehouseOut
	}
	
	void testCreateUnapprovedDebitMemoItem() {
		def stockCardItem = StockCardItemFactory.createUnapprovedCreditMemoItem(sampleDebitMemoItem)

		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("02/10/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_DEBIT_MEMO, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", "CM-00000002", stockCardItem.referenceId
		assertEquals "created Stock Card Item link id is wrong", sampleDebitMemo.id, stockCardItem.linkId
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item amount is wrong", new BigDecimal("-10.23"), stockCardItem.sellingAmount
		assertEquals "created Stock Card Item qty is wrong", new BigDecimal("2"), stockCardItem.qtyOut
		assertEquals "created Stock Card Item warehouse is wrong", "123", stockCardItem.warehouseOut
		assertNull "created Stock Card Item qty out must be empty", stockCardItem.qtyIn
		assertNull "created Stock Card Item warehouse out must be empty", stockCardItem.warehouseIn
	}
	
	void testCreateApprovedJobOutItem() {
		def stockCardItem = StockCardItemFactory.createApprovedJobOutItem(sampleJobOut)

		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("01/01/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.APPROVED_JOB_OUT, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", sampleJobOut.toString(), stockCardItem.referenceId
		assertEquals "created Stock Card Item link id is wrong", sampleJobOut.id, stockCardItem.linkId
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertEquals "created Stock Card Item qty (in) is wrong", new BigDecimal("30"), stockCardItem.qtyIn
		assertEquals "created Stock Card Item warehouse (in) is wrong", "123", stockCardItem.warehouseIn
		assertNull "created Stock Card Item qty (out) must be empty", stockCardItem.qtyOut
		assertNull "created Stock Card Item warehouse (out) must be empty", stockCardItem.warehouseOut
	}
	
	void testCreateUnapprovedJobOutItem() {
		def stockCardItem = StockCardItemFactory.createUnapprovedJobOutItem(sampleJobOut)

		assertEquals "created Stock Card Item date is wrong", generalMethodService.createDate("01/01/2010"), stockCardItem.dateOpened
		assertEquals "created Stock Card Item type is wrong", StockCardItem.Type.UNAPPROVED_JOB_OUT, stockCardItem.type
		assertEquals "created Stock Card Item reference is wrong", sampleJobOut.toString(), stockCardItem.referenceId
		assertEquals "created Stock Card Item link id is wrong", sampleJobOut.id, stockCardItem.linkId
		assertEquals "created Stock Card Item balance is wrong", new BigDecimal("100"), stockCardItem.balance
		assertNull "created Stock Card Item qty (in) must be empty", stockCardItem.qtyIn
		assertNull "created Stock Card Item warehouse (in) must be empty", stockCardItem.warehouseIn
		assertEquals "created Stock Card Item qty (out) is wrong", new BigDecimal("30"), stockCardItem.qtyOut
		assertEquals "created Stock Card Item warehouse (out) is wrong", "123", stockCardItem.warehouseOut
	}

	private void setupWarehouse(){
		sampleWarehouse = new Warehouse(identifier: "123")
		mockDomain(Warehouse,[sampleWarehouse])
	}
	
	private void setupProduct(){
		sampleProduct = new Product()
		mockDomain(Product, [sampleProduct])
		sampleProduct.metaClass.'static'.getTotalStock = { -> new BigDecimal("100") }
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

	private void setupSalesOrder() {
		sampleSalesOrder = new SalesOrder(
			discount: new BigDecimal("20"),
			netDiscount: new BigDecimal("10")
		)
		mockDomain(SalesOrder, [sampleSalesOrder])
	}
	
	private void setupSalesOrderItem(){
		sampleSalesOrderItem = new SalesOrderItem(
				finalPrice: new BigDecimal("2"))
		mockDomain(SalesOrderItem, [sampleSalesOrderItem])
	}

	private void setupSalesDelivery(){
		def sampleDate = generalMethodService.createDate("10/10/2000")
		sampleSalesDelivery = new SalesDelivery(
			date: sampleDate,
			salesDeliveryId: "123",
			customer: sampleCustomer,
			warehouse: sampleWarehouse,
			invoice: sampleSalesOrder)
		mockDomain(SalesDelivery,[sampleSalesDelivery])

	}
	
	private void setupSalesDeliveryItem(){
		sampleSalesDeliveryItem = new SalesDeliveryItem(
			product: sampleProduct,
			orderItem: sampleSalesOrderItem,
			qty: BigDecimal.ONE,
			price: BigDecimal.ONE)
		mockDomain(SalesDeliveryItem,[sampleSalesDeliveryItem])
	}
	
	private void setupSalesDeliveryAndItemRelationship(){
		sampleSalesDelivery.addToItems(sampleSalesDeliveryItem)
		sampleSalesDeliveryItem.delivery = sampleSalesDelivery
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
	
	private void setUpPurchaseInvoices() {
		samplePurchaseInvoice = new PurchaseInvoice(
			date: generalMethodService.createDate("01/01/2010"),
			reference: "1",
			supplier: sampleSupplier,
			exchangeRate: new BigDecimal("0.5"),
			discountRate: new BigDecimal("10"),
			warehouse: sampleWarehouse
		)
		mockDomain(PurchaseInvoice, [samplePurchaseInvoice])
	}
	
	private void setUpPurchaseOrderItems() {
		samplePurchaseOrderItem = new PurchaseOrderItem(
			product: sampleProduct
		)
		mockDomain(PurchaseOrderItem, [samplePurchaseOrderItem])
	}
	
	private void setUpPurchaseInvoiceItems() {
		samplePurchaseInvoiceItem = new PurchaseInvoiceItem(
			purchaseInvoice: samplePurchaseInvoice,
			purchaseOrderItem: samplePurchaseOrderItem,
			finalPrice: new BigDecimal("30"),
			qty: new BigDecimal("20")
		)
		mockDomain(PurchaseInvoiceItem, [samplePurchaseInvoiceItem])
	}
	
	private void setUpPurchaseInvoiceAndItemRelationship(){
		samplePurchaseInvoice.addToItems(samplePurchaseInvoiceItem)
	}
	
	private void setUpDiscountTypes(){
		sampleDiscountType = new DiscountType(
			identifier:"discountType",
			description:"discount type"
		)
	}
	
	private void setUpCreditMemos() {
		sampleCreditMemo = new CreditMemo(
			discountType: sampleDiscountType,
			discount: new BigDecimal("0"),
			status: "Approved",
			customer: sampleCustomer,
			preparedBy: "me",
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
		assertEquals "[GUARD]Should have a value of 66.66", 66.66, sampleCreditMemoItem.computeFinalAmount()
		mockDomain(CreditMemoItem, [sampleCreditMemoItem])
	}
	
	private void setUpCreditMemoAndCreditMemoItemsRelationship() {
		sampleCreditMemo.addToItems(sampleCreditMemoItem)
	}
	
	private void setUpDebitMemos() {
		sampleDebitMemo = new CreditMemo(
			discountType: sampleDiscountType,
			discount: new BigDecimal("0"),
			status: "Approved",
			customer: sampleCustomer,
			preparedBy: "me",
			commissionRate: new BigDecimal("0"),
			date: new Date()
		)
		mockDomain(CreditMemo, [sampleDebitMemo])
	}
	
	private void setUpDebitMemoItems() {
		sampleDebitMemoItem = new CreditMemoItem(
			date:generalMethodService.createDate("02/10/2010"),
			deliveryItem: sampleSalesDeliveryItem,
			oldQty:new BigDecimal("9"),
			oldPrice:new BigDecimal("11.11"),
			newQty:new BigDecimal("7"),
			newPrice:new BigDecimal("16.11")
			)
		assertEquals "[GUARD]Should have a value of -12.78", -12.78, sampleDebitMemoItem.computeFinalAmount()
		mockDomain(CreditMemoItem, [sampleCreditMemoItem])
	}
	
	private void setUpDebitMemoAndDebitMemoItemsRelationship() {
		sampleDebitMemo.addToItems(sampleDebitMemoItem)
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
