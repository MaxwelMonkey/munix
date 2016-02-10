package com.munix
import org.apache.commons.collections.list.LazyList;
import org.apache.commons.collections.FactoryUtils;

class SalesOrder {
    Date date = new Date()
    Date deliveryDate = new Date() + 1
    String remark
    String status = "Unapproved"
    DiscountGroup discountGroup
	DiscountGroup netDiscountGroup
    BigDecimal discount = 0
	BigDecimal netDiscount = 0
    String preparedBy
    String approvedBy
    String approvedTwoBy
    String closedBy
	String cancelledBy
    Customer customer
	PriceType priceType
    DiscountType discountType
    SalesAgent salesAgent
    List items = new ArrayList()
    
    static hasMany = [items:SalesOrderItem, deliveries:SalesDelivery, printLogs:PrintLogSalesOrder]
	static transients = ['isUnapprovable']

    static constraints = {
        date(nullable:false)
        deliveryDate(nullable:true)
        remark(nullable:true)
        customer(nullable:false)
        status(nullable:false, inList:["Approved","Unapproved","Second Approval Pending","Complete","Cancelled"])
        discount(nullable:true,min:BigDecimal.ZERO)
		netDiscount(nullable:true,min:BigDecimal.ZERO)
        discountGroup(nullable:true)
		netDiscountGroup(nullable:true)
        preparedBy(nullable:true)
        approvedBy(nullable:true)
        approvedTwoBy(nullable:true)
        closedBy(nullable:true)
		cancelledBy(nullable:true)
        discountType(nullable:false)
        salesAgent(nullable:true)
		priceType(inList:[PriceType.RETAIL, PriceType.WHOLESALE])

        items (cascade:"all-delete-orphan,delete",validator : {val, obj->
            if(obj.items?.isEmpty()) return ['salesOrder.items.notEmpty']
        })
    }

	boolean getIsUnapprovable() {
		if (deliveries) {
			return false
			//return !deliveries.find{!it.isCancelled()}
		} else {
			return true
		}
	}
	
    void approved(){
		status = "Approved"
	}

	void unapproved(){
		status = "Unapproved"
	}

	void secondApproval(){
		status = "Second Approval Pending"
	}

	void complete(){
		status = "Complete"
	}

	void cancelled(){
		status = "Cancelled"
	}

    def isUnapproved(){
        return status == "Unapproved"
    }
    def isSecondApproval(){
        return status == "Second Approval Pending"
    }

    def isApproved(){
        return status == "Approved"
    }
    def isCancelled(){
        return status == "Cancelled"
    }
    def isComplete(){
        return status == "Complete"
    }
	def isCancelable(){
		def cancelable = true
		if(deliveries){
			deliveries.each{
				if(!it.isCancelled()) {
					cancelable = false
				}
			}
		}
        if(!isUnapproved()){
            cancelable = false
        }
		return cancelable
	}
    static namedQueries = {
       queryFilter{ queryParams ->
            and{
	            //Search
	            if(queryParams?.searchIdentifier){
	                eq('id', Long.parseLong(queryParams?.searchIdentifier))
	            }
	            if(queryParams?.searchCustomer){
	                customer{
	                    ilike('name', "%${queryParams?.searchCustomer}%")
	                }
	            }
	            if(queryParams?.searchRemark){
	                ilike('remark', "%${queryParams?.searchRemark}%")
	            }
				if(queryParams?.searchDiscountType){
					eq('discountType', DiscountType.get(queryParams?.searchDiscountType))
				}
	            if(queryParams?.searchStatuses){
	                'in'('status', queryParams?.searchStatuses)
                }else{
		            if(queryParams?.searchStatus){
	                	eq('status', "${queryParams?.searchStatus}")
	            	}else if(queryParams?.searchStatus==null){
            	        not{
        	                'in'('status', ["Complete","Cancelled"])
    	                }

	                }
                }
				if(queryParams?.searchPriceType){
					eq('priceType', PriceType.getTypeByName(queryParams?.searchPriceType))
				}
                if(queryParams?.searchCustomerType){
                    customer{
                        eq('type',CustomerType.get(queryParams?.searchCustomerType))
                    }
                }
			}
       }
       countFilter{ queryParams ->
            queryFilter(queryParams)
       }
       filter{ queryParams ->
            queryFilter(queryParams)
            if(queryParams.sort && queryParams.order){
                order(queryParams.sort, queryParams.order)
            }
            if(queryParams.max){
                maxResults(queryParams.int("max"))
            }
            if(queryParams.offset){
                firstResult(queryParams.int("offset"))
            }
       }
    }
    
