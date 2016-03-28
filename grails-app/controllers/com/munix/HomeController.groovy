package com.munix

class HomeController {
	
	def authenticateService
	def sessionFactory
	def salesOrderService

	def clear = {
		sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()
		System.gc()
		render "cleared2!"
	}
	
	def cleanSalesOrders = {
		salesOrderService.autocancelSalesOrders()
		salesOrderService.autocompleteSalesOrders()
		render "cleaned sales orders."
	}
	
	def index = {
	
		def user = User.findByUsername(authenticateService.principal()?.username)
		if(user?.authorities?.size()==1){
			user?.authorities?.each{
				if(it.authority == "ROLE_PURCHASING"){
					redirect controller:"home", action:"purchasing"
					return
				}else if(it.authority == "ROLE_SALES"){
					redirect controller:"home", action:"sales"
					return
				}else if(it.authority == "ROLE_PRODUCTION"){
					redirect controller:"home", action:"production"
					return
				}else if(it.authority == "ROLE_ACCOUNTING"){
					redirect controller:"home", action:"accounting"
					return
				}else if(it.authority == "ROLE_DELIVERY"){
					redirect controller:"home", action:"delivery"
					return
				}
			}
		}
		render view:"dashboard"
	}
	
	def purchasing = {
		def sections = []
    	sections << unapprovedPos()
		sections << incompletePos()
		sections << unapprovedPis()
		render view:"index", model:[sections:sections]	
	}
	
	def salesDeliveriesForApproval = {
		def section = [:]
      	section["class"] = "unapprovedPos"
  		section["title"] = "For Approval (Sales Delivery)"
  		section["object"] = "salesDelivery"
  		section["columns"] = ["Description", "Requested By", "Date","Type", "Status", "Action"]
   		def approvals = ApprovalProcess.findAllByStatusAndType("Unapproved", "Sales Delivery")                     
 		def list = []
 		approvals.each{
 			list << [it.referenceNumber,it.description, it.requestedBy?.userRealName, formatDate(date:it?.date, format:"MMM. dd, yyyy HH:mm:ss"),	it.type, it.status, "<a href='${request.contextPath}/salesDelivery/show/${it.referenceNumber}'>View Sales Delivery</a> | <a href='${request.contextPath}/approvalProcess/approve/${it.id}?view=sales'>Approve</a> | <a href='${request.contextPath}/approvalProcess/reject/${it.id}?view=sales'>Reject</a> "]
 		}
  		section["list"] = list
   		section
   	}
	
	def salesOrdersForApproval = {
		def section = [:]
      	section["class"] = "salesOrdersForApproval"
  		section["title"] = "For Approval (Sales Order)"
  		section["object"] = "salesOrder"
  		section["columns"] = ["Description", "Requested By", "Date","Type", "Status", "Action"]
   		def approvals = ApprovalProcess.findAllByStatusAndType("Unapproved", "Sales Order")                     
 		def list = []
 		approvals.each{
 			list << [it.referenceNumber,it.description, it.requestedBy?.userRealName, formatDate(date:it?.date, format:"MMM. dd, yyyy HH:mm:ss"),	it.type, it.status, "<a href='${request.contextPath}/salesOrder/show/${it.referenceNumber}'>View Sales Order</a> | <a href='${request.contextPath}/approvalProcess/approve/${it.id}?view=sales'>Approve</a> | <a href='${request.contextPath}/approvalProcess/reject/${it.id}?view=sales'>Reject</a> "]
 		}
  		section["list"] = list
   		section
   	}
	
	def productsForApproval = {
		def section = [:]
      	section["class"] = "unapprovedProducts"
  		section["title"] = "For Approval (Product Update)"
  		section["object"] = "product"
  		section["columns"] = ["Description", "Requested By", "Date","Type", "Status", "Action"]
   		def approvals = ApprovalProcess.findAllByStatusAndType("Unapproved", "Product")
 		def list = []
 		approvals.each{
 			list << [it.referenceNumber,it.description, it.requestedBy?.userRealName, formatDate(date:it?.date, format:"MMM. dd, yyyy HH:mm:ss"),	it.type, it.status, "<a href='${request.contextPath}/product/editMultiple3/${it.id}'>View</a> "]
 		}
  		section["list"] = list
   		section
   	}
		
