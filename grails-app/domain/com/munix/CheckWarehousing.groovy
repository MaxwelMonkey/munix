package com.munix

class CheckWarehousing {
    Date date = new Date()
    CheckWarehouse originWarehouse
    CheckWarehouse destinationWarehouse
    String preparedBy
    String approvedBy
	String remark
    String status = "Unapproved"

    static hasMany = [checks:CheckPayment]

    static constraints = {
        date(nullable: false)
        originWarehouse(nullable: false)
        destinationWarehouse(nullable: false)
        status(nullable: false)
        preparedBy(nullable: false)
        approvedBy(nullable: true)
		remark(nullable: true)
    }

    String toString(){
        "CW-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    String formatTotal(){
        "PHP ${String.format( '%,.2f',computeTotal() )}"
    }

    BigDecimal computeTotal(){
        def total = 0
        checks.each{
            total += it.amount
        }
        return total
    }
    Boolean isUnapproved(){
        status == "Unapproved"
    }
    Boolean isApproved(){
        status == "Approved"
    }
}
