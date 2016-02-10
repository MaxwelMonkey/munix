package com.munix

class DiscountGroup {
    String identifier
    String description
    BigDecimal rate

    static constraints = {
        identifier(nullable:false,unique:true)
        description(nullable:false)
        rate(nullable:false,scale:4)
    }

    String toString() {
        identifier
    }

    String formatRate(){
        "${String.format('%,.4f',rate)}%"
    }

    boolean equals(object) {
        if (this.is(object)) return true;
        if (getClass() != object.class) return false;
        if (!super.equals(object)) return false;

        DiscountGroup that = (DiscountGroup) object;

        if (identifier != that.identifier) return false;

        return true;
    }

    int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        return result;
    }
}
