package com.munix

import grails.test.*

class CustomerDiscountServiceTests extends GroovyTestCase {
	def customerDiscountService

	def sampleCity
	def sampleCustomerType
	def sampleCustomer

	def sampleDiscountGroup
	def sampleDiscountType	
	def sampleCustomerDiscount
	def sampleTerm

    protected void setUp() {
        super.setUp()
		setUpCities()
		setUpCustomerTypes()
		setupTerms()
		setUpCustomers()
		
		setUpDiscountGroups()
		setUpDiscountTypes()
		setUpCustomerDiscounts()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testValidateCustomerDiscount() {
		def customerDiscount = new CustomerDiscount(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discountGroup: sampleDiscountGroup,
			type: CustomerDiscount.Type.DISCOUNT
		)

		assertFalse "customer discount should already exist", customerDiscountService.validateCustomerDiscount(customerDiscount)
	}
	
    void testValidateCustomerDiscountDifferentDiscountType() {
		def discountType = new DiscountType(
			identifier: "discount type new",
			description: "desc"
		)
		discountType.save(flush: true)

		def customerDiscount = new CustomerDiscount(
			customer: sampleCustomer,
			discountType: discountType,
			discountGroup: sampleDiscountGroup,
			type: CustomerDiscount.Type.DISCOUNT
		)

		assertTrue "customer discount should be valid", customerDiscountService.validateCustomerDiscount(customerDiscount)
    }
	
    void testValidateCustomerDiscountDifferentCustomer() {
		def customer = new Customer(
			identifier: "customer new",
			name: "customer name",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
			term: sampleTerm
		)
		customer.save(flush: true)

		def customerDiscount = new CustomerDiscount(
			customer: customer,
			discountType: sampleDiscountType,
			discountGroup: sampleDiscountGroup,
			type: CustomerDiscount.Type.DISCOUNT
		)

		assertTrue "customer discount should be valid", customerDiscountService.validateCustomerDiscount(customerDiscount)
    }

	void testValidateCustomerDiscountDifferentType() {
		def customerDiscount = new CustomerDiscount(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discountGroup: sampleDiscountGroup,
			type: CustomerDiscount.Type.NET
		)

		assertTrue "customer discount should be valid", customerDiscountService.validateCustomerDiscount(customerDiscount)
	}
	
	void testValidateUpdatedCustomerDiscountSameDiscountTypeAndType() {
		def discountGroup = new DiscountGroup(identifier: "10%", description: "10%", rate:new BigDecimal("0"))
		discountGroup.save(flush: true)
		def customerDiscount = new CustomerDiscount(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discountGroup: discountGroup,
			type: CustomerDiscount.Type.DISCOUNT
		)

		assertTrue "customer discount should be valid", customerDiscountService.validateUpdatedCustomerDiscount(customerDiscount, sampleCustomerDiscount)
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
	private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(
			identifier: "discount type",
			description: "desc",
			margin: BigDecimal.ONE
		)
		sampleDiscountType.save(flush: true)
	}
	
	private void setUpDiscountGroups() {
		sampleDiscountGroup = new DiscountGroup(identifier: "10%", description: "10%", rate:new BigDecimal("0"))
		sampleDiscountGroup.save(flush: true)
	}

	private void setUpCustomerDiscounts() {
		sampleCustomerDiscount = new CustomerDiscount(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discountGroup: sampleDiscountGroup,
			type: CustomerDiscount.Type.DISCOUNT
		)
		sampleCustomerDiscount.save(flush: true)
	}
}
