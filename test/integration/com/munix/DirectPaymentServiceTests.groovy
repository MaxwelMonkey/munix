package com.munix

import grails.test.*;
import java.text.SimpleDateFormat

class DirectPaymentServiceTests extends GrailsUnitTestCase {
	def directPaymentService
	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleCustomerAccount
	def sampleDirectPayment
	def samplePaymentType1
	def samplePaymentType2
	def samplePaymentType3
	def sampleBank
	def sampleCheckType
	def sampleCheckWarehouse
	def sampleCheckPayment
	def sampleCheckPaymentForBouncedCheck
	def sampleDirectPaymentItem1
	def sampleDirectPaymentItem2
	def sampleDirectPaymentItem3
	def sampleWarehouse
	def sampleProduct
	def sampleSalesOrder
	def sampleSalesDeliveryItem
	def sampleSalesDelivery
	def sampleSalesDeliveryWithAttachedInvoice
	def sampleDirectPaymentInvoiceBouncedCheck
	def sampleDirectPaymentInvoiceCreditMemo
	def sampleDirectPaymentInvoiceSalesDelivery
	def sampleDirectPaymentInvoiceCustomerCharge
	def sampleDiscountType
	def sampleReason
	def sampleCheckStatus
	def sampleCreditMemo
	def sampleCreditMemoWithAttachedInvoice
	def sampleDebitMemo
	def sampleTerm
	def sampleCreditMemoItem
	def sampleCreditMemoItemForSampleCreditMemoWithAttachedInvoice
	def sampleDebitMemoItem
	def sampleBouncedCheck
	def sampleBouncedCheckWithAttachedInvoice
	def sampleCustomerCharge
	def sampleCustomerChargeWithAttachedInvoice
	def sampleCharge
	def sampleCustomerChargeItem
	
	boolean transactional = true
	def sdf 

	protected void setUp() {
		super.setUp()
		sdf = new SimpleDateFormat("MM/dd/yyyy")

		setUpCities()
		setUpCustomerTypes()
		setupTerms()
		setUpCustomers()
		setUpPaymentTypes()
		setUpDirectPayments()

		setUpBanks()
		setUpCheckTypes()
		setUpCheckWarehouses()
		setUpCheckPayments()
		setUpDirectPaymentItems()
		setUpDirectPaymentAndDirectPaymentItemRelationship()

		setUpWarehouses()
		setUpDiscountTypes()
		setUpProducts()
		setUpReasons()
		
		setUpSalesOrders()
		setUpSalesDeliveries()
		setUpSalesDeliveryItems()
		setUpSalesDeliveryAndSalesDeliveryItemRelationship()

		setUpCreditMemos()
		setUpCreditMemoItems()
		setUpCreditMemoAndCreditMemoItemRelationship()

		setUpCustomerCharges()
		setUpCharges()
		setUpCustomerChargeItems()
		setUpCustomerChargeAndCustomerChargeItemRelationship()
		
		setUpCheckStatuses()
		setUpBouncedChecks()
		setUpBouncedCheckAndCheckPaymentRelationship()
		
		setUpDirectPaymentInvoices()
		setUpDirectPaymentAndDirectPaymentInvoiceRelationship()
		setUpCustomerPaymentAndDirectPaymentInvoiceRelationship()

        setupCustomerAccount()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testGenerateList() {
		def directPayment1 = new DirectPayment(
			date: sdf.parse("06/01/2010"),
			customer: sampleCustomer,
			preparedBy: "me1",
			approvedBy: "you",
			remark: "fail",
		)
		directPayment1.save(flush: true)
		directPayment1.metaClass.'static'.computeBalance = {-> return BigDecimal.ONE}
		setUpFilteredDirectPayments()

		def params = [:]
		params.searchIdentifier = directPayment1.id
		params.searchCustomerName = "customer"
		params.searchStatus = "Unapproved"
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
		params.searchBalance = "true"
												

		def result = directPaymentService.generateList(params)
		
		assertTrue "Result should contain directPayment1", result.directPayments.contains(directPayment1)
		assertEquals "Result total should be 1", 1, result.directPaymentsTotal
	}

	void testGenerateInvoices() {
		def mapOfCustomerPaymentLists = [creditMemoList:[sampleCreditMemo.id], bouncedCheckList:[sampleBouncedCheck.id], deliveryList:[sampleSalesDelivery.id], chargeList:[sampleCustomerCharge.id]]
		def numberOfdirectPaymentInvoices = DirectPaymentInvoice.list().size()
		assertNull "[GUARD] CreditMemo should not have a related invoice", CreditMemo.get(sampleCreditMemo.id).invoices
		assertNull "[GUARD] BouncedCheck should not have a related invoice", BouncedCheck.get(sampleBouncedCheck.id).invoices
		assertNull "[GUARD] SalesDelivery should not have a related invoice", SalesDelivery.get(sampleSalesDelivery.id).invoices
		assertNull "[GUARD] CustomerCharge should not have a related invoice", CustomerCharge.get(sampleCustomerCharge.id).invoices
		def directPaymentNumberOfInvoices = sampleDirectPayment.invoices.size()

		directPaymentService.generateInvoices(sampleDirectPayment, mapOfCustomerPaymentLists)

		assertEquals "CreditMemo should have a created related invoice", 1, CreditMemo.get(sampleCreditMemo.id).invoices.size()
		assertEquals "BouncedCheck should have a created related invoice", 1, BouncedCheck.get(sampleBouncedCheck.id).invoices.size()
		assertEquals "SalesDelivery should have a created related invoice", 1, SalesDelivery.get(sampleSalesDelivery.id).invoices.size()
		assertEquals "CustomerCharge should have a created related invoice", 1, CustomerCharge.get(sampleCustomerCharge.id).invoices.size()
		assertEquals "Should create 4 new Direct Payment Invoices", numberOfdirectPaymentInvoices + 4, DirectPaymentInvoice.list().size()
		assertEquals "DirectPayment should have 4 created related invoices", directPaymentNumberOfInvoices + 4, sampleDirectPayment.invoices.size()
		
		def startId = DirectPaymentInvoice.list()[0].id
		deleteCreatedInvoices(startId + 4, startId + 8)
		assertEquals "[GUARD] DirectPaymentInvoices should be reset to 4", 4, DirectPaymentInvoice.list().size()
	}
	
	void testGenerateInvoicesWithCreditMemoZeroAmount() {
		
		sampleCreditMemoItem.oldQty = new BigDecimal("1")
		sampleCreditMemoItem.oldPrice = new BigDecimal("1")
		sampleCreditMemoItem.newQty = new BigDecimal("1")
		sampleCreditMemoItem.newPrice = new BigDecimal("1")
		sampleCreditMemoItem.save()
		
		sampleCreditMemo.isTakenByDirectPayment = false
		sampleCreditMemo.save()
		
		assertFalse "[GUARD] Sample Credit Memo should not be taken by Direct Payment", sampleCreditMemo.isTakenByDirectPayment
		
		def mapOfCustomerPaymentLists = [creditMemoList:[sampleCreditMemo.id], bouncedCheckList:[], deliveryList:[], chargeList:[]]
		def directPaymentNumberOfItems = sampleDirectPayment.items.size()

		directPaymentService.generateInvoices(sampleDirectPayment, mapOfCustomerPaymentLists)
		
		assertEquals "Direct Payment should have added 1 item", directPaymentNumberOfItems + 1, sampleDirectPayment.items.size()
		assertTrue "Sample Credit Memo should be taken by Direct Payment", sampleCreditMemo.isTakenByDirectPayment
	}
	
	void testUpdateDirectPaymentRemoveItemNotCheck() {
		def itemId = [sampleDirectPaymentItem1.id]
        def createdItemId = []
		def object = [counter: "0"]
		object["textAmountUpdate${sampleDirectPaymentItem1.id}"] = "1200"
		object["paymentTypeUpdate${sampleDirectPaymentItem1.id}"] = samplePaymentType1.id
		object["dateUpdate${sampleDirectPaymentItem1.id}"] = "12/04/2010"
		object["textCheckDateUpdate${sampleDirectPaymentItem1.id}"] = "12/04/2010"
		object["textCheckNumUpdate${sampleDirectPaymentItem1.id}"] = "10101"
		object["bankTypeUpdate${sampleDirectPaymentItem1.id}"] = sampleBank.id
		object["checkTypeUpdate${sampleDirectPaymentItem1.id}"] = sampleCheckType.id
		object["textBranchUpdate${sampleDirectPaymentItem1.id}"] = "makati"
		object["remarkUpdate${sampleDirectPaymentItem1.id}"] = "check remark"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		assertEquals "Direct Payment should only contain 1 item", [sampleDirectPaymentItem1.id], sampleDirectPayment.items.id
	}

	void testUpdateDirectPaymentItemRemoveCheck() {
		def itemId = [sampleDirectPaymentItem2.id]
		def object = [counter: "0"]
        def createdItemId = []
		object["textAmountUpdate${sampleDirectPaymentItem2.id}"] = "1001"
		object["paymentTypeUpdate${sampleDirectPaymentItem2.id}"] = samplePaymentType2.id
		object["dateUpdate${sampleDirectPaymentItem2.id}"] = "12/04/2010"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		assertEquals "Direct Payment should contain only 1 item", [sampleDirectPaymentItem2.id], sampleDirectPayment.items.id
	}

	void testUpdateDirectPaymentRemoveAll() {
		def itemId = []
        def createdItemId = []
		def object = [counter: "0"]
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId, createdItemId)

		assertTrue "Direct Payment is empty", sampleDirectPayment.items.isEmpty()
	}

