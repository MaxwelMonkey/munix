package com.munix

class JobOut {
    static enum Status {
		UNAPPROVED("Unapproved"), APPROVED("Approved"), CANCELLED("Cancelled")

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
    Date date = new Date()
    Warehouse warehouse
    Integer qty
	LaborCost laborCost
    String preparedBy
    String approvedBy
	String cancelledBy
    Status status = Status.UNAPPROVED
    boolean isDeleted
    static transients = [ 'isDeleted' ]

    static belongsTo = [jobOrder: JobOrder]

    static constraints = {
        date(nullable: false)
        warehouse(nullable: false)
        qty(nullable: false, min: 1)
        laborCost(nullable: false)
        preparedBy(nullable: false)
        approvedBy(nullable: true)
		cancelledBy(nullable:true)
        status(nullable: false)
        jobOrder(nullable: false)
    }

    String toString() {
        "JT-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    void approve(){
        status = Status.APPROVED
    }
    void unapprove(){
        status = Status.UNAPPROVED
    }
    void cancel(){
        status = Status.CANCELLED
    }
    Boolean isApproved(){
        status == Status.APPROVED
    }
    Boolean isUnapproved(){
        status == Status.UNAPPROVED
    }
    Boolean isCancelled(){
        status == Status.CANCELLED
    }
    
    def computeTotalCost(){
		def laborCostAmount = this?.laborCost?.amount ?: BigDecimal.ZERO
		def componentsCost =  this?.jobOrder?.requisition?.computeTotalCostOfMaterialsPerAssembly() ?: BigDecimal.ZERO
		def totalCostPerAssembly = laborCostAmount + componentsCost
		def jobOutTotalCost = totalCostPerAssembly * this?.qty
    }
}
