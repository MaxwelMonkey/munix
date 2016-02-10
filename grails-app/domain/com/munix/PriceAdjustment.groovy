package com.munix

import org.apache.commons.collections.list.LazyList
import org.apache.commons.collections.FactoryUtils

class PriceAdjustment {
	static enum Status {
		UNAPPROVED("Unapproved"), APPROVED("Approved"), CANCELLED("Cancelled"),	APPLIED("Applied")
		
		String name
		
		Status(String name) {
			this.name = name
		}
		
		@Override
		public String toString() {
			return name
		}
	}
	
	Status status = Status.UNAPPROVED
	String preparedBy
	String approvedBy
	String cancelledBy
	String remark
	PriceType priceType
	ItemType itemType
	Date dateGenerated = new Date()
	Date effectiveDate
	transient String priceAdjustmentId
	List items = new ArrayList()
	
	static hasMany = [items: PriceAdjustmentItem]
	
    static constraints = {
		status(nullable: false)
		preparedBy(nullable: false)
		approvedBy(nullable: true)
		cancelledBy(nullable:true)
		remark(nullable:true, maxSize:250)
		priceType(nullable: false)
		itemType(nullable: false)
		dateGenerated(nullable: false)
        effectiveDate(nullable: false)
        items(validator : {val, obj->
            if(obj.items?.isEmpty()) return ['priceAdjustment.items.notEmpty']
        })
    }
	
	static mapping = {
		items cascade: "all, delete-orphan"
	}
	
	@Override
	String toString() {
		"PA-${padId()}"
	}
	
	private String padId() {
		"${id}".padLeft(8,'0')
	}
	
	String getPriceAdjustmentId() {
		return toString()
	}
	
	void cancelled(){
		status = Status.CANCELLED
	}

	void approved(){
		status = Status.APPROVED
	}
	
	void unapproved(){
		status = Status.UNAPPROVED
	}

	def isApplied() {
		return status == Status.APPLIED
	}

	def isCancelled() {
		return status == Status.CANCELLED
	}
	def isUnapproved() {
		return status == Status.UNAPPROVED
	}

	def isApproved() {
		return status == Status.APPROVED
	}

	def getItemList(){
		return LazyList.decorate(items, FactoryUtils.instantiateFactory(PriceAdjustmentItem.class))
	}
}
