package com.munix

class Customer {
    static enum Status {
		ACTIVE("Active"), INACTIVE("Inactive"), ONHOLD("On Hold"),	BADACCOUNT("Bad Account")

		String name

		Status(String name) {
			this.name = name
		}

		@Override
		public String toString() {
			return name
		}

        public static Status getStatusByName(String name) {
            return Status.values().find { it.toString().equalsIgnoreCase name}
        }
	}

    String identifier
    String name
    String busAddrStreet
    City busAddrCity
    String busAddrZip
    String bilAddrStreet
    City bilAddrCity
    String bilAddrZip
    String skype
    String email
    String website
    String landline
    String fax
    String contact
    String mobile
    BigDecimal declaredValue = 0 //in percentage
    CustomerType type
    Term term
    Status status //Bad Account, Hold
    Forwarder forwarder
    SalesAgent salesAgent
    Boolean autoApprove = false
    Boolean autoSecondApprove = false
    Boolean enableCreditLimit
    BigDecimal commissionRate = 0 //in percentage
    BigDecimal creditLimit = 0
    Bank bank1
    String branch1
    String accountName1
	String accountNumber1
	Bank bank2
	String branch2
	String accountName2
	String accountNumber2
	Bank bank3
	String branch3
	String accountName3
	String accountNumber3
    String tin
    String collectionMode
    String collectionPreference
    String collectionSchedule
    String generalRemark
    String creditRemark
    String contactPosition
    String owner
    String yahoo
    String secondaryContact
    String secondaryContactPosition
    CustomerLedger customerLedger
    CustomerAccount customerAccount
    Boolean managerSecondApprove = false

	static transients = ['remainingCredit']
    static hasMany = [statusLogs:CustomerLog, invoices:SalesOrder, deliveries:SalesDelivery, bouncedChecks:BouncedCheck, counterReceipts:CounterReceipt, charges:CustomerCharge, discounts:CustomerDiscount, auditTrails:CustomerAuditTrail]
    
    static constraints = {
        identifier(nullable:false,blank:false,unique:true)
        name(nullable:false,blank:false)
        busAddrStreet(nullable:true)
        busAddrCity(nullable:true)
        busAddrZip(nullable:true)
        bilAddrStreet(nullable:false,blank:false)
        bilAddrCity(nullable:false,blank:false)
        bilAddrZip(nullable:true)
        skype(nullable:true)
        email(nullable:true,email:true)
        website(nullable:true,url:true)
        term(nullable:false)
        landline(nullable:true)
		fax(nullable:true)
        contact(nullable:true)
        mobile(nullable:true)
        forwarder(nullable:true)
        salesAgent(nullable:true)
        status(nullable:false)
        type(nullable:false)
        declaredValue(nullable:false, min:new BigDecimal("0"))
        autoApprove(nullable:false)
        commissionRate(nullable:false)
        creditLimit( nullable:false,min:new BigDecimal("0") )
        generalRemark(nullable:true)
        bank1(nullable:true)
        branch1(nullable:true)
        accountName1(nullable:true)
		accountNumber1(nullable:true)
		bank2(nullable:true)
		branch2(nullable:true)
		accountName2(nullable:true)
		accountNumber2(nullable:true)
		bank3(nullable:true)
		branch3(nullable:true)
		accountName3(nullable:true)
		accountNumber3(nullable:true)
        tin(nullable:true)
        collectionMode(nullable:true)
        collectionPreference(nullable:true)
        collectionSchedule(nullable:true)
        creditRemark(nullable:true)
        contactPosition(nullable:true)
        owner(nullable:true)
        yahoo(nullable:true)
        secondaryContact(nullable:true)
        secondaryContactPosition(nullable:true)
        enableCreditLimit(nullable:true)
        customerLedger(nullable:true)
        customerAccount(nullable:true)
        managerSecondApprove(nullable:true)
    }

    String toString(){
        "${identifier} - ${name}"
    }

	BigDecimal getRemainingCredit() {
		return computeRemainingCredit()
	}
	
    String formatId(){
         "${id}".padLeft(4,'0')
    }

    String formatBusinessAddress(){
        def address = ""

        if(busAddrStreet){
            address = "${busAddrStreet}, "
        }

        if(busAddrCity){
            address = "${address}${busAddrCity}, ${busAddrCity.province.region}"
        }

        if(busAddrZip){
            address = "${address}, ${busAddrZip}"
        }

        return address

    }

    String formatBillingAddress(){
        def address = ""

        if(bilAddrStreet){
            address = "${bilAddrStreet}, "
        }

        if(bilAddrCity){
            address = "${address}${bilAddrCity}, ${bilAddrCity.province.region}"
        }

        if(bilAddrZip){
            address = "${address}, ${bilAddrZip}"
        }

        return address
    }

    String formatDeclaredValue(){
        "${String.format( '%,.2f',declaredValue )}%"
    }

    String formatCommissionRate(){
        "${String.format( '%,.2f',commissionRate )}%"
    }

    String formatCreditLimit(){
        "PHP ${String.format( '%,.2f',creditLimit )}"
    }
    DiscountGroup getDiscountGroup(DiscountType discountType){
        return discounts.find{discountType.equals(it?.discountType)}?.discountGroup
    }
    void active(){
        status=Status.getStatusByName("Active")
    }
    void onHold(){
        status=Status.getStatusByName("On Hold")
    }
    void inactive(){
        status=Status.getStatusByName("Inactive")
    }
    void badAccount(){
        status=Status.getStatusByName("Bad Account")
    }
    def isActive(){
        return status==Status.getStatusByName("Active")
    }
    def isOnHold(){
        return status==Status.getStatusByName("On Hold")
    }
    def isInactive(){
        return status==Status.getStatusByName("Inactive")
    }
    def isBadAccount(){
        return status==Status.getStatusByName("Bad Account")
    }
	
	BigDecimal computeRemainingCredit() {
		return creditLimit - customerLedger.balance 
	}
    Boolean remainingCreditIsNegative(){
        return remainingCredit.signum() == -1
    }
}
