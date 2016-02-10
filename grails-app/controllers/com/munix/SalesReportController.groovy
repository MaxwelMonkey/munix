package com.munix

import java.util.Calendar;

class SalesReportController {
	
	def authenticateService
	def directPaymentService
    
    def search = {
		def productTypes = DiscountType.findAll([sort:"description"])
		def customers = Customer.findAll([sort:"identifier"])
		def salesAgents = SalesAgent.findAll([sort:"identifier"])
		def products = Product.findAll([sort:"identifier"])
		def categories = ProductCategory.findAll([sort:"identifier"])
		def subcategories = ProductSubcategory.findAll([sort:"identifier"])
		def brands = ProductBrand.findAll([sort:"identifier"])
		def models = ProductModel.findAll([sort:"identifier"])
		def itemTypes = ItemType.findAll([sort:"identifier"])
		def materials = ProductMaterial.findAll([sort:"identifier"])
		def colors = ProductColor.list(sort:"identifier");
 		[productTypes:productTypes, customers:customers, salesAgents:salesAgents, products:products, brands:brands, categories:categories, subcategories:subcategories, models: models, itemTypes:itemTypes, materials:materials, colors:colors]
    }
    
    def list = {	
    	def seriesColumns = ['date','id','invoice.discountType','customer','discountTotal','netTotal','grandTotal']
     	def itemsSoldColumns = ['date','id','discountType','customer','discountTotal','netTotal','grandTotal']
    	def where = "where 1=1"
    	def map = [:]
    	if(params.dateFrom){
    		where += " and date>=:dateFrom"
    		map.put("dateFrom",params.dateFrom)
    	}
    	if(params.dateTo){
    		where += " and date<:dateTo"
        	map.put("dateTo",params.dateTo + 1)
    	}
    	if(params.customerType?.id != null ){
    		where += " and invoice.customer.type.id in (:customerTypes)"

            if(params.customerType?.id instanceof String)
                map.put("customerTypes",Long.parseLong(params.customerType?.id))
            else
                map.put("customerTypes",params.customerType?.id.collect{value -> Long.parseLong(value)})
    	}
    	if(params.productType?.id != null ){
    		where += " and invoice.discountType.id in (:discountTypes)"
        	//map.put(Long.parseLong(params.productType.id))
    	
			if(params.productType.id instanceof String)
				map.put("discountTypes",Long.parseLong(params.productType?.id))
			else
				map.put("discountTypes",params.productType?.id.collect{value -> Long.parseLong(value)})
	
		}
    	if(params.customer?.id != null ){
    		where += " and customer.id in (:customers)"
        	//map.add(Long.parseLong(params.customer.id))
    		
			if(params.customer?.id instanceof String)
				map.put("customers",Long.parseLong(params.customer?.id))
			else
				map.put("customers",params.customer?.id.collect{value -> Long.parseLong(value)})

		}
    	if(params.salesAgent?.id != null){
    		/*
			where += " and salesAgent.id=?"
        	map.add(Long.parseLong(params.salesAgent.id))
        	*/
			where += " and salesAgent.id in (:salesAgentIds)"
			if(params.salesAgent.id instanceof String)
				map.put("salesAgentIds",Long.parseLong(params.salesAgent?.id))
			else
				map.put("salesAgentIds",params.salesAgent.id.collect{value -> Long.parseLong(value)})
    	}
		if( params.reportType == "itemsSold" || params.reportType == "itemsSoldDetailed"){
			if(params.searchStatus != null){
				//where += "and status='${params.searchStatus}'"
				where += " and status in (:searchStatuses)"
				if(params.searchStatus instanceof String)
					map.put("searchStatuses",params.searchStatus)
				else
					map.put("searchStatuses",params.searchStatus.collect{value -> value.toString()})
			}
		}
		
		/*if(params.salesAgent?.id){
			where += " and d.directPayment.customer.salesAgent.id in (:salesAgentIds)"
			if(params.salesAgent.id instanceof String)
				map.put("salesAgentIds",Long.parseLong(params.salesAgent?.id))
			else
				map.put("salesAgentIds",params.salesAgent.id.collect{value -> Long.parseLong(value)})
		}*/
    	
    	def list = SalesDelivery.executeQuery("from ${params.model} ${where}",map)
		def products = [:]
		def ids = []
   		def customers = [:]
  		def customerIds = []
		def newList = []
		list.each{
    		// need to check if the Sales Delivery has that specific product
    		def hasProduct = true
    		def hasOutstandingBalance = true
    		if(params.product?.id != "" || params.product?.category?.id  || params.product?.subcategory?.id  || params.product?.brand?.id  || params.product?.model?.id ){
    			hasProduct = false
    		}
    		if(params.balance && it.computeAmountDue()==0){
    			hasOutstandingBalance = false
    		}
    		
    		def delivery = it
			int customerId = (int)delivery.customer.id
			def customer = customers[customerId]
		    if(!customer){
		    	customerIds.add(customerId)
		    	customer = [:]
		    	customer["customer"] = delivery.customer
		    	customer["products"] = [:]
		    	customer["ids"] = []
		    }
			delivery?.items?.each{
				
	    		if(it != null && ((params.product?.id != "" && it?.product?.id.toString() == params.product?.id) || !params.product?.id)){
				
					
		    		if((listContains(params.product?.category?.id,it.product?.category?.id.toString()) || !params.product?.category?.id) &&
		    				(listContains(params.product?.subcategory?.id,it.product?.subcategory?.id.toString()) || !params.product?.subcategory?.id) &&
		    				(listContains(params.product?.brand?.id,it.product?.brand?.id.toString()) || !params.product?.brand?.id) &&
		    				(listContains(params.product?.model?.id,it.product?.model?.id.toString()) || !params.product?.model?.id) &&
		    				(listContains(params.product?.itemType?.id,it.product?.itemType?.id.toString()) || !params.product?.itemType?.id)
		    				){
		    			hasProduct = true
				    			
						int id = (int)it.product?.id
		    			def product = products[id]
						if(!product){
							ids.add(id)
							product = [:]
				    		product["product"] = it.product
				    		product["quantity"] = 0
				    		product["price"] = 0
				    		product["amount"] = 0
				    		product["cost"] = 0
						}
		    			def customerProduct = customer["products"][id]
						if(!customerProduct){
							customer["ids"].add(id)
							customerProduct = [:]
		                    customerProduct["product"] = it.product
		                    customerProduct["quantity"] = 0
		                    customerProduct["price"] = 0
		                    customerProduct["amount"] = 0
				    		customerProduct["cost"] = 0
						}
			    		product["quantity"] = product["quantity"] + it.qty
			    		customerProduct["quantity"] = customerProduct["quantity"] + it.qty
			    		if(delivery.invoice?.priceType == "Wholesale"){
			    			if(it.product?.wholesalePrice){
			    				product["price"] = product["price"] + (it.product?.wholeSalePrice * it.qty)
			    				customerProduct["price"] = customerProduct["price"] + (it.product?.wholeSalePrice * it.qty)
			    			}
			    		}else{
			    			if(it.product?.retailPrice){
			    				product["price"] = product["price"] + (it.product?.retailPrice * it.qty)
			    				customerProduct["price"] = customerProduct["price"] + (it.product?.retailPrice * it.qty)
			    			}
			    		}
			    		def amt = it.computeAmount()
			    		if(!it?.orderItem?.isNet && delivery.invoice.discount)
			    			amt = amt - (amt * (delivery.invoice.discount/100))
			    		product["amount"] = product["amount"] + amt
			    		customerProduct["amount"] = customerProduct["amount"] + amt
			    		if(it.cost){
			    			product["cost"] = product["cost"] + it.cost
			    			customerProduct["cost"] = customerProduct["cost"] + it.cost
			    		}
			    	    products[id] = product
			    	    customer["products"][id] = customerProduct
		    		}
	    		}
			}
    	    customers[customerId] = customer
    		
    		if(hasProduct && hasOutstandingBalance){
    			newList.add(delivery)
    		}
		}
    	if(params.reportType == "date")
    		newList = newList.sort{it.date}
    	if(params.sort){
    		String s = params.sort
    		if(s.indexOf(".")>=0 || s.indexOf("()")>=0)
    			newList = newList.sort{Eval.x(it,"x."+s)}
    		else
    			newList = newList.sort{it[s]}
        	if(params.direction == "desc"){
        		list = list.reverse();
        	}
    	}
		
		if(params.reportType == "salesReportCustomerType"){
			def customerTypes = CustomerType.list(sort:"identifier");
			def cTypesMap = [:]
			def newerDateFrom = new Date().parse("E MMM dd H:m:s z yyyy", params.dateFrom.toString())
			def newerDateTo = new Date().parse("E MMM dd H:m:s z yyyy", params.dateTo.toString())
			newList = newList.sort{it.date};
			def byDateList = [:]
			for(Date i=newerDateFrom; i<=newerDateTo; i++){
				//def now=i;
				Date dateRange = i
				dateRange++;
				def preList = newList.findAll{i<it.date && it.date<dateRange}
				if(preList.size()>0){
					for(int j=0; j<customerTypes.size().toInteger(); j++)
					{
						def types = customerTypes[j];
						cTypesMap[types.identifier] = preList.findAll{it.customer.type.identifier.toString()==types.identifier.toString()};
					}
					byDateList.put("${formatDate(date:i, format:'MM/dd/yyyy E')}", cTypesMap);
					cTypesMap= [:];
				}
			}
			def user = User.findByUsername(authenticateService.principal().username)
			def model = [list:newList, modelName:params.model, columns:seriesColumns, products:products, ids:ids, username: user.userRealName, customers:customers, customerIds:customerIds, byDateList:byDateList, customerTypes:customerTypes]
			render(view:params.reportType, model:model)
		}else if(params.reportType == "salesReportDiscountType"){
			def discTypes = DiscountType.list(sort:"identifier");
			def dTypesMap = [:]	
			def newerDateFrom = new Date().parse("E MMM dd H:m:s z yyyy", params.dateFrom.toString())
			def newerDateTo = new Date().parse("E MMM dd H:m:s z yyyy", params.dateTo.toString())
			newList = newList.sort{it.date};
			def byDateList = [:]
			for(Date i=newerDateFrom; i<=newerDateTo; i++){
				//def now=i;
				Date dateRange = i
				dateRange++;
				def preList = newList.findAll{i<it.date && it.date<dateRange}
				if(preList.size()>0){
					for(int j=0; j<discTypes.size().toInteger(); j++)
					{
						def types = discTypes[j];
						dTypesMap[types.identifier] = preList.findAll{it.invoice?.discountType?.identifier.toString()==types.identifier.toString()};
					}
					byDateList.put("${formatDate(date:i, format:'MM/dd/yyyy E')}", dTypesMap);
					dTypesMap= [:];
				}
			}
			def user = User.findByUsername(authenticateService.principal().username)
			def model = [list:newList, modelName:params.model, columns:seriesColumns, products:products, ids:ids, username: user.userRealName, customers:customers, customerIds:customerIds, byDateList:byDateList, discTypes:discTypes]
			render(view:params.reportType, model:model)
		}else if(params.reportType == "salesReportCustomerDiscountType"){
			def discTypes = DiscountType.list(sort:"identifier");
			def customerTypes = CustomerType.list(sort:"identifier");
			newList = newList.sort{it.date};
//			def dTypesMap = [:]	
//			def newerDateFrom = new Date().parse("E MMM dd H:m:s z yyyy", params.dateFrom.toString())
//			def newerDateTo = new Date().parse("E MMM dd H:m:s z yyyy", params.dateTo.toString())
//			def byDateList = [:]
//			for(Date i=newerDateFrom; i<=newerDateTo; i++){
//				//def now=i;
//				Date dateRange = i
//				dateRange++;
//				def preList = newList.findAll{i<it.date && it.date<dateRange}
//				if(preList.size()>0){
//					for(int j=0; j<discTypes.size().toInteger(); j++)
//					{
//						def types = discTypes[j];
//						dTypesMap[types.identifier] = preList.findAll{it.invoice?.discountType?.identifier.toString()==types.identifier.toString()};
//					}
//					byDateList.put("${formatDate(date:i, format:'MM/dd/yyyy E')}", dTypesMap);
//					dTypesMap= [:];
//				}
//			}
			def user = User.findByUsername(authenticateService.principal().username)
			def model = [list:newList, modelName:params.model, columns:seriesColumns, products:products, ids:ids, username: user.userRealName, customers:customers, customerIds:customerIds, discTypes:discTypes, customerTypes:customerTypes]
			render(view:params.reportType, model:model)
		}else if(params.reportType == "salesReportByMonthComparison"){
			def discTypes = DiscountType.list(sort:"identifier");
			def customerTypes = []
			CustomerType.list(sort:"identifier").each{
				def ct = it
				if(params.customerType?.id){
		            if(params.customerType?.id instanceof String){
		                if(ct.id == params.customerType?.id){
		                	customerTypes.add(ct)
		                }
		            }else{
		            	params.customerType?.id.each{
		            		if(ct.id?.toString() == it)
		            			customerTypes.add(ct)
		            	}
		            }
				}else{
        			customerTypes.add(ct)
				}
			}
			
			newList = newList.sort{it.date};
			def result = [:]	
			def newerDateFrom = new Date().parse("E MMM dd H:m:s z yyyy", params.dateFrom.toString())
			def newerDateTo = new Date().parse("E MMM dd H:m:s z yyyy", params.dateTo.toString())
			def cal = Calendar.getInstance()
			cal.setTime(newerDateFrom)
			def months = []
			def totals = [:]
			def xxx = 0
			def dateFrom = cal.getTime() 
			while(dateFrom.getTime()<=newerDateTo.getTime() && xxx<100){
				def key = formatDate(format:"MMMM yyyy",date:dateFrom)
				months.add(key)
				def keyData = [:]
				def preList = newList.findAll{formatDate(format:"MMMM yyyy",date:it.date)==key}
				preList.each{
					def ct = it.customer?.type
					def ctData = keyData[ct.id]
					if(!ctData) ctData = [:]
					
					
					def dt = it.invoice?.discountType
					def dtData = ctData[dt.id]
					if(!dtData) dtData = 0
					dtData += it.computeTotalAmount()
					
					ctData[dt.id] = dtData
					keyData[ct.id] = ctData
					
					def ctTotalData = totals[ct.id]
					if(!ctTotalData) ctTotalData = [:]                         
					
					def dtTotalData = ctTotalData[dt.id]
			        if(!dtTotalData) dtTotalData = 0
			        
			        dtTotalData += it.computeTotalAmount()
			        
			        ctTotalData[dt.id] = dtTotalData
			        totals[ct.id] = ctTotalData
				}
				result[key] = keyData
				cal.add(Calendar.MONTH,1)
				dateFrom = cal.getTime()
			}
			def user = User.findByUsername(authenticateService.principal().username)
			def model = [username: user.userRealName, result:result, months:months, discTypes:discTypes, customerTypes:customerTypes, totals:totals]
			render(view:params.reportType, model:model)
		}else{
    		def user = User.findByUsername(authenticateService.principal().username)
			def model = [list:newList, modelName:params.model, columns:seriesColumns, products:products, ids:ids, username: user.userRealName, customers:customers, customerIds:customerIds]
            render(view:params.reportType, model:model)
		}
    }
	
