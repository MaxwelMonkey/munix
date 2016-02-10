package com.munix

import grails.test.*

class BouncedCheckServiceTests extends GrailsUnitTestCase {
    def bouncedCheckService
	def generalMethodService
	
	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleCheckWarehouse
	def sampleTerm
	def samplePaymentTypeCheck
	def sampleBank
	def sampleCheckType
	def sampleCheckPayment
    def sampleCheckPayment2
	def sampleCheckStatus
    def sampleBouncedCheck
	
    protected void setUp() {
        super.setUp()
		
		setUpCities()
		setUpCustomerTypes()
		setupTerms()
		setUpCustomers()
		setUpCheckWarehouses()
		setUpPaymentTypes()
		setUpBanks()
		setUpCheckTypes()
		setUpCheckPayments()
		setUpCheckStatuses()
		setUpBouncedChecks()
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testGenerateList() {
		def bouncedCheck = new BouncedCheck(
			date: generalMethodService.createDate("06/03/2010"),
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: true
		)
		bouncedCheck.save(flush: true)
	
		setUpFilteredBouncedChecks()	

		def params = [:]
		params.searchIdentifier = bouncedCheck.id.toString()
		params.searchCustomerName = "customer"
		params.searchStatus = "Unapproved"
		params.searchDateFrom = "01/01/2010"
		params.searchDateFrom_value = "01/01/2010"
		params.searchDateFrom_month = "01"
		params.searchDateFrom_day = "01"
		params.searchDateFrom_year = "2010"
		params.searchDateTo = "01/01/2011"
		params.searchDateTo_value = "01/01/2011"
		params.searchDateTo_month = "01"
		params.searchDateTo_day = "01"
		params.searchDateTo_year = "2011"
		params.searchBouncedCheckStatus = "Pull"
		params.searchForRedeposit = "True"
												
		def result = bouncedCheckService.generateList(params)
		
		assertTrue "Result should contain bouncedCheck", result.contains(bouncedCheck)
		assertEquals "Result total should be 1", 1, result.size()

	}
    void testUpdateChecksFromBouncedCheckRemoveAll() {
        assertTrue "The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        sampleBouncedCheck.addToChecks(sampleCheckPayment)
		bouncedCheckService.generalMethodService= new GeneralMethodService()
        bouncedCheckService.editBouncedCheckService = new EditBouncedCheckService()

        sampleBouncedCheck = bouncedCheckService.updateChecksFromBouncedCheck(sampleBouncedCheck, [])
        assertTrue "The checks list is not empty", sampleBouncedCheck.checks.isEmpty()
    }
	
    void testUpdateChecksFromBouncedCheckChangedCheck() {
        assertTrue "The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        sampleBouncedCheck.addToChecks(sampleCheckPayment)
        bouncedCheckService.generalMethodService= new GeneralMethodService()
        bouncedCheckService.editBouncedCheckService = new EditBouncedCheckService()
        sampleBouncedCheck = bouncedCheckService.updateChecksFromBouncedCheck(sampleBouncedCheck, [sampleCheckPayment2.id])
        assertTrue "The checks list does not contain the expected result", sampleBouncedCheck.checks.contains(sampleCheckPayment2)
        assertEquals "The checks list contains more than the expected result", 1, sampleBouncedCheck.checks.size()
    }
	
    void testUpdateChecksFromBouncedCheckAddCheck() {
        assertTrue "The checks for bounced check is not empty", sampleBouncedCheck.checks.isEmpty()
        sampleBouncedCheck.addToChecks(sampleCheckPayment)
        bouncedCheckService.generalMethodService= new GeneralMethodService()
        bouncedCheckService.editBouncedCheckService = new EditBouncedCheckService()
        sampleBouncedCheck = bouncedCheckService.updateChecksFromBouncedCheck(sampleBouncedCheck, [sampleCheckPayment.id, sampleCheckPayment2.id])
        assertTrue "The checks list does not contain the expected result", sampleBouncedCheck.checks.contains(sampleCheckPayment)
        assertTrue "The checks list does not contain the expected result", sampleBouncedCheck.checks.contains(sampleCheckPayment2)
        assertEquals "The checks list contains more than the expected result", 2, sampleBouncedCheck.checks.size()
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
	private void setUpCustomers() {
		sampleCustomer = new Customer(
			identifier: "customer",
			name: "customer name",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
            term:sampleTerm
		)
		sampleCustomer.save(flush: true)
	}
	
	private void setUpCheckWarehouses() {
		sampleCheckWarehouse = new CheckWarehouse(
				identifier: "identifier",
				description: "desc"
		).save(flush: true)
	}
	
	private void setUpPaymentTypes() {
		samplePaymentTypeCheck = new PaymentType(
				identifier:"CHK",
				description:"check",
				isCheck:true)
		samplePaymentTypeCheck.save(flush: true)
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
			checkNumber: "1234",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : generalMethodService.createDate("12/10/2001"),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPayment2.save(flush: true)
	}
	
	private void setUpCheckStatuses() {
		sampleCheckStatus = new CheckStatus(
				identifier: "Pull Out",
				description: "pulled out")
		sampleCheckStatus.save(flush: true)
	}
	
	private void setUpBouncedChecks() {
		sampleBouncedCheck = new BouncedCheck(
				date: generalMethodService.createDate("06/03/2011"),
				status: "Approved",
				customer: sampleCustomer,
				preparedBy: "me",
				bouncedCheckStatus: sampleCheckStatus,
				forRedeposit: false)
		sampleBouncedCheck.checks = []
		sampleBouncedCheck.save(flush: true)
	}
	
	private void setUpFilteredBouncedChecks() {
		def bouncedCheckFilteredByID = new BouncedCheck(
			date: generalMethodService.createDate("06/03/2010"),
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: true
		)
		bouncedCheckFilteredByID.save(flush: true)

		def customer = new Customer(
			identifier: "customer1",
			name: "nuuuuu",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
			term: sampleTerm
		)
		customer.save(flush: true)

		def bouncedCheckFilteredByCustomerName = new BouncedCheck(
			date: generalMethodService.createDate("06/03/2010"),
			customer: customer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: true
		)
		bouncedCheckFilteredByCustomerName.save(flush: true)

		def bouncedCheckFilteredByStatus = new BouncedCheck(
			date: generalMethodService.createDate("06/03/2010"),
			customer: sampleCustomer,
			preparedBy: "me",
			status: "Unapproved",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: true
		)
		bouncedCheckFilteredByStatus.save(flush: true)

		def bouncedCheckFilteredByDate = new BouncedCheck(
			date: generalMethodService.createDate("06/03/2011"),
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: true
		)
		bouncedCheckFilteredByDate.save(flush: true)

		def checkStatus = new CheckStatus(
			identifier: "nuuu",
			description: "nuuu")
		checkStatus.save(flush: true)

		def bouncedCheckFilteredByBouncedCheckStatus = new BouncedCheck(
			date: generalMethodService.createDate("06/03/2010"),
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: checkStatus,
			forRedeposit: true
		)
		bouncedCheckFilteredByBouncedCheckStatus.save(flush: true)


		def bouncedCheckFilteredByForRedeposit = new BouncedCheck(
			date: generalMethodService.createDate("06/03/2010"),
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: false
		)
		bouncedCheckFilteredByForRedeposit.save(flush: true)
	}
}
