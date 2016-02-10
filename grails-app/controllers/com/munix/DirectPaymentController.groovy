package com.munix
import grails.converters.*
import java.text.SimpleDateFormat;


class DirectPaymentController {

    static allowedMethods = [save: "POST", update: "POST", approve: "POST"]
    def constantService
    def authenticateService
	def directPaymentService
	def generalMethodService
    def customerLedgerService
	private static final APPROVABLE = "Approvable"
	private static final MOREAPPLIED = "Applied more than amount"
	private static final TOTALPAYMENTLESSTHANAPPLIED = "Payment less than applied"
	private static final NOINVOICE = "No invoice"
	private static final NOENTRIES = "No entries"
	private static final NOAPPLIED = "No amount applied"
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

        def generatedList = directPaymentService.generateList(params)
		def dateMap = directPaymentService.generateDateStructList(params)
		[directPaymentInstanceList: generatedList.directPayments, directPaymentInstanceTotal: generatedList.directPaymentsTotal, dateMap: dateMap]
    }

    def create = {
        def directPaymentInstance = new DirectPayment()
        directPaymentInstance.properties = params
		
        if(params.id) {
            def customerInstance = Customer.get(params?.id)
            def parameters = directPaymentService.getAvailableCustomerPaymentsForDirectPayment(customerInstance)
			parameters["directPaymentInstance"] = directPaymentInstance
			return parameters
        } else {
            return [directPaymentInstance: directPaymentInstance]
        }
    }

	private void checkIfCustomerExists(customer){
		if(!customer){
			flash.message = "Please select a customer"
			redirect(action: "create")
		}
	}

    def save = {
        def directPaymentInstance = new DirectPayment(params)
		checkIfCustomerExists(directPaymentInstance.customer)
        directPaymentInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		directPaymentInstance.save()
		
		def deliveryList = request.getParameterValues('deliveries').collect { it.toLong() }
		def chargeList = request.getParameterValues('charges').collect { it.toLong() }
		def bouncedCheckList = request.getParameterValues('bouncedCheck').collect{it.toLong()}
		def creditMemoList = request.getParameterValues('creditMemo').collect{it.toLong()}
		
		def mapOfCustomerPaymentLists = [deliveryList:deliveryList, chargeList:chargeList, bouncedCheckList:bouncedCheckList, creditMemoList:creditMemoList]
		
		def invoiceTakenByDirectPayment = directPaymentService.checkAllInvoicesIfTakenByDirectPayment(directPaymentInstance, mapOfCustomerPaymentLists)
		
		if(invoiceTakenByDirectPayment){
			flash.error = "Cannot create Direct Payment because of an invoice already taken by other direct payment!"
			render(view: "create", model: [directPaymentInstance: directPaymentInstance])
		} else {
		
			if (directPaymentInstance.save(flush: true)) {
				directPaymentService.generateInvoices(directPaymentInstance, mapOfCustomerPaymentLists)
				flash.message = "${message(code: 'default.created.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), directPaymentInstance.id])}"
				redirect(action: "show",id: directPaymentInstance.id)
			}
			else {
				render(view: "create", model: [directPaymentInstance: directPaymentInstance])
			}
		}
    }
	
	def edit = {
		def directPaymentInstance = DirectPayment.get(params.id)
		if (directPaymentInstance) {
            def customerPayments = directPaymentService.getCustomerPaymentsForDirectPayment(directPaymentInstance)
			customerPayments["directPaymentInstance"] = directPaymentInstance
			return customerPayments
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), params.id])}"
			redirect(action: "list")
		}
	}

	def updateDirectPayment ={
		def directPaymentInstance = DirectPayment.get(params.id)
		if(directPaymentInstance){
            def deliveryList = request.getParameterValues('deliveries').collect { it.toLong() }
            def chargeList = request.getParameterValues('charges').collect { it.toLong() }
            def bouncedCheckList = request.getParameterValues('bouncedCheck').collect{it.toLong()}
            def creditMemoList = request.getParameterValues('creditMemo').collect{it.toLong()}

            def mapOfCustomerPaymentLists = [deliveryList:deliveryList, chargeList:chargeList, bouncedCheckList:bouncedCheckList, creditMemoList:creditMemoList]
			directPaymentInstance.properties = params
			
            directPaymentService.updateDirectPaymentInvoice(directPaymentInstance, mapOfCustomerPaymentLists)

			directPaymentInstance.save(flush: true)
			flash.message = "${message(code: 'default.updated.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), directPaymentInstance.id])}"
			redirect(action: "show", id: directPaymentInstance.id)
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), params.id])}"
			redirect(action: "list")
		} 
	}
	
    def show = {
        def directPaymentInstance = DirectPayment.get(params.id)
        if (directPaymentInstance) {
		    def checker=true
		    def futureTransaction=DirectPayment.find("from DirectPayment as dP where dP.customer=? and 	dP.date>? and dP.status=?",[directPaymentInstance.customer,directPaymentInstance.date,constantService.DIRECT_PAYMENT_APPROVE])
            if(futureTransaction){
				checker=false
	    	}
			
			def items = directPaymentService?.retrieveDetailsOfItems(directPaymentInstance)
			BigDecimal paymentTotal = directPaymentInstance?.computePaymentTotal() 
			def invoices = directPaymentService?.retrieveDetailsOfInvoices(directPaymentInstance)
			Map invoiceTotals = directPaymentService?.accumulateTotals(invoices) 
			BigDecimal balance = paymentTotal - (invoiceTotals?.applied ? invoiceTotals?.applied : 0 ) 
			
			[directPaymentInstance: directPaymentInstance, checker: checker, items: items, paymentTotal: paymentTotal, invoices: invoices, invoiceTotals: invoiceTotals, balance: balance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def changeValues ={
		def directPaymentInstance = DirectPayment.get(params.id)
		if (directPaymentInstance) {
			def invoices = directPaymentService?.retrieveDetailsOfInvoices(directPaymentInstance)
			Map invoiceTotals = directPaymentService?.accumulateTotals(invoices)
			
			[directPaymentInstance: directPaymentInstance, invoices: invoices, invoiceTotals: invoiceTotals]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), params.id])}"
			redirect(action: "list")
		}
	}

	def showSelectValues = {
		def sdf = new SimpleDateFormat("MM/dd/yyyy");
		def tempPaymentType=PaymentType.list()
		def paymentType=[]
		tempPaymentType.each{
			paymentType.add([name:it.identifier,typeId:it.id,isCheck:it.isCheck,deductibleFromSales:it.deductibleFromSales])
		}
		def tempBankList=Bank.list().sort{it.identifier}
		def bankList=[]
		tempBankList.each{
			bankList.add([name:it.identifier + " - " + it.description,typeId:it.id])
		}
		def tempCheckType=CheckType.list()
		def checkType=[]
		tempCheckType.each{
			checkType.add([name:it.routingNumber,typeId:it.id,branch:it.branch])
		}
		def previousItems=[]
		def directPaymentInstance=DirectPayment.get(params.directPaymentId)
		directPaymentInstance.items.sort{it.date}.each{
			def itemDate=sdf.format(it.date)
			if(it.paymentType?.isCheck){
				previousItems.add([id:it.id,
					date:itemDate,
					payment:it.paymentType.identifier,
                    deductibleFromSales:it.paymentType.deductibleFromSales,
					amount:it.amount,
					remark:it.remark,
					checkNum:it.checkPayment?.checkNumber,
					checkDate:sdf.format(it.checkPayment?.date),
					bank:it.checkPayment?.bank?.id,
					branch:it.checkPayment?.branch,
					type:it.checkPayment?.type?.routingNumber,
					isCheck:it.paymentType?.isCheck])
			}else{
				previousItems.add([id:it.id,
					date:itemDate,
					payment:it.paymentType.identifier,
                    deductibleFromSales:it.paymentType.deductibleFromSales,
					amount:it.amount,
					remark:it.remark,
					isCheck:it.paymentType?.isCheck])
			}
			
		}
		def object = [checkType:checkType,bankList:bankList,paymentType:paymentType,previousItems:previousItems.sort{it.id}]
		render object as JSON
	}
    
	
    def update = {
        def directPaymentInstance = DirectPayment.get(params.id)
        if (directPaymentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (directPaymentInstance.version > version) {
                    directPaymentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'directPayment.label', default: 'DirectPayment')] as Object[], "Another user has updated this DirectPayment while you were editing")
                    render(view: "edit", model: [directPaymentInstance: directPaymentInstance])
                    return
                }
            }
			def itemId=request.getParameterValues("itemId").collect { it.toLong() }
			def createdItemId=request.getParameterValues("createdItemId").collect { it.toLong() }
			directPaymentService.updateDirectPayment(directPaymentInstance, params, itemId, createdItemId)
            directPaymentInstance.properties = params
            if (!directPaymentInstance.hasErrors() && directPaymentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), directPaymentInstance.id])}"
                redirect(action: "show", id: directPaymentInstance.id)
            }
            else {
                render(view: "edit", model: [directPaymentInstance: directPaymentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directPayment.label', default: 'DirectPayment'), params.id])}"
            redirect(action: "list")
        }
    }

	def unapprove = {
		def directPaymentInstance = DirectPayment.get(params.id)
		if (directPaymentInstance.isNotUnapprovable()) {
			flash.error = message([code: directPaymentInstance.isNotUnapprovable()])
		} else {
			if(!directPaymentInstance?.isUnapproved()){
				directPaymentInstance.approvedBy = ""
	            directPaymentService.removePendingChecksFromCustomerAccountService(directPaymentInstance)
				directPaymentInstance?.unapproveTransaction()
				directPaymentInstance?.overpayment?.unapproved()
				directPaymentInstance?.save(flush:true)
				customerLedgerService?.unapproveDirectPayment(directPaymentInstance)
				flash.message = "Direct Payment has been successfully unapproved"
			}
		}
		redirect(action: "show", id: directPaymentInstance?.id)
	}

    def approve = {
        def directPaymentInstance = DirectPayment.get(params.id)
		if(directPaymentInstance){
            def status =  directPaymentService.isApprovable(directPaymentInstance)
			if (status==TOTALPAYMENTLESSTHANAPPLIED) {
				flash.error = "The direct payment cannot be approved because total payment is less than total amount applied."
			}else if(status==MOREAPPLIED){
                flash.error = "The direct payment cannot be approved because one or more of the amount applied is more than the amount needed."
			}else if(status==NOAPPLIED){
				flash.error = "The direct payment cannot be approved because there is no amount applied."
			}else if(status==NOINVOICE){
				flash.error = "The direct payment cannot be approved because it must have at least one invoice."
			}else if(status==NOENTRIES){
				flash.error = "The direct payment cannot be approved because it must have at least one payment entry."
            }else if(status==APPROVABLE){
            	if(!directPaymentInstance.isApproved()){
					directPaymentInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
					directPaymentInstance?.approveTransaction()
					
					if(directPaymentInstance?.save(flush:true)){
						directPaymentService.checkAndGenerateOverpayment(directPaymentService.generateComputedBalance(directPaymentInstance), directPaymentInstance)
		                directPaymentService.addPendingChecksToCustomerAccountService(directPaymentInstance)
						customerLedgerService?.approveDirectPayment(directPaymentInstance)
						flash.message = "Direct Payment has been successfully approved"
					}
            	}
			}
        }
        redirect(action: "show", id: directPaymentInstance?.id)
    }
	
    def doPrint = {
        def directPaymentInstance = DirectPayment.get(params.id)
        directPaymentInstance.addToPrintLogs(new PrintLogDirectPayment(directPayment:directPaymentInstance,user:authenticateService.userDomain()))
        directPaymentInstance.save()
        redirect(controller:"print", action: "directPayment", id: directPaymentInstance?.id)
    }

    def cancel = {
        def directPaymentInstance = DirectPayment.get(params.id)
        if(directPaymentInstance.items||directPaymentInstance.invoices){
            flash.message = "Direct Payment cannot be cancelled because there are items that is still in use."
        }else{
		    directPaymentInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
            directPaymentInstance.cancelled()
            if(directPaymentInstance?.save(flush:true)){
                flash.message = "Direct Payment has been successfully cancelled"
            }
        }
        redirect(action: "show", id: directPaymentInstance?.id)
    }
}