	/*def salesReportCustomerType={
		println params;
		println "salesReportCustomerType";
	}*/

	
	def listContains(list, value){
		if(list instanceof String){
			return list == value
		}else{
			def result = false
			list.each{
				if(value==it) {
					result = true;
				}
			}
			return result;
		}
	}
    
    def pendingSearch = {
		def customers = Customer.findAll([sort:"identifier"])
		def discountTypes = DiscountType.findAll([sort:"description"])
		def customerTypes = CustomerType.findAll([sort:"identifier"])
		def products = Product.findAll([sort:"identifier"])
		def brands = ProductBrand.findAll([sort:"identifier"])
		def categories = ProductCategory.findAll([sort:"identifier"])
		def subcategories = ProductSubcategory.findAll([sort:"identifier"])
		def itemTypes = ItemType.findAll([sort:"identifier"])
		def models = ProductModel.findAll([sort:"identifier"])
		[customers:customers, discountTypes:discountTypes, customerTypes:customerTypes, 
		products:products, brands:brands, categories:categories, subcategories:subcategories, itemTypes:itemTypes, models: models]
    }

    def pendingList = {
    	def where = "where i.invoice.status='Approved' and i.qty-i.deliveredQty > 0"
       	def map = [:]
    	if(params.dateFrom){
    		where += " and i.invoice.date>=:dateFrom"
            map.put("dateFrom",params.dateFrom)
    	}

    	if(params.dateTo){
    		where += " and i.invoice.date<:dateTo"
            map.put("dateTo",params.dateTo + 1)
    	}
        if(params.customer?.id != null ){
            where += " and i.invoice.customer.id in (:customers)"
            //map.add(Long.parseLong(params.customer.id))

            if(params.customer?.id instanceof String)
                map.put("customers",Long.parseLong(params.customer?.id))
            else
                map.put("customers",params.customer?.id.collect{value -> Long.parseLong(value)})

        }
    	if(params.discountType?.id != null ){
    		where += " and i.invoice.discountType.id in (:discountTypes)"
//        	map.add(Long.parseLong(params.discountType.id))

            if(params.discountType?.id instanceof String)
                map.put("discountTypes",Long.parseLong(params.discountType?.id))
            else
                map.put("discountTypes",params.discountType?.id.collect{value -> Long.parseLong(value)})
    	}
    	if(params.customer?.type?.id != null ){
    		where += " and i.invoice.customer.type.id in (:customerTypes)"
//        	map.add(Long.parseLong(params.customer.type.id))

            if(params.customer.type?.id instanceof String)
                map.put("customerTypes",Long.parseLong(params.customer.type?.id))
            else
                map.put("customerTypes",params.customer.type?.id.collect{value -> Long.parseLong(value)})
    	}
    	if(params.product?.id != null ){
    		where += " and i.product.id in (:products)"
//        	map.add(Long.parseLong(params.product.id))


            if(params.product?.id instanceof String)
                map.put("products",Long.parseLong(params.product?.id))
            else
                map.put("products",params.product?.id.collect{value -> Long.parseLong(value)})
    	}

    	def order = "i.product"
    	if(params.reportType == "customer")
    		order = "i.invoice.customer.name"
    	def list = SalesOrderItem.executeQuery("from SalesOrderItem i ${where} order by ${order}, i.invoice.id",map)
		def result = [:]
		def quantity = []
		def newList = []
		list.each{
			def product = it.product 
    		// need to check if the Sales Delivery has that specific product
    		def hasProduct = true
    		if(params.product?.id != "" || params.product?.category?.id  || params.product?.subcategory?.id  || params.product?.brand?.id  || params.product?.model?.id  || params.product?.itemType?.id ){
    			hasProduct = false
    		}
    		
    		if((params.product?.id != "" && product.id.toString() == params.product?.id) || !params.product?.id){
	    		if((listContains(params.product?.category?.id,product?.category?.id.toString()) || !params.product?.category?.id) &&
	    				(listContains(params.product?.subcategory?.id,product?.subcategory?.id.toString()) || !params.product?.subcategory?.id) &&
	    				(listContains(params.product?.brand?.id,product?.brand?.id.toString()) || !params.product?.brand?.id) &&
	    				(listContains(params.product?.itemType?.id,product?.itemType?.id.toString()) || !params.product?.itemType?.id) &&
	    				(listContains(params.product?.model?.id,product?.model?.id.toString()) || !params.product?.model?.id)
	    				){
	    			hasProduct = true
	    		}
    		}
    		
    		if(hasProduct){
	    		if(params.reportType == "product"){
					def productList = new ArrayList()
					if(result[product]){
						productList = result[product]
					}
					productList.add(it)
					result[product] = productList
	    		}else{
	    			def customer = it.invoice?.customer 
	    			def customerList = new ArrayList()
	    			if(result[customer]){
	    				customerList = result[customer]
	    			}
	    			customerList.add(it)
	    			result[customer] = customerList
	    		}
    		}
		}
    	def user = User.findByUsername(authenticateService.principal().username)
    	def model = [list:result, username: user.userRealName]
    	render(view:params.reportType, model:model)
    }
	    
    
    def purchaseSearch = {
		def suppliers = Supplier.findAll([sort:"identifier"])
		def products = Product.findAll([sort:"identifier"])
		def brands = ProductBrand.findAll([sort:"identifier"])
		def categories = ProductCategory.findAll([sort:"identifier"])
		def subcategories = ProductSubcategory.findAll([sort:"identifier"])
		def itemTypes = ItemType.findAll([sort:"identifier"])
		def models = ProductModel.findAll([sort:"identifier"])
		[suppliers:suppliers, products:products, brands:brands, categories:categories, subcategories:subcategories, itemTypes:itemTypes, models: models]
    }

