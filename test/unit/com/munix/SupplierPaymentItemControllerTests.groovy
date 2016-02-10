package com.munix

import grails.test.*

class SupplierPaymentItemControllerTests extends ControllerUnitTestCase {
	def sampleSupplierPayment
	def sampleSupplierPaymentItem
	def samplePaymentTypeCash
	def samplePaymentTypeCheck
	def sampleBank
	def sampleCheckType

    protected void setUp() {
        super.setUp()
		controller.metaClass.message = {Map map-> map}
		
		sampleSupplierPayment = new SupplierPayment()
		mockDomain(SupplierPayment, [sampleSupplierPayment])
		
		samplePaymentTypeCash = new PaymentType(
			identifier: "cash",
			description: "cash",
			isCheck: false
		)
		samplePaymentTypeCheck = new PaymentType(
			identifier: "check",
			description: "check",
			isCheck: true
		)
		mockDomain(PaymentType, [samplePaymentTypeCash, samplePaymentTypeCheck])
		
		sampleBank = new Bank()
		mockDomain(Bank, [sampleBank])
		
		sampleCheckType = new CheckType(
			routingNumber: "routingNumber",
			description: "description",
			branch: "branch"
			)
		mockDomain(CheckType, [sampleCheckType])
		
		sampleSupplierPaymentItem = new SupplierPaymentItem(
			type: samplePaymentTypeCheck,
			amount: new BigDecimal("19"),
			date: new Date(),
			checkNumber: "23",
			checkBank: sampleBank,
			checkBranch: "branch",
			checkType: sampleCheckType,
			remark: "remark",
			payment: sampleSupplierPayment
		)
		mockDomain(SupplierPaymentItem, [sampleSupplierPaymentItem])
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testSaveWithoutErrors() {
		mockParams."type.id" = samplePaymentTypeCheck.id
		mockParams.date = "10/10/2010"
		mockParams.amount = "12"
		mockParams.checkNumber = "1"
		mockParams."checkBank.id" = sampleBank.id
		mockParams.checkBranch = "branch"
		mockParams."checkType.id" = sampleCheckType.id
		mockParams.remark = "remark"
		mockParams.id = sampleSupplierPayment.id 
		
		controller.save()
		
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
		assertEquals "should redirect to supplier payment controller", "supplierPayment", controller.redirectArgs.controller
	}
	
	void testSaveWithErrors() {
		mockParams."type.id" = samplePaymentTypeCheck.id
		mockParams.date = "10/10/2010"
		mockParams.checkNumber = "1"
		mockParams."checkBank.id" = sampleBank.id
		mockParams."checkType.id" = sampleCheckType.id
		mockParams.remark = "remark"
		mockParams.id = sampleSupplierPayment.id
		
		controller.save()
		
		assertEquals "should return to create", "create", renderArgs.view
	}
	
	void testSaveWithErrorsCheckAndNullCheckNumber() {
		mockParams."type.id" = samplePaymentTypeCheck.id
		mockParams.date = "10/10/2010"
		mockParams.amount = "12"
		mockParams."checkBank.id" = sampleBank.id
		mockParams."checkType.id" = sampleCheckType.id
		mockParams.remark = "remark"
		mockParams.id = sampleSupplierPayment.id 
		
		controller.save()
		
		assertEquals "should return to create", "create", renderArgs.view
	}
	
	void testSaveWithErrorsCheckAndNullCheckBank() {
		mockParams."type.id" = samplePaymentTypeCheck.id
		mockParams.date = "10/10/2010"
		mockParams.amount = "12"
		mockParams.checkNumber = "1"
		mockParams."checkType.id" = sampleCheckType.id
		mockParams.remark = "remark"
		mockParams.id = sampleSupplierPayment.id
		
		controller.save()
		
		assertEquals "should return to create", "create", renderArgs.view
	}
	
	void testSaveWithErrorsCheckAndNullCheckType() {
		mockParams."type.id" = samplePaymentTypeCheck.id
		mockParams.date = "10/10/2010"
		mockParams.amount = "12"
		mockParams.checkNumber = "1"
		mockParams."checkBank.id" = sampleBank.id
		mockParams.remark = "remark"
		mockParams.id = sampleSupplierPayment.id
		
		controller.save()
		
		assertEquals "should return to create", "create", renderArgs.view
	}
	
	void testUpdateWithoutErrors() {
		mockParams.id = sampleSupplierPaymentItem.id
		mockParams."type.id" = samplePaymentTypeCash.id
		
		controller.update()
		
		assertEquals "Supplier payment item type should be cash", "cash", sampleSupplierPaymentItem.type.identifier 
		assertEquals "Supplier payment item check number should be null", null, sampleSupplierPaymentItem.checkNumber
		assertEquals "Supplier payment item check type should be null", null, sampleSupplierPaymentItem.checkType
		assertEquals "Supplier payment item check bank should be null", null, sampleSupplierPaymentItem.checkBank
		assertEquals "Supplier payment item check branch should be null", "", sampleSupplierPaymentItem.checkBranch
		assertEquals "should redirect to controller supplierPayment", "supplierPayment", controller.redirectArgs.controller
		assertEquals "should redirect to show", "show", controller.redirectArgs.action
	}
	
	void testUpdateWithErrors() {
		mockParams.id = sampleSupplierPaymentItem.id
		mockParams.amount = null
		
		controller.update()
		
		assertEquals "should return to edit", "edit", renderArgs.view
	}
	
	void testUpdateWithErrorNoCheckNumber() {
		mockParams.id = sampleSupplierPaymentItem.id
		mockParams.checkNumber = null
		
		controller.update()
		
		assertEquals "should return to edit", "edit", renderArgs.view
	}
	
	void testUpdateWithErrorNoCheckBank() {
		mockParams.id = sampleSupplierPaymentItem.id
		sampleSupplierPaymentItem.checkBank = null
		
		controller.update()
		
		assertEquals "should return to edit", "edit", renderArgs.view
	}
	
	void testUpdateWithErrorNoCheckType() {
		mockParams.id = sampleSupplierPaymentItem.id
		sampleSupplierPaymentItem.checkType = null
		
		controller.update()
		
		assertEquals "should return to edit", "edit", renderArgs.view
	}
}
