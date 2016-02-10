package com.munix

import grails.test.*

class DirectPaymentInvoiceTests extends GrailsUnitTestCase {
    def sampleCustomer 
	
	protected void setUp() {
    	sampleCustomer = new Customer(identifier: "customer")
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testSomething() {
		
	}
}
