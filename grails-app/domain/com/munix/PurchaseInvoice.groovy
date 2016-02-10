package com.munix
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.FactoryUtils;

class PurchaseInvoice {
	static enum Status {
		UNAPPROVED("Unapproved"), APPROVED("Approved"), CANCELLED("Cancelled"),	PAID("Paid")
		
		String name
		
		Status(String name) {
			this.name = name
		}
		
		@Override
		public String toString() {
			return name
		}
	}

    Supplier supplier
    Date date = new Date()
    String reference
	String supplierReference
    Status status = Status.UNAPPROVED
    String preparedBy
    String approvedBy
	String cancelledBy
    String type
    Date invoiceDate
    Warehouse warehouse
    Date deliveryDate
    BigDecimal exchangeRate = 0
    List items = new ArrayList()
    SupplierPayment payment
    String remark
    BigDecimal discountRate = 0
	
    static hasMany = [pos : PurchaseOrder, items : PurchaseInvoiceItem]

    static constraints = {
        discountRate(nullable:false,max:new BigDecimal("100"))
        supplier(nullable:false)
        date(nullable:false)
		type(nullable: false)
        invoiceDate(nullable:true)
        reference(nullable:false, unique:true, blank : false, maxSize:255)
		deliveryDate(nullable: false)
		supplierReference(nullable:true, maxSize:255)
        status(nullable:false)
        preparedBy(nullable:true)
        approvedBy(nullable:true)
		cancelledBy(nullable:true)
        warehouse(nullable:false)
        exchangeRate(nullable:true)
        payment(nullable : true)
        remark(nullable:true, maxSize:255)
        items (cascade:"all-delete-orphan,delete", validator : {val, obj->
            if(obj.items?.isEmpty()) return ['purchaseInvoice.items.notEmpty']
        })
    }

    String toString(){
        reference
    }

    String formatDeliveryForexTotal(){
        "PHP ${String.format('%,.2f',computeForeignAmountTotal())}"
    }

    String formatDeliveryAmountTotal(){
        "${supplier?.currency} ${String.format('%,.2f',computeDeliveryAmountTotal())}"
    }
    String formatPurchaseInvoiceDiscountedPhpTotal(){
        "PHP ${String.format('%,.2f',computePurchaseInvoiceDiscountedPhpTotal())}"
    }

    String formatPurchaseInvoiceDiscountedForeignTotal(){
        "${supplier?.currency} ${String.format('%,.2f',computePurchaseInvoiceDiscountedForeignTotal())}"
    }

    void approved(){
        status = Status.APPROVED
    }
    void unapproved(){
        status = Status.UNAPPROVED
    }
    void cancel(){
        status = Status.CANCELLED
    }
    void paid(){
        status = Status.PAID
    }
    def isApproved(){
        return status == Status.APPROVED
    }
    def isUnapproved(){
        return status == Status.UNAPPROVED
    }
    def isCancelled(){
        return status == Status.CANCELLED
    }
    def isPaid(){
        return status == Status.PAID
    }
    String formatExchangeRate(){
        exchangeRate? "${String.format('%,.4f',exchangeRate)}" : ""
    }

    BigDecimal computePurchaseInvoicePhpTotal(){
        def total=0
        total = (computeForeignAmountTotal()?:0)* (exchangeRate?:0)
        return total
    }
    BigDecimal computePurchaseInvoiceDiscountPhpAmount(){
        def total=0
        total = computePurchaseInvoicePhpTotal() * (discountRate?:0) / 100
        return total
    }
    BigDecimal computePurchaseInvoiceDiscountForeignAmount(){
        def total=0
        total = computeForeignAmountTotal() * (discountRate?:0) / 100
        return total
    }
    BigDecimal computePurchaseInvoiceDiscountedForeignTotal(){
        def total=0
        total = computeForeignAmountTotal() - computePurchaseInvoiceDiscountForeignAmount()
        return total
    }
    BigDecimal computePurchaseInvoiceDiscountedPhpTotal(){
        def total=0
        total = computePurchaseInvoicePhpTotal() - computePurchaseInvoiceDiscountPhpAmount()
        return total
    }

    Integer computeQtyTotal(){
        def total = 0
        items?.each{
            total += it.qty
        }
        return total
    }

    BigDecimal computeFinalPriceTotal(){
        def total = 0
        items?.each{
            total += it.finalPrice
        }
        return total
    }

    BigDecimal computeForeignAmountTotal(){
        def total = 0
        items?.each{
            total += it.finalPrice * it.qty
        }
        return total
    }

    def getInvoiceItemList(){
         return LazyList.decorate(items,FactoryUtils.instantiateFactory(PurchaseInvoiceItem.class))       
    }
}
