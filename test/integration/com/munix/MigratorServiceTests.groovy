package com.munix

import grails.test.*

class MigratorServiceTests extends GrailsUnitTestCase {
	def migratorService
	
	def sampleTerm
	def sampleCity
	def sampleCustomerType
	
    protected void setUp() {
        super.setUp()
		setupTerms()
		setUpCities()
		setUpCustomerTypes()
		setUpCustomer("cust1")
		setUpCustomer("cust2")
		setUpCustomer("cust3")
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerateCustomerLedgers() {
		assertEquals "[GUARD] Customer Ledger should have no entries", 0, CustomerLedger.list().size()
		
		migratorService.generateCustomerLedgers()
		
		assertEquals "Customer Ledger should have 3 entries", 3, CustomerLedger.list().size()
    }
	
	private void setUpCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		sampleCity.save(flush: true)
	}

	private void setUpCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier: "customerType", description: "descCustomerType")
		sampleCustomerType.save(flush: true)
	}
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
	private void setUpCustomer(String customerName) {
		def sampleCustomer = new Customer(
			identifier: customerName,
			name: "customer name",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
			term: sampleTerm
		)
		sampleCustomer.save(flush: true)
	}
}
