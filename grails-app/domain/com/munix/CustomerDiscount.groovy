package com.munix

class CustomerDiscount {
    static enum Type {
        DISCOUNT("Discount"), NET("Net")

        String name

        Type(String name) {
            this.name = name
        }

        @Override
        public String toString() {
            return name
        }
		
		public static Type getTypeByName(String name) {
			return Type.values().find { it.toString().equalsIgnoreCase name}
		}
    }

    DiscountType discountType
    DiscountGroup discountGroup
    Type type
	CustomerDiscountLog log

    static belongsTo = [customer:Customer]

    static constraints = {
        type(nullable:false)
        discountType(nullable:false)
        discountGroup(nullable:false)
        customer(nullable:false)
		log(nullable: true)
    }
}
