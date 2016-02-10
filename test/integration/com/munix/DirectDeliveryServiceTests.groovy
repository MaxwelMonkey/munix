package com.munix

import grails.test.*

class DirectDeliveryServiceTests extends GroovyTestCase {
    def sampleCity
    def sampleCustomerType
    def sampleCustomer
    def sampleDirectDelivery
    def sampleTripTicket
    def sampleTruck
    def samplePersonnel
    def samplePersonnelType
    def sampleTerm
    def directDeliveryService
    def sampleTripTicketDeliveryItem
    def generalMethodService
    protected void setUp() {
        super.setUp()
        setUpCities()
        setUpCustomerTypes()
        setupTerms()
        setUpCustomers()
        setUpPersonnelType()
        setUpPersonnel()
        setUpTruck()
        setUpTripTicket()
        setUpDirectDelivery()
        setUpTripTicketDeliveryItem()

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
    private void setUpDirectDelivery() {
		sampleDirectDelivery = new DirectDelivery(
			customer:sampleCustomer,
            date:generalMethodService.createDate("02/12/2000"),
            preparedBy:"me",
            tripTicket:sampleTripTicketDeliveryItem
		)
		sampleDirectDelivery.save(flush: true)
        println sampleDirectDelivery.errors
	}
    private void setUpTruck(){
        sampleTruck = new Truck(
            identifier:"1234",
            model:"567",
            plateNumber:"890")
        sampleTruck.save()
    }
    private void setUpPersonnel(){
        samplePersonnel = new Personnel(
            identifier:"driver",
            lastName:"jose",
            firstName:"mang",
            type:samplePersonnelType)
        samplePersonnel.save()
    }
    private void setUpPersonnelType(){
        samplePersonnelType = new PersonnelType(
            identifier:"type",
            description:"typist")
        samplePersonnelType.save()
    }
    private void setupTerms(){
        sampleTerm = new Term(identifier: "asd",
            description: "qwe",
            dayValue: new BigDecimal("1"))
        sampleTerm.save()
    }
    private void setUpTripTicket(){
        sampleTripTicket = new TripTicket(
            truck:sampleTruck,
            driver:samplePersonnel,
            preparedBy:"me",
            markAsCompleteBy:"complete")
        sampleTripTicket.save()
    }
    private void setUpTripTicketDeliveryItem(){
        sampleTripTicketDeliveryItem = new TripTicketDeliveryItem(
            type:"123",
            tripTicket: sampleTripTicket,
            item:sampleDirectDelivery
        )
        sampleTripTicket.addToItems(sampleTripTicketDeliveryItem)
        sampleTripTicket.save()
    }
    protected void tearDown() {
        super.tearDown()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForId() {
        def objectParam = [:]
        objectParam["searchIdentifier"]= sampleDirectDelivery.id.toString()
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForIdNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchIdentifier"]= (sampleDirectDelivery.id+1).toString()
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForCustomer() {
        def objectParam = [:]
        objectParam["searchCustomerName"]= "customer"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForCustomerIncompleteInput() {
        def objectParam = [:]
        objectParam["searchCustomerName"]= "custom"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForCustomerNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchCustomerName"]= "asdf"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForStatus() {
        def objectParam = [:]
        objectParam["searchStatus"]= "Processing"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForStatusNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchStatus"]= "Cancelled"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForTripTicketExist() {
        def objectParam = [:]
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForTripTicketExistNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchWithTripTicket"]= "No"
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForTripTicketId() {
        def objectParam = [:]
        objectParam["searchTripTicketId"]= sampleTripTicket.id.toString()
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForTripTicketIdNoMatchingResult() {
        def objectParam = [:]
        objectParam["searchTripTicketId"]= (sampleTripTicket.id+1).toString()
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForDateBefore() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= "02/10/2000"
		objectParam["dateBeforeText_value"]= "02/10/2000"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForDateBeforeNoMatchingResult() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= "02/28/2000"
		objectParam["dateBeforeText_value"]= "02/28/2000"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForDateAfter() {
        def objectParam = [:]
        objectParam["dateAfterText"]= "02/13/2000"
		objectParam["dateAfterText_value"]= "02/13/2000"
		objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForDateAfterNoMatchingResult() {
        def objectParam = [:]
        objectParam["dateAfterText"]= "02/10/2000"
		objectParam["dateAfterText_value"]= "02/10/2000"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
    void testGetDirectDeliveryListWithCriteriaSearchForDateBeforeAndAfter() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= "02/10/2000"
		objectParam["dateBeforeText_value"]= "02/10/2000"
        objectParam["dateAfterText"]= "02/13/2000"
		objectParam["dateAfterText_value"]= "02/13/2000"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertEquals "The result must only have one object in it", 1, result.size()
        assertTrue "The result must contain sampleDirectDelivery", result.contains(sampleDirectDelivery)
    }
    void testGetDirectDeliveryListWithCriteriaSearchForDateBeforeAndAfterNoMatchingResult() {
        def objectParam = [:]
        objectParam["dateBeforeText"]= "02/14/2000"
		objectParam["dateBeforeText_value"]= "02/14/2000"
        objectParam["dateAfterText"]= "02/17/2000"
		objectParam["dateAfterText_value"]= "02/17/2000"
        objectParam["searchWithTripTicket"]= true
        def result = directDeliveryService.getDirectDeliveryListWithCriteria(objectParam)
        assertTrue "The result must be empty", result.isEmpty()
    }
}
