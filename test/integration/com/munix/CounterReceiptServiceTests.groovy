package com.munix;

import grails.test.*
import java.text.SimpleDateFormat

class CounterReceiptServiceTests  extends GrailsUnitTestCase {
	def counterReceiptService
	def editCounterReceiptService
	def generalMethodService

	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleWarehouse
	def sampleProduct
	def sampleSalesOrder
	def sampleSalesDelivery
	def sampleAttachedSalesDelivery
	def sampleSalesDeliveryItem
	def sampleDiscountType
	def sampleReason
	def sampleCreditMemo
	def sampleAttachedCreditMemo
	def sampleCheckStatus
	def sampleBouncedCheck
	def sampleAttachedBouncedCheck
	def sampleCustomerCharge
	def sampleAttachedCustomerCharge
	def sampleCharge
	def sampleCounterReceiptWithCustomerPayments
	def sampleTerm
	def sampleCounterReceipt
	def sampleCounterReceipt2
	def sampleCounterReceiptItemWithSalesDelivery
	def sampleCounterReceiptItemWithCustomerCharge
	def sampleCounterReceiptItemWithCreditMemo
	def sampleCounterReceiptItemWithBouncedCheck
	def sampleDirectPaymentInvoiceBouncedCheck
	def sampleDirectPaymentInvoiceCreditMemo
	def sampleDirectPaymentInvoiceSalesDelivery
	def sampleDirectPaymentInvoiceCustomerCharge
	def sampleDirectPayment
	def sampleCollectionSchedule
	def sampleCollectionScheduleItem
	def sampleCollectionScheduleItem2
	def sampleCollectionScheduleItem3
    def sampleCollector

	def sdf = new SimpleDateFormat("MM/dd/yyyy")
	
	protected void setUp() {
		super.setUp()
		setUpCities()
		setupTerms()
		setUpCustomerTypes()
		setUpCustomers()
				
		setUpWarehouses()
		setUpDiscountTypes()
		setUpProducts()
		setUpReasons()
		
		setUpSalesOrders()
		
		setUpSalesDeliveries()
		setUpSalesDeliveryItems()
		setUpSalesDeliveryAndSalesDeliveryItemRelationship()
		
		setUpCreditMemos()

		setUpCustomerCharges()
		setUpCharges()
		
		setUpCheckStatuses()
		setUpBouncedChecks()

		setUpCounterReceipts()
		setUpCounterReceiptItems()
		setUpCounterReceiptAndCounterReceiptItemsRelationship()

        setUpCollector()
        setUpCollectionSchedule()
        setUpCollectionScheduleItem()

        setUpDirectPayments()
        setUpDirectPaymentInvoices()
        setUpCustomerPaymentAndDirectPaymentInvoiceRelationship()
	}
	