    def purchaseList = {
    	def productColumns = ['customer','discountType','invoice.productType','customer','discountTotal','netTotal','grandTotal']
     	def itemsSoldColumns = ['date','id','productType','customer','discountTotal','netTotal','grandTotal']
    	def where = "where i.po.status='Approved' and i.qty - i.receivedQty <> 0"
       	def map = []
    	if(params.dateFrom){
    		where += " and i.po.date>=?"
    		map.add(params.dateFrom)
    	}
    	if(params.dateTo){
    		where += " and i.po.date<?"
        	map.add(params.dateTo + 1)
    	}

    	def order = "i.po.id"

    	
    	def list = PurchaseOrderItem.executeQuery("from PurchaseOrderItem i ${where} order by ${order}",map)
		def result = [:]
		def quantity = []
		def newList = []
		def totals = [:]
		if(params.reportType=="purchaseSupplier") list.sort{it.product.description}
		list.each{
			def product = it.product 
    		// need to check if the Purchase Order has that specific product
    		def hasProduct = true
    		if(params.product?.id != "" || params.product?.category?.id  || params.product?.subcategory?.id  || params.product?.brand?.id  || params.product?.model?.id  || params.product?.itemType?.id ){
    			hasProduct = false
    		}
    		
    		if((listContains(params.product?.id,product?.id.toString()) || !params.product?.id) &&
    				(listContains(params.supplier?.id,it.po?.supplier?.id.toString()) || !params.supplier?.id) &&
    				(listContains(params.product?.category?.id,product?.category?.id.toString()) || !params.product?.category?.id) &&
    				(listContains(params.product?.subcategory?.id,product?.subcategory?.id.toString()) || !params.product?.subcategory?.id) &&
    				(listContains(params.product?.brand?.id,product?.brand?.id.toString()) || !params.product?.brand?.id) &&
    				(listContains(params.product?.itemType?.id,product?.itemType?.id.toString()) || !params.product?.itemType?.id) &&
    				(listContains(params.product?.model?.id,product?.model?.id.toString()) || !params.product?.model?.id)
    				){
    			hasProduct = true
    		}
    		
    		if(hasProduct){
	    		if(params.reportType == "purchaseProduct"){
	        		def currency = it.po?.currency
    	    		def total = 0
    	    		if(!totals[product])
    	    			totals[product] = [:]
    	    		if(totals[product][currency]){
    	    			total = totals[product][currency]
    	    		}
    	    		totals[product][currency] = total + it.computeAmount() 
					def productList = new ArrayList()
					if(result[product]){
						productList = result[product]
					}
					productList.add(it)
					result[product] = productList
	    		}else{
	    			def supplier = it.po?.supplier 
	        		def currency = it.po?.currency
    	    		def total = 0
    	    		if(!totals[supplier])
    	    			totals[supplier] = [:]
    	    		if(totals[supplier][currency]){
    	    			total = totals[supplier][currency]
    	    		}
    	    		totals[supplier][currency] = total + it.computeAmount() 
	    			def supplierList = new ArrayList()
	    			if(result[supplier]){
	    				supplierList = result[supplier]
	    			}
	    			supplierList.add(it)
	    			result[supplier] = supplierList
	    		}
    		}
		}
    	def user = User.findByUsername(authenticateService.principal().username)
    	def model = [list:result, username: user.userRealName, totals:totals]
    	render(view:params.reportType, model:model)
    }

	    
    def directPaymentSearch = {
		def customers = Customer.findAll([sort:"identifier"])
		def discountTypes = DiscountType.findAll([sort:"description"])
		def customerTypes = CustomerType.findAll([sort:"identifier"])
		def paymentTypes = PaymentType.findAll([sort:"identifier"])
		def salesAgents = SalesAgent.findAll([sort:"identifier"])
		[customers:customers, discountTypes:discountTypes, customerTypes:customerTypes, paymentTypes:paymentTypes, salesAgents:salesAgents]
    }

