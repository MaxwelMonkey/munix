package com.munix

import grails.test.*
import java.text.SimpleDateFormat;

class SupplierPaymentControllerTests extends ControllerUnitTestCase {
	def sampleUser
	def sampleSupplier
	def sampleSupplierPayment
	def sampleSupplierPaymentItem
	def samplePurchaseInvoice
	def samplePurchaseInvoice2
	def sdf

    protected void setUp() {
        super.setUp()
		controller.metaClass.message = {Map map-> map}
		
		sdf = new SimpleDateFormat("MM/dd/yyyy")
		
		sampleUser = new User(username: "user")
		mockDomain(User, [sampleUser])
		
		def mockAuthenticate = mockFor(org.grails.plugins.springsecurity.service.AuthenticateService)
		mockAuthenticate.demand.userDomain(1..1) { ->
			return sampleUser
		}
		controller.authenticateService = mockAuthenticate.createMock()

		
		sampleSupplier = new Supplier(name: "supplier")
		mockDomain(Supplier, [sampleSupplier])
		
		sampleSupplierPaymentItem = new SupplierPaymentItem(
			amount: new BigDecimal("100")
		)
		mockDomain(SupplierPaymentItem, [sampleSupplierPaymentItem])
		
		samplePurchaseInvoice = new PurchaseInvoice()
		samplePurchaseInvoice2 = new PurchaseInvoice()
		mockDomain(PurchaseInvoice, [samplePurchaseInvoice, samplePurchaseInvoice2])
		samplePurchaseInvoice.metaClass.'static'.computePurchaseInvoiceDiscountedForeignTotal = { -> new BigDecimal("100") }
		
		sampleSupplierPayment = new SupplierPayment(
				supplier: sampleSupplier,
				preparedBy: "me"
		)
		sampleSupplierPayment.items = [sampleSupplierPaymentItem]
		sampleSupplierPayment.purchaseInvoices = [samplePurchaseInvoice]
		mockDomain(SupplierPayment, [sampleSupplierPayment])		
    }

    protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove PurchaseInvoice
    }

	void testIndex() {
		controller.index()
		
		assertEquals("Should redirect to list", "list", controller.redirectArgs.action)
	}
	
    void testCreateWithoutId() {

        def mockSupplierPaymentService = mockFor(SupplierPaymentService)
		mockSupplierPaymentService.demand.availableSuppliers(1..1){ ->
			return [sortedByIdentifier:[12],sortedByName:[13]]
		}
		controller.supplierPaymentService= mockSupplierPaymentService.createMock()
        def result = controller.create()
        assertTrue "sortedByIdentifier must contain 12", result.sortedByIdentifier.contains(12)
        assertEquals "sortedByIdentifier must contain 1 element",1, result.sortedByIdentifier.size()
        assertTrue "sortedByName must contain 13", result.sortedByName.contains(13)
        assertEquals "sortedByName must contain 1 element", 1, result.sortedByName.size()
        assertNull "supplierPaymentInstance's supplier must be null", result.supplierPaymentInstance.supplier
    }
	
    void testCreateWithId() {
        mockParams.id = sampleSupplier.id
        def mockSupplierPaymentService = mockFor(SupplierPaymentService)
		mockSupplierPaymentService.demand.availableSuppliers(1..1){ ->
			return [sortedByIdentifier:[12],sortedByName:[13]]
		}
        mockSupplierPaymentService.demand.availablePurchaseInvoices(1..1){x ->
			return "hello"
		}
		controller.supplierPaymentService= mockSupplierPaymentService.createMock()
        def result = controller.create()
        assertTrue "sortedByIdentifier must contain 12", result.sortedByIdentifier.contains(12)
        assertEquals "sortedByIdentifier must contain 1 element",1, result.sortedByIdentifier.size()
        assertTrue "sortedByName must contain 13", result.sortedByName.contains(13)
        assertEquals "sortedByName must contain 1 element", 1, result.sortedByName.size()
        assertEquals "supplierPaymentInstance's supplier must be sampleSupplier with id 1", sampleSupplier,result.supplierPaymentInstance.supplier
        assertEquals "result must contain hello string", "hello",result.purchaseInvoiceList
    }
	
	void testEditWithId() {
		mockParams.id = sampleSupplier.id
		def mockSupplierPaymentService = mockFor(SupplierPaymentService)
		mockSupplierPaymentService.demand.availablePurchaseInvoices(1..1){x ->
			return [samplePurchaseInvoice2]
		}
		controller.supplierPaymentService = mockSupplierPaymentService.createMock()
		
		def result = controller.edit()
		
		assertEquals "purchase invoice list must contain 2 elements", 2, result.purchaseInvoiceList.size()
		assertEquals "first element should be the original purchase invoice", samplePurchaseInvoice, result.purchaseInvoiceList.get(0)
	}
	
	void testEditWithoutId() {
		def result = controller.edit()
		
		assertEquals "should redirect to list", "list", controller.redirectArgs.action
	}
	
	void testShowWithId() {
		mockParams.id = sampleSupplierPayment.id
		
		def result = controller.show()
		
		assertNotNull "should have a supplier payment object", result.supplierPaymentInstance
	}
	
	void testShowWithoutId() {
		controller.show()
		
		assertEquals "should redirect to list", "list", controller.redirectArgs.action
	}
	
	void testSaveWithoutErrors() {
		mockParams["supplier.id"] = sampleSupplier.id
		mockRequest.addParameter "purchaseInvoice", samplePurchaseInvoice.id.toString()
		def mockSupplierPaymentService = mockFor(SupplierPaymentService)
		mockSupplierPaymentService.demand.checkIfAPurchaseInvoiceHasAlreadyBeenTaken(1..1){x, y ->
			return false
		}
		controller.supplierPaymentService= mockSupplierPaymentService.createMock()
        def mockEditSupplierPaymentService = mockFor(EditSupplierPaymentService)
		mockSupplierPaymentService.demand.addPurchaseInvoiceToSupplierPayment(1..1){ ->
			return ""
		}
		controller.editSupplierPaymentService= mockSupplierPaymentService.createMock()
		controller.save()
		
		assertEquals("Should redirect to show", "show", controller.redirectArgs.action)
	}

	void testSaveWithErrors() {
		def mockSupplierPaymentService = mockFor(SupplierPaymentService)
		mockSupplierPaymentService.demand.checkIfAPurchaseInvoiceHasAlreadyBeenTaken(1..1){x, y ->
			return true
		}
		controller.supplierPaymentService= mockSupplierPaymentService.createMock()
        def mockEditSupplierPaymentService = mockFor(EditSupplierPaymentService)
		mockEditSupplierPaymentService.demand.addPurchaseInvoiceToSupplierPayment(1..1){ ->
			return ""
		}
		controller.editSupplierPaymentService= mockEditSupplierPaymentService.createMock()
		
		def result = controller.save()

		assertEquals("Should render create page", "create", controller.redirectArgs.action)
	}
	
	void testUpdateWithoutErrors() {
		mockParams.id = sampleSupplierPayment.id
		mockRequest.addParameter "purchaseInvoice", samplePurchaseInvoice.id.toString()
		def mockSupplierPaymentService = mockFor(SupplierPaymentService)
		mockSupplierPaymentService.demand.updatePurchaseInvoiceFromSupplierPayment(1..1){x,y ->
			return ""
		}
		controller.supplierPaymentService = mockSupplierPaymentService.createMock()
		
		controller.update()
		
		assertEquals("Should redirect to show", "show", controller.redirectArgs.action)
		assertNull("Should have no errors", controller.flash.error)
	}
	
	void testUpdateWithErrorsEmptyPurchaseInvoiceList() {
		mockParams.id = sampleSupplierPayment.id
		
		controller.update()
		
		assertEquals("Should redirect to show", "show", controller.redirectArgs.action)
		assertNotNull("Should have no errors", controller.flash.error)
	}
	
	void testUpdateWithErrorsNullId() {
		controller.update()
		
		assertEquals("Should redirect to list", "list", controller.redirectArgs.action)
	}
	
	void testUpdateWithErrors() {
		mockParams.id = sampleSupplierPayment.id
		mockParams.preparedBy = null
		mockRequest.addParameter "purchaseInvoice", samplePurchaseInvoice.id.toString()
		def mockSupplierPaymentService = mockFor(SupplierPaymentService)
		mockSupplierPaymentService.demand.availablePurchaseInvoices(1..1){x ->
			return ""
		}
		controller.supplierPaymentService = mockSupplierPaymentService.createMock()
		
		controller.update()
		
		assertEquals("Should return to edit", "edit", controller.renderArgs.view)
	}

	
	void testApproveWithoutErrors() {
		mockParams.id = sampleSupplierPayment.id
		
		controller.approve()
		
		assertEquals "Sample supplier payment should be approved", SupplierPayment.Status.APPROVED, sampleSupplierPayment.status
		assertEquals "Sample supplier payment purchase invoices should be paid", PurchaseInvoice.Status.PAID, samplePurchaseInvoice.status
	}
	
	void testApproveWithErrors() {
		sampleSupplierPayment.preparedBy = null
		sampleSupplierPayment.save(flush: true)
		
		mockParams.id = sampleSupplierPayment.id
		
		controller.approve()
		
		assertEquals "should return to show page", "show", controller.redirectArgs.action
		assertEquals "should display error message", "Failed to approve supplier payment.", controller.flash.message
	}
	
	void testApproveWithErrorsBalanceNotEnough() {
		mockParams.id = sampleSupplierPayment.id
		sampleSupplierPaymentItem.amount = new BigDecimal("99.99")
		
		controller.approve()
		
		assertEquals "Sample supplier payment should not be approved", SupplierPayment.Status.UNAPPROVED, sampleSupplierPayment.status
		assertEquals "should return to show page", "show", controller.redirectArgs.action
		assertEquals "should display error message", "Can not approve until all invoices are paid.", controller.flash.message
	}
	
	void testUnapproveWithoutErrors() {
		sampleSupplierPayment.status = SupplierPayment.Status.APPROVED
		samplePurchaseInvoice.status = PurchaseInvoice.Status.PAID
		assertEquals "[GUARD] Sample supplier payment should be approved", SupplierPayment.Status.APPROVED, sampleSupplierPayment.status
		assertEquals "[GUARD] Sample supplier payment purchase invoices should be paid", PurchaseInvoice.Status.PAID, samplePurchaseInvoice.status
		
		mockParams.id = sampleSupplierPayment.id
		
		controller.unapprove()
		
		assertEquals "Sample supplier payment should not be approved", SupplierPayment.Status.UNAPPROVED, sampleSupplierPayment.status
		assertEquals "Sample supplier payment purchase invoices should be approved", PurchaseInvoice.Status.APPROVED, samplePurchaseInvoice.status		
		assertEquals "should return to show page", "show", controller.redirectArgs.action
	}
	
	void testUnapproveWithErrors() {
		sampleSupplierPayment.preparedBy = null
		sampleSupplierPayment.save(flush: true)
		
		mockParams.id = sampleSupplierPayment.id
		
		controller.unapprove()
		
		assertEquals "should return to show page", "show", controller.redirectArgs.action
		assertEquals "should display error message", "Failed to unapprove supplier payment.", controller.flash.message
	}
	
	void testCancelWithoutErrors() {
		sampleSupplierPayment.items = null
		mockParams.id = sampleSupplierPayment.id
		
		controller.cancel()
		
		assertEquals "Sample supplier payment should be cancelled", SupplierPayment.Status.CANCELLED, sampleSupplierPayment.status
		assertEquals "should return to show page", "show", controller.redirectArgs.action
	}
	
	void testCancelWithErrorsHasItems() {
		mockParams.id = sampleSupplierPayment.id
		
		controller.cancel()
		
		assertEquals "Sample supplier payment should be unapproved", SupplierPayment.Status.UNAPPROVED, sampleSupplierPayment.status
		assertEquals "should return to show page", "show", controller.redirectArgs.action
	}
}
