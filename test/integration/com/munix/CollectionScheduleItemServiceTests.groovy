package com.munix

import grails.test.*

class CollectionScheduleItemServiceTests extends GroovyTestCase {
    def collectionScheduleItemService
    def sampleCollectionSchedule
    def sampleCollectionScheduleItem
    def sampleCollector
    def sampleCounterReceipt
    def sampleCity
    def sampleCustomerType
    def sampleCustomer
    def sampleTerm

    protected void setUp() {
        super.setUp()
        setUpCities()
        setUpCustomerTypes()
        setupTerms()
        setUpCustomers()
        setupCounterReceipt()

        setupCollector()
        setupCollectionSchedule()
        setupCollectionScheduleItem()

    }
    private setupCollector(){
        sampleCollector = new Collector(
            identifier:"5678",
            description:"desc"
        )
        sampleCollector.save()
    }
    private setupCollectionSchedule(){
        sampleCollectionSchedule= new CollectionSchedule(
            identifier:"1234",
            startDate:new Date(),
            endDate:new Date(),
            preparedBy:"me",
            collector: sampleCollector
        )
        sampleCollectionSchedule.save()
    }
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
    private setupCollectionScheduleItem(){
        sampleCollectionScheduleItem= new CollectionScheduleItem(
            counterReceipt:sampleCounterReceipt,
            type:"Counter"
        )
        sampleCollectionScheduleItem.save()
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
    private void setUpCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		sampleCity.save(flush: true)
	}

	private void setUpCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier: "customerType", description: "descCustomerType")
		sampleCustomerType.save(flush: true)
	}
    private setupCounterReceipt(){
        sampleCounterReceipt= new CounterReceipt(
            status: CounterReceipt.Status.APPROVED,
            customer:sampleCustomer,
            preparedBy:"me"
        )
        sampleCounterReceipt.save()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGenerateCounterReceiptListCounterDateNotNullCollectionDateNull() {
        sampleCounterReceipt.counterDate = new Date()
        sampleCounterReceipt.save()
        assertNotNull "Counter receipt's counter date must not be null", sampleCounterReceipt.counterDate
        assertNull "Counter receipt's collection date must be null", sampleCounterReceipt.collectionDate

        def result = collectionScheduleItemService.generateCounterReceiptList(sampleCollectionSchedule)
        assertEquals "the number of content of the list must only be one", 1, result.size()
        assertTrue "the result must contain sampleCounterReceipt", result.contains(sampleCounterReceipt)
    }

    void testGenerateCounterReceiptListCounterDateNotNullCollectionDateNotNull() {
        sampleCounterReceipt.counterDate = new Date()
        sampleCounterReceipt.collectionDate = new Date()
        sampleCounterReceipt.save()
        assertNotNull "Counter receipt's counter date must not be null", sampleCounterReceipt.counterDate
        assertNotNull "Counter receipt's collection date must not be null", sampleCounterReceipt.collectionDate

        def result = collectionScheduleItemService.generateCounterReceiptList(sampleCollectionSchedule)
        assertEquals "the number of content of the list must be empty", 0, result.size()
    }

    void testGenerateCounterReceiptListCounterDateNullCollectionDateNotNull() {
        sampleCounterReceipt.collectionDate = new Date()
        sampleCounterReceipt.save()
        assertNull "Counter receipt's counter date must be null", sampleCounterReceipt.counterDate
        assertNotNull "Counter receipt's collection date must not be null", sampleCounterReceipt.collectionDate

        def result = collectionScheduleItemService.generateCounterReceiptList(sampleCollectionSchedule)
        assertEquals "the number of content of the list must only be 1", 1, result.size()
        assertTrue "the result must contain sampleCounterReceipt", result.contains(sampleCounterReceipt)
    }
    void testGenerateCounterReceiptListCounterDateNullCollectionDateNull() {
        assertNull "Counter receipt's counter date must be null", sampleCounterReceipt.counterDate
        assertNull "Counter receipt's collection date must be null", sampleCounterReceipt.collectionDate

        def result = collectionScheduleItemService.generateCounterReceiptList(sampleCollectionSchedule)
        assertEquals "the number of content of the list must only be 1", 1, result.size()
        assertTrue "the result must contain sampleCounterReceipt", result.contains(sampleCounterReceipt)
    }
    void testGenerateCounterReceiptListItemAlreadyIncluded() {
        sampleCollectionSchedule.addToItems(sampleCollectionScheduleItem)
        sampleCollectionSchedule.save()

        assertFalse "Collection Schedule items must not be empty", sampleCollectionSchedule.items.isEmpty()
        assertEquals "the number of content of the list must only be 1", 1, sampleCollectionSchedule.items.size()

        def result = collectionScheduleItemService.generateCounterReceiptList(sampleCollectionSchedule)
        assertEquals "the number of content of the list must be empty", 0, result.size()
    }
    void testGenerateCounterReceiptListCounterReceiptNotApproved() {
        def counterReceipt = CounterReceipt.get(sampleCounterReceipt.id)
        counterReceipt.status = CounterReceipt.Status.UNAPPROVED
        counterReceipt.save(flush:true)
        assertEquals "Counter receipt's status must not be Unapproved", "Unapproved", counterReceipt.status.toString()
        def result = collectionScheduleItemService.generateCounterReceiptList(sampleCollectionSchedule)
        assertEquals "the number of content of the list must be empty", 0, result.size()

    }

}
