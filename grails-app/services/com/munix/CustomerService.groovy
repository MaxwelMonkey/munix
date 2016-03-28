package com.munix

import groovy.sql.Sql

class CustomerService {

    static transactional = true
    def dataSource

	def authenticateService
	def generalMethodService

    def getListOfCustomersWithCriteria(params) {
        def searchIdentifier = params.searchIdentifier
        def searchName = params.searchName
        def searchSalesAgent = params.searchSalesAgent
        def searchStatus = params.searchStatus

        def query = {
            //Search
            if(searchIdentifier){
                like('identifier', "%${searchIdentifier}%")
            }

            if(searchName){
                like('name', "%${searchName}%")
            }

            if(searchSalesAgent){
                salesAgent{
                    like('identifier', "%${searchSalesAgent}%")
                }
            }

            if(searchStatus){
                eq('status', com.munix.Customer.Status.getStatusByName(searchStatus))
            }
        }
        return Customer.createCriteria().list(params,query)
    }
	
    def generateAuditTrails(Customer customer){
    	generateAuditTrails(customer, authenticateService.userDomain())
    }

    def generateAuditTrails(Customer customer, User user ){
        if(customer.isDirty()){
            def map =[busAddrStreet:"Street (Business)",
				busAddrCity:"City (Business)",
				busAddrZip:"Zip (Business)", 
				bilAddrStreet:"Street (Billing)", 
				bilAddrCity:"City (Billing)",
				bilAddrZip:"Zip (Billing)",
				bank1:"Bank 1",
				branch1:"Branch 1",
				accountName1:"Account Name 1",
				accountNumber1:"Account Number 1",
				bank2:"Bank 2",
				branch2:"Branch 2",
				accountName2:"Account Name 2",
				accountNumber2:"Account Number 2",
				bank3:"Bank 3",
				branch3:"Branch 3",
				accountName3:"Account Name 3",
				accountNumber3:"Account Number 3",
				contact:"Main Contact",
				contactPosition:"Main Contact Position",
				]
            def modifiedFieldNames = customer.getDirtyPropertyNames()
            modifiedFieldNames.each{fieldName->
                def field = map.get(fieldName)
                if(!field){
                    def display = generalMethodService.addWhiteSpaceAfterCapital(fieldName)
                    field = generalMethodService.capitalizeFirstLetter(display)
                }
                def currentValue = customer."$fieldName"==null?"":customer."$fieldName".toString()
                def originalValue = customer.getPersistentValue(fieldName)==null?"":customer.getPersistentValue(fieldName).toString()
                if (currentValue != originalValue) {
                    customer.addToAuditTrails(new CustomerAuditTrail(
                        user:user,
                        previousEntry:originalValue,
                        newEntry: currentValue,
                        fieldName: field,
                        customer: customer,
                        approvedBy: authenticateService.userDomain()
                    ))
                }
            }
        }
    }

    def getUnpaidSalesDeliveries(dateFrom, dateTo) {
    	def result = [:]
    	def query = "select sd_total.customer_id, sum(remaining) - paid amount from ( " + 
		"select b.customer_id, SUM((a.qty * a.price) * (100 - coalesce(c.discount,0))/100) remaining from sales_delivery_item a, sales_delivery b, sales_order c, sales_order_item d "+
		"where a.delivery_id = b.id and b.invoice_id = c.id and a.order_item_id = d.id " +
		"and b.status='Unpaid' " +
		"and d.is_net = false " +
		"and b.date>=? and b.date<? " +
		"group by b.customer_id " +
		"union " +
		"select b.customer_id, SUM((a.qty * a.price) * (100 - coalesce(c.net_discount,0))/100) remaining from sales_delivery_item a, sales_delivery b, sales_order c, sales_order_item d " +
		"where a.delivery_id = b.id and b.invoice_id = c.id and a.order_item_id = d.id " +
		"and b.status='Unpaid' " +
		"and d.is_net = true " +
		"and b.date>=? and b.date<? " +
		"group by b.customer_id " +
		") sd_total, " +
		"(select customer_id, SUM(amount_paid) paid from sales_delivery " +
		"where status='Unpaid' " +
		"and date>=? and date<? " +
		"group by customer_id " +
		") amount_paid " +
		"where sd_total.customer_id = amount_paid.customer_id " +
		"group by sd_total.customer_id";
        Sql sqlQuery = new Sql(dataSource)
        def rows = sqlQuery.rows(query,[dateFrom, dateTo+1, dateFrom, dateTo+1, dateFrom, dateTo+1])
        sqlQuery.close();
        rows.each{
        	result[it.customer_id] = it.amount
        }
        result
    }

