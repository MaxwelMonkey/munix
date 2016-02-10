package com.munix

import groovy.sql.Sql

class CleanStockCardJob {
    
	def cronExpression = "0 0 0 ? * MON-SUN"
		
	def salesOrderService
	def dataSource 

	def execute() {
		println "CLEAN STOCK CARD JOB"
		Sql sql = new Sql(dataSource)
		//sql.execute("")
	}
}
