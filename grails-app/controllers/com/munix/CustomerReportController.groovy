package com.munix

import groovy.sql.Sql

class CustomerReportController {
	
	def authenticateService
	def dataSource
    
    def search = {
    	def reportTypes = ["customerList", "topCustomersReport"]
       	def reportTypeLabels = ["Customer List","Top Customers Report"]
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
	
	
    def customerList = {
	  	def where = "where 1=1"
        def whereParams = []
    	if(params.id){
    		where = appendWhereQuery (params.id, "c.id", where, whereParams)
    	}
    	if(params.salesAgent?.id){
    		where = appendWhereQuery (params.salesAgent.id, "sales_agent_id", where, whereParams)
    	}
    	if(params.type?.id){
    		where = appendWhereQuery (params.type.id, "type_id", where, whereParams)
    	}
    	if(params.city?.id){
    		where = appendWhereQuery (params.city.id, "bus_addr_city_id", where, whereParams)
    	}
    	if(params.region?.id){
    		where = appendWhereQuery (params.region.id, "region_id", where, whereParams)
    	}
    	if(params.status){
    		where = appendWhereQuery (params.status, "status", where, whereParams)
    	}
		def query = "select c.*, t.identifier terms, ct.identifier city, p.identifier province, r.identifier region, s.first_name, s.last_name " +
				"from customer c left join sales_agent s on c.sales_agent_id = s.id " +
				"left join term t on c.term_id = t.id " +
				"left join city ct on c.bus_addr_city_id=ct.id " +
				"left join province p on ct.province_id = p.id " +
				"left join region r on p.region_id = r.id " +
				"${where} order by identifier"
		println query
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query, whereParams)
        sqlQuery.close();
    	model:["list":result]
    }
	    	
    def topCustomersReport = {
	  	def where = "where 1=1"
        def whereParams = []
        def ccDate = ""
     	if(params.dateFrom){
       		ccDate += " and b.date>=? "
       		whereParams.add(params.dateFrom)
       	}
      	if(params.dateTo){
       		ccDate += " and b.date<=? "
       		whereParams.add(params.dateTo)
       	}
       	if(params.id){
    		where = appendWhereQuery (params.id, "a.id", where, whereParams)
    	}
    	if(params.salesAgent?.id){
    		where = appendWhereQuery (params.salesAgent.id, "a.sales_agent_id", where, whereParams)
    	}
    	if(params.type?.id){
    		where = appendWhereQuery (params.type.id, "a.type_id", where, whereParams)
    	}
    	if(params.city?.id){
    		where = appendWhereQuery (params.city.id, "bus_addr_city_id", where, whereParams)
    	}
    	if(params.region?.id){
    		where = appendWhereQuery (params.region.id, "region_id", where, whereParams)
    	}
    	if(params.status){
    		where = appendWhereQuery (params.status, "a.status", where, whereParams)
    	}
    	
    	def totalPurchasesBalanceNet = getTotalPurchasesBalance(params.dateFrom, params.dateTo, true)
    	def totalPurchasesBalanceDiscounted = getTotalPurchasesBalance(params.dateFrom, params.dateTo, false)
    	def amountPaid = getAmountPaid(params.dateFrom, params.dateTo)
    	def customerCharges = getCustomerCharges(params.dateFrom, params.dateTo)
    	def bouncedChecks = getBouncedChecks(params.dateFrom, params.dateTo)
    	def miscChargesBalance = getMiscChargesBalance(params.dateFrom, params.dateTo)
    	def totalPendingPayments = getTotalPendingPayments(params.dateFrom, params.dateTo)
		def query = "select id, identifier, name, sum(total_purchases) total_purchases from " +
				"(select a.id, a.identifier, a.name, sum(coalesce(c.qty,0) * coalesce(c.price-(c.price*(coalesce(so.net_discount,0)/100)),0)) total_purchases " +
				"from customer a left join sales_delivery b on a.id = b.customer_id ${ccDate} " +
				"left join sales_delivery_item c on b.id = c.delivery_id " +
				"join sales_order_item soi on c.order_item_id = soi.id and soi.is_net is true " +
				"left join sales_order so on b.invoice_id = so.id " +
				"left join city ct on a.bus_addr_city_id = ct.id " +
				"left join province p on ct.province_id = p.id " +
				where +
				" group by a.id " +
				"union " +
				"select a.id, a.identifier, a.name, sum(coalesce(c.qty,0) * coalesce(c.price-(c.price*(coalesce(so.discount,0)/100)),0)) total_purchases " +
				"from customer a left join sales_delivery b on a.id = b.customer_id ${ccDate} " +
				"left join sales_delivery_item c on b.id = c.delivery_id " +
				"join sales_order_item soi on c.order_item_id = soi.id and soi.is_net is false " +
				"left join sales_order so on b.invoice_id = so.id " +
				"left join city ct on a.bus_addr_city_id = ct.id " +
				"left join province p on ct.province_id = p.id " +
				where +
				" group by a.id " +
				") tp " +
				"group by id, identifier, name order by total_purchases desc"
		println query
		def whereParams2 = whereParams.clone()
		whereParams.addAll(whereParams2)
        Sql sqlQuery = new Sql(dataSource)
        def result = sqlQuery.rows(query, whereParams)
        sqlQuery.close();
    	model:["list":result, "totalPurchasesBalanceNet":totalPurchasesBalanceNet, "totalPurchasesBalanceDiscounted":totalPurchasesBalanceDiscounted, "amountPaid":amountPaid, "customerCharges":customerCharges, "bouncedChecks":bouncedChecks, "miscChargesBalance":miscChargesBalance, "totalPendingPayments":totalPendingPayments]
    }
    
