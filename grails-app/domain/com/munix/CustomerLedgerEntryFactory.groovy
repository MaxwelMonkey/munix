package com.munix

abstract class CustomerLedgerEntryFactory {
	def static generalMethodService = new GeneralMethodService()

	public static CustomerLedgerEntry createInitialBalanceEntry(Customer customer) {
		return new CustomerLedgerEntry(
			dateOpened : new Date(),
			datePosted : new Date(), 
			type : CustomerLedgerEntry.Type.CUSTOMER_INITIAL_BALANCE_ENTRY,
			referenceId : customer.identifier,
			amount : new BigDecimal("0.00"),
			runningBalance : new BigDecimal("0.00"),
			linkId : customer.id,
			isChild : false
		)
	}
	
	public static CustomerLedgerEntry createApprovedSalesDelivery(SalesDelivery salesDelivery) {
		CustomerLedgerEntry entry = createSalesDelivery(salesDelivery)
		entry.type = CustomerLedgerEntry.Type.APPROVED_SALES_DELIVERY
		entry.computeDebit()
		return entry
	}
	
	public static CustomerLedgerEntry createUnapprovedSalesDelivery(SalesDelivery salesDelivery) {
		CustomerLedgerEntry entry = createSalesDelivery(salesDelivery)
		entry.type = CustomerLedgerEntry.Type.UNAPPROVED_SALES_DELIVERY
		entry.computeCredit()
		return entry
	}
	
    public static CustomerLedgerEntry createApprovedDirectPayment(DirectPayment directPayment) {
		CustomerLedgerEntry entry = new CustomerLedgerEntry(
			type: CustomerLedgerEntry.Type.APPROVED_DIRECT_PAYMENT,
			dateOpened: directPayment.date,
			datePosted: new Date(),
			referenceId: directPayment.toString(),
			amount: directPayment.computePaymentTotal(),
            linkId: directPayment.id,
            isChild: false
		)
		return entry
	}
	
    public static CustomerLedgerEntry createUnapprovedDirectPayment(DirectPayment directPayment) {
		CustomerLedgerEntry entry = new CustomerLedgerEntry(
			type: CustomerLedgerEntry.Type.UNAPPROVED_DIRECT_PAYMENT,
			dateOpened: directPayment.date,
            datePosted: new Date(),
			referenceId: directPayment.toString(),
			amount: directPayment.computePaymentTotal(),
            linkId: directPayment.id,
            isChild: false
		)
		return entry
	}
	
	public static CustomerLedgerEntry createDirectPaymentItem(DirectPaymentItem directPaymentItem) {
		def amount = directPaymentItem.amount
		def paymentType = directPaymentItem.paymentType
		def remark = directPaymentItem.remark
		def details = paymentType.identifier
		if (paymentType.isCheck) {
			def checkPayment = directPaymentItem.checkPayment
			def dateString = generalMethodService.getDateString(checkPayment.date, "MM/dd/yyyy")
			details += " - " + dateString + " " + checkPayment.bank.identifier + " - " + checkPayment.branch + " - " + checkPayment.checkNumber +" "+ checkPayment.type.description
		}
		CustomerLedgerEntry entry = new CustomerLedgerEntry (
			type: CustomerLedgerEntry.Type.DIRECT_PAYMENT_ITEM,
			details: details,
			remark: remark,
			datePosted: new Date(),
			dateOpened: directPaymentItem.directPayment.date,
			referenceId: directPaymentItem.directPayment.toString(),
			amount: amount,
			linkId: directPaymentItem.id,
			isChild: true
		)

		return entry
	}
	
	public static CustomerLedgerEntry createApprovedOverpayment(Overpayment overpayment) {
		CustomerLedgerEntry entry = createOverpayment(overpayment)
		entry.computeDebit()
		return entry
	}
	
	public static CustomerLedgerEntry createUnapprovedOverpayment(Overpayment overpayment) {
		CustomerLedgerEntry entry = createOverpayment(overpayment)
		entry.computeCredit()
		return entry
	}
	
	public static CustomerLedgerEntry createOverpayment(Overpayment overpayment) {
		new CustomerLedgerEntry(
			type: CustomerLedgerEntry.Type.OVERPAYMENT,
			dateOpened: overpayment.directPayment.date,
			datePosted: new Date(),
			details: CustomerLedgerEntry.Type.OVERPAYMENT.toString(),
			referenceId: overpayment.directPayment.toString(),
			amount: overpayment.amount,
			linkId: overpayment.directPayment.id,
			isChild: true
		)
	}

	public static CustomerLedgerEntry createApprovedCustomerCharge(CustomerCharge customerCharge) {
		CustomerLedgerEntry entry = createCustomerCharge(customerCharge)
		entry.type = CustomerLedgerEntry.Type.APPROVED_CUSTOMER_CHARGE
		entry.computeDebit()
		return entry
	}
	
	public static CustomerLedgerEntry createUnapprovedCustomerCharge(CustomerCharge customerCharge) {
		CustomerLedgerEntry entry = createCustomerCharge(customerCharge) 
		entry.type = CustomerLedgerEntry.Type.UNAPPROVED_CUSTOMER_CHARGE
		entry.computeCredit()
		return entry
	}
	
