package com.munix

import grails.converters.*

class ProductController {

    static allowedMethods = [save: "POST", update: "POST"]
                             
	def productService;
    def authenticateService
    def FIELDS = ["brand","category","color","material","model","subcategory","type","unit","itemType","isComponent","isForSale","isForAssembly","status"]

	
	def index = {
		redirect(action: "list", params: params)
	}
	
    def viewImage = {
        def productInstance = Product.get(params.id)
        byte []img = productInstance.photo
        //response.contentType = "image/jpeg"
        response.outputStream << img
    }
	
	def editProductComponent = {
		def productInstance = Product.get(params.id)
		if (productInstance) {
			return [productInstance: productInstance]
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
		}
	}
	def auditLogs = {
        def productInstance = Product.get(params.id)
		if (productInstance) {
			return [productInstance: productInstance]
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
		}
    }
	def generateProductComponentTable = { 
		def searchField = params.sSearch
		def products = productService.getAllComponents(searchField)
		def data = createProductComponentsDataMap(products)

		def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
		render jsonResponse as JSON
	}

	private List createProductComponentsDataMap(List products) {
		def data = []
		def productId = params.productId
		products?.each {
			def dataMap = []
			dataMap.addAll(
					it.id,
					it.identifier,
					it.description,
					BigDecimal.ONE,
					it?.unit?.toString() ?: "",
					productId
					)
			data.add(dataMap)
		}
		return data
	}
	
