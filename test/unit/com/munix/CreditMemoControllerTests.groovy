package com.munix

import grails.test.*
import org.grails.plugins.springsecurity.service.AuthenticateService

class CreditMemoControllerTests extends ControllerUnitTestCase {
	def authenticateService
	def mockStockCardService
	def sampleDebitMemo
	def sampleCreditMemo
	def sampleCustomer

	void setUp() {
		super.setUp()
		
		mockStockCardService = mockFor(StockCardService)
		authenticateService = mockFor(AuthenticateService)
		authenticateService.demand.userDomain(1..1) { ->
			new User(userRealName: "realName")
		}
		
		sampleDebitMemo = new CreditMemo (
			status: "Unapproved"			
		)
		sampleCreditMemo = new CreditMemo (
			status: "Unapproved"
		)
		mockDomain(CreditMemo, [sampleCreditMemo, sampleDebitMemo])
		sampleCreditMemo.metaClass.'static'.computeTotalAmount = { -> return new BigDecimal("10") }
		sampleDebitMemo.metaClass.'static'.computeTotalAmount = { -> return new BigDecimal("-10") }

        sampleCustomer = new Customer()
        mockDomain(Customer, [sampleCustomer])
	}
	
	void tearDown() {
		super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove CreditMemo
	}
			
	void testApproveTwoCreditMemo() {
		mockParams.id = sampleCreditMemo.id
		controller.authenticateService = authenticateService.createMock()
		def mockCreditMemoService = mockFor(CreditMemoService)
		mockCreditMemoService.demand.secondApproveCreditMemo(1..1) {x ->
			return true
		}
		controller.creditMemoService = mockCreditMemoService.createMock()
		
		controller.approveTwo()
		
		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}
	
	void testUnapproveCreditMemo() {
		mockParams.id = sampleCreditMemo.id
		def mockCreditMemoService = mockFor(CreditMemoService)
		mockCreditMemoService.demand.unapproveCreditMemo(1..1) { x->
			return true
		}
		controller.creditMemoService = mockCreditMemoService.createMock()

		controller.unapprove()
		
		assertEquals "Should redirect to show view", "show", redirectArgs.action
	}
    void testApprovedDebitMemos() {
		mockParams.customerId = sampleCustomer.id
		def mockCreditMemoService = mockFor(CreditMemoService)
		mockCreditMemoService.demand.getApprovedDebitMemoList(1..1) {x,y ->
			return [debitMemoList:"123",totalCount : 456]
		}
		controller.creditMemoService = mockCreditMemoService.createMock()

		def result = controller.unpaidList()

		assertEquals "result debit memo list must be 123", "123", result.debitMemoList
		assertEquals "result debit memo list total count must be 456", 456, result.debitMemoListTotal
	}

}
