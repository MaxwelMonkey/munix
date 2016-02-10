package com.munix

import com.munix.PrintLogSalesDelivery.Type;

class PrintLogPurchaseOrder extends Log{
	static enum Type {
		WITHPRICE("With Price"), NOPRICE("No Price"), NOTYPE("No Type")

		String name

		Type(String name) {
			this.name = name
		}

		@Override
		public String toString() {
			return name
		}
	}
	Type type
    PurchaseOrder purchaseOrder
    static constraints = {
    }
}
