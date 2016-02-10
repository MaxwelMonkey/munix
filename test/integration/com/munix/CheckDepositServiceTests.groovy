package com.munix

import grails.test.*

class CheckDepositServiceTests extends GrailsUnitTestCase {
    def checkDepositService
	def generalMethodService
	
	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleCheckWarehouse
	def samplePaymentTypeCheck
	def sampleBank
	def sampleCheckType
    def sampleCheckPayment
    def sampleCheckPayment2
    def sampleCheckDeposit
	def sampleBankAccount
	def sampleTerm

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
		setUpBankAccounts()
		setUpCheckDeposits()		
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testGenerateList() {
		def checkDeposit = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: generalMethodService.createDate("06/01/2010"),
			billsPurchase: true,
			preparedBy: "me"
		)
		checkDeposit.save(flush: true)
		
		setUpFilteredCheckDeposits()
		
		def params = [:]
		params.searchIdentifier = checkDeposit.id.toString()
		params.searchAccount= "bank"
		params.searchBillsPurchase = "True"
		params.searchStatus = "Unapproved"
		params.searchDepositDateFrom = "01/01/2010"
		params.searchDepositDateFrom_value = "01/01/2010"
		params.searchDepositDateFrom_month = "01"
		params.searchDepositDateFrom_day = "01"
		params.searchDepositDateFrom_year = "2010"
		params.searchDepositDateTo = "01/01/2011"
		params.searchDepositDateTo_value = "01/01/2011"
		params.searchDepositDateTo_month = "01"
		params.searchDepositDateTo_day = "01"
		params.searchDepositDateTo_year = "2011"
												
		def result = checkDepositService.generateList(params)
		
		assertTrue "Result should contain checkDeposit", result.contains(checkDeposit)
		assertEquals "Result total should be 1", 1, result.size()
	}

    void testUpdateChecksInCheckDepositRemoveAll() {
        assertTrue "The checks for bounced check is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit.addToChecks(sampleCheckPayment)

        sampleCheckDeposit = checkDepositService.updateChecksInCheckDeposit( [],sampleCheckDeposit)
        assertTrue "The checks list is not empty", sampleCheckDeposit.checks.isEmpty()
    }
	
    void testUpdateChecksInCheckDepositChangedCheck() {
        assertTrue "The checks for bounced check is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit.addToChecks(sampleCheckPayment)
        sampleCheckDeposit = checkDepositService.updateChecksInCheckDeposit([sampleCheckPayment2.id],sampleCheckDeposit )
        assertTrue "The checks list does not contain the expected result", sampleCheckDeposit.checks.contains(sampleCheckPayment2)
        assertEquals "The checks list contains more than the expected result", 1, sampleCheckDeposit.checks.size()
    }
	
    void testUpdateChecksInCheckDepositAddCheck() {
        assertTrue "The checks for bounced check is not empty", sampleCheckDeposit.checks.isEmpty()
        sampleCheckDeposit.addToChecks(sampleCheckPayment)
        sampleCheckDeposit = checkDepositService.updateChecksInCheckDeposit([sampleCheckPayment.id, sampleCheckPayment2.id],sampleCheckDeposit )
        assertTrue "The checks list does not contain the expected result", sampleCheckDeposit.checks.contains(sampleCheckPayment)
        assertTrue "The checks list does not contain the expected result", sampleCheckDeposit.checks.contains(sampleCheckPayment2)
        assertEquals "The checks list contains more than the expected result", 2, sampleCheckDeposit.checks.size()
    }

    void testCancelCheckDepositNotCleared(){
        sampleCheckDeposit.unapprove()
        sampleCheckDeposit.save()

        assertTrue "Check Deposit must be unapproved", sampleCheckDeposit.isUnapproved()
        checkDepositService.cancelCheckDeposit(sampleCheckDeposit)
        assertTrue "Check Deposit must cancelled", sampleCheckDeposit.isCancelled()
    }

    void testCancelCheckDepositCleared(){
        sampleCheckDeposit.cleared()
        sampleCheckDeposit.save()
        assertTrue "Check Deposit must be cleared", sampleCheckDeposit.isCleared()
        checkDepositService.cancelCheckDeposit(sampleCheckDeposit)
        assertTrue "Check Deposit must still be cleared", sampleCheckDeposit.isCleared()
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
	private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
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
	
	private void setUpBankAccounts() {
		sampleBankAccount = new BankAccount(
			accountName: "bank",
			accountNumber: "1234"
		)
		sampleBankAccount.save(flush: true)
	}
	
	private void setUpCheckDeposits() {
		sampleCheckDeposit = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: generalMethodService.createDate("06/01/2010"),
			billsPurchase: true,
			preparedBy: "me"
		)
		sampleCheckDeposit.checks = []
		sampleCheckDeposit.save(flush: true)
	}
	
	private void setUpFilteredCheckDeposits() {
		def checkDepositFilteredByID = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: generalMethodService.createDate("06/01/2010"),
			billsPurchase: true,
			preparedBy: "me"
		)
		checkDepositFilteredByID.save(flush: true)

		def bankAccount = new BankAccount(
			accountName: "nuuu",
			accountNumber: "12334"
		)
		bankAccount.save(flush: true)

		def checkDepositFilteredByAccount = new CheckDeposit(
			account: bankAccount,
			depositDate: generalMethodService.createDate("06/01/2010"),
			billsPurchase: true,
			preparedBy: "me"
		)
		checkDepositFilteredByAccount.save(flush: true)

		def checkDepositFilteredByBillsPurchase = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: generalMethodService.createDate("06/01/2010"),
			billsPurchase: false,
			preparedBy: "me"
		)
		checkDepositFilteredByBillsPurchase.save(flush: true)

		def checkDepositFilteredByStatus = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: generalMethodService.createDate("06/01/2010"),
			status: "Cleared",
			billsPurchase: true,
			preparedBy: "me"
		)
		checkDepositFilteredByStatus.save(flush: true)

		def checkDepositFilteredByDepositDate = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: generalMethodService.createDate("06/01/2011"),
			billsPurchase: true,
			preparedBy: "me"
		)
		checkDepositFilteredByDepositDate.save(flush: true)
	}
}
