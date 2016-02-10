package com.munix

import grails.test.*

class EditCounterReceiptServiceTests extends GrailsUnitTestCase {
    def editCounterReceiptService = new EditCounterReceiptService()
    def sampleSalesDelivery1
    def sampleSalesDelivery2
    def sampleSalesDelivery3
    def sampleCustomer
    def sampleCustomer2
    def sampleCounterReceipt
    def sampleCustomerCharge1
    def sampleCustomerCharge2
	def sampleBouncedCheck1
	def sampleBouncedCheck2
	def sampleCreditMemo1
	def sampleCreditMemo2
	def sampleCounterReceiptItemWithSalesDelivery1
	def sampleCounterReceiptItemWithSalesDelivery2
	def sampleCounterReceiptItemWithCustomerCharge1
	def sampleCounterReceiptItemWithCustomerCharge2
	def sampleCounterReceiptItemWithBouncedCheck1
	def sampleCounterReceiptItemWithBouncedCheck2
	def sampleCounterReceiptItemWithCreditMemo1
	def sampleCounterReceiptItemWithCreditMemo2
	
    protected void setUp() {
        super.setUp()
        
		sampleCustomer = new Customer()
        sampleCustomer2 = new Customer()
        mockDomain(Customer, [sampleCustomer,sampleCustomer2])
        
		sampleSalesDelivery1 = new SalesDelivery(customer: sampleCustomer,status:"Unpaid")
        sampleSalesDelivery2 = new SalesDelivery(customer: sampleCustomer,status:"paid")
        sampleSalesDelivery3 = new SalesDelivery(customer: sampleCustomer2,status:"Unpaid")
        mockDomain(SalesDelivery, [sampleSalesDelivery1,sampleSalesDelivery2,sampleSalesDelivery3])
		SalesDelivery.metaClass.static.computeTotalAmount = { -> return new BigDecimal("200")}
		
		sampleCustomerCharge1 = new CustomerCharge()
		sampleCustomerCharge2 = new CustomerCharge()
		mockDomain(CustomerCharge, [sampleCustomerCharge1,sampleCustomerCharge2])
		CustomerCharge.metaClass.static.computeTotalAmount = { -> return new BigDecimal("200")}
		
		sampleBouncedCheck1 = new BouncedCheck()
		sampleBouncedCheck2 = new BouncedCheck()
		mockDomain(BouncedCheck, [sampleBouncedCheck1,sampleBouncedCheck2])
		BouncedCheck.metaClass.static.computeTotalAmount = { -> return new BigDecimal("200")}
		
		sampleCreditMemo1 = new CreditMemo()
		sampleCreditMemo2 = new CreditMemo()
		mockDomain(CreditMemo, [sampleCreditMemo1,sampleCreditMemo2])
		CreditMemo.metaClass.static.computeCreditMemoTotalAmount = { -> return new BigDecimal("200")}
        
		sampleCounterReceiptItemWithSalesDelivery1 = new CounterReceiptItem(invoice:sampleSalesDelivery1, amount: new BigDecimal("200"))
		sampleCounterReceiptItemWithSalesDelivery2 = new CounterReceiptItem(invoice:sampleSalesDelivery2, amount: new BigDecimal("200"))
		sampleCounterReceiptItemWithCustomerCharge1 = new CounterReceiptItem(invoice:sampleCustomerCharge1, amount: new BigDecimal("200")) 
		sampleCounterReceiptItemWithCustomerCharge2 = new CounterReceiptItem(invoice:sampleCustomerCharge2, amount: new BigDecimal("200")) 
		sampleCounterReceiptItemWithBouncedCheck1 = new CounterReceiptItem(invoice:sampleBouncedCheck1, amount: new BigDecimal("200"))
		sampleCounterReceiptItemWithBouncedCheck2 = new CounterReceiptItem(invoice:sampleBouncedCheck2, amount: new BigDecimal("200"))
		sampleCounterReceiptItemWithCreditMemo1 = new CounterReceiptItem(invoice:sampleCreditMemo1, amount: new BigDecimal("200"))
		sampleCounterReceiptItemWithCreditMemo2 = new CounterReceiptItem(invoice:sampleCreditMemo2, amount: new BigDecimal("200"))
		mockDomain(CounterReceiptItem, [
			sampleCounterReceiptItemWithSalesDelivery1, sampleCounterReceiptItemWithSalesDelivery2, 
			sampleCounterReceiptItemWithCustomerCharge1, sampleCounterReceiptItemWithCustomerCharge2,
			sampleCounterReceiptItemWithBouncedCheck1, sampleCounterReceiptItemWithBouncedCheck2,
			sampleCounterReceiptItemWithCreditMemo1, sampleCounterReceiptItemWithCreditMemo2
			])
		
		sampleCounterReceipt = new CounterReceipt(items:[])
		mockDomain(CounterReceipt, [sampleCounterReceipt])
		
    }

