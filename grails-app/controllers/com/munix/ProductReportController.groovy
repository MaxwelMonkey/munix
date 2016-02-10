package com.munix

import groovy.sql.Sql

class ProductReportController extends ReportController{

    def dataSource
    def sessionFactory
    
    
	def search = {
    	def reportTypes = ["productPricelist","productListSelling","productInventoryList","productListCost","inventoryReport"]
	    	def reportTypeLabels = ["Product Pricelist","Product Pricelist (w/ Picture)","Product Inventory List","Product List (Local Cost)","Inventory Report (Foreign Cost)"]
	    def result = Product.findAll()
    	System.gc()
	    ["reportTypes":reportTypes, "reportTypeLabels":reportTypeLabels, "productList":result]
    }
	
	
	def report = {
		def tableName = getTableName(params.reportType)
	  	buildWhere(params)
		if(params.currency){
    		if(params.currency!=""){
    			tableName = tableName=="product"?"PRODUCT_LIST_COST_REPORT":tableName
    		}
    	}


		def query = "SELECT * FROM ${tableName} product ${where} order by identifier";
		if(tableName=="PRODUCT_LIST_COST_REPORT") query = "SELECT * FROM ${tableName} a, PRODUCT_LIST product ${where} and a.id=product.id order by a.identifier";
        println "QUERY:"+query
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query,whereParams)
        sqlQuery.close();
        println "DONE"
        if(params.reportType=="productCostVsSelling"){
        	def marginsMap=[:]
        	DiscountType.list().each{
        		marginsMap.put(it.id, it)
        	}
        	
        	if(!params.listSize){
	        	query = "SELECT count(1) CNT FROM ${tableName} a, PRODUCT_LIST product ${where} and a.id=product.id"
	            sqlQuery = new Sql(dataSource)
	            def countResult = sqlQuery.rows(query,whereParams)
	            sqlQuery.close();
        		countResult.each{
        			params.listSize = it["CNT"]
        		}
        	}
    		render(view:params.reportType, model:["list":result, marginsMap:marginsMap, "listSize":params.listSize])
        }else{
        	System.gc()
        	render(view:params.reportType, model:["list":result])
        }
	}
	
	def productCostVsSelling = {
			def tableName = getTableName(params.reportType)
		  	def where = "where 1=1"
	        def whereParams = []
	        if(params.id){
	        	where = appendWhereQuery (params.id, "product.id", where, whereParams)
	        }
			if(params.status){
	    		if(params.status!=""){
		    		where += " and status=?"
		    		whereParams.add(params.status)
	    		}
	    	}
			if(params.currency){
	    		if(params.currency!=""){
	    			tableName = tableName=="product"?"PRODUCT_LIST_COST_REPORT":tableName
		    		where += " and currency=?"
		    		whereParams.add(params.currency)
	    		}
	    	}
			if(params.isNet){
	    		if(params.isNet!=""){
		    		where += " and is_net=true"
	    		}
	    	}
	    	if(params.component){
	    		if(params.component!=""){
	    			where += " and exists (select 1 from product_component c where c.COMPONENT_ID = product.ID)"
	    		}
	    	}
			if(params.forSale){
	    		if(params.forSale!=""){
		    		where += " and is_for_sale=true"
	    		}
	    	}
			if(params.forAssembly){
	    		if(params.forAssembly!=""){
		    		where += " and is_for_assembly=true"
	    		}
	    	}
	    	if(params.supplier){
	    		if(params.supplier instanceof String){
	    			where += " and exists (select 1 from supplier_item c where c.PRODUCT_ID = product.ID AND c.SUPPLIER_ID = ?)"
	            	whereParams.add(params.supplier)
	    		}else{
	                def idsString = ""
	            	params.supplier.each{
	                	if(idsString!="") idsString += ","
	                	idsString += "?"
	            	    whereParams.add(it)
	            	}
	    			where += " and exists (select 1 from supplier_item c where c.PRODUCT_ID = product.ID AND c.SUPPLIER_ID in (${idsString}))"
	    		}
	    	}
	    	if(params.customer){
	    		where = appendWhereQuery (params.customer, "customer_id", where, whereParams)
	    	}
	    	if(params.type){
	    		where = appendWhereQuery (params.type, "type_id", where, whereParams)
	    	}
	       	if(params.category){
	       		where = appendWhereQuery (params.category, "category_id", where, whereParams)
	    	}
	       	if(params.subcategory){
	       		where = appendWhereQuery (params.subcategory, "subcategory_id", where, whereParams)
	    	}
	       	if(params.brand){
	       		where = appendWhereQuery (params.brand, "brand_id", where, whereParams)
	    	}
	       	if(params.model){
	       		where = appendWhereQuery (params.model, "model_id", where, whereParams)
	    	}
			if(params.itemType){
				where = appendWhereQuery (params.itemType, "item_type_id", where, whereParams)
	    	}
			if(params.balance!='null' && params.warehouse){
				if(params.warehouse instanceof String){
					where += " and exists (select 1 from stock where stock.PRODUCT_ID = product.ID AND stock.WAREHOUSE_ID = ?"
					whereParams.add(params.warehouse)
				}else{
		            def idsString = ""
	            	params.warehouse.each{
	                	if(idsString!="") idsString += ","
	                	idsString += "?"
	            	    whereParams.add(it)
	            	}
					where += " and exists (select 1 from stock where stock.PRODUCT_ID = product.ID AND stock.WAREHOUSE_ID in (${idsString})"
				}
				where += " AND stock.QTY ${params.balance})"
			}
			if(!params.offset) params.offset = 0 
			if(!params.max) params.max = 100 
			def startNum = params.offset
			def endNum = params.max
			def query = "SELECT * FROM PRODUCT_LIST_COST_REPORT a, PRODUCT_LIST product ${where} and a.id=product.id order by a.identifier limit ${startNum},${endNum}";
	        println "QUERY:"+query
	        Sql sqlQuery = new Sql(dataSource)
	        def result = sqlQuery.rows(query,whereParams)
	        sqlQuery.close();
	        println "DONE"
        	def marginsMap=[:]
        	DiscountType.list().each{
        		marginsMap.put(it.id, it)
        	}
        	
        	if(!params.listSize){
	        	query = "SELECT count(1) CNT FROM PRODUCT_LIST_COST_REPORT a, PRODUCT_LIST product ${where} and a.id=product.id"
	            sqlQuery = new Sql(dataSource)
	            def countResult = sqlQuery.rows(query,whereParams)
	            sqlQuery.close();
        		countResult.each{
        			params.listSize = it["CNT"]
        		}
        	}
        	System.gc()
    		["list":result, marginsMap:marginsMap, "listSize":params.listSize]
		}
		
	def getTableName(reportType){
		if(reportType=="productListCost" || reportType=="productCostVsSelling"){
			return "PRODUCT_LIST_COST_REPORT"
		}else if(reportType=="productInventoryList"){
			return "INVENTORY_REPORT"
		}else if(reportType=="inventoryReport"){
			//return "INVENTORY_REPORT_FOREIGN"
			return "INVENTORY_REPORT_FOREIGN"
		}else{
			return "PRODUCT_LIST"
		}
	}
	
	def retrieveRunningBalance = {
			try{
				def date = Date.parse("MM/dd/yyyy",params.date)
				def productId = params.productId
				Sql sqlQuery = new Sql(dataSource)
				def result = sqlQuery.rows("select coalesce(balance, 0) bal from stock_card_item a, stock_card b where a.stock_card_id = b.id " +
						"and product_id = ? and date_posted < ? order by date_posted desc limit 1",[productId, date + 1])
		        sqlQuery.close();
				def balance = 0.00
				result.each{
					balance = it.bal
				}
				render String.format('%,.0f',balance)
			}catch(Exception e){
				e.printStackTrace()
				render "X"
			}
		}
		
	def retrieveRunningWarehouseBalance = {
			try{
				def date = Date.parse("MM/dd/yyyy",params.date)
				def productId = params.productId
				def warehouse = params.warehouse
				Sql sqlQuery = new Sql(dataSource)
				def result = sqlQuery.rows("select coalesce(i.qi,0) - coalesce(o.qo,0) bal from ( select sum(a.qty_in) qi from stock_card_item a, stock_card b where a.stock_card_id=b.id and b.product_id=? and date_posted < ? and warehouse_in = ?) i, " +
						"(select sum(a.qty_out) qo from stock_card_item a, stock_card b where a.stock_card_id=b.id and b.product_id=? and date_posted < ? and warehouse_out = ?) o;",
						[productId, date + 1, warehouse, productId, date + 1, warehouse])
		        sqlQuery.close();
				def balance = 0.00
				result.each{
					balance = it.bal
				}
				render String.format('%,.0f',balance)
			}catch(Exception e){
				e.printStackTrace()
				render "X"
			}
		}
		
	def retrieveRunningCost = {
		try{
			def date = Date.parse("MM/dd/yyyy",params.date)
			def productId = params.productId
			Sql sqlQuery = new Sql(dataSource)
			def result = sqlQuery.rows("select a.currency, coalesce(cost_foreign, 0) cost from stock_cost_items a " +
					"where product_id = ? and date < ? and currency is not null and cost_foreign is not null order by date desc limit 1",[productId, date + 1])
	        sqlQuery.close();
			def cost = 0.00
			def curr = ""
			result.each{
				cost = it.cost
				curr = it.currency
			}
			render curr + "::" + String.format('%,.2f',cost)
		}catch(Exception e){
			e.printStackTrace();
			render "X"
		}
	}
    	
}