	void testUpdateDirectPaymentAddItemNotCheck() {
		def itemId = []
        def createdItemId = [1]
		def object = [counter: "1"]
		object["textAmount1"] = "1034"
		object["paymentType1"] = samplePaymentType2.id
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		def actualSampleDirectPayment = DirectPayment.get(sampleDirectPayment.id).refresh()
		def addedItemIndex = actualSampleDirectPayment.items.size()-1
		def actualSampleDirectPaymentItem = actualSampleDirectPayment.items.sort{it.date}.toArray()[addedItemIndex]

		assertFalse "Direct Payment is not empty", actualSampleDirectPayment.items.isEmpty()
		assertEquals "Direct payment item amount should be 1034", new BigDecimal(1034), actualSampleDirectPaymentItem.amount
		assertEquals "Direct payment item type should be samplePaymentType2", samplePaymentType2, actualSampleDirectPaymentItem.paymentType
	}

	void testUpdateDirectPaymentAddItemCheck() {
		def itemId = []
        def createdItemId = [1]
		def object = [counter: "1"]
		object["textAmount1"] = "1201"
		object["paymentType1"] = samplePaymentType1.id
		object["textCheckDate1"] = "12/10/2010"
		object["textCheckNum1"] = "123326"
		object["bankType1"] = sampleBank.id
		object["checkType1"] = sampleCheckType.id
		object["textBranch1"] = "buranchu"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		def actualSampleDirectPayment = DirectPayment.get(sampleDirectPayment.id).refresh()
		def addedItemIndex = actualSampleDirectPayment.items.size()-1
		def actualSampleDirectPaymentItem = actualSampleDirectPayment.items.sort{it.date}.toArray()[addedItemIndex]

		assertFalse "Direct Payment is not empty",actualSampleDirectPayment.items.isEmpty()
		assertEquals "Direct payment item amount should be 1201", new BigDecimal(1201), actualSampleDirectPaymentItem.amount
		assertEquals "Direct payment item type should be samplePaymentType1", samplePaymentType1, actualSampleDirectPaymentItem.paymentType
		assertEquals "Direct payment item check date should be 12/10/2010", sdf.parse("12/10/2010"), actualSampleDirectPaymentItem.checkPayment.date
		assertEquals "Direct payment item check number should be 123326", "123326", actualSampleDirectPaymentItem.checkPayment.checkNumber
		assertEquals "Direct payment item check bank should be sample bank", sampleBank, actualSampleDirectPaymentItem.checkPayment.bank
		assertEquals "Direct payment item check type should be sample check type", sampleCheckType, actualSampleDirectPaymentItem.checkPayment.type
		assertEquals "Direct payment item check branch should be buranchu", "buranchu", actualSampleDirectPaymentItem.checkPayment.branch
	}