	private void setUpCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		sampleCity.save(flush: true)
	}
	
    private void setUpCollector() {
		sampleCollector = new Collector(identifier: "collector", description: "collecting")
		sampleCollector.save(flush: true)
	}
	
    private void setUpCollectionSchedule() {
		sampleCollectionSchedule = new CollectionSchedule(
            identifier:"CollectionSched",
            startDate:new Date(),
            endDate:new Date(),
            preparedBy:"me",
            collector:sampleCollector)
		sampleCollectionSchedule.save(flush: true)
	}
	private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
    private void setUpCollectionScheduleItem() {
		sampleCollectionScheduleItem = new CollectionScheduleItem(
            counterReceipt:sampleCounterReceipt,
            isComplete: true,
            amount:BigDecimal.ONE,
            type:"Counter")
		sampleCollectionScheduleItem.save(flush: true)
        sampleCollectionScheduleItem2 = new CollectionScheduleItem(
            counterReceipt:sampleCounterReceipt,
            isComplete: true,
            amount:BigDecimal.ONE,
            type:"Collection")
		sampleCollectionScheduleItem2.save(flush: true)
        sampleCollectionScheduleItem3 = new CollectionScheduleItem(
            counterReceipt:sampleCounterReceipt,
            isComplete: true,
            amount:BigDecimal.ONE,
            type:"Both")
		sampleCollectionScheduleItem3.save(flush: true)
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

	private void setUpWarehouses() {
		sampleWarehouse = new Warehouse(identifier: "warehouse", description: "sample")
		sampleWarehouse.save(flush: true)
	}
	
	private void setUpSalesOrders() {
        sampleSalesOrder = new SalesOrder(
            discountType:sampleDiscountType,
            priceType:PriceType.RETAIL,
            customer: sampleCustomer,
            items:[new SalesOrderItem(product:sampleProduct,
                qty:new BigDecimal("1"),
                price:new BigDecimal("1"),
                finalPrice:new BigDecimal("1"))])
        sampleSalesOrder.save()
	}

	private void setUpSalesDeliveries() {
		sampleSalesDelivery = new SalesDelivery(
			preparedBy: "me",
			customer: sampleCustomer,
			status: "Unpaid",
			termDay: BigDecimal.ZERO,
			warehouse: sampleWarehouse,
			deliveryType: "Deliver",
			invoice: sampleSalesOrder,
		)
		sampleSalesDelivery.salesDeliveryId = sampleSalesDelivery.constructId()
		sampleSalesDelivery.save(flush: true)

		sampleAttachedSalesDelivery = new SalesDelivery(
			preparedBy: "me",
			customer: sampleCustomer,
			status: "Unpaid",
			termDay: BigDecimal.ZERO,
			warehouse: sampleWarehouse,
			deliveryType: "Deliver",
			invoice: sampleSalesOrder,
		)
		sampleAttachedSalesDelivery.salesDeliveryId = sampleAttachedSalesDelivery.constructId()
		sampleAttachedSalesDelivery.save(flush: true)
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

		sampleAttachedSalesDelivery.items = [sampleSalesDeliveryItem]
		sampleAttachedSalesDelivery.save(flush: true)
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

		sampleAttachedCreditMemo = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discount: BigDecimal.ZERO,
			reason: sampleReason,
			status: "Approved",
			preparedBy: "me",
			date: new Date(),
			commissionRate: BigDecimal.ZERO
		)
		sampleAttachedCreditMemo.save(flush: true)
	}

	private void setUpCustomerCharges() {
		sampleCustomerCharge = new CustomerCharge(
			date: new Date(),
			status: "Unpaid",
			customer: sampleCustomer,
			preparedBy: "me",
			amountPaid: new BigDecimal("500")
		)
		sampleCustomerCharge.save(flush: true)

		sampleAttachedCustomerCharge = new CustomerCharge(
			date: new Date(),
			status: "Unpaid",
			customer: sampleCustomer,
			preparedBy: "me",
			amountPaid: new BigDecimal("500")
		)
		sampleAttachedCustomerCharge.save(flush: true)
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
			description: "description"
		)
		sampleCheckStatus.save(flush: true)
	}

	private void setUpBouncedChecks() {
		sampleBouncedCheck = new BouncedCheck(
			date: sdf.parse("06/03/2011"),
			status: "Approved",
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: false
		)
		sampleBouncedCheck.save(flush: true)

		sampleAttachedBouncedCheck = new BouncedCheck(
			date: sdf.parse("06/03/2011"),
			status: "Approved",
			customer: sampleCustomer,
			preparedBy: "me",
			bouncedCheckStatus: sampleCheckStatus,
			forRedeposit: false
		)
		sampleAttachedBouncedCheck.save(flush: true)
	}

	private void setUpCounterReceipts() {
		sampleCounterReceipt = new CounterReceipt(
			date: sdf.parse("06/09/2011"),
			status: CounterReceipt.Status.UNAPPROVED,
			customer: sampleCustomer,
			preparedBy: "me"
		)
		sampleCounterReceipt.save(flush: true)

		sampleCounterReceiptWithCustomerPayments = new CounterReceipt(
			date: sdf.parse("06/09/2011"),
			status: CounterReceipt.Status.UNAPPROVED,
			customer: sampleCustomer,
			preparedBy: "me"
		)
		sampleCounterReceiptWithCustomerPayments.save(flush: true)
	}
	
	private void setUpCounterReceiptItems() {
		sampleCounterReceiptItemWithSalesDelivery = new CounterReceiptItem(
			counterReceipt: sampleCounterReceiptWithCustomerPayments,
			invoice: sampleAttachedSalesDelivery
		)
		sampleCounterReceiptItemWithSalesDelivery.save(flush: true)
		
		sampleCounterReceiptItemWithCustomerCharge = new CounterReceiptItem(
			counterReceipt: sampleCounterReceiptWithCustomerPayments,
			invoice: sampleAttachedCustomerCharge
		)
		sampleCounterReceiptItemWithCustomerCharge.save(flush: true)
		
		sampleCounterReceiptItemWithCreditMemo = new CounterReceiptItem(
			counterReceipt: sampleCounterReceiptWithCustomerPayments,
			invoice: sampleAttachedCreditMemo
		)
		sampleCounterReceiptItemWithCreditMemo.save(flush: true)
		
		sampleCounterReceiptItemWithBouncedCheck = new CounterReceiptItem(
			counterReceipt: sampleCounterReceiptWithCustomerPayments,
			invoice: sampleAttachedBouncedCheck
		)
		sampleCounterReceiptItemWithBouncedCheck.save(flush: true)
	}
	
	private void setUpCounterReceiptAndCounterReceiptItemsRelationship() {
		sampleCounterReceiptWithCustomerPayments.items = [
			sampleCounterReceiptItemWithSalesDelivery, sampleCounterReceiptItemWithCustomerCharge, 
			sampleCounterReceiptItemWithCreditMemo, sampleCounterReceiptItemWithBouncedCheck
		]

		sampleCounterReceiptWithCustomerPayments.save(flush: true)

		sampleAttachedSalesDelivery.takenByCounterReceipt()
		sampleAttachedSalesDelivery.save()
		sampleAttachedCustomerCharge.takenByCounterReceipt()
		sampleAttachedCustomerCharge.save()
		sampleAttachedCreditMemo.takenByCounterReceipt()
		sampleAttachedCreditMemo.save()
		sampleAttachedBouncedCheck.takenByCounterReceipt()
		sampleAttachedBouncedCheck.save()
	}

    private void setUpDirectPayments() {
		sampleDirectPayment = new DirectPayment(
				customer: sampleCustomer,
				preparedBy: "me",
				approvedBy: "you",
				remark: "fail",
        )
		sampleDirectPayment.save()
	}

    private void setUpDirectPaymentInvoices() {
		sampleDirectPaymentInvoiceBouncedCheck = new DirectPaymentInvoice(
				type: CustomerPaymentType.BOUNCED_CHECK,
				amount: new BigDecimal("148"))
		sampleDirectPaymentInvoiceBouncedCheck.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceBouncedCheck.save()

		sampleDirectPaymentInvoiceSalesDelivery = new DirectPaymentInvoice(
				type: CustomerPaymentType.SALES_DELIVERY,
				amount: new BigDecimal("12"))
		sampleDirectPaymentInvoiceSalesDelivery.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceSalesDelivery.save()

		sampleDirectPaymentInvoiceCreditMemo = new DirectPaymentInvoice(
				type: CustomerPaymentType.CREDIT_MEMO,
				amount: new BigDecimal("12"))
		sampleDirectPaymentInvoiceCreditMemo.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceCreditMemo.save()

		sampleDirectPaymentInvoiceCustomerCharge = new DirectPaymentInvoice(
				type: CustomerPaymentType.CUSTOMER_CHARGE,
				amount: new BigDecimal("12"))
		sampleDirectPaymentInvoiceCustomerCharge.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceCustomerCharge.save()
	}
	
	private void setUpCustomerPaymentAndDirectPaymentInvoiceRelationship() {
		sampleCreditMemo.invoices = [sampleDirectPaymentInvoiceCreditMemo]
		sampleCreditMemo.takenByDirectPayment()
		sampleCreditMemo.save()

		sampleBouncedCheck.invoices = [sampleDirectPaymentInvoiceBouncedCheck]
		sampleBouncedCheck.takenByDirectPayment()
		sampleBouncedCheck.save()

		sampleCustomerCharge.invoices = [sampleDirectPaymentInvoiceCustomerCharge]
		sampleCustomerCharge.takenByDirectPayment()
		sampleCustomerCharge.save()

		sampleSalesDelivery.invoices = [sampleDirectPaymentInvoiceSalesDelivery]
		sampleSalesDelivery.takenByDirectPayment()
		sampleSalesDelivery.save()
	}
	protected void tearDown() {
		super.tearDown()
	}
	
	void testGetAvailableCustomerPaymentsForCounterReceipt() {
		def mockCustomerPaymentService = mockFor(CustomerPaymentService)
		mockCustomerPaymentService.demand.getAvailableCustomerPaymentsForCounterReceipt(1..1) {x ->
			return [deliveries: [sampleSalesDelivery], charges: [sampleCustomerCharge],
					bouncedChecks: [sampleBouncedCheck], creditMemos: [sampleCreditMemo]] 
		}		
		counterReceiptService.customerPaymentService = mockCustomerPaymentService.createMock()
		
		def result = counterReceiptService.getAvailableCustomerPaymentsForCounterReceipt(sampleCounterReceiptWithCustomerPayments)
		
		assertTrue "The result should contain sampleSalesDelivery", result.deliveries.contains(sampleSalesDelivery)
		assertEquals "The result should have 1 salesDelivery", 1, result.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", result.charges.contains(sampleCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, result.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", result.bouncedChecks.contains(sampleBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, result.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", result.creditMemos.contains(sampleCreditMemo)
		assertEquals "The result should have 1 creditMemo", 1, result.creditMemos.size()
	}
	
	void testGetCustomerPaymentsForCounterReceipt() {
		assertTrue "[GUARD] The result should contain sampleAttachedSalesDelivery", sampleCounterReceiptWithCustomerPayments.deliveries.contains(sampleAttachedSalesDelivery)
		assertEquals "[GUARD] The result should have 1 salesDelivery", 1, sampleCounterReceiptWithCustomerPayments.deliveries.size()
		assertTrue "[GUARD] The result should contain sampleAttachedCustomerCharge", sampleCounterReceiptWithCustomerPayments.charges.contains(sampleAttachedCustomerCharge)
		assertEquals "[GUARD] The result should have 1 customerCharge", 1, sampleCounterReceiptWithCustomerPayments.charges.size()
		assertTrue "[GUARD] The result should contain sampleAttachedBouncedCheck", sampleCounterReceiptWithCustomerPayments.bouncedChecks.contains(sampleAttachedBouncedCheck)
		assertEquals "[GUARD] The result should have 1 bouncedCheck", 1, sampleCounterReceiptWithCustomerPayments.bouncedChecks.size()
		assertTrue "[GUARD] The result should contain sampleAttachedCreditMemo", sampleCounterReceiptWithCustomerPayments.creditMemos.contains(sampleAttachedCreditMemo)
		assertEquals "[GUARD] The result should have 1 creditMemo", 1, sampleCounterReceiptWithCustomerPayments.creditMemos.size()
		def mockCustomerPaymentService = mockFor(CustomerPaymentService)
		mockCustomerPaymentService.demand.getAvailableCustomerPaymentsForCounterReceipt(1..1) {x ->
			return [deliveries: [sampleSalesDelivery], charges: [sampleCustomerCharge],
					bouncedChecks: [sampleBouncedCheck], creditMemos: [sampleCreditMemo]]
		}
		counterReceiptService.customerPaymentService = mockCustomerPaymentService.createMock()
		
		def result = counterReceiptService.getCustomerPaymentsForCounterReceipt(sampleCounterReceiptWithCustomerPayments)
		
		assertTrue "The result should contain sampleSalesDelivery", result.deliveries.contains(sampleSalesDelivery)
		assertEquals "The result should have 1 salesDelivery", 2, result.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", result.charges.contains(sampleCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 2, result.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", result.bouncedChecks.contains(sampleBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 2, result.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", result.creditMemos.contains(sampleCreditMemo)
		assertEquals "The result should have 1 creditMemo", 2, result.creditMemos.size()
	}
	
	void testUpdateCounterReceipt() {
		assertTrue "[GUARD] The result should contain sampleAttachedSalesDelivery", sampleCounterReceiptWithCustomerPayments.deliveries.contains(sampleAttachedSalesDelivery)
		assertEquals "[GUARD] The result should have 1 salesDelivery", 1, sampleCounterReceiptWithCustomerPayments.deliveries.size()
		assertTrue "[GUARD] The result should contain sampleAttachedCustomerCharge", sampleCounterReceiptWithCustomerPayments.charges.contains(sampleAttachedCustomerCharge)
		assertEquals "[GUARD] The result should have 1 customerCharge", 1, sampleCounterReceiptWithCustomerPayments.charges.size()
		assertTrue "[GUARD] The result should contain sampleAttachedBouncedCheck", sampleCounterReceiptWithCustomerPayments.bouncedChecks.contains(sampleAttachedBouncedCheck)
		assertEquals "[GUARD] The result should have 1 bouncedCheck", 1, sampleCounterReceiptWithCustomerPayments.bouncedChecks.size()
		assertTrue "[GUARD] The result should contain sampleAttachedCreditMemo", sampleCounterReceiptWithCustomerPayments.creditMemos.contains(sampleAttachedCreditMemo)
		assertEquals "[GUARD] The result should have 1 creditMemo", 1, sampleCounterReceiptWithCustomerPayments.creditMemos.size()
		def map = [deliveries: [sampleAttachedSalesDelivery.id], charges: [sampleAttachedCustomerCharge.id],
				   bouncedChecks: [sampleAttachedBouncedCheck.id], creditMemos: [sampleAttachedCreditMemo.id]]
		
		counterReceiptService.updateCounterReceipt(sampleCounterReceiptWithCustomerPayments, map)
		
		assertTrue "The result should contain sampleSalesDelivery", sampleCounterReceiptWithCustomerPayments.deliveries.contains(sampleAttachedSalesDelivery)
		assertEquals "The result should have 1 salesDelivery", 1, sampleCounterReceiptWithCustomerPayments.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", sampleCounterReceiptWithCustomerPayments.charges.contains(sampleAttachedCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, sampleCounterReceiptWithCustomerPayments.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", sampleCounterReceiptWithCustomerPayments.bouncedChecks.contains(sampleAttachedBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, sampleCounterReceiptWithCustomerPayments.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", sampleCounterReceiptWithCustomerPayments.creditMemos.contains(sampleAttachedCreditMemo)
		assertEquals "The result should have 1 creditMemo", 1, sampleCounterReceiptWithCustomerPayments.creditMemos.size()
	}
	
	void testUpdateCounterReceiptAddOneSalesDelivery(){
        assertTrue "[GUARD] Counter Receipt must not contain a sales delivery", sampleCounterReceipt.deliveries.isEmpty()
        assertTrue "[GUARD] Counter Receipt must not contain a customer charge", sampleCounterReceipt.charges.isEmpty()
		
        def result = counterReceiptService.updateCounterReceipt(sampleCounterReceipt, [deliveries:[sampleAttachedSalesDelivery.id],charges:[]])

        assertEquals "counter receipt deliveries must only contain one delivery", 1, sampleCounterReceipt.deliveries.size()
        assertTrue "counter receipt must contain sample sales delivery", sampleCounterReceipt.deliveries.contains(sampleAttachedSalesDelivery)
        assertTrue "Counter Receipt must have a return value of true", result
		assertTrue "SampleSalesDelivery should be taken be by counter receipt", sampleAttachedSalesDelivery.isTakenByCounterReceipt
    }

    void testUpdateCounterReceiptRemoveSalesDelivery(){
		def counterReceiptItem = new CounterReceiptItem(
			invoice: sampleAttachedSalesDelivery,
			counterReceipt: sampleCounterReceipt
		) 
		counterReceiptItem.save(flush: true)
        sampleCounterReceipt.addToItems(counterReceiptItem)
        sampleCounterReceipt.save(flush: true)
		sampleAttachedSalesDelivery.takenByCounterReceipt()
		sampleAttachedSalesDelivery.save(flush: true)
        assertEquals "[GUARD] counter receipt deliveries must only contain one delivery", 1, sampleCounterReceipt.deliveries.size()
        assertTrue "[GUARD] Counter Receipt must contain a sales delivery", sampleCounterReceipt.deliveries.contains(sampleAttachedSalesDelivery)
        assertTrue "[GUARD] Counter Receipt must not contain a customer charge", sampleCounterReceipt.charges.isEmpty()
		assertTrue "[GUARD] SampleSalesDelivery must be taken by counter receipt", sampleAttachedSalesDelivery.isTakenByCounterReceipt

        def result = counterReceiptService.updateCounterReceipt(sampleCounterReceipt, [deliveries:[],charges:[]])

        assertTrue "Counter Receipt must not contain a sales delivery", sampleCounterReceipt.deliveries.isEmpty()
        assertTrue "Counter Receipt must have a return value of true", result
		assertFalse "SampleSalesDelivery should not be taken by counter receipt", sampleSalesDelivery.isTakenByCounterReceipt
    }
	
    void testUpdateCounterReceiptAddOneCustomerCharge(){
        assertTrue "[GUARD] Counter Receipt must not contain a sales delivery", sampleCounterReceipt.deliveries.isEmpty()
        assertTrue "[GUARD] Counter Receipt must not contain a customer charge", sampleCounterReceipt.charges.isEmpty()

        def result = counterReceiptService.updateCounterReceipt(sampleCounterReceipt, [deliveries:[],charges:[sampleAttachedCustomerCharge.id]])

        assertEquals "counter receipt charges must only contain one charge", 1, sampleCounterReceipt.charges.size()
        assertTrue "counter receipt must contain sampleCustomerCharge", sampleCounterReceipt.charges.contains(sampleAttachedCustomerCharge)
        assertTrue "Counter Receipt must have a return value of true", result
		assertTrue "SampleCustomerCharge should be taken be by counter receipt", sampleAttachedCustomerCharge.isTakenByCounterReceipt
    }
	
    void testUpdateCounterReceiptRemoveCharges(){
		def counterReceiptItem = new CounterReceiptItem(
			invoice: sampleAttachedCustomerCharge,
			counterReceipt: sampleCounterReceipt
		)
		counterReceiptItem.save(flush: true)
        sampleCounterReceipt.addToItems(counterReceiptItem)
        sampleCounterReceipt.save()
        assertEquals "[GUARD] counter receipt charges must only contain one charge", 1, sampleCounterReceipt.charges.size()
        assertTrue "[GUARD] Counter Receipt must contain sampleCustomerCharge", sampleCounterReceipt.charges.contains(sampleAttachedCustomerCharge)
        assertTrue "[GUARD] Counter Receipt must not contain a sales delivery", sampleCounterReceipt.deliveries.isEmpty()

        def result = counterReceiptService.updateCounterReceipt(sampleCounterReceipt, [deliveries:[],charges:[]])
		
        assertTrue "Counter Receipt must not contain a sales delivery", sampleCounterReceipt.charges.isEmpty()
        assertTrue "Counter Receipt must have a return value of true", result
    }
	
    void testUpdateCounterReceiptWithError(){
        sampleCounterReceipt.preparedBy = null
        sampleCounterReceipt.save()
        def result = counterReceiptService.updateCounterReceipt(sampleCounterReceipt, [deliveries:[],charges:[]])
        assertTrue "Counter Receipt must not contain a sales delivery", sampleCounterReceipt.charges.isEmpty()
        assertFalse "Counter Receipt must have a return value of true", result
    }
	
    void testSaveCounterReceiptAddOneSalesDelivery(){
        sampleCounterReceipt2 = new CounterReceipt(
            customer:sampleCustomer,
            preparedBy:"you",
            deliveries: [],
            charges: [])
		sampleCounterReceipt2.save(flush: true)
        assertTrue "Counter Receipt must not contain a sales delivery", sampleCounterReceipt2.deliveries.isEmpty()
        assertTrue "Counter Receipt must not contain a customer charge", sampleCounterReceipt2.charges.isEmpty()
		
        def result = counterReceiptService.saveCounterReceipt(sampleCounterReceipt2, [deliveries:[sampleAttachedSalesDelivery.id],charges:[]])
		
        assertTrue "The return value must be true", result
        assertEquals "Counter receipt domain now must contain 3 CR", 3, CounterReceipt.list().size()
        assertEquals "counter receipt deliveries must only contain one delivery", 1, sampleCounterReceipt2.deliveries.size()
        assertTrue "counter receipt must contain sample sales delivery", sampleCounterReceipt2.deliveries.contains(sampleAttachedSalesDelivery)
    }

    void testSaveCounterReceiptAddOneCustomerCharge(){
        sampleCounterReceipt2 = new CounterReceipt(
            customer:sampleCustomer,
            preparedBy:"you",
            deliveries: [],
            charges: [])
		sampleCounterReceipt2.save(flush: true)
        assertTrue "Counter Receipt must not contain a sales delivery", sampleCounterReceipt2.deliveries.isEmpty()
        assertTrue "Counter Receipt must not contain a customer charge", sampleCounterReceipt2.charges.isEmpty()

        def result = counterReceiptService.saveCounterReceipt(sampleCounterReceipt2, [deliveries:[],charges:[sampleAttachedCustomerCharge.id]])
        assertTrue "The return value must be true", result
        assertEquals "Counter receipt domain now must contain 3 CR", 3, CounterReceipt.list().size()
        assertEquals "counter receipt charges must only contain one charge", 1, sampleCounterReceipt2.charges.size()
        assertTrue "counter receipt must contain sampleCustomerCharge", sampleCounterReceipt2.charges.contains(sampleAttachedCustomerCharge)
    }
	
	void testSaveCounterReceipt() {
		def map = [deliveries: [sampleAttachedSalesDelivery.id], charges: [sampleAttachedCustomerCharge.id],
				   bouncedChecks: [sampleAttachedBouncedCheck.id], creditMemos: [sampleAttachedCreditMemo.id]]

		counterReceiptService.saveCounterReceipt(sampleCounterReceipt, map)
		
		assertTrue "The result should contain sampleSalesDelivery", sampleCounterReceipt.deliveries.contains(sampleAttachedSalesDelivery)
		assertEquals "The result should have 1 salesDelivery", 1, sampleCounterReceipt.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", sampleCounterReceipt.charges.contains(sampleAttachedCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, sampleCounterReceipt.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", sampleCounterReceipt.bouncedChecks.contains(sampleAttachedBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, sampleCounterReceipt.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", sampleCounterReceipt.creditMemos.contains(sampleAttachedCreditMemo)
		assertEquals "The result should have 1 creditMemo", 1, sampleCounterReceipt.creditMemos.size()
	}
	
    void testValidateAndApproveCounterReceiptCCExistsAndBalanceNotZero(){
        sampleCounterReceipt.addToItems(sampleCounterReceiptItemWithCustomerCharge)
		sampleAttachedCustomerCharge.metaClass.'static'.computeProjectedDue = {-> new BigDecimal("100")}
        sampleCounterReceipt.save()

        def response = counterReceiptService.validateAndApproveCounterReceipt(sampleCounterReceipt)
        assertTrue "The result must be true",response.result
        assertEquals "The message is not expected", "Counter Receipt has been successfully approved!", response.message
        assertTrue "The status of CR must be approved", sampleCounterReceipt.isApproved()
    }
		
    void testValidateAndApproveCounterReceiptSDExistsAndBalanceNotZero(){
        sampleCounterReceipt.addToItems(sampleCounterReceiptItemWithSalesDelivery)
        sampleCounterReceipt.save()

        def response = counterReceiptService.validateAndApproveCounterReceipt(sampleCounterReceipt)
        assertTrue "The result must be true",response.result
        assertEquals "The message is not expected", "Counter Receipt has been successfully approved!", response.message
        assertTrue "The status of CR must be approved", sampleCounterReceipt.isApproved()
    }
	
    void testValidateAndApproveCounterReceiptBCExistsAndBalanceNotZero(){
        sampleCounterReceipt.addToItems(sampleCounterReceiptItemWithBouncedCheck)
        sampleCounterReceipt.save()

        def response = counterReceiptService.validateAndApproveCounterReceipt(sampleCounterReceipt)
        assertTrue "The result must be true",response.result
        assertEquals "The message is not expected", "Counter Receipt has been successfully approved!", response.message
        assertTrue "The status of CR must be approved", sampleCounterReceipt.isApproved()
    }
	
    void testValidateAndApproveCounterReceiptCMExistsAndBalanceNotZero(){
        sampleCounterReceipt.addToItems(sampleCounterReceiptItemWithCreditMemo)
        sampleCounterReceipt.save()

        def response = counterReceiptService.validateAndApproveCounterReceipt(sampleCounterReceipt)
        assertTrue "The result must be true",response.result
        assertEquals "The message is not expected", "Counter Receipt has been successfully approved!", response.message
        assertTrue "The status of CR must be approved", sampleCounterReceipt.isApproved()
    }
	
    void testValidateAndApproveCounterReceiptNoInvoices(){
        def response = counterReceiptService.validateAndApproveCounterReceipt(sampleCounterReceipt)
        assertFalse "The result must be false",response.result
        assertEquals "The message is not expected", "Counter Receipt cannot be approved because no invoices has been selected!", response.message
        assertTrue "The status of CR must not be approved", sampleCounterReceipt.isUnapproved()
    }
	
    void testGetCollectionSchedulesForCounterReceiptCounterDateExist(){
        sampleCollectionSchedule.addToItems(sampleCollectionScheduleItem)
        sampleCollectionSchedule.save()
        sampleCounterReceipt.counterDate = new Date()
        sampleCounterReceipt.save()

        assertEquals "[Guard] collection sched must only have 1 item",1, sampleCollectionSchedule.items.size()
        assertTrue "[Guard] collection sched items must contain collectionScheduleItem", sampleCollectionSchedule.items.contains(sampleCollectionScheduleItem)

        assertNotNull "[Guard] counter receipt's counter date must not be empty",sampleCounterReceipt.counterDate
        assertNull "[Guard] counter receipt's collection date must be empty",sampleCounterReceipt.collectionDate
        def result = counterReceiptService.getCollectionSchedulesForCounterReceipt(sampleCounterReceipt)
        assertTrue "the result must be the sampleCollectionSchedule", result.collectionScheduleForCounter.contains(sampleCollectionSchedule)
		println result.collectionScheduleForCollection
        assertTrue "the result must be the null", result.collectionScheduleForCollection.isEmpty()
    }
	
    void testGetCollectionSchedulesForCounterReceiptCollectionDateExist(){
        sampleCollectionSchedule.addToItems(sampleCollectionScheduleItem2)
        sampleCollectionSchedule.save()

        sampleCounterReceipt.collectionDate = new Date()
        sampleCounterReceipt.save()

        assertEquals "[Guard] collection sched must only have 1 item",1, sampleCollectionSchedule.items.size()
        assertTrue "[Guard] collection sched items must contain collectionScheduleItem", sampleCollectionSchedule.items.contains(sampleCollectionScheduleItem2)

        assertNotNull "[Guard] counter receipt's counter date must not be empty",sampleCounterReceipt.collectionDate
        assertNull "[Guard] counter receipt's collection date must be empty",sampleCounterReceipt.counterDate
        def result = counterReceiptService.getCollectionSchedulesForCounterReceipt(sampleCounterReceipt)
        assertTrue "the result must be the sampleCollectionSchedule", result.collectionScheduleForCollection.contains(sampleCollectionSchedule)
		println result.collectionScheduleForCounter
        assertTrue "the result must be the null", result.collectionScheduleForCounter.isEmpty()
    }
	
    void testGetCollectionSchedulesForCounterReceiptBothDateExist(){
        sampleCollectionSchedule.addToItems(sampleCollectionScheduleItem3)
        sampleCollectionSchedule.save()

        sampleCounterReceipt.collectionDate = new Date()
        sampleCounterReceipt.counterDate = new Date()
        sampleCounterReceipt.save()

        assertEquals "[Guard] collection sched must only have 1 item",1, sampleCollectionSchedule.items.size()
        assertTrue "[Guard] collection sched items must contain collectionScheduleItem", sampleCollectionSchedule.items.contains(sampleCollectionScheduleItem3)
        assertNotNull "[Guard] counter receipt's collection date must not be empty",sampleCounterReceipt.collectionDate
        assertNotNull "[Guard] counter receipt's counter date must not be empty",sampleCounterReceipt.counterDate
		
        def result = counterReceiptService.getCollectionSchedulesForCounterReceipt(sampleCounterReceipt)
		
        assertTrue "the result must be the sampleCollectionSchedule", result.collectionScheduleForCollection.contains(sampleCollectionSchedule)
        assertTrue "the result must be the sampleCollectionSchedule", result.collectionScheduleForCounter.contains(sampleCollectionSchedule)
    }

	void testGenerateList(){
		def counterReceipt = new CounterReceipt(
			date : sdf.parse("09/01/2011"),
			status: CounterReceipt.Status.APPROVED,
			customer: sampleCustomer,
			preparedBy: "me",
			approvedBy: "you"
			)

		counterReceipt.save(flush:true)
		def params = [:]
		params.id = counterReceipt.id
		params.status = "Approved"
		
		def result = counterReceiptService.generateList(params)
		assertTrue "Result should contain counterReceipt", result.contains(counterReceipt)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testIsUnapprovable() {
        def dateString = generalMethodService.getDateString(new Date(), "MMM. dd, yyyy - hh:mm a")
        sampleCounterReceipt.approvedBy = "haha, " + dateString
        sampleCounterReceipt.save()
		def result = counterReceiptService.isUnapprovable(sampleCounterReceipt)

		assertEquals "counter receipt should be unapprovable", result, "Unapprovable"
	}
	
	void testIsUnapprovableWithCollection() {
		sampleCollectionSchedule.addToItems(sampleCollectionScheduleItem3)
		sampleCollectionSchedule.save()
		def dateString = generalMethodService.getDateString(new Date(), "MMM. dd, yyyy - hh:mm a")
        sampleCounterReceipt.approvedBy = "haha, " + dateString
        sampleCounterReceipt.save()
		def result = counterReceiptService.isUnapprovable(sampleCounterReceipt)
		
		assertEquals "counter receipt should be Collection Exist", result, "Collection Exist"
	}
    void testIsUnapprovableTwoDaysPassed() {
        def dateThreeDaysBefore = generalMethodService.performDayOperationsOnDate(Operation.SUBTRACT, generalMethodService.dateToday(), 3)
        def dateString = generalMethodService.getDateString(dateThreeDaysBefore, "MMM. dd, yyyy - hh:mm a")
        sampleCounterReceipt.approvedBy = "haha, " + dateString
        sampleCounterReceipt.save()

		def result = counterReceiptService.isUnapprovable(sampleCounterReceipt)

		assertEquals "counter receipt should be Time passed", result, "Time passed"
	}
	
	void testIsCancellable() {
		def result = counterReceiptService.isCancellable(sampleCounterReceipt)
		
		assertTrue "counter receipt should be cancellable", result
	}
	
	void testIsCancellableNotCancellable() {
		sampleCounterReceipt.approve()
        sampleCounterReceipt.save()
		
		def result = counterReceiptService.isCancellable(sampleCounterReceipt)
		
		assertFalse "counter receipt should not be cancellable", result
	}
}
