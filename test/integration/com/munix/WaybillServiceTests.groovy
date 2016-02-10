package com.munix

import grails.test.*
class WaybillServiceTests extends GroovyTestCase {
    def waybillService
    def sampleWaybill
	def sampleCity
	def sampleCustomerType
	def sampleCustomer
	def sampleForwarder
	def samplePersonnel
	def samplePersonnelType
	def sampleTripTicket
	def sampleTripTicketWaybillItem
	def sampleTruck
	def sampleTerm
    def generalMethodService
	
    private setupWaybill(){
        sampleWaybill = new Waybill(
            date: generalMethodService.createDate("05/05/2500"),
            customer: sampleCustomer,
			forwarder: sampleForwarder,
			declaredValue: 100.50,
			preparedBy: "me",
			status:  "Processing",
			tripTicket: sampleTripTicketWaybillItem
        )
        sampleWaybill.save(flush:true)
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
	
	private void setUpTruck(){
		sampleTruck = new Truck(
			identifier:"1234",
			model:"567",
			plateNumber:"890")
		sampleTruck.save()
	}
	
	private setupForwarders() {
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
	
	private void setupCities() {
		sampleCity = new City(identifier: "city", description: "cityDesc")
		sampleCity.save(flush: true)
	}
	
	private void setupCustomers() {
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
	
	private void setupCustomerTypes() {
		sampleCustomerType = new CustomerType(identifier: "customerType", description: "descCustomerType")
		sampleCustomerType.save(flush: true)
	}
	
	private void setupPersonnel(){
		samplePersonnel = new Personnel(
			identifier:"driver",
			lastName:"jose",
			firstName:"mang",
			type:samplePersonnelType)
		samplePersonnel.save()
	}
	private void setupPersonnelType(){
		samplePersonnelType = new PersonnelType(
			identifier:"type",
			description:"typist")
		samplePersonnelType.save()
	}
	private void setUpTripTicketWaybillItem(){
		sampleTripTicketWaybillItem = new TripTicketWaybillItem(
			type:"123",
			tripTicket: sampleTripTicket,
			item:sampleWaybill
		)
		sampleTripTicket.addToItems(sampleTripTicketWaybillItem)
		sampleTripTicket.save()
	}
	
    protected void setUp() {
        super.setUp()
		setupForwarders()
		setupCities()
		setupTerms()
		setupCustomerTypes()
        setupCustomers()
		setupPersonnelType()
		setupPersonnel()
		setUpTruck()
		setUpTripTicket()
        setupWaybill()
		setUpTripTicketWaybillItem()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testGenerateList() {
		def objectParam = [:]
		objectParam.searchIdentifier =  sampleWaybill.id.toString()
		objectParam.searchCustomerName = "customer name"
		objectParam.searchCustomerId = "customer"
		objectParam.searchForwarder = "forward"
		objectParam.searchStatus = "Processing"
		objectParam.searchWithTripTicket = "false"
		objectParam.searchDateFrom = "05/05/2500"
		objectParam.searchDateFrom_value = "05/05/2500"
		objectParam.searchDateTo = "05/05/2500"
		objectParam.searchDateTo_value = "05/05/2500"
		objectParam.searchTripTicketId = sampleTripTicket.id.toString()
		
		def result = waybillService.generateList(objectParam)
		
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListIdentifier() {
		def objectParam = [:]
		objectParam["searchIdentifier"] =  sampleWaybill.id.toString()
		
		def result = waybillService.generateList(objectParam)
		
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListIdentifierNoResults() {
		def objectParam = [:]
		objectParam["searchIdentifier"] =  "1234"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListCustomerName() {
		def objectParam = [:]
		objectParam["searchCustomerName"] = "customer name"
		
		def result = waybillService.generateList(objectParam)
		
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListCustomerNameNoResults() {
		def objectParam = [:]
		objectParam["searchCustomerName"] = "blank"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListCustomerId() {
		def objectParam = [:]
		objectParam["searchCustomerId"] = "customer"
		
		def result = waybillService.generateList(objectParam)
		
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListCustomerIdNoResults() {
		def objectParam = [:]
		objectParam["searchCustomerId"] = "blank"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListForwarder() {
		def objectParam = [:]
		objectParam["searchForwarder"] = "forward"
		
		def result = waybillService.generateList(objectParam)
		
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListForwarderNoResults() {
		def objectParam = [:]
		objectParam["searchForwarder"] = "blank"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListStatus() {
		def objectParam = [:]
		objectParam["searchStatus"] = "Processing"
		
		def result = waybillService.generateList(objectParam)
		
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListStatusNoResults() {
		def objectParam = [:]
		objectParam["searchStatus"] = "blank"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListWithTripTicket() {
		def objectParam = [:]
		objectParam["searchWithTripTicket"] = "false"
		
		def result = waybillService.generateList(objectParam)
		
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListDateFrom() {
		def objectParam = [:]
		objectParam["searchDateFrom"] = "05/05/2500"
		objectParam["searchDateFrom_value"] = "05/05/2500"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListDateFromNoResults() {
		def objectParam = [:]
		objectParam["searchDateFrom"] = "06/06/2600"
		objectParam["searchDateFrom_value"] = "06/06/2600"
		
		def result = waybillService.generateList(objectParam)
		assertFalse "Result should not contain sampleWaybill", result.contains(sampleWaybill)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListDateTo() {
		def objectParam = [:]
		objectParam["searchDateTo"] = "05/05/2500"
		objectParam["searchDateTo_value"] = "05/05/2500"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListDateToNoResults() {
		def objectParam = [:]
		objectParam["searchDateTo"] = "01/01/2100"
		objectParam["searchDateTo_value"] = "01/01/2100"
		
		def result = waybillService.generateList(objectParam)
		assertFalse "Result should not contain sampleWaybill", result.contains(sampleWaybill)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListDateFromDateTo() {
		def objectParam = [:]
		objectParam["searchDateFrom"] = "05/05/2500"
		objectParam["searchDateFrom_value"] = "05/05/2500"
		objectParam["searchDateTo"] = "05/05/2500"
		objectParam["searchDateTo_value"] = "05/05/2500"
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListDateFromDateToNoResults() {
		def objectParam = [:]
		objectParam["searchDateFrom"] = "06/06/2600"
		objectParam["searchDateFrom_value"] = "06/06/2600"
		objectParam["searchDateTo"] = "01/01/2100"
		objectParam["searchDateTo_value"] = "01/01/2100"
		
		def result = waybillService.generateList(objectParam)
		assertFalse "Result should not contain sampleWaybill", result.contains(sampleWaybill)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
	void testGenerateListTripTicketId() {
		def objectParam = [:]
        objectParam["searchTripTicketId"]= sampleTripTicket.id.toString()
		
		def result = waybillService.generateList(objectParam)
		assertTrue "Result should contain sampleWaybill", result.contains(sampleWaybill)
		assertEquals "Result total should be 1", 1, result.size()
	}
	
	void testGenerateListTripTicketIdNoResults() {
		def objectParam = [:]
		objectParam["searchTripTicketId"]= "1234"
		
		def result = waybillService.generateList(objectParam)
		assertFalse "Result should not contain sampleWaybill", result.contains(sampleWaybill)
		assertTrue "Result must be empty", result.isEmpty()
	}
	
}
