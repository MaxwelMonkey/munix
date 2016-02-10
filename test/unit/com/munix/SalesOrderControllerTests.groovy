package com.munix

import grails.test.*
import grails.converters.JSON

class SalesOrderControllerTests extends ControllerUnitTestCase {
	def sampleCity
	def sampleCustomerType
	def sampleCustomerLedger
	def sampleCustomer
	def sampleDiscountType
	def sampleSalesAgent
	def sampleUser
	def sampleProduct1
	def sampleProduct2
    def sampleSalesOrder
    def sampleCustomer2
    def sampleCustomer3
	def sampleCustomer4
	def sampleSalesDelivery

	protected void setUp() {
		super.setUp()
		controller.metaClass.message={Map map-> map}

		mockDomain(Warehouse)
		sampleUser = new User(username:"user")
		mockDomain(User, [sampleUser])
		
		sampleCity= new City(identifier:"city",
				description:"cityDesc")
		mockDomain(City, [sampleCity])
		
		sampleCustomerType = new CustomerType(identifier:"type-1",
				description:"sample type")
		mockDomain(CustomerType, [sampleCustomerType])
		
		sampleSalesAgent = new SalesAgent(identifier:"SA-1",
			lastName:"ko",
			firstName:"malay")
		mockDomain(SalesAgent, [sampleSalesAgent])
		
		sampleCustomerLedger = new CustomerLedger(
			balance: new BigDecimal("400")
		)
		mockDomain(CustomerLedger, [sampleCustomerLedger])
		
		sampleCustomer = new Customer(identifier:"C-001",
			name:"sampleCustomer",
			bilAddrStreet:"some where",
			bilAddrCity:sampleCity,
			type:sampleCustomerType,
			declaredValue:new BigDecimal("0"),
			commissionRate:new BigDecimal("0"),
			salesAgent:sampleSalesAgent,
            status:Customer.Status.ACTIVE,
			creditLimit:new BigDecimal("1000"),
			customerLedger: sampleCustomerLedger)
        sampleCustomer2 = new Customer(identifier:"C-002",
			name:"sampleCustomer2",
			bilAddrStreet:"some where",
			bilAddrCity:sampleCity,
			type:sampleCustomerType,
			declaredValue:new BigDecimal("0"),
			autoApprove:true,
			commissionRate:new BigDecimal("0"),
			salesAgent:sampleSalesAgent,
            status:Customer.Status.ACTIVE,
			creditLimit:new BigDecimal("1000"))
        sampleCustomer3 = new Customer(identifier:"C-003",
			name:"sampleCustomer3",
			bilAddrStreet:"some where",
			bilAddrCity:sampleCity,
			type:sampleCustomerType,
			declaredValue:new BigDecimal("0"),
			autoSecondApprove:true,
			commissionRate:new BigDecimal("0"),
			salesAgent:sampleSalesAgent,
            status:Customer.Status.ACTIVE,
			creditLimit:new BigDecimal("0"))
		sampleCustomer4 = new Customer(identifier:"C-004",
			name:"sampleCustomer4",
			bilAddrStreet:"some where",
			bilAddrCity:sampleCity,
			type:sampleCustomerType,
			declaredValue:new BigDecimal("0"),
			autoApprove:true,
			autoSecondApprove:true,
			commissionRate:new BigDecimal("0"),
			salesAgent:sampleSalesAgent,
			status:Customer.Status.ACTIVE,
			creditLimit:new BigDecimal("0"))
		mockDomain(Customer, [sampleCustomer, sampleCustomer2, sampleCustomer3, sampleCustomer4])

		sampleDiscountType=new DiscountType(identifier:"discountType",
			description:"discount type")
		mockDomain(DiscountType, [sampleDiscountType])
		
		sampleProduct1=new Product(
			addedDescription:"1",
			identifier:"product1",
            runningCost:BigDecimal.ONE)
		sampleProduct2=new Product(
			addedDescription:"2",
			identifier:"product2",
            runningCost:BigDecimal.ONE)
		mockDomain(Product,[sampleProduct1,sampleProduct2])

        sampleSalesOrder = new SalesOrder(
            discountType:sampleDiscountType,
            priceType:PriceType.RETAIL,
            customer: sampleCustomer,
            items:[new SalesOrderItem(product:sampleProduct1,
                qty:new BigDecimal("1"),
                price:new BigDecimal("0"),
                finalPrice:new BigDecimal("0"))])
        mockDomain(SalesOrder, [sampleSalesOrder])
		
		sampleSalesDelivery = new SalesDelivery()
		mockDomain(SalesDelivery, [sampleSalesDelivery])
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testIndex() {
		controller.index()
		assertEquals "list", redirectArgs.action
	}
	void testCreate(){
		def createResults=controller.create()
		def results= [sampleCustomer, sampleCustomer2, sampleCustomer3, sampleCustomer4]
		assertEquals results,createResults.customerList
	}
    def testSaveWithOutErrors(){
        mockDomain(SalesOrder)
        mockDomain(SalesOrderItem)
        mockParams.priceType = "Retail"
        mockParams.customer = sampleCustomer
        mockParams.discountType = sampleDiscountType
        mockParams.items = [
                ["product.id": sampleProduct1.id, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: false],
                ["product.id": sampleProduct2.id, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: true]
        ]

        def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
        mockAuthenticate.demand.userDomain(1..1) {->
            return sampleUser
        }
        controller.authenticateService = mockAuthenticate.createMock()
        controller.save()
        assertEquals("List size not equal to 1",1,SalesOrder.list().size())
        SalesOrder.list().each{
            assertEquals("Wrong status", "Unapproved", it.status)
        }
        assertEquals("wrong redirection", "show", controller.redirectArgs.action)

    }
    def testSaveWithOutErrorsSecondApproval(){
        mockDomain(SalesOrder)
        mockDomain(SalesOrderItem)
        mockParams.priceType = "Retail"
        mockParams.customer = sampleCustomer2
        mockParams.discountType = sampleDiscountType
        mockParams.items = [
                ["product.id": sampleProduct1.id, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: false],
                ["product.id": sampleProduct2.id, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: true]
        ]

        def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
        mockAuthenticate.demand.userDomain(1..2) {->
            return sampleUser
        }
        controller.authenticateService = mockAuthenticate.createMock()
        controller.save()
        assertEquals("List size not equal to 1",1,SalesOrder.list().size())
        SalesOrder.list().each{
            assertEquals("Wrong status", "Second Approval Pending", it.status)
        }
        assertEquals("wrong redirection", "show", controller.redirectArgs.action)

    }
    def testSaveWithOutErrorsApprove(){
        mockDomain(SalesOrder)
        mockDomain(SalesOrderItem)
        mockParams.priceType = "Retail"
        mockParams.customer = sampleCustomer3
        mockParams.discountType = sampleDiscountType
        mockParams.items = [
                ["product.id": sampleProduct1.id, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: false],
                ["product.id": sampleProduct2.id, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: true]
        ]

        def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
        mockAuthenticate.demand.userDomain(1..1) {->
            return sampleUser
        }
        controller.authenticateService = mockAuthenticate.createMock()
		
        controller.save()
		
        assertEquals("List size not equal to 1",1,SalesOrder.list().size())
        SalesOrder.list().each{
            assertEquals("Wrong status", "Unapproved", it.status)
        }
        assertEquals("wrong redirection", "show", controller.redirectArgs.action)
    }
	
	def testSaveWithOutErrorsApproveAndSecondApprove(){
		mockDomain(SalesOrder)
		mockDomain(SalesOrderItem)
		mockParams.priceType = "Retail"
		mockParams.customer = sampleCustomer4
		mockParams.discountType = sampleDiscountType
		mockParams.items = [
				[product: sampleProduct1, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: false],
				[product: sampleProduct2, qty: 1, price: 2.00, finalPrice: 1.50, isDeleted: true]
		]

		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..3) {->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticate.createMock()
		
		controller.save()
		
		assertEquals("List size not equal to 1",1,SalesOrder.list().size())
		SalesOrder.list().each{
			assertEquals("Wrong status", "Approved", it.status)
		}
		assertEquals("wrong redirection", "show", controller.redirectArgs.action)
	}
	
	void testSaveWithErrors(){
		mockDomain(SalesOrder)
		mockDomain(SalesOrderItem)
		mockParams.priceType = "Retail"
		mockParams.items = [
			["product.id" : sampleProduct1.id
			, qty : 1, price : 2.00, finalPrice : 1.50, isDeleted : false],
			["product.id" : sampleProduct2.id
			, qty : 1, price : 2.00, finalPrice : 1.50, isDeleted : true]
		]
		
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1){ ->
			return sampleUser
		}
		controller.authenticateService= mockAuthenticate.createMock()
		controller.save()
		
		assertEquals("wrong render","create", renderArgs.view)

	}
    void testShowWithoutId(){
        mockDomain(SalesOrder)
        controller.show()
        assertEquals("wrong redirection", "list", controller.redirectArgs.action)
    }
	
	void testRetrieveCustomerRemainingCredit() {
		mockParams.customerId = sampleCustomer.id
		def expectedJsonResponse = [remainingCredit: new BigDecimal("600")]
		
		def result = controller.retrieveCustomerRemainingCredit()
		
		def controllerResponse = controller.response.contentAsString
		def jsonResult = JSON.parse(controllerResponse)
		
		assertEquals "json data should be correct", expectedJsonResponse.remainingCredit, jsonResult.remainingCredit
	}
	
	void testUnapprove() {
		sampleSalesOrder.status = "Approved"
		mockParams.id = sampleSalesOrder.id
		
		controller.unapprove()
		
		assertTrue "sample sales order should be unapproved", sampleSalesOrder.isUnapproved()
	}
	
	void testUnapproveNotUnapprovable() {
		sampleSalesOrder.status = "Approved"
		sampleSalesOrder.addToDeliveries(sampleSalesDelivery)
		mockParams.id = sampleSalesOrder.id
		
		controller.unapprove()
		
		assertTrue "sample sales order should still be approved", sampleSalesOrder.isApproved()
	}
}