    def directPaymentList = {
    	def where = "where 1=1"
       	def map = [:]
    	if(params.dateFrom){
    		where += " and d.directPayment.date>=:dateFrom"
    		map.put("dateFrom",params.dateFrom)
    	}
    	if(params.dateTo){
    		where += " and d.directPayment.date<:dateTo"
        	map.put("dateTo", params.dateTo + 1)
    	}
    	if(params.customer?.id){
    		where += " and d.directPayment.customer.id in (:customerIds)"
    		if(params.customer.id instanceof String)
    			map.put("customerIds",Long.parseLong(params.customer.id))
    		else
    			map.put("customerIds",params.customer.id.collect{value -> Long.parseLong(value)})
    	}
    	if(params.customer?.type?.id){
    		where += " and d.directPayment.customer.type.id in (:typeIds)"
    		if(params.customer.type.id instanceof String)
    			map.put("typeIds",Long.parseLong(params.customer?.type?.id))
    		else
    			map.put("typeIds",params.customer.type.id.collect{value -> Long.parseLong(value)})
    	}
    	if(params.paymentType?.id){
    		where += " and d.paymentType.id in (:paymentTypeIds)"
    		if(params.paymentType.id instanceof String)
    			map.put("paymentTypeIds",Long.parseLong(params.paymentType?.id))
    		else
    			map.put("paymentTypeIds",params.paymentType.id.collect{value -> Long.parseLong(value)})
    	}
    	if(params.salesAgent?.id){
    		where += " and d.directPayment.customer.salesAgent.id in (:salesAgentIds)"
    		if(params.salesAgent.id instanceof String)
    			map.put("salesAgentIds",Long.parseLong(params.salesAgent?.id))
    		else
    			map.put("salesAgentIds",params.salesAgent.id.collect{value -> Long.parseLong(value)})
    	}
    	
    	def currDirectPayment = ""
    	def totals = ["Credit Memo":0.0, "Bounced Check":0.0, "Charge":0.0]
		def totalsAppliedExc = [:];
		def totalsAppliedInc = [:];
		def deductibleToSales = [];
    	def balance = 0
    	def paymentTypeTotals = [:]
    	
    	def list = SalesOrderItem.executeQuery("from DirectPaymentItem d ${where} order by d.directPayment",map)
		def result = [:]
		def quantity = []
		def newList = []
		
	    // loop thru all direct payment items
		list.each{
			def customer = it.directPayment?.customer
			def salesAgent = it.directPayment?.customer?.salesAgent;
			def hasDiscountType = true
			// apply more filters
			if(params.discountType?.id){
				hasDiscountType = false
				customer.discounts.each{
					if(listContains(params.discountType?.id, it.discountType))
						hasDiscountType = true
				}
			}
			
			// Group output into maps
			if(hasDiscountType){
	    		if(params.reportType == "directPaymentCustomer"){
					def customerList = new ArrayList()
					if(result[customer]){
						customerList = result[customer]
					}
					customerList.add(it)
					result[customer] = customerList
	    		}else if(params.reportType == "directPaymentType"){
					def paymentType = it.paymentType
					def paymentTypeList = new ArrayList()
					if(result[paymentType]){
						paymentTypeList = result[paymentType]
					}
					paymentTypeList.add(it)
					result[paymentType] = paymentTypeList
	    		}else if(params.reportType == "directPaymentSalesAgent" || params.reportType == "directPaymentSalesAgentDetailed"){
	    			def salesAgentMap = [:]
										
	    			if(result[salesAgent]){
	    				salesAgentMap = result[salesAgent];
	    			}
					def paymentType = it.paymentType
					def paymentTypeList = new ArrayList()
					if(salesAgentMap[paymentType]){
						paymentTypeList = salesAgentMap[paymentType]
					}
					paymentTypeList.add(it)
					salesAgentMap[paymentType] = paymentTypeList
					
					result[salesAgent] = salesAgentMap;
					
					def paymentTypeTotal = paymentTypeTotals[paymentType]
					if(!paymentTypeTotal) paymentTypeTotal = 0
					paymentTypeTotal += it.amount
					paymentTypeTotals[paymentType] = paymentTypeTotal
	    		}else{
	    			result = list
	    		}
			}
			
			// get direct payments and build totals
			if(currDirectPayment != it.directPayment){
				if(currDirectPayment != ""){
					def applied = 0
					currDirectPayment.invoices.each{
						applied += it.amount
						def key = it.type?.name
						if(it.type?.name == "Delivery"){// && it.getRelatedCustomerPayment()?.invoice?.discountType.identifier == "Dunlop")
							key = it.getRelatedCustomerPayment()?.invoice?.discountType.identifier
							if (it.getRelatedCustomerPayment()?.invoice?.discountType.excludeInCommission==true){
								def total = totalsAppliedExc[key]
								if(!total){
									total = 0
								}
								total += it.amount
								totalsAppliedExc[key]=total;
							}else{
								def total = totalsAppliedInc[key]
								if(!total){
									total = 0
								}
								total += it.amount
								totalsAppliedInc[key]=total;
							}
						}
						def total = totals[key]
						if(!total){
							total = 0
						}
						total += it.amount
						totals[key] = total
					}
					BigDecimal paymentTotal = currDirectPayment?.computePaymentTotal() 
					balance += paymentTotal - applied
				}
				currDirectPayment = it.directPayment
			}
		}
		// get direct payments and build totals
		if(currDirectPayment != ""){
			def applied = 0
			currDirectPayment.invoices.each{
				applied += it.amount
				def key = it.type?.name
				if(it.type?.name == "Delivery"){// && it.getRelatedCustomerPayment()?.invoice?.discountType.identifier == "Dunlop")
					key = it.getRelatedCustomerPayment()?.invoice?.discountType.identifier
					if (it.getRelatedCustomerPayment()?.invoice?.discountType.excludeInCommission==true){
						def total = totalsAppliedExc[key]
						if(!total){
							total = 0
						}
						total += it.amount
						totalsAppliedExc[key]=total;
					}else{
						def total = totalsAppliedInc[key]
						if(!total){
							total = 0
						}
						total += it.amount
						totalsAppliedInc[key]=total;
					}
				}
				def total = totals[key]
				if(!total){
					total = 0
				}
				total += it.amount
				totals[key] = total
			}
			BigDecimal paymentTotal = currDirectPayment?.computePaymentTotal() 
			balance += paymentTotal - applied
		}
    	def user = User.findByUsername(authenticateService.principal().username)
    	def model = [list:result, username: user.userRealName, totals:totals, balance: balance, paymentTypeTotals: paymentTypeTotals]
		if(params.reportType == "directPaymentSalesAgent"){
			deductibleToSales = PaymentType.findAllByDeductibleFromSales(true).identifier;
			model = [list:result, username: user.userRealName, totals:totals, totalsAppliedExc:totalsAppliedExc, totalsAppliedInc:totalsAppliedInc, deductibleToSales:deductibleToSales, balance: balance, paymentTypeTotals: paymentTypeTotals]
		}
		render(view:params.reportType, model:model)
    }

