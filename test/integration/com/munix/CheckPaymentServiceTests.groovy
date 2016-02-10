package com.munix

import grails.test.*
import java.text.SimpleDateFormat

class CheckPaymentServiceTests extends GrailsUnitTestCase {
	def checkPaymentService
	def generalMethodService
	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleDirectPayment
	def sampleCheckWarehouse
	def samplePaymentTypeCheck
	def sampleBank
	def sampleCheckType
	def sampleCheckPayment
	def sampleCheckPaymentWithRelatedCheckDeposit
	def sampleCheckPaymentWithRelatedCancelledCheckDeposit
	def sampleCheckPaymentWithBouncedCheck
	def sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit
	def sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit
	def sampleCheckPaymentForRedeposit
	def sampleCheckPaymentForRedepositWithUnapprovedBouncedCheck
	def sampleDirectPaymentItem
	def sampleDirectPaymentItem2
	def sampleDirectPaymentItem3
	def sampleDirectPaymentItem4
	def sampleDirectPaymentItem5
	def sampleDirectPaymentItem6
	def sampleBankAccount
	def sampleCheckDeposit
	def sampleCheckDepositCancelled
	def sampleCheckDepositApproved
	def sampleCheckStatus
	def sampleBouncedCheck
	def sampleTerm
	def sampleBouncedCheckUnapproved
	
	def sdf
	
    protected void setUp() {
        super.setUp()
		sdf = new SimpleDateFormat("MM/dd/yyyy")
		
        setUpCities()
        setUpCustomerTypes()
        setupTerms()
        setUpCustomers()
		setUpDirectPayments()
		
		setUpBankAccounts()
		setUpCheckDeposits()

		setUpCheckWarehouses()
		setUpPaymentTypes()
		setUpBanks()
		setUpCheckTypes()
		setUpCheckPayments()
		
		setUpDirectPaymentItems()
		setUpCheckPaymentAndDirectPaymentItemRelationship()
		
		setUpCheckStatuses()
		setUpBouncedChecks()
		setUpCheckPaymentAndBouncedCheckRelationship()
    }

    protected void tearDown() {
        super.tearDown()
    }

	public void testGeneratePendingAndForRedepositBouncedCheck() {
		def result = checkPaymentService.generatePendingAndForRedepositBouncedCheck()

		assertTrue "Result should contain sample check payment", result.contains(sampleCheckPayment)
		assertFalse "Result should not contain sample check payment with related check deposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
		assertTrue "Result should contain sample check payment with related cancelled check deposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
		assertFalse "Result should not contain sample check payment with bounced check", result.contains(sampleCheckPaymentWithBouncedCheck)
		assertFalse "Result sholud not contain sample bounced check payment with related check deposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
		assertFalse "Result should not contain sample bounced check payment with related cancelled check deposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
		assertFalse "Result should contain sample for redeposit check payment", result.contains(sampleCheckPaymentForRedeposit)
		assertFalse "Result should not contain sample for redeposit check payment with unapproved bounced check", result.contains(sampleCheckPaymentForRedepositWithUnapprovedBouncedCheck)
	}