    def getUnpaidCustomerCharges(dateFrom, dateTo) {
    	def result = [:]
    	def query = "select customer_id, sum(a.amount) amount from customer_charge_item a, customer_charge b " +
    	"where a.customer_charge_id = b.id " +
    	"and b.status='Unpaid' " +
		"and b.date>=? and b.date<? " +
    	"group by b.customer_id";
        Sql sqlQuery = new Sql(dataSource)
        def rows = sqlQuery.rows(query,[dateFrom, dateTo+1])
        sqlQuery.close();
        rows.each{
        	result[it.customer_id] = it.amount
        }
        query = "select b.customer_id, sum(c.amount) amount from customer_charge_direct_payment_invoice a, customer_charge b, direct_payment_invoice c " +
        "where a.direct_payment_invoice_id = c.id and a.customer_charge_invoices_id = b.id " +
		"and b.status='Unpaid' " +
		"and b.date>=? and b.date<? " +
		"group by b.customer_id"
		sqlQuery = new Sql(dataSource)
        rows = sqlQuery.rows(query, [dateFrom, dateTo+1])
        sqlQuery.close()
        rows.each{
        	result[it.customer_id] = result[it.customer_id] - it.amount
        }
        result
    }
    
    def getUnpaidDebitMemos(dateFrom, dateTo) {
    	def result = [:]
    	def query = "select customer_id, sum(final_amount)*-1 amount from ( " +
    			"select b.id, b.customer_id, sum(((old_qty - new_qty) * new_price) * (1-(coalesce(e.net_discount,0)/100)))  final_amount  from credit_memo_item a, credit_memo b, " + 
    			"sales_delivery_item c, sales_delivery d, sales_order e, sales_order_item f " +
    			"where a.credit_memo_id = b.id " +
    			"and a.delivery_item_id = c.id and c.delivery_id = d.id and d.invoice_id = e.id and c.order_item_id = f.id " +
    			"and b.status='Approved' " +
    			"and a.old_qty<>a.new_qty " +
    			"and a.old_price<>a.new_price " +
    			"and f.is_net = true " +
    			"and b.date>=? and b.date<? " +
    			"group by b.id " +
    			"union " +
    			"select b.id, b.customer_id, sum(((old_qty*old_price) - (new_qty*new_price)) * (1-(coalesce(e.net_discount,0)/100)))  final_amount  from credit_memo_item a, credit_memo b, " + 
    			"sales_delivery_item c, sales_delivery d, sales_order e, sales_order_item f " +
    			"where a.credit_memo_id = b.id " +
    			"and a.delivery_item_id = c.id and c.delivery_id = d.id and d.invoice_id = e.id and c.order_item_id = f.id " +
    			"and b.status='Approved' " +
    			"and (a.old_qty=a.new_qty or a.old_price=a.new_price) " +
    			"and f.is_net = true " +
    			"and b.date>=? and b.date<? " +
    			"group by b.id " +
    			"union " +
    			"select b.id, b.customer_id, sum(((old_qty - new_qty) * new_price) * (1-(coalesce(e.discount,0)/100))) final_amount  from credit_memo_item a, credit_memo b, " + 
    			"sales_delivery_item c, sales_delivery d, sales_order e, sales_order_item f " +
    			"where a.credit_memo_id = b.id " +
    			"and a.delivery_item_id = c.id and c.delivery_id = d.id and d.invoice_id = e.id and c.order_item_id = f.id " +
    			"and b.status='Approved' " +
    			"and a.old_qty<>a.new_qty " +
    			"and a.old_price<>a.new_price " +
    			"and f.is_net = false " +
    			"and b.date>=? and b.date<? " +
    			"group by b.id " +
    			"union " +
    			"select b.id, b.customer_id, sum(((old_qty*old_price) - (new_qty*new_price)) * (1-(coalesce(e.discount,0)/100))) final_amount   from credit_memo_item a, credit_memo b, " + 
    			"sales_delivery_item c, sales_delivery d, sales_order e, sales_order_item f " +
    			"where a.credit_memo_id = b.id " +
    			"and a.delivery_item_id = c.id and c.delivery_id = d.id and d.invoice_id = e.id and c.order_item_id = f.id " +
    			"and b.status='Approved' " +
    			"and (a.old_qty=a.new_qty or a.old_price=a.new_price) " +
    			"and f.is_net = false " +
    			"and b.date>=? and b.date<? " +
    			"group by b.id " +
    			") x where final_amount<0 " +
    			"group by customer_id";
        Sql sqlQuery = new Sql(dataSource)
        def rows = sqlQuery.rows(query,[dateFrom, dateTo+1, dateFrom, dateTo+1, dateFrom, dateTo+1, dateFrom, dateTo+1])
        sqlQuery.close();
        rows.each{
        	result[it.customer_id] = it.amount
        }
        result
    }

    def getUnpaidBouncedChecks(dateFrom, dateTo) {
    	def result = [:]
    	def query = "select a.customer_id, sum(b.amount) amount from bounced_check a, check_payment b, check_payment_bounced_checks c " +
    	"where a.id = c.bounced_check_id and b.id = c.check_payment_id " +
    	"and a.status='Approved' " +
    	"and b.status='FOR_REDEPOSIT' " +
    	"and for_redeposit= true " + 
		"and a.date>=? and a.date<? " +
    	"group by a.customer_id";
        Sql sqlQuery = new Sql(dataSource)
        def rows = sqlQuery.rows(query,[dateFrom, dateTo+1])
        sqlQuery.close();
        rows.each{
        	result[it.customer_id] = it.amount
        }
        query = "select a.customer_id, sum(b.amount) amount from bounced_check a, check_payment b, check_payment_bounced_checks c " +
    	"where a.id = c.bounced_check_id and b.id = c.check_payment_id " +
    	"and a.status='Approved' " +
    	"and b.status<>'FOR_REDEPOSIT' " +
		"and a.for_redeposit = false " +
		"and a.date>=? and a.date<? " +
    	"group by a.customer_id";
		sqlQuery = new Sql(dataSource)
        rows = sqlQuery.rows(query, [dateFrom, dateTo+1])
        sqlQuery.close()
        rows.each{
        	result[it.customer_id] = (result[it.customer_id]?result[it.customer_id]:0) + it.amount
        }
        query = "select b.customer_id, sum(c.amount) amount from bounced_check_direct_payment_invoice a, bounced_check b, direct_payment_invoice c, direct_payment d " +
    	"where a.direct_payment_invoice_id = c.id and a.bounced_check_invoices_id = b.id and c.direct_payment_id = d.id " +
    	"and b.status='Approved' " +
    	"and b.status<>'FOR_REDEPOSIT' " +
    	"and d.status<>'Cancelled' " +
		"and b.for_redeposit = false " +
		"and b.date>=? and b.date<? " +
    	"group by b.customer_id";
		sqlQuery = new Sql(dataSource)
        rows = sqlQuery.rows(query, [dateFrom, dateTo+1])
        sqlQuery.close()
        rows.each{
        	result[it.customer_id] = result[it.customer_id] - it.amount
        }
        result
    }
    
    def getUnpaidCheckPayments(dateFrom, dateTo) {
    	def result = [:]
    	def query = "select a.customer_id, sum(a.amount) amount from check_payment a, direct_payment_item b, direct_payment c " +
    	"where a.direct_payment_item_id = b.id and b.direct_payment_id = c.id and c.status='Approved' " +
    	"and (a.status = 'PENDING' or a.status='FOR_REDEPOSIT') " +
		"and a.date>=? and a.date<? " +
    	"group by a.customer_id";
        Sql sqlQuery = new Sql(dataSource)
        def rows = sqlQuery.rows(query,[dateFrom, dateTo+1])
        sqlQuery.close();
        rows.each{
        	result[it.customer_id] = it.amount
        }
        result
    }
    
}