	def unapprovedPos = {
		def section = [:]
       	section["class"] = "unapprovedPos"
   		section["title"] = "Unapproved POs"
   		section["object"] = "purchaseOrder"
   		section["columns"] = ["Date","Reference #","Supplier","Amount","Status"]
   		section["list"] = poList("Unapproved")
		section
	}
	
	def incompletePos = {
		def section = [:]
       	section["class"] = "incompletePos"
   		section["title"] = "Incomplete POs"
   		section["object"] = "purchaseOrder"
   		section["columns"] = ["Date","Reference #","Supplier","Amount","Status"]
   		section["list"] = poList("Approved")
		section
	}
		
	def poList(status) {
   		def purchaseOrders = PurchaseOrder.findAllByStatus(status)                     
   		def list = []
   		purchaseOrders.each{
   			list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.formatId(),it.supplier,it.formatTotal(),it.status]
   		}
   		list
	}

	def unapprovedPis = {
		def section = [:]
       	section["class"] = "unapprovedPis"
   		section["title"] = "Unapproved PIs"
   		section["object"] = "purchaseInvoice"
   		section["columns"] = ["Date","Reference #","Supplier Reference #","Supplier","Amount","Status"]
  		def purchaseInvoices = PurchaseInvoice.findAllByStatus(PurchaseInvoice.Status.UNAPPROVED)                     
		section["list"] = []
		purchaseInvoices.each{
			section["list"] << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it,it.supplier?.identifier,it.supplier,String.format('%,.4f',it.computePurchaseInvoicePhpTotal()),it.status]
		}
		section
	}
	
	def sales = {
		def sections = []
		def user = authenticateService.userDomain()
		user?.authorities?.each{
			if(it.authority == "ROLE_MANAGER_SALES"){
				sections << salesDeliveriesForApproval()
				sections << salesOrdersForApproval()
				sections << productsForApproval()
			}
		}                
    	sections << unapprovedSos()
		sections << openSos()
		//sections << undeliveredSds()
		sections << unapprovedCms()
		sections << customersOnHold()
		render view:"index", model:[sections:sections]	
	}
	
	def unapprovedSos = {
		def section = soList("unapprovedSos","Unapproved SOs","Unapproved")
 		def salesOrders = SalesOrder.executeQuery("from SalesOrder s where status=:status and preparedBy like :preparedBy",["status":"Second Approval Pending","preparedBy":"%"+(authenticateService.userDomain()?.userRealName)+"%"])                     
   		salesOrders.each{
			section["list"] << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.discountType,it,it.customer,it.formatTotal(),it.status,it.preparedBy]
   		}
  		section
	}
	
	def openSos = {
		def section = [:]
     	section["class"] = "openSos"
 		section["title"] = "Open SOs"
  		section["object"] = "salesOrder"
  		section["columns"] = ["Date","Discount Type","Reference #","Customer","Amount","Deliveries","Status","Created By"]
		def salesOrders = SalesOrder.executeQuery("from SalesOrder s where status=:status and preparedBy like :preparedBy",["status":"Approved","preparedBy":"%"+(authenticateService.userDomain()?.userRealName)+"%"])                     
  		def list = []
  		salesOrders.each{
			def deliveries = ""
			it?.deliveries?.each{
				def delivery = it
				deliveries += "<a href='${request.contextPath}/salesDelivery/show/${delivery.id}'>[${delivery}]</a> "
			}
			list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.discountType,it,it.customer,it.formatTotal(),deliveries, it.status,it.preparedBy]
  		}
 		section["list"] = list
 		section
	}
	
	def soList(className, title, status){
		def section = [:]
      	section["class"] = className
  		section["title"] = title
   		section["object"] = "salesOrder"
	   	section["columns"] = ["Date","Discount Type","Reference #","Customer","Amount","Status","Created By"]
 		def salesOrders = SalesOrder.executeQuery("from SalesOrder s where status=:status and preparedBy like :preparedBy",["status":status,"preparedBy":"%"+(authenticateService.userDomain()?.userRealName)+"%"])                     
   		def list = []
   		salesOrders.each{
			list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.discountType,it,it.customer,it.formatTotal(),it.status,it.preparedBy]
   		}
  		section["list"] = list
  		section
  		
	}
	
	def undeliveredSds = {
		def section = [:]
       	section["class"] = "undeliveredSds"
   		section["title"] = "Undelivered SDs"
   		section["object"] = "salesDelivery"
	   	section["columns"] = ["Date","Discount Type","Reference #","Customer","Amount","Status"]
		def salesDeliveries = SalesDelivery.executeQuery("from SalesDelivery where deliveryType=:deliveryType and waybill is null and directDelivery is null",["deliveryType":"Deliver"])                     
		def list = []
		salesDeliveries.each{
			list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.invoice?.discountType,it,it.customer,String.format('%,.2f',it.computeTotalAmount()),it.status]
		}
   		section["list"] = list
		section
	}

	def unapprovedCms = {
		cmList("unapprovedCms","Unapproved CMs","Unapproved")
	}

	def customersOnHold = {
		def section = [:]
       	section["class"] = "customersOnHold"
   		section["title"] = "Customers On Hold"
   		section["object"] = "customer"
	   	section["columns"] = ["Code","Customer Name","Remaining Amount"]
		def customers = Customer.findAllByStatus(Customer.Status.ONHOLD)                     
		def list = []
		customers.each{
 			list << [it.id, it.identifier, it.name, ""]
		}
   		section["list"] = list
		section
	}

	def production = {
		def sections = []
    	sections << unapprovedJobOrders()
		sections << unapprovedJobOuts()
		render view:"index", model:[sections:sections]	
	}
	

	def unapprovedJobOrders = {
		def section = [:]
       	section["class"] = "unapprovedJobOrders"
   		section["title"] = "Unapproved Job Orders"
   		section["object"] = "jobOrder"
	   	section["columns"] = ["Date","Reference #","Item","Quantity","Assigned To","Status"]
		def jobOrders = JobOrder.findAllByStatus(JobOrder.Status.UNAPPROVED)                     
		def list = []
        jobOrders.each{
 			list << [it.id,formatDate(date:it?.startDate, format:"MMM. dd, yyyy"),it,it.product,it.formatQty(),it.assignedTo,it.status]
		}
   		section["list"] = list
		section
	}

	def unapprovedJobOuts = {
		def section = [:]
       	section["class"] = "unapprovedJobOuts"
   		section["title"] = "Unapproved Job Outs"
   		section["object"] = "jobOut"
	   	section["columns"] = ["Date","Job Order #","Job Out #","Item","Quantity","Assigned To","Status"]
		def jobOuts = JobOut.findAllByStatus(JobOut.Status.UNAPPROVED)                     
		def list = []
        jobOuts.each{
 			list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.jobOrder, it,it.jobOrder.product,it.qty,it.jobOrder.assignedTo,it.status]
		}
   		section["list"] = list
		section
	}

	def delivery = {
		def sections = []
    	sections << unassignedWaybills()
		sections << unassignedDirectDeliveries()
		sections << incompleteTripTickets()
		render view:"index", model:[sections:sections]	
	}
	
	def unassignedWaybills = {
		def section = [:]
      	section["class"] = "unassignedWaybills"
  		section["title"] = "Unassigned Waybills"
   		section["object"] = "waybill"
   	   	section["columns"] = ["Date","Reference #","Customer","Forwarder","Status"]
   		def waybills = Waybill.findAllByStatus("Processing")                     
   		def list = []
        waybills.each{
			if(!it.tripTicket){
				list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it,it.customer,it.forwarder,it.status]
			}
   		}
  		section["list"] = list
   		section
	}
	
	
	def unassignedDirectDeliveries = {
		def section = [:]
      	section["class"] = "unassignedDirectDeliveries"
  		section["title"] = "Unassigned Direct Deliveries"
   		section["object"] = "directDelivery"
   	   	section["columns"] = ["Date","Reference #","Customer","Status"]
   		def directDeliveries = DirectDelivery.findAllByStatus(DirectDelivery.Status.PROCESSING)                     
   		def list = []
        directDeliveries.each{
			if(!it.tripTicket){
				list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it,it.customer,it.status]
			}
   		}
  		section["list"] = list
   		section
	}
	
	def incompleteTripTickets = {
		def section = [:]
      	section["class"] = "incompleteTripTickets"
  		section["title"] = "Incomplete Trip Tickets"
  	   		section["object"] = "tripTicket"
   	   	section["columns"] = ["Date","Reference #","Truck","Driver","Helper/s","Status"]
   		def tripTickets = TripTicket.findAllByStatus("Processing")                     
   		def list = []
        tripTickets.each{
			def tt = it
			def helpers = ""
			tt.helpers.each{
				if(helpers!="")
					helpers += ","
				helpers = helpers + it.toString()
			}
			list << [it.id,formatDate(date:tt?.date, format:"MMM. dd, yyyy"),tt,tt.truck,tt.driver,helpers,tt.status]
   		}
  		section["list"] = list
   		section
	}

	def accounting = {
		def sections = []
		if(authenticateService.ifAnyGranted("ROLE_MANAGER_ACCOUNTING,ROLE_SUPER")){
	    	sections << customersForApproval()
		}
    	sections << unapprovedAccountingSos()
		//sections << unpaidSds()
		sections << unapprovedAccountingCms()
		sections << unassignedCms()
		sections << customersOnHold()
		render view:"index", model:[sections:sections]	
	}
	
	def customersForApproval = {
		def section = [:]
      	section["class"] = "unapprovedCustomerUpdates"
  		section["title"] = "Customer Updates For Approval"
  		section["object"] = "customer"
  		section["columns"] = ["Description", "Requested By", "Date","Type", "Status", "Action"]
   		def approvals = ApprovalProcess.findAllByStatusAndType("Unapproved", "Customer")                     
 		def list = []
 		approvals.each{
 			list << [it.referenceNumber,it.description, it.requestedBy?.userRealName, formatDate(date:it?.date, format:"MMM. dd, yyyy HH:mm:ss"),	it.type, it.status, "<a href='${request.contextPath}/customer/show/${it.referenceNumber}'>View Customer</a> | <a href='${request.contextPath}/approvalProcess/approve/${it.id}?view=accounting'>Approve</a> | <a href='${request.contextPath}/approvalProcess/reject/${it.id}?view=accounting'>Reject</a> "]
 		}
   		approvals = ApprovalProcess.findAllByStatusAndType("Unapproved", "Customer Discount")                     
 		approvals.each{
 			list << [it.referenceNumber,it.description, it.requestedBy?.userRealName, formatDate(date:it?.date, format:"MMM. dd, yyyy HH:mm:ss"),	it.type, it.status, "<a href='${request.contextPath}/customer/show/${it.referenceNumber}'>View Customer</a> | <a href='${request.contextPath}/approvalProcess/approve/${it.id}?view=accounting'>Approve</a> | <a href='${request.contextPath}/approvalProcess/reject/${it.id}?view=accounting'>Reject</a> "]
 		}
  		section["list"] = list
   		section
   	}
		
	def unapprovedAccountingSos = {
		def section = [:]
     	section["class"] = "unapprovedSos"
 		section["title"] = "Unapproved SOs"
  		section["object"] = "salesOrder"
		section["columns"] = ["Date","Discount Type","Reference #","Customer","Amount","Status","Created By"]
		def salesOrders = SalesOrder.findAllByStatus("Second Approval Pending")                     
  		def list = []
  		salesOrders.each{
		list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.discountType,it,it.customer,it.formatTotal(),it.status,it.preparedBy]
  		}
 		section["list"] = list
 		section
	}

	def unpaidSds = {
		def section = [:]
       	section["class"] = "unpaidSds"
   		section["title"] = "Unpaid SDs"
   		section["object"] = "salesDelivery"
	   	section["columns"] = ["Date","Discount Group","Reference #","Customer","Amount","Balance","Status"]
		def salesDeliveries = SalesDelivery.findAllByStatus("Unpaid")                     
		def list = []
		salesDeliveries.each{
 			list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.discountGroup,it,it.customer,String.format('%,.2f',it.computeTotalAmount()),it.status]
		}
   		section["list"] = list
		section
	}
	
	def unapprovedAccountingCms = {
		def section = cmList("unapprovedCms","Unapproved CMs","Unapproved")
 		def creditMemos = CreditMemo.findAllByStatus("Second Approval Pending")                     
   		creditMemos.each{
			section["list"] << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.discountType,it,it.customer,it.formatAmountTotal(),it.status]
   		}
  		section
	}
	
	def unassignedCms = {
		cmList("unassignedCms","Unassigned CMs","Approved")
	}

	def cmList(className, title, status){
		def section = [:]
      	section["class"] = className
  		section["title"] = title
   		section["object"] = "creditMemo"
	   	section["columns"] = ["Date","Discount Type","Reference #","Customer","Amount","Status"]
   		def creditMemos = CreditMemo.findAllByStatus(status)                     
   		def list = []
   		creditMemos.each{
			list << [it.id,formatDate(date:it?.date, format:"MMM. dd, yyyy"),it.discountType,it,it.customer,it.formatAmountTotal(),it.status]
   		}
  		section["list"] = list
  		section
  		
	}

}