    def getTotalPurchases(dateFrom, dateTo) {
	  	def where = ""
        def whereParams = []
       	if(dateFrom){
    		where += "and b.date>=? "
    		whereParams.add(params.dateFrom)
    	}
       	if(dateTo){
       		where += "and b.date<=? "
    		whereParams.add(params.dateTo)
    	}
    	
		def query = "select a.id, sum(coalesce(c.qty,0) * coalesce(c.price,0)) total_purchases " +
		"from customer a left join sales_delivery b on a.id = b.customer_id" +
		"left join sales_delivery_item c on b.id = c.delivery_id " +
		"where 1=1 " +
		where +
		"group by a.id "
		println query
		Sql sqlQuery = new Sql(dataSource)
		def result = sqlQuery.rows(query, whereParams)
		sqlQuery.close();
		def output = [:]
		def keys = []
		result.each{
			keys.add(it["id"])
			output[it["id"]] = it["total_purchases"]
		}
		["keys":keys, "output":output]
    }
	    	
    def getTotalPurchasesBalance(dateFrom, dateTo, net) {
	  	def where = " and e.is_net = ? "
        def whereParams = [net]
       	if(dateFrom){
    		where += "and b.date>=? "
    		whereParams.add(params.dateFrom)
    	}
       	if(dateTo){
       		where += "and b.date<=? "
    		whereParams.add(params.dateTo)
    	}
    	
       	def discountField = net?"net_discount":"discount"
		def query = "select tp.customer_id, sum(total_purchases) total_purchases from (select b.customer_id, sum(c.qty * c.price) - (sum(c.qty * c.price) * coalesce(d.${discountField},0)/100) total_purchases " +
		"from sales_delivery b, sales_delivery_item c, sales_order d, sales_order_item e " +
		"where b.id = c.delivery_id and c.order_item_id = e.id and e.invoice_id = d.id and b.invoice_id = d.id " +
		"and b.status='Unpaid' " +
		where +
		"group by b.id) tp group by customer_id"
		println query
		Sql sqlQuery = new Sql(dataSource)
		def result = sqlQuery.rows(query, whereParams)
		sqlQuery.close();
		def output = [:]
		result.each{
			output[it["customer_id"]] = it["total_purchases"]
		}
		output
    }
	    	
