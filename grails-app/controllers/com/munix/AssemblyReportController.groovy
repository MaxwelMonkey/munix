package com.munix

import groovy.sql.Sql

class AssemblyReportController {
	
	def authenticateService
	def dataSource
    
    def search = {
       	[:]
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
    	
		def query = "from JobOrder j ${where} order by endDate"
		println query
		def result = [:]
        def jobOrders = JobOut.executeQuery(query,whereParams)
        jobOrders.each { 
			def product = it.product
			def joList = result[product]
			if(!joList)	{
				joList = []
			}
			joList.add(it)
			result[product] = joList
		}
    	model:["list":result]
    }
	    	
}
