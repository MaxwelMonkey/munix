package com.munix

class MaterialReleaseServiceTests extends GroovyTestCase {
    def materialReleaseService
    def sampleMaterialRelease
    def sampleWarehouse
    def sampleWarehouse2
	def sampleDiscountType
    def sampleProduct
    def sampleAssembler
    def sampleJobOrder
    def sampleMaterialRequisition
    def sampleMaterialRequisitionItem
    def sampleMaterialReleaseItem
    def sampleStock
    protected void setUp() {
        super.setUp()
        setUpAssemblers()
        setUpDiscountTypes()
        setUpProducts()
        setUpJobOrders()
        setUpWarehouses()
        setupStock()
        setupMaterialRequisition()
        setupMaterialRequisitionItem()
        setupMaterialRelease()
        setupMaterialReleaseItem()
    }
    private void setupStock(){
        sampleStock = new Stock(
            warehouse:sampleWarehouse,
            product:sampleProduct
        )
        sampleProduct.addToStocks(sampleStock)
        sampleProduct.save()
    }
    private void setupMaterialRelease(){
        sampleMaterialRelease = new MaterialRelease(
            warehouse:sampleWarehouse,
            jobOrder:sampleJobOrder,
        )
        sampleMaterialRelease.save()
    }
    private void setupMaterialReleaseItem(){
        sampleMaterialReleaseItem = new MaterialReleaseItem(
            materialRequisitionItem:sampleMaterialRequisitionItem,
            qty:1,
            materialRelease:sampleMaterialRelease
        )
        sampleMaterialRelease.addToItems(sampleMaterialReleaseItem)
        sampleMaterialRelease.save()
        sampleJobOrder.addToReleases(sampleMaterialRelease)
        sampleJobOrder.save()
    }
    private void setupMaterialRequisition(){
        sampleMaterialRequisition = new MaterialRequisition(
            preparedBy:"me",
            jobOrder:sampleJobOrder
        )
        sampleMaterialRequisition.save()
    }
    private void setupMaterialRequisitionItem(){
        sampleMaterialRequisitionItem = new MaterialRequisitionItem(
            component:sampleProduct,
            unitsRequired:1,
            requisition:sampleMaterialRequisition
        )
        sampleMaterialRequisition.addToItems(sampleMaterialRequisitionItem)
        sampleMaterialRequisition.save()
    }
    private setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier:"WH", description:"White House")
		sampleWarehouse.save()

		sampleWarehouse2 = new Warehouse(identifier:"BUL", description:"Bulacan")
		sampleWarehouse2.save()
	}
	
	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(
			identifier: "discount type",
			description: "desc"
		)
		sampleDiscountType.save()
	}
	
    private void setUpProducts() {
		sampleProduct = new Product(
			identifier: "product",
			type: sampleDiscountType
		)
		sampleProduct.save(flush: true)
		sampleProduct.metaClass.'static'.formatDescription = {-> return "description"}
	}

	private void setUpAssemblers() {
		sampleAssembler = new Assembler(
			identifier: "assembler",
			description: "assembler"
		)
		sampleAssembler.save(flush: true)
	}

	private void setUpJobOrders() {
		sampleJobOrder = new JobOrder(
			product: sampleProduct,
			remark: "remarkable",
			startDate: new Date(),
			targetDate: new Date(),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		sampleJobOrder.save(flush: true)
    }
    protected void tearDown() {
        super.tearDown()
    }

    void testReleaseStocksInWarehouse() {
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
        materialReleaseService.releaseStocksInWarehouse(sampleMaterialRelease)
        assertEquals "Guard stock qty must be -1", -1, sampleStock.qty
    }
    void testReleaseStocksInWarehouseMaterialReleaseHasNoItems() {
        sampleMaterialRelease.removeFromItems(sampleMaterialReleaseItem)
        sampleMaterialRelease.save()
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
        assertTrue "Guard materail release items must be empty", sampleMaterialRelease.items.isEmpty()
        materialReleaseService.releaseStocksInWarehouse(sampleMaterialRelease)
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
    }
    void testReturnStocksToWarehouse() {
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
        materialReleaseService.returnStocksToWarehouse(sampleMaterialRelease)
        assertEquals "Guard stock qty must be 1", 1, sampleStock.qty
    }
    void testReturnStocksToWarehouseMaterialReleaseHasNoItems() {
        sampleMaterialRelease.removeFromItems(sampleMaterialReleaseItem)
        sampleMaterialRelease.save()
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
        assertTrue "Guard materail release items must be empty", sampleMaterialRelease.items.isEmpty()
        materialReleaseService.returnStocksToWarehouse(sampleMaterialRelease)
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
    }
    void testIsApprovable(){
        def result = materialReleaseService.isApprovable(sampleMaterialRelease)
        assertTrue "The result must be true", result
    }
    void testIsApprovableFalseReturn(){
        sampleMaterialReleaseItem.qty = 2
		sampleMaterialRelease.approve()
        sampleMaterialRelease.save()
        def result = materialReleaseService.isApprovable(sampleMaterialRelease)
        assertFalse "The result must be false", result
    }
    void testIsUnapprovable(){
        def result = materialReleaseService.isUnapprovable(sampleMaterialRelease)
        assertTrue "The result must be true", result
    }
    void testIsUnapprovableFalseReturn(){
        sampleJobOrder.materialReleasesApprovedBy = "1234"
        sampleJobOrder.save()
        def result = materialReleaseService.isUnapprovable(sampleMaterialRelease)
        assertFalse "The result must be false", result
    }
	void testHasNoReleaseValuesQuantityGreaterZero(){
		def result = materialReleaseService.hasNoReleaseValues(sampleMaterialRelease)
		assertFalse "The result must be false", result
	}
	void testHasNoReleaseValuesQuantityZero(){
		sampleMaterialReleaseItem.qty = 0
		sampleMaterialRelease.save()
		def result = materialReleaseService.hasNoReleaseValues(sampleMaterialRelease)
		assertTrue "The result must be true", result
	}
	void testGetRemainingBalanceListNotNull(){
		def result = materialReleaseService.getRemainingBalanceList(sampleMaterialRelease)
		assertNotNull "Remaining Balance List should not be empty", result
	}
}
