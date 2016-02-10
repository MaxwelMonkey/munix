package com.munix

class SupplierItem {
    static enum Status {
		ACTIVE("Active"),
		INACTIVE("Inactive")

		String description
        Status(String description){
            this.description = description
        }
		@Override
		public String toString() {
			return description
		}
        public static Status getTypeByName(String name) {
            return Status.values().find { it.toString().equalsIgnoreCase name}
        }
        String getKey() { name() }
	}
    String productCode
    Product product
    Status status = Status.ACTIVE
    boolean isDeleted

	static transients = [ 'isDeleted' ]
    static belongsTo = [supplier:Supplier]

    static constraints = {
        productCode(nullable:true)
        product(nullable:false)
        supplier(nullable:true)
    }

    String toString() {
        "[${productCode}] ${product.description}"
    }
}