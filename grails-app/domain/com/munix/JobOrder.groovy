package com.munix

class JobOrder {
	static enum Status {
		UNAPPROVED("Unapproved"), JOB_ORDER_APPROVED("Job Order Approved"),	MATERIAL_RELEASES_APPROVED("Material Releases Approved"), COMPLETED("Completed"),CANCELLED("Cancelled")

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

    Product product
    Date startDate
    Date endDate
    Date targetDate
    Integer qty
    Assembler assignedTo
    Status status = Status.UNAPPROVED
    String preparedBy
    String approvedBy
	String cancelledBy
    String materialReleasesApprovedBy
    String markAsCompleteBy
    String remark
	static transients = [ 'cost' ]

    static hasOne = [requisition: MaterialRequisition]
    static hasMany = [jobOuts: JobOut, releases: MaterialRelease]

    static constraints = {
        requisition(nullable:true)
        product(nullable:false)
        startDate(nullable:false)
        endDate(nullable:true)
        targetDate(nullable:false)
        qty(nullable:false,min:1,blank:false)
        assignedTo(nullable:false)
        status(nullable:false)
        preparedBy(nullable:false)
        approvedBy(nullable:true)
		cancelledBy(nullable:true)
        materialReleasesApprovedBy(nullable:true)
        remark(nullable:true)
		releases(nullable:true)
        markAsCompleteBy(nullable: true)
    }
	
	BigDecimal getCost() {
		return computeCost()
	}
	
	private BigDecimal computeCost() {
		def cost = 0
		releases.each {
			cost += it.cost
		}
		return cost
	}
	
	void unapprove(){
		status = Status.UNAPPROVED
	}

	void approveJobOrder(){
		status = Status.JOB_ORDER_APPROVED
	}

	void approveMaterialReleases(){
		status = Status.MATERIAL_RELEASES_APPROVED
	}
	
	void unapproveMaterialReleases(){
		status = Status.JOB_ORDER_APPROVED
	}

	void complete(){
		status = Status.COMPLETED
	}

	void cancel(){
		status = Status.CANCELLED
	}

	def isUnapproved(){
		return status == Status.UNAPPROVED
	}

	def isJobOrderApproved(){
		return status == Status.JOB_ORDER_APPROVED
	}

	def isMaterialReleasesApproved(){
		return status == Status.MATERIAL_RELEASES_APPROVED
	}

	def isCompleted(){
		return status == Status.COMPLETED
	}

	def isCancelled(){
		return status == Status.CANCELLED
	}
	
	def isReadyForMaterialReleasesApproval() {
		hasAllRequisitonItemsReleased() && hasAllMaterialReleasesApproved() && !materialReleasesApprovedBy
	}
	
	def hasAllMaterialReleasesApproved() {
		def result = false
		if (releases && !releases.isEmpty()) {
			result = true
			releases.each {
				if (it.isUnapproved()) {
					result = false
				}
			}
		}
		return result
	}

    String toString() {
        "JO-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

    String formatQty(){
        qty
    }

    String formatOutTotal(){
        "${String.format( '%,.0f',computeOutTotal() )}"
    }

    String formatCompletion(){
        "${String.format( '%,.2f',computeCompletion() )}%"
    }

    BigDecimal computeOutTotal(){
        def total = 0
        jobOuts?.each{
            if(!it?.isCancelled()){
                total += it?.qty ?: BigDecimal.ZERO
            }
        }
        return total
    }

    BigDecimal computeRemainingBalance(){
        qty - computeOutTotal()
    }

    BigDecimal computeCompletion(){
        (computeRemainingBalance()/qty) * 100
    }
	
    BigDecimal computeRequisitionItemRemainingBalance(MaterialRequisitionItem materialRequisitionItem){
        materialRequisitionItem.computeQuantity() - computeReleasesItemTotal(materialRequisitionItem.component)
    }
    BigDecimal computeProjectedRequisitionItemRemainingBalance(MaterialRequisitionItem materialRequisitionItem){
        materialRequisitionItem.computeQuantity() - computeProjectedReleasesItemTotal(materialRequisitionItem.component)
    }

    BigDecimal computeReleasesItemTotal(Product component){
        def total = 0
        releases?.each{
            if(it.isApproved()){
				def item = it?.items.find{component == it?.materialRequisitionItem.component}
				total += item?.qty? item.qty : 0
            }
        }
        return total
    }
    BigDecimal computeProjectedReleasesItemTotal(Product component){
        def total = 0
        releases?.each{
            if(!it.isCancelled()){
				def item = it?.items.find{component == it?.materialRequisitionItem.component}
				total += item?.qty? item.qty : 0
            }
        }
        return total
    }

	Boolean hasAllRequisitonItemsReleased() {
		def result = true
		requisition?.items.each {
			if (computeRequisitionItemRemainingBalance(it) != 0) {
				result = false
			}
		}
		return result
	}
	Boolean hasAllJobOutCancelled(){
        def result = true
		jobOuts?.each {
			if (!it.isCancelled()) {
				result = false
			}
		}
		return result
    }
	Boolean hasNoActiveReleases() {
		def result = true
			releases?.each{
				if(!it.isCancelled()){
					result = false
				}
			}
		return result
	}
}
