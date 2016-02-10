package com.munix

class WaybillPackage {
    String description
    Packaging packaging
    BigDecimal qty

    static belongsTo = [waybill:Waybill]

    static constraints = {
        description(nullable:true)
        packaging(nullable:false)
        qty(nullable:false,blank:false,min:new BigDecimal("0"))
    }

    String formatQty(){
        "${String.format( '%,.0f',qty )}"
    }
	
	String toString() {
		packaging
	}
}
