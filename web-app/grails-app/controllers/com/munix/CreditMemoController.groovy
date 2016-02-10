package com.munix

class CreditMemoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", approve: "POST", cancel: "POST"]
    def authenticateService
    def constantService
    def creditMemoService
	def customerLedgerService
	def stockCardService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "id"
        if (!params.order) params.order = "asc"

        def creditMemoInstanceList = creditMemoService.generateList(params)
		[creditMemoInstanceList: creditMemoInstanceList, creditMemoInstanceTotal: creditMemoInstanceList.totalCount, discountTypes: DiscountType.list().sort{it.id}, reasons: Reason.list()]
    }

    def unpaidList = {
        if (!params.max) params.max = 100
        if (!params.offset) params.offset = 0

        def customerInstance = Customer.get(params.customerId)
        if(customerInstance){
            def filterResult = creditMemoService.getApprovedDebitMemoList(params,customerInstance)
            [debitMemoList: filterResult.debitMemoList, debitMemoListTotal: filterResult.totalCount, customerInstance:customerInstance]
        }else{
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'customer.label', default: 'Customer'), params.customerId])}"
            redirect(action: "list")
        }

    }

    def create = {
        def creditMemoInstance = new CreditMemo()
        creditMemoInstance.properties = params
        return [creditMemoInstance: creditMemoInstance]
    }

    def save = {
        def creditMemoInstance = new CreditMemo(params)
        creditMemoInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        
        def discountGroupInstance = null
        creditMemoInstance.customer.discounts.each{
            if(it.discountType == creditMemoInstance.discountType){
                discountGroupInstance = it.discountGroup
            }
        }

        creditMemoInstance.commissionRate = creditMemoInstance?.customer?.commissionRate

        if (creditMemoInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), creditMemoInstance.id])}"
            redirect(action: "show", id: creditMemoInstance.id)
        }
        else {
            render(view: "create", model: [creditMemoInstance: creditMemoInstance])
        }
    }

    def show = {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoInstance) {
            def showCancelButton = false
            if(!creditMemoInstance.isCancelled()&&!creditMemoInstance.isFirstApproved()){
                if(creditMemoService.checkIfAllCreditMemoItemsAreTheLatest(creditMemoInstance)){
                    showCancelButton = true
                }
            }
            [creditMemoInstance: creditMemoInstance,showCancelButton:showCancelButton]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoInstance) {
            return [creditMemoInstance: creditMemoInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (creditMemoInstance.version > version) {
                    
                    creditMemoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'creditMemo.label', default: 'CreditMemo')] as Object[], "Another user has updated this CreditMemo while you were editing")
                    render(view: "edit", model: [creditMemoInstance: creditMemoInstance])
                    return
                }
            }
            creditMemoInstance.properties = params
            if (!creditMemoInstance.hasErrors() && creditMemoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), creditMemoInstance.id])}"
                redirect(action: "show", id: creditMemoInstance.id)
            }
            else {
                render(view: "edit", model: [creditMemoInstance: creditMemoInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), params.id])}"
            redirect(action: "list")
        }
    }

    def approve = {
        def creditMemoInstance = CreditMemo.get(params.id)
        creditMemoInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        creditMemoInstance?.secondApproval()
        creditMemoInstance?.save(flush:true)
        flash.message = "Credit Memo is now waiting for second approval!"
        redirect(action:'show',id:creditMemoInstance?.id)
    }

    def approveTwo = {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoService.secondApproveCreditMemo(creditMemoInstance)) {
    		flash.message = "Credit Memo has been successfully approved!"
        }else{
            flash.message = "An error occurred during the approval of Credit Memo!"
        }
        redirect(action:'show',id:creditMemoInstance?.id)
    }

    def unapprove = {
        def creditMemoInstance = CreditMemo.get(params.id)
		if(creditMemoInstance.isTakenByDirectPayment) {
			flash.message = "Credit Memo is already taken!"
		} else {
        	if (creditMemoService.unapproveCreditMemo(creditMemoInstance)) {
                flash.message = "Credit Memo has been successfully unapproved!"
            }else{
                flash.error = "An error occurred during the unapproval of Credit Memo!"
            }
		}
        redirect(action:'show',id:creditMemoInstance?.id)
    }

    def cancel = {
        def creditMemoInstance = CreditMemo.get(params.id)
		creditMemoInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        if(creditMemoService.cancelCreditMemo(creditMemoInstance)){
            flash.message = "Credit Memo has been successfully cancelled!"
        }else{
            flash.error = "Credit Memo can't be cancelled because it is already approved!"
        }
        redirect(action:'show',id:creditMemoInstance?.id)
    }
    def doPrint = {
        def creditMemoInstance = CreditMemo.get(params.id)
        creditMemoInstance.addToPrintLogs(new PrintLogCreditMemo(creditMemo:creditMemoInstance,user:authenticateService.userDomain()))
        creditMemoInstance.save()
        redirect(action: "show", id: creditMemoInstance?.id)
    }
}
