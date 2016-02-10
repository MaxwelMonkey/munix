package com.munix

class CheckDepositController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", approve: "POST"]
    def authenticateService
    def constantService
    def checkDepositService
	def checkPaymentService
    def editCheckDepositService
	def customerLedgerService
	def customerAccountsSummaryService
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

        def checkDepositInstanceList = checkDepositService.generateList(params)
		def dateMap = checkDepositService.generateDateStructList(params)
		[checkDepositInstanceList: checkDepositInstanceList, checkDepositInstanceTotal: checkDepositInstanceList.totalCount, dateMap: dateMap]
    }

    def create = {
        def checkDepositInstance = new CheckDeposit()
        checkDepositInstance.properties = params
		def checkPayments = checkPaymentService.generatePendingAndForRedepositBouncedCheck()
		
        return [checkDepositInstance: checkDepositInstance, checkPayments: checkPayments]
    }

    def save = {
        def checkDepositInstance = new CheckDeposit(params)
        checkDepositInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        def checkIdList=request.getParameterValues('checks').collect { it.toLong() }
        checkDepositInstance = editCheckDepositService.addChecksToCheckDeposit(checkIdList,checkDepositInstance)
        if (checkDepositInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), checkDepositInstance.id])}"
            redirect(action: "show", id: checkDepositInstance.id)
        }
        else {
            render(view: "create", model: [checkDepositInstance: checkDepositInstance])
        }
    }

    def show = {
        def checkDepositInstance = CheckDeposit.get(params.id)
        if (checkDepositInstance) {
            [checkDepositInstance: checkDepositInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), params.id])}"
            redirect(action: "list")
        } 
    }

    def edit = {
        def checkDepositInstance = CheckDeposit.get(params.id)
        if (checkDepositInstance) {
        	def checkPayments = checkPaymentService.generatePendingAndForRedepositBouncedCheck()
			checkPayments.addAll(0, checkDepositInstance.checks)
        	return [checkDepositInstance: checkDepositInstance, checkPayments: checkPayments]
        } else {
        	flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), params.id])}"
    		redirect(action: "list")
        }
    }

    def update = {
        def checkDepositInstance = CheckDeposit.get(params.id)
        if (checkDepositInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (checkDepositInstance.version > version) {
                    checkDepositInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'checkDeposit.label', default: 'CheckDeposit')] as Object[], "Another user has updated this CheckDeposit while you were editing")
                    render(view: "edit", model: [checkDepositInstance: checkDepositInstance])
                    return
                }
            }
            checkDepositInstance.properties = params
            def checkList = request.getParameterValues('checkBoxes').collect { it.toLong() }
            checkDepositInstance = checkDepositService.updateChecksInCheckDeposit(checkList, checkDepositInstance)
            if (!checkDepositInstance.hasErrors() && checkDepositInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), checkDepositInstance.id])}"
                redirect(action: "show", id: checkDepositInstance.id)
            }
            else {
                render(view: "edit", model: [checkDepositInstance: checkDepositInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def checkDepositInstance = CheckDeposit.get(params.id)
        if (checkDepositInstance) {
            try {
                checkDepositInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkDeposit.label', default: 'CheckDeposit'), params.id])}"
            redirect(action: "list")
        }
    }

    def approve = {
        def checkDepositInstance = CheckDeposit.get(params.id)
        checkDepositInstance?.cleared()
        checkDepositInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())

        checkDepositInstance.checks.each{
            if(it.isForRedeposit()){
                it.bouncedCheck.removeToCustomerAccountBCAmount(it.amount)
            }else{
                customerAccountsSummaryService.removeCustomerAccountCheckPayment(it)
            }
            it.status = CheckPayment.Status.DEPOSITED
			if (it?.bouncedCheck?.computeNotClearedChecks()==0) {
				it.bouncedCheck.paid()
			}
        }

        if (checkDepositInstance?.save()) {
			customerLedgerService.createApprovedCheckDeposit(checkDepositInstance)
        	flash.message = "Check Deposit has been successfully approved!"
		}
        redirect(action:'show',id:checkDepositInstance?.id)
    }

    def doPrint = {
        def checkDepositInstance = CheckDeposit.get(params.id)
        checkDepositInstance.addToPrintLogs(new PrintLogCheckDeposit(checkDeposit:checkDepositInstance,type:params._action_doPrint,user:authenticateService.userDomain()))
        checkDepositInstance.save()
        def view
        if(params._action_doPrint=="MBTC") view="mb"
        if(params._action_doPrint=="MBTC-BP") view="mbbp"
        if(params._action_doPrint=="BDO") view="bdo"
        
        redirect(controller:"print", action: "checkDeposit", id: checkDepositInstance?.id, params:["view":view])
    }
	
	def cancel = {
		def checkDepositInstance = CheckDeposit.get(params.id)
		checkDepositInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        if(checkDepositService.cancelCheckDeposit(checkDepositInstance)){
            flash.message = "Check Deposit has been successfully cancelled!"
        }else{
            flash.error = "Check Deposit can't be cancelled because it is already cleared!"
        }
		redirect(action: 'show', id: checkDepositInstance?.id)
	}
}
