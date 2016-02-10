package com.munix

import grails.test.*

class CheckWarehousingServiceTests extends GroovyTestCase {
    def sampleCheckPayment
    def sampleCheckPayment2
    def sampleBank
    def sampleCheckType
    def sampleCity
    def sampleCustomerType
    def sampleCustomer
    def sampleCheckWarehouse
    def sampleTerm
    def generalMethodService
    def checkWarehousingService
    protected void setUp() {
        super.setUp()
        setUpBanks()
        setUpCheckTypes()
        setUpCities()
        setUpCustomerTypes()
        setUpCheckWarehouses()
        setupTerms()
        setUpCustomers()
        setUpCheckPayments()
    }
    private void setUpBanks() {
		sampleBank = new Bank(identifier: "bank", description: "bank description")
		sampleBank.save(flush: true)
	}

	private void setUpCheckTypes() {
		sampleCheckType = new CheckType(
				routingNumber: "1234",
				description: "idontknow",
				branch: "branch")
		sampleCheckType.save(flush: true)
	}
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
	private void setUpCheckPayments() {
		sampleCheckPayment = new CheckPayment(
				amount: new BigDecimal("123"),
				checkNumber: "1234",
				bank : sampleBank,
				type : sampleCheckType,
				branch : "university",
				date : generalMethodService.createDate("12/10/2001"),
				customer : sampleCustomer,
				warehouse : sampleCheckWarehouse)
		sampleCheckPayment.save(flush: true)
		sampleCheckPayment2 = new CheckPayment(
			amount: new BigDecimal("123"),
			checkNumber: "12345",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : generalMethodService.createDate("12/13/2001"),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPayment2.save(flush: true)
	}
    private void setUpCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		sampleCity.save(flush: true)
	}

	private void setUpCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier: "customerType", description: "descCustomerType")
		sampleCustomerType.save(flush: true)
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
			term: sampleTerm
		)
		sampleCustomer.save(flush: true)
	}

	private void setUpCheckWarehouses() {
		sampleCheckWarehouse = new CheckWarehouse(
				identifier: "identifier",
				description: "desc"
		).save(flush: true)
	}
    protected void tearDown() {
        super.tearDown()
    }

    void testGetCheckPaymentsWithCriteria() {
        def originWarehouse = sampleCheckWarehouse
        def object = [:]
        object.startDate = generalMethodService.createDate("12/09/2001")
        object.endDate = generalMethodService.createDate("12/12/2001")

        def result = checkWarehousingService.getCheckPaymentsWithCriteria(object,originWarehouse)
        assertTrue "result must contain checkpayment 1",result.contains(sampleCheckPayment)
        assertEquals "result must contain 1 item",1,result.size()
    }
    void testGetCheckPaymentsWithCriteriaAllDate() {
        def originWarehouse = sampleCheckWarehouse
        def object = [:]
        object.startDate = generalMethodService.createDate("12/09/2001")
        object.endDate = generalMethodService.createDate("12/15/2001")

        def result = checkWarehousingService.getCheckPaymentsWithCriteria(object,originWarehouse)
        assertTrue "result must contain checkpayment 1",result.contains(sampleCheckPayment)
        assertTrue "result must contain checkpayment 2",result.contains(sampleCheckPayment2)
        assertEquals "result must contain 2 item",2,result.size()
    }
}
