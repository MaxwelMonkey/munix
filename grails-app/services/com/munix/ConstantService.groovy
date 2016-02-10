package com.munix

class ConstantService {
    public static final String PURCHASE_ORDER_APPROVED = "Approved"
    public static final String PURCHASE_ORDER_COMPLETE = "Complete"
    public static final String PURCHASE_ORDER_UNAPPROVED = "Unapproved"
	public static final String PURCHASE_ORDER_CANCEL = "Cancelled"

    public static final String PURCHASE_DELIVERY_APPROVED = "Approved"
    public static final String PURCHASE_DELIVERY_UNAPPROVED = "Unapproved"

    public static final String PURCHASE_INVOICE_APPROVED = "Approved"
    public static final String PURCHASE_INVOICE_UNAPPROVED = "Unapproved"
    public static final String PURCHASE_INVOICE_CANCELLED = "Cancelled"
    public static final String PURCHASE_INVOICE_PAID = "Paid"

    public static final String DIRECT_PAYMENT_APPROVE = "Approved"
	public static final String DIRECT_PAYMENT_UNAPPROVE = "Unapproved"
	public static final String DIRECT_PAYMENT_CANCEL = "Cancelled"

    public static final String SALES_INVOICE_APPROVED = "Approved"
    public static final String SALES_INVOICE_UNAPPROVED = "Unapproved"
    public static final String SALES_INVOICE_SECOND_APPROVAL = "Second Approval Pending"
    public static final String SALES_INVOICE_COMPLETE = "Complete"
    public static final String SALES_INVOICE_CANCEL = "Cancelled"
    
    public static final String SALES_DELIVERY_APPROVED = "Unpaid"
	public static final String SALES_DELIVERY_PAID = "Paid"
	public static final String SALES_DELIVERY_TAKEN = "Taken"
    public static final String SALES_DELIVERY_UNAPPROVED = "Unapproved"
    public static final String SALES_DELIVERY_CANCEL = "Cancelled"
    public static final String SALES_DELIVERY_CLOSED = "Closed"

    public static final String CHECK_COUNTER_RECEIPT_TYPE = "Counter Receipt"

    public static final String COUNTER_RECEIPT_COUNTERED = "Countered"
    public static final String COUNTER_RECEIPT_PAID = "Paid"

    public static final String JOB_ORDER_OUT_APPROVED = "Approved"
    public static final String JOB_ORDER_COMPLETE = "Complete"
    public static final String JOB_ORDER_APPROVE = "Requisition Pending"
    public static final String JOB_ORDER_PROCESSING = "Processing Assembly"

    public static final String MATERIAL_RELEASE_APPROVED = "Approved"
    public static final String MATERIAL_REQUISITION_COMPLETE = "Complete"

    public static final String INVENTORY_TRANSFER_RECEIVE = "Complete"

    public static final String BOUNCED_CHECK_APPROVED = "Approved"
	public static final String BOUNCED_CHECK_PAID = "Paid"
	public static final String BOUNCED_CHECK_TAKEN = "Taken"

    public static final String CHECK_PAYMENT_BOUNCED = "Bounced"
    public static final String CHECK_PAYMENT_DEPOSITED = "Deposited"
    public static final String CHECK_PAYMENT_POSTDATED = "Post-dated"

    public static final String CUSTOMER_CHARGE_APPROVED = "Unpaid"
	public static final String CUSTOMER_CHARGE_PAID = "Paid"
	public static final String CUSTOMER_CHARGE_TAKEN = "Taken"
    public static final String CUSTOMER_CHARGE_UNAPPROVED = "Unapproved"

    public static final String COUNTER_RECEIPT_APPROVED = "Approved"
    public static final String COUNTER_RECEIPT_CANCEL = "Cancelled"
    public static final String COUNTER_RECEIPT_COMPLETE = "Complete"

    public static final String COLLECTION_SCHEDULE_COMPLETE = "Complete"

    public static final String CHECK_DEPOSIT_APPROVED = "Approved"
    
	public static final String CREDIT_MEMO_APPROVED = "Approved"
	public static final String CREDIT_MEMO_PAID = "Paid"
	public static final String CREDIT_MEMO_TAKEN = "Taken"
    public static final String CREDIT_MEMO_CANCEL = "Cancelled"


    public static final String SUPPLIER_PAYMENT_APPROVED = "Approved"
	public static final String SUPPLIER_PAYMENT_CANCELLED = "Cancelled"

    public static final String CHECK_WAREHOUSING_APPROVED = "Approved"

    public static final String TRIP_TICKET_COMPLETE = "Complete"
	public static final String TRIP_TICKET_PROCESSING = "Processing"
	public static final String TRIP_TICKET_CANCELLED = "Cancelled"
	

    boolean transactional = true

    def serviceMethod() {

    }
}
