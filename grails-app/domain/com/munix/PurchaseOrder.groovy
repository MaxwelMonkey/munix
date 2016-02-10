package com.munix

import java.text.DecimalFormat
import java.util.List;
import org.apache.commons.collections.list.LazyList
import org.apache.commons.collections.FactoryUtils

class PurchaseOrder {
    Supplier supplier
    Date date = new Date()
    String status = "Unapproved"
    CurrencyType currency
    String preparedBy
    String approvedBy
	String closedBy
	String cancelledBy
	String remark
	PurchaseOrderId poId
	String year = Calendar.getInstance().get(Calendar.YEAR)
	Integer counterId = 0
	BigDecimal discountRate = 0
	Date shipBy
	List items = []
    static hasMany = [items:PurchaseOrderItem,printLogs:PrintLogPurchaseOrder]
    static Searchable = true
	
    static constraints = {
		discountRate(nullable:false,max:new BigDecimal("100"))
        supplier(nullable:false)
        date(nullable:false)
        status(nullable:false)
        currency(nullable:false)
        preparedBy(nullable:true)
        approvedBy(nullable:true)
		closedBy(nullable:true)
		cancelledBy(nullable:true)
		remark(nullable:true)
		poId(nullable: true)
		shipBy(nullable: true)
		items(validator : {val, obj->
			if(obj.items?.isEmpty()) return ['purchaseOrder.items.notEmpty']
		})
    }

	int generateCounterId(){
		def pos = PurchaseOrder.findAllBySupplierAndYear(supplier, year)
		int maxCounterId = 1
		if(pos) {
				def intComparator = [compare: {a, b -> a.counterId <=> b.counterId }] as Comparator
				maxCounterId = Collections.max( pos, intComparator ).counterId + 1
		}
		
		return maxCounterId
	}
	
	void approve() {
		status = "Approved"
	}
    void complete() {
		status = "Complete"
	}
	
	def isApproved() {
		return "Approved".equals(status)
	}
	
	def isUnapproved() {
		return "Unapproved".equals(status)
	}

    def isComplete() {
		return "Complete".equals(status)
    }
	
    String formatId(){
        def number=new DecimalFormat("#00000")
        return supplier.identifier + "-" + year + "-" + number.format(counterId)
    }

    String formatTotal(){
        "${currency} ${String.format('%,.4f',computeTotal())}"
    }
	
    String formatDeliveryAmountTotal(){
        "${currency} ${String.format('%,.4f',computeDeliveryAmountTotal())}"
    }

    String formatDeliveryForexTotal(){
        "PHP ${String.format('%,.4f',computeDeliveryForexTotal())}"
    }

	String formatGrandTotal(){
		"${currency} ${String.format('%,.4f',computeGrandTotal())}"
	}
	
    BigDecimal computeTotal(){
        def total = 0
        items.each{
            total += it.computeAmount()
        }
        return total
    }

	BigDecimal computeGrandTotalDiscount(){
		return computeTotal() * (discountRate / 100)
	}
	
	BigDecimal computeGrandTotal(){
		return computeTotal() - computeGrandTotalDiscount()
	}
	
    boolean checkForCompletion(){
        def isComplete = items.find{
            !it.isComplete
        }
        return isComplete == null
    }
	
	def getItemList(){
		return LazyList.decorate(items,FactoryUtils.instantiateFactory(PurchaseOrderItem.class))
	}
}
