package com.munix

import grails.test.*

class CustomerTests extends GrailsUnitTestCase {
	def sampleCustomer
	def sampleCustomerLedger
	
    protected void setUp() {
        super.setUp()
		
		sampleCustomer = new Customer()
		mockDomain(Customer, [sampleCustomer])
		
		sampleCustomerLedger = new CustomerLedger()
		mockDomain(CustomerLedger, [sampleCustomerLedger])
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	public static Customer createSampleCustomer() {
		return new Customer(identifier:"customer",
			name:"customer name",
			busAddrStreet:"here",
			busAddrCity:CityTests.createSampleCity(),
			busAddrZip:"1234",
			bilAddrStreet:"1234 here",
			bilAddrCity:CityTests.createSampleCity(),
			bilAddrZip:"1234",
			skype:"hi",
			email:"1@yahoo.com",
			landline:"1234",
			fax:"1234",
			contact:"1234",
			mobile:"1234",
			type:CustomerTypeTests.createSampleCustomerType(),
			term:TermTests.createSampleTerm(),
			status:Customer.Status.ACTIVE,
			forwarder:ForwarderTests.createSampleForwarder(),
			salesAgent:SalesAgentTests.createSampleSalesAgent(),
			autoApprove:false,
			enableCreditLimit:true,
			accountName1:"1234",
			accountNumber1:"12345",
			tin:"1234",
			collectionMode:"12345",
			collectionPreference:"12345",
			collectionSchedule:"12345",
			generalRemark:"2345",
			creditRemark:"12345",
			contactPosition:"12345",
			owner:"12345",
			yahoo:"12345",
			secondaryContact:"12345",
			secondaryContactPosition:"12345")
	}

    void testRemainingCredit() {
		sampleCustomer.creditLimit = new BigDecimal("1000")
		sampleCustomer.customerLedger = sampleCustomerLedger
		sampleCustomerLedger.balance = new BigDecimal("500")
		
		def result = sampleCustomer.remainingCredit
		
		assertEquals "wrong computation of remaining credit limit", new BigDecimal("500"), result
    }
}
