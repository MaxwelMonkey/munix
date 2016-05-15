package com.munix

class AuditLogService {

    static transactional = true
    private static final String SO = "Sales Order"

    def log(user, action, recordType, id){
    	new AuditLog(user:user, action:action, recordType: recordType, linkId: id).save()
    }
    
    def so(user, action, id){
    	log(user, action, SO, id)
    }

}
