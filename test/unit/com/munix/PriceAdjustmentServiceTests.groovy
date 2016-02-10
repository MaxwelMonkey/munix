package com.munix

import grails.test.*

class PriceAdjustmentServiceTests extends GrailsUnitTestCase {
    def priceAdjustmentService = new PriceAdjustmentService()
    def generalMethodService = new GeneralMethodService()
    def samplePriceAdjustment
    protected void setUp() {
        super.setUp()
        priceAdjustmentService.generalMethodService = new GeneralMethodService()
        setupPriceAdjustment()
    }
    private setupPriceAdjustment(){
        samplePriceAdjustment = new PriceAdjustment()
        mockDomain(PriceAdjustment, [samplePriceAdjustment])
    }
    protected void tearDown() {
        super.tearDown()
    }

    void testCheckIfUnapprovable() {
        def today = generalMethodService.dateToday()
        samplePriceAdjustment.effectiveDate = today
        samplePriceAdjustment.save()
        assertEquals "The effective date of price adjustment must be today", today, samplePriceAdjustment.effectiveDate
        def result = priceAdjustmentService.checkIfUnapprovable(samplePriceAdjustment)
        assertFalse "The price adjustment should not be unapprovable", result
    }
    void testCheckIfUnapprovableTomorrow() {
        def today = Calendar.getInstance()
		def tomorrow = generalMethodService.createDate((today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.DATE)+1 + "/" + today.get(Calendar.YEAR))
        samplePriceAdjustment.effectiveDate = tomorrow
        samplePriceAdjustment.save()
        assertEquals "The effective date of price adjustment must be tomorrow", tomorrow, samplePriceAdjustment.effectiveDate
        def result = priceAdjustmentService.checkIfUnapprovable(samplePriceAdjustment)
        assertTrue "The price adjustment should be unapprovable", result
    }
    void testCheckIfUnapprovableYesterday() {
		def today = generalMethodService.dateToday()
		def yesterday = generalMethodService.performDayOperationsOnDate(Operation.SUBTRACT, today, 1)
        samplePriceAdjustment.effectiveDate = yesterday
        samplePriceAdjustment.save()
        assertEquals "The effective date of price adjustment must be tomorrow", yesterday, samplePriceAdjustment.effectiveDate
        def result = priceAdjustmentService.checkIfUnapprovable(samplePriceAdjustment)
        assertFalse "The price adjustment should be unapprovable", result
    }
}
