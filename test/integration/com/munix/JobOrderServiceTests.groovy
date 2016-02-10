package com.munix

import grails.test.*

class JobOrderServiceTests extends GrailsUnitTestCase {
	def jobOrderService
	def generalMethodService

	def sampleDiscountType	
	def sampleProduct
	def sampleAssembler
	def sampleJobOrder
	def sampleMaterialRequisition
	def sampleMaterialRequisitionItem

    protected void setUp() {
        super.setUp()
		
		setUpDiscountTypes()
		setUpProducts()
		setUpAssemblers()
        setupJobOrder()
        setupMaterialRequisition()
        setupMaterialRequisitionItem()

    }

    protected void tearDown() {
        super.tearDown()

    }
    void testUnMarkarbleAsComplete(){
        sampleJobOrder.markAsCompleteBy = "me "+generalMethodService.getDateString(new Date(), "MMM. dd, yyyy - hh:mm a")
        println sampleJobOrder.markAsCompleteBy
        def result = jobOrderService.unmarkableAsComplete(sampleJobOrder)
        assertTrue "the result must be true", result
    }

    void testUnMarkarbleAsCompleteTwoDaysAfter(){
        def twoDaysAfter = generalMethodService.performDayOperationsOnDate(Operation.ADD, new Date(), 2)
        sampleJobOrder.markAsCompleteBy = "me "+generalMethodService.getDateString(twoDaysAfter, "MMM. dd, yyyy - hh:mm a")
        def result = jobOrderService.unmarkableAsComplete(sampleJobOrder)
        assertTrue "the result must be true", result
    }
    void testUnMarkarbleAsCompleteTwoDaysBefore(){
        def twoDaysBefore = generalMethodService.performDayOperationsOnDate(Operation.SUBTRACT, new Date(), 2)
        sampleJobOrder.markAsCompleteBy = "me "+generalMethodService.getDateString(twoDaysBefore, "MMM. dd, yyyy - hh:mm a")
        def result = jobOrderService.unmarkableAsComplete(sampleJobOrder)
        assertFalse "the result must be false", result
    }
    void testUnMarkarbleAsCompleteOneDayBefore(){
        def twoDaysBefore = generalMethodService.performDayOperationsOnDate(Operation.SUBTRACT, new Date(), 1)
        sampleJobOrder.markAsCompleteBy = "me "+generalMethodService.getDateString(twoDaysBefore, "MMM. dd, yyyy - hh:mm a")
        def result = jobOrderService.unmarkableAsComplete(sampleJobOrder)
        assertTrue "the result must be true", result
    }
	
    void testGenerateList() {
		def jobOrder = new JobOrder(
			product: sampleProduct,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrder.save(flush: true)
		
		setUpFilteredJobOrders()
		
		def params = [:]
		params.searchId = jobOrder.id
		params.searchIdentifier = "product"
		params.searchDescription = "description"
		params.assignedTo = "assembler"
		params.searchRemarks = "remarkable"
		params.searchStatus = "Unapproved"
		params.searchStartDateFrom = "01/01/2010"
		params.searchStartDateFrom_value = "01/01/2010"
		params.searchStartDateFrom_month = "01"
		params.searchStartDateFrom_day = "01"
		params.searchStartDateFrom_year = "2010"
		params.searchStartDateTo = "01/01/2011"
		params.searchStartDateTo_value = "01/01/2011"
		params.searchStartDateTo_month = "01"
		params.searchStartDateTo_day = "01"
		params.searchStartDateTo_year = "2011"
		params.searchTargetDateFrom = "01/01/2010"
		params.searchTargetDateFrom_value = "01/01/2010"
		params.searchTargetDateFrom_month = "01"
		params.searchTargetDateFrom_day = "01"
		params.searchTargetDateFrom_year = "2010"
		params.searchTargetDateTo = "01/01/2011"
		params.searchTargetDateTo_value = "01/01/2011"
		params.searchTargetDateTo_month = "01"
		params.searchTargetDateTo_day = "01"
		params.searchTargetDateTo_year = "2011"
		params.searchEndDateFrom = "01/01/2010"
		params.searchEndDateFrom_value = "01/01/2010"
		params.searchEndDateFrom_month = "01"
		params.searchEndDateFrom_day = "01"
		params.searchEndDateFrom_year = "2010"
		params.searchEndDateTo = "01/01/2011"
		params.searchEndDateTo_value = "01/01/2011"
		params.searchEndDateTo_month = "01"
		params.searchEndDateTo_day = "01"
		params.searchEndDateTo_year = "2011"

		def result = jobOrderService.generateList(params)
		
		assertTrue "Result should contain jobOrder", result.jobOrders.contains(jobOrder)
		assertEquals "Result total should be 1", 1, result.jobOrdersTotal
    }

    void testGetNotCompletedRequisitionItems(){
        sampleJobOrder.metaClass.'static'.computeRequisitionItemRemainingBalance = {-> return 10}
        def result = jobOrderService.getNotCompletedRequisitionItems(sampleJobOrder)

        assertEquals "the item obtained must be the requisition item",sampleMaterialRequisitionItem, result[0].item
        assertEquals "the remaining balance must be 10",10, result[0].remainingBalance
    }
	private void setupJobOrder(){
        sampleJobOrder = new JobOrder(
            product:sampleProduct,
            startDate: new Date(),
            targetDate:new Date(),
            qty:10,
            assignedTo:sampleAssembler,
            preparedBy:"me"
        )
        sampleJobOrder.save()
    }
    private void setupMaterialRequisition(){
        sampleMaterialRequisition = new MaterialRequisition(
            preparedBy:"me",
            jobOrder:sampleJobOrder
        )
        sampleMaterialRequisition.save()
        sampleJobOrder.requisition = sampleMaterialRequisition
        sampleJobOrder.save()

    }
    private void setupMaterialRequisitionItem(){
        sampleMaterialRequisitionItem = new MaterialRequisitionItem(
            component:sampleProduct,
            unitsRequired:1
        )
        sampleMaterialRequisition.addToItems(sampleMaterialRequisitionItem)
        sampleMaterialRequisition.save()
    }

	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(
			identifier:"discountType",
			description:"discount type", 
			margin: BigDecimal.ONE)
		sampleDiscountType.save()
	}
	
