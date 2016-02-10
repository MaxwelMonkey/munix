package com.munix

class SupplierPaymentItem {
    PaymentType type
    BigDecimal amount
	Date date
    String checkNumber
    Bank checkBank
	String checkBranch
    CheckType checkType
	String remark
    
    static belongsTo = [payment:SupplierPayment]

    static constraints = {
        type(nullable:false)
        amount(nullable:false,blank:false,min:new BigDecimal("0"))
		date(nullable: false)
        checkNumber(nullable:true, validator: {val, obj ->
			if (obj.type?.isCheck) {
				if (null == val) {
					return ['invalid.check.field']
				}
			}
        })
        checkBranch(nullable:true, validator: {val, obj ->
			if (obj.type?.isCheck) {
				if (null == val) {
					return ['invalid.check.field']
				}
			}
        })
        checkType(nullable:true, validator: {val, obj ->
			if (obj.type?.isCheck) {
				if (null == val) {
					return ['invalid.check.field']
				}
			}
        })
        checkBank(nullable:true, validator: {val, obj ->
			if (obj.type?.isCheck) {
				if (null == val) {
					return ['invalid.check.field']
				}
			}
        })
		remark(nullable:true)
    }

    String formatAmount(){
        "${String.format( '%,.2f',amount )}"
    }
	
	String formatCheckBankAndBranch() {
        def bankString = ""
        if(type.isCheck){
            bankString = (checkBank.identifier?:"") +" - " + (checkBranch?:"")
        }
		return bankString
	}
}
