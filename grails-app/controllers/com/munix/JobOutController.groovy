package com.munix

class JobOutController {

	static allowedMethods = [save: "POST", update: "POST", cancel: "POST", approve: "POST"]
	def authenticateService
	def constantService
	def jobOutService
	def stockCardService

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		params.max = 100
		[jobOutInstanceList: JobOut.list(params), jobOutInstanceTotal: JobOut.count()]
	}

	def create = {
		def jobOutInstance = new JobOut(params)

		return [jobOutInstance: jobOutInstance]
	}

	def save = {
		def jobOutInstance = new JobOut(params)
		def jobOrderInstance = JobOrder.get(params.id)
		jobOutInstance.jobOrder = jobOrderInstance
		jobOutInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		if (!jobOutService.validateQuantity(new BigDecimal(params.qty), jobOutInstance?.jobOrder?.computeRemainingBalance())) {
			jobOutInstance.errors.rejectValue("qty", "jobOut.qty.exceeding.remaining.balance", [message(code: 'jobOut.label', default: 'Job Out')] as Object[], "Quantity cannot exceed remaining balance")
		}
		if (!jobOutInstance.hasErrors() && jobOutInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'jobOut.label', default: 'jobOut'), jobOutInstance.id])}"
			redirect(action: "show", id: jobOutInstance.id)
		}
		else {
			render(view: "create", model: [jobOutInstance: jobOutInstance, id: params.id])
		}
	}

	def show = {
		def jobOutInstance = JobOut.get(params.id)

		if (jobOutInstance) {
			def laborCost = jobOutInstance?.laborCost?.amount ?: BigDecimal.ZERO
			def componentsCost =  jobOutInstance?.jobOrder?.requisition?.computeTotalCostOfMaterialsPerAssembly() ?: BigDecimal.ZERO
			def totalCostPerAssembly = laborCost + componentsCost
			def jobOutTotalCost = totalCostPerAssembly * jobOutInstance?.qty
			[jobOutInstance: jobOutInstance, totalCostPerAssembly : totalCostPerAssembly, componentsCost: componentsCost, jobOutTotalCost : jobOutTotalCost]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOut.label', default: 'jobOut'), params.id])}"
			redirect(action: "list")
		}
	}

	def edit = {
		def jobOutInstance = JobOut.get(params.id)
		if (jobOutInstance) {
			return [jobOutInstance: jobOutInstance]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOut.label', default: 'jobOut'), params.id])}"
			redirect(action: "list")
		}
	}

	def update = {
		def jobOutInstance = JobOut.get(params.id)
		if (jobOutInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (jobOutInstance.version > version) {

					jobOutInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'jobOut.label', default: 'jobOut')] as Object[], "Another user has updated this jobOut while you were editing")
					render(view: "edit", model: [jobOutInstance: jobOutInstance])
					return
				}
			}
			def remaining = jobOutInstance.qty + jobOutInstance?.jobOrder?.computeRemainingBalance()
			if (jobOutInstance.qty && !jobOutService.validateQuantity(new BigDecimal(params.qty), remaining)) {
				jobOutInstance.errors.rejectValue("qty", "jobOut.qty.exceeding.remaining.balance", [message(code: 'jobOut.label', default: 'Job Out')] as Object[], "Quantity cannot exceed remaining balance")
			} else {
				jobOutInstance.properties = params
			}
			if (!jobOutInstance.hasErrors() && jobOutInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'jobOut.label', default: 'jobOut'), jobOutInstance.id])}"
				redirect(action: "show", id: jobOutInstance.id)
			} else {
				render(view: "edit", model: [jobOutInstance: jobOutInstance])
			}
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOut.label', default: 'jobOut'), params.id])}"
			redirect(action: "list")
		}
	}

	def cancel = {
		def jobOutInstance = JobOut.get(params.id)
		jobOutInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		jobOutInstance?.cancel()
		if (jobOutInstance?.save()) {
			flash.message = "Job Out has now been cancelled!"
			redirect(action: 'show', id: jobOutInstance?.id)
		} else {
			flash.message = "Unable to cancel Job Out!"
			redirect(action:'show', id: jobOutInstance?.id)
		}
	}

	def approve = {
		def jobOutInstance = JobOut.get(params.id)
		if (jobOutService.approve(jobOutInstance)) {
			flash.message = "Job Out has been successfully approved!"
		} else {
			flash.message = "Job Out cannot be approved!"
		}
		redirect(action:'show', id: jobOutInstance?.id)

	}

	def unapprove = {
		def jobOutInstance = JobOut.get(params.id)
		if(jobOutService.isUnapprovable(jobOutInstance)){
			jobOutInstance.approvedBy = ""
			jobOutInstance.unapprove()
			if (jobOutInstance?.save()) {
				jobOutService.removeStocksInJobOut(jobOutInstance)
				stockCardService.createUnapprovedJobOut(jobOutInstance)
				flash.message = "Job Out has been unapproved!"
			} else {
				flash.message = "Job Out cannot be unapproved!"
			}
		} else {
			flash.message = "Job Out cannot be unapproved!"
		}
		redirect(action:'show', id: jobOutInstance?.id)
	}
}
