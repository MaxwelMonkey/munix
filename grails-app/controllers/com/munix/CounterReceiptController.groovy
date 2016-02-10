package com.munix

class CounterReceiptController {

    static allowedMethods = [save: "POST", update: "POST", cancel:"POST", approve: "POST"]
    def constantService
    def authenticateService
    def counterReceiptService
    def editCounterReceiptService

    private final String CANBEUNAPPROVED = "Unapprovable"
    private final String COLLECTIONEXIST = "Collection Exist"
    private final String TIMEPASSED = "Time passed"

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

		def counterReceiptInstanceList = counterReceiptService.generateList(params)
		def dateMap = counterReceiptService.generateDateStructList(params)
		[counterReceiptInstanceList: counterReceiptInstanceList, counterReceiptInstanceTotal: counterReceiptInstanceList.totalCount, dateMap: dateMap]
    }

    def create = {
        def counterReceiptInstance = new CounterReceipt(params)
        if(params.id){
            counterReceiptInstance.customer = Customer.get(params?.id)
            def parameters = counterReceiptService.getAvailableCustomerPaymentsForCounterReceipt(counterReceiptInstance)
			parameters["counterReceiptInstance"] = counterReceiptInstance
            return parameters
        }
        else{
            return [counterReceiptInstance: counterReceiptInstance]
        }
    }

    def save = {
        def counterReceiptInstance = new CounterReceipt(params)
		counterReceiptInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		counterReceiptInstance.save(flush: true)
		
        List<Long> deliveryList = request.getParameterValues('deliveries').collect { it.toLong() }
        List<Long> chargeList = request.getParameterValues('charges').collect { it.toLong() }
		List<Long> creditMemoList = request.getParameterValues('creditMemos').collect { it.toLong() }
		List<Long> bouncedCheckList = request.getParameterValues('bouncedChecks').collect { it.toLong() }
		
        def mapOfCustomerPayments = [charges: chargeList, deliveries: deliveryList, creditMemos: creditMemoList, bouncedChecks: bouncedCheckList]
        if (counterReceiptService.saveCounterReceipt(counterReceiptInstance, mapOfCustomerPayments)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'counterReceipt.label', default: 'CounterReceipt'), counterReceiptInstance.id])}"
            redirect(action: "show", id: counterReceiptInstance.id)
        }
        else {
            render(view: "create", model: [counterReceiptInstance: counterReceiptInstance])
        }
    }

    def show = {
        def counterReceiptInstance = CounterReceipt.get(params.id)
        if (counterReceiptInstance) {
            def collectionSchedules = counterReceiptService.getCollectionSchedulesForCounterReceipt(counterReceiptInstance)
			def deliveries = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.SALES_DELIVERY }
			def charges = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.CUSTOMER_CHARGE }
			def creditMemos = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.CREDIT_MEMO }
			def bouncedChecks = counterReceiptInstance.items.findAll { it.invoiceType == CustomerPaymentType.BOUNCED_CHECK }
            [counterReceiptInstance: counterReceiptInstance, collectionSchedules:collectionSchedules, deliveries: deliveries, charges: charges, creditMemos: creditMemos, bouncedChecks: bouncedChecks]
        } else {
        	flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'counterReceipt.label', default: 'CounterReceipt'), params.id])}"
        		redirect(action: "list")
        }
    }

    def edit = {
        def counterReceiptInstance = CounterReceipt.get(params.id)
        if (counterReceiptInstance) {
			def parameters = counterReceiptService.getCustomerPaymentsForCounterReceipt(counterReceiptInstance)
			parameters["counterReceiptInstance"] = counterReceiptInstance
			return parameters
        }
        else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'counterReceipt.label', default: 'CounterReceipt'), params.id])}"
			redirect(action: "list")
        }
    }

    def update = {
        def counterReceiptInstance = CounterReceipt.get(params.id)
        if (counterReceiptInstance) {
			counterReceiptInstance.remark = params.remark
			counterReceiptInstance.dueDate = params.dueDate
            if (params.version) {
                def version = params.version.toLong()
                if (counterReceiptInstance.version > version) {
                    counterReceiptInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'counterReceipt.label', default: 'CounterReceipt')] as Object[], "Another user has updated this CounterReceipt while you were editing")
                    render(view: "edit", model: [counterReceiptInstance: counterReceiptInstance])
                    return
                }
            }
            List<Long> deliveryList = request.getParameterValues('deliveries').collect { it.toLong() }
            List<Long> chargeList = request.getParameterValues('charges').collect { it.toLong() }
			List<Long> creditMemoList = request.getParameterValues('creditMemos').collect { it.toLong() }
			List<Long> bouncedCheckList = request.getParameterValues('bouncedChecks').collect { it.toLong() }
			
            def mapOfCustomerPayments = [deliveries:deliveryList, charges:chargeList, creditMemos: creditMemoList, bouncedChecks: bouncedCheckList]

            if (counterReceiptService.updateCounterReceipt(counterReceiptInstance, mapOfCustomerPayments)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'counterReceipt.label', default: 'CounterReceipt'), counterReceiptInstance.id])}"
                redirect(action: "show", id: counterReceiptInstance.id)
            }
            else {
                render(view: "edit", model: [counterReceiptInstance: counterReceiptInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'counterReceipt.label', default: 'CounterReceipt'), params.id])}"
            redirect(action: "list")
        }
    }

    def approve = {
        def counterReceiptInstance = CounterReceipt.get(params.id)
        if(counterReceiptInstance){
            def approveResult = counterReceiptService.validateAndApproveCounterReceipt(counterReceiptInstance)
            if(approveResult.result){
                flash.message = approveResult.message
            }
            else{
                flash.error = approveResult.message
            }
        }
        redirect(action:'show',id:counterReceiptInstance?.id)
    }
    def unapprove = {
        def counterReceiptInstance = CounterReceipt.get(params.id)
        if(counterReceiptInstance){
            def canBeUnapproved = counterReceiptService.isUnapprovable(counterReceiptInstance)
            if(canBeUnapproved == CANBEUNAPPROVED){
                counterReceiptInstance.approvedBy = ""
                counterReceiptInstance.unapprove()
                counterReceiptInstance.save()
            }else if(canBeUnapproved == COLLECTIONEXIST){
                flash.error = "Cannot unapprove since it is assigned to a collection already"
            }else{
                flash.error = "Cannot unapprove for 2 days have already passed since the approved date"
            }
            redirect(action: "show", id:counterReceiptInstance.id)
        }else{
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'counterReceipt.label', default: 'CounterReceipt'), params.id])}"
            redirect(action: "list")
        }


    }
    def cancel = {
        def counterReceiptInstance = CounterReceipt.get(params.id)
		if(counterReceiptInstance){
            if(counterReceiptService.isCancellable(counterReceiptInstance)){
            	counterReceiptInstance?.cancel()
            	counterReceiptInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
				counterReceiptInstance?.save()
				flash.message = "Counter Receipt has been successfully cancelled!"
            } else {
            	flash.error = "Counter Receipt can't be cancelled because it is approved."
            }
			redirect(action:'show',id:counterReceiptInstance?.id)
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'counterReceipt.label', default: 'CounterReceipt'), params.id])}"
            redirect(action: "list")
        }
    }
	
    def doPrint = {
        def counterReceiptInstance = CounterReceipt.get(params.id)
        def printLog = new PrintLogCounterReceipt(counterReceipt: counterReceiptInstance, user:authenticateService.userDomain())
        counterReceiptInstance.addToPrintLogs(printLog)
        counterReceiptInstance.save(flush:true)
        redirect(controller:"print", action: "counterReceipt", id: counterReceiptInstance?.id)
    }
}
