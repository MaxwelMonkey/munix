package com.munix

class CustomerLedger {

	BigDecimal balance = BigDecimal.ZERO
	
    static constraints = {
		customer(nullable: false)
		balance(nullable: false)
    }

	static belongsTo = [customer: Customer]
	static hasMany = [entries: CustomerLedgerEntry]
	
	def updateBalanceAddCredit(BigDecimal credit) {
		this.balance -= credit;
	}
	
	def updateBalanceAddDebit(BigDecimal debit) {
		this.balance += debit;
	}
}