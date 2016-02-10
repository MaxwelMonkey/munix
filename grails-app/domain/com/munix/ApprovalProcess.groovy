package com.munix

class ApprovalProcess {
    String description
    User requestedBy
    Date date
    String type
    Integer referenceNumber
    String status = "Unapproved"
    String remarks
    User approvedBy
    Date approvedDate

    static constraints = {
        description(nullable:false, blank:false)
        requestedBy(nullable:false)
        date(nullable:false, blank:false)
        type(nullable:true, blank:true)
        referenceNumber(nullable:true, blank:true)
        status(nullable:false, blank:false)
        remarks(nullable:true, blank:true)
        approvedBy(nullable:true)
        approvedDate(nullable:true, blank:true)
    }
    
    String toString() {
        return description
    }
}
