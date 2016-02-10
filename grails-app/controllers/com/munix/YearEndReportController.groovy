package com.munix

import groovy.sql.Sql

class YearEndReportController {
	
	def authenticateService
	def dataSource
    
    def search = {
    	def reportTypes = ["detailedSalesReport", "customerBalances"]
       	def reportTypeLabels = ["Detailed Sales Report","Customer Balances"]
       	["reportTypes":reportTypes, "reportTypeLabels":reportTypeLabels]
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
	
	
    def detailedSalesReport = {
	  	def where = "where 1=1"
        def whereParams = []
       	if(params.dateFrom){
    		where += " and date>=?"
    		whereParams.add(params.dateFrom)
    	}
       	if(params.dateTo){
    		where += " and date<=?"
    		whereParams.add(params.dateTo)
    	}
    	if(params.customer?.id){
    		where = appendWhereQuery (params.customer.id, "customer_id", where, whereParams)
    	}
    	if(params.type?.id){
    		where = appendWhereQuery (params.type.id, "customer_type_id", where, whereParams)
    	}
    	if(params.discountType?.id){
    		where = appendWhereQuery (params.discountType.id, "discount_type_id", where, whereParams)
    	}
			
		def query = "select * from detailed_sales_report ${where} order by date"
		println query
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query, whereParams)
        sqlQuery.close();
    	model:["list":result]
    }
	    	
    def customerBalances = {
	  	def where = "where 1=1"
        def whereParams = []
    	if(params.customer?.id){
    		where = appendWhereQuery (params.customer.id, "id", where, whereParams)
    	}
    	if(params.type?.id){
    		where = appendWhereQuery (params.type.id, "customer_type_id", where, whereParams)
    	}
		def query = "select * from customer_balances ${where} order by ${params.sortBy}"
		println query
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query, whereParams)
        sqlQuery.close();
    	model:["list":result]
    }
	    	
}