	void testUpdateDirectPaymentUpdateItemNotCheck(){
		def itemId = [sampleDirectPaymentItem2.id]
        def createdItemId = []
		def amount = ""
		def object = [counter:"0"]
		object["textAmountUpdate${sampleDirectPaymentItem2.id}"] = "1001"
		object["paymentTypeUpdate${sampleDirectPaymentItem2.id}"] = samplePaymentType2.id
		object["dateUpdate${sampleDirectPaymentItem2.id}"] = "12/10/2010"
		object["remarkUpdate${sampleDirectPaymentItem2.id}"] = "cash remark"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		assertEquals "Direct payment item amount should be 1001", new BigDecimal(1001), sampleDirectPaymentItem2.amount
		assertEquals "Direct payment item type should be samplePaymentType2", samplePaymentType2, sampleDirectPaymentItem2.paymentType
		assertEquals "Direct payment item date should be 12/10/2010", sdf.parse("12/10/2010"), sampleDirectPaymentItem2.date
		assertEquals "Direct payment item remark should be cash remark", "cash remark", sampleDirectPaymentItem2.remark
	}

	void testUpdateDirectPaymentUpdateItemDebitMemo(){
		def itemId = [sampleDirectPaymentItem3.id]
		def object = [counter: "0"]
        def createdItemId = []
		object["dateUpdate${sampleDirectPaymentItem3.id}"] = "12/10/2010"
		object["remarkUpdate${sampleDirectPaymentItem3.id}"] = "cm remark"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		assertEquals "Direct payment item date should be 12/10/2010", sdf.parse("12/10/2010"), sampleDirectPaymentItem3.date
		assertEquals "Direct payment item remark should be cm remark", "cm remark", sampleDirectPaymentItem3.remark
	}

	void testUpdateDirectPaymentUpdateItemCheck() {
		def itemId = [sampleDirectPaymentItem1.id]
		def branch = ""
        def createdItemId = []
		def object = [counter: "0"]
		object["textAmountUpdate${sampleDirectPaymentItem1.id}"] = "1200"
		object["paymentTypeUpdate${sampleDirectPaymentItem1.id}"] = samplePaymentType1.id
		object["dateUpdate${sampleDirectPaymentItem1.id}"] = "12/10/2010"
		object["textCheckDateUpdate${sampleDirectPaymentItem1.id}"] = "12/04/2010"
		object["textCheckNumUpdate${sampleDirectPaymentItem1.id}"] = "10101"
		object["bankTypeUpdate${sampleDirectPaymentItem1.id}"] = sampleBank.id
		object["checkTypeUpdate${sampleDirectPaymentItem1.id}"] = sampleCheckType.id
		object["textBranchUpdate${sampleDirectPaymentItem1.id}"] = "makati"
		object["remarkUpdate${sampleDirectPaymentItem1.id}"] = "check remark"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		assertEquals "Direct payment item type should be check", samplePaymentType1, sampleDirectPaymentItem1.paymentType
		assertEquals "Direct payment item amount should be 1200", new BigDecimal(1200), sampleDirectPaymentItem1.amount
		assertEquals "Direct payment item date should be 12/10/2010", sdf.parse("12/10/2010"), sampleDirectPaymentItem1.date
		assertEquals "Direct payment item date should be check remark", "check remark", sampleDirectPaymentItem1.remark
		assertEquals "Direct payment item check date should be 12/04/2010", sdf.parse("12/04/2010"), sampleDirectPaymentItem1.checkPayment.date
		assertEquals "Direct payment item check number should be 10101", "10101", sampleDirectPaymentItem1.checkPayment.checkNumber
		assertEquals "Direct payment item check bank should be sample bank", sampleBank, sampleDirectPaymentItem1.checkPayment.bank
		assertEquals "Direct payment item check type should be sample check type", sampleCheckType, sampleDirectPaymentItem1.checkPayment.type
		assertEquals "Direct payment item check branch should be makati", "makati", sampleDirectPaymentItem1.checkPayment.branch
	}

	void testUpdateDirectPaymentUpdateInvoiceBouncedCheck() {
		def itemId = []
        def createdItemId = []
		def object = [counter: "0"]
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		def amount = DirectPaymentInvoice.get(sampleDirectPaymentInvoiceBouncedCheck.id).amount
		assertEquals "Invoice amount should be 10", new BigDecimal("10"), amount
	}

