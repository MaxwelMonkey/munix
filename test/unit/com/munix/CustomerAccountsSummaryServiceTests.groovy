package com.munix

import grails.test.*

class CustomerAccountsSummaryServiceTests extends GrailsUnitTestCase {
    def customerAccountsSummaryService = new CustomerAccountsSummaryService()
    def sampleCustomerCharge
    def sampleDebitMemo
    def sampleCustomerAccount
    def sampleCustomer
	def sampleCheckPayment
    protected void setUp() {
        super.setUp()
		
		setupCustomer()
		setupCustomerAccount()
		setupLinkBetweenCustomerAccountAndCustomer()
    }

    protected void tearDown() {
        super.tearDown()
    }
    private setupCustomerAccount(){
        sampleCustomerAccount = new CustomerAccount()
        mockDomain(CustomerAccount, [sampleCustomerAccount])
    }
    private setupCustomerCharge(){
        sampleCustomerCharge = new CustomerCharge()
        mockDomain(CustomerCharge, [sampleCustomerCharge])
        sampleCustomerCharge.metaClass.'static'.computeTotalAmount=  { -> return new BigDecimal("200") }
    }
    private setupDebitMemo(){
        sampleDebitMemo = new CreditMemo()
        mockDomain(CreditMemo, [sampleDebitMemo])
        sampleDebitMemo.metaClass.'static'.computeTotalAmount=  { -> return new BigDecimal("-200") }
        sampleDebitMemo.metaClass.'static'.isADebitMemo=  { -> return true }
    }
    private setupLinkBetweenCustomerAndDebitMemo(){
        sampleDebitMemo.customer = sampleCustomer
        sampleDebitMemo.save()
    }
    private setupCustomer(){
        sampleCustomer = new Customer()
        mockDomain(Customer, [sampleCustomer])
    }
    private setupLinkBetweenCustomerAndCustomerCharge(){
        sampleCustomerCharge.customer = sampleCustomer
        sampleCustomerCharge.save()
    }
    private setupLinkBetweenCustomerAccountAndCustomer(){
        sampleCustomer.customerAccount = sampleCustomerAccount
        sampleCustomer.save()
    }
	private setupCheckPayment(){
		sampleCheckPayment = new CheckPayment()
		mockDomain(CheckPayment, [sampleCheckPayment])
		sampleCheckPayment.amount = new BigDecimal("100")
		sampleCheckPayment.customer = sampleCustomer
		sampleCheckPayment.save()
	}
	
    void testAddCustomerAccountCustomerCharge() {
        setupCustomerCharge()
        setupLinkBetweenCustomerAndCustomerCharge()
        assertEquals "the total customer charge of customer account must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidCustomerCharges
        customerAccountsSummaryService.addCustomerAccountCustomerCharge(sampleCustomerCharge)
        assertEquals "the unpaid customer charge must be 200", new BigDecimal("200"), sampleCustomerAccount.totalUnpaidCustomerCharges
    }
    void testRemoveCustomerAccountCustomerCharge() {
        setupCustomerCharge()
        setupLinkBetweenCustomerAndCustomerCharge()
        assertEquals "the total customer charge of customer account must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidCustomerCharges
        customerAccountsSummaryService.removeCustomerAccountCustomerCharge(sampleCustomerCharge)
        assertEquals "the unpaid customer charge must be -200", new BigDecimal("-200"), sampleCustomerAccount.totalUnpaidCustomerCharges
    }
    void testAddCustomerAccountDebitMemo() {
        setupDebitMemo()
        setupLinkBetweenCustomerAndDebitMemo()
        assertEquals "the total customer charge of customer account must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidDebitMemos
        customerAccountsSummaryService.addCustomerAccountDebitMemo(sampleDebitMemo)
        assertEquals "the unpaid customer charge must be 200", new BigDecimal("200"), sampleCustomerAccount.totalUnpaidDebitMemos
    }
    void testRemoveCustomerAccountDebitMemo() {
        setupDebitMemo()
        setupLinkBetweenCustomerAndDebitMemo()
        assertEquals "the total customer charge of customer account must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidDebitMemos
        customerAccountsSummaryService.removeCustomerAccountDebitMemo(sampleDebitMemo)
        assertEquals "the unpaid customer charge must be -200", new BigDecimal("-200"), sampleCustomerAccount.totalUnpaidDebitMemos
    }
	void testAddCustomerAccountCheckPayment() {
		setupCheckPayment()
		assertEquals "the total check payment of customer account must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidCheckPayments
		customerAccountsSummaryService.addCustomerAccountCheckPayment(sampleCheckPayment)
		assertEquals "the unpaid check payment must be 100", new BigDecimal("100"), sampleCustomerAccount.totalUnpaidCheckPayments
	}
	void testRemoveCustomerAccountCheckPayment() {
		setupCheckPayment()
		assertEquals "the total check payment of customer account must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidCheckPayments
		customerAccountsSummaryService.removeCustomerAccountCheckPayment(sampleCheckPayment)
		assertEquals "the unpaid check payment must be -100", new BigDecimal("-100"), sampleCustomerAccount.totalUnpaidCheckPayments
	}
	void testUpdateCustomerAccountCheckPayment() {
		setupCheckPayment()
		assertEquals "the total check payment of customer account must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidCheckPayments
		customerAccountsSummaryService.updateCustomerAccountCheckPayment(sampleCheckPayment, new BigDecimal("30"))
		assertEquals "the unpaid check payment must be 50", new BigDecimal("70"), sampleCustomerAccount.totalUnpaidCheckPayments
	}
}
