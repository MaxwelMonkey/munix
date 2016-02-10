package com.munix

class BankBranch {
    String identifier
    String description

    static belongsTo = [bank:Bank]

    static constraints = {
        identifier(nullable:false, blank:false, unique:true)
        description(nullable:false, blank:false)
        bank(nullable:true)
    }

    String toString() {
        "${bank} - ${identifier}"
    }
}

