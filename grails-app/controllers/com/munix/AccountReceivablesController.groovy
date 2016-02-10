package com.munix

import groovy.sql.Sql

class AccountReceivablesController {

    def dataSource

    
	def index = {
		def query = "SELECT * FROM age_of_receivables";
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query)
        sqlQuery.close();
        [list:result]
    }
    
    def list = {
    	def calFrom = Calendar.getInstance()
		calFrom.add(Calendar.DATE, Integer.parseInt(params.from) * -1)
    	def calTo = Calendar.getInstance()
		calTo.add(Calendar.DATE, Integer.parseInt(params.to) * -1)
		def sds = SalesDelivery.executeQuery("from SalesDelivery sd where sd.invoice.customer.id=? and status = ? and sd.date <? and sd.date >=? order by sd.date",[Long.parseLong(params.id), 'Unpaid', calFrom.getTime(), calTo.getTime()])
        [list:sds]
    }
   	
}