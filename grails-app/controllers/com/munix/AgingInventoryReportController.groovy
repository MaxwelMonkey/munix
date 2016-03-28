package com.munix

import groovy.sql.Sql

class AgingInventoryReportController extends ReportController{

    def dataSource

    def search = {
    		
    }
    
	def list = {
		if(!params.status) params.status='Active'
	  	buildWhere(params)
		def query = "SELECT * FROM age_of_inventory product ${where}";
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query, whereParams)
        sqlQuery.close();
        [list:result]
    }
        
}