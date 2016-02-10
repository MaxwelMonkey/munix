package com.munix

import java.math.RoundingMode;

class SalesDeliveryItem {

    Product product
    BigDecimal qty
    BigDecimal price
    SalesOrderItem orderItem
    BigDecimal cost
    
	static transients = ['discountedPrice','margin','discount']
	static hasMany = [creditMemoItems:CreditMemoItem]
    static belongsTo =[delivery:SalesDelivery]

    static constraints = {
        product(nullable:false)
        qty(nullable:false, min:new BigDecimal("0"))
        price(nullable:false, scale:4)
        orderItem(nullable: true)
        cost(nullable:true, scale:4)
    }
    BigDecimal getMargin(){
        return getDiscountedPrice() - (cost ?:product.runningCost)
    }
	BigDecimal getDiscount(){
        def discount = delivery.invoice.discount
		if(orderItem.isNet) {
			discount = delivery.invoice.netDiscount
		}
        return discount?:0
    }
	BigDecimal getDiscountedPrice() {
		def discount = getDiscount()
		return (price - (price * (discount/100).setScale(2,RoundingMode.DOWN)))
	}
	
    String formatQty(){
         "${String.format('%,.2f',qty)}"
    }

    String formatPrice(){
         "PHP ${String.format('%,.2f',price)}"
    }

    String formatAmount(){
         "PHP ${String.format('%,.2f',computeAmount())}"
    }

    BigDecimal computeAmount(){
        qty * price
    }
	
    BigDecimal getRemainingQty(creditMemoList,remainingQty){
        creditMemoList.each{
            if(!it.creditMemo.isCancelled()){
                remainingQty-=(it.oldQty - it.newQty)
            }
        }
        return remainingQty
    }
	
    BigDecimal computeRemainingQtyExcludingCreditMemo(CreditMemoItem creditMemoItemInstance){
        def creditMemoList = creditMemoItems-creditMemoItemInstance
        return getRemainingQty(creditMemoList,qty)
    }
	
    CreditMemoItem obtainLatestCreditMemoItem(){
        return creditMemoItems.max{it.date}
    }
	
	def getPriceMargin() {
		def currentCost
		if(this.delivery.isUnapproved()) {
			currentCost = this.product.runningCost
		} else {
			currentCost = this.cost
		}
		
		return currentCost == 0 ? BigDecimal.ZERO : (((getDiscountedPrice()/currentCost) - 1) * 100).setScale(4,RoundingMode.FLOOR)
	}
}
