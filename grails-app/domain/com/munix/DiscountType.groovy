package com.munix

class DiscountType {
    String identifier
    String description
	Boolean excludeInCommission
	BigDecimal discountedItemMargin = BigDecimal.ONE
	BigDecimal netItemMargin = BigDecimal.ONE
	
    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
		excludeInCommission(nullable:true)
		discountedItemMargin(nullable:true)
		netItemMargin(nullable:true)
    }

    String toString(){
	    "${identifier}"
    }

    boolean equals(o) {
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        DiscountType that = (DiscountType) o;

        if (identifier != that.identifier) return false;

        return true;
    }

    int hashCode() {
        return (identifier != null ? identifier.hashCode() : 0);
    }
}
