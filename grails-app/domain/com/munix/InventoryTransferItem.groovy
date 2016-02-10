package com.munix

class InventoryTransferItem {
    Product product
    BigDecimal qty
	BigDecimal originWarehouseQty
	BigDecimal destinationWarehouseQty
	boolean isDeleted
	
	static transients = ['isDeleted']
    static belongsTo = [transfer:InventoryTransfer]

    static constraints = {
        product(nullable:false)
        qty(nullable:false,min:new BigDecimal("1"))
		originWarehouseQty(nullable:true)
		destinationWarehouseQty(nullable:true)
        transfer(nullable:true)
    }

    String toString(){
        product
    }

    String formatQty(){
        "${String.format('%,.2f',qty)}"
    }

    String formatReleasedQty(){
        "${String.format('%,.2f',releasedQty)}"
    }

    String formatReceivedQty(){
        "${String.format('%,.2f',receivedQty)}"
    }
}