	void testUpdateDirectPaymentUpdateInvoiceCreditMemo() {
		def itemId = []
		def object = [counter: "0"]
        def createdItemId = []
		object["textApplied${sampleDirectPaymentInvoiceCreditMemo.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		def amount = DirectPaymentInvoice.get(sampleDirectPaymentInvoiceCreditMemo.id).amount
		assertEquals "Invoice amount should still be 12", new BigDecimal("12"), amount
	}

	void testUpdateDirectPaymentUpdateInvoiceSalesDelivery() {
		def itemId = []
        def createdItemId = []
		def object = [counter: "0"]
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		def amount = DirectPaymentInvoice.get(sampleDirectPaymentInvoiceSalesDelivery.id).amount
		assertEquals "Invoice amount should still be 10", new BigDecimal("10"), amount
	}

	void testUpdateDirectPaymentUpdateInvoiceCustomerCharge() {
		def itemId = []
        def createdItemId = []
		def object = [counter: "0"]
		object["textApplied${sampleDirectPaymentInvoiceCustomerCharge.id}"] = "10"
		object["textApplied${sampleDirectPaymentInvoiceSalesDelivery.id}"] = "12"
		object["textApplied${sampleDirectPaymentInvoiceBouncedCheck.id}"] = "12"

		directPaymentService.updateDirectPayment(sampleDirectPayment, object, itemId,createdItemId)

		def amount = DirectPaymentInvoice.get(sampleDirectPaymentInvoiceCustomerCharge.id).amount
		assertEquals "Invoice amount should still be 10", new BigDecimal("10"), amount
	}

	void testUpdateDirectPaymentInvoiceAddAllInvoices(){
		assertNull "[GUARD] CreditMemo should not have a related invoice", CreditMemo.get(sampleCreditMemo.id).invoices
		assertNull "[GUARD] BouncedCheck should not have a related invoice", BouncedCheck.get(sampleBouncedCheck.id).invoices
		assertNull "[GUARD] SalesDelivery should not have a related invoice", SalesDelivery.get(sampleSalesDelivery.id).invoices
		assertNull "[GUARD] CustomerCharge should not have a related invoice", CustomerCharge.get(sampleCustomerCharge.id).invoices
		def directPaymentNumberOfInvoices = sampleDirectPayment.invoices.size()
		def mapOfCustomerPaymentLists = [
				creditMemoList:[sampleCreditMemo.id, sampleCreditMemoWithAttachedInvoice.id, sampleDebitMemo.id], 
				bouncedCheckList:[sampleBouncedCheck.id, sampleBouncedCheckWithAttachedInvoice.id], 
				deliveryList:[sampleSalesDelivery.id, sampleSalesDeliveryWithAttachedInvoice.id], 
				chargeList:[sampleCustomerCharge.id, sampleCustomerChargeWithAttachedInvoice.id]
			]

		directPaymentService.updateDirectPaymentInvoice(sampleDirectPayment, mapOfCustomerPaymentLists)

		assertEquals "CreditMemo should have a created related invoice", 1, CreditMemo.get(sampleCreditMemo.id).invoices.size()
		assertEquals "BouncedCheck should have a created related invoice", 1, BouncedCheck.get(sampleBouncedCheck.id).invoices.size()
		assertEquals "SalesDelivery should have a created related invoice", 1, SalesDelivery.get(sampleSalesDelivery.id).invoices.size()
		assertEquals "CustomerCharge should have a created related invoice", 1, CustomerCharge.get(sampleCustomerCharge.id).invoices.size()
		assertEquals "DirectPayment should have 4 created related invoices", directPaymentNumberOfInvoices + 4, sampleDirectPayment.invoices.size()
		
		def startId = DirectPaymentInvoice.list()[0].id
		deleteCreatedInvoices(startId + 4, startId + 8)
		assertEquals "[GUARD] DirectPaymentInvoices should be reset to 4", 4, DirectPaymentInvoice.list().size()
	}

	void testUpdateDirectPaymentInvoiceRemoveAllInvoices(){
		assertNull "[GUARD] CreditMemo should not have a related invoice", CreditMemo.get(sampleCreditMemo.id).invoices
		assertNull "[GUARD] BouncedCheck should have a related invoice", BouncedCheck.get(sampleBouncedCheck.id).invoices
		assertNull "[GUARD] SalesDelivery should not have a related invoice", SalesDelivery.get(sampleSalesDelivery.id).invoices
		assertNull "[GUARD] CustomerCharge should not have a related invoice", CustomerCharge.get(sampleCustomerCharge.id).invoices
		assertEquals "[GUARD] DirectPayment should have a related invoice", 4, sampleDirectPayment.invoices.size()
		def mapOfCustomerPaymentLists = [creditMemoList:[], bouncedCheckList:[], deliveryList:[], chargeList:[]]

		directPaymentService.updateDirectPaymentInvoice(sampleDirectPayment, mapOfCustomerPaymentLists)

		assertNull "CreditMemo should be empty",CreditMemo.get(sampleCreditMemo.id).invoices
		assertNull "BouncedCheck should be empty", BouncedCheck.get(sampleBouncedCheck.id).invoices
		assertNull "SalesDelivery should be empty", SalesDelivery.get(sampleSalesDelivery.id).invoices
		assertNull "CustomerCharge should be empty", CustomerCharge.get(sampleCustomerCharge.id).invoices
		assertEquals "DirectPayment invoices should be zero", 0, sampleDirectPayment.invoices.size()
	}

	void testGetAvailableCustomerPaymentsForDirectPayment() {
		def result = directPaymentService.getAvailableCustomerPaymentsForDirectPayment(sampleCustomer)
		
		assertTrue "The result should contain sampleSalesDelivery", result.deliveries.contains(sampleSalesDelivery)
		assertEquals "The result should have 1 salesDelivery", 1, result.deliveries.size()
		assertTrue "The result should contain sampleCustomerCharge", result.charges.contains(sampleCustomerCharge)
		assertEquals "The result should have 1 customerCharge", 1, result.charges.size()
		assertTrue "The result should contain sampleBouncedCheck", result.bouncedChecks.contains(sampleBouncedCheck)
		assertEquals "The result should have 1 bouncedCheck", 1, result.bouncedChecks.size()
		assertTrue "The result should contain sampleCreditMemo", result.creditMemos.contains(sampleCreditMemo)
		assertEquals "The result should have 1 creditMemos", 1, result.creditMemos.size()
	}
	
	void testGetCustomerPaymentsForDirectPayment() {
		assertNull "[GUARD] CreditMemo should not have a related invoice", CreditMemo.get(sampleCreditMemo.id).invoices
		assertNull "[GUARD] BouncedCheck should not have a related invoice", BouncedCheck.get(sampleBouncedCheck.id).invoices
		assertNull "[GUARD] SalesDelivery should not have a related invoice", SalesDelivery.get(sampleSalesDelivery.id).invoices
		assertNull "[GUARD] CustomerCharge should not have a related invoice", CustomerCharge.get(sampleCustomerCharge.id).invoices

		def result = directPaymentService.getCustomerPaymentsForDirectPayment(sampleDirectPayment)

		assertEquals "The result should contain 2 salesDeliveries", 2, result.deliveries.size()
		assertEquals "The result should contain 2 customerCharges", 2, result.charges.size()
		assertEquals "The result should contain 2 bouncedChecks", 2, result.bouncedChecks.size()
		assertEquals "The result should contain 3 creditMemos", 3, result.creditMemos.size()
	}

	void testAccumulateTotals(){
		def invoices = []
		def invoice = [:]
		invoice.amount = new BigDecimal("100")
		invoice.due = new BigDecimal("100")
		invoice.applied = new BigDecimal("100")
		invoice.net = new BigDecimal("100")
		invoices.add(invoice)
		def invoice2 = [:]
		invoice2.amount = new BigDecimal("200")
		invoice2.due = new BigDecimal("200")
		invoice2.applied = new BigDecimal("200")
		invoice2.net = new BigDecimal("200")
		invoices.add(invoice2)

		def result = directPaymentService.accumulateTotals(invoices)
		assertEquals 300, result.amount
		assertEquals 300, result.due
		assertEquals 300, result.applied
		assertEquals 300, result.net
	}

	void testRetrieveDetailsOfInvoices(){
		assertEquals "[GUARD] DirectPayment should have 4 related invoices", 4, sampleDirectPayment.invoices.size()
		
		def results = directPaymentService.retrieveDetailsOfInvoices(sampleDirectPayment)
		
		def ids = [sampleDirectPaymentInvoiceBouncedCheck.id, sampleDirectPaymentInvoiceCustomerCharge.id, 
				   sampleDirectPaymentInvoiceCreditMemo.id, sampleDirectPaymentInvoiceSalesDelivery.id]
		def customerPayments = [sampleDirectPaymentInvoiceBouncedCheck.getRelatedCustomerPayment(), sampleDirectPaymentInvoiceCustomerCharge.getRelatedCustomerPayment(), 
						sampleDirectPaymentInvoiceCreditMemo.getRelatedCustomerPayment(), sampleDirectPaymentInvoiceSalesDelivery.getRelatedCustomerPayment()]
		def dates = [sdf.parse("06/03/2011"), sdf.parse("06/03/2011"), sdf.parse("06/03/2011"), sdf.parse("06/03/2011")]
		def types = [CustomerPaymentType.BOUNCED_CHECK, CustomerPaymentType.CUSTOMER_CHARGE,
					 CustomerPaymentType.CREDIT_MEMO, CustomerPaymentType.SALES_DELIVERY]
		def amounts = [148, 50, 12, 100]
        def dues = [148, 50, 12, 100]
		def nets = [0, 38, 0, 88]
		def applieds = [148, 12, 12, 12]
		def isInstallments = [true, true, false, true]
		
		for (int i = 0; i < 4; i++) {
			assertDetailsOfInvoices(results[i], ids[i], customerPayments[i], dates[i], types[i], amounts[i], nets[i], applieds[i], dues[i], isInstallments[i])
		}
	}
	
	void testRetrieveDetailsOfItems() {
		sampleDirectPayment.addToItems(sampleDirectPaymentItem3)
		sampleDirectPayment.save(flush: true)
		assertEquals "[GUARD] Direct Payment should have 3 items", 3, sampleDirectPayment.items.size()
		
		def results = directPaymentService.retrieveDetailsOfItems(sampleDirectPayment)
		
		def paymentTypes = [samplePaymentType1, samplePaymentType2, samplePaymentType3]
		def dates = [sdf.parse("06/03/2011"), sdf.parse("06/03/2011"), sdf.parse("06/03/2011")]
		def checkNumbers = ["1234", null, null]
		def checkDates = [sdf.parse("12/10/2001"), null, null]
		def checkBanks = ["bank - university", null, null]
		def checkTypes = ["1234 - idontknow", null, null]
		def debitMemos = [null, null, sampleDebitMemo]
		def remarks = ["remarkable", null, null]
		def amounts = [1234, 5678, 90]
		
		for (int i = 0; i < sampleDirectPayment.items.size(); i++) {
			assertDetailsOfItems(results[i], paymentTypes[i], dates[i], checkNumbers[i], checkDates[i], checkBanks[i], checkTypes[i], debitMemos[i], remarks[i], amounts[i])
		}
	}

    void testIsApprovableAppliedMoreThanAmount(){
        sampleDirectPaymentInvoiceSalesDelivery.amount = new BigDecimal("2000")
        sampleDirectPayment.save(flush:true)
        def results = directPaymentService.isApprovable(sampleDirectPayment)
        assertEquals "The result must be applied more than amount","Applied more than amount", results
    }
    void testIsApprovable(){
        sampleDirectPayment.removeFromInvoices(sampleDirectPaymentInvoiceCreditMemo)
        sampleCreditMemoWithAttachedInvoice.removeFromInvoices(sampleDirectPaymentInvoiceCreditMemo)
        sampleDirectPaymentInvoiceCreditMemo.delete()
        sampleDirectPayment.removeFromInvoices(sampleDirectPaymentInvoiceSalesDelivery)
        sampleSalesDeliveryWithAttachedInvoice.removeFromInvoices(sampleDirectPaymentInvoiceSalesDelivery)
        sampleDirectPaymentInvoiceSalesDelivery.delete()
        sampleDirectPayment.removeFromInvoices(sampleDirectPaymentInvoiceCustomerCharge)
        sampleCustomerChargeWithAttachedInvoice.removeFromInvoices(sampleDirectPaymentInvoiceCustomerCharge)
        sampleDirectPaymentInvoiceCustomerCharge.delete()
        sampleDirectPaymentItem1.amount = new BigDecimal("194")
        sampleDirectPaymentItem2.amount = BigDecimal.ZERO
        sampleDirectPayment.save(flush:true)
        assertEquals "the number of invoices must be 1", 1,sampleDirectPayment.invoices.size()
        def results = directPaymentService.isApprovable(sampleDirectPayment)
        assertEquals "The result must be approvable","Approvable", results
    }
    void testIsApprovablePaymentLessThanApplied(){
        sampleDirectPayment.removeFromInvoices(sampleDirectPaymentInvoiceCreditMemo)
        sampleCreditMemoWithAttachedInvoice.removeFromInvoices(sampleDirectPaymentInvoiceCreditMemo)
        sampleDirectPaymentInvoiceCreditMemo.delete()
        sampleDirectPayment.removeFromInvoices(sampleDirectPaymentInvoiceSalesDelivery)
        sampleSalesDeliveryWithAttachedInvoice.removeFromInvoices(sampleDirectPaymentInvoiceSalesDelivery)
        sampleDirectPaymentInvoiceSalesDelivery.delete()
        sampleDirectPayment.removeFromInvoices(sampleDirectPaymentInvoiceCustomerCharge)
        sampleCustomerChargeWithAttachedInvoice.removeFromInvoices(sampleDirectPaymentInvoiceCustomerCharge)
        sampleDirectPaymentInvoiceCustomerCharge.delete()
        sampleDirectPaymentItem1.amount = new BigDecimal("100")
        sampleDirectPaymentItem2.amount = BigDecimal.ZERO
        sampleDirectPayment.save(flush:true)
        assertEquals "the number of invoices must be 1", 1,sampleDirectPayment.invoices.size()
        def results = directPaymentService.isApprovable(sampleDirectPayment)
        assertEquals "The result must be Payment less than applied","Payment less than applied", results
    }

	private void assertDetailsOfInvoices(detail, id, customerPayment, date, type, amount, net, applied, due, isInstallment) {
		assertEquals type.toString() + " - Id should be " + id, id, detail.id
		assertEquals type.toString() + " - Customer Payment should be " + customerPayment, customerPayment, detail.customerPayment
		assertEquals type.toString() + " - Date should be " + date, date, detail.date
		assertEquals type.toString() + " - Type should be " + type, type, detail.type
		assertEquals type.toString() + " - Amount should be " + amount, amount, detail.amount
		assertEquals type.toString() + " - Due should be " + due, due, detail.due
		assertEquals type.toString() + " - Applied should be " + applied, applied, detail.applied
		assertEquals type.toString() + " - IsInstallment should be " + isInstallment, isInstallment, detail.isInstallment
	}
	
	private void assertDetailsOfItems(detail, paymentType, date, checkNumber, checkDate, checkBank, checkType, debitMemo, remark, amount) {
		assertEquals paymentType.toString() + " - Payment Type should be " + paymentType, paymentType, detail.paymentType
		assertEquals paymentType.toString() + " - Date should be " + date, date, detail.date
		assertEquals paymentType.toString() + " - Check Number should be " + checkNumber, checkNumber, detail.checkNumber
		assertEquals paymentType.toString() + " - Check Date should be " + checkDate, checkDate, detail.checkDate
		assertEquals paymentType.toString() + " - Check Bank should be " + checkBank, checkBank, detail.checkBank
		assertEquals paymentType.toString() + " - Check Type should be " + checkType, checkType, detail.checkType
		assertEquals paymentType.toString() + " - Debit Memo should be " + debitMemo, debitMemo, detail.debitMemo
		assertEquals paymentType.toString() + " - Remark should be " + remark, remark, detail.remark
		assertEquals paymentType.toString() + " - Amount should be " + amount, amount, detail.amount
	}
	
	private deleteCreatedInvoices(long startId, long endId) {
		for (long i = startId; i < endId; i++) {
			def invoice = DirectPaymentInvoice.get(i)
			sampleDirectPayment.removeFromInvoices(invoice)
			switch (invoice.type) {
				case (CustomerPaymentType.CREDIT_MEMO) :
					sampleCreditMemo.removeFromInvoices(invoice)
					break
				case (CustomerPaymentType.SALES_DELIVERY) :
					sampleSalesDelivery.removeFromInvoices(invoice)
					break
				case (CustomerPaymentType.CUSTOMER_CHARGE) :
					sampleCustomerCharge.removeFromInvoices(invoice)
					break
				case (CustomerPaymentType.BOUNCED_CHECK) :
					sampleBouncedCheck.removeFromInvoices(invoice)
					break
			}
			invoice.delete()
		}
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

	private void setUpDirectPayments() {
		sampleDirectPayment = new DirectPayment(
				customer: sampleCustomer,
				preparedBy: "me",
				approvedBy: "you",
				remark: "fail",
				)
		sampleDirectPayment.save(flush: true)
	}

	private void setUpPaymentTypes() {
		samplePaymentType1 = new PaymentType(
				identifier:"CHK",
				description:"check",
				isCheck:true,
                deductibleFromSales: false)
		samplePaymentType1.save(flush: true)

		samplePaymentType2 = new PaymentType(
				identifier:"CS",
				description:"cash",
				isCheck:false,
                deductibleFromSales: false)
		samplePaymentType2.save(flush: true)

		samplePaymentType3 = new PaymentType(
				identifier:"CM",
				description:"Credit Memo",
				isCheck:false,
                deductibleFromSales: false)
		samplePaymentType3.save(flush: true)
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
		sampleCheckPayment = new CheckPayment(
				amount: new BigDecimal("123"),
				checkNumber: "1234",
				bank : sampleBank,
				type : sampleCheckType,
				branch : "university",
				date : sdf.parse("12/10/2001"),
				customer : sampleCustomer,
				warehouse : sampleCheckWarehouse)
		sampleCheckPayment.save(flush: true)
		
		sampleCheckPaymentForBouncedCheck = new CheckPayment(
			amount: new BigDecimal("148"),
			checkNumber: "1232",
			bank : sampleBank,
			type : sampleCheckType,
			branch : "university",
			date : new Date(),
			customer : sampleCustomer,
			warehouse : sampleCheckWarehouse)
		sampleCheckPayment.save(flush: true)
	}

	private void setUpDirectPaymentItems() {
		sampleDirectPaymentItem1 = new DirectPaymentItemCheck(
				directPayment: sampleDirectPayment,
				date: sdf.parse("06/03/2011"),
				paymentType: samplePaymentType1,
				amount: new BigDecimal("1234"),
				remark: "remarkable",
				checkPayment: sampleCheckPayment)
		sampleDirectPaymentItem1.save(flush: true)

		sampleDirectPaymentItem2 = new DirectPaymentItem(
				directPayment: sampleDirectPayment,
				date: sdf.parse("06/03/2011"),
				paymentType: samplePaymentType2,
				amount: new BigDecimal("5678"))
		sampleDirectPaymentItem2.save(flush: true)
		
		sampleDirectPaymentItem3 = new DirectPaymentItem(
			directPayment: sampleDirectPayment,
			date: sdf.parse("06/03/2011"),
			paymentType: samplePaymentType3,
			amount: new BigDecimal("90"))
		sampleDirectPaymentItem3.save(flush: true)
	}
	
	private void setUpDirectPaymentAndDirectPaymentItemRelationship() {
		sampleDirectPayment.items = [sampleDirectPaymentItem1, sampleDirectPaymentItem2]
		sampleDirectPayment.save(flush: true)
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
				customer: sampleCustomer,
				status: "Unpaid",
				termDay: BigDecimal.ZERO,
				warehouse: sampleWarehouse,
				deliveryType: "Deliver",
				invoice: sampleSalesOrder,
				)
		sampleSalesDelivery.salesDeliveryId = sampleSalesDelivery.constructId()
		sampleSalesDelivery.save(flush: true)

		sampleSalesDeliveryWithAttachedInvoice = new SalesDelivery(
				preparedBy: "me",
				customer: sampleCustomer,
				date: sdf.parse("06/03/2011"),
				status: "Unpaid",
				termDay: BigDecimal.ZERO,
				warehouse: sampleWarehouse,
				deliveryType: "Deliver",
				invoice: sampleSalesOrder,
				)
		sampleSalesDeliveryWithAttachedInvoice.salesDeliveryId = sampleSalesDeliveryWithAttachedInvoice.constructId()
		sampleSalesDeliveryWithAttachedInvoice.save(flush: true)
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
		sampleSalesDelivery.addToItems(sampleSalesDeliveryItem)
		sampleSalesDelivery.save(flush: true)

		sampleSalesDeliveryWithAttachedInvoice.addToItems(sampleSalesDeliveryItem)
		sampleSalesDeliveryWithAttachedInvoice.save(flush: true)
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

		sampleCreditMemoWithAttachedInvoice = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discount: BigDecimal.ZERO,
			reason: sampleReason,
			status: "Approved",
			preparedBy: "me",
			date: sdf.parse("06/03/2011"),
			commissionRate: BigDecimal.ZERO
		)
		sampleCreditMemoWithAttachedInvoice.save(flush: true)
		
		sampleDebitMemo = new CreditMemo(
			directPaymentItem: sampleDirectPaymentItem3,
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			discount: BigDecimal.ZERO,
			reason: sampleReason,
			status: "Approved",
			preparedBy: "me",
			date: new Date(),
			commissionRate: BigDecimal.ZERO
		)
		sampleDebitMemo.save(flush: true)
	}
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
	private void setUpCreditMemoItems() {
		sampleCreditMemoItem = new CreditMemoItem(
				deliveryItem: sampleSalesDeliveryItem,
				oldQty: new BigDecimal("1"),
				oldPrice: new BigDecimal("1"),
				newQty: new BigDecimal("1"),
				newPrice: new BigDecimal("2"),
				creditMemo: sampleCreditMemo)
		sampleCreditMemo.save(flush: true)
		
		sampleCreditMemoItemForSampleCreditMemoWithAttachedInvoice = new CreditMemoItem(
			deliveryItem: sampleSalesDeliveryItem,
			oldQty: new BigDecimal("1"),
			oldPrice: new BigDecimal("15"),
			newQty: new BigDecimal("1"),
			newPrice: new BigDecimal("27"),
			creditMemo: sampleCreditMemoWithAttachedInvoice)
		sampleCreditMemoItemForSampleCreditMemoWithAttachedInvoice.save(flush: true)
		
		sampleDebitMemoItem = new CreditMemoItem(
			deliveryItem: sampleSalesDeliveryItem,
			oldQty: new BigDecimal("2"),
			oldPrice: new BigDecimal("1"),
			newQty: new BigDecimal("1"),
			newPrice: new BigDecimal("1"),
			creditMemo: sampleDebitMemo)
		sampleDebitMemo.save(flush: true)
	}

