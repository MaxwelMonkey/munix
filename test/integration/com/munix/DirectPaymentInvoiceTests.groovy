package com.munix	

public class DirectPaymentInvoiceTests extends GroovyTestCase {
	def sampleCustomer
	def sampleDirectPaymentInvoice
	def sampleDiscountType
	def sampleReason
	def sampleCreditMemo
	def sampleCity
	def sampleCustomerType
	def sampleTerm
	def sampleForwarder
	def sampleSalesAgent
	def sampleDirectPayment
	def sampleCustomerCharge
	def sampleSalesDelivery
	def sampleWarehouse
	def sampleProduct
	def sampleBouncedCheck
	def sampleCheckStatus

	protected void setUp() {
		super.setUp()
        setUpCities()
        setUpCustomerTypes()
        setUpTerms()
        setUpForwarders()
        setUpSalesAgents()
        setUpWarehouses()
        setUpDiscountTypes()
        setUpProducts()
        setUpCheckStatuses()

		setUpCustomers()
		setUpReasons()

		setUpCreditMemos()
        setCustomerCharges()
        setUpSalesDeliveries()
        setUpBouncedChecks()

        setUpDirectPayments()
	}


    private setUpCities() {
		sampleCity= new City(identifier:"city",
				description:"cityDesc")
		sampleCity.save()
	}
    private setUpCustomerTypes() {
		sampleCustomerType= new CustomerType(identifier:"customerType",
				description:"descCustomerType")
		sampleCustomerType.save()
	}
    private setUpTerms() {
		sampleTerm=new Term(identifier:"term",
				description:"termDesc",
				dayValue:new BigDecimal("1234"))
		sampleTerm.save()
	}
    private setUpDirectPayments() {
		sampleDirectPayment= new DirectPayment(customer:sampleCustomer,
				preparedBy:"me",
				approvedBy:"you",
				remark:"fail"
				)
		sampleDirectPayment.save()
	}
    private setUpProducts() {
		sampleProduct = new Product(
			identifier:"product2",
			type: sampleDiscountType)
		sampleProduct.save()
	}
    private setUpCheckStatuses() {
		sampleCheckStatus = new CheckStatus(
				identifier: "identifier",
				description: "description").save()
	}
	private setUpCustomers() {
		sampleCustomer= new Customer(identifier:"customer",
				name:"customer name",
				busAddrStreet:"here",
				busAddrCity:sampleCity,
				busAddrZip:"1234",
				bilAddrStreet:"1234 here",
				bilAddrCity:sampleCity,
				bilAddrZip:"1234",
				skype:"hi",
				email:"1@yahoo.com",
				landline:"1234",
				fax:"1234",
				contact:"1234",
				mobile:"1234",
				type:sampleCustomerType,
				term:sampleTerm,
				status:Customer.Status.ACTIVE,
				forwarder:sampleForwarder,
				salesAgent:sampleSalesAgent,
				autoApprove:false,
				enableCreditLimit:true,
				accountName1:"1234",
				accountNumber1:"12345",
				tin:"1234",
				collectionMode:"12345",
				collectionPreference:"12345",
				collectionSchedule:"12345",
				generalRemark:"2345",
				creditRemark:"12345",
				contactPosition:"12345",
				owner:"12345",
				yahoo:"12345",
				secondaryContact:"12345",
				secondaryContactPosition:"12345")
		sampleCustomer.save()
	}
    private setUpSalesAgents() {
		sampleSalesAgent= new SalesAgent(identifier:"salesAgent",
				lastName:"last",
				firstName:"first",
				street:"street",
				city:sampleCity,
				zip:"1234",
				mobile:"1233",
				landline:"12345",
				skype:"nothing.com",
				yahoo:"1@yahoo.com",
				email:"something@samthing.com")
		sampleSalesAgent.save()
	}
	private setUpForwarders() {
		sampleForwarder=new Forwarder(identifier:"forward",
				description:"descForw",
				street:"nothing",
				city:sampleCity,
				zip:"1234",
				landline:"1234",
				contact:"1234",
				contactPosition:"you")
		sampleForwarder.save()
	}
	private setUpDiscountTypes() {
		sampleDiscountType=new DiscountType(identifier:"discountType",
				description:"discount type", margin: BigDecimal.ONE)
		sampleDiscountType.save()
	}
	
	private setUpReasons() {
		sampleReason = new Reason(identifier: "Reason", description: "reason")
		sampleReason.save(flush: true)
	}

	private setUpCreditMemos() {
		sampleCreditMemo=new CreditMemo(
				customer:sampleCustomer,
				discountType:sampleDiscountType,
				reason: sampleReason,
				discount:new BigDecimal("0"),
				status:"Unapproved",
				preparedBy:"me",
				date:new Date(),
				commissionRate: BigDecimal.ZERO
				)
		sampleCreditMemo.save()
	}
	private setCustomerCharges() {
		sampleCustomerCharge=new CustomerCharge(
				date:new Date(),
				status:"Unpaid",
				customer:sampleCustomer,
				preparedBy:"me",
				amountPaid:new BigDecimal("500"))
		sampleCustomerCharge.save()
	}
    private setUpSalesDeliveries() {
		sampleSalesDelivery=new SalesDelivery(
				preparedBy: "me",
				customer:sampleCustomer,
				status:"Unpaid",
				termDay:new BigDecimal("0"),
				warehouse:sampleWarehouse,
				deliveryType:"Deliver",
				items: [new SalesDeliveryItem(product: sampleProduct, price: 1, qty: 1)]
				)
		sampleSalesDelivery.salesDeliveryId = sampleSalesDelivery.constructId()
		sampleSalesDelivery.save()
	}
    private setUpBouncedChecks() {
		sampleBouncedCheck= new BouncedCheck(
				date:new Date(),
				status:"Approved",
				customer:sampleCustomer,
				preparedBy:"me",
				bouncedCheckStatus: sampleCheckStatus,
				forRedeposit: false)
		sampleBouncedCheck.save()
	}
    private setUpWarehouses() {
		sampleWarehouse=new Warehouse(identifier:"warehouse",
				description:"sample")
		sampleWarehouse.save()
	}
	protected void tearDown() {
		super.tearDown()
	}

