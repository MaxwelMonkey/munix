package com.munix

import grails.test.*

class PriceAdjustmentServiceTests extends GroovyTestCase {
    def priceAdjustmentService
    def samplePriceAdjustment
    def samplePriceAdjustment2
    def sampleItemType
	def sampleDiscountType
    def sampleProduct
    def generalMethodService
		
    protected void setUp() {
        super.setUp()
        setupItemType()
		setupDiscountType()
        setupProduct()
        setupPriceAdjustments()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
    void testCheckIfApprovable() {
        def result = priceAdjustmentService.checkIfApprovable(samplePriceAdjustment)
        assertEquals "Price Adjustment should be approvable","Approvable", result
    }
	
    void testCheckIfApprovableExistingApprovedPriceAdjustment() {
        samplePriceAdjustment2.status = PriceAdjustment.Status.APPROVED
        samplePriceAdjustment2.save()
        assertTrue "[GUARD]Price Adjustment2 must be approved", samplePriceAdjustment2.isApproved()
        def result = priceAdjustmentService.checkIfApprovable(samplePriceAdjustment)
        assertEquals "Price Adjustment should be approvable","Approvable", result
    }
	
    void testCheckIfApprovableExistingApprovedPriceAdjustmentAndSamePriceType() {
        samplePriceAdjustment2.status = PriceAdjustment.Status.APPROVED
        samplePriceAdjustment2.priceType = PriceType.WHOLESALE
		samplePriceAdjustment2.itemType = sampleItemType
        samplePriceAdjustment2.save(flush:true)
        assertTrue "[GUARD]Price Adjustment2 must be approved", samplePriceAdjustment2.isApproved()
        assertEquals "[GUARD]Price Adjustment2 must be wholesale",samplePriceAdjustment2.priceType, PriceType.WHOLESALE
		assertEquals "[GUARD]Price Adjustment2 must be sampleItemType",samplePriceAdjustment2.itemType, sampleItemType
        assertEquals "[GUARD]Price Adjustment2 must have the same effective date",samplePriceAdjustment2.effectiveDate, samplePriceAdjustment.effectiveDate
        def result = priceAdjustmentService.checkIfApprovable(samplePriceAdjustment)
        assertEquals "Price Adjustment should be PriceTypeAndItemTypeError","PriceTypeAndItemTypeError", result
    }
	
    void testCheckIfApprovableInvalidDate() {
        def today = generalMethodService.dateToday()
		def yesterday = generalMethodService.performDayOperationsOnDate(Operation.SUBTRACT, today, 1)
        samplePriceAdjustment.effectiveDate = yesterday
        samplePriceAdjustment.save(flush:true)
        assertEquals "[GUARD]Price Adjustment must have the same effective date",yesterday, samplePriceAdjustment.effectiveDate
        def result = priceAdjustmentService.checkIfApprovable(samplePriceAdjustment)
        assertEquals "Price Adjustment should be DATEERROR","DateError", result
    }
	
	void testGenerateList() {
		samplePriceAdjustment.status = PriceAdjustment.Status.APPLIED
		samplePriceAdjustment.save()
		
		def params = [:]
		params.searchIdentifier = samplePriceAdjustment.id
		params.searchStatus = "Applied"
		params.searchPriceType = "Wholesale"
		params.searchItemType = "Parts"
		params.searchDateGeneratedFrom = "01/01/2010"
		params.searchDateGeneratedTo = "01/01/2011"
		params.searchEffectiveDateFrom = "05/05/2500"
		params.searchEffectiveDateTo = "05/05/2500"

		def result = priceAdjustmentService.generateList(params)

		assertTrue "Result should contain samplePriceAdjustment", result.contains(samplePriceAdjustment)
		assertEquals "Result total should be 1", 1, result.totalCount
	}

    void testIsCancelable(){
        samplePriceAdjustment.status = PriceAdjustment.Status.UNAPPROVED
		samplePriceAdjustment.save()
        assertEquals "[guard]price adjustment should be unapproved", PriceAdjustment.Status.UNAPPROVED, samplePriceAdjustment.status

        def result = priceAdjustmentService.isCancelable(samplePriceAdjustment)

        assertTrue "price adjustment should be approved", result
    }
    void testIsCancelableNot(){
        samplePriceAdjustment.status = PriceAdjustment.Status.APPLIED
		samplePriceAdjustment.save()
        assertNotSame "[guard]price adjustment should be unapproved", PriceAdjustment.Status.UNAPPROVED, samplePriceAdjustment.status

        def result = priceAdjustmentService.isCancelable(samplePriceAdjustment)

        assertFalse "price adjustment should be approved", result
    }
	void testQueryAvailableProducts() {
		def result = priceAdjustmentService.queryAvailableProducts(sampleItemType, "1234")
		
		assertTrue "Result should contain sample product", result.contains(sampleProduct)
		assertEquals "Result should only return 1 product", 1, result.size()
	}
	
	private void setupItemType(){
		sampleItemType = new ItemType(
			identifier: "Parts",
			description: "Parts"
		)
		sampleItemType.save(flush: true)
	}
	
	private void setupDiscountType() {
		sampleDiscountType = new DiscountType(
			identifier: "discount type",
			description: "desc"
		) 
		sampleDiscountType.save()
	}
	
	private void setupProduct(){
		sampleProduct = new Product(
			identifier:"1234",
			itemType: sampleItemType,
			type: sampleDiscountType
		)
		sampleProduct.save(flush: true)
	}
	
	private void setupPriceAdjustments() {
		samplePriceAdjustment = new PriceAdjustment(
			dateGenerated: generalMethodService.createDate("01/01/2010"),
			effectiveDate: generalMethodService.createDate("05/05/2500"),
			preparedBy:"me",
			priceType:PriceType.WHOLESALE,
			itemType: sampleItemType,
			items:new PriceAdjustmentItem(product:sampleProduct,
				newPrice:new BigDecimal("1234"), oldPrice:BigDecimal.ONE)
		)
		samplePriceAdjustment.save(flush:true)
		
		samplePriceAdjustment2 = new PriceAdjustment(
			effectiveDate: generalMethodService.createDate("05/05/2500"),
			preparedBy:"me",
			priceType:PriceType.RETAIL,
			itemType: sampleItemType,
			items:new PriceAdjustmentItem(product:sampleProduct,
				newPrice:new BigDecimal("1234"), oldPrice:BigDecimal.ONE)
		)
		samplePriceAdjustment2.save(flush:true)
	}
}