    def productSearch = {
    	def reportTypes = ["productListSelling","productInventoryList","productListCost","productPricelist","inventoryReport"]
	    	def reportTypeLabels = ["Product List (w/ Picture)","Product Inventory List","Product List (Cost)","Product Pricelist","Inventory Report"]
    	["reportTypes":reportTypes, "reportTypeLabels":reportTypeLabels]
    }

    def productList = {
		//println Product.stocks.find{it.product.id==params.id};
        def productIds = []
        def iteration = 1
    	def where = "where 1=1"
    	def map = [:]
       	if(params.id){
    		where += " and id in (:ids)"
    		if(params.id instanceof String)
    			map.put("ids",Long.parseLong(params.id))
    		else{
    			productIds = params.id.collect{value -> Long.parseLong(value)}
    			//def subList = productIds.subList((iteration-1)* 4000, iteration * 4000)
    			map.put("ids",productIds)
    			/*if(subList.size()<productIds.size())
    				iteration++;
    			else
    				iteration = -1;*/
    		}
    	}
		if(params.currency){
			where += " and model.id in (:modelIds)"
			if(params.model.id instanceof String)
				map.put("modelIds",Long.parseLong(params.model.id))
			else
				map.put("modelIds",params.model.id.collect{value -> Long.parseLong(value)})
		}
		if(params.status){
    		if(params.status!="")
    		where += " and status=:status"
    		map.put("status",params.status)
    	}
    	if(params.customer?.id){
    		where += " and customer.id in (:customerIds)"
    		if(params.customer.id instanceof String)
    			map.put("customerIds",Long.parseLong(params.customer.id))
    		else
    			map.put("customerIds",params.customer.id.collect{value -> Long.parseLong(value)})
    	}
    	if(params.type?.id){
    		where += " and type.id in (:typeIds)"
    		if(params.type.id instanceof String)
    			map.put("typeIds",Long.parseLong(params.type.id))
    		else
    			map.put("typeIds",params.type.id.collect{value -> Long.parseLong(value)})
    	}
       	if(params.category?.id){
    		where += " and category.id in (:categoryIds)"
    		if(params.category.id instanceof String)
    			map.put("categoryIds",Long.parseLong(params.category.id))
    		else
    			map.put("categoryIds",params.category.id.collect{value -> Long.parseLong(value)})
    	}
       	if(params.subcategory?.id){
    		where += " and subcategory.id in (:subcategoryIds)"
    		if(params.subcategory.id instanceof String)
    			map.put("subcategoryIds",Long.parseLong(params.subcategory.id))
    		else
    			map.put("subcategoryIds",params.subcategory.id.collect{value -> Long.parseLong(value)})
    	}
       	if(params.brand?.id){
    		where += " and brand.id in (:brandIds)"
    		if(params.brand.id instanceof String)
    			map.put("brandIds",Long.parseLong(params.brand.id))
    		else
    			map.put("brandIds",params.brand.id.collect{value -> Long.parseLong(value)})
    	}
       	if(params.model?.id){
    		where += " and model.id in (:modelIds)"
    		if(params.model.id instanceof String)
    			map.put("modelIds",Long.parseLong(params.model.id))
    		else
    			map.put("modelIds",params.model.id.collect{value -> Long.parseLong(value)})
    	}
		if(params.itemType?.id){
    		where += " and itemType.id in (:itemTypeIds)"
    		if(params.itemType.id instanceof String)
    			map.put("itemTypeIds",Long.parseLong(params.itemType.id))
    		else
    			map.put("itemTypeIds",params.itemType.id.collect{value -> Long.parseLong(value)})
    	}
    	    	
    	def list = Product.executeQuery("from Product ${where} order by identifier",map)
    	if(params.sort){
    		String s = params.sort
    		if(s.indexOf(".")>=0 || s.indexOf("()")>=0)
    			list = list.sort{Eval.x(it,"x."+s)}
    		else
    			list = list.sort{it[s]}
        	if(params.direction == "desc"){
        		list = list.reverse();
        	}
    	}

		
		def user = User.findByUsername(authenticateService.principal().username)
		def model

		if(params.balance!='null'){
			def newList = [];
			if(params.balance == "<0"){
				list.each {
					def product = it
					product.stocks.each{
							if (it.warehouse.id == params.warehouseBalanceFilter.toInteger() && it.qty<0){
								newList.add(product);
							}
					}
				}
			}else if(params.balance == ">0"){
				list.each {
					def product = it
					product.stocks.each{
							if (it.warehouse.id == params.warehouseBalanceFilter.toInteger() && it.qty>0){
								newList.add(product);
							}
					}
				}
			}else if(params.balance == "=0"){
				list.each {
					def product = it
					product.stocks.each{
							if (it.warehouse.id == params.warehouseBalanceFilter.toInteger() && it.qty==0){
								newList.add(product);
							}
					}
				}			
			}
			if(params.sort){
				String s = params.sort
				if(s.indexOf(".")>=0 || s.indexOf("()")>=0)
					newList = newList.sort{Eval.x(it,"x."+s)}
				else
					newList = newList.sort{it[s]}
				if(params.direction == "desc"){
					newList = newList.reverse();
				}
			}
			model = [list:newList, modelName:params.model, username:user.userRealName, priceType:params.priceType, params:params]
		}else{
			model = [list:list, modelName:params.model, username:user.userRealName, priceType:params.priceType, params:params]
		}
		
    	render(view:params.reportType, model:model)
    }

