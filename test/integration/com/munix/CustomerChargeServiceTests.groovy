package com.munix

import grails.test.*

class CustomerChargeServiceTests extends GrailsUnitTestCase {
	def customerChargeService
	def generalMethodService
	
	def sampleCity
	def sampleDiscountGroup
	def sampleCustomerType
	def sampleCustomer
	def sampleCustomerAccount
	def sampleCustomerCharge
	def sampleCustomerCharge2
	def sampleItem
	def sampleItem2
	def sampleInvoice
	def sampleInvoice2
	def sampleTerm
	def sampleDirectPayment
	
    protected void setUp() {
        super.setUp()
		
		setUpCities()
		setUpCustomerTypes()
		setupTerms()
		setUpCustomers()
        setupCustomerAccount()
		setUpDirectPayments()
		setUpCustomerCharge()
		setUpItems()
		setUpInvoices()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerateList() {
		def customerCharge1 = new CustomerCharge(
			customer: sampleCustomer,
			date: generalMethodService.createDate("06/01/2010"),
			remark: "remark",
			preparedBy: "me"
		)
		customerCharge1.save(flush: true)

		setUpFilteredCustomerCharges()
		
		def params = [:]
		params.searchIdentifier = customerCharge1.id.toString()
		params.searchCustomerName = "customer"
		params.searchStatus = "Unapproved"
		params.searchRemarks = "remark"
		params.searchDateFrom = "01/01/2010"
		params.searchDateFrom_value = "01/01/2010"
		params.searchDateFrom_month = "01"
		params.searchDateFrom_day = "01"
		params.searchDateFrom_year = "2010"
		params.searchDateTo = "01/01/2011"
		params.searchDateTo_value = "01/01/2011"
		params.searchDateTo_month = "01"
		params.searchDateTo_day = "01"
		params.searchDateTo_year = "2011"

		def result = customerChargeService.generateList(params)
		
		assertTrue "Result should contain customerCharge1", result.contains(customerCharge1)
		assertEquals "Result total should be 1", 1, result.size()
    }
	
	private void setUpCustomerCharge(){
		sampleCustomerCharge = new CustomerCharge(
			customer: sampleCustomer,
			date: generalMethodService.createDate("06/01/2010"),
			remark: "remark",
			preparedBy: "me"
		)
		sampleCustomerCharge.save()
		
		sampleCustomerCharge2 = new CustomerCharge(
			customer: sampleCustomer,
			date: generalMethodService.createDate("06/01/2010"),
			remark: "remark",
			preparedBy: "me"
		)
		sampleCustomerCharge2.save()
	}
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
    private setupCustomerAccount(){
        sampleCustomerAccount = new CustomerAccount(customer:sampleCustomer)
        sampleCustomerAccount.save()
        sampleCustomer.customerAccount = sampleCustomerAccount
        sampleCustomer.save()
    }
	//CustomerChargeItems
	private void setUpItems(){
		def sampleCharge = new Charge(identifier: "id", description: "description")
		sampleCharge.save()
		sampleItem = new CustomerChargeItem(
			charge: sampleCharge,
			amount: new BigDecimal("80.00"),
			customerCharge: sampleCustomerCharge)
		sampleItem.save()
		sampleCustomerCharge.addToItems(sampleItem)
		sampleCustomerCharge.save()
		
		sampleItem2 = new CustomerChargeItem(
			charge: sampleCharge,
			amount: new BigDecimal("20.00"),
			customerCharge: sampleCustomerCharge)
		sampleItem2.save()
		sampleCustomerCharge.addToItems(sampleItem2)
		sampleCustomerCharge.save()
	}
	
	private void setUpInvoices(){
		sampleInvoice = new DirectPaymentInvoice(type: CustomerPaymentType.CUSTOMER_CHARGE, amount: new BigDecimal("10.00"), ,directPayment:sampleDirectPayment)
		sampleInvoice.save()
		sampleCustomerCharge.addToInvoices(sampleInvoice)
		sampleCustomerCharge.save()
		
		sampleInvoice2 = new DirectPaymentInvoice(type: CustomerPaymentType.CUSTOMER_CHARGE, amount: new BigDecimal("20.00"), ,directPayment:sampleDirectPayment)
		sampleInvoice2.save()
		sampleCustomerCharge.addToInvoices(sampleInvoice2)
		sampleCustomerCharge.save()
	}
	
	private setUpDirectPayments() {
		sampleDirectPayment= new DirectPayment(customer:sampleCustomer,
				preparedBy:"me",
				approvedBy:"you",
				remark:"fail"
				)
		sampleDirectPayment.save()
	}
	
	private void setUpCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		sampleCity.save(flush: true)
	}
	private void setUpDiscountGroup() {
		sampleDiscountGroup = new DiscountGroup(identifier: "10%", description: "10%", rate:new BigDecimal("0"))
		sampleDiscountGroup.save(flush: true)
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
	
	private void setUpFilteredCustomerCharges() {
		def customerChargeFilteredByID = new CustomerCharge(
			customer: sampleCustomer,
			date: generalMethodService.createDate("06/01/2010"),
			remark: "remark",
			preparedBy: "me"
		)
		customerChargeFilteredByID.save(flush: true)

		def customer1 = new Customer(
			identifier: "customer new",
			name: "name",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
			term: sampleTerm
		)
		customer1.save(flush: true)
		
		def customerChargeFilteredByCustomerName = new CustomerCharge(
			customer: customer1,
			date: generalMethodService.createDate("06/01/2010"),
			remark: "remark",
			preparedBy: "me"
		)
		customerChargeFilteredByCustomerName.save(flush: true)
		
		def customerChargeFilteredByDate = new CustomerCharge(
			customer: sampleCustomer,
			date: generalMethodService.createDate("06/01/2011"),
			remark: "remark",
			preparedBy: "me"
		)
		customerChargeFilteredByDate.save(flush: true)
		
		def customerChargeFilteredByRemark = new CustomerCharge(
			customer: sampleCustomer,
			date: generalMethodService.createDate("06/01/2010"),
			remark: "boo hoo",
			preparedBy: "me"
		)
		customerChargeFilteredByRemark.save(flush: true)
	}
	
	def testGetAllUnpaidCustomerChargesNoResult(){
		sampleCustomerCharge.unapprove()
		sampleCustomerCharge2.unapprove()
		
		assertNotNull "[Guard] Customer Charge must not be null", sampleCustomerCharge
		assertTrue "[Guard] Customer Charge must have a content", CustomerCharge.list().size() > 0
		
		def counter = 0
		CustomerCharge.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] Customer Charge must not unpaid status", 0, counter
		
		def result = customerChargeService.getAllUnpaidSalesDeliveryList(sampleCustomer)
		assertEquals "Result must not have a content", 0, result.size()
	}
	
