package com.munix

class CustomerChargeItem {
	Date date
    Charge charge
    String reference
	String remark
    BigDecimal amount = 1

    static belongsTo = [customerCharge:CustomerCharge]

    static constraints = {
        charge(nullable:false)
        amount(nullable:false, min:new BigDecimal("1"))
        customerCharge(nullable:true)
        reference(nullable:true)
		remark(nullable: true)
		date(nullable: true)
    }
}