	public static CustomerLedgerEntry createApprovedBouncedCheck(BouncedCheck bouncedCheck) {
		CustomerLedgerEntry entry = new CustomerLedgerEntry(
			type: CustomerLedgerEntry.Type.APPROVED_BOUNCED_CHECK,
			dateOpened: bouncedCheck.date,
			datePosted: new Date(),
			referenceId: bouncedCheck.toString(),
			amount: bouncedCheck.computeTotalAmount(),
			linkId: bouncedCheck.id,
			isChild: false
		)
		entry.computeDebit()
		return entry
	}
    public static CustomerLedgerEntry createUnapprovedBouncedCheck(BouncedCheck bouncedCheck) {
		CustomerLedgerEntry entry = new CustomerLedgerEntry(
			type: CustomerLedgerEntry.Type.UNAPPROVED_BOUNCED_CHECK,
			dateOpened: bouncedCheck.date,
			datePosted: new Date(),
			referenceId: bouncedCheck.toString(),
			amount: bouncedCheck.computeTotalAmount(),
			linkId: bouncedCheck.id,
			isChild: false
		)
		entry.computeCredit()
		return entry
	}
	
	public static CustomerLedgerEntry createApprovedBouncedCheckItem(BouncedCheck bouncedCheck, CheckPayment checkPayment) {
		CustomerLedgerEntry entry = createCheckPayment(checkPayment)
		entry.dateOpened = bouncedCheck.date
		entry.type = CustomerLedgerEntry.Type.BOUNCED_CHECK_ITEM
		entry.referenceId = bouncedCheck.toString()
		entry.linkId = bouncedCheck.id
		entry.details = entry.type.toString() + entry.details 
		entry.isChild = true
		entry.runningBalance = BigDecimal.ZERO
		entry.computeDebit()
		return entry
	}
    public static CustomerLedgerEntry createUnapprovedBouncedCheckItem(BouncedCheck bouncedCheck, CheckPayment checkPayment) {
		CustomerLedgerEntry entry = createCheckPayment(checkPayment)
		entry.dateOpened = bouncedCheck.date
		entry.type = CustomerLedgerEntry.Type.BOUNCED_CHECK_ITEM
		entry.referenceId = bouncedCheck.toString()
		entry.linkId = bouncedCheck.id
		entry.details = entry.type.toString() + entry.details
		entry.isChild = true
		entry.runningBalance = BigDecimal.ZERO
		entry.computeCredit()
		return entry
	}
	
	public static CustomerLedgerEntry createApprovedCheckDepositItem(CheckDeposit checkDeposit, CheckPayment checkPayment) {
		CustomerLedgerEntry entry = createCheckPayment(checkPayment)
		entry.dateOpened = checkDeposit.depositDate
		entry.type = CustomerLedgerEntry.Type.APPROVED_CHECK_DEPOSIT_ITEM
		entry.referenceId = checkDeposit.toString()
		entry.linkId = checkDeposit.id
		entry.computeCredit()
		return entry
	}
	
	public static CustomerLedgerEntry createApprovedDebitMemo(CreditMemo creditMemo) {
		CustomerLedgerEntry entry = createCreditMemo(creditMemo)
		entry.type = CustomerLedgerEntry.Type.APPROVED_DEBIT_MEMO
		entry.amount = creditMemo.computeCreditMemoTotalAmount()
		entry.computeDebit()
		return entry
	}
	
	public static CustomerLedgerEntry createUnapprovedDebitMemo(CreditMemo creditMemo) {
		CustomerLedgerEntry entry = createCreditMemo(creditMemo)
		entry.type = CustomerLedgerEntry.Type.UNAPPROVED_DEBIT_MEMO
		entry.amount = creditMemo.computeCreditMemoTotalAmount()
		entry.computeCredit()
		return entry
	}
	
	public static CustomerLedgerEntry createApprovedCreditMemo(CreditMemo creditMemo) {
		CustomerLedgerEntry entry = createCreditMemo(creditMemo)
		entry.type = CustomerLedgerEntry.Type.APPROVED_CREDIT_MEMO
		entry.amount = creditMemo.computeTotalAmount()
		return entry
	}
	
	public static CustomerLedgerEntry createUnapprovedCreditMemo(CreditMemo creditMemo) {
		CustomerLedgerEntry entry = createCreditMemo(creditMemo)
		entry.type = CustomerLedgerEntry.Type.UNAPPROVED_CREDIT_MEMO
		entry.amount = creditMemo.computeTotalAmount()
		return entry
	}
	
	private static CustomerLedgerEntry createCreditMemo(CreditMemo creditMemo) {
		return new CustomerLedgerEntry(
			dateOpened: creditMemo.date,
			datePosted: new Date(),
			referenceId: creditMemo.toString(),
			linkId: creditMemo.id,
			isChild: false
		)
	}
		
	private static CustomerLedgerEntry createCustomerCharge(CustomerCharge customerCharge) {
		new CustomerLedgerEntry(
			dateOpened: customerCharge.date,
			datePosted: new Date(),
			referenceId: customerCharge.toString(),
			amount: customerCharge.computeTotalAmount(),
			linkId: customerCharge.id,
			isChild: false
		)
	}
	
	private static CustomerLedgerEntry createSalesDelivery(SalesDelivery salesDelivery) {
		new CustomerLedgerEntry(
			dateOpened: salesDelivery.date,
			datePosted: new Date(),
			referenceId: salesDelivery.salesDeliveryId,
			amount: salesDelivery.computeTotalAmount(),
			linkId: salesDelivery.id,
			isChild: false
		)
	}
	
	private static CustomerLedgerEntry createCheckPayment(CheckPayment checkPayment) {
		def dateString = generalMethodService.getDateString(checkPayment.date, "MM/dd/yyyy")
		new CustomerLedgerEntry(
			datePosted: new Date(),
			details: " - " + dateString + " " + checkPayment.bank.identifier + " - " + checkPayment.branch + " - " + checkPayment.checkNumber + " " + checkPayment.type.description,
			amount: checkPayment.amount,
			isChild: false
		)
	}
}