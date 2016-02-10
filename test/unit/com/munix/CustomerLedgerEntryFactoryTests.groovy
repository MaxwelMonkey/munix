package com.munix

import grails.test.*

class CustomerLedgerEntryFactoryTests extends GrailsUnitTestCase {
	def generalMethodService = new GeneralMethodService()
	def sampleSalesDelivery
	def sampleCheckPayment
	def sampleBouncedCheck
	def sampleCheckDeposit
	def sampleCustomerCharge
	def sampleCreditMemo
	def sampleDebitMemo
	def sampleDirectPayment
	def sampleDirectPaymentItemCheck
	def samplePaymentTypeCash
	def samplePaymentTypeCheck
	def sampleOverpayment
	
	protected void setUp() {
		super.setUp()

		sampleSalesDelivery = new SalesDelivery(
			date: generalMethodService.createDate("07/27/1984"),
			salesDeliveryId: "SD-C-00000301"
		)
		mockDomain(SalesDelivery, [sampleSalesDelivery])
		sampleSalesDelivery.metaClass.'static'.computeTotalAmount = { -> return new BigDecimal("10") }

		sampleCheckPayment = new CheckPayment(
			checkNumber: "1234",
			amount: new BigDecimal("10"),
			bank: new Bank(identifier: "bank", description: "bank desc"),
			branch: "bank branch",
			type: new CheckType(description: "check type desc"),
			date: generalMethodService.createDate("07/27/1984")
		)
		mockDomain(CheckPayment, [sampleCheckPayment])

		sampleBouncedCheck = new BouncedCheck(
			date: generalMethodService.createDate("07/27/1984")
		)
		mockDomain(BouncedCheck, [sampleBouncedCheck])
		sampleBouncedCheck.metaClass.'static'.computeTotalAmount = { -> return new BigDecimal("10") }

		sampleCheckDeposit = new CheckDeposit(
			depositDate: generalMethodService.createDate("07/27/1984")
		)
		mockDomain(CheckDeposit, [sampleCheckDeposit])
		sampleCheckDeposit.metaClass.'static'.computeTotal = { -> return new BigDecimal("10") }

		sampleCustomerCharge = new CustomerCharge(
			date: generalMethodService.createDate("07/27/1984")
		)	
		mockDomain(CustomerCharge, [sampleCustomerCharge])
		sampleCustomerCharge.metaClass.'static'.computeTotalAmount = { -> return new BigDecimal("10") }

		sampleCreditMemo = new CreditMemo(
			date: generalMethodService.createDate("07/27/1984")
		)
		sampleDebitMemo = new CreditMemo(
			date: generalMethodService.createDate("07/27/1984")
		)
		mockDomain(CreditMemo, [sampleCreditMemo, sampleDebitMemo])
		sampleCreditMemo.metaClass.'static'.computeTotalAmount = { -> return new BigDecimal("10") }
		sampleDebitMemo.metaClass.'static'.computeTotalAmount = { -> return new BigDecimal("-10") }

		sampleDirectPayment = new DirectPayment(
			date: generalMethodService.createDate("07/27/1984")
		)
		mockDomain(DirectPayment, [sampleDirectPayment])
		sampleDirectPayment.metaClass.'static'.computePaymentTotal = { -> return new BigDecimal("10") }
		
		samplePaymentTypeCheck = new PaymentType(
			identifier: "check",
			isCheck: true
		)
		samplePaymentTypeCash = new PaymentType(
			identifier: "cash",
			isCheck: false
		)
		mockDomain(PaymentType, [samplePaymentTypeCheck, samplePaymentTypeCash])
		
		sampleDirectPaymentItemCheck = new DirectPaymentItemCheck(
			date: generalMethodService.createDate("07/27/1984"),
			amount: new BigDecimal("10"),
			paymentType: samplePaymentTypeCheck,
			checkPayment: sampleCheckPayment,
			directPayment: sampleDirectPayment
		)
		mockDomain(DirectPaymentItemCheck, [sampleDirectPaymentItemCheck])
		
		sampleOverpayment = new Overpayment(
			amount: new BigDecimal("10"),
			directPayment: sampleDirectPayment
		)
		mockDomain(Overpayment, [sampleOverpayment])
	}

