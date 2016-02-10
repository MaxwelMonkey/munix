package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class BouncedCheckControllerTests extends ControllerUnitTestCase {
	def authenticateService
	def mockBouncedCheckService
	def editBouncedCheckService
	
	def sampleBouncedCheck
	Customer customer
	CustomerAccount customerAccount
	
	void setUp() {
		super.setUp()
		
		controller.constantService = new ConstantService()
		
		authenticateService = mockFor(AuthenticateService)
		authenticateService.demand.userDomain(1..1) { ->
			new User(userRealName: "realName")
		}
		
		editBouncedCheckService = mockFor(EditBouncedCheckService)
		editBouncedCheckService.demand.addChecksToBouncedCheck(1..1) { BouncedCheck bouncedCheckInstance, List<String> checkList ->
		}
		
		mockBouncedCheckService = mockFor(BouncedCheckService)

		customer = createDummyCustomer()
		
		
		CheckPayment pendingCheck = createDummyCheckPayment()
		CheckPayment depositedCheck = createDummyCheckPayment()
		depositedCheck.status = CheckPayment.Status.DEPOSITED
		CheckPayment bouncedCheck = createDummyCheckPayment()
		bouncedCheck.status = CheckPayment.Status.BOUNCED
	
		mockDomain(CheckPayment, [
			pendingCheck, depositedCheck, bouncedCheck
			])
		mockDomain(Customer, [
			customer, new Customer()
			])
		customerAccount = new CustomerAccount(customer: customer)
		mockDomain(CustomerAccount,[customerAccount])
		customer.customerAccount = customerAccount
		
		mockDomain(CheckStatus, [
			createDummyCheckStatus()
			])
		
		sampleBouncedCheck = new BouncedCheck(
			customer : customer,
			status: "Unapproved",
            checks:[depositedCheck]
		)
		mockDomain(BouncedCheck, [sampleBouncedCheck])
	}
		
	private CheckPayment createDummyCheckPayment() {
		new CheckPayment(
			customer: customer,
			date: new Date(),
			checkNumber: "checkNumber",
			amount: new BigDecimal(1000),
			bank: new Bank(),
			status: CheckPayment.Status.PENDING,
			type: new CheckType())
	}
	
	private CustomerAccount createDummyCustomerAccount() {
		new CustomerAccount(id:1, customer: customer, 
				totalUnpaidSalesDeliveries: BigDecimal.ZERO,
			 	totalUnpaidCustomerCharges: BigDecimal.ZERO,
			 	totalUnpaidDebitMemos: BigDecimal.ZERO,
			 	totalUnpaidBouncedChecks: BigDecimal.ZERO,
				totalUnpaidCheckPayments: BigDecimal.ZERO
			)
	}
	
	private Customer createDummyCustomer() {
		new Customer(id: 1, identifier: "identifier", name: "name")
	}
	
	private CheckStatus createDummyCheckStatus() {
		new CheckStatus(id: 1, identifier: "identifier", description: "description")
	}
	
	void testCreate() {
		this.controller.params.id = 1
		
		def model = controller.create()
		
		assertEquals customer, model["customer"]
		assertEquals 1, model["checkPayments"].size()
	}
	
	public testSaveFail() {
		def controller = new BouncedCheckController()
		controller.authenticateService = authenticateService.createMock()
		controller.editBouncedCheckService = editBouncedCheckService.createMock()
		mockDomain(BouncedCheck)
		controller.save()
		
		assertEquals "create", controller.redirectArgs["action"]	
	}
	
	public testSaveSuccess() {
		def controller = new BouncedCheckController()
		controller.metaClass.message={Map map-> map}
		mockParams."customer.id"=1
		mockParams."remark"="remark"
		mockParams."bouncedCheckStatus.id"=1
		mockParams."forRedeposit"=true
		
		controller.authenticateService = authenticateService.createMock()
		controller.editBouncedCheckService = editBouncedCheckService.createMock()
		mockDomain(BouncedCheck)
		controller.save()
		
		assertEquals "show", controller.redirectArgs["action"]
	}
	
	void testApprove() {
		mockParams.id = sampleBouncedCheck.id
		def authenticateService = mockFor(AuthenticateService)
		authenticateService.demand.principal(1..1) { ->
			new User(userRealName: "realName")
		}
		controller.authenticateService = authenticateService.createMock()
		def mockCustomerLedgerService = mockFor(CustomerLedgerService)
		mockCustomerLedgerService.demand.createApprovedBouncedCheck(1..1) { ->
			return true
		}
		controller.customerLedgerService = mockCustomerLedgerService.createMock()
		
		controller.approve()
		
		assertEquals "Sample bounced check status should be Approved", "Approved", sampleBouncedCheck.status
		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}
	
	void testCancel() {
		mockParams.id = sampleBouncedCheck.id
		
		controller.cancel()
		
		assertEquals "Bounced Check status should be cancelled", "Cancelled", sampleBouncedCheck.status
	}
	
	void testCancelNotCancellable() {
		mockParams.id = sampleBouncedCheck.id
		sampleBouncedCheck.approve()
		
		controller.cancel()
		
		assertEquals "Bounced Check status should not be cancelled", "Approved", sampleBouncedCheck.status
	}
	
	void testUnapprove() {
		mockParams.id = sampleBouncedCheck.id
		sampleBouncedCheck.status = "Approved"
		
		controller.unapprove()
		
		assertEquals "Bounced Check status should be unapproved", "Unapproved", sampleBouncedCheck.status
	}
	
	void testUnapproveNotUnapprovable() {
		mockParams.id = sampleBouncedCheck.id
		sampleBouncedCheck.status = "Approved"
		sampleBouncedCheck.isTakenByDirectPayment = true
		
		controller.unapprove()
		
		assertEquals "Bounced Check status should still be approved", "Approved", sampleBouncedCheck.status
	}
}
