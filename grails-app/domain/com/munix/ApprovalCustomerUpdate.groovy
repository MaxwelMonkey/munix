package com.munix

class ApprovalCustomerUpdate {

	ApprovalProcess approvalProcess
	Customer customer
    BigDecimal creditLimit
    Boolean enableCreditLimit
    Term term
    Boolean autoApprove
    Boolean autoSecondApprove
    String status
    
    static belongsTo = ApprovalProcess

    static constraints = {
        customer(nullable:false)
        creditLimit(nullable:true)
        enableCreditLimit(nullable:true)
        term(nullable:true)
        autoApprove(nullable:true)
        autoSecondApprove(nullable:true)
        status(nullable:true)
    }
    
    public String toString() {
    	def result = ""
    	if(creditLimit) result+="Credit Limit"
    	if(enableCreditLimit){
    		if(result!="") result+=", "
    		result+="Enable Credit Limit"
    	} 
    	if(term){
    		if(result!="") result+=", "
    		result+="Term"
    	} 
    	if(autoApprove){
    		if(result!="") result+=", "
    		result+="Auto Approve"
    	} 
    	if(autoSecondApprove){
    		if(result!="") result+=", "
    		result+="Auto Second Approve"
    	} 
    	if(status){
    		if(result!="") result+=", "
    		result+="Status"
    	}
    	return result 
    }
}
