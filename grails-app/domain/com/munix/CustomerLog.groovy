package com.munix

class CustomerLog extends Log{
    Customer customer
    String previous
    String current
    CustomerLog(){}
    CustomerLog(status, user,customer){
        this.user=user
        this.customer=customer
        this.previous=customer.status
        this.current=status
    }
    static constraints = {
    }
}