	void testGetRelatedCustomerPaymentCreditMemo() {
        sampleDirectPaymentInvoice = new DirectPaymentInvoice(type: CustomerPaymentType.CREDIT_MEMO,directPayment:sampleDirectPayment)
		sampleDirectPaymentInvoice.save()
        sampleCreditMemo.addToInvoices(sampleDirectPaymentInvoice)
        sampleCreditMemo.save()
		assertEquals "[GUARD] Credit Memo should have 1 invoice", 1, sampleCreditMemo.invoices.size()
		assertTrue "[GUARD] Credit Memo should contain sampleDirectPaymentInvoice", sampleCreditMemo.invoices.contains(sampleDirectPaymentInvoice)

		def customerPayment = sampleDirectPaymentInvoice.getRelatedCustomerPayment()
		assertNotNull "Should retrieve a credit memo", customerPayment
		assertTrue "Retrieved customer payment should be a CreditMemo", customerPayment instanceof CreditMemo
        assertEquals "Retrieved customer payment should be sampleCreditMemo", customerPayment,sampleCreditMemo
	}
	
    void testGetRelatedCustomerPaymentCustomerCharge() {
        sampleDirectPaymentInvoice = new DirectPaymentInvoice(type: CustomerPaymentType.CUSTOMER_CHARGE,directPayment:sampleDirectPayment)
		sampleDirectPaymentInvoice.save()
        sampleCustomerCharge.addToInvoices(sampleDirectPaymentInvoice)
        sampleCustomerCharge.save()
		assertEquals "[GUARD] CustomerCharge should have 1 invoice", 1, sampleCustomerCharge.invoices.size()
		assertTrue "[GUARD] CustomerCharge should contain sampleDirectPaymentInvoice", sampleCustomerCharge.invoices.contains(sampleDirectPaymentInvoice)

		def customerPayment = sampleDirectPaymentInvoice.getRelatedCustomerPayment()

		assertNotNull "Should retrieve a CustomerCharge", customerPayment
		assertTrue "Retrieved customer payment should be a CustomerCharge", customerPayment instanceof CustomerCharge
        assertEquals "Retrieved customer payment should be sampleCustomerCharge", customerPayment,sampleCustomerCharge

	}
	
    void testGetRelatedCustomerPaymentSalesDelivery() {
        sampleDirectPaymentInvoice = new DirectPaymentInvoice(type: CustomerPaymentType.SALES_DELIVERY,directPayment:sampleDirectPayment)
		sampleDirectPaymentInvoice.save()
        sampleSalesDelivery.addToInvoices(sampleDirectPaymentInvoice)
        sampleSalesDelivery.save()
		assertEquals "[GUARD] SalesDelivery should have 1 invoice", 1, sampleSalesDelivery.invoices.size()
		assertTrue "[GUARD] SalesDelivery should contain sampleDirectPaymentInvoice", sampleSalesDelivery.invoices.contains(sampleDirectPaymentInvoice)

		def customerPayment = sampleDirectPaymentInvoice.getRelatedCustomerPayment()

		assertNotNull "Should retrieve a SalesDelivery", customerPayment
		assertTrue "Retrieved customer payment should be a SalesDelivery", customerPayment instanceof SalesDelivery
        assertEquals "Retrieved customer payment should be sampleSalesDelivery", customerPayment,sampleSalesDelivery

	}
	
    void testGetRelatedCustomerPaymentBouncedCheck() {
        sampleDirectPaymentInvoice = new DirectPaymentInvoice(type: CustomerPaymentType.BOUNCED_CHECK,directPayment:sampleDirectPayment)
		sampleDirectPaymentInvoice.save()
        sampleBouncedCheck.addToInvoices(sampleDirectPaymentInvoice)
        sampleBouncedCheck.save()
		assertEquals "[GUARD] BouncedCheck should have 1 invoice", 1, sampleBouncedCheck.invoices.size()
		assertTrue "[GUARD] BouncedCheck should contain sampleDirectPaymentInvoice", sampleBouncedCheck.invoices.contains(sampleDirectPaymentInvoice)

		def customerPayment = sampleDirectPaymentInvoice.getRelatedCustomerPayment()

		assertNotNull "Should retrieve a BouncedCheck", customerPayment
		assertTrue "Retrieved customer payment should be a BouncedCheck", customerPayment instanceof BouncedCheck
        assertEquals "Retrieved customer payment should be sampleBouncedCheck", customerPayment,sampleBouncedCheck

	}
}