	private void setUpCreditMemoAndCreditMemoItemRelationship() {
		sampleCreditMemo.items = [sampleCreditMemoItem]
		sampleCreditMemo.save(flush: true)
		
		sampleCreditMemoWithAttachedInvoice.items = [sampleCreditMemoItemForSampleCreditMemoWithAttachedInvoice]
		sampleCreditMemoWithAttachedInvoice.save(flush: true)
		
		sampleDebitMemo.items = [sampleDebitMemoItem]
		sampleDebitMemo.save(flush: true)
	}
	
	private void setUpCustomerCharges() {
		sampleCustomerCharge = new CustomerCharge(
				date: new Date(),
				status: "Unpaid",
				customer: sampleCustomer,
				preparedBy: "me",
				amountPaid: new BigDecimal("500"))
		sampleCustomerCharge.save(flush: true)
		
		sampleCustomerChargeWithAttachedInvoice = new CustomerCharge(
				date: sdf.parse("06/03/2011"),
				status: "Unpaid",
				customer: sampleCustomer,
				preparedBy: "me",
				amountPaid: new BigDecimal("500"))
		sampleCustomerChargeWithAttachedInvoice.save(flush: true)
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
		sampleCustomerChargeWithAttachedInvoice.items = [sampleCustomerChargeItem]
		sampleCustomerChargeWithAttachedInvoice.save(flush: true)
	}
	
