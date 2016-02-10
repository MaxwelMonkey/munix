package com.munix

class CustomerServiceTests extends GroovyTestCase {
    def customerService
    def sampleCustomer
    def sampleCity
    def sampleCustomerType
    def sampleSalesAgent
    def sampleTerm

    protected void setUp() {
        super.setUp()
        setUpCities()
        setUpCustomerTypes()
        setUpSalesAgent()
        setupTerms()
        setUpCustomers()

    }

    protected void tearDown() {
        super.tearDown()
    }

    private void setUpCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		sampleCity.save(flush: true)
	}
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
	private void setUpCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier: "customerType", description: "descCustomerType")
		sampleCustomerType.save(flush: true)
	}
    private void setUpSalesAgent() {
		sampleSalesAgent= new SalesAgent(identifier: "salesAgent",
			lastName: "last",
			firstName: "first",
			street: "street",
			city: sampleCity,
			zip: "1234",
			mobile: "1233",
			landline: "12345",
			skype: "nothing.com",
			yahoo: "1@yahoo.com",
			email: "something@samthing.com")
		sampleSalesAgent.save()
	}
	private void setUpCustomers() {
		sampleCustomer = new Customer(
			identifier: "customer",
			name: "customer name",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
            salesAgent:sampleSalesAgent,
            term:sampleTerm
		)
		sampleCustomer.save(flush: true)
	}

    void testGetListOfCustomersWithCriteriaHasSearchForIdentifier() {
        def objectParam = [:]
        objectParam["searchIdentifier"]="customer"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }
    void testGetListOfCustomersWithCriteriaHasSearchForIdentifierIncompleteInput() {
        def objectParam = [:]
        objectParam["searchIdentifier"]="custom"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }
    void testGetListOfCustomersWithCriteriaHasSearchForIdentifierNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchIdentifier"]="1234"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetListOfCustomersWithCriteriaHasSearchForName() {
        def objectParam = [:]
        objectParam["searchName"]="customer name"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }
    void testGetListOfCustomersWithCriteriaHasSearchForNameIncompleteInput() {
        def objectParam = [:]
        objectParam["searchName"]="custom"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }
    void testGetListOfCustomersWithCriteriaHasSearchForNameNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchName"]="4321"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetListOfCustomersWithCriteriaHasSearchForStatus() {
        def objectParam = [:]
        objectParam["searchStatus"]="Active"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }
    void testGetListOfCustomersWithCriteriaHasSearchForStatusNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchStatus"]="Inactive"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetListOfCustomersWithCriteriaHasSearchForSalesAgent() {
        def objectParam = [:]
        objectParam["searchSalesAgent"]="salesAgent"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }
    void testGetListOfCustomersWithCriteriaHasSearchForSalesAgentIncompleteInput() {
        def objectParam = [:]
        objectParam["searchSalesAgent"]="salesAg"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }
    void testGetListOfCustomersWithCriteriaHasSearchForSalesAgentNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchSalesAgent"]="sdfg"

        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetListOfCustomersWithCriteriaNoSearchValues() {
        def objectParam = [:]
        def result = customerService.getListOfCustomersWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "the return value of the method must contain sampleCustomer", result.contains(sampleCustomer)
    }

	void testGenerateAuditTrails(){
		def customer = new Customer(identifier: "sample",
			name: "customer name",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
			salesAgent:sampleSalesAgent,
			term:sampleTerm
			)
		customer.save()
		assertEquals "Customer Identifier must be sample","sample",customer.identifier
		assertNull "Customer audit trails must not have any value",customer.auditTrails
		customer.identifier = "edited"
		customerService.generateAuditTrails(customer)
		assertEquals "audit trails must contain 1 element", 1, customer.auditTrails.size()
		customer.auditTrails.each{
			assertEquals "auditTrails current value must be edited","edited",it.newEntry
			assertEquals "auditTrails previous value must be sample","sample",it.previousEntry
			assertEquals "auditTrails fieldName value must be identifier","Identifier",it.fieldName
		}
	}

	void testGenerateAuditTrailsCreditLimit(){
		def customer = new Customer(identifier: "test", 
			name: "customer name",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
			salesAgent:sampleSalesAgent,
			creditLimit:new BigDecimal("0.00"),
			term:sampleTerm
			)
		customer.save()
		assertEquals "Customer Identifier must be sample","test",customer.identifier
		assertNull "Customer audit trails must not have any value",customer.auditTrails
		assertEquals "Customer credit limit must be zero","0.00",customer.creditLimit.toString()
		customer.creditLimit = new BigDecimal("10.00")
		customerService.generateAuditTrails(customer)
		assertEquals "audit trails must contain 1 element", 1, customer.auditTrails.size()
		customer.auditTrails.each{
			assertEquals "auditTrails current value must be 10.00","10.00",it.newEntry
			assertEquals "auditTrails previous value must be 0.00","0.00",it.previousEntry
			assertEquals "auditTrails fieldName value must be credit limit","Credit Limit",it.fieldName
		}
	}

}
