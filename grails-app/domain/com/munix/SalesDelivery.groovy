package com.munix
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.FactoryUtils;

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date;
import java.math.RoundingMode;

class SalesDelivery extends CustomerPayment {
    String salesDeliveryId
    BigDecimal termDay
    String autoApprovedBy
    Warehouse warehouse
    Waybill waybill
    SalesAgent salesAgent
    DirectDelivery directDelivery
    String deliveryType
    BigDecimal commissionRate = 0
	BigDecimal amountPaid = 0
    String salesDeliveryNumber
	String deliveryReceiptNumber
	String remark
    List items = new ArrayList()

    static belongsTo = [invoice:SalesOrder]
    static transients = ['counterReceipts', 'dateApproved']
    static hasMany = [items:SalesDeliveryItem, printLogs:PrintLogSalesDelivery]
	
    static constraints = {
		salesDeliveryNumber(nullable:true)
		deliveryReceiptNumber(nullable:true, maxSize:50)
		amountPaid(nullable:true)
        invoice(nullable:true)
        waybill(nullable:true)
        termDay(nullable:false)
        warehouse(nullable:false)
        directDelivery(nullable:true)
        salesAgent(nullable:true)
        deliveryType(nullable:false)
        commissionRate(nullable:false)
        autoApprovedBy(nullable:true)
		remark(nullable:true, maxSize:250)
		status(nullable:false,inList : ["Unpaid", "Unapproved", "Paid", "Taken","Cancelled","Closed"])
        items (cascade:"all-delete-orphan,delete",validator : {val, obj->
			def zeroQtyitemList = obj.items?.findAll {it?.qty == 0}
            if(obj.items?.size() == zeroQtyitemList.size()) return ['salesDelivery.items.notEmpty']
        })
    }
	
	SalesDelivery(){}
	
	SalesDelivery(SalesOrder salesOrder){
		invoice = salesOrder
		customer = salesOrder?.customer
		salesDeliveryId = constructId()
		termDay = salesOrder?.customer?.term?.dayValue
		salesAgent = salesOrder?.salesAgent
		commissionRate = salesOrder?.customer?.commissionRate
	}
	
	String constructId(){
		def numberFormat=new DecimalFormat("#00000000")
		def company = Company.findAll()[0]
		def cal = Calendar.getInstance()
		def month = cal.get(Calendar.MONTH)
		def quarter = "D"
		if(month<3)
			quarter = "A"
		else if(month<6)
			quarter = "B"
		else if(month<9)
			quarter="C"
		def year = cal.get(Calendar.YEAR)?.toString().substring(2)
		def number = SalesDelivery.countBySalesDeliveryIdLike("${company?.code}-SD-${quarter}${year}-${customer?.type}-%")
		return "${company?.code}-SD-${quarter}${year}-${customer?.type}-${numberFormat.format(number+1)}"
	}

    String toString(){
        salesDeliveryId
    }

	void approve(){
		status = "Unpaid"
	}
	
	void cancel(){
		status = "Cancelled"
	}
	
	void close(){
		status = "Closed"
	}
	void paid(){
		status = "Paid"
	}
	
	void unapprove(){
		status = "Unapproved"
	}
	
	def isApproved(){
		return status == "Unpaid"
	}
	
	def isCancelled(){
		return status == "Cancelled"
	}
	
	def isClosed(){
		return status == "Closed"
	}
	
	def isUnapproved(){
		return status == "Unapproved"
	}
	
	def isUnapprovable() {
		return !invoices
	}
	
	def isPaid(){
		return status == "Paid"
	}
	
	def isDeliver(){
		return deliveryType == "Deliver"
	}

	def getDeliveryItemList(){
		return LazyList.decorate(items,FactoryUtils.instantiateFactory(SalesDeliveryItem.class))
	}
	
    BigDecimal computeDiscountedDiscount(){
		def computedDiscountedDiscount = 0
        if(this.invoice.discount && this.invoice.discountGroup){
            computedDiscountedDiscount = (computeDiscountedItemsTotal() * (this.invoice.discount/100)).setScale(2,RoundingMode.DOWN)
        }
        return computedDiscountedDiscount
    }
	BigDecimal computeNetDiscount(){
		def computedNetDiscount = 0
		if(this.invoice.netDiscount && this.invoice.netDiscountGroup){
			computedNetDiscount = (computeNetItemsTotal() * (this.invoice.netDiscount/100)).setScale(2,RoundingMode.DOWN)
		}
        return computedNetDiscount
	}

