package com.munix

class CheckStatus {
    String identifier
    String description
    static constraints = {
        identifier(nullable: false)
        description(nullable: false)
    }
}