	protected void tearDown() {
		super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove SalesDelivery
		remove BouncedCheck
		remove CustomerCharge
		remove CreditMemo
		remove DirectPayment
	}

	void testCreateApprovedSalesDelivery() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedSalesDelivery(sampleSalesDelivery)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.APPROVED_SALES_DELIVERY, customerLedgerEntry.type
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertEquals "created Customer Ledger Entry debit is wrong", new BigDecimal("10"), customerLedgerEntry.debitAmount
		assertSalesDeliveryEntryDetails(customerLedgerEntry)
	}

	void testCreateUnapprovedSalesDelivery() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedSalesDelivery(sampleSalesDelivery)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.UNAPPROVED_SALES_DELIVERY, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry credit is wrong", new BigDecimal("10"), customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertSalesDeliveryEntryDetails(customerLedgerEntry)
	}
	
	private void assertSalesDeliveryEntryDetails(CustomerLedgerEntry customerLedgerEntry) {
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "SD-C-00000301", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId		
	}

	void testCreateApprovedBouncedCheck() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedBouncedCheck(sampleBouncedCheck)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.APPROVED_BOUNCED_CHECK, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "BC-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertEquals "created Customer Ledger Entry debit is wrong", new BigDecimal("10"), customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}

    void testCreateUnapprovedBouncedCheck() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedBouncedCheck(sampleBouncedCheck)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.UNAPPROVED_BOUNCED_CHECK, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "BC-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry debit is wrong", new BigDecimal("10"), customerLedgerEntry.creditAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}

	void testCreateApprovedBouncedCheckItem() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedBouncedCheckItem(sampleBouncedCheck, sampleCheckPayment)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.BOUNCED_CHECK_ITEM, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "BC-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertEquals "created Customer Ledger Entry debit is wrong", new BigDecimal("10"), customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}

    void testCreateUnapprovedBouncedCheckItem() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedBouncedCheckItem(sampleBouncedCheck, sampleCheckPayment)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.BOUNCED_CHECK_ITEM, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "BC-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry debit is wrong", new BigDecimal("10"), customerLedgerEntry.creditAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}

	void testCreateApprovedCheckDepositItem() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedCheckDepositItem(sampleCheckDeposit, sampleCheckPayment)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.APPROVED_CHECK_DEPOSIT_ITEM, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry details is wrong", "CD-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry referenceId is wrong", " - 07/27/1984 bank - bank branch - 1234 check type desc", customerLedgerEntry.details
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertEquals "created Customer Ledger Entry credit should be 10", new BigDecimal("10"), customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit should be null", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}

	void testCreateApprovedCustomerCharge() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedCustomerCharge(sampleCustomerCharge)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.APPROVED_CUSTOMER_CHARGE, customerLedgerEntry.type
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertEquals "created Customer Ledger Entry debit is wrong", new BigDecimal("10"), customerLedgerEntry.debitAmount
		assertCustomerChargeEntryDetails(customerLedgerEntry)
	}

	void testCreateUnapprovedCustomerCharge() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedCustomerCharge(sampleCustomerCharge)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.UNAPPROVED_CUSTOMER_CHARGE, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry credit is wrong", new BigDecimal("10"), customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertCustomerChargeEntryDetails(customerLedgerEntry)
	}

	private void assertCustomerChargeEntryDetails(CustomerLedgerEntry customerLedgerEntry) {
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "CC-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}

	void testCreateApprovedCreditMemo() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedCreditMemo(sampleCreditMemo)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.APPROVED_CREDIT_MEMO, customerLedgerEntry.type
		assertCreditMemoEntryDetails(customerLedgerEntry)
	}

	void testCreateUnapprovedCreditMemo() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedCreditMemo(sampleCreditMemo)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.UNAPPROVED_CREDIT_MEMO, customerLedgerEntry.type
		assertCreditMemoEntryDetails(customerLedgerEntry)
	}

	private void assertCreditMemoEntryDetails(CustomerLedgerEntry customerLedgerEntry) {
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "CM-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}

	void testCreateApprovedDebitMemo() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedDebitMemo(sampleDebitMemo)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.APPROVED_DEBIT_MEMO, customerLedgerEntry.type
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertEquals "created Customer Ledger Entry debit is wrong", new BigDecimal("10"), customerLedgerEntry.debitAmount
		assertDebitMemoEntryDetails(customerLedgerEntry)
	}

	void testCreateUnapprovedDebitMemo() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedDebitMemo(sampleDebitMemo)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.UNAPPROVED_DEBIT_MEMO, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry credit is wrong", new BigDecimal("10"), customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertDebitMemoEntryDetails(customerLedgerEntry)
	}

	private void assertDebitMemoEntryDetails(CustomerLedgerEntry customerLedgerEntry) {
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "CM-00000002", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertEquals "created Customer Ledger Entry linkId is wrong", 2L, customerLedgerEntry.linkId
	}

	void testCreateApprovedDirectPayment() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedDirectPayment(sampleDirectPayment)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.APPROVED_DIRECT_PAYMENT, customerLedgerEntry.type
		assertDirectPaymentEntryDetails(customerLedgerEntry)
	}

	void testCreateUnapprovedDirectPayment() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedDirectPayment(sampleDirectPayment)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.UNAPPROVED_DIRECT_PAYMENT, customerLedgerEntry.type
		assertDirectPaymentEntryDetails(customerLedgerEntry)
	}

	private void assertDirectPaymentEntryDetails(CustomerLedgerEntry customerLedgerEntry) {
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry referenceId is wrong", "DP-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}
	
	void testCreateDirectPaymentItem() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createDirectPaymentItem(sampleDirectPaymentItemCheck)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.DIRECT_PAYMENT_ITEM, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry details is wrong", "check - 07/27/1984 bank - bank branch - 1234 check type desc", customerLedgerEntry.details
		assertEquals "created Customer Ledger Entry referenceId is wrong", "DP-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}
	
	void testCreateApprovedOverpayment() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createApprovedOverpayment(sampleOverpayment)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.OVERPAYMENT, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry details is wrong", CustomerLedgerEntry.Type.OVERPAYMENT.toString(), customerLedgerEntry.details
		assertEquals "created Customer Ledger Entry referenceId is wrong", "DP-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertNotNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}
	void testCreateUnapprovedOverpayment() {
		def customerLedgerEntry = CustomerLedgerEntryFactory.createUnapprovedOverpayment(sampleOverpayment)

		assertEquals "created Customer Ledger Entry type is wrong", CustomerLedgerEntry.Type.OVERPAYMENT, customerLedgerEntry.type
		assertEquals "created Customer Ledger Entry dateOpened is wrong", generalMethodService.createDate("07/27/1984"), customerLedgerEntry.dateOpened
		assertNotNull "created Customer Ledger Entry datePosted is empty", customerLedgerEntry.datePosted
		assertEquals "created Customer Ledger Entry details is wrong", CustomerLedgerEntry.Type.OVERPAYMENT.toString(), customerLedgerEntry.details
		assertEquals "created Customer Ledger Entry referenceId is wrong", "DP-00000001", customerLedgerEntry.referenceId
		assertEquals "created Customer Ledger Entry amount is wrong", new BigDecimal("10"), customerLedgerEntry.amount
		assertNotNull "created Customer Ledger Entry credit is wrong", customerLedgerEntry.creditAmount
		assertNull "created Customer Ledger Entry debit is wrong", customerLedgerEntry.debitAmount
		assertEquals "created Customer Ledger Entry linkId is wrong", 1L, customerLedgerEntry.linkId
	}
}
