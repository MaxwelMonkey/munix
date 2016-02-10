package com.munix

import grails.test.*

class DirectDeliveryTests extends GrailsUnitTestCase {
	
	def sampleDirectDelivery
	def sampleTripTicketDeliveryItem
	
    protected void setUp() {
        super.setUp()
		
		sampleDirectDelivery = new DirectDelivery()
		mockDomain(DirectDelivery, [sampleDirectDelivery])
		
		sampleTripTicketDeliveryItem = new TripTicketDeliveryItem()
		mockDomain(TripTicketDeliveryItem, [sampleTripTicketDeliveryItem])
		
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIsCancelable() {
		assertTrue "Sample direct delivery should be cancelable", sampleDirectDelivery.isCancelable()
    }
	
	void testIsCancelableNotCancelable() {
		sampleDirectDelivery.tripTicket = sampleTripTicketDeliveryItem
		sampleDirectDelivery.save()
		assertFalse "Sample direct delivery should not be cancelable", sampleDirectDelivery.isCancelable()
	}
}
