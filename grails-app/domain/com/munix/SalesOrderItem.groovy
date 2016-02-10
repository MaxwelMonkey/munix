package com.munix

import java.math.RoundingMode;

class SalesOrderItem {
    Product product
    BigDecimal qty
    BigDecimal price
    BigDecimal finalPrice
    BigDecimal deliveredQty = 0
    BigDecimal cost
    boolean isDeleted
	Boolean isNet

    static belongsTo = [invoice:SalesOrder]
    static transients = ['isDeleted', 'remainingBalance', 'margin']


    static constraints = {
        product(nullable:false)
        qty(nullable:false, min:new BigDecimal("1"))
        price(nullable:false, min:new BigDecimal("0"), scale:4)
		finalPrice(nullable:true, scale:4)
        deliveredQty(nullable:true)
        invoice(nullable:true)
        cost(nullable:true, scale:4)
		isNet(nullable:true)
    }
	
	BigDecimal getRemainingBalance() {
		return computeRemainingBalance()
	}
	
	BigDecimal getMargin() {
		return discountedFinalPrice() - (cost ?: product.runningCost)
	}
	
    String toString() {
        product
    }

    BigDecimal computeAmount(){
        finalPrice * qty
    }

    BigDecimal computeRemainingBalance(){
        qty - deliveredQty
    }

    BigDecimal computeDiscount(){
        computeAmount() * (invoice.discountGroup.rate/100)
    }

    BigDecimal computeNetProfit(){
        computeAmount() - computeDiscount() - cost
    }
    BigDecimal discountRate(){
        def discountRate = invoice.discount
        if(isNet){
            discountRate = invoice.netDiscount
        }
        return discountRate?:0
    }
    BigDecimal discountedFinalPrice(){
        def discountAmount = finalPrice*discountRate()/100
        return finalPrice - discountAmount
    }
    String formatAmount(){
        "Php ${String.format('%,.2f',computeAmount()?.doubleValue())}"
    }

    String formatRemainingBalance(){
        "${String.format('%,.0f',computeRemainingBalance()?.doubleValue())}"
    }

    String formatDeliveredQty(){
        "${String.format('%,.0f',deliveredQty?.doubleValue())}"
    }

    String formatQty(){
        "${String.format('%,.0f',qty?.doubleValue())}"
    }

    String formatPrice(){
        "Php ${String.format('%,.2f',price?.doubleValue())}"
    }

    String formatFinalPrice(){
        "Php ${String.format('%,.2f',finalPrice?.doubleValue())}"
    }

    String formatDiscountedFinalPrice(){
        "Php ${String.format('%,.2f',discountedFinalPrice()?.doubleValue())}"
    }

    String formatCost(){
        "Php ${String.format('%,.2f',cost?.doubleValue())}"
    }
	
	def getPriceMargin() {
		def currentCost
		if(this.invoice.isUnapproved() || this.invoice.isSecondApproval()) {
			currentCost = this.product.runningCost
		} else {
			currentCost = this.cost
		}

		return currentCost == 0 ? BigDecimal.ZERO : (((discountedFinalPrice()/currentCost) - 1) * 100).setScale(4,RoundingMode.FLOOR)
	}
}
