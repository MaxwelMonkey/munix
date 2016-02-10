package com.munix

import grails.test.*

class CustomerLedgerEntryTests extends GrailsUnitTestCase {
	def sampleCustomerLedgerEntry
	
    protected void setUp() {
        super.setUp()
		sampleCustomerLedgerEntry = new CustomerLedgerEntry(
			type: CustomerLedgerEntry.Type.APPROVED_SALES_DELIVERY,
			linkId: 1L
		)
		mockDomain(CustomerLedgerEntry, [sampleCustomerLedgerEntry])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerateLink() {
		def link = sampleCustomerLedgerEntry.generateLink()
		
		assertEquals "Link id should be 1", link.id, sampleCustomerLedgerEntry.linkId
		assertEquals "Link controller should be salesDelivery", link.controller, "salesDelivery"
		assertEquals "Link action should be show", link.action, "show" 
    }
}