	private void setUpCheckStatuses() {
		sampleCheckStatus = new CheckStatus(
				identifier: "identifier",
				description: "description")
		sampleCheckStatus.save(flush: true)
	}
    private void setupCustomerAccount() {
		sampleCustomerAccount = new CustomerAccount(
				customer:sampleCustomer)
		sampleCustomerAccount.save(flush: true)
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

		sampleBouncedCheckWithAttachedInvoice = new BouncedCheck(
				date: sdf.parse("06/03/2011"),
				status: "Approved",
				customer: sampleCustomer,
				preparedBy: "me",
				bouncedCheckStatus: sampleCheckStatus,
				forRedeposit: false)
		sampleBouncedCheckWithAttachedInvoice.save(flush: true)
	}
	
	private void setUpBouncedCheckAndCheckPaymentRelationship() {
		sampleBouncedCheckWithAttachedInvoice.checks = [sampleCheckPaymentForBouncedCheck]
		sampleBouncedCheckWithAttachedInvoice.save(flush: true)
	}
	
	private void setUpDirectPaymentInvoices() {
		sampleDirectPaymentInvoiceBouncedCheck = new DirectPaymentInvoice(
				type: CustomerPaymentType.BOUNCED_CHECK,
				amount: new BigDecimal("148"))
		sampleDirectPaymentInvoiceBouncedCheck.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceBouncedCheck.save(flush: true)

		sampleDirectPaymentInvoiceSalesDelivery = new DirectPaymentInvoice(
				type: CustomerPaymentType.SALES_DELIVERY,
				amount: new BigDecimal("12"))
		sampleDirectPaymentInvoiceSalesDelivery.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceSalesDelivery.save(flush: true)

		sampleDirectPaymentInvoiceCreditMemo = new DirectPaymentInvoice(
				type: CustomerPaymentType.CREDIT_MEMO,
				amount: new BigDecimal("12"))
		sampleDirectPaymentInvoiceCreditMemo.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceCreditMemo.save(flush: true)

		sampleDirectPaymentInvoiceCustomerCharge = new DirectPaymentInvoice(
				type: CustomerPaymentType.CUSTOMER_CHARGE,
				amount: new BigDecimal("12"))
		sampleDirectPaymentInvoiceCustomerCharge.directPayment = sampleDirectPayment
		sampleDirectPaymentInvoiceCustomerCharge.save(flush: true)
	}

