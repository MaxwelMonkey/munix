package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class CheckDepositControllerTests extends ControllerUnitTestCase {

	def sampleCheckDeposit
	def sampleCheckPayment
	def sampleBouncedCheck
	
	protected void setUp() {
		super.setUp()
		controller.constantService = new ConstantService()
		
		sampleCheckDeposit = new CheckDeposit()
		mockDomain(CheckDeposit, [sampleCheckDeposit])

		sampleCheckPayment = new CheckPayment()
		mockDomain(CheckPayment, [sampleCheckPayment])
				
		sampleBouncedCheck = new BouncedCheck()
		mockDomain(BouncedCheck, [sampleBouncedCheck])
		
		sampleCheckDeposit.checks = [sampleCheckPayment]
	}

	protected void tearDown() {
		super.tearDown()
	}
	
	void testApprove() {
		mockParams.id = sampleCheckDeposit.id
		sampleCheckPayment.addToBouncedChecks(sampleBouncedCheck)
		def authenticateService = mockFor(AuthenticateService)
		authenticateService.demand.userDomain(1..1) { ->
			new User(userRealName: "realName")
		}
		controller.authenticateService = authenticateService.createMock()
		def mockCustomerLedgerService = mockFor(CustomerLedgerService)
		mockCustomerLedgerService.demand.createApprovedCheckDepositItem(1..1) { ->
			return true
		}
		controller.customerLedgerService = mockCustomerLedgerService.createMock()
		def mockCustomerAccountsSummaryService = mockFor(CustomerAccountsSummaryService)
		mockCustomerAccountsSummaryService.demand.removeCustomerAccountCheckPayment(1..1) {x ->
			return null
		}
		controller.customerAccountsSummaryService = mockCustomerAccountsSummaryService.createMock()
		
		controller.approve()
		
		assertEquals "Sample customer charge status should be Cleared", "Cleared", sampleCheckDeposit.status
		assertEquals "Should redirect to show view", "show", redirectArgs.action
		
		sampleCheckDeposit.checks.each {
			assertEquals "Checks' status should be set to Deposited", CheckPayment.Status.DEPOSITED, it.status
			assertEquals "Checks' bounced check status should be set to Paid", "Paid", it.bouncedCheck.status
		}
	}
	
	void testCancel() {
		mockParams.id = sampleCheckDeposit.id
		sampleCheckDeposit.checks = []
        def mockCheckDepositService = mockFor(CheckDepositService)
		mockCheckDepositService.demand.cancelCheckDeposit(1..1) {x ->
			return true
		}
		controller.checkDepositService = mockCheckDepositService.createMock()
		controller.cancel()
		assertEquals "Check Deposit has been successfully cancelled!", controller.flash.message
	}
    void testCancelNotSuccessful() {
		mockParams.id = sampleCheckDeposit.id
		sampleCheckDeposit.checks = []
        def mockCheckDepositService = mockFor(CheckDepositService)
		mockCheckDepositService.demand.cancelCheckDeposit(1..1) { x->
			return false
		}
		controller.checkDepositService = mockCheckDepositService.createMock()
		controller.cancel()
		assertEquals "Check Deposit can't be cancelled because it is already cleared!", controller.flash.error
	}
}
