package com.munix

class PurchaseOrderItem {
    Product product
    BigDecimal qty
    BigDecimal price = 0
    BigDecimal finalPrice
    BigDecimal receivedQty = 0
    Boolean isComplete
	String productCode
    List receivedItems = new ArrayList()
	boolean isDeleted

    static belongsTo = [po:PurchaseOrder]
    static hasMany = [receivedItems : PurchaseInvoiceItem]
	static transients = ['isDeleted']
	
    static constraints = {
        product(nullable:false)
        qty(nullable:false,min:new BigDecimal("0"))
        price(nullable:false,min:new BigDecimal("0"), scale:4)
        po(nullable:true)
        isComplete(nullable:true)
		productCode(nullable:true)
        finalPrice(nullable:false, scale:4)
        receivedItems(cascade:"all-delete-orphan")
    }
    
    String toString(){
        "${product}"
    }

    Integer computeRemaining(){
        def total = 0
        receivedItems?.each{
            total += it.qty
        }
        return qty.intValue() - total
    }

    BigDecimal computeAmount(){
        finalPrice * qty
    }

    BigDecimal computeRemainingBalance(){
        qty - receivedQty
    }
	
	Boolean hasInvoice() {
		return !receivedItems?.isEmpty()
	}

    String formatAmount(){
        "${po.currency} ${String.format('%,.4f',computeAmount())}"
    }

    String formatQty(){
        "${String.format('%,.0f',qty)}"
    }

    String formatPrice(){
        "${po.currency} ${String.format('%,.4f',price)}"
    }

    String formatFinalPrice(){
        "${po.currency} ${String.format('%,.4f',finalPrice)}"
    }

    String formatReceivedQty(){
        "${String.format('%,.0f',receivedQty)}"
    }

    String formatRemainingBalance(){
        "${String.format( '%,.0f',computeRemainingBalance() )}"
    }
	
	String formatRunningCostInForeignCurrency(){
		"${po.currency} ${String.format('%,.4f',(product.runningCostInForeignCurrency?:BigDecimal.ZERO))}"
	}
}
