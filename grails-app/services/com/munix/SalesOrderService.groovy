package com.munix

class SalesOrderService {

    static transactional = true
    def generalMethodService
    def generateSalesOrderItems(SalesOrder salesOrderInstance) {
        def productItems = []
        salesOrderInstance.items.eachWithIndex { item, idx->
            def itemMap = [:]
            itemMap["isNet"] = item.isNet
            itemMap["productId"] = item.product.id
            itemMap["identifier"] = item.product?.identifier
			itemMap["partNumber"] = item.product?.partNumber
            itemMap["description"] = item.product?.description
			itemMap["packageDetails"] = item.product?.getPackageDetails()
            itemMap["price"] = item.price
            itemMap["finalPrice"] = item.finalPrice
            itemMap["qty"] = item.qty.intValue()
            itemMap["amount"] = item.formatAmount()
            itemMap["idx"] = idx
            productItems.add(itemMap)
        }
        return productItems.sort{it.description}
    }
	
	boolean checkIfDefaultQuantityHasBeenChanged(SalesOrder salesOrder) {
		def result = false

		if (salesOrder.discountGroup != salesOrder.customer.discounts.find {it.discountType ==  salesOrder.discountType && it.type == CustomerDiscount.Type.DISCOUNT}?.discountGroup) {
			result = true
		}
		
	if (salesOrder.netDiscountGroup != salesOrder.customer.discounts.find {it.discountType ==  salesOrder.discountType && it.type == CustomerDiscount.Type.NET}?.discountGroup) {
			result = true
		}
		
		salesOrder.items.each {
			if (it.finalPrice != it.price) {
				result = true
			}
		}
		return result
	}

    private def getApprovedOrCompleteSalesDeliveryItemsWithProductAndCustomer(Product productInstance, Customer customerInstance){
        def salesDeliveryItems = SalesDeliveryItem.withCriteria {
            eq("product",productInstance)
            delivery{
                and{
                    or{
                        eq("status","Unpaid")
                        eq("status","Paid")
                    }
                    eq('customer',customerInstance)
                }

            }
        }
        return salesDeliveryItems
    }
    def getThreeLatestSalesDeliveryItemsOfProductAndCustomer(Product productInstance, Customer customerInstance){
        def salesDeliveryItems = getApprovedOrCompleteSalesDeliveryItemsWithProductAndCustomer(productInstance, customerInstance)
        def listOfMappedSalesDeliveryItems = []
        salesDeliveryItems.each{
            listOfMappedSalesDeliveryItems.add([date:generalMethodService.getDateFromApprovedBy(it.delivery.approvedBy),salesDeliveryItem:it])
        }
        def sizeOfList = listOfMappedSalesDeliveryItems.size()
        def listOfFirstThreeSalesDeliveryItems = []
        if(sizeOfList>0){
            def maxIndex= sizeOfList
            if(sizeOfList>3){
                maxIndex = 3
            }
            def listOfFirstThreeMappedSalesDelivery = listOfMappedSalesDeliveryItems.sort{it.date}.reverse().subList(0,maxIndex)
            listOfFirstThreeMappedSalesDelivery.each{
                listOfFirstThreeSalesDeliveryItems.add(it.salesDeliveryItem)
            }
        }
        return listOfFirstThreeSalesDeliveryItems
    }
    def getAvailableProducts(DiscountType discountType, HashMap searchFields, String priceType){
        def searchIdentifier = searchFields.identifier
        def searchCategory = searchFields.category
        def searchSubcategory = searchFields.subcategory
        def searchBrand = searchFields.brand
        def searchModel = searchFields.model
        def searchSize = searchFields.size
        def searchColor = searchFields.color
        def products = Product.withCriteria {
            and{
                eq("status", "Active")
                eq("isForSale", true)
                eq("type", discountType)
                if(searchIdentifier){
                    ilike("identifier", "%${searchIdentifier}%")
                }
                if(searchCategory){
                    category{
                        ilike("identifier", "%${searchCategory}%")
                    }
                }
                if(searchSubcategory){
                    subcategory{
                        ilike("identifier", "%${searchSubcategory}%")
                    }
                }
                if(searchBrand){
                    brand{
                        ilike("identifier", "%${searchBrand}%")
                    }
                }
                if(searchModel){
                    model{
                        ilike("identifier", "%${searchModel}%")
                    }
                }
                if(searchSize){
                    ilike("size", "%${searchSize}%")
                }
                if(searchColor){
                    color{
                        ilike("identifier", "%${searchColor}%")
                    }
                }
                if(priceType == "Retail"){
                    isNotNull("retailPrice")
                }else if(priceType == "Wholesale"){
                    isNotNull("wholeSalePrice")
                }
           }
        }
        return products
    }
    
    def autocancelSalesOrders() {
		def salesOrders = SalesOrder.findAllByStatusInList(["Unapproved","Second Approval Pending"])
        def cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
		salesOrders.each{
        	if(it.date.getTime()< cal.getTime().getTime()){
        		it.status="Cancelled"
        		it.cancelledBy = "System "+new Date().format("MMM. dd, yyyy - hh:mm a")
        		it.save()
        	}
		}
    }

    def autocompleteSalesOrders() {
		def salesOrders = SalesOrder.findAllByStatusInList(["Approved"])
        def cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
		salesOrders.each{
        	if(it.date.getTime()< cal.getTime().getTime()){
        		if(it.deliveries?.size()>0){
        			it.status="Complete"
        			it.closedBy = "System "+new Date().format("MMM. dd, yyyy - hh:mm a")
        			it.save()
        		}else{
        			it.status="Cancelled"
        			it.cancelledBy = "System (No Deliveries) "+new Date().format("MMM. dd, yyyy - hh:mm a")
        			it.save()
        		}
        	}
		}
    }
}


