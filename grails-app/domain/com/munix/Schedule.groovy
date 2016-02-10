package com.munix

class Schedule {

    Date startDate
    Date endDate
    String status = "Pending"
    String preparedBy
    static hasMany =[jobOrders:JobOrder]

    static constraints = {
        startDate(nullable:false)
        endDate(nullable:false)
        status(nullable:false)
        preparedBy(nullable:false)
    }

    String toString(){
       "SC-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    String formatCompletion(){
        "${String.format( '%,.2f',computeCompletion() )}%"
    }

    BigDecimal computeCompletion(){
        ( computeRemainingBalanceTotal()/computeQtyTotal() ) * 100
    }

    BigDecimal computeQtyTotal(){
        def total = 0
        jobOrders?.each{
            total += it.qty
        }
        return total
    }

    BigDecimal computeRemainingBalanceTotal(){
        def total = 0
        jobOrders?.each{
            total += it.computeRemainingBalance()
        }
        return total
    }
}
