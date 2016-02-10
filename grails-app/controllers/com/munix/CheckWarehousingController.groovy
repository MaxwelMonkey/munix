package com.munix

class CheckWarehousingController {

    static allowedMethods = [save: "POST", update: "POST", approve: "POST"]
    def authenticateService
    def constantService
    def checkWarehousingService
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        if (!params.max) params.max = 100
        [checkWarehousingInstanceList: CheckWarehousing.list(params), checkWarehousingInstanceTotal: CheckWarehousing.count()]
    }

    def create = {
        def checkWarehousingInstance = new CheckWarehousing()
        checkWarehousingInstance.properties = params
        def originWarehouse = CheckWarehouse.get(params."originWarehouse.id")
        def checkPayments = checkWarehousingService.getDirectPaymentChecksWithCriteria(params, originWarehouse)
        return [checkWarehousingInstance: checkWarehousingInstance, checkPayments: checkPayments, originWarehouse: params.originWarehouse]
    }

    def save = {
        def checkWarehousingInstance = new CheckWarehousing(params)
        checkWarehousingInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        def checks = request.getParameterValues('checks').collect { it.toLong() }
        if(checks){
            checkWarehousingInstance.checks = CheckPayment.getAll(checks)
        }
        if (checkWarehousingInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'checkWarehousing.label', default: 'CheckWarehousing'), checkWarehousingInstance.id])}"
            redirect(action: "show", id: checkWarehousingInstance.id)
        }
        else {
            render(view: "create", model: [checkWarehousingInstance: checkWarehousingInstance])
        }
    }

    def show = {
        def checkWarehousingInstance = CheckWarehousing.get(params.id)
        if (checkWarehousingInstance) {
            [checkWarehousingInstance: checkWarehousingInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkWarehousing.label', default: 'CheckWarehousing'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def checkWarehousingInstance = CheckWarehousing.get(params.id)
        if (checkWarehousingInstance) {
            def checkPayments = CheckPayment.findAllByStatusAndWarehouse(CheckPayment.Status.PENDING,checkWarehousingInstance.originWarehouse)
        	return [checkWarehousingInstance: checkWarehousingInstance, checkPayments:checkPayments]
        } else {
        	flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkWarehousing.label', default: 'CheckWarehousing'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def checkWarehousingInstance = CheckWarehousing.get(params.id)
        if (checkWarehousingInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (checkWarehousingInstance.version > version) {
                    
                    checkWarehousingInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'checkWarehousing.label', default: 'CheckWarehousing')] as Object[], "Another user has updated this CheckWarehousing while you were editing")
                    render(view: "edit", model: [checkWarehousingInstance: checkWarehousingInstance])
                    return
                }
            }
            checkWarehousingInstance.properties = params
			def checks = request.getParameterValues('checks').collect { it.toLong() }
            if(checks){
                checkWarehousingInstance.checks = CheckPayment.getAll(checks)
            }
            if (!checkWarehousingInstance.hasErrors() && checkWarehousingInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'checkWarehousing.label', default: 'CheckWarehousing'), checkWarehousingInstance.id])}"
                redirect(action: "show", id: checkWarehousingInstance.id)
            }
            else {
                render(view: "edit", model: [checkWarehousingInstance: checkWarehousingInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkWarehousing.label', default: 'CheckWarehousing'), params.id])}"
            redirect(action: "list")
        }
    }

    def filter ={
        def checkWarehousingInstance = new CheckWarehousing()
        render(view: "filter", model: [checkWarehousingInstance: checkWarehousingInstance])
    }

    def approve = {
        def checkWarehousingInstance = CheckWarehousing.get(params.id)
        if(checkWarehousingInstance?.status!=constantService.CHECK_WAREHOUSING_APPROVED){
	        checkWarehousingInstance?.status = constantService.CHECK_WAREHOUSING_APPROVED
	        checkWarehousingInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
	
	        checkWarehousingInstance.checks.each{
	            it.warehouse = checkWarehousingInstance.destinationWarehouse
	        }
	
	        checkWarehousingInstance?.save()
	        flash.message = "Check Warehousing has been successfully approved!"
        }
        redirect(action:'show',id:checkWarehousingInstance?.id)
    }
}
