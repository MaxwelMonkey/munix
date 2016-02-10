package com.munix

abstract class AuditTrail extends Log{
    String previousEntry
    String newEntry
    String fieldName

    static constraints = {
        previousEntry(nullable: false)
        newEntry(nullable: false)
        fieldName(nullable: false)
    }
}
