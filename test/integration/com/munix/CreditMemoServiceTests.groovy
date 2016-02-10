package com.munix

import grails.test.*

class CreditMemoServiceTests extends GroovyTestCase {
    def generalMethodService
    def creditMemoService
    def sampleTerm
    def sampleCustomer
    def sampleCity
    def sampleCustomerType
    def sampleDiscountType
	def sampleReason
    def sampleSalesDelivery
    def sampleWarehouse
    def sampleSalesOrder
    def sampleCreditMemo
    def sampleCreditMemo2
    def sampleSalesDeliveryItem
    def sampleProduct
    def sampleCreditMemoItem
    def sampleCreditMemoItem2
    def sampleCustomerAccount
    def sampleStock
    def sampleStockCard
	
    protected void setUp() {
        super.setUp()
        sampleTerm=new Term(identifier:"term",
            description:"termDesc",
            dayValue:new BigDecimal("1234"))
		sampleTerm.save()

        sampleWarehouse = new Warehouse(identifier:"ABBY",
        description:"Assembly")
        sampleWarehouse.save()

        sampleCity = new City( description:"quezon city",
        identifier:"QC")
        sampleCity.save()

        sampleCustomerType = new CustomerType(identifier:"H",
        description:"happy")
        sampleCustomerType.save()

        sampleCustomer = new Customer(identifier: "customer1",
            name:"cust",
            bilAddrCity:sampleCity,
            bilAddrStreet:"Accuier",
            status:Customer.Status.ACTIVE,
            type:sampleCustomerType,
            fax:"12345",
            term:sampleTerm,
            autoApprove:true)
        sampleCustomer.save()

        sampleDiscountType = new DiscountType(description:"prod",
        identifier:"P", margin: BigDecimal.ONE)
        sampleDiscountType.save()

		sampleReason = new Reason(description:"reason",
			identifier:"Reason")
			sampleReason.save()
			
        sampleProduct = new Product(
			identifier:"product2",
			status:"active",
			type: sampleDiscountType)
        sampleProduct.save()

        sampleSalesOrder = new SalesOrder(
            discountType:sampleDiscountType,
            priceType:PriceType.RETAIL,
            customer: sampleCustomer,
            items:[new SalesOrderItem(product:sampleProduct,
                qty:new BigDecimal("1"),
                price:new BigDecimal("0"),
                finalPrice:new BigDecimal("0"))])
        sampleSalesOrder.save()

        sampleSalesDelivery = new SalesDelivery(sampleSalesOrder)
        sampleSalesDelivery.warehouse = sampleWarehouse
        sampleSalesDelivery.preparedBy="me"
        sampleSalesDelivery.setDeliveryType("nothing")
        sampleSalesDeliveryItem = new SalesDeliveryItem(
			product: sampleProduct, 
			qty: new BigDecimal("10"),
			price: new BigDecimal("100.00")
		)
        sampleSalesDelivery.addToItems(sampleSalesDeliveryItem)
        sampleSalesDelivery.save()
        sampleSalesDeliveryItem.save()

        sampleCreditMemo=new CreditMemo(
	        customer:sampleCustomer,
	        discountType:sampleDiscountType,
			reason: sampleReason,
	        discount:new BigDecimal("0"),
	        preparedBy:"me",
	        date:new Date()
		)
		sampleCreditMemo.save()
        sampleCreditMemo2=new CreditMemo(
	        customer:sampleCustomer,
	        discountType:sampleDiscountType,
			reason: sampleReason,
	        discount:new BigDecimal("0"),
	        preparedBy:"me",
	        date:new Date()
		)
		sampleCreditMemo2.save()

        sampleCreditMemoItem = new CreditMemoItem(deliveryItem:sampleSalesDeliveryItem,
			date: generalMethodService.createDate("01/01/2001"),
            oldQty: new BigDecimal("10"),
            oldPrice: new BigDecimal("100.00"),
            newQty:new BigDecimal("5"),
            newPrice:new BigDecimal("200.00"),
            creditMemo:sampleCreditMemo)
        sampleCreditMemoItem = sampleCreditMemoItem.save(flush: true)
        sampleSalesDeliveryItem.addToCreditMemoItems(sampleCreditMemoItem)

        sampleCreditMemoItem2 = new CreditMemoItem(deliveryItem:sampleSalesDeliveryItem,
			date: generalMethodService.createDate("01/01/2002"),
            oldQty:new BigDecimal("5"),
            oldPrice:new BigDecimal("100.00"),
            newQty:new BigDecimal("3"),
            newPrice:new BigDecimal("200.00"),
            creditMemo:sampleCreditMemo2)
        sampleCreditMemoItem2 = sampleCreditMemoItem2.save(flush: true)
        sampleSalesDeliveryItem.addToCreditMemoItems(sampleCreditMemoItem2)
		
        sampleSalesDelivery.save()
        sampleCreditMemo.addToItems(sampleCreditMemoItem)
        sampleCreditMemo.save(flush: true)

        sampleCreditMemo2.addToItems(sampleCreditMemoItem2)
        sampleCreditMemo2.save(flush: true)

        sampleCustomerAccount = new CustomerAccount(customer:sampleCustomer)
        sampleCustomerAccount.save()

        sampleCustomer.customerAccount = sampleCustomerAccount
        sampleCustomer.save()

        sampleStock = new Stock(qty:0,warehouse: sampleWarehouse, product: sampleProduct)
        sampleStock.save()
        sampleWarehouse.addToStocks(sampleStock)
        sampleWarehouse.save()
        sampleStockCard = new StockCard(product: sampleProduct)
        sampleStockCard.save()
        sampleProduct.addToStocks(sampleStock)
        sampleProduct.stockCard = sampleStockCard
        sampleProduct.save()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testGenerateList() {
		def creditMemo = new CreditMemo(
	        customer: sampleCustomer,
	        discountType: sampleDiscountType,
			reason: sampleReason,
	        preparedBy: "me"
		)
		creditMemo.save(flush: true)
		
		setUpFilteredCreditMemos()

		def params = [:]
		params.searchIdentifier = creditMemo.id.toString()
		params.searchCustomerName = "cust"
		params.searchDiscountType = "P"
		params.searchStatus = "Unapproved"
		params.searchReason = "Reason"
		
		def result = creditMemoService.generateList(params)
		
		assertTrue "Result should contain creditMemo", result.contains(creditMemo)
		assertEquals "Result total should be 1", 1, result.size()
	}

    void testGetCreditMemoListForSalesDelivery() {
        def creditMemoList = creditMemoService.getCreditMemoListForSalesDelivery(sampleSalesDelivery)
        assertFalse "The list is empty", creditMemoList.isEmpty()
        assertTrue "The list does not contain the expected value", creditMemoList.contains(sampleCreditMemoItem)
    }
    void testGetCreditMemoListForSalesDeliveryNullSD() {
        def creditMemoList = creditMemoService.getCreditMemoListForSalesDelivery(null)
        assertTrue "The list is not empty", creditMemoList.isEmpty()
    }
    void testCheckIfCreditMemoExistForProduct(){
        def date= generalMethodService.createDate("09/20/2000")
        sampleCreditMemoItem.date= date
        sampleCreditMemoItem.save()
        creditMemoService.checkIfCreditMemoExistForProduct(sampleSalesDeliveryItem,date)

    }
    void testCheckIfAllCreditMemoItemsAreTheLatestNotTheLatest() {
        def cmitem1date= generalMethodService.createDate("09/20/2000")
        def cmitem2date= generalMethodService.createDate("09/23/2000")
        sampleCreditMemoItem.date=cmitem1date
        sampleCreditMemoItem2.date=cmitem2date
        sampleCreditMemoItem.save(flush: true)
        sampleCreditMemoItem2.save(flush: true)
        assertFalse "The creditMemoItem is the latest",creditMemoService.checkIfAllCreditMemoItemsAreTheLatest(sampleCreditMemo)
    }
	
    void testCheckIfAllCreditMemoItemsAreTheLatestLatest() {
        def cmitem1date= generalMethodService.createDate("09/23/2000")
        def cmitem2date= generalMethodService.createDate("09/20/2000")
        sampleCreditMemoItem.date=cmitem1date
        sampleCreditMemoItem2.date=cmitem2date
        sampleCreditMemoItem.save(flush: true)
        sampleCreditMemoItem2.save(flush: true)
        assertTrue "The creditMemoItem is not the latest",creditMemoService.checkIfAllCreditMemoItemsAreTheLatest(sampleCreditMemo)
    }

    void testGetAllUnpaidDebitMemoList(){
        sampleCreditMemo.approve()
        sampleCreditMemo2.approve()
        assertTrue "[Guard] Debit memo must be approve",sampleCreditMemo.isApproved()
        assertTrue "[Guard] Debit memo must be approve",sampleCreditMemo2.isApproved()
        assertTrue "[Guard] credit memo 2 must be a debit memo",sampleCreditMemo2.isADebitMemo()

        def result = creditMemoService.getAllUnpaidDebitMemoList(sampleCustomer)
        assertEquals "Debit memo list must contain one item",1,result.size()
        result.each{
            assertEquals "The result must be sampleCreditMemo2",sampleCreditMemo2, it
        }
    }
    void testGetAllUnpaidDebitMemoListNoResult(){
        assertTrue "[Guard] Debit memo must be unapprove",sampleCreditMemo.isUnapproved()
        assertTrue "[Guard] Debit memo must be unapprove",sampleCreditMemo2.isUnapproved()
        assertTrue "[Guard] credit memo 2 must be a debit memo",sampleCreditMemo2.isADebitMemo()

        def result = creditMemoService.getAllUnpaidDebitMemoList(sampleCustomer)
        assertEquals "Debit memo list must contain 0 item",0,result.size()

    }

    void testComputeTotalUnpaidDebitMemos(){
        sampleCreditMemo.approve()
        sampleCreditMemo2.approve()
        assertTrue "[Guard] Debit memo must be approve",sampleCreditMemo.isApproved()
        assertTrue "[Guard] Debit memo must be approve",sampleCreditMemo2.isApproved()
        assertTrue "[Guard] credit memo 2 must be a debit memo",sampleCreditMemo2.isADebitMemo()

        def result = creditMemoService.computeTotalUnpaidDebitMemos(sampleCustomer)
        assertEquals "Debit memo return value must be 100",100,result
    }
    void testGetApprovedDebitMemoList(){
        sampleCreditMemo.approve()
        sampleCreditMemo2.approve()
        assertTrue "[Guard] Debit memo must be approve",sampleCreditMemo.isApproved()
        assertTrue "[Guard] Debit memo must be approve",sampleCreditMemo2.isApproved()
        assertTrue "[Guard] credit memo 2 must be a debit memo",sampleCreditMemo2.isADebitMemo()

        def result = creditMemoService.getApprovedDebitMemoList([max:1,offset:0],sampleCustomer)
        assertEquals "Debit memo list must contain one item",1,result.totalCount
        result.debitMemoList.each{
            assertEquals "The result must be sampleCreditMemo2",sampleCreditMemo2, it
        }
    }
    void testSecondApproveCreditMemo() {
		def result = creditMemoService.secondApproveCreditMemo(sampleCreditMemo)
        assertEquals "Sample credit memo status should be Approved", "Approved", sampleCreditMemo.status
		assertTrue "The result return by the method must be true", result
        assertNotNull "The approved two by field in credit memo must not be null", sampleCreditMemo.approvedTwoBy
        assertEquals "Unpaid debit memos must still be 0 since its a credit memo",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidDebitMemos
	}

	void testSecondApproveDebitMemo() {
		def result = creditMemoService.secondApproveCreditMemo(sampleCreditMemo2)
        assertEquals "Sample credit memo status should be Approved", "Approved", sampleCreditMemo2.status
		assertTrue "The result return by the method must be true", result
        assertNotNull "The approved two by field in credit memo must not be null", sampleCreditMemo2.approvedTwoBy
        assertEquals "Unpaid debit memos must be 100",new BigDecimal("100"), sampleCustomerAccount.totalUnpaidDebitMemos
	}

	void testUnapproveDebitMemo() {
		def result = creditMemoService.unapproveCreditMemo(sampleCreditMemo2)
        assertEquals "Sample credit memo status should be Unapproved", "Unapproved", sampleCreditMemo2.status
		assertTrue "The result return by the method must be true", result
        assertEquals "Unpaid debit memos must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidDebitMemos
	}

	void testUnapproveCreditMemo() {
		def result = creditMemoService.unapproveCreditMemo(sampleCreditMemo)
        assertEquals "Sample credit memo status should be Unapproved", "Unapproved", sampleCreditMemo.status
		assertTrue "The result return by the method must be true", result
        assertEquals "Unpaid debit memos must be 0",BigDecimal.ZERO, sampleCustomerAccount.totalUnpaidDebitMemos
	}

    void testCancelCreditMemo() {
		creditMemoService.cancelCreditMemo(sampleCreditMemo)

		assertTrue "credit memo should be cancelled", sampleCreditMemo.isCancelled()
	}

	private void setUpFilteredCreditMemos() {
		def creditMemoFilteredByID = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			preparedBy: "me"
		)
		creditMemoFilteredByID.save(flush: true)

		def customer = new Customer(
			identifier: "customer",
			name: "nuuuuu",
			bilAddrStreet: "1234 here",
			bilAddrCity: sampleCity,
			status: Customer.Status.ACTIVE,
			type: sampleCustomerType,
			autoApprove: false
		)
		customer.save(flush: true)

		def creditMemoFilteredByCustomerName = new CreditMemo(
			customer: customer,
			discountType: sampleDiscountType,
			preparedBy: "me"
		)
		creditMemoFilteredByCustomerName.save(flush: true)

		def discountType = new DiscountType(
			identifier:"fail",
			description:"prod"
		)
		discountType.save()
	
		def creditMemoFilteredByDiscountType = new CreditMemo(
			customer: sampleCustomer,
			discountType: discountType,
			preparedBy: "me"
		)
		creditMemoFilteredByDiscountType.save(flush: true)
		
		def creditMemoFilteredByStatus = new CreditMemo(
			customer: sampleCustomer,
			discountType: sampleDiscountType,
			status: "Approved",
			preparedBy: "me"
		)
		creditMemoFilteredByStatus.save(flush: true)
	}
}
