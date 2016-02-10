package com.munix

import grails.test.*
import java.text.SimpleDateFormat

class CustomerPaymentServiceTests extends GrailsUnitTestCase {
	def customerPaymentService
	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleWarehouse
	def sampleProduct
	def sampleSalesDelivery
	def sampleSalesDeliveryItem
	def sampleDiscountType
	def sampleReason
	def sampleCreditMemo
	def sampleCheckStatus
	def sampleBouncedCheck
	def sampleCustomerCharge
	def sampleCharge
	def sampleTerm

	def sdf
	
    protected void setUp() {
        super.setUp()
		sdf = new SimpleDateFormat("MM/dd/yyyy")
		
		setUpCities()
		setupTerms()
		setUpCustomerTypes()
		setUpCustomers()
				
		setUpWarehouses()
		setUpDiscountTypes()
		setUpProducts()
		setUpSalesDeliveries()
		setUpSalesDeliveryItems()
		setUpSalesDeliveryAndSalesDeliveryItemRelationship()
		
		setUpReasons()
		setUpCreditMemos()

		setUpCustomerCharges()
		setUpCharges()
		
		setUpCheckStatuses()
		setUpBouncedChecks()
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
	private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier: "warehouse", description: "sample")
		sampleWarehouse.save(flush: true)
	}

	private void setUpSalesDeliveries() {
		sampleSalesDelivery = new SalesDelivery(
				preparedBy: "me",
				customer: sampleCustomer,
				status: "Unpaid",
				termDay: BigDecimal.ZERO,
				warehouse: sampleWarehouse,
				deliveryType: "Deliver",
				)
		sampleSalesDelivery.salesDeliveryId = sampleSalesDelivery.constructId()
		sampleSalesDelivery.save(flush: true)
	}

	private void setUpProducts() {
		sampleProduct = new Product(
			identifier: "product2",
			type: sampleDiscountType)
		sampleProduct.save(flush: true)
	}

	private void setUpSalesDeliveryItems() {
		sampleSalesDeliveryItem = new SalesDeliveryItem(
				product: sampleProduct,
				price: new BigDecimal("100"),
				qty: new BigDecimal("1")
				)
		sampleSalesDeliveryItem.save(flush: true)
	}

	private void setUpSalesDeliveryAndSalesDeliveryItemRelationship() {
		sampleSalesDelivery.items = [sampleSalesDeliveryItem]
		sampleSalesDelivery.save(flush: true)
	}
	
	private void setUpDiscountTypes() {
		sampleDiscountType = new DiscountType(identifier: "discountType", description: "discount type", margin: BigDecimal.ONE)
		sampleDiscountType.save(flush: true)
	}
	
	private void setUpReasons() {
		sampleReason = new Reason(identifier: "Reason", description: "reason")
		sampleReason.save(flush: true)
	}

	private void setUpCreditMemos() {
		sampleCreditMemo = new CreditMemo(
				customer: sampleCustomer,
				discountType: sampleDiscountType,
				discount: BigDecimal.ZERO,
				reason: sampleReason,
				status: "Approved",
				preparedBy: "me",
				date: new Date(),
				commissionRate: BigDecimal.ZERO
				)
		sampleCreditMemo.save(flush: true)
	}
	
	private void setUpCustomerCharges() {
		sampleCustomerCharge = new CustomerCharge(
				date: new Date(),
				status: "Unpaid",
				customer: sampleCustomer,
				preparedBy: "me",
				amountPaid: new BigDecimal("500"))
		sampleCustomerCharge.save(flush: true)
	}
	
	private void setUpCharges() {
		sampleCharge = new Charge(
				identifier: "identifier",
				description: "description"
			)
		sampleCharge.save(flush: true)
	}
	
	private void setUpCheckStatuses() {
		sampleCheckStatus = new CheckStatus(
				identifier: "identifier",
				description: "description")
		sampleCheckStatus.save(flush: true)
	}
	
	private void setUpBouncedChecks() {
		sampleBouncedCheck = new BouncedCheck(
				date: sdf.parse("06/03/2011"),
				status: "Approved",
				customer: sampleCustomer,
				preparedBy: "me",
				bouncedCheckStatus: sampleCheckStatus,
				forRedeposit: false)
		sampleBouncedCheck.save(flush: true)
	}
		
    protected void tearDown() {
        super.tearDown()
    }

    void testGetAvailableCustomerPaymentsForDirectPayment() {
		def result = customerPaymentService.getAvailableCustomerPaymentsForDirectPayment(sampleCustomer)
		
		assertTrue "The result should contain sampleSalesDelivery", result.deliveries.contains(sampleSalesDelivery)
		assertEquals "The result should have 1 salesDelivery", 1, result.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", result.charges.contains(sampleCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, result.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", result.bouncedChecks.contains(sampleBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, result.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", result.creditMemos.contains(sampleCreditMemo)
		assertEquals "The result should have 1 creditMemo", 1, result.creditMemos.size()
	}
	
	void testGetAvailableCustomerPaymentsForDirectPaymentWithTaken() {
		sampleSalesDelivery.takenByDirectPayment()
		sampleSalesDelivery.save(flush: true)
		
		def result = customerPaymentService.getAvailableCustomerPaymentsForDirectPayment(sampleCustomer)
		
		assertFalse "The result should not contain sampleSalesDelivery", result.deliveries.contains(sampleSalesDelivery)
		assertEquals "The result should have 0 salesDelivery", 0, result.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", result.charges.contains(sampleCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, result.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", result.bouncedChecks.contains(sampleBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, result.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", result.creditMemos.contains(sampleCreditMemo)
		assertEquals "The result should have 1 creditMemo", 1, result.creditMemos.size()
	}
	
	void testGetAvailableCustomerPaymentsForCounterReceipt() {
		def result = customerPaymentService.getAvailableCustomerPaymentsForCounterReceipt(sampleCustomer)
		
		assertTrue "The result should contain sampleSalesDelivery", result.deliveries.contains(sampleSalesDelivery)
		assertEquals "The result should have 1 salesDelivery", 1, result.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", result.charges.contains(sampleCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, result.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", result.bouncedChecks.contains(sampleBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, result.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", result.creditMemos.contains(sampleCreditMemo)
		assertEquals "The result should have 1 creditMemo", 1, result.creditMemos.size()
	}
	
	void testGetAvailableCustomerPaymentsForCounterReceiptWithTaken() {
		sampleSalesDelivery.takenByCounterReceipt()
		sampleSalesDelivery.save(flush: true)
		
		def result = customerPaymentService.getAvailableCustomerPaymentsForCounterReceipt(sampleCustomer)
		
		assertFalse "The result should not contain sampleSalesDelivery", result.deliveries.contains(sampleSalesDelivery)
		assertEquals "The result should have 0 salesDelivery", 0, result.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", result.charges.contains(sampleCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, result.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", result.bouncedChecks.contains(sampleBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, result.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", result.creditMemos.contains(sampleCreditMemo)
		assertEquals "The result should have 1 creditMemo", 1, result.creditMemos.size()
	}
}