	private void setUpDirectPaymentAndDirectPaymentInvoiceRelationship() {
		sampleDirectPayment.invoices = [sampleDirectPaymentInvoiceBouncedCheck, sampleDirectPaymentInvoiceCreditMemo, sampleDirectPaymentInvoiceCustomerCharge, sampleDirectPaymentInvoiceSalesDelivery]
		sampleDirectPayment.save(flush: true)
	}

	private void setUpCustomerPaymentAndDirectPaymentInvoiceRelationship() {
		sampleCreditMemoWithAttachedInvoice.invoices = [sampleDirectPaymentInvoiceCreditMemo]
		sampleCreditMemoWithAttachedInvoice.takenByDirectPayment()
		sampleCreditMemoWithAttachedInvoice.save(flush: true)

		sampleBouncedCheckWithAttachedInvoice.invoices = [sampleDirectPaymentInvoiceBouncedCheck]
		sampleBouncedCheckWithAttachedInvoice.takenByDirectPayment()
		sampleBouncedCheckWithAttachedInvoice.save(flush: true)

		sampleCustomerChargeWithAttachedInvoice.invoices = [sampleDirectPaymentInvoiceCustomerCharge]
		sampleCustomerChargeWithAttachedInvoice.takenByDirectPayment()
		sampleCustomerChargeWithAttachedInvoice.save(flush: true)

		sampleSalesDeliveryWithAttachedInvoice.invoices = [sampleDirectPaymentInvoiceSalesDelivery]
		sampleSalesDeliveryWithAttachedInvoice.takenByDirectPayment()
		sampleSalesDeliveryWithAttachedInvoice.save(flush: true)
	}
	
