package com.munix

import groovy.sql.Sql

class SalesDeliveryMarginReportController {
	
	def authenticateService
	def dataSource
    
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

	def appendWhereQuery (list, columnName, where, whereParams) {
		if(list instanceof String){
            where += " and ${columnName} = ?"
        	whereParams.add(list)
		}else{
            def idsString = ""
        	list.each{
            	if(idsString!="") idsString += ","
            	idsString += "?"
        	    whereParams.add(it)
        	}
            where += " and ${columnName} in (${idsString})"
		}
		return where
	}
	
	def appendWhereQueryLong (list, columnName, where, whereParams) {
		if(list instanceof String){
            where += " and ${columnName} = ?"
        	whereParams.add(Long.parseLong(list))
		}else{
            def idsString = ""
        	list.each{
            	if(idsString!="") idsString += ","
            	idsString += "?"
        	    whereParams.add(Long.parseLong(it))
        	}
            where += " and ${columnName} in (${idsString})"
		}
		return where
	}
	
	
    def list = {
	  	def where = "where 1=1"
        def whereParams = []
       	if(params.dateFrom){
    		where += " and j.endDate>=?"
    		whereParams.add(params.dateFrom)
    	}
       	if(params.dateTo){
    		where += " and j.startDate<=?"
    		whereParams.add(params.dateTo)
    	}
    	if(params.product?.id){
    		where = appendWhereQueryLong (params.product.id, "j.product.id", where, whereParams)
    	}
    	
		def query = "from SalesDelivery sd ${where}"
		println query
		def result = [:]
        def salesDeliveries = SalesDelivery.executeQuery(query,whereParams)
    	model:["list":salesDeliveries]
    }
	    	
}
