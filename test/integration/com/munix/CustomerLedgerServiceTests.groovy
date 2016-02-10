package com.munix

import grails.test.*

class CustomerLedgerServiceTests extends GrailsUnitTestCase {
	def customerLedgerService
    def generalMethodService

	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleCustomerLedger
	def sampleCustomerLedgerEntry1
	def sampleCustomerLedgerEntry2
	def sampleCustomerLedgerEntry3
	def sampleWarehouse
	def sampleSalesOrder
	def sampleSalesDelivery
    def samplePaymentTypeCash
    def samplePaymentTypeCheck
    def samplePaymentTypeCM
    def sampleDirectPayment
    def sampleDirectPaymentItemCash
	def sampleDirectPaymentItemCheck
	def sampleDirectPaymentItemCreditMemo
	def sampleOverpayment
	def sampleProduct
	def sampleSalesDeliveryItem
	def sampleCheckStatus
	def sampleBouncedCheck
	def sampleBank
	def sampleCheckType
	def sampleCheckWarehouse
	def sampleCheckPaymentForBouncedCheck
	def sampleCheckPaymentForCheckDeposit
	def sampleCustomerCharge
	def sampleCharge
	def sampleCustomerChargeItem
	def sampleDiscountType
	def sampleReason
	def sampleCreditMemo
	def sampleCreditMemo2
	def sampleDebitMemo
	def sampleCreditMemoItem
	def sampleDebitMemoItem
	def sampleBankAccount
	def sampleCheckDeposit
	def sampleTerm

	
    protected void setUp() {
        super.setUp()
		
		setUpCities()
		setupTerms()
		setUpCustomerTypes()
		setUpCustomers()
		
		setUpCustomerLedgers()
		setUpCustomerLedgerEntries()
		
		setUpWarehouses()
		
		setUpDiscountTypes()
		setUpProducts()
		setUpReasons()
		setUpSalesOrders()
		setUpSalesDeliveries()
		setUpSalesDeliveryItems()
		setUpSalesDeliveryAndSalesDeliveryItemRelationship()

		setUpCheckStatuses()
		setUpBouncedChecks()
		setUpBanks()
		setUpCheckTypes()
		setUpCheckWarehouses()
		setUpCheckPayments()
		setUpBouncedCheckAndCheckPaymentRelationship()
		
		setUpCustomerCharges()
		setUpCharges()
		setUpCustomerChargeItems()
		setUpCustomerChargeAndCustomerChargeItemRelationship()
		
		setUpCreditMemos()
		setUpCreditMemoItems()
		setUpCreditMemoAndCreditMemoItemRelationship()
		
		setUpBankAccounts()
		setUpCheckDeposits()
		setUpCheckDepositAndCheckPaymentRelationship()
		
		setUpPaymentTypes()
		setUpDirectPayments()
		setUpDirectPaymentItems()
		setUpOverpayments()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateApprovedSalesDelivery() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		customerLedgerService.createApprovedSalesDelivery(sampleSalesDelivery)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of 100", new BigDecimal("100"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of 100", new BigDecimal("100"), it.runningBalance 
		}
    }
	
	void testCreateUnapprovedSalesDelivery() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		customerLedgerService.createUnapprovedSalesDelivery(sampleSalesDelivery)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of -100", new BigDecimal("-100"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of -100", new BigDecimal("-100"), it.runningBalance
		}
	}
	
	void testCreateApprovedBouncedCheck() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		customerLedgerService.createApprovedBouncedCheck(sampleBouncedCheck)
		
		assertEquals "Sample Customer Ledger should have 2 entries", 2, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of 148", new BigDecimal("148"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			if (it.type == CustomerLedgerEntry.Type.APPROVED_BOUNCED_CHECK) {
				assertEquals "Sample Customer Ledger Entry should have a running balance of 148", new BigDecimal("148"), it.runningBalance
			} else {
			assertEquals "Sample Customer Ledger Entry Bounced Check item should have a running balance of 0", BigDecimal.ZERO, it.runningBalance
			}
		}
	}
    void testCreateUnapprovedBouncedCheck() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance

		customerLedgerService.createUnapprovedBouncedCheck(sampleBouncedCheck)

		assertEquals "Sample Customer Ledger should have 2 entries", 2, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of -148", new BigDecimal("-148"), sampleCustomerLedger.balance

		sampleCustomerLedger.entries.each {
			if (it.type == CustomerLedgerEntry.Type.UNAPPROVED_BOUNCED_CHECK) {
				assertEquals "Sample Customer Ledger Entry should have a running balance of -148", new BigDecimal("-148"), it.runningBalance
			} else {
			    assertEquals "Sample Customer Ledger Entry Bounced Check item should have a running balance of 0", BigDecimal.ZERO, it.runningBalance
			}
		}
	}
	
	void testCreateApprovedCheckDeposit() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		customerLedgerService.createApprovedCheckDeposit(sampleCheckDeposit)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of -148", new BigDecimal("-148"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of -148", new BigDecimal("-148"), it.runningBalance
		}
	}
	
	void testCreateApprovedCustomerCharge() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		customerLedgerService.createApprovedCustomerCharge(sampleCustomerCharge)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of 50", new BigDecimal("50"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of 50", new BigDecimal("50"), it.runningBalance
		}
	}
	
	void testCreateUnapprovedCustomerCharge() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		customerLedgerService.createUnapprovedCustomerCharge(sampleCustomerCharge)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of -50", new BigDecimal("-50"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of -50", new BigDecimal("-50"), it.runningBalance
		}
	}

	void testCreateApprovedDebitMemo() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		assertTrue "[GUARD] Sample Debit Memo should be a debit memo", sampleDebitMemo.isADebitMemo()
		
		customerLedgerService.createApprovedDebitMemo(sampleDebitMemo)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of 5.00", new BigDecimal("5.00"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of 5.00", new BigDecimal("5.00"), it.runningBalance
		}
	}
	
	void testCreateUnapprovedDebitMemo() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		assertTrue "[GUARD] Sample Debit Memo should be a debit memo", sampleDebitMemo.isADebitMemo()
		
		customerLedgerService.createUnapprovedDebitMemo(sampleDebitMemo)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of -5.00", new BigDecimal("-5.00"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of -5.00", new BigDecimal("-5.00"), it.runningBalance
		}
	}
	
	void testCreateApprovedCreditMemo() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		assertFalse "[GUARD] Sample Credit Memo should be a credit memo", sampleCreditMemo2.isADebitMemo()
		
		customerLedgerService.createApprovedCreditMemo(sampleCreditMemo2)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of 0", BigDecimal.ZERO, it.runningBalance
		}
	}
	
	void testCreateUnapprovedCreditMemo() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		assertFalse"[GUARD] Sample Credit Memo should be a credit memo", sampleCreditMemo2.isADebitMemo()
		
		customerLedgerService.createUnapprovedCreditMemo(sampleCreditMemo2)
		
		assertEquals "Sample Customer Ledger should have 1 entry", 1, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			assertEquals "Sample Customer Ledger Entry should have a running balance of 0", BigDecimal.ZERO, it.runningBalance
		}
	}
	
	void testApproveDirectPayment() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		assertNotNull "[GUARD] Sample Direct Payment should have an overpayment", sampleDirectPayment.overpayment
		
		customerLedgerService.approveDirectPayment(sampleDirectPayment)
		
		assertEquals "Sample Customer Ledger should have 5 entries", 5, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of -199.25", new BigDecimal("-199.25"), sampleCustomerLedger.balance
		
		sampleCustomerLedger.entries.each {
			if (it.type == CustomerLedgerEntry.Type.APPROVED_DIRECT_PAYMENT) {
				assertEquals "Sample Customer Ledger Entry should have a balance of 300", new BigDecimal("300"), it.amount
				assertNull "Sample Customer Ledger Entry Approved Direct Payment credit should be null", it.creditAmount
				assertNull "Sample Customer Ledger Entry Approved Direct Payment debit should be null", it.debitAmount
			} else if (it.type == CustomerLedgerEntry.Type.DIRECT_PAYMENT_ITEM) {
				if (it.details.startsWith(sampleDirectPaymentItemCheck.paymentType.toString())) {
					assertEquals "Sample Customer Ledger Entry should have a balance of 100", new BigDecimal("100"), it.amount
					assertNull "Sample Customer Ledger Entry Check credit should be null", it.creditAmount
					assertNull "Sample Customer Ledger Entry Check debit should be null", it.debitAmount
				} else {
					assertEquals "Sample Customer Ledger Entry should have a balance of 100", new BigDecimal("100"), it.amount
					assertEquals "Sample Customer Ledger Entry should have a credit of 100", new BigDecimal("100"), it.creditAmount
					assertNull "Sample Customer Ledger Entry debit should be null", it.debitAmount
				}
			} else if (it.type == CustomerLedgerEntry.Type.OVERPAYMENT) {
				assertEquals "Sample Customer Ledger Entry should have a balance of 0.75", new BigDecimal("0.75"), it.amount
				assertNull "Sample Customer Ledger Entry credit should be null", it.creditAmount
				assertEquals "Sample Customer Ledger Entry should have a debit of 0.75", new BigDecimal("0.75"), it.debitAmount
			}
		}
	}

    void testUnapproveDirectPayment() {
		assertNull "[GUARD] Sample Customer Ledger should not have any entries", sampleCustomerLedger.entries
		assertEquals "[GUARD] Sample Customer Ledger should have a balance of 0", BigDecimal.ZERO, sampleCustomerLedger.balance
		assertNotNull "[GUARD] Sample Direct Payment should have an overpayment", sampleDirectPayment.overpayment

		customerLedgerService.unapproveDirectPayment(sampleDirectPayment)

		assertEquals "Sample Customer Ledger should have 5 entry", 5, sampleCustomerLedger.entries.size()
		assertEquals "Sample Customer Ledger should have a balance of 199.25", new BigDecimal("199.25"), sampleCustomerLedger.balance

		sampleCustomerLedger.entries.each {
			if (it.type == CustomerLedgerEntry.Type.APPROVED_DIRECT_PAYMENT) {
				assertEquals "Sample Customer Ledger Entry should have a balance of 300", new BigDecimal("300"), it.amount
				assertNull "Sample Customer Ledger Entry Approved Direct Payment credit should be null", it.creditAmount
				assertNull "Sample Customer Ledger Entry Approved Direct Payment debit should be null", it.debitAmount
			} else if (it.type == CustomerLedgerEntry.Type.DIRECT_PAYMENT_ITEM) {
				if (it.details.startsWith(sampleDirectPaymentItemCheck.paymentType.toString())) {
					assertEquals "Sample Customer Ledger Entry should have a balance of 100", new BigDecimal("100"), it.amount
					assertNull "Sample Customer Ledger Entry Check credit should be null", it.creditAmount
					assertNull "Sample Customer Ledger Entry debit should be null", it.debitAmount
				} else {
					assertEquals "Sample Customer Ledger Entry should have a balance of 100", new BigDecimal("100"), it.amount
					assertNull "Sample Customer Ledger Entry credit should be null", it.creditAmount
					assertEquals "Sample Customer Ledger Entry should have a debit of 100", new BigDecimal("100"), it.debitAmount
					
				}
			} else if (it.type == CustomerLedgerEntry.Type.OVERPAYMENT) {
				assertEquals "Sample Customer Ledger Entry should have a balance of 0.75", new BigDecimal("0.75"), it.amount
				assertEquals "Sample Customer Ledger Entry should have a credit of 0.75", new BigDecimal("0.75"), it.creditAmount
				assertNull "Sample Customer Ledger Entry debit should be null", it.debitAmount
			}
		}
	}

    void testGetCustomerLedgerEntries(){
        def result = customerLedgerService.getCustomerLedgerEntries(CustomerLedger.get(sampleCustomerLedger.id),[offset:0,max:100])
        assertEquals "Results for the method must contain 3 items",3,result.entries.size()
        assertEquals "Results total must be 3 ",3,result.entriesTotal
        assertTrue "Results must contain sampleCustomerLedgerEntry1", result.entries.contains(sampleCustomerLedgerEntry1)
        assertTrue "Results must contain sampleCustomerLedgerEntry2", result.entries.contains(sampleCustomerLedgerEntry2)
        assertTrue "Results must contain sampleCustomerLedgerEntry3", result.entries.contains(sampleCustomerLedgerEntry3)
    }
	
    void testGetCustomerLedgerEntriesWithPostDateBefore(){
        def result = customerLedgerService.getCustomerLedgerEntries(CustomerLedger.get(sampleCustomerLedger.id),[offset:0,max:100, postDateBeforeText:"05/08/2000"])
        assertEquals "Results for the method must contain 2 items",2,result.entries.size()
        assertEquals "Results total must be 2 ",2,result.entriesTotal
        assertTrue "Results must contain sampleCustomerLedgerEntry2", result.entries.contains(sampleCustomerLedgerEntry2)
        assertTrue "Results must contain sampleCustomerLedgerEntry3", result.entries.contains(sampleCustomerLedgerEntry3)
    }
	
    void testGetCustomerLedgerEntriesWithPostDateAfter(){
        def result = customerLedgerService.getCustomerLedgerEntries(CustomerLedger.get(sampleCustomerLedger.id),[offset:0,max:100, postDateAfterText:"05/08/2000"])
        assertEquals "Results for the method must contain 2 items",2,result.entries.size()
        assertEquals "Results total must be 2 ",2,result.entriesTotal
        assertTrue "Results must contain sampleCustomerLedgerEntry1", result.entries.contains(sampleCustomerLedgerEntry1)
        assertTrue "Results must contain sampleCustomerLedgerEntry2", result.entries.contains(sampleCustomerLedgerEntry2)
    }
	
    void testGetCustomerLedgerEntriesWithPostDateAfterAndPostDateBefore(){
        def result = customerLedgerService.getCustomerLedgerEntries(CustomerLedger.get(sampleCustomerLedger.id),[offset:0,max:100, postDateAfterText:"05/08/2000", postDateBeforeText:"05/08/2000"])
        assertEquals "Results for the method must contain 1 item",1,result.entries.size()
        assertEquals "Results total must be 1 ",1,result.entriesTotal
        assertTrue "Results must contain sampleCustomerLedgerEntry2", result.entries.contains(sampleCustomerLedgerEntry2)
    }
	
    void testGetCustomerLedgerEntriesMaxResults(){
        def result = customerLedgerService.getCustomerLedgerEntries(CustomerLedger.get(sampleCustomerLedger.id),[offset:0,max:1, postDateAfterText:"05/08/2000"])
        assertEquals "Results for the method must contain 1 item",1,result.entries.size()
        assertEquals "Results total must be 2 ",2,result.entriesTotal
        assertTrue "Results must contain sampleCustomerLedgerEntry1", result.entries.contains(sampleCustomerLedgerEntry1)
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

	private void setUpCustomerLedgers() {
		sampleCustomerLedger = new CustomerLedger(
			customer: sampleCustomer
		)
		sampleCustomerLedger.save(flush: true)
	}
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
    private void setUpDirectPayments(){
		sampleDirectPayment = new DirectPayment(
			customer:sampleCustomer,
			preparedBy:"Rechilda Naval, Apr. 02, 2011 - 00:00 AM",
			approvedBy:"Rechilda Naval, Apr. 02, 2011 - 00:00 AM")
		sampleDirectPayment.save(flush:true)
    }
	
	private void setUpPaymentTypes(){
		samplePaymentTypeCash = new PaymentType(
			description: "cash",
			identifier: "cs",
			isCheck: false,
            deductibleFromSales: false
		).save(flush: true)

		samplePaymentTypeCheck = new PaymentType(
			description: "check",
			identifier: "chk",
			isCheck: true,
            deductibleFromSales: false
		).save(flush: true)

		samplePaymentTypeCM = new PaymentType(
			description: "Credit Memo",
			identifier: "CM",
			isCheck: false,
            deductibleFromSales: false
		).save(flush: true)
	}
	
	private void setUpDirectPaymentItems(){
		sampleDirectPaymentItemCheck = new DirectPaymentItemCheck(
			amount:new BigDecimal("100"),
			paymentType: samplePaymentTypeCheck,
			directPayment: sampleDirectPayment,
			checkPayment: sampleCheckPaymentForBouncedCheck
		).save(flush: true)

		sampleDirectPaymentItemCash = new DirectPaymentItem(
			amount:new BigDecimal("100"),
			paymentType: samplePaymentTypeCash,
			directPayment: sampleDirectPayment
		).save(flush: true)

		sampleDirectPaymentItemCreditMemo = new DirectPaymentItem(
			amount:new BigDecimal("100"),
			paymentType: samplePaymentTypeCM,
			directPayment: sampleDirectPayment
		).save(flush: true)

		sampleCreditMemo.directPaymentItem = sampleDirectPaymentItemCreditMemo
		sampleCreditMemo.save(flush: true)

		sampleDirectPayment.addToItems(sampleDirectPaymentItemCash)
		sampleDirectPayment.addToItems(sampleDirectPaymentItemCheck)
		sampleDirectPayment.addToItems(sampleDirectPaymentItemCreditMemo)
		sampleDirectPayment.save(flush: true)
    }
	
	private void setUpOverpayments() {
		sampleOverpayment = new Overpayment(
			status: Overpayment.Status.APPROVED,
			amount: new BigDecimal("0.75"),
			directPayment: sampleDirectPayment
		).save(flush: true)
		
		sampleDirectPayment.overpayment = sampleOverpayment
		sampleDirectPayment.save(flush: true)
	}
	
	private void setUpCustomerLedgerEntries() {
		sampleCustomerLedgerEntry1 = new CustomerLedgerEntry(
			dateOpened:generalMethodService.createDate("05/04/2000"),
			datePosted:generalMethodService.createDate("05/07/2000"),
			type:CustomerLedgerEntry.Type.BOUNCED_CHECK_ITEM,
			referenceId:"1",
			linkId:1L,
			amount:BigDecimal.ZERO,
			debitAmount:BigDecimal.ZERO,
			creditAmount:BigDecimal.ZERO,
			runningBalance:BigDecimal.ZERO,
			customerLedger:sampleCustomerLedger,
			isChild:false
		).save(flush: true)
		
		sampleCustomerLedgerEntry2 = new CustomerLedgerEntry(
			dateOpened:generalMethodService.createDate("05/05/2000"),
			datePosted:generalMethodService.createDate("05/08/2000"),
			type:CustomerLedgerEntry.Type.BOUNCED_CHECK_ITEM,
			referenceId:"1",
			linkId:1L,
			amount:BigDecimal.ZERO,
			debitAmount:BigDecimal.ZERO,
			creditAmount:BigDecimal.ZERO,
			runningBalance:BigDecimal.ZERO,
			customerLedger:sampleCustomerLedger,
			isChild:false
		).save(flush: true)
		
		sampleCustomerLedgerEntry3 = new CustomerLedgerEntry(
			dateOpened:generalMethodService.createDate("05/06/2000"),
			datePosted:generalMethodService.createDate("05/10/2000"),
			type:CustomerLedgerEntry.Type.BOUNCED_CHECK_ITEM,
			referenceId:"1",
			linkId:1L,
			amount:BigDecimal.ZERO,
			debitAmount:BigDecimal.ZERO,
			creditAmount:BigDecimal.ZERO,
			runningBalance:BigDecimal.ZERO,
			customerLedger:sampleCustomerLedger,
			isChild:false
		).save(flush: true)
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
				price:new BigDecimal("0"),
				finalPrice:new BigDecimal("0"))])
		sampleSalesOrder.save(flush: true)
	}

	private void setUpSalesDeliveries() {
		sampleSalesDelivery = new SalesDelivery(
			preparedBy: "me",
			approvedBy: "Rechilda Naval, Apr. 02, 2011 - 00:00 AM",
			customer: sampleCustomer,
			status: "Unpaid",
			termDay: BigDecimal.ZERO,
			warehouse: sampleWarehouse,
			deliveryType: "Deliver",
			invoice: sampleSalesOrder 
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
		
		sampleSalesDeliveryItem.delivery = sampleSalesDelivery
		sampleSalesDeliveryItem.save(flush: true)
	}
	
	private void setUpCheckStatuses() {
		sampleCheckStatus = new CheckStatus(
				identifier: "identifier",
				description: "description")
		sampleCheckStatus.save(flush: true)
	}
	
	private void setUpBouncedChecks() {
		sampleBouncedCheck = new BouncedCheck(
				date: generalMethodService.createDate("06/03/2011"),
				status: "Approved",
				customer: sampleCustomer,
				preparedBy: "me",
				bouncedCheckStatus: sampleCheckStatus,
				forRedeposit: false)
		sampleBouncedCheck.save(flush: true)
	}
	
	private void setUpBanks() {
		sampleBank = new Bank(identifier: "bank", description: "bank description")
		sampleBank.save(flush: true)
	}

	private void setUpCheckTypes() {
		sampleCheckType = new CheckType(
				routingNumber: "1234",
				description: "idontknow",
				branch: "branch")
		sampleCheckType.save(flush: true)
	}

	private void setUpCheckWarehouses() {
		sampleCheckWarehouse = new CheckWarehouse(
				identifier: "identifier",
				description: "description",
				isDefault: true)
		sampleCheckWarehouse.save(flush: true)
	}

	private void setUpCheckPayments() {
		sampleCheckPaymentForBouncedCheck = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1232",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentForBouncedCheck.save(flush: true)
		
		sampleCheckPaymentForCheckDeposit = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1232",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPaymentForCheckDeposit.save(flush: true)
	}
	
	private void setUpBouncedCheckAndCheckPaymentRelationship() {
		sampleBouncedCheck.checks = [sampleCheckPaymentForBouncedCheck]
		sampleBouncedCheck.save(flush: true)
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
	
	private void setUpCustomerChargeItems() {
		sampleCustomerChargeItem = new CustomerChargeItem(
				amount: new BigDecimal("50"),
				charge: sampleCharge)
		sampleCustomerChargeItem.save(flush: true)
	}
	
	private void setUpCustomerChargeAndCustomerChargeItemRelationship() {
		sampleCustomerCharge.items = [sampleCustomerChargeItem]
		sampleCustomerCharge.save(flush: true)
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
			reason: sampleReason,
			discount: BigDecimal.ZERO,
			status: "Approved",
			preparedBy: "me",
			date: new Date(),
			commissionRate: BigDecimal.ZERO
		)
		sampleCreditMemo.save(flush: true)
		
		sampleCreditMemo2 = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			reason: sampleReason,
			discount: BigDecimal.ZERO,
			status: "Approved",
			preparedBy: "me",
			date: new Date(),
			commissionRate: BigDecimal.ZERO
		)
		sampleCreditMemo2.save(flush: true)

		sampleDebitMemo = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			reason: sampleReason,
			discount: BigDecimal.ZERO,
			status: "Approved",
			preparedBy: "me",
			date: new Date(),
			commissionRate: BigDecimal.ZERO
		)
		sampleDebitMemo.save(flush: true)
	}

	private void setUpCreditMemoItems() {
		sampleCreditMemoItem = new CreditMemoItem(
			deliveryItem: sampleSalesDeliveryItem,
			oldQty: new BigDecimal("3"),
			oldPrice: new BigDecimal("2"),
			newQty: new BigDecimal("1"),
			newPrice: new BigDecimal("1"),
			creditMemo:sampleDebitMemo)
		sampleCreditMemoItem.save(flush: true)
	
		sampleDebitMemoItem = new CreditMemoItem(
				deliveryItem: sampleSalesDeliveryItem,
				oldQty: new BigDecimal("1"),
				oldPrice: new BigDecimal("1"),
				newQty: new BigDecimal("3"),
				newPrice: new BigDecimal("2"),
				creditMemo:sampleDebitMemo)
		sampleDebitMemoItem.save(flush: true)
	}

	private void setUpCreditMemoAndCreditMemoItemRelationship() {
		sampleDebitMemo.items = [sampleDebitMemoItem]
		sampleDebitMemo.save(flush: true)
		
		sampleCreditMemo2.items = [sampleCreditMemoItem]
		sampleCreditMemo2.save(flush: true)
	}
	
	private void setUpBankAccounts() {
		sampleBankAccount = new BankAccount(
			accountName: "account",
			accountNumber: "12345"
		)
		sampleBankAccount.save(flush: true)
	}
	
	private void setUpCheckDeposits() {
		sampleCheckDeposit = new CheckDeposit(
			account: sampleBankAccount,
			depositDate: new Date(),
			preparedBy: "me"
		)
		sampleCheckDeposit.save(flush: true)
	}
	
	private void setUpCheckDepositAndCheckPaymentRelationship() {
		sampleCheckDeposit.addToChecks(sampleCheckPaymentForCheckDeposit)
		sampleCheckDeposit.save(flush: true)
	}
}
