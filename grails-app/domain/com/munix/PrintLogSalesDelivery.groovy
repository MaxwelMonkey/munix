package com.munix

class PrintLogSalesDelivery extends Log{
	static enum Type {
		INVOICE("Invoice"), PACKINGLIST("Packing List"), NOTYPE("No Type")

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
    SalesDelivery salesDelivery
    User approvedBy
    Date approvedDate
	
    static constraints = {
        approvedBy(nullable:true)
        approvedDate(nullable:true, blank:true)
    }
}
