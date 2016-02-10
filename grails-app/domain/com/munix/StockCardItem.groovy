package com.munix

import com.munix.CustomerLedgerEntry.Type;

class StockCardItem {
	static enum Type {
		APPROVED_SALES_DELIVERY("Sales Delivery"),
		UNAPPROVED_SALES_DELIVERY("Sales Delivery - Unapproved"),
		APPROVED_PURCHASE_INVOICE("Purchase Invoice"),
		UNAPPROVED_PURCHASE_INVOICE("Purchase Invoice - Unapproved"),
        APPROVED_MATERIAL_RELEASE("Material Release"),
		UNAPPROVED_MATERIAL_RELEASE("Material Release - Unapproved"),
		APPROVED_CREDIT_MEMO("Credit Memo"),
		UNAPPROVED_CREDIT_MEMO("Credit Memo - Unapproved"),
		APPROVED_DEBIT_MEMO("Debit Memo"),
		UNAPPROVED_DEBIT_MEMO("Debit Memo - Unapproved"),
		APPROVED_JOB_OUT("Job Out"),
		UNAPPROVED_JOB_OUT("Job Out - Unapproved"),
		APPROVED_INVENTORY_TRANSFER("Inventory Transfer"),
		APPROVED_INVENTORY_ADJUSTMENT("Inventory Adjustment"),
		INITIAL_ENTRY("Initial Item")
		
		String type
		
		Type(String type) {
			this.type = type
		}
		
		@Override
		public String toString() {
			return type
		}
	}
	
	Date dateOpened
	Date datePosted
	Type type
	String referenceId
	String supplierCustomer
	BigDecimal costForeign
	BigDecimal costLocal
	BigDecimal sellingAmount
	String warehouseIn
	BigDecimal qtyIn
	String warehouseOut
	BigDecimal qtyOut
    BigDecimal balance
	CurrencyType currencyForeign
    Long linkId

	static belongsTo = [stockCard: StockCard]
	
    static constraints = {
		dateOpened(nullable: true)
		datePosted(nullable: true)
		type(nullable: false)
		referenceId(nullable: true)
		supplierCustomer(nullable: true)
		costForeign(nullable: true)
		costLocal(nullable: true)
		sellingAmount(nullable: true)
		warehouseIn(nullable: true)
		qtyIn(nullable: true)
		warehouseOut(nullable: true)
		qtyOut(nullable: true)
    	balance(nullable: false)
    	linkId(nullable: true)
		currencyForeign(nullable:true)
    }
	
	String formatCostForeign() {
		if(costForeign){
			if(currencyForeign) {
				"${currencyForeign} ${String.format('%,.2f',costForeign)}"
			} else {
				"${String.format('%,.2f',costForeign)}"
			}
		}
	}
}
