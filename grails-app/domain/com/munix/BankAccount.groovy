package com.munix

class BankAccount {
    String accountName
    String accountNumber
    Bank bank

    static constraints = {
        accountName(nullable:false,blank:false)
        accountNumber(nullable:false,blank:false, unique:true)
        bank(nullable:true)
    }

    String toString(){
        return bank?.identifier+" - "+accountName+" - "+accountNumber
    }

}

