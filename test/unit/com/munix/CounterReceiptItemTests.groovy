package com.munix

import grails.test.*

class CounterReceiptItemTests extends GrailsUnitTestCase {
	def sampleSalesDelivery
	def sampleBouncedCheck
	def sampleCustomerCharge
	def sampleCreditMemo
	def sampleCounterReceipt
	def sampleCounterReceiptItem
	
    protected void setUp() {
        super.setUp()
		
		sampleSalesDelivery = new SalesDelivery()
		mockDomain(SalesDelivery, [sampleSalesDelivery])
		
		sampleBouncedCheck = new BouncedCheck()
		mockDomain(BouncedCheck, [sampleBouncedCheck])
		
		sampleCustomerCharge = new CustomerCharge()
		mockDomain(CustomerCharge, [sampleCustomerCharge])
		
		sampleCreditMemo = new CreditMemo()
		mockDomain(CreditMemo, [sampleCreditMemo])
		
		sampleCounterReceipt = new CounterReceipt()
		mockDomain(CounterReceipt, [sampleCounterReceipt])
		
		sampleCounterReceiptItem = new CounterReceiptItem(
			counterReceipt: sampleCounterReceipt,
			invoice: sampleSalesDelivery
		)
		mockDomain(CounterReceiptItem, [sampleCounterReceiptItem])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSetInvoiceSalesDelivery() {
		sampleCounterReceiptItem.setInvoice(sampleSalesDelivery)
		
		assertEquals "wrong invoice id", sampleSalesDelivery.id, sampleCounterReceiptItem.invoiceId
		assertEquals "wrong invoice type", CustomerPaymentType.SALES_DELIVERY, sampleCounterReceiptItem.invoiceType
    }
	
	void testSetInvoiceCustomerCharge() {
		sampleCounterReceiptItem.setInvoice(sampleCustomerCharge)
		
		assertEquals "wrong invoice id", sampleCustomerCharge.id, sampleCounterReceiptItem.invoiceId
		assertEquals "wrong invoice type", CustomerPaymentType.CUSTOMER_CHARGE, sampleCounterReceiptItem.invoiceType
	}
	
	void testSetInvoiceBouncedCheck() {
		sampleCounterReceiptItem.setInvoice(sampleBouncedCheck)
		
		assertEquals "wrong invoice id", sampleBouncedCheck.id, sampleCounterReceiptItem.invoiceId
		assertEquals "wrong invoice type", CustomerPaymentType.BOUNCED_CHECK, sampleCounterReceiptItem.invoiceType
	}
	
	void testSetInvoiceCreditMemo() {
		sampleCounterReceiptItem.setInvoice(sampleCreditMemo)
		
		assertEquals "wrong invoice id", sampleCreditMemo.id, sampleCounterReceiptItem.invoiceId
		assertEquals "wrong invoice type", CustomerPaymentType.CREDIT_MEMO, sampleCounterReceiptItem.invoiceType
	}
	
	void testGetInvoiceSalesDelivery() {
		sampleCounterReceiptItem.invoice = sampleSalesDelivery
		
		def result = sampleCounterReceiptItem.getInvoice()
		
		assertEquals "wrong invoice", sampleSalesDelivery, result  
	}
	
	void testGetInvoiceCustomerCharge() {
		sampleCounterReceiptItem.invoice = sampleCustomerCharge
		
		def result = sampleCounterReceiptItem.getInvoice()
		
		assertEquals "wrong invoice", sampleCustomerCharge, result
	}
	
	void testGetInvoiceBouncedCheck() {
		sampleCounterReceiptItem.invoice = sampleBouncedCheck
		
		def result = sampleCounterReceiptItem.getInvoice()
		
		assertEquals "wrong invoice", sampleBouncedCheck, result
	}
	
	void testGetInvoiceCreditMemo() {
		sampleCounterReceiptItem.invoice = sampleCreditMemo
		
		def result = sampleCounterReceiptItem.getInvoice()
		
		assertEquals "wrong invoice", sampleCreditMemo, result
	}
}