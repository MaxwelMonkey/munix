package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class JobOutServiceTests extends GrailsUnitTestCase {
    def jobOutService
    def sampleWarehouse
    def sampleWarehouse2
	def sampleDiscountType
    def sampleProduct
    def sampleAssembler
    def sampleJobOrder
    def sampleMaterialRequisition
    def sampleMaterialRequisitionItem
	def sampleMaterialRelease
    def sampleMaterialReleaseItem
    def sampleStock
    def sampleJobOut
    def sampleLaborCost

    protected void setUp() {
        super.setUp()
        setUpAssemblers()
        setUpDiscountTypes()
        setUpProducts()
        setUpJobOrders()
        setUpWarehouses()
        setupStock()
        setupLaborCost()
		setupMaterialRequisition()
		setupMaterialRequisitionItem()
		setupMaterialRelease()
		setupMaterialReleaseItem()
        setupJobOut()
    }
	
    protected void tearDown() {
        super.tearDown()
    }

    void testUpdateStocksInJobOut() {
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
        jobOutService.updateStocksInJobOut(sampleJobOut)
        assertEquals "stock qty must be 3", 3, sampleStock.qty
    }
	
    void testRemoveStocksInJobOut() {
        assertEquals "Guard stock qty must be 0", 0, sampleStock.qty
        jobOutService.removeStocksInJobOut(sampleJobOut)
        assertEquals "stock qty must be -3", -3, sampleStock.qty
    }
	
    void testIsUnapprovable(){
        def result = jobOutService.isUnapprovable(sampleJobOut)
        assertTrue "result must be true", result
    }
	
    void testIsUnapprovableJobOrderIsCompleted(){
        sampleJobOrder.markAsCompleteBy = "hsad"
        sampleJobOrder.complete()
        sampleJobOrder.save()
        assertNotNull "job order mark as complete must not be empty", sampleJobOrder.markAsCompleteBy
        def result = jobOutService.isUnapprovable(sampleJobOut)
        assertFalse "result must be false", result
    }
	
	void testValidateQuantity() {
		def remaining = sampleJobOut.jobOrder.computeRemainingBalance()
		
		def jobOut = new JobOut(
			qty: remaining,
			warehouse: sampleWarehouse,
			laborCost: sampleLaborCost,
			preparedBy: "me",
			jobOrder: sampleJobOrder
		)
		jobOut.save()
		
		assertTrue "job out should be valid", jobOutService.validateQuantity(jobOut.qty, remaining)
	}
	
	void testValidateQuantityMoreThanRemaining() {
		def remaining = sampleJobOut.jobOrder.computeRemainingBalance()
		
		def jobOut = new JobOut(
			qty: remaining + 1,
			warehouse: sampleWarehouse,
			laborCost: sampleLaborCost,
			preparedBy: "me",
			jobOrder: sampleJobOrder
		)
		jobOut.save()
		
		assertFalse "job out should be invalid because of quantity exceeds remaining balance", jobOutService.validateQuantity(jobOut.qty, remaining)
	}
	
	void testApprove() {
		def mockAuthenticateService = mockFor(AuthenticateService)
		mockAuthenticateService.demand.userDomain(1..1){->
			return new User(username : "user")
		}
		jobOutService.authenticateService = mockAuthenticateService.createMock()
		def mockStockCardService = mockFor(StockCardService)
		mockStockCardService.demand.createApprovedJobOut(1..1){x ->
			return null
		}
		jobOutService.stockCardService = mockStockCardService.createMock()

		jobOutService.approve(sampleJobOut)

		assertEquals "sample job out should be approved", JobOut.Status.APPROVED, sampleJobOut.status
		assertEquals "sample stock's quantity should increase", sampleJobOut.qty, sampleStock.qty
		assertEquals "sample product's running cost should update", new BigDecimal("1234.0000"), sampleProduct.runningCost
	}
	
	private void setupStock(){
		sampleStock = new Stock(
			warehouse:sampleWarehouse,
			product:sampleProduct
		)
		sampleProduct.addToStocks(sampleStock)
		sampleProduct.save()
	}
	
	private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier:"WH", description:"White House")
		sampleWarehouse.save()

		sampleWarehouse2 = new Warehouse(identifier:"BUL", description:"Bulacan")
		sampleWarehouse2.save()
	}
	
	private void setUpDiscountTypes() {
		sampleDiscountType=new DiscountType(identifier:"discountType",
				description:"discount type", margin: BigDecimal.ONE)
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
			qty: 10,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		sampleJobOrder.save(flush: true)
	}

	private void setupLaborCost(){
		sampleLaborCost = new LaborCost(
			type:"1234",
			amount:new BigDecimal("1234"),
			product: sampleProduct
		)
		sampleLaborCost.save()
	}
	
	private void setupJobOut(){
		sampleJobOut = new JobOut(
			date: new Date(),
			qty:3,
			warehouse:sampleWarehouse,
			laborCost:sampleLaborCost,
			preparedBy:"me",
			jobOrder:sampleJobOrder,
			status: JobOut.Status.UNAPPROVED
		)
		sampleJobOut.save()
		sampleJobOrder.addToJobOuts(sampleJobOut)
		sampleJobOrder.save()
	}
	
	private void setupMaterialRequisition(){
		sampleMaterialRequisition = new MaterialRequisition(
			date: new Date(),
			preparedBy: "user",
			jobOrder: sampleJobOrder
		)
		sampleMaterialRequisition.save()
		sampleJobOrder.requisition = sampleMaterialRequisition
		sampleJobOrder.save()
	}
	
	private void setupMaterialRequisitionItem(){
		sampleMaterialRequisitionItem = new MaterialRequisitionItem(
			component: sampleProduct, 
			unitsRequired: 10
		)
        sampleMaterialRequisition.addToItems(sampleMaterialRequisitionItem)
        sampleMaterialRequisition.save()
	}
	
	private void setupMaterialRelease(){
		sampleMaterialRelease = new MaterialRelease(
			date: new Date(),
			status: MaterialRelease.Status.UNAPPROVED,
			preparedBy: "me",
			jobOrder: sampleJobOrder,
			warehouse: sampleWarehouse
		)
		sampleMaterialRelease.save()
	}
	
	private void setupMaterialReleaseItem(){
		sampleMaterialReleaseItem = new MaterialReleaseItem(
			materialRequisitionItem: sampleMaterialRequisitionItem, 
			qty: 10, 
			cost: 1000
		)
		sampleMaterialReleaseItem.save()
		sampleMaterialRelease.addToItems(sampleMaterialReleaseItem)
		sampleMaterialRelease.save()
	}
	
}