    def getAmountPaid(dateFrom, dateTo) {
	  	def where = ""
        def whereParams = []
       	if(dateFrom){
    		where += "and b.date>=? "
    		whereParams.add(params.dateFrom)
    	}
       	if(dateTo){
       		where += "and b.date<=? "
    		whereParams.add(params.dateTo)
    	}
    	
		def query = "select customer_id, sum(amount_paid) amount_paid " +
		"from sales_delivery b " +
		"where b.status='Unpaid' " +
		where +
		"group by customer_id"
		println query
		Sql sqlQuery = new Sql(dataSource)
		def result = sqlQuery.rows(query, whereParams)
		sqlQuery.close();
		def output = [:]
		result.each{
			output[it["customer_id"]] = it["amount_paid"]
		}
		output
    }
	    	
    def getCustomerCharges(dateFrom, dateTo) {
        def ccDate = ""
        def whereParams = []
      	if(dateFrom){
    		ccDate += "and c.date>=? "
    		whereParams.add(params.dateFrom)
    	}
       	if(dateTo){
    		ccDate += "and c.date<=? "
    		whereParams.add(params.dateTo)
    	}
    	
		def query = "select a.id, sum(coalesce(c.amount_paid,0)) customer_charges " +
		"from customer a left join customer_charge c on a.id = c.customer_id and c.status='Paid' ${ccDate} " +
		"group by a.id "
		println query
		Sql sqlQuery = new Sql(dataSource)
		def result = sqlQuery.rows(query, whereParams)
		sqlQuery.close();
		def output = [:]
		result.each{
			output[it["id"]] = it["customer_charges"]
		}
		output
    }
	    	
    def getBouncedChecks(dateFrom, dateTo) {
        def bcDate = ""
        def whereParams = []
      	if(dateFrom){
            bcDate += "and e.date>=? "
    		whereParams.add(params.dateFrom)
    	}
       	if(dateTo){
            bcDate += "and e.date<=? "
    		whereParams.add(params.dateTo)
    	}
    	
		def query = "select a.id, sum(coalesce(e.amount_paid,0)) bounced_checks " +
		"from customer a left join bounced_check e on a.id = e.customer_id and e.status='Approved' ${bcDate} " +
		"group by a.id "
		println query
		Sql sqlQuery = new Sql(dataSource)
		def result = sqlQuery.rows(query, whereParams)
		sqlQuery.close();
		def output = [:]
		result.each{
			output[it["id"]] = it["bounced_checks"]
		}
		println result
		output
    }
	    	
    def getMiscChargesBalance(dateFrom, dateTo) {
        def ccDate = ""
        def bcDate = ""
        def whereParams = []
      	if(dateFrom){
    		ccDate += "and c.date>=? "
    		whereParams.add(params.dateFrom)
    	}
       	if(dateTo){
    		ccDate += "and c.date<=? "
    		whereParams.add(params.dateTo)
    	}
    	
		def query = "select a.id, coalesce(c.amount_paid,0) misc_charges " +
		"from customer a left join customer_charge c on a.id = c.customer_id and c.status='Unpaid' ${ccDate} " +
		"group by a.id "
		println query
		Sql sqlQuery = new Sql(dataSource)
		def result = sqlQuery.rows(query, whereParams)
		sqlQuery.close();
		def output = [:]
		result.each{
			output[it["id"]] = it["misc_charges"]
		}
		output
    }
	    	
    def getTotalPendingPayments(dateFrom, dateTo) {
        def ccDate = ""
        def whereParams = []
      	if(dateFrom){
    		ccDate += "and c.date>=? "
    		whereParams.add(params.dateFrom)
    	}
       	if(dateTo){
    		ccDate += "and c.date<=? "
    		whereParams.add(params.dateTo)
    	}
    	
		def query = "select a.id, coalesce(c.amount,0) pending_payments " +
		"from customer a left join check_payment c on a.id = c.customer_id and (c.status='UNPAID' or c.status='FOR_REDEPOSIT') ${ccDate} " +
		"group by a.id "
		println query
		Sql sqlQuery = new Sql(dataSource)
		def result = sqlQuery.rows(query, whereParams)
		sqlQuery.close();
		def output = [:]
		result.each{
			output[it["id"]] = it["pending_payments"]
		}
		output
    }
	    	
}