	def updateProductComponent = {
		def productInstance = Product.get(params.id)
		productInstance?.properties = params

		def isComponentsEmpty = false
		def toBeDeleted = productInstance?.components.findAll {it.isDeleted}
		if (toBeDeleted) {
			if(toBeDeleted.size() >= productInstance?.components.size()) {
				isComponentsEmpty = true
			}
			else {
				toBeDeleted.each { deleted->
					productInstance.removeFromComponents(deleted)
					deleted.delete()
				}
				productInstance?.components?.removeAll(toBeDeleted)
			}
		}
			
		if(isComponentsEmpty) {
			flash.error = "Product not updated! Must have at least one component."
		} else {
			if (productInstance.save(flush: true, cascade : true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'product.label', default: 'Product'), productInstance.id])}"
				redirect(action: "show", id: productInstance.id)
				return
			} else {
				flash.error = "Product not updated!"
			}
		}
		redirect(action: "show", id: productInstance.id)
		return
	}

    def list = {
        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0
        if (!params.order) params.order = "asc"
        if (params.searchStatus==null) params.searchStatus = "Active"

        def productInstanceList = productService.generateList(params)
        
        def pendingMap = [:]
        def poPendingMap = [:]
        productInstanceList.each{
        	def product = it
        	def items = SalesOrderItem.findAllByProduct(product)
        	def pending = 0
        	items.each{
        		if(it.invoice.status=="Approved"){
        			if(it.qty!=it.deliveredQty){
        				pending += it.qty - it.deliveredQty
        			}
        		}
        	}
			pendingMap[product.id] = pending

        	items = PurchaseOrderItem.findAllByProduct(product)
        	pending = 0
        	items.each{
        		if(it.po?.status=="Approved"){
        			if(it.qty!=it.receivedQty){
        				pending += it.qty - it.receivedQty
        			}
        		}
        	}
			poPendingMap[product.id] = pending
        }
        [productInstanceList: productInstanceList, productInstanceTotal: productInstanceList.totalCount, itemTypes: ItemType.list().sort{it.toString()}, discountTypes: DiscountType.list().sort{it.toString()}, pendingMap:pendingMap, poPendingMap:poPendingMap]
    }
	
	def pendingPo = {
		def product = Product.get(params.id)
        def poList = PurchaseOrderItem.executeQuery("from PurchaseOrderItem i where product = :product and qty-receivedQty!=0 and po.status=:approved order by i.po.date",["product":product, "approved":"Approved"])
        def unapprovedPoList = PurchaseOrderItem.executeQuery("from PurchaseOrderItem i where product = :product and po.status=:unapproved order by i.po.date",["product":product, "unapproved":"Unapproved"])
        ["product":product, "pendingPo":poList,"pendingUnapprovedPo":unapprovedPoList]
	}

	def pendingSo = {
		def product = Product.get(params.id)
        def soList = SalesOrderItem.executeQuery("from SalesOrderItem i where product = :product and qty-deliveredQty!=0 and invoice.status=:approved order by i.invoice.date",["product":product, "approved":"Approved"])
        def unapprovedSoList = SalesOrderItem.executeQuery("from SalesOrderItem i where product = :product and invoice.status=:unapproved order by i.invoice.date",["product":product, "unapproved":"Unapproved"])
        ["product":product, "pendingSo":soList, "pendingUnapprovedSo":unapprovedSoList]
	}

    def create = {
        def productInstance = new Product()
		def warehouses = productService.generateWarehouses(productInstance)
        productInstance.properties = params
        return [productInstance: productInstance, warehouses: warehouses]
    }

    def save = {
        def productInstance = new Product(params)
		def itemLocationParam = request.getParameterValues('itemLocation').collect {it.toString()}
		productInstance?.itemLocations = productService.constructItemLocationList(itemLocationParam)
		
        if (productInstance.save(flush: true)) {
        	productService.createStockCardForNewProduct(productInstance)
        	productService.createStocksForNewProduct(productInstance)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'product.label', default: 'Product'), productInstance.id])}"
            redirect(action: "show", id: productInstance.id)
        }
        else {
            render(view: "create", model: [productInstance: productInstance])
        }
    }

    def show = {
        def productInstance = Product.get(params.id)
		
		def warehouses = productService.generateWarehouses(productInstance)
		
        if (productInstance) {
            [productInstance: productInstance, warehouses: warehouses, stocks : productInstance.stocks.sort{it.id}]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        
        def productInstance = Product.get(params.id)
		def warehouses = productService.generateWarehouses(productInstance)
        if (productInstance) {
            return [productInstance: productInstance, warehouses: warehouses]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def editMultiple = {
		//[products:Product.findAll([sort:"identifier"])]
		[products:[]]
	}
	
	def editMultiple2 = {
			def products = []
			def productIds = []
			if(params.product?.id){
				if(params.product.id instanceof String){
					productIds.add(Long.parseLong(params.product.id))
				}else{
					params.product?.id.each{
						productIds.add(Long.parseLong(it))
					}
				}
				
				products = Product.executeQuery("from Product p where p.id in (:ids)",[ids:productIds])
			}
			def fields = []
			if(params.field instanceof String)
				fields.add(params.field)
			else
				fields = params.field
			[products:products, productIds:productIds, fields:fields]
		}
		
	def editMultiple3 = {
		def approval = ApprovalProcess.get(Long.parseLong(params.id))
		def approvalProducts = ApprovalProductUpdate.findAllByApprovalProcess(approval)
		def fieldLabels = [:]
		fieldLabels["brand"] = "Brand"
		fieldLabels["category"] = "Category"
		fieldLabels["color"] = "Color"
		fieldLabels["material"] = "Material"
		fieldLabels["model"] = "Model"
		fieldLabels["subcategory"] = "Subcategory"
		fieldLabels["type"] = "Type"
		fieldLabels["unit"] = "Unit"
		fieldLabels["itemType"] = "Item Type"
		fieldLabels["isComponent"] = "Component"
		fieldLabels["isForSale"] = "For Sale"
		fieldLabels["isForAssembly"] = "For Assembly"
		fieldLabels["status"] = "Status"
		[approval:approval, approvalProducts:approvalProducts, fields:FIELDS, fieldLabels:fieldLabels]
	}
		
	def updateMultiple = {
		def productIds = []
		if(params.product instanceof String){
			productIds.add(Long.parseLong(params.product))
		}else{
			params.product?.each{
				productIds.add(Long.parseLong(it))
			}
		}
		
        def user = authenticateService.userDomain()
       	def approval = new ApprovalProcess(description:"Multiple Products for Update", requestedBy:user, date:new Date(), type:"Product", referenceNumber:null)
        approval.save(flush:true)
		def products = Product.executeQuery("from Product p where p.id in (:ids)",[ids:productIds])
		products.each{
			def product = it
	        def approvalProduct = new ApprovalProductUpdate()
	        if(params.brand!=null && product.brand?.id!=params.brand?.id) { approvalProduct.brand = ProductBrand.get(Long.parseLong(params.brand?.id))} 
	        if(params.category!=null && product.category?.id!=params.category?.id) { approvalProduct.category = ProductCategory.get(Long.parseLong(params.category?.id))} 
	        if(params.color!=null && product.color?.id!=params.color?.id) { approvalProduct.color = ProductColor.get(Long.parseLong(params.color?.id))} 
	        if(params.material!=null && product.material?.id!=params.material?.id) { approvalProduct.material = ProductMaterial.get(Long.parseLong(params.material?.id))} 
	        if(params.model!=null && product.model?.id!=params.model?.id) { approvalProduct.model = ProductModel.get(Long.parseLong(params.model?.id))} 
	        if(params.subcategory!=null && product.subcategory?.id!=params.subcategory?.id) { approvalProduct.subcategory = ProductSubcategory.get(Long.parseLong(params.subcategory?.id))} 
	        if(params.type!=null && product.type?.id!=params.type?.id) { approvalProduct.type = DiscountType.get(Long.parseLong(params.type?.id))} 
	        if(params.unit!=null && product.unit?.id!=params.unit?.id) { approvalProduct.unit = ProductUnit.get(Long.parseLong(params.unit?.id))} 
	        if(params.itemType!=null && product.itemType?.id!=params.itemType?.id) { approvalProduct.itemType = ItemType.get(Long.parseLong(params.itemType?.id))} 
	        if(params.isComponent!=null && product.isComponent!=params.isComponent) { approvalProduct.isComponent = params.isComponent} 
	        if(params.isForSale!=null && product.isForSale!=params.isForSale) { approvalProduct.isForSale = params.isForSale} 
	        if(params.isForAssembly!=null && product.isForAssembly!=params.isForAssembly) { approvalProduct.isForAssembly = params.isForAssembly} 
	        if(params.status!=null && product.status!=params.status) { approvalProduct.status = params.status}

	        approvalProduct.product = product
	    	approvalProduct.approvalProcess = approval
	    	if(!approvalProduct.save()){
	    		approvalProduct.errors.allErrors.each{println it}
	    	}
        }
/*			product.properties = params
           productService.generateAuditTrails(product)
			
			if(product.save()){
				
				println "SAVED"
			}else{
				product.errors.allErrors.each{
					println it
				}
			}
		}
		flash.message = "Products have been updated successfully."
		*/
        
        flash.message = "Product update has been sent for approval."

		redirect action:"editMultiple3", id:approval.id
	}
	
	def rejectUpdateMultiple= {
		def a = ApprovalProcess.get(Long.parseLong(params.id))
		a.status="Rejected"
		a.approvedDate = new Date()
		a.save(flush:true)
		flash.message = "Product updates have been rejected."
		redirect action:"list"
	}
	
	def approveUpdateMultiple= {
		def a = ApprovalProcess.get(Long.parseLong(params.id))
    	a.status = "Approved"
    	a.approvedBy = authenticateService.userDomain()
    	a.approvedDate = new Date()
    	a.save(flush:true)
		def approvalProducts = ApprovalProductUpdate.findAllByApprovalProcess(a)
		approvalProducts.each{
			def approvalProduct = it
			def product = approvalProduct.product
			FIELDS.each{
				if(approvalProduct[it]!=null) {
					product[it] = approvalProduct[it]
				}
			}
	        productService.generateAuditTrails(product)
			
			if(product.save()){
				
				println "SAVED"
			}else{
				product.errors.allErrors.each{
					println it
				}
			}
		}
		flash.message = "Products have been updated successfully."
		redirect action:"list"
	}
	
	def productList = {
		def result = []
		def products = Product.findAllByIdentifierLike("%${params.term}%")
		def i = 0
		products.each{
			result[i]=[:]
			result[i]["identifier"] = it.identifier;
			result[i]["id"] = it.id;
			result[i]["type"] = it.type?.identifier;
			result[i]["itemType"] = it.itemType?.identifier;
			result[i]["unit"] = it.unit?.identifier;
			result[i]["category"] = it.category?.identifier;
			result[i]["subcategory"] = it.subcategory?.identifier;
			result[i]["brand"] = it.brand?.identifier;
			result[i]["model"] = it.model?.identifier;
			result[i]["material"] = it.material?.identifier;
			result[i]["color"] = it.color?.identifier;
			result[i]["isComponent"] = it.isComponent;
			result[i]["isForAssembly"] = it.isForAssembly;
			result[i]["isForSale"] = it.isForSale;
			result[i]["status"] = it.status;
			i++
		}
		render result as JSON
	}
	
	def productListByKeyword = {
		def warehouses = Warehouse.list()
		def result = []
		try{
		def products = Product.executeQuery("select p from Product p LEFT JOIN p.type t LEFT JOIN p.itemType it LEFT JOIN p.unit u LEFT JOIN p.category c LEFT JOIN p.subcategory sc LEFT JOIN p.brand b LEFT JOIN p.model m LEFT JOIN p.material ma LEFT JOIN p.color co where p.identifier like '%${params.term}%' or t.identifier like '%${params.term}%' or it.identifier like '%${params.term}%' or u.identifier like '%${params.term}%' or c.identifier like '%${params.term}%' or sc.identifier like '%${params.term}%' or b.identifier like '%${params.term}%' or m.identifier like '%${params.term}%' or ma.identifier like '%${params.term}%' or co.identifier like '%${params.term}%'")
		def i = 0
		products.each{
			result[i]=[:]
			result[i]["identifier"] = it.identifier;
			result[i]["id"] = it.id;
			result[i]["type"] = it.type?.identifier;
			result[i]["itemType"] = it.itemType?.identifier;
			result[i]["unit"] = it.unit?.identifier;
			result[i]["category"] = it.category?.identifier;
			result[i]["subcategory"] = it.subcategory?.identifier;
			result[i]["brand"] = it.brand?.identifier;
			result[i]["model"] = it.model?.identifier;
			result[i]["material"] = it.material?.identifier;
			result[i]["color"] = it.color?.identifier;
			result[i]["isComponent"] = it.isComponent;
			result[i]["isForAssembly"] = it.isForAssembly;
			result[i]["isForSale"] = it.isForSale;
			result[i]["status"] = it.status;
			def product = it
			warehouses.each{
				def positive=""
				if(product.getStock(it).qty>0){
					positive="positive"
				}else if (product.getStock(it).qty<0){
					positive="negative"
				}
				result[i]["warehouse"+it.id] = "<span class='"+positive+"'>"+product.formatSOH(it)+"</span>"
			}
			i++
		}
		}catch(e){
			e.printStackTrace()
		}
		render result as JSON
	}
	
    def update = {
        def productInstance = Product.get(params.id)

        if (productInstance) {
			if (params.isComponent && productService.checkIfProductComponentExistForProduct(params.isComponent, productInstance)) {
				flash.message = "Product is used as a component of another product. Use as Component cannot be set to false."
				render(view: "edit", model: [productInstance: productInstance])
				return
			}
			
			def itemLocationParam = request.getParameterValues('itemLocation').collect {it.toString()}
			productInstance?.itemLocations = productService.constructItemLocationList(itemLocationParam)
			
            if (params.version) {
                def version = params.version.toLong()
                if (productInstance.version > version) {
                    
                    productInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'product.label', default: 'Product')] as Object[], "Another user has updated this Product while you were editing")
                    render(view: "edit", model: [productInstance: productInstance])
                    return
                }
            }
            productInstance.properties = params
            productService.generateAuditTrails(productInstance)
            if (!productInstance.hasErrors() && productInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'product.label', default: 'Product'), productInstance.id])}"
                redirect(action: "show", id: productInstance.id)
            }
            else {
                render(view: "edit", model: [productInstance: productInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
        }
    }

	def viewPhoto = {
		def productInstance = Product.get(params.id)
		
		if (productInstance) {
			[productInstance: productInstance]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
			redirect(action: "list")
		}
	}

	def viewPriceAdjustmentHistory = {
		def productInstance = Product.get(params.id)
		
		if (productInstance) {
			def appliedPriceAdjustmentItems = productService.generateAppliedPriceAdjustmentItemsForProduct(productInstance)
			[productInstance: productInstance, appliedPriceAdjustmentItems: appliedPriceAdjustmentItems]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
			redirect(action: "list")
		}
	}
	
	def viewSellingHistory = {
        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0
		if (!params.order) params.order = "asc"
		
		def productInstance = Product.get(params.id)
		
		if (productInstance) {
			def approvedSalesDeliveries = productService.generateApprovedSalesDeliveriesForProduct(params)
			[productInstance: productInstance, approvedSalesDeliveries: approvedSalesDeliveries, deliveriesTotal: approvedSalesDeliveries.totalCount]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
			redirect(action: "list")
		}
	}
	
}
