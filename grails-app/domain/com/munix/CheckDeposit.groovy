package com.munix

class CheckDeposit {
    BankAccount account
    Date depositDate
    Boolean billsPurchase
    String preparedBy
    String approvedBy
	String cancelledBy
    String status = "Unapproved"

    static hasMany = [checks:CheckPayment,printLogs:PrintLogCheckDeposit]
	static belongsTo = [CheckPayment]

    static constraints = {
        account(nullable:false)
        depositDate(nullable:false)
        billsPurchase(nullable:true)
        status(nullable:false)
        preparedBy(nullable:false)
        approvedBy(nullable:true)
		cancelledBy(nullable:true)
    }

    String toString(){
        "CD-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }
    
    String formatTotal(){
        "PHP ${String.format( '%,.2f',computeTotal() )}"
    }
    def isCleared(){
        return status=="Cleared"
    }
    def isUnapproved(){
        return status=="Unapproved"
    }
    def isCancelled(){
        return status=="Cancelled"
    }
    void cleared(){
        status="Cleared"
    }
    void unapprove(){
        status = "Unapproved"
    }
	void cancel(){
		status="Cancelled"
	}
    BigDecimal computeTotal(){
        def total = 0
        checks.each{
            total += it.amount
        }
        return total
    }
}
