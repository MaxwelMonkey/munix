package com.munix

import java.text.DateFormat
import java.text.SimpleDateFormat

import grails.test.*

class DirectPaymentTests extends GrailsUnitTestCase {
	Date newDate
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
	def sampleCheckPayment
	def sampleBank
	def sampleCheckType
	def sampleCheckWarehouse
	def sampleSalesDelivery
	def sampleCustomerCharge
	def sampleCreditMemo
	def sampleDiscountType
	def sampleBouncedCheck
	def sampleBouncedCheck2
	def sampleUser
	def constantService
	def sampleWarehouse
	def sampleCustomerAccount
	def sampleCheckDeposit
	
    protected void setUp() {
        super.setUp()
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		newDate = df.parse("12/12/2010");
		
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
		setUpCustomerAccount()
		setUpCustomerAccountRelationship()
		setUpCheckPayments()
		setUpDirectPaymentItems()
		setUpDirectPayments()
		setUpSalesDeliveries()
		setUpCustomerCharges()
		setUpBouncedChecks()
		setUpDiscountTypes()
		setUpCreditMemos()
		setUpCheckDeposits()
    }

	private void setUpCustomerAccountRelationship() {
		sampleCustomer.customerAccount = sampleCustomerAccount
		sampleCustomer.save()
	}
	
	private void setUpCustomerAccount() {
		sampleCustomerAccount = new CustomerAccount(
			totalUnpaidSalesDeliveries : new BigDecimal("1.00"),
			totalUnpaidCustomerCharges : new BigDecimal("1.00"),
			totalUnpaidDebitMemos : new BigDecimal("1.00"),
			totalUnpaidBouncedChecks : new BigDecimal("1.00"),
			totalUnpaidCheckPayments : new BigDecimal("1.00"),
			customer : sampleCustomer
			)
		mockDomain(CustomerAccount, [sampleCustomerAccount])
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
			date: newDate,
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
	
	private setUpCheckDeposits() {
		sampleCheckDeposit = new CheckDeposit()
		mockDomain(CheckDeposit, [sampleCheckDeposit])
	}
	
    protected void tearDown() {
        super.tearDown()
        def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
        remove DirectPaymentInvoice
        remove DirectPaymentItem
        remove DirectPayment
    }

    void testComputePaymentTotal() {
		def sampleDirectPaymentItem1 = new DirectPaymentItem(amount: new BigDecimal("10.50"))
		def sampleDirectPaymentItem2 = new DirectPaymentItem(amount: new BigDecimal("12.50"))
		mockDomain(DirectPaymentItem, [sampleDirectPaymentItem1, sampleDirectPaymentItem2])
		
		def sampleDirectPayment = new DirectPayment(items: [sampleDirectPaymentItem1, sampleDirectPaymentItem2])
		
		mockDomain(DirectPayment, [sampleDirectPayment])
		
		assertEquals "Total payment should be 23.00", new BigDecimal("23"), sampleDirectPayment.computePaymentTotal()
    }
	
	void testComputeInvoiceTotal() {
		def sampleDirectPaymentInvoice1 = new DirectPaymentInvoice(amount: new BigDecimal("10.50"))
		def sampleDirectPaymentInvoice2 = new DirectPaymentInvoice(amount: new BigDecimal("12.50"))
		mockDomain(DirectPaymentItem, [sampleDirectPaymentInvoice1, sampleDirectPaymentInvoice2])
		
		def sampleDirectPayment = new DirectPayment(invoices: [sampleDirectPaymentInvoice1, sampleDirectPaymentInvoice2])
		
		mockDomain(DirectPayment, [sampleDirectPayment])
		
		assertEquals "Total invoice should be 23.00", new BigDecimal("23"), sampleDirectPayment.computeInvoiceTotal()
	}

	void testAddDirectPaymentInvoice(DirectPaymentInvoice directPaymentInvoice){
		def sampleDirectPaymentInvoice = new DirectPaymentInvoice(amount: new BigDecimal("6.66"))
		mockDomain(DirectPayment, [sampleDirectPayment])
		mockDomain(DirectPaymentItem, sampleDirectPaymentInvoice)
		def sampleDirectPaymentSizeAfterRemoval = sampleDirectPayment.invoices.size()
		sampleDirectPayment.addDirectPaymentInvoice(sampleDirectPaymentInvoice)
		assertEquals "Number of invoices should be 1.", sampleDirectPaymentSizeAfterRemoval + 1, sampleDirectPayment.invoices.size()
	}

	void testRemoveDirectPaymentItem(DirectPaymentItem directPaymentItemInstance){
		def sampleDirectPaymentItem1 = new DirectPaymentItem(amount: new BigDecimal("6.66"))
		def sampleDirectPaymentItem2 = new DirectPaymentItem(amount: new BigDecimal("7.77"))
		mockDomain(DirectPaymentItem, [sampleDirectPaymentItem1, sampleDirectPaymentItem2])
		mockDomain(DirectPayment, [sampleDirectPayment])
		sampleDirectPayment.addToItems(sampleDirectPaymentItem1)
		sampleDirectPayment.addToItems(sampleDirectPaymentItem2)
		def sampleDirectPaymentSizeBeforeRemoval = sampleDirectPayment.items.size()
		sampleDirectPayment.removeDirectPaymentItem(sampleDirectPaymentItem2)
		assertEquals "Number of invoices should be 1.", sampleDirectPaymentSizeBeforeRemoval - 1, sampleDirectPayment.items.size()
	}
	
	void testDirectPaymentItemsWithCreditMemo() {
//		return DirectPaymentItem.withCriteria {
//			paymentType {
//				eq('description', 'Credit Memo')
//			}
//			eq('directPayment',this)
//		}
	}
	
	void testApproveTransaction(){
		def directPaymentItem = new DirectPaymentItem(paymentType : new PaymentType(description : "Credit Memo", identifier : "CM"), 
			directPayment : sampleDirectPayment)
		sampleCreditMemo.setDirectPaymentItem(directPaymentItem)
		sampleCreditMemo.takenByDirectPayment()
		sampleDirectPayment.addToItems(directPaymentItem)
		def directPaymentInvoice1 = new DirectPaymentInvoice(CustomerPaymentType.BOUNCED_CHECK)
		sampleDirectPayment.addToInvoices(directPaymentInvoice1)
		sampleBouncedCheck.linkDirectPaymentInvoice(directPaymentInvoice1)
		DirectPaymentInvoice.metaClass.'static'.getRelatedCustomerPayment = {->sampleBouncedCheck}
		DirectPaymentItem.metaClass.'static'.relatedCreditMemo = {->sampleCreditMemo}
		DirectPayment.metaClass.'static'.directPaymentItemsWithCreditMemo = {->directPaymentItem}
		assertTrue "Sample bounced check must be takenByDirectPayment", sampleBouncedCheck.isTakenByDirectPayment
		assertTrue "Sample bounced check must be approved", sampleBouncedCheck.isApproved()
		assertTrue "Credit memo must be takenByDirectPayment", sampleCreditMemo.isTakenByDirectPayment
		assertTrue "Credit memo must be approved", sampleCreditMemo.isApproved()
		sampleDirectPayment.approveTransaction()
		assertFalse "Sample bounced check must not be takenByDirectPayment", sampleBouncedCheck.isTakenByDirectPayment
		assertEquals "Sample bounced check status must be paid", "Paid", sampleBouncedCheck.status
		assertEquals "Credit memo status must be paid", "Paid", sampleCreditMemo.status
	}
	
	void testUnapproveTransaction(){
		def directPaymentItem = new DirectPaymentItem(paymentType : new PaymentType(description : "Credit Memo", identifier : "CM"), 
			directPayment : sampleDirectPayment)
		sampleCreditMemo.setDirectPaymentItem(directPaymentItem)
		sampleCreditMemo.unapprove()
		sampleDirectPayment.addToItems(directPaymentItem)
		def directPaymentInvoice1 = new DirectPaymentInvoice(CustomerPaymentType.BOUNCED_CHECK)
		sampleDirectPayment.addToInvoices(directPaymentInvoice1)
		sampleBouncedCheck.linkDirectPaymentInvoice(directPaymentInvoice1)
		sampleBouncedCheck.notTakenByDirectPayment()
		sampleBouncedCheck.unapprove()
		DirectPaymentInvoice.metaClass.'static'.getRelatedCustomerPayment = {->sampleBouncedCheck}
		DirectPaymentItem.metaClass.'static'.relatedCreditMemo = {->sampleCreditMemo}
		DirectPayment.metaClass.'static'.directPaymentItemsWithCreditMemo = {->directPaymentItem}
		assertFalse "Sample bounced check must not be takenByDirectPayment", sampleBouncedCheck.isTakenByDirectPayment
		assertTrue "Sample bounced check must be unapproved", sampleBouncedCheck.isUnapproved()
		assertTrue "Credit memo must be unapproved", sampleCreditMemo.isUnapproved()
		assertEquals "Direct Payment item must contain 3 items", 3, sampleDirectPayment.items.size()
		assertEquals "DIrect Payment invoice must be 1", 1 , sampleDirectPayment.invoices.size()
		sampleDirectPayment.unapproveTransaction()
		assertTrue "Sample bounced check must be takenByDirectPayment", sampleBouncedCheck.isTakenByDirectPayment
		assertTrue "Sample bounced check must be approved", sampleBouncedCheck.isApproved()
		assertTrue "Credit memo must be takenByDirectPayment", sampleCreditMemo.isTakenByDirectPayment
		assertTrue "Credit memo must be approved", sampleCreditMemo.isApproved()
	}
	
	void testUnassociateItems(){
        def directPaymentItem = new DirectPaymentItem(paymentType : new PaymentType(description : "Credit Memo", identifier : "CM"),
			directPayment : sampleDirectPayment)
		sampleCreditMemo.setDirectPaymentItem(directPaymentItem)
		sampleCreditMemo.takenByDirectPayment()
		sampleDirectPayment.addToItems(directPaymentItem)
		def directPaymentInvoice1 = new DirectPaymentInvoice(CustomerPaymentType.BOUNCED_CHECK)
		sampleDirectPayment.addToInvoices(directPaymentInvoice1)
		sampleBouncedCheck.linkDirectPaymentInvoice(directPaymentInvoice1)
		DirectPaymentInvoice.metaClass.'static'.getRelatedCustomerPayment = {->sampleBouncedCheck}
		DirectPaymentItem.metaClass.'static'.relatedCreditMemo = {->sampleCreditMemo}
        DirectPayment.metaClass.'static'.directPaymentItemsWithCreditMemo = {->directPaymentItem}
		assertTrue "Sample bounced check must be takenByDirectPayment", sampleBouncedCheck.isTakenByDirectPayment
		assertTrue "Sample bounced check must be approved", sampleBouncedCheck.isApproved()
		assertTrue "Credit memo must be takenByDirectPayment", sampleCreditMemo.isTakenByDirectPayment
		assertTrue "Credit memo must be approved", sampleCreditMemo.isApproved()
		sampleDirectPayment.unassociateItems()
		assertFalse "Sample bounced check must not be takenByDirectPayment", sampleBouncedCheck.isTakenByDirectPayment
		assertEquals "Sample bounced check status must be approved", "Approved", sampleBouncedCheck.status
		assertEquals "Credit memo status must be approved", "Approved", sampleCreditMemo.status
        assertFalse "Credit memo must not be takenByDirectPayment", sampleCreditMemo.isTakenByDirectPayment

	}
	
	void testIsNotUnapprovable() {
		def result = sampleDirectPayment.isNotUnapprovable()
		
		assertNull "should be unapprovable", result 
	}
	
	void testIsNotUnapprovableNotUnapprovable() {
		sampleCheckPayment.addToCheckDeposits(sampleCheckDeposit)
		
		def result = sampleDirectPayment.isNotUnapprovable()
		
		assertNotNull "should not be unapprovable", result
	}
}