    def creditMemoSearch ={

        def customers = Customer.findAll([sort:"identifier"]);
        def products = Product.findAll([sort:"identifier"]);
        def reasons = Reason.findAll([sort:"identifier"]);
        [customers:customers, products:products, reasons:reasons]
    }

    def creditMemoList ={

        def salesDeliveryItemInstance
        def modelInstance = CreditMemo.createCriteria();

        if (params.product?.id){
            if(params?.product?.id instanceof String)
                salesDeliveryItemInstance = SalesDeliveryItem.findAllByProduct(Product.get(params?.product?.id as Long));
            else
                salesDeliveryItemInstance = SalesDeliveryItem.findAllByProductInList(Product.findAllByIdInList(params?.product?.id.collect{value -> Long.parseLong(value)}));
        }

        def modelInstanceList = modelInstance.listDistinct{
        	and {
        		eq("status","Approved")
        	}
            between('date', params?.dateFrom, params?.dateTo);
            if (params.customer?.id){
                and {
                    customer{
                        if(params.customer?.id instanceof String)
                            eq("id", params.customer?.id as Long)
                        else
                            'in'("id", params.customer?.id.collect{value -> Long.parseLong(value)})
                    }
                }
            }
            if (params.reason?.id){
                and {
                    reason{
                        if(params?.reason?.id instanceof String)
                            eq("id", params?.reason?.id as Long)
                        else
                            'in'("id", params?.reason?.id.collect{value -> Long.parseLong(value)})
                    }
                }
            }
            if (params.product?.id && salesDeliveryItemInstance){
                items{
                   'in'("deliveryItem", salesDeliveryItemInstance);
                    order("id");
                }
            }
        }
        def newMap = [:];
        Reason.list().each {
            def reason = it
            def tempList = modelInstanceList.findAll {it.reason?.id == reason.id};
            newMap.put(reason, modelInstanceList.findAll {it.reason?.id == reason.id});
        }
        [modelInstanceList:newMap, username:authenticateService.principal()?.username];
    }