	private void setUpFilteredDirectPayments() {
		def directPaymentFilteredByID = new DirectPayment(
			date: sdf.parse("06/01/2010"),
			customer: sampleCustomer,
			preparedBy: "me5",
			approvedBy: "you",
			remark: "fail",
		)
		directPaymentFilteredByID.save(flush: true)
		directPaymentFilteredByID.metaClass.'static'.computeBalance = {-> return BigDecimal.ONE}

		def customer1 = new Customer(
			identifier: "customer1",
			name: "nuuuuu",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false,
			term: sampleTerm
		)
		customer1.save(flush: true)

		def directPaymentFilteredByCustomerName = new DirectPayment(
			date: sdf.parse("06/01/2010"),
			customer: customer1,
			preparedBy: "me4",
			approvedBy: "you",
			remark: "fail",
		)
		directPaymentFilteredByCustomerName.save(flush: true)
		directPaymentFilteredByCustomerName.metaClass.'static'.computeBalance = {-> return BigDecimal.ONE}

		def directPaymentFilteredByStatus = new DirectPayment(
			date: sdf.parse("06/01/2010"),
			status: "Approved",
			customer: sampleCustomer,
			preparedBy: "me6",
			approvedBy: "you",
			remark: "fail",
		)
		directPaymentFilteredByStatus.save(flush: true)
		directPaymentFilteredByStatus.metaClass.'static'.computeBalance = {-> return BigDecimal.ONE}

		def directPaymentFilteredByDate = new DirectPayment(
			date: sdf.parse("06/01/2011"),
			customer: sampleCustomer,
			preparedBy: "me2",
			approvedBy: "you",
			remark: "fail",
		)
		directPaymentFilteredByDate.save(flush: true)
		directPaymentFilteredByDate.metaClass.'static'.computeBalance = {-> return BigDecimal.ONE}

		def directPaymentFilteredByWithBalance = new DirectPayment(
			date: sdf.parse("06/01/2010"),
			customer: sampleCustomer,
			preparedBy: "me3",
			remark: "fail",
			approvedBy: "you",
		)
		directPaymentFilteredByWithBalance.save(flush: true)
		directPaymentFilteredByWithBalance.metaClass.'static'.computeBalance = {-> return BigDecimal.ZERO}
	}
}
