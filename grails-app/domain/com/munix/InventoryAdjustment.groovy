package com.munix

import org.apache.commons.collections.list.LazyList
import org.apache.commons.collections.FactoryUtils

class InventoryAdjustment {
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
	}

	Status status = Status.UNAPPROVED
	Warehouse warehouse
	ItemType itemType
	String inventoryStatus
	String remark
	String preparedBy
	String approvedBy
	String cancelledBy
	Date dateGenerated = new Date()
	List items = []
	
    static constraints = {
		status(nullable: false)
		preparedBy(nullable: false)
		approvedBy(nullable: true)
		cancelledBy(nullable:true)
		remark(nullable:true, maxSize:255)
		itemType(nullable: false)
		inventoryStatus(nullable: false)
		dateGenerated(nullable: false)
		items(validator : {val, obj->
			if(obj.items?.isEmpty()) return ['inventoryAdjustment.items.notEmpty']
		})
    }
	
	static hasMany = [items : InventoryAdjustmentItem]
	
	static mapping = {
		items cascade: "all, delete-orphan"
	}
	
	void cancel(){
		status = Status.CANCELLED
	}

	void approved(){
		status = Status.APPROVED
	}
	
	void unapproved(){
		status = Status.UNAPPROVED
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
		return LazyList.decorate(items, FactoryUtils.instantiateFactory(InventoryAdjustmentItem.class))
	}
	
	String toString(){
		"IA-${formatId()}"
	}
	String formatId(){
		"${id}".padLeft(8,'0')
	}
}
