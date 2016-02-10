
package com.munix

import groovy.sql.Sql

class CustomerController {

    def dataSource

    def authenticateService
	def migrationService
    def customerService
    def customerAccountsSummaryService
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "identifier"
        if (!params.order) params.order = "asc"

        def customerInstanceList = customerService.getListOfCustomersWithCriteria(params)
        [customerInstanceList: customerInstanceList, customerInstanceTotal: customerInstanceList.totalCount]
    }

    def create = {
        def customerInstance = new Customer()
        customerInstance.properties = params
        return [customerInstance: customerInstance]
    }

    def save = {
        params.status = Customer.Status.getStatusByName(params.status)
        def customerInstance = new Customer(params)
        if (customerInstance.save(flush: true)) {
			customerInstance.customerLedger = new CustomerLedger(customer: customerInstance)
			customerInstance.customerLedger.addToEntries(CustomerLedgerEntryFactory.createInitialBalanceEntry(customerInstance))
			customerInstance.customerLedger.save(flush: true)
			
			customerInstance.customerAccount = new CustomerAccount(customer: customerInstance)
			customerInstance.customerAccount.save(flush: true)
			
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'customer.label', default: 'Customer'), customerInstance.id])}"
            redirect(action: "show", id: customerInstance.id)
        }
        else {
            render(view: "create", model: [customerInstance: customerInstance], params: params)
        }
    }

    def show = {
        def customerInstance = Customer.get(params.id)
        if (customerInstance) {
            [customerInstance: customerInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def customerInstance = Customer.get(params.id)
        if (customerInstance) {
            return [customerInstance: customerInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def customerInstance = Customer.get(params.id)
        if (customerInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (customerInstance.version > version) {      
                    customerInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'customer.label', default: 'Customer')] as Object[], "Another user has updated this Customer while you were editing")
                    render(view: "edit", model: [customerInstance: customerInstance])
                    return
                }
            }
            def approvalCustomer = needsApproval(customerInstance, params)
            if(approvalCustomer){
                def user = authenticateService.userDomain()
	        	def msg = "${user.userRealName} wants to change the following for Customer ${customerInstance.identifier} - ${customerInstance.name}:"
	        	if(approvalCustomer.creditLimit!=null){
	        		msg +="<br/>Credit Limit from ${String.format('%,.2f',customerInstance.creditLimit)} to ${String.format('%,.2f',approvalCustomer.creditLimit)};"
	        	}
	        	if(approvalCustomer.enableCreditLimit!=null){
	        		msg +="<br/>Enable Credit Limit from ${customerInstance.enableCreditLimit} to ${approvalCustomer.enableCreditLimit};"
	        	}
	        	if(approvalCustomer.term!=null){
	        		msg +="<br/>Term from ${customerInstance.term} to ${approvalCustomer.term};"
	        	}
	        	if(approvalCustomer.autoApprove!=null){
	        		msg +="<br/>Auto Approve from ${customerInstance.autoApprove} to ${approvalCustomer.autoApprove};"
	        	}
	        	if(approvalCustomer.autoSecondApprove!=null){
	        		msg +="<br/>Auto Second Approve from ${customerInstance.autoSecondApprove} to ${approvalCustomer.autoSecondApprove};"
	        	}
	        	if(approvalCustomer.status!=null){
	        		msg +="<br/>Status from ${customerInstance.status} to ${approvalCustomer.status};"
	        	}
	        		
	        	def approval = new ApprovalProcess(description:msg, requestedBy:user, date:new Date(), type:"Customer", referenceNumber:customerInstance.id)
                approval.save(flush:true)
            	approvalCustomer.approvalProcess = approval
            	approvalCustomer.save()
            	params.creditLimit = customerInstance.creditLimit
            	params.enableCreditLimit = customerInstance.enableCreditLimit
            	params.term = customerInstance.term
            	params.autoApprove = customerInstance.autoApprove
            	params.autoSecondApprove = customerInstance.autoSecondApprove
            	params.status = customerInstance.status
            }else{
            	// Update 02-27-2014: status can only be updated for approval
            	def status = Customer.Status.getStatusByName(params.status)
	            if(status!=customerInstance.status){
	                def statusLog = new CustomerLog(params.status,authenticateService.userDomain(),customerInstance)
	                customerInstance.addToStatusLogs(statusLog)
	            }
                params.status = status
            }
            customerInstance.properties = params
			customerService.generateAuditTrails(customerInstance)
            if (!customerInstance.hasErrors() && customerInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'customer.label', default: 'Customer'), customerInstance.id])}"
                if(approvalCustomer) {
                	flash.message = "Customer has been updated, but some updated fields need to be approved: " + approvalCustomer.toString()
                }
                redirect(action: "show", id: customerInstance.id)
            }
            else {
                render(view: "edit", model: [customerInstance: customerInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])}"
            redirect(action: "list")
        }
    }
    
    def needsApproval(customer, params){
//		if (authenticateService.ifAnyGranted("ROLE_MANAGER_ACCOUNTING,ROLE_SUPER")) {
	//		return null;
		//}
    	def result = false
        def approvalCustomer = new ApprovalCustomerUpdate()
    	approvalCustomer.customer = customer
        def status = Customer.Status.getStatusByName(params.status)
        if(customer.creditLimit?.toString()!=params.creditLimit) { approvalCustomer.creditLimit = Double.parseDouble(params.creditLimit); result = true } 
        if(customer.enableCreditLimit!=(params.enableCreditLimit=="on")) { approvalCustomer.enableCreditLimit = (params.enableCreditLimit=="on"); result = true } 
        if(customer.term?.id?.toString() != params.term?.id) { approvalCustomer.term = Term.get(params.term?.id); result = true }
        if(customer.autoApprove!=(params.autoApprove=="on")) { approvalCustomer.autoApprove = (params.autoApprove=="on"); result = true }
        if(customer.autoSecondApprove!=(params.autoSecondApprove=="on")) { approvalCustomer.autoSecondApprove = (params.autoSecondApprove=="on"); result = true } 
        if(customer.status!=status && params.status=="Active") { approvalCustomer.status = params.status; result = true }
        if(result==true) return approvalCustomer
        return null
    }
	
	def auditLogs = {
		def customerInstance = Customer.get(params.id)
		if (customerInstance) {
			return [customerInstance: customerInstance]
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])}"
			redirect(action: "list")
		}
	}

    def accountsSummary = {
		def customerAccounts = customerAccountsSummaryService.generateFilteredCustomerAccountList(params)
		def model = [customerAccounts:customerAccounts] 
		def df = new Date().minus(200000)
		def dt = new Date().plus(200000)
    	if(params.dateRange=="range"){
    		df=params.dateFrom
    		dt=params.dateTo
    	}
		def unpaidSds = getUnpaidSalesDeliveries(df, dt)
		def unpaidCcs = getUnpaidCustomerCharges(df, dt)
		def unpaidDms = getUnpaidDebitMemos(df, dt)
		def unpaidBcs = getUnpaidBouncedChecks(df, dt)
		def unpaidCps = getUnpaidCheckPayments(df, dt)
		model["unpaidSds"] = unpaidSds
		model["unpaidCcs"] = unpaidCcs
		model["unpaidDms"] = unpaidDms
		model["unpaidBcs"] = unpaidBcs
		model["unpaidCps"] = unpaidCps
		model
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
    
    def salesSummary = {
    	def customer = Customer.get(params.id)
		def where = "where sd.delivery.customer.id = :customerId and sd.delivery.status<>:statusName" 
    	def map = ["customerId":customer.id, "statusName":"Cancelled"]
    	if(params.dateFrom){
    		where += " and sd.delivery.date>=:dateFrom"
    		map.put("dateFrom",params.dateFrom)
    	}
    	if(params.dateTo){
    		where += " and sd.delivery.date<:dateTo"
        	map.put("dateTo",params.dateTo + 1)
    	}
    	if(params.discountType?.id != null ){
    		where += " and sd.delivery.invoice.discountType.id in (:discountTypes)"
        	//map.put(Long.parseLong(params.productType.id))
    	
			if(params.discountType.id instanceof String)
				map.put("discountTypes",Long.parseLong(params.discountType?.id))
			else
				map.put("discountTypes",params.discountType?.id.collect{value -> Long.parseLong(value)})
	
		}
    	if(params.product){
    		where += " and sd.product.identifier like :product"
           	map.put("product","%"+params.product+"%")
    	}
    	def salesDeliveries = SalesDeliveryItem.executeQuery("from SalesDeliveryItem sd ${where}",map)
    	def creditMemos = CreditMemoItem.executeQuery("from CreditMemoItem cm inner join cm.deliveryItem sd ${where}",map)
    	def result = []
    	salesDeliveries.each{
    		def sd = it
    		if(sd.qty != 0){
	    		def netAmount = sd.qty * sd.discountedPrice
	    		def obj = [:]
	    		obj["flag"] = false
	    		obj["type"] = "Sales Delivery"
	        	obj["date"] = sd.delivery.date
	    		obj["reference"] = sd.delivery
	    		obj["discountType"] = sd.delivery.invoice.discountType
	            obj["productCode"] = sd.product
	            obj["productDescription"] = sd.product.formatDescription()
	            obj["qty"] = sd.formatQty()
	            obj["price"] = sd.formatPrice()
	            obj["amount"] = sd.formatAmount()
	            obj["discount"] = sd?.orderItem?.isNet?sd.delivery.invoice.netDiscountGroup:sd.delivery.invoice.discountGroup
	            obj["netAmount"] = netAmount
	            result.add(obj)
    		}
    	}
    	creditMemos.each{
    		def cm = it[0]
    		def sd = it[1]
    		def obj = [:]
    		obj["flag"] = (cm.newQty - cm.oldQty < 0 || cm.computeFinalAmount()< 0)
    		obj["type"] = "Credit Memo"
    		obj["date"] = cm.date
    		obj["reference"] = cm.creditMemo
    		obj["discountType"] = sd.delivery.invoice.discountType
            obj["productCode"] = sd.product
            obj["productDescription"] = sd.product.formatDescription()
            obj["qty"] = cm.newQty - cm.oldQty
            obj["price"] = cm.formatNewPrice()
            obj["amount"] = cm.formatFinalAmount()
            obj["discount"] = sd?.orderItem?.isNet?sd.delivery.invoice.netDiscountGroup:sd.delivery.invoice.discountGroup
            obj["netAmount"] = cm.computeDiscountedAmount()
            result.add(obj)
    	}
    	[customer:customer,result:result]
    }
}
