package com.munix

import java.math.RoundingMode

class CreditMemoItem {
    SalesDeliveryItem deliveryItem
    Date date = new Date()
    BigDecimal oldQty = 0
    BigDecimal oldPrice = 0
    BigDecimal newQty = 0
    BigDecimal newPrice = 0
    String remark

    static belongsTo = [creditMemo: CreditMemo]

    static constraints = {
        deliveryItem(nullable: false)
        oldQty(nullable: false)
        oldPrice(nullable: false)
        newQty(nullable: false)
        newPrice(nullable: false)
        remark(nullable: true)
    }
	
    String formatOldPrice(){
        "PHP ${String.format( '%,.2f',oldPrice )}"
    }

    String formatNewPrice(){
        "PHP ${String.format( '%,.2f',newPrice )}"
    }

    String formatOldQty(){
        "${String.format( '%,.0f',oldQty )}"
    }

    String formatNewQty(){
        "${String.format( '%,.0f',newQty )}"
    }

    String formatFinalAmount(){
        "PHP ${String.format( '%,.2f',computeFinalAmount() )}"
    }
    String formatDiscountAmount(){
        "PHP ${String.format( '%,.2f',computeDiscountAmount() )}"
    }
    String formatDiscountedAmount(){
        "PHP ${String.format( '%,.2f',computeDiscountedAmount() )}"
    }

    BigDecimal computeNewAmount(){
        newQty * newPrice
    }
    BigDecimal computeDiscountedNewPricePerUnit(){
        def rate = obtainDiscountRate()
        def result = newPrice * (1-rate/100)
        if(creditMemo.additionalDiscount) result = result * (1-creditMemo.additionalDiscount/100)
        result
    }
    BigDecimal obtainDiscountRate(){
        def rate = 0
        if(!deliveryItem.orderItem?.isNet){
			rate = deliveryItem.delivery.invoice.discount ?: 0
        } else {
			rate = deliveryItem.delivery.invoice.netDiscount ?: 0
		}
        return rate
    }
    String obtainDiscountGroup(){
        def dg = 0
        if(!deliveryItem.orderItem?.isNet){
        	dg = deliveryItem.delivery.invoice.discountGroup
        } else {
			dg = deliveryItem.delivery.invoice.netDiscountGroup
		}
        return dg
    }
    BigDecimal computeDiscountAmount(){
        def rate = obtainDiscountRate()
        def fa = computeFinalAmount()
        def result = (fa * (rate/100))
        if(creditMemo.additionalDiscount) result = result + ((fa - result)*(creditMemo.additionalDiscount/100))
        result.setScale(2,RoundingMode.DOWN)
    }

    BigDecimal computeDiscountedAmount(){
        computeFinalAmount() - computeDiscountAmount()
    }
    BigDecimal computeOldAmount(){
        oldQty * oldPrice
    }

    BigDecimal computeFinalAmount(){
    	if(oldQty!=newQty && oldPrice!=newPrice){
    		(oldQty - newQty) * newPrice
    	}else
    		computeOldAmount() - computeNewAmount()
    }

	def hasQtyChanged(){
		return oldQty != newQty
	}
	
	def hasPriceChanged(){
		return oldPrice != newPrice
	}

	def isUnapproved(){
		return creditMemo.status == "Unapproved"
	}
	
    boolean equals(o) {
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        CreditMemoItem that = (CreditMemoItem) o;

        if (date != that.date) return false;
        if (deliveryItem != that.deliveryItem) return false;

        return true;
    }

    int hashCode() {
        int result;
        result = (deliveryItem != null ? deliveryItem.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }


}
