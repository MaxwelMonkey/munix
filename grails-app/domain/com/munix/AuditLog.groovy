package com.munix

class AuditLog extends Log{
    
    String action
    String recordType
    int linkId

    static constraints = {
        action(nullable: false)
        recordType(nullable:true)
        linkId(nullable: true)
    }
}
