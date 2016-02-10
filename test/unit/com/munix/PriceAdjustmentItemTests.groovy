package com.munix

import grails.test.*

class PriceAdjustmentItemTests extends GrailsUnitTestCase {
	def samplePriceAdjustmentItem
	
    protected void setUp() {
        super.setUp()
		
		samplePriceAdjustmentItem = new PriceAdjustmentItem(
			oldPrice: new BigDecimal("100"),
			newPrice: new BigDecimal("200")
		)
		mockDomain(PriceAdjustmentItem, [samplePriceAdjustmentItem]) 
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testComputeMargin() {
		def margin = samplePriceAdjustmentItem.computeMargin()
		
		assertEquals "wrong margin", new BigDecimal("100"), margin
    }
	
	void testComputeMarginZeroOldPrice() {
		samplePriceAdjustmentItem.oldPrice = BigDecimal.ZERO
		
		def margin = samplePriceAdjustmentItem.computeMargin()
		
		assertEquals "wrong margin", BigDecimal.ZERO, margin
	}

}