	def testAllGetUnpaidCustomerChargesOneResult(){
		sampleCustomerCharge.approve()
		sampleCustomerCharge2.unapprove()
		
		assertNotNull "[Guard] Customer Charge must not be null", sampleCustomerCharge
		assertTrue "[Guard] Customer Charge must have a content", CustomerCharge.list().size() > 0
		
		def counter = 0
		CustomerCharge.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] List must have one customer charge that has unpaid status", 1, counter
		
		def result = customerChargeService.getAllUnpaidSalesDeliveryList(sampleCustomer)
		assertEquals "Result must not have a content", 1, result.size()
	}
	
	def testGetUnpaidCustomerChargesNoResult(){
		sampleCustomerCharge.unapprove()
		sampleCustomerCharge2.unapprove()
		
		assertNotNull "[Guard] Customer Charge must not be null", sampleCustomerCharge
		assertTrue "[Guard] Customer Charge must have a content", CustomerCharge.list().size() > 0
		
		def counter = 0
		CustomerCharge.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] Customer Charge must not unpaid status", 0, counter
		
		
		def result = customerChargeService.getUnpaidCustomerCharges(sampleCustomer, [id: sampleCustomer.id])
		assertEquals "Result must not have a content", 0, result.size()
	}
	
	def testGetUnpaidCustomerChargesOneResult(){
		sampleCustomerCharge.approve()
		sampleCustomerCharge2.unapprove()
		
		assertNotNull "[Guard] Customer Charge must not be null", sampleCustomerCharge
		assertTrue "[Guard] Customer Charge must have a content", CustomerCharge.list().size() > 0
		
		def counter = 0
		CustomerCharge.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals "[Guard] List must have one customer charge that has unpaid status", 1, counter
		
		def result = customerChargeService.getUnpaidCustomerCharges(sampleCustomer, [id: sampleCustomer.id])
		assertEquals "Result must not have a content", 1, result.size()
	}
	
	void testComputeTotalUnpaidCustomerCharges(){
		sampleCustomerCharge.approve()
		sampleCustomerCharge2.approve()
		
		assertNotNull "[Guard] Customer Charge must not be null", sampleCustomerCharge
		assertTrue    "[Guard] Customer Charge must have a content", CustomerCharge.list().size() > 0
		
		def counter = 0
		CustomerCharge.list().each{
			if(it.isApproved()){
				counter++;
			}
		}
		
		assertEquals  "[Guard] Customer Charge must have two unpaid status", 2, counter
		
		BigDecimal result = customerChargeService.computeTotalUnpaidCustomerCharges(sampleCustomer)
		assertEquals  "Customer Charge must have a result", new BigDecimal("70.00"), result
	}
    void testUnapproveCustomerCharge(){
        def result = customerChargeService.unapproveCustomerCharge(sampleCustomerCharge)
		assertEquals "Sample bounced check status should be Unapproved", "Unapproved", sampleCustomerCharge.status
        assertTrue "Result must be true", result
    }
    void testApproveCustomerCharge(){
        def result = customerChargeService.approveCustomerCharge(sampleCustomerCharge)
		assertEquals "Sample bounced check status should be Unpaid", "Unpaid", sampleCustomerCharge.status
        assertTrue "Result must be true", result
    }
}
