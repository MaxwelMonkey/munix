package com.munix

class CustomerAccount {

	BigDecimal totalUnpaidSalesDeliveries = 0
	BigDecimal totalUnpaidCustomerCharges = 0
	BigDecimal totalUnpaidDebitMemos = 0
	BigDecimal totalUnpaidBouncedChecks = 0
	BigDecimal totalUnpaidCheckPayments = 0
	
	static belongsTo = [customer: Customer]
	
    static constraints = {
		totalUnpaidSalesDeliveries(nullable: false)
		totalUnpaidCustomerCharges(nullable: false)
		totalUnpaidDebitMemos(nullable: false)
		totalUnpaidBouncedChecks(nullable: false)
		totalUnpaidCheckPayments(nullable: false)
    }

	def computeTotalUnpaidBalance() {
		return totalUnpaidSalesDeliveries + totalUnpaidCustomerCharges + totalUnpaidDebitMemos + totalUnpaidBouncedChecks + totalUnpaidCheckPayments
	}
	
	def isOverLimit() {
		return customer.creditLimit < computeTotalUnpaidBalance() ? true : false
	}
    def addSDAmount(BigDecimal amount){
        totalUnpaidSalesDeliveries+=amount
    }
    def removeSDAmount(BigDecimal amount){
        totalUnpaidSalesDeliveries-=amount
    }
    def addCCAmount(BigDecimal amount){
        totalUnpaidCustomerCharges+=amount
    }
    def removeCCAmount(BigDecimal amount){
        totalUnpaidCustomerCharges-=amount
    }
    def addDMAmount(BigDecimal amount){
        totalUnpaidDebitMemos+=amount
    }
    def removeDMAmount(BigDecimal amount){
        totalUnpaidDebitMemos-=amount
    }
    def addBCAmount(BigDecimal amount){
        totalUnpaidBouncedChecks+=amount
    }
    def removeBCAmount(BigDecimal amount){
        totalUnpaidBouncedChecks-=amount
    }
    def addCPAmount(BigDecimal amount){
        totalUnpaidCheckPayments+=amount
    }
    def removeCPAmount(BigDecimal amount){
        totalUnpaidCheckPayments-=amount
    }
}
