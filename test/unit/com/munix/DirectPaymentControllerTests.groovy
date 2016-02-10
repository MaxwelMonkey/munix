package com.munix

import grails.test.*
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class DirectPaymentControllerTests extends ControllerUnitTestCase {
	Date newDate
	def dateFormatter
	def sampleDirectPayment
	def sampleCustomer
	def sampleCity
	def sampleCustomerType
	def sampleTerm
	def sampleForwarder
	def sampleSalesAgent
	def samplePaymentType1
	def samplePaymentType2
	def samplePaymentType3
	def sampleDirectPaymentItem1
	def sampleDirectPaymentItem2
	def sampleOverpayment
	def sampleCheckPayment
	def sampleBank
	def sampleCheckType
	def sampleCheckWarehouse
	def sampleSalesDelivery
	def sampleCustomerCharge
	def sampleCreditMemo
	def sampleDiscountType
	def sampleBouncedCheck
	def sampleUser
	def constantService
	def sampleWarehouse
	def sampleCheckDeposit

	protected void setUp() {
		super.setUp()
		controller.generalMethodService = new GeneralMethodService()
		controller.metaClass.message={Map map-> map}

		dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		newDate = dateFormatter.parse("12/12/2010");

		setUpUsers()
		setUpWarehouses()
		setUpBanks()
		setUpCheckWarehouses()
		setUpPaymentTypes()
		setUpCheckTypes()
		setUpCities()
		setUpCustomerTypes()
		setUpSalesAgent()
		setUpCustomers()
		setUpCheckPayments()
		setUpDirectPaymentItems()
		setUpDirectPayments()
		setUpOverpayments()
		setUpSalesDeliveries()
		setUpCustomerCharges()
		setUpBouncedChecks()
		setUpDiscountTypes()
		setUpCreditMemos()
		setUpCheckDeposits()
	}

	private void setUpUsers() {
		sampleUser = new User(username: "user")
		mockDomain(User, [sampleUser])
	}

	private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier: "warehouse", description: "sample")
		mockDomain(Warehouse, [sampleWarehouse])
	}

	private void setUpBanks() {
		sampleBank = new Bank(identifier: "bank", description: "bank2")
		mockDomain(Bank, [sampleBank])
	}

	private void setUpCheckWarehouses() {
		sampleCheckWarehouse = new CheckWarehouse(identifier: "1234", description: "1234", isDefault: true)
		mockDomain(CheckWarehouse, [sampleCheckWarehouse])
	}

	private void setUpPaymentTypes() {
		samplePaymentType1 = new PaymentType(identifier: "CHK", description: "check", isCheck: true)
		samplePaymentType2 = new PaymentType(identifier: "CS", description: "cash",	isCheck: false)
		samplePaymentType3 = new PaymentType(identifier: "CM", description: "Credit Memo", isCheck: false)
		mockDomain(PaymentType, [samplePaymentType1, samplePaymentType2, samplePaymentType3])
	}

	private void setUpCheckTypes() {
		sampleCheckType = new CheckType(routingNumber: "1234", description: "idontknow", branch: "1345")
		mockDomain(CheckType, [sampleCheckType])
	}

	private void setUpCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		mockDomain(City, [sampleCity])
	}

	private void setUpCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier: "customerType", description: "descCustomerType")
		mockDomain(CustomerType, [sampleCustomerType])
	}

	private void setUpTerms() {
		sampleTerm = new Term(identifier: "term", description: "termDesc", dayValue: new BigDecimal("1234"))
		mockDomain(Term, [sampleTerm])
	}

	private void setUpForwarders() {
		sampleForwarder = new Forwarder(identifier: "forward",
				description: "descForw",
				street: "nothing",
				city: sampleCity,
				zip: "1234",
				landline: "1234",
				contact: "1234",
				contactPosition: "you")
		mockDomain(Forwarder, [sampleForwarder])
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
		mockDomain(SalesAgent, [sampleSalesAgent])
	}

	private void setUpCustomers() {
		sampleCustomer = new Customer(identifier: "customer",
				name: "customer name",
				busAddrStreet: "here",
				busAddrCity: sampleCity,
				busAddrZip: "1234",
				bilAddrStreet: "1234 here",
				bilAddrCity: sampleCity,
				bilAddrZip: "1234",
				skype: "hi",
				email: "1@yahoo.com",
				landline: "1234",
				fax: "1234",
				contact: "1234",
				mobile: "1234",
				type: sampleCustomerType,
				term: sampleTerm,
				status: Customer.Status.ACTIVE,
				forwarder: sampleForwarder,
				salesAgent: sampleSalesAgent,
				autoApprove: false,
				enableCreditLimit: true,
				tin: "1234",
				collectionMode: "12345",
				collectionPreference: "12345",
				collectionSchedule: "12345",
				generalRemark: "2345",
				creditRemark: "12345",
				contactPosition: "12345",
				owner: "12345",
				yahoo: "12345",
				secondaryContact: "12345",
				secondaryContactPosition: "12345")
		mockDomain(Customer, [sampleCustomer])
	}

	private void setUpCheckPayments() {
		sampleCheckPayment = new CheckPayment(
				amount: new BigDecimal("123"),
				checkNumber: "1234",
				bank : Bank.findById(sampleBank.id),
				type : CheckType.findById(sampleCheckType.id),
				branch : "university",
				date : newDate,
				customer : sampleCustomer)
		mockDomain(CheckPayment, [sampleCheckPayment])
	}

	private void setUpDirectPaymentItems() {
		sampleDirectPaymentItem1 = new DirectPaymentItemCheck(
				date: dateFormatter.parse("11/12/2010"),
				paymentType: samplePaymentType1,
				amount: new BigDecimal("1234"),
				checkPayment: sampleCheckPayment)
		mockDomain(DirectPaymentItemCheck, [sampleDirectPaymentItem1])

		sampleDirectPaymentItem2 = new DirectPaymentItem(
				date: newDate,
				paymentType: samplePaymentType2,
				amount: new BigDecimal("5678"))
		mockDomain(DirectPaymentItem, [sampleDirectPaymentItem2])
	}

	private void setUpDirectPayments() {
		sampleDirectPayment = new DirectPayment(
				customer: sampleCustomer,
				preparedBy: "me",
				approvedBy: "you",
				remark: "fail",
				items:[sampleDirectPaymentItem1, sampleDirectPaymentItem2])

		def mockFind = mockFor(DirectPayment)
		mockFind.demand.find(1..1){ ->
			return null
		}
		mockDomain(DirectPayment, [sampleDirectPayment])
		sampleDirectPayment.metaClass.'static'.directPaymentItemsWithCreditMemo = { -> return null }
		sampleDirectPayment.metaClass.'static'.unapproveTransaction = {sampleDirectPayment -> sampleDirectPayment.unapproved()}
	}

	private void setUpSalesDeliveries() {
		sampleSalesDelivery = new SalesDelivery(
				customer: sampleCustomer,
				status: "Unpaid",
				termDay: new BigDecimal("0"),
				warehouse: sampleWarehouse
				)
		mockDomain(SalesDelivery, [sampleSalesDelivery])
	}

	private void setUpCustomerCharges() {
		sampleCustomerCharge = new CustomerCharge(
				date: new Date(),
				status: "Unpaid",
				customer: sampleCustomer,
				preparedBy: "me",
				amountPaid: new BigDecimal("500"))
		mockDomain(CustomerCharge, [sampleCustomerCharge])
	}

	private void setUpBouncedChecks() {
		sampleBouncedCheck = new BouncedCheck(
				date: new Date(),
				status: "Approved",
				customer: sampleCustomer,
				preparedBy: "me")
		mockDomain(BouncedCheck, [sampleBouncedCheck])
	}

	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(identifier: "discountType", description: "discount type")
		mockDomain(DiscountType, [sampleDiscountType])
	}

	private void setUpCreditMemos() {
		sampleCreditMemo = new CreditMemo(
				customer: sampleCustomer,
				discountType: sampleDiscountType,
				discount: new BigDecimal("0"),
				status: "Approved",
				preparedBy: "me",
				date: new Date()
				)
		mockDomain(CreditMemo, [sampleCreditMemo])
	}
	
	private void setUpCheckDeposits() {
		sampleCheckDeposit = new CheckDeposit()
		mockDomain(CheckDeposit, [sampleCheckDeposit])
	}

	protected void tearDown() {
		super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove DirectPayment
	}

	void testShowSelectValues() {
		mockParams.directPaymentId = sampleDirectPayment.id
		controller.showSelectValues()
		assertEquals '{"checkType":[{"name":"1234","typeId":1,"branch":"1345"}],"bankList":[{"name":"bank2 (bank)","typeId":1}],"paymentType":[{"name":"CHK","typeId":1,"isCheck":true,"deductibleFromSales":null},{"name":"CS","typeId":2,"isCheck":false,"deductibleFromSales":null},{"name":"CM","typeId":3,"isCheck":false,"deductibleFromSales":null}],"previousItems":[{"id":1,"date":"11/12/2010","payment":"CHK","deductibleFromSales":null,"amount":1234,"remark":null,"checkNum":"1234","checkDate":"12/12/2010","bank":1,"branch":"university","type":"1234","isCheck":true},{"id":1,"date":"12/12/2010","payment":"CS","deductibleFromSales":null,"amount":5678,"remark":null,"isCheck":false}]}',controller.response.contentAsString
	}

	void testCreateWithId() {
		mockParams.id = sampleCustomer.id
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.getAvailableCustomerPaymentsForDirectPayment(1..1){x->
			return [deliveries: sampleSalesDelivery, customerCharges: sampleCustomerCharge, bouncedChecks: sampleBouncedCheck, creditMemos: sampleCreditMemo]
		}
		controller.directPaymentService = mockDirectPaymentService.createMock()

		def output = controller.create()

		def checkDeliveryInstance = [sampleSalesDelivery.toString()]
		def checkCharge = [sampleCustomerCharge.toString()]
		def checkBouncedCheck = [sampleBouncedCheck.toString()]
		def checkCreditMemo = [sampleCreditMemo.toString()]

		def resultSd = []
		output.deliveries.each {
			resultSd.add(it.toString())
		}
		def resultCc = []
		output.customerCharges.each {
			resultCc.add(it.toString())
		}
		def resultBc = []
		output.bouncedChecks.each {
			resultBc.add(it.toString())
		}
		def resultCm=[]
		output.creditMemos.each {
			resultCm.add(it.toString())
		}
		assertEquals sampleDirectPayment.toString(), output.directPaymentInstance.toString()
		assertEquals checkDeliveryInstance, resultSd
		assertEquals checkCharge, resultCc
		assertEquals checkBouncedCheck, resultBc
		assertEquals checkCreditMemo, resultCm
	}

	void testCreateWithoutId(){
		def output = controller.create()
		assertEquals "[directPaymentInstance:DP-0000null]", output.toString()
	}

	void testSaveWithoutErrors(){
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()

		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.checkAllInvoicesIfTakenByDirectPayment(1..1){x, y->
			return false
		}
		mockDirectPaymentService.demand.generateInvoices(1..1){x, y->
			return ""
		}

		controller.directPaymentService= mockDirectPaymentService.createMock()

		mockRequest.addParameter "deliveries", sampleSalesDelivery.id.toString()
		mockRequest.addParameter "charges", sampleCustomerCharge.id.toString()
		mockRequest.addParameter "bouncedCheck", sampleBouncedCheck.id.toString()
		mockRequest.addParameter "creditMemo", sampleCreditMemo.id.toString()
		mockParams.customer=sampleCustomer

		controller.save()

		assertEquals "Should redirect to show", "show", redirectArgs.action
	}

	void testSaveWithErrors(){
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.checkAllInvoicesIfTakenByDirectPayment(1..1){x, y->
			return false
		}
		mockDirectPaymentService.demand.generateInvoices(1..1){x, y->
			return ""
		}

		controller.directPaymentService= mockDirectPaymentService.createMock()

		mockRequest.addParameter "deliveries", sampleSalesDelivery.id.toString()
		mockRequest.addParameter "charges", sampleCustomerCharge.id.toString()
		mockRequest.addParameter "bouncedCheck", sampleBouncedCheck.id.toString()
		mockRequest.addParameter "creditMemo", sampleCreditMemo.id.toString()

		controller.save()

		assertEquals "Should redirect to create", "create", renderArgs.view
	}

	void testShowWithValueWithoutFutureQuery() {
		DirectPayment.metaClass.'static'.find = { String query, ArrayList list -> return null }
		mockParams.id = sampleDirectPayment.id
		def mockConstantService = mockFor(ConstantService)
		mockConstantService.demand.DIRECT_PAYMENT_APPROVE(1..1){ ->
			return "Approved"
		}
		controller.constantService = mockConstantService.createMock()
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.retrieveDetailsOfItems(1..1){x ->
			return []
		}
		mockDirectPaymentService.demand.retrieveDetailsOfInvoices(1..1){x ->
			return []
		}
		mockDirectPaymentService.demand.accumulateTotals(1..1){x ->
			return [:]
		}
		controller.directPaymentService = mockDirectPaymentService.createMock()

		def value = controller.show()

		assertEquals "DP-00000001", value.directPaymentInstance.toString()
		assertTrue "true", value.checker
	}

	void testShowWithValueWithFutureQuery() {
		DirectPayment.metaClass.'static'.find = { String query, ArrayList list -> return sampleDirectPayment }
		mockParams.id = sampleDirectPayment.id
		def mockConstantService = mockFor(ConstantService)
		mockConstantService.demand.DIRECT_PAYMENT_APPROVE(1..1){ ->
			return "Approved"
		}
		controller.constantService = mockConstantService.createMock()
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.retrieveDetailsOfItems(1..1){x ->
			return []
		}
		mockDirectPaymentService.demand.retrieveDetailsOfInvoices(1..1){x ->
			return []
		}
		mockDirectPaymentService.demand.accumulateTotals(1..1){x ->
			return [:]
		}
		controller.directPaymentService = mockDirectPaymentService.createMock()

		def value = controller.show()

		assertEquals "DP-00000001", value.directPaymentInstance.toString()
		assertFalse "false", value.checker
	}

	void testShowWithoutValue() {
		mockParams.id = 0

		controller.show()

		assertEquals "Should redirect to list view", "list", redirectArgs.action
	}

	void testEditWithoutValue() {
		mockParams.id = 0

		controller.edit()

		assertEquals "Should redirect to list view", "list", redirectArgs.action
	}

	void testEditWithValue() {
		mockParams.id = sampleDirectPayment.id
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.getCustomerPaymentsForDirectPayment(1..1){x->
			return [deliveries: sampleSalesDelivery, customerCharges: sampleCustomerCharge, bouncedChecks: sampleBouncedCheck, creditMemos: sampleCreditMemo]
		}
		controller.directPaymentService = mockDirectPaymentService.createMock()

		def output = controller.edit()

		def checkDeliveryInstance = [sampleSalesDelivery.toString()]
		def checkCharge = [sampleCustomerCharge.toString()]
		def checkBouncedCheck = [sampleBouncedCheck.toString()]
		def checkCreditMemo = [sampleCreditMemo.toString()]

		def resultSd = []
		output.deliveries.each {
			resultSd.add(it.toString())
		}
		def resultCc = []
		output.customerCharges.each {
			resultCc.add(it.toString())
		}
		def resultBc = []
		output.bouncedChecks.each {
			resultBc.add(it.toString())
		}
		def resultCm = []
		output.creditMemos.each {
			resultCm.add(it.toString())
		}
		assertEquals sampleDirectPayment.toString(), output.directPaymentInstance.toString()
		assertEquals checkDeliveryInstance, resultSd
		assertEquals checkCharge, resultCc
		assertEquals checkBouncedCheck, resultBc
		assertEquals checkCreditMemo, resultCm
	}

	void testUpdateDirectPayment() {
		def ctr = 1
		mockParams.id = sampleDirectPayment.id
		mockRequest.addParameter "deliveries", sampleSalesDelivery.id.toString()
		mockRequest.addParameter "charges", sampleCustomerCharge.id.toString()
		mockRequest.addParameter "bouncedCheck", sampleBouncedCheck.id.toString()
		mockRequest.addParameter "creditMemo", sampleCreditMemo.id.toString()
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.updateDirectPaymentInvoice(1..1){x,y->
			return
		}
		controller.directPaymentService = mockDirectPaymentService.createMock()

		controller.updateDirectPayment()

		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}

	void testUpdateDirectPaymentWithoutValue() {
		mockParams.id = 0

		controller.updateDirectPayment()

		assertEquals "Should redirect to list view", "list", redirectArgs.action
	}

	void testUpdate() {
		mockParams.id = sampleDirectPayment.id
		mockRequest.addParameter "itemId", sampleSalesDelivery.id.toString()
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		mockDirectPaymentService.demand.updateDirectPayment(1..1){ sampleDirectPayment,p,d->
			return "updated values"
		}
		controller.directPaymentService = mockDirectPaymentService.createMock()

		controller.update()

		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}

	void testUpdateWithoutValue(){
		mockParams.id = 0

		controller.update()

		assertEquals "Should redirect to list view", "list", redirectArgs.action
	}

	void testUnapprove() {
		sampleDirectPayment.status = "Approved"
		sampleDirectPayment.overpayment = sampleOverpayment
		def mockCustomerLedgerService = mockFor(CustomerLedgerService)
		mockCustomerLedgerService.demand.unapproveDirectPayment(1..1) {x ->
			return null
		}
		controller.customerLedgerService = mockCustomerLedgerService.createMock()
        def mockDirectPaymentService = mockFor(DirectPaymentService)
        mockDirectPaymentService.demand.removePendingChecksFromCustomerAccountService(1..1){ b->
			return "removed CAS"
		}
		controller.directPaymentService = mockDirectPaymentService.createMock()
		mockParams.id = sampleDirectPayment.id
		assertEquals "[GUARD] Sample overpayment should be approved", Overpayment.Status.APPROVED, sampleDirectPayment.overpayment.status
		assertEquals "[GUARD] Sample direct payment's status should be approved", "Approved", sampleDirectPayment.status

		controller.unapprove()

		assertEquals "Sample direct payment should be unapproved", "Unapproved", sampleDirectPayment.status
		assertEquals "Sample overpayment should be unapproved", Overpayment.Status.UNAPPROVED, sampleDirectPayment.overpayment.status
		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}

	void testUnapproveNotUnapprovable() {
		sampleDirectPayment.status = "Approved"
		sampleCheckPayment.addToCheckDeposits(sampleCheckDeposit)
		mockParams.id = sampleDirectPayment.id
		assertEquals "[GUARD] Sample direct payment's status should be approved", "Approved", sampleDirectPayment.status

		controller.unapprove()

		assertEquals "Sample direct payment should still be approved", "Approved", sampleDirectPayment.status
		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}

	void testApprove() {
		mockParams.id = sampleDirectPayment.id
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		def sampleBalance

		mockDirectPaymentService.demand.isApprovable(1..1){d->
			return "Approvable"
		}

		mockDirectPaymentService.demand.generateComputedBalance(1..1){d->
			return sampleBalance
		}

		mockDirectPaymentService.demand.checkAndGenerateOverpayment(1..1){ b, d->
			return "updated values"
		}
        mockDirectPaymentService.demand.addPendingChecksToCustomerAccountService(1..1){ b->
			return "added CAS"
		}
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}

		controller.directPaymentService = mockDirectPaymentService.createMock()
		controller.authenticateService = mockAuthenticate.createMock()

		controller.approve()

		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}

	private void setUpOverpayments() {
		sampleOverpayment = new Overpayment(
				amount: new BigDecimal("0.25"),
				directPayment: sampleDirectPayment,
				status: Overpayment.Status.APPROVED
				)
		mockDomain(Overpayment, [sampleOverpayment])
	}

	void testBalanceIfNotNegative() {
		mockParams.id = sampleDirectPayment.id
		def mockDirectPaymentService = mockFor(DirectPaymentService)
		def mockCustomerLedgerService = mockFor(CustomerLedgerService)
		def sampleBalance = new BigDecimal('1')
		def sampleOverpayment

		mockDirectPaymentService.demand.isApprovable(1..1){d->
			return "Approvable"
		}
		mockDirectPaymentService.demand.generateComputedBalance(1..1){d->
			return sampleBalance
		}
		mockDirectPaymentService.demand.checkAndGenerateOverpayment(1..1){b, d->
			return sampleOverpayment
		}
		mockCustomerLedgerService.demand.approveDirectPayment(1..1){d->
			return "updated values"
		}
        mockDirectPaymentService.demand.addPendingChecksToCustomerAccountService(1..1){ b->
			return "added CAS"
		}
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}

		controller.directPaymentService = mockDirectPaymentService.createMock()
		controller.customerLedgerService = mockCustomerLedgerService.createMock()
		controller.authenticateService = mockAuthenticate.createMock()

		controller.approve()

		assertEquals "Status should be APPROVED", "Approved", sampleDirectPayment.status
	}
}