    def purchaseInvoiceSearch = {
        def suppliers = Supplier.findAll([sort:"identifier"])
        def products = Product.findAll([sort:"identifier"])
        def currencyTypes = CurrencyType.findAll([sort:"identifier"])
        def warehouses = Warehouse.findAll([sort:"identifier"])
        [suppliers:suppliers, products:products, currencyTypes:currencyTypes, warehouses:warehouses]
    }

    def purchaseInvoiceList = {
    	def purchaseInvoices
    	if(params.reportType == "purchaseInvoiceSupplier"){
    		purchaseInvoices = retrievePurchaseInvoices(params)
    	}else{
    		purchaseInvoices = retrievePurchaseInvoiceItems(params)
    	}
        
        def result = [:]
        def suppliers = []
        purchaseInvoices.each{
        	def invoice = it
        	def key
        	if(params.reportType == "purchaseInvoiceSupplier")
        		key = it.supplier.identifier
        	else
        		key = it.purchaseInvoice.supplier.identifier
        	def list = result[key]
        	if(!list) {
        		list = []
        		suppliers.add(key)
        	}
        	list.add(it)
        	result[key] = list
        }
        suppliers.sort()

        def model = [result:result, suppliers:suppliers, modelInstanceList:purchaseInvoices, username:authenticateService.principal()?.username];
        render(view:params.reportType, model:model)

    }