    BigDecimal computeDiscountedTotal(){
        computeDiscountedItemsTotal() - computeDiscountedDiscount()
    }
	BigDecimal computeNetTotal(){
		computeNetItemsTotal() - computeNetDiscount()
	}
    BigDecimal computeWaybillDeclaredValue(){
        computeTotalAmount() * (waybill?.declaredValue/100)
    }

    BigDecimal computeAmountDue(){
        computeTotalAmount() - amountPaid
    }

    BigDecimal computeCommissionAmount(){
        computeTotalAmount() * (commissionRate/100)
    }
	
	BigDecimal computeNetItemsTotal(){
		def total = 0
		items?.findAll {it?.orderItem?.isNet}?.each { total += it.computeAmount()}
		return total
	}

	BigDecimal computeDiscountedItemsTotal(){
		def total = 0
		items?.findAll {!it?.orderItem?.isNet}?.each { total += it.computeAmount()}
		return total
	}
	
	BigDecimal computeGrossTotal(){
		return computeNetItemsTotal() + computeDiscountedItemsTotal()
	}

    BigDecimal computeCounterReceiptsAmountTotal(){
		def total=0
        counterReceipts.each{
            total += it.computeTotal()
        }
        return total
	}
    
	BigDecimal computeCounterReceiptsAmountDueTotal(){
		def total=0
        counterReceipts.each{
            total += it.computeAmountDueTotal()
        }
        return total
	}
	
	Date getDateApproved(){
		def df = new SimpleDateFormat("MMM. dd, yyyy - hh:mm a");
		return df.parse(approvedBy.substring(approvedBy.length() - 24, approvedBy.length()))
	}
	
    @Override
	BigDecimal computeTotalAmount(){
		def totalAmount = computeNetTotal() + computeDiscountedTotal() 
		return totalAmount.setScale(2, RoundingMode.UP) 
	}

	@Override
	BigDecimal computeProjectedDue() {
		def due = computeTotalAmount()
        invoices.each{
            if(!it.directPayment.isCancelled()){
                due -= it.amount
            }
        }
        return due
	}
	
    @Override
    Map createLink(){
        [controller:"salesDelivery", id:this.id, action:"show"]
    }
	
	@Override
    void finishTransaction(BigDecimal amount){
		addToAmountPaid(amount)
        if(this.amountPaid>=computeTotalAmount()){
            paid()
        }
        notTakenByDirectPayment()

    }
	
	private void addToAmountPaid(BigDecimal amount) {
		this.amountPaid += amount
		removeFromCustomerAccountSDAmount(amount)
	}
	
	void addToCustomerAccountSDAmount(BigDecimal amount) {
		this.customer.customerAccount.addSDAmount(amount)
	}
	
	private void removeFromAmountPaid(BigDecimal amount) {
		this.amountPaid -= amount
		addToCustomerAccountSDAmount(amount)
	}
    
	void removeFromCustomerAccountSDAmount(BigDecimal amount) {
		this.customer.customerAccount.removeSDAmount(amount)
	}
	
	@Override
    void rollbackTransaction(BigDecimal amount){
		removeFromAmountPaid(amount)
        approve()
        takenByDirectPayment()
    }
	
	List<CounterReceipt> getCounterReceipts() {
		def counterReceiptItems = CounterReceiptItem.findAllByInvoiceIdAndInvoiceType(id, CustomerPaymentType.SALES_DELIVERY)
		def counterReceipts = []
		counterReceiptItems.each {
			counterReceipts.add(it.counterReceipt)
		}
		return counterReceipts
	}
    void updateItemCosts() {
		items.each {
			it.cost = it.product.runningCost
		}
	}
    void removeItemCosts() {
		items.each {
			it.cost = null
		}
	}
	
	def computeCostTotal(){
		def total = 0.00
		items.each {
			total += it.cost?it.cost:0.00
		}
		total
	}
	
	def computePhpDiffTotal(){
		def total = 0.00
		items.each {
			total += it.getMargin()
		}
		total
	}
	
	def computeAverageMarginPercentage(){
		def total = 0.00
		items.each {
			total += it.getPriceMargin()?it.getPriceMargin():0.00
		}
		if(items.size()>0){
			total = total/items.size()
		}
		total
	}
	def computeMarginGrandTotal(){
		def total = 0.00
		items.each {
			total += it.getTotalMargin()?it.getTotalMargin():0.00
		}
		total
	}
}
