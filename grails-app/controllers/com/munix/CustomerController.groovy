
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
		def unpaidSds = customerService.getUnpaidSalesDeliveries(df, dt)
		def unpaidCcs = customerService.getUnpaidCustomerCharges(df, dt)
		def unpaidDms = customerService.getUnpaidDebitMemos(df, dt)
		def unpaidBcs = customerService.getUnpaidBouncedChecks(df, dt)
		def unpaidCps = customerService.getUnpaidCheckPayments(df, dt)
		model["unpaidSds"] = unpaidSds
		model["unpaidCcs"] = unpaidCcs
		model["unpaidDms"] = unpaidDms
		model["unpaidBcs"] = unpaidBcs
		model["unpaidCps"] = unpaidCps
		model
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
