package com.munix

import grails.converters.*

class SalesDeliveryController {

    static allowedMethods = [save: "POST", update: "POST", approve: "POST", unapprove: "POST", cancel: "POST"]
    def authenticateService
    def constantService
    def salesDeliveryService
    def creditMemoService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		def salesDeliveries = salesDeliveryService.generateList(params)
		def dateMap = salesDeliveryService.generateDateStructList(params)
		def reviewerList = User.findAllBySdPrintoutReviewer(true)
		def isReviewer =false
		def user = authenticateService.userDomain()
		reviewerList.each{
			if(user.id == it.id) isReviewer=true
		}
		return [salesDeliveryInstanceList: salesDeliveries.salesDeliveryInstanceList, salesDeliveryInstanceTotal : salesDeliveries.salesDeliveryInstanceTotal, dateMap: dateMap, reviewerList:reviewerList, isReviewer:isReviewer, userId:user.id]
    }
    
	
    def reviewerCheckbox = {
		try{
			def reviewerId = params.reviewerId
			def salesDeliveryId = params.salesDeliveryId
			
			def spr = SdPrintoutReview.executeQuery("from SdPrintoutReview spr where spr.reviewer.id=:reviewerId and spr.salesDelivery.id=:salesDeliveryId",["reviewerId":Long.valueOf(reviewerId), "salesDeliveryId":Long.valueOf(salesDeliveryId)])
			def checked = ""
			spr?.each{
				if(it?.reviewed)
					checked = "checked='true'"
			}
			render "<input type='checkbox' onclick='saveReviewerCheckbox(this)' reviewer='${reviewerId}' salesDelivery='${salesDeliveryId}' ${checked}>"
		}catch(Exception e){
			e.printStackTrace()
			render "X"
		}
	}
    
    def saveReviewerCheckbox = {
    	try{
	    	def checked = params.checked=="Y"
			def reviewerId = params.reviewerId
			def salesDeliveryId = params.salesDeliveryId
			
			def spr = SdPrintoutReview.executeQuery("from SdPrintoutReview spr where spr.reviewer.id=:reviewerId and spr.salesDelivery.id=:salesDeliveryId",["reviewerId":Long.valueOf(reviewerId), "salesDeliveryId":Long.valueOf(salesDeliveryId)])
			if(spr.size()==0){
				spr = new SdPrintoutReview(reviewer:User.get(Long.parseLong(reviewerId)), salesDelivery:SalesDelivery.get(Long.parseLong(salesDeliveryId)), reviewed:checked).save()
			}else{
				spr?.each{
					it?.reviewed=checked
					it.save()
				}
			}
		}catch(Exception e){
			e.printStackTrace()
		}
    }

	def unpaidList ={
		def customerInstance = Customer.get(params.customerId)
		if(customerInstance){
			if (!params.max) params.max = 100
			if (!params.offset) params.offset = 0
			if (!params.sort) params.sort = "salesDeliveryId"
			if (!params.order) params.order = "asc"
			
			def unpaidSalesDelivery = salesDeliveryService.getUnpaidSalesDeliveryList(customerInstance, params)
			def totalUnpaidSalesDelivery = salesDeliveryService.computeTotalUnpaidSalesDeliveries(customerInstance)
			return [salesDeliveryInstanceList: unpaidSalesDelivery, totalUnpaidSalesDelivery: totalUnpaidSalesDelivery, salesDeliveryInstanceTotal: unpaidSalesDelivery.totalCount, customerInstance: customerInstance]
		}else{
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.id])}"
			redirect(action: "accountsSummary" ,controller: "customer")
		}
	} 
	
    def create = {
        def salesDeliveryInstance = new SalesDelivery()
		def salesOrderInstance = SalesOrder.get(params?.id)
		def salesOrderItems = salesDeliveryService.generateOrderItems(salesOrderInstance)
		def warehouses = salesDeliveryService.generateWarehouseList()
		
        return [salesDeliveryInstance: salesDeliveryInstance, salesOrderInstance: salesOrderInstance, salesOrderItems: salesOrderItems, warehouses: warehouses]
    }

    def save = {
        def salesOrderInstance = SalesOrder.get(params.id)
        def invoiceList = salesOrderInstance.deliveries.findAll{
            it.isUnapproved()
        }

        if(invoiceList.size() > 0){
            flash.message = "Unable to create a sales delivery due to unapproved deliveries"
            redirect(controller: "salesOrder", action: "show", id: salesOrderInstance.id)
        } else if (params.version.toString() != salesOrderInstance.version.toString()) {
			flash.message = "Unable to create a sales delivery because sales order has been changed"
			redirect(controller: "salesOrder", action: "show", id: salesOrderInstance.id)
        } else {
            def salesDeliveryInstance = new SalesDelivery(salesOrderInstance)
			salesDeliveryInstance.properties= params
            salesDeliveryInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())

            if (salesDeliveryService.saveSalesDelivery(salesDeliveryInstance)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'salesDelivery.label', default: 'SalesDelivery'), salesDeliveryInstance.id])}"
                redirect(action: "show", id: salesDeliveryInstance.id)
            } else {
                render(view: "create", model: [salesDeliveryInstance: salesDeliveryInstance, salesOrderInstance: salesOrderInstance, salesOrderItems: salesDeliveryService.generateOrderItems(salesOrderInstance), warehouses: salesDeliveryService.generateWarehouseList()])
            }
        }

    }

    def show = {
        def salesDeliveryInstance = SalesDelivery.get(params.id)
        if (!salesDeliveryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesDelivery.label', default: 'SalesDelivery'), params.id])}"
            redirect(action: "list")
        }
        else {
        	if(params.a=="print"){
        		flash.message = "Your request has been sent for approval."
        	}
            def creditMemoItems = creditMemoService.getCreditMemoListForSalesDelivery(salesDeliveryInstance)
            def user = authenticateService.userDomain()
            def printablePacking = PrintLogSalesDelivery.executeQuery("from PrintLogSalesDelivery p where p.salesDelivery=:sd and p.type=:t",[sd:salesDeliveryInstance, t:PrintLogSalesDelivery.Type.PACKINGLIST])?.size()==0
            def unapprovedPacking = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Unapproved", id:salesDeliveryInstance.id?.intValue(), type:"Sales Delivery", u:user, r:"Packing List"])?.size()>0
            def approvedPacking = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Approved", id:salesDeliveryInstance.id?.intValue(), type:"Sales Delivery", u:user, r:"Packing List"])?.size()>0
            def rejectedPacking = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Rejected", id:salesDeliveryInstance.id?.intValue(), type:"Sales Delivery", u:user, r:"Packing List"])?.size()>0
            def printableInvoice = PrintLogSalesDelivery.executeQuery("from PrintLogSalesDelivery p where p.salesDelivery=:sd and p.type=:t",[sd:salesDeliveryInstance, t:PrintLogSalesDelivery.Type.INVOICE])?.size()==0
            def unapprovedInvoice = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Unapproved", id:salesDeliveryInstance.id?.intValue(), type:"Sales Delivery", u:user, r:"Invoice"])?.size()>0
            def approvedInvoice = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Approved", id:salesDeliveryInstance.id?.intValue(), type:"Sales Delivery", u:user, r:"Invoice"])?.size()>0
            def rejectedInvoice = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Rejected", id:salesDeliveryInstance.id?.intValue(), type:"Sales Delivery", u:user, r:"Invoice"])?.size()>0
            [salesDeliveryInstance: salesDeliveryInstance, showUnapprove:salesDeliveryService.showUnapproveButton(salesDeliveryInstance), creditMemoItems : creditMemoItems, printablePacking:printablePacking, printableInvoice:printableInvoice, 
             unapprovedPacking: unapprovedPacking, unapprovedInvoice:unapprovedInvoice,
             approvedPacking: approvedPacking, approvedInvoice:approvedInvoice,
             rejectedPacking: rejectedPacking, rejectedInvoice:rejectedInvoice
             ]
        }
    }

    def edit = {
        def salesDeliveryInstance = SalesDelivery.get(params.id)
		def productList = salesDeliveryService.generateListOfSalesDeliveryItems(salesDeliveryInstance)
		def warehouses = salesDeliveryService.generateWarehouseList()

        if (!salesDeliveryInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesDelivery.label', default: 'SalesDelivery'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [salesDeliveryInstance: salesDeliveryInstance, productList : productList, warehouses: warehouses]
        }
    }

    def update = {
        def salesDeliveryInstance = SalesDelivery.get(params.id)
        if (salesDeliveryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (salesDeliveryInstance.version > version) {
                    salesDeliveryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'salesDelivery.label', default: 'SalesDelivery')] as Object[], "Another user has updated this SalesDelivery while you were editing")
                    render(view: "edit", model: [salesDeliveryInstance: salesDeliveryInstance])
                    return
                }
            }
			salesDeliveryInstance.properties = params
            if (salesDeliveryService.saveSalesDelivery(salesDeliveryInstance)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'salesDelivery.label', default: 'SalesDelivery'), salesDeliveryInstance.id])}"
                redirect(action: "show", id: salesDeliveryInstance.id)
            } else {
                render(view: "edit", model: [salesDeliveryInstance: salesDeliveryInstance, productList : salesDeliveryService.generateListOfSalesDeliveryItems(salesDeliveryInstance), warehouses: salesDeliveryService.generateWarehouseList()])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesDelivery.label', default: 'SalesDelivery'), params.id])}"
            redirect(action: "list")
        }
    }

    def approve = {
    	def salesDeliveryInstance = SalesDelivery.get(params.id)
		if(salesDeliveryService.approveSalesDelivery(salesDeliveryInstance)){
			flash.message = "Delivery has been successfully approved!"
		} else{
			flash.message = "An error occured during approved!"
		}
        redirect(action:'show',id:salesDeliveryInstance?.id)
    }

    def unapprove = {
        def salesDeliveryInstance = SalesDelivery.get(params.id)
		if(salesDeliveryInstance.isUnapprovable()) {
			if(salesDeliveryService.unapproveSalesDelivery(salesDeliveryInstance)){
				flash.message = "Delivery has been successfully unapproved!"
			} else {
            	flash.message = "An error occured during unapproved!"
			}
		}else{
		    flash.error = "Sales Delivery can't be unapproved because SO is marked as complete or has at least one Direct Payment!"
		}
        redirect(action:'show',id:salesDeliveryInstance?.id)
    }

    def print = {
        def salesDeliveryInstance = SalesDelivery.get(params.id)
        def type
        if(params?.noPl) {
            type = PrintLogSalesDelivery.Type.INVOICE
        } else {
            type = PrintLogSalesDelivery.Type.PACKINGLIST
        }
        def user = authenticateService.userDomain()

        def canPrint = true
        
		if (authenticateService.ifNotGranted("ROLE_MANAGER_SALES")) {
			def printable = false
			def printlogs = PrintLogSalesDelivery.executeQuery("from PrintLogSalesDelivery p where p.salesDelivery=:sd and p.type=:t",[sd:salesDeliveryInstance, t:type])
			if(printlogs.size()==0) printable = true
	        def approved = ApprovalProcess.executeQuery("from ApprovalProcess a where a.status=:status and a.referenceNumber=:id and a.type=:type and a.requestedBy=:u and a.remarks=:r",[status:"Approved", id:salesDeliveryInstance.id?.intValue(), type:"Sales Delivery", u:user, r:type.toString()])
	        if(!printable && approved.size()==0){
	        	def msg = "${user.userRealName} has requested to print Sales Delivery # ${salesDeliveryInstance.salesDeliveryId} for ${type.toString()}."
	        	new ApprovalProcess(description:msg, requestedBy:user, date:new Date(), type:"Sales Delivery", referenceNumber:salesDeliveryInstance.id, remarks: type.toString()).save(flush:true)
		        redirect action:"show", id: salesDeliveryInstance.id, params:["a":"print"]
		        canPrint = false
		        return
	        }else{
	        	def printLog
	        	if(approved.size()>0){
	        		printLog = new PrintLogSalesDelivery(salesDelivery:salesDeliveryInstance,user:authenticateService.userDomain(), type:type, approvedBy:approved[0].approvedBy, approvedDate:approved[0].approvedDate)
	        		approved[0].delete()
	        	}else{
	        		printLog = new PrintLogSalesDelivery(salesDelivery:salesDeliveryInstance,user:authenticateService.userDomain(), type:type)
	        	}
				salesDeliveryInstance.addToPrintLogs(printLog)
	        }
		}else{
        	def printLog = new PrintLogSalesDelivery(salesDelivery:salesDeliveryInstance,user:authenticateService.userDomain(), type:type)
			salesDeliveryInstance.addToPrintLogs(printLog)
		}
        
        if(canPrint){
			if(salesDeliveryInstance.isUnapproved()){
	            salesDeliveryInstance.autoApprovedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
				if(salesDeliveryService.approveSalesDelivery(salesDeliveryInstance)) {
	                flash.message = "Delivery has been successfully approved!"
	            } else {
	                flash.message = "An error occured during approved!"
	            }
			}
	        salesDeliveryInstance.save(flush:true)
	        [salesDeliveryInstance: salesDeliveryInstance]
    	}
    }

    def cancel = {
        def salesDeliveryInstance = SalesDelivery.get(params.id)
		salesDeliveryInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		if(salesDeliveryService.cancelSalesDelivery(salesDeliveryInstance)){
			flash.message = "Delivery has been successfully cancelled!"			
		} else {
			flash.message = "Delivery cannot be cancelled!"
		}
        redirect(action:'show',id:salesDeliveryInstance?.id)
    }
	
    def viewPriceMargin = {
		def salesDeliveryInstance = SalesDelivery.get(params.id)
		[salesDeliveryInstance: salesDeliveryInstance]
	}
}
