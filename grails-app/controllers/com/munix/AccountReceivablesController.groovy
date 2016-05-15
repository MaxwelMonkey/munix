package com.munix

import groovy.sql.Sql

class AccountReceivablesController extends ReportController {

    def dataSource
    def customerService

    
	def index = {
		def model = [:]
		params.idField = "id"
	  	buildWhere(params)
       	if(params.city?.id){
       		where = appendWhereQuery (params.city.id, "bus_addr_city_id", where, whereParams)
    	}
    	if(params.region?.id){
    		where = appendWhereQuery (params.region.id, "region_id", where, whereParams)
    	}
    	if(params.status){
    		where = appendWhereQuery (params.status, "status", where, whereParams)
    	}
		def query = "SELECT * FROM age_of_receivables ${where}";
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query, whereParams)
        println query
        sqlQuery.close();
		model["list"] = result
		def df = new Date().minus(200000)
		def dt = new Date().plus(200000)
    	def unpaidSds = customerService.getUnpaidSalesDeliveries(df, dt)
		def unpaidCcs = customerService.getUnpaidCustomerCharges(df, dt)
		def unpaidDms = customerService.getUnpaidDebitMemos(df, dt)
		def unpaidBcs = customerService.getUnpaidBouncedChecks(df, dt)
		def unpaidCps = customerService.getUnpaidCheckPayments(df, dt)
		model["unpaidCcs"] = unpaidCcs
		model["unpaidBcs"] = unpaidBcs
		model
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