	private void setUpProducts() {
		sampleProduct = new Product(
			identifier: "product",
			type: sampleDiscountType)
		sampleProduct.save()
		sampleProduct.metaClass.'static'.formatDescription = {-> return "description"}
	}
	
	private void setUpAssemblers() {
		sampleAssembler = new Assembler(
			identifier: "assembler",
			description: "assembler"
		)
		sampleAssembler.save(flush: true)
	}
	
	private void setUpFilteredJobOrders() {
		def jobOrderFilteredById = new JobOrder(
			product: sampleProduct,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrderFilteredById.save(flush: true)
		
		def product = new Product(identifier: "nooo", type: sampleDiscountType)
		product.save(flush: true)
		product.metaClass.'static'.formatDescription = {-> return "description"}
		
		def jobOrderFilteredByIdentifier = new JobOrder(
			product: product,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		
		def product2 = new Product(identifier: "productive", type: sampleDiscountType)
		product2.save(flush: true)
		product2.metaClass.'static'.formatDescription = {-> return "nooo"}
		
		def jobOrderFilteredByDescription = new JobOrder(
			product: product2,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrderFilteredByDescription.save(flush: true)

		def assembler = new Assembler(
			identifier: "nooo",
			description: "nooo"
		)
		assembler.save(flush: true)
		
		def jobOrderFilteredByAssignedTo = new JobOrder(
			product: sampleProduct,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: assembler,
			preparedBy: "me"
		)
		jobOrderFilteredByAssignedTo.save(flush: true)
		
		def jobOrderFilteredByRemark = new JobOrder(
			product: sampleProduct,
			remark: "noooo",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrderFilteredByRemark.save(flush: true)

		def jobOrderFilteredByStatus = new JobOrder(
			product: sampleProduct,
			status: "Material Releases Approved",
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrderFilteredByStatus.save(flush: true)
		
		def jobOrderFilteredByStartDate = new JobOrder(
			product: sampleProduct,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2011"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrderFilteredByStartDate.save(flush: true)

		def jobOrderFilteredByTargetDate = new JobOrder(
			product: sampleProduct,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2011"),
			endDate: generalMethodService.createDate("06/01/2010"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrderFilteredByTargetDate.save(flush: true)

		def jobOrderFilteredByEndDate = new JobOrder(
			product: sampleProduct,
			remark: "remarkable",
			startDate: generalMethodService.createDate("06/01/2010"),
			targetDate: generalMethodService.createDate("06/01/2010"),
			endDate: generalMethodService.createDate("06/01/2011"),
			qty: BigDecimal.ONE,
			assignedTo: sampleAssembler,
			preparedBy: "me"
		)
		jobOrderFilteredByEndDate.save(flush: true)
	}	
}
