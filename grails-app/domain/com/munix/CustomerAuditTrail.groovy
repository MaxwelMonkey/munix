package com.munix

class CustomerAuditTrail extends AuditTrail {
	Customer customer
	User approvedBy

    static constraints = {
        approvedBy(nullable: true)
    }
}