    String toString(){
        "SO-${formatId()}"
    }
    String formatId(){
        "${id}".padLeft(8,'0')
    }
    String formatTotal(){
        "Php ${String.format('%,.2f',computeTotal())}"
    }
    String formatDiscountedDiscount(){
        "Php ${String.format('%,.2f',computeDiscountedDiscount())}"
    }
	String formatNetDiscount(){
		"Php ${String.format('%,.2f',computeNetDiscount())}"
	}
    String formatDiscountedTotal(){
        "Php ${String.format('%,.2f',computeDiscountedTotal())}"
    }
	String formatNetTotal(){
		"Php ${String.format('%,.2f',computeNetTotal())}"
	}
    String formatDeliveryTotal(){
        "Php ${String.format('%,.2f',computeDeliveryTotal())}"
    }
    String formatGrandTotal(){
        "Php ${String.format('%,.2f',computeGrandTotal())}"
    }
	String formatNetItemsTotal(){
		"Php ${String.format('%,.2f',computeNetItemsTotal())}"
	}
	String formatDiscountedItemsTotal(){
		"Php ${String.format('%,.2f',computeDiscountedItemsTotal())}"
	}
	String formatDiscountLabel(){
		if(discount) {
			return "${String.format('%,.2f',discount)}%"
		} else {
			return ""
		}
	}
	String formatNetDiscountLabel(){
		if(netDiscount) {
			return "${String.format('%,.2f',netDiscount)}%"
		} else {
			return ""
		}
	}
	def setRetail(){
		priceType = PriceType.RETAIL	
	}
	def setWholesale(){
		priceType = PriceType.WHOLESALE
	}
	def getPricePrintCount(){
		return PrintLogSalesOrder.countBySalesOrderAndPrintType(this,PrintLogSalesOrder.PrintType.PRICE)
	}
	def getNoPricePrintCount(){
		return PrintLogSalesOrder.countBySalesOrderAndPrintType(this,PrintLogSalesOrder.PrintType.NO_PRICE)
	}
    def getOrderItemList(){
         return LazyList.decorate(items,FactoryUtils.instantiateFactory(SalesOrderItem.class))
    }
	
	Boolean hasNoActiveDeliveries() {
		def result = true
			deliveries?.each{
				if(!it.isCancelled()){
					result = false
				}
			}
		return result
	}

	BigDecimal computeTotal(){
		def total = 0
		items.each{
			total += it.qty * it.finalPrice
		}
		return total
	}
	BigDecimal computeDiscountedDiscount(){
		def discountedDiscount = 0
		if(discount && discountGroup){
			discountedDiscount = computeDiscountedItemsTotal() * ((this.discount?:0)/100)
		}
		return discountedDiscount
	}
	BigDecimal computeNetDiscount(){
		def netItemsDiscount = 0
		if(netDiscount && netDiscountGroup){
			netItemsDiscount = computeNetItemsTotal() * ((this.netDiscount?:0)/100)
		}
		return netItemsDiscount
	}
	BigDecimal computeDiscountedTotal(){
		computeDiscountedItemsTotal() - computeDiscountedDiscount()
	}
	BigDecimal computeNetTotal(){
		computeNetItemsTotal() - computeNetDiscount()
	}
	BigDecimal computeDeliveryTotal(){
		def total = 0
		deliveries.each{
			total += it.computeTotalAmount()
		}
		return total
	}
    BigDecimal computeNetItemsTotal(){
      def total = 0
      items?.findAll {it?.isNet}?.each { total += it.computeAmount()}
      return total
    }
    BigDecimal computeDiscountedItemsTotal(){
      def total = 0
      items?.findAll {!it?.isNet}?.each { total += it.computeAmount()}
      return total
    }
    BigDecimal computeGrandTotal(){
      return computeNetTotal() + computeDiscountedTotal()
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
}