    def retrievePurchaseInvoices(params) {
        def purchaseInvoiceInstance = PurchaseInvoice.createCriteria();
        return purchaseInvoiceInstance.listDistinct {
            if(params.dateType == "invoice"){
                between('invoiceDate', params?.invoiceDateFrom, params?.invoiceDateTo);
            }
            if(params.dateType == "delivery"){
                between('deliveryDate', params?.deliveryDateFrom, params?.deliveryDateTo);
            }
            if(params.supplier?.id){
                and{
                    supplier{
                        if(params?.supplier?.id instanceof String)
                            eq("id", params?.supplier?.id as Long)
                        else
                            'in'("id", params?.supplier?.id.collect{value -> Long.parseLong(value)})
                    }
                }
            }
            if(params.warehouse?.id){
                and{
                    warehouse{
                        if(params?.warehouse?.id instanceof String)
                            eq("id", params?.warehouse?.id as Long)
                        else
                            'in'("id", params?.warehouse?.id.collect{value -> Long.parseLong(value)})
                    }
                }
            }
            if(params.product?.id){
                and{
                    items{
                        purchaseOrderItem{
                            product{
                                if(params?.product?.id instanceof String)
                                    eq("id", params?.product?.id as Long)
                                else
                                    'in'("id", params?.product?.id.collect{value -> Long.parseLong(value)})
                            }
                        }
                    }


                }
            }
        }
    }

    def retrievePurchaseInvoiceItems(params) {
        def purchaseInvoiceInstanceItem = PurchaseInvoiceItem.createCriteria();
        return purchaseInvoiceInstanceItem.listDistinct {
            if(params.dateType == "invoice"){
            	purchaseInvoice{
            		between('invoiceDate', params?.invoiceDateFrom, params?.invoiceDateTo);
            	}
            }
            if(params.dateType == "delivery"){
            	purchaseInvoice{
            		between('deliveryDate', params?.deliveryDateFrom, params?.deliveryDateTo);
            	}
            }
            if(params.supplier?.id){
            	purchaseInvoice{
            		supplier{
            			if(params?.supplier?.id instanceof String)
            				eq("id", params?.supplier?.id as Long)
            			else
            				'in'("id", params?.supplier?.id.collect{value -> Long.parseLong(value)})
            		}
                }
            }
            if(params.warehouse?.id){
            	purchaseInvoice{
            		warehouse{
            			if(params?.warehouse?.id instanceof String)
                        	eq("id", params?.warehouse?.id as Long)
                        else
                        	'in'("id", params?.warehouse?.id.collect{value -> Long.parseLong(value)})
            		}
                }
            }
            if(params.product?.id){
                and{
                    items{
                        purchaseOrderItem{
                            product{
                                if(params?.product?.id instanceof String)
                                    eq("id", params?.product?.id as Long)
                                else
                                    'in'("id", params?.product?.id.collect{value -> Long.parseLong(value)})
                            }
                        }
                    }


                }
            }
        }
    }
}