    void testGetCheckPaymentListWithCriteriaWithCriteriaSearchForCustomer() {
        def objectParam = [:]
        objectParam["searchCustomer"]= "customer"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 6 objects in it", 6, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForCustomerIncompleteInput() {
        def objectParam = [:]
        objectParam["searchCustomer"]= "custom"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 6 objects in it", 6, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForCustomerNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchCustomerName"]= "asdf"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaWithCriteriaSearchForCheckNumber() {
        def objectParam = [:]
        objectParam["searchCheckNumber"]= "1234"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
    }
    void testGetCheckPaymentListWithCriteriaSearchForCheckNumberIncompleteInput() {
        def objectParam = [:]
        objectParam["searchCheckNumber"]= "12"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 6 objects in it", 6, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForCheckNumberNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchCheckNumber"]= "asdf"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaSearchForStatus() {
        def objectParam = [:]
        objectParam["searchStatus"]= "Pending"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 3 objects in it", 3, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForStatusNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchStatus"]= "Deposited"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaSearchForBank() {
        def objectParam = [:]
        objectParam["searchBank"]= sampleBank.id.toString()
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 6 objects in it", 6, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForBankNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchBank"]= (sampleBank.id+1).toString()
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaWithCriteriaSearchForBranch() {
        def objectParam = [:]
        objectParam["searchBranch"]= "university"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 4 objects in it", 4, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForBranchIncompleteInput() {
        def objectParam = [:]
        objectParam["searchBranch"]= "univer"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 4 objects in it", 4, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForBranchNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchBranch"]= "asdf"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaSearchForWarehouse() {
        def objectParam = [:]
        objectParam["searchBank"]= sampleCheckWarehouse.id.toString()
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 6 objects in it", 6, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForWarehouseNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchBank"]= (sampleCheckWarehouse.id+1).toString()
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaSearchForDateBefore() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= "12/09/2001"
		objectParam["dateBeforeText_value"]= "12/09/2001"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 6 objects in it", 6, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheck", result.contains(sampleCheckPaymentWithBouncedCheck)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForDateBeforeNoMatchingResult() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= sdf.format(generalMethodService.performDayOperationsOnDate(Operation.ADD, new Date(), 1))
		objectParam["dateBeforeText_value"]= sdf.format(generalMethodService.performDayOperationsOnDate(Operation.ADD, new Date(), 1))
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaSearchForDateAfter() {
        def objectParam = [:]
        objectParam["dateAfterText"]= "12/11/2001"
		objectParam["dateAfterText_value"]= "12/11/2001"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 3 objects in it", 3, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForDateAfterNoMatchingResult() {
        def objectParam = [:]
        objectParam["dateAfterText"]= "12/09/2000"
		objectParam["dateAfterText_value"]= "12/09/2000"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetCheckPaymentListWithCriteriaSearchForDateBeforeAndAfter() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= "12/09/2001"
		objectParam["dateBeforeText_value"]= "12/09/2001"
        objectParam["dateAfterText"]= "12/11/2001"
		objectParam["dateAfterText_value"]= "12/11/2001"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertEquals "The result must have 3 objects in it", 3, result.size()
        assertTrue "The result must contain sampleCheckPayment", result.contains(sampleCheckPayment)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCheckDeposit)
        assertTrue "The result must contain sampleCheckPaymentWithRelatedCancelledCheckDeposit", result.contains(sampleCheckPaymentWithRelatedCancelledCheckDeposit)
    }
    void testGetCheckPaymentListWithCriteriaSearchForDateBeforeAndAfterNoMatchingResult() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= "12/14/2001"
		objectParam["dateBeforeText_value"]= "12/14/2001"
        objectParam["dateAfterText"]= "12/15/2001"
		objectParam["dateAfterText_value"]= "12/15/2001"
        def result = checkPaymentService.getCheckPaymentListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
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

	private void setUpDirectPayments() {
		sampleDirectPayment = new DirectPayment(
			customer: sampleCustomer,
			preparedBy: "me",
			status: "Approved",
			remark: "fail").save(flush: true)
	}
	private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
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
				isCheck:true,
                deductibleFromSales: false)
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
			date : sdf.parse("12/10/2001"),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPayment.save()
		
		sampleCheckPaymentWithRelatedCheckDeposit = new CheckPayment(
			amount: new BigDecimal("123"),
			checkNumber: "123",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "branch",
			date : sdf.parse("12/10/2001"),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentWithRelatedCheckDeposit.addToCheckDeposits(sampleCheckDeposit)
		sampleCheckPaymentWithRelatedCheckDeposit.save()
		
		sampleCheckPaymentWithRelatedCancelledCheckDeposit = new CheckPayment(
			amount: new BigDecimal("123"),
			checkNumber: "121",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "branch",
			date : sdf.parse("12/10/2001"),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentWithRelatedCancelledCheckDeposit.addToCheckDeposits(sampleCheckDepositCancelled)
		sampleCheckPaymentWithRelatedCancelledCheckDeposit.save()
		
		sampleCheckPaymentWithBouncedCheck = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1232",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			status: CheckPayment.Status.BOUNCED,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentWithBouncedCheck.save()
		
		sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1231",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			status: CheckPayment.Status.BOUNCED,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit.addToCheckDeposits(sampleCheckDeposit)
		sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit.save()
		
		sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1222",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			status: CheckPayment.Status.BOUNCED,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit.addToCheckDeposits(sampleCheckDepositCancelled)
		sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit.save()
		
		sampleCheckPaymentForRedeposit = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1223",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			status: CheckPayment.Status.FOR_REDEPOSIT,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentForRedeposit.addToCheckDeposits(sampleCheckDeposit)
		sampleCheckPaymentForRedeposit.save()
		
		sampleCheckPaymentForRedepositWithUnapprovedBouncedCheck = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1225",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			status: CheckPayment.Status.FOR_REDEPOSIT,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentForRedeposit.addToCheckDeposits(sampleCheckDeposit)
		sampleCheckPaymentForRedeposit.save()
	}
	
	private void setUpDirectPaymentItems() {
		sampleDirectPaymentItem = new DirectPaymentItemCheck(
				directPayment: sampleDirectPayment,
				date: sdf.parse("06/03/2011"),
				paymentType: samplePaymentTypeCheck,
				amount: new BigDecimal("1234"),
				remark: "remarkable",
				checkPayment: sampleCheckPayment)
		sampleDirectPaymentItem.save()
		
		sampleDirectPaymentItem2 = new DirectPaymentItemCheck(
			directPayment: sampleDirectPayment,
			date: sdf.parse("06/03/2011"),
			paymentType: samplePaymentTypeCheck,
			amount: new BigDecimal("1234"),
			remark: "remarkable",
			checkPayment: sampleCheckPaymentWithRelatedCheckDeposit)
		sampleDirectPaymentItem2.save()
		
		sampleDirectPaymentItem3 = new DirectPaymentItemCheck(
			directPayment: sampleDirectPayment,
			date: sdf.parse("06/03/2011"),
			paymentType: samplePaymentTypeCheck,
			amount: new BigDecimal("1234"),
			remark: "remarkable",
			checkPayment: sampleCheckPaymentWithRelatedCancelledCheckDeposit)
		sampleDirectPaymentItem3.save()

		sampleDirectPaymentItem4 = new DirectPaymentItemCheck(
			directPayment: sampleDirectPayment,
			date: sdf.parse("06/03/2011"),
			paymentType: samplePaymentTypeCheck,
			amount: new BigDecimal("1234"),
			remark: "remarkable",
			checkPayment: sampleCheckPaymentWithBouncedCheck)
		sampleDirectPaymentItem4.save()
		
		sampleDirectPaymentItem5 = new DirectPaymentItemCheck(
			directPayment: sampleDirectPayment,
			date: sdf.parse("06/03/2011"),
			paymentType: samplePaymentTypeCheck,
			amount: new BigDecimal("1234"),
			remark: "remarkable",
			checkPayment: sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit)
		sampleDirectPaymentItem5.save()
		
		sampleDirectPaymentItem6 = new DirectPaymentItemCheck(
			directPayment: sampleDirectPayment,
			date: sdf.parse("06/03/2011"),
			paymentType: samplePaymentTypeCheck,
			amount: new BigDecimal("1234"),
			remark: "remarkable",
			checkPayment: sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit)
		sampleDirectPaymentItem6.save()
	}
	
	private void setUpCheckPaymentAndDirectPaymentItemRelationship() {
		sampleCheckPayment.directPaymentItem = sampleDirectPaymentItem
		sampleCheckPayment.save(flush: true)
		
		sampleCheckPaymentWithRelatedCheckDeposit.directPaymentItem = sampleDirectPaymentItem2
		sampleCheckPaymentWithRelatedCheckDeposit.save(flush: true)
		
		sampleCheckPaymentWithRelatedCancelledCheckDeposit.directPaymentItem = sampleDirectPaymentItem3
		sampleCheckPaymentWithRelatedCancelledCheckDeposit.save(flush: true)
		
		sampleCheckPaymentWithBouncedCheck.directPaymentItem = sampleDirectPaymentItem4
		sampleCheckPaymentWithBouncedCheck.save(flush: true)
		
		sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit.directPaymentItem = sampleDirectPaymentItem5
		sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit.save(flush: true)
		
		sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit.directPaymentItem = sampleDirectPaymentItem6
		sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit.save(flush: true)
	}
	
	private void setUpBankAccounts() {
		sampleBankAccount = new BankAccount(
			accountName: "account name",
			accountNumber: "1234"
		).save(flush: true)
	}
	
	private void setUpCheckDeposits() {
		sampleCheckDeposit = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: new Date(),
			preparedBy: "me"
		).save(flush: true)
		
		sampleCheckDepositCancelled = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: new Date(),
			preparedBy: "me",
			status: "Cancelled"
		).save(flush: true)
		
		sampleCheckDepositApproved = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: new Date(),
			preparedBy: "me",
			status: "Approved"
		).save(flush: true)
	}
	
	private void setUpCheckStatuses() {
		sampleCheckStatus = new CheckStatus(
			identifier: "identifier",
			description: "description").save(flush: true)
	}
	
	private void setUpBouncedChecks() {
		sampleBouncedCheck = new BouncedCheck(
			date: sdf.parse("06/03/2011"),
			status: "Approved",
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: true)
		sampleBouncedCheck.save()
		
		sampleBouncedCheckUnapproved = new BouncedCheck(
			date: sdf.parse("06/03/2011"),
			status: "Unpproved",
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: true)
		sampleBouncedCheckUnapproved.save()
	}
	
	private void setUpCheckPaymentAndBouncedCheckRelationship() {
		sampleCheckPaymentWithBouncedCheck.addToBouncedChecks(sampleBouncedCheck)
		sampleCheckPaymentWithBouncedCheck.save(flush: true)
		
		sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit.addToBouncedChecks(sampleBouncedCheck)
		sampleCheckPaymentWithBouncedCheckAndRelatedCheckDeposit.save(flush: true)
		
		sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit.addToBouncedChecks(sampleBouncedCheck)
		sampleCheckPaymentWithBouncedCheckAndRelatedCancelledCheckDeposit.save(flush: true)
		
		sampleCheckPaymentForRedeposit.addToBouncedChecks(sampleBouncedCheck)
		sampleCheckPaymentForRedeposit.save()
		
		sampleCheckPaymentForRedepositWithUnapprovedBouncedCheck.addToBouncedChecks(sampleBouncedCheckUnapproved)
		sampleCheckPaymentForRedepositWithUnapprovedBouncedCheck.save()
	
	}
}
