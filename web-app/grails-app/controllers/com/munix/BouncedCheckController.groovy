package com.munix

class BouncedCheckController {

	static allowedMethods = [save: "POST", update: "POST", approve: "POST"]
	def messageSource
	def constantService
	def authenticateService
	def bouncedCheckService
	def editBouncedCheckService
	def customerLedgerService
	
	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

        def bouncedCheckInstanceList = bouncedCheckService.generateList(params)
		def dateMap = bouncedCheckService.generateDateStructList(params)
		[bouncedCheckInstanceList: bouncedCheckInstanceList, bouncedCheckInstanceTotal: bouncedCheckInstanceList.totalCount, dateMap: dateMap]
	}

	def filter = {
		def customers = Customer.list([sort: 'identifier'])
		[customers: customers]
	}

	def create = {
		params.remove("customer")
		def bouncedCheckInstance = new BouncedCheck(params)
		def customer = Customer.get(params['id'])
		def statuses = CheckStatus.list(sort: 'identifier')
		def checkPayments = CheckPayment.findAllByCustomerAndStatus(customer, CheckPayment.Status.DEPOSITED)
		[bouncedCheckInstance: bouncedCheckInstance, customer: customer, statuses: statuses, checkPayments : checkPayments]
	}

	def save = {
		def bouncedCheckInstance = new BouncedCheck(params)
		bouncedCheckInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())

		def checkList = request.getParameterValues('check').collect { it.toLong() }
		editBouncedCheckService.addChecksToBouncedCheck(bouncedCheckInstance, checkList)

		if (bouncedCheckInstance.save(flush:true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'bouncedCheck.label', default: 'BouncedCheck'), bouncedCheckInstance.id])}"
			redirect(action: "show", id: bouncedCheckInstance.id)
		}
		else {
			flash.error = messageSource?.getMessage(bouncedCheckInstance.errors.fieldError, Locale.getDefault()) 
			redirect(action: "create", params: params)
		}
	}

	def show = {
		def bouncedCheckInstance = BouncedCheck.get(params.id)
		if (bouncedCheckInstance) {
			def forRedepositStatus = "No"
			if(bouncedCheckInstance.forRedeposit){
				forRedepositStatus = "Yes"
			}
			[bouncedCheckInstance: bouncedCheckInstance, forRedepositStatus:forRedepositStatus]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bouncedCheck.label', default: 'BouncedCheck'), params.id])}"
			redirect(action: "list")
		}
	}

	def edit = {
		def bouncedCheckInstance = BouncedCheck.get(params.id)
		def checkPayments = CheckPayment.findAllByCustomerAndStatus(bouncedCheckInstance.customer, CheckPayment.Status.DEPOSITED)
		if (bouncedCheckInstance) {
			return [bouncedCheckInstance: bouncedCheckInstance, checkPayments: checkPayments]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bouncedCheck.label', default: 'BouncedCheck'), params.id])}"
			redirect(action: "list")
		}
	}

	def update = {
		def bouncedCheckInstance = BouncedCheck.get(params.id)
		if (bouncedCheckInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (bouncedCheckInstance.version > version) {

					bouncedCheckInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'bouncedCheck.label', default: 'BouncedCheck')] as Object[], "Another user has updated this BouncedCheck while you were editing")
					render(view: "edit", model: [bouncedCheckInstance: bouncedCheckInstance])
					return
				}
			}

			bouncedCheckInstance.properties = params

			def checkList = request.getParameterValues('check').collect { it.toLong() }
			bouncedCheckInstance = bouncedCheckService.updateChecksFromBouncedCheck(bouncedCheckInstance, checkList)

			if (!bouncedCheckInstance.hasErrors() && bouncedCheckInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'bouncedCheck.label', default: 'BouncedCheck'), bouncedCheckInstance.id])}"
				redirect(action: "show", id: bouncedCheckInstance.id)
			}
			else {
				render(view: "edit", model: [bouncedCheckInstance: bouncedCheckInstance])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bouncedCheck.label', default: 'BouncedCheck'), params.id])}"
			redirect(action: "list")
		}
	}

	def approve = {
		def bouncedCheckInstance = BouncedCheck.get(params.id)
        if(bouncedCheckInstance.checks){
            bouncedCheckInstance?.approve()
            bouncedCheckInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			bouncedCheckInstance?.addToCustomerAccountBCAmount(bouncedCheckInstance.computeTotalAmount())
            bouncedCheckInstance.checks.each{
                it.status = bouncedCheckInstance?.forRedeposit ? CheckPayment.Status.FOR_REDEPOSIT : CheckPayment.Status.BOUNCED
            }

            if (bouncedCheckInstance?.save()) {
                customerLedgerService.createApprovedBouncedCheck(bouncedCheckInstance)
                flash.message = "Bounced Check has been successfully approved!"
            }
        }
		else{
            flash.error = "Bounced Check cannot be approved when no checks are selected."
        }
		redirect(action:'show',id:bouncedCheckInstance?.id)
	}

	def unapprove = {
		def bouncedCheckInstance = BouncedCheck.get(params.id)
		if (bouncedCheckInstance.isUnapprovable()) {
			bouncedCheckInstance?.unapprove()
					bouncedCheckInstance.approvedBy = ""
						bouncedCheckInstance.checks.each{
				it.status = CheckPayment.Status.DEPOSITED
			}			
			if (bouncedCheckInstance?.save()) {
				bouncedCheckInstance?.customer.customerAccount.removeBCAmount(bouncedCheckInstance?.computeProjectedDue())
				customerLedgerService.createUnapprovedBouncedCheck(bouncedCheckInstance)
				flash.message = "Bounced Check has been successfully unapproved!"
			} else {
				flash.message = "Failed to unapprove bounced check!"
			}
		} else {
			flash.error = "Bounced Check cannot be unapproved!"
		}
		redirect(action:'show',id:bouncedCheckInstance?.id)
	}
	
	def cancel = {
		def bouncedCheckInstance = BouncedCheck.get(params.id)
		if (bouncedCheckInstance.isUnapproved()) {
			bouncedCheckInstance?.cancel()
		    bouncedCheckInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			bouncedCheckInstance?.save()
			flash.message = "Bounced Check has been successfully cancelled!"
		} else {
			flash.error = "Bounced Check can't be cancelled in this status!"
		}
		redirect(action: 'show', id: bouncedCheckInstance?.id)
	}
	
	def unpaidList = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

		def customerInstance = Customer.get(params.customerId)
		def unpaidBouncedChecks = bouncedCheckService.getUnpaidBouncedCheckList(params, customerInstance)
		def totalUnpaidBouncedChecks = bouncedCheckService.computeTotalUnpaidBouncedCheck(customerInstance)
		[customerInstance: customerInstance, totalUnpaidBouncedChecks: totalUnpaidBouncedChecks, unpaidBouncedCheckList: unpaidBouncedChecks, unpaidBouncedCheckTotal: unpaidBouncedChecks.totalCount]
	}
}
