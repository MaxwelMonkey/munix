package com.munix

import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.FactoryUtils;

class MaterialRelease {
    static enum Status {
		UNAPPROVED("Unapproved"), APPROVED("Approved"), CANCELLED("Cancelled"), SECOND_APPROVED("Second Approved")

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
    String preparedBy
    String approvedBy
	String cancelledBy
    Status status = Status.UNAPPROVED
    Warehouse warehouse
	List items = new ArrayList()
	static transients = [ 'cost' ]

    static belongsTo = [jobOrder:JobOrder]
    static hasMany = [items:MaterialReleaseItem]

    static constraints = {
        date(nullable:false)
        status(nullable:false)
        approvedBy(nullable:true)
        preparedBy(nullable:false)
		cancelledBy(nullable:true)
        jobOrder(nullable:false)
        warehouse(nullable:false)
        items(cascade:"all-delete-orphan")
    }

	BigDecimal getCost() {
		return computeCost()
	}
	
	private BigDecimal computeCost() {
		def cost = 0
		items.each {
			cost += it.totalCost
		}
		return cost
	}
	
    String toString() {
        "ML-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

	void approve() {
		status = Status.APPROVED
	}

	void secondApprove() {
		status = Status.SECOND_APPROVED
	}
	
    void unapprove() {
		status = Status.UNAPPROVED
	}
	
	void unapproveSecondApproval() {
		status = Status.APPROVED
	}
	
	void cancel() {
		status = Status.CANCELLED
	}	

	def isApproved() {
		return Status.APPROVED==status
	}

	def isSecondApproved() {
		return Status.SECOND_APPROVED==status
	}
	
	def isUnapproved() {
		return Status.UNAPPROVED==status
	}

    def isCancelled() {
		return Status.CANCELLED==status
	}
	
	def getReleaseItemList(){
		return LazyList.decorate(items,FactoryUtils.instantiateFactory(MaterialReleaseItem.class))
   }
}
