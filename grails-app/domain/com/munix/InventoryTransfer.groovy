package com.munix

import org.apache.commons.collections.list.LazyList
import org.apache.commons.collections.FactoryUtils

import com.munix.JobOrder.Status;

class InventoryTransfer {
	static enum Status {
		UNAPPROVED("Unapproved"), RECEIVED("Received"), CANCELLED("Cancelled")
		
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
    Warehouse originWarehouse
    Warehouse destinationWarehouse
    Status status = Status.UNAPPROVED
    String remark
    String preparedBy
    String receivedBy
	String cancelledBy
	List items = []

    static hasMany = [items:InventoryTransferItem]

    static constraints = {
        date(nullable:false)
        originWarehouse(nullable:false)
        destinationWarehouse(nullable:false)
        status(nullable:false)
        remark(nullable:true, maxSize:255)
        preparedBy(nullable:false)
        receivedBy(nullable:true)
		cancelledBy(nullable:true)
		items(validator : {val, obj->
			if(obj.items?.isEmpty()) return ['inventoryTransfer.items.notEmpty']
		})
    }

	static mapping = {
		items cascade: "all, delete-orphan"
	}

    String toString(){
        "ST-${formatId()}"
    }

    String formatId(){
        "${id}".padLeft(8,'0')
    }

	void cancel(){
		status = Status.CANCELLED
	}

	void received(){
		status = Status.RECEIVED
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

	def isReceived() {
		return status == Status.RECEIVED
	}
	
	def getItemList(){
		return LazyList.decorate(items, FactoryUtils.instantiateFactory(InventoryTransferItem.class))
	}

}
