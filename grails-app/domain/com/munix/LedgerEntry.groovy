package com.munix

class LedgerEntry {
    String reference
    String type
    BigDecimal debit = 0
    BigDecimal credit = 0
    BigDecimal balance = 0
    Date date = new Date()

    static belongsTo = [customer:Customer]

    static constraints = {
        reference(nullable:true)
        type(nullable:true)
        customer(nullable:false)
        debit(nullable:false)
        credit(nullable:false)
        balance(nullable:false)
        date(nullable:false)
    }
}