    protected void tearDown() {
        super.tearDown()
		def remove =  GroovySystem.metaClassRegistry.&removeMetaClass
		remove SalesDelivery
		remove CustomerCharge
    }

    void testAddListOfSalesDeliveryIdToCounterReceiptOne(){
        assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
        
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithSalesDelivery1])
        
		assertEquals "The list of items contains more than one salesDelivery", 1, sampleCounterReceipt.items.size()
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithSalesDelivery1)
        sampleCounterReceipt.items.each { 
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount 
		}
    }
	
    void testAddListOfSalesDeliveryIdToCounterReceiptMoreThanOne(){
        assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
        
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithSalesDelivery1,sampleCounterReceiptItemWithSalesDelivery2])
        
		assertEquals "The list of items contains more than two salesDelivery", 2, sampleCounterReceipt.items.size()
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithSalesDelivery1)
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithSalesDelivery2)
		sampleCounterReceipt.items.each {
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount
		}
    }
	
    void testAddListOfSalesDeliveryIdToCounterReceiptEmpty(){
        assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
        sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [])
		
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
	
    void testAddListOfCustomerChargeIdToCounterReceiptOne(){
        assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
        sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCustomerCharge1])
		
        assertEquals "The list of items contains more than one charge", 1, sampleCounterReceipt.items.size()
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCustomerCharge1)
		sampleCounterReceipt.items.each {
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount
		}
    }
	
    void testAddListOfCustomerChargeIdToCounterReceiptTwo(){
        assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
        sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCustomerCharge1,sampleCounterReceiptItemWithCustomerCharge2])
		
        assertEquals "The list of items contains more than one charge", 2, sampleCounterReceipt.items.size()
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCustomerCharge1)
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCustomerCharge2)
		sampleCounterReceipt.items.each {
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount
		}
    }
	
    void testAddListOfCustomerChargeIdToCounterReceiptNone(){
        assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
        sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [])
		
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
	
	void testAddListOfBouncedCheckIdToCounterReceiptOne(){
		assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithBouncedCheck1])
		
		assertEquals "The list of items contains more than one bouncedCheck", 1, sampleCounterReceipt.items.size()
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithBouncedCheck1)
		sampleCounterReceipt.items.each {
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount
		}
	}
	
	void testAddListOfBouncedCheckIdToCounterReceiptMoreThanOne(){
		assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithBouncedCheck1,sampleCounterReceiptItemWithBouncedCheck2])
		
		assertEquals "The list of items contains more than two Bounced Checks", 2, sampleCounterReceipt.items.size()
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithBouncedCheck1)
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithBouncedCheck2)
		sampleCounterReceipt.items.each {
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount
		}
	}
	
	void testAddListOfBouncedCheckIdToCounterReceiptEmpty(){
		assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [])
		
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
	
	void testAddListOfCreditMemoIdToCounterReceiptOne(){
		assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCreditMemo1])
		
		assertEquals "The list of items contains more than one CreditMemo", 1, sampleCounterReceipt.items.size()
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCreditMemo1)
		sampleCounterReceipt.items.each {
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount
		}
	}
	
	void testAddListOfCreditMemoIdToCounterReceiptMoreThanOne(){
		assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCreditMemo1,sampleCounterReceiptItemWithCreditMemo2])
		
		assertEquals "The list of items contains more than two CreditMemos", 2, sampleCounterReceipt.items.size()
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCreditMemo1)
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCreditMemo2)
		sampleCounterReceipt.items.each {
			assertEquals "Incorrect amount", new BigDecimal("200"), it.amount
		}
	}
	
	void testAddListOfCreditMemoIdToCounterReceiptEmpty(){
		assertTrue "[GUARD] The items list is not empty", sampleCounterReceipt.items.isEmpty()
		
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [])
		
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
	
    void testRemoveItemsFromCounterReceiptOneDelivery(){
        assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithSalesDelivery1])
        assertTrue "The items list does not have to expected value", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithSalesDelivery1)
        sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithSalesDelivery1])
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
    void testRemoveItemsFromCounterReceiptMoreThanOneDelivery(){
        assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithSalesDelivery1,sampleCounterReceiptItemWithSalesDelivery2])
        assertEquals "The items list does not have to expected value",2, sampleCounterReceipt.items.size()
        sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithSalesDelivery1,sampleCounterReceiptItemWithSalesDelivery2])
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
    void testRemoveItemsFromCounterReceiptNoDelivery(){
        assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
        sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [])
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
    void testRemoveItemsFromCounterReceiptOneCharge(){
        assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
        sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCustomerCharge1])
        assertTrue "The charge list does not have to expected value", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCustomerCharge1)
        sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCustomerCharge1])
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
    void testRemoveItemsFromCounterReceiptMoreThanOneCharge(){
        assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
        sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCustomerCharge1, sampleCounterReceiptItemWithCustomerCharge2])
        assertEquals "The charge list does not have to expected value",2, sampleCounterReceipt.items.size()
        sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCustomerCharge1,sampleCounterReceiptItemWithCustomerCharge2])
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
    void testRemoveItemsFromCounterReceiptNoCharge(){
        assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
        sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [])
        assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
    }
	void testRemoveItemsFromCounterReceiptOneBouncedCheck(){
		assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithBouncedCheck1])
		assertTrue "The items list does not have to expected value", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithBouncedCheck1)
		sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithBouncedCheck1])
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
	void testRemoveItemsFromCounterReceiptMoreThanOneBouncedCheck(){
		assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithBouncedCheck1,sampleCounterReceiptItemWithBouncedCheck2])
		assertEquals "The items list does not have to expected value",2, sampleCounterReceipt.items.size()
		sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithBouncedCheck1,sampleCounterReceiptItemWithBouncedCheck2])
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
	void testRemoveItemsFromCounterReceiptNoBouncedCheck(){
		assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [])
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
	void testRemoveItemsFromCounterReceiptOneCreditMemo(){
		assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCreditMemo1])
		assertTrue "The items list does not have to expected value", sampleCounterReceipt.items.contains(sampleCounterReceiptItemWithCreditMemo1)
		sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCreditMemo1])
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
	void testRemoveItemsFromCounterReceiptMoreThanOneCreditMemo(){
		assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.addItemsToCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCreditMemo1,sampleCounterReceiptItemWithCreditMemo2])
		assertEquals "The items list does not have to expected value",2, sampleCounterReceipt.items.size()
		sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [sampleCounterReceiptItemWithCreditMemo1,sampleCounterReceiptItemWithCreditMemo2])
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
	void testRemoveItemsFromCounterReceiptNoCreditMemo(){
		assertTrue "The items list is not empty", sampleCounterReceipt.items.isEmpty()
		sampleCounterReceipt = editCounterReceiptService.removeItemsFromCounterReceipt(sampleCounterReceipt, [])
		assertTrue "The list does not contain the expected result", sampleCounterReceipt.items.isEmpty()
	}
}
