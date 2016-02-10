package com.munix

class CollectionScheduleController {

	static allowedMethods = [save: "POST", update: "POST", markAsComplete: "POST"]
	def authenticateService
	def constantService
    def collectionScheduleService
	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"
		
        def collectionScheduleInstanceList = collectionScheduleService.getCollectionScheduleListWithCriteria(params)
		[collectionScheduleInstanceList: collectionScheduleInstanceList, collectionScheduleInstanceTotal: collectionScheduleInstanceList.totalCount]
	}

	def create = {
		def collectionScheduleInstance = new CollectionSchedule()
		collectionScheduleInstance.properties = params
		return [collectionScheduleInstance: collectionScheduleInstance]
	}
	
	def cancel = {
		def collectionScheduleInstance = CollectionSchedule.get(params.id)
		if(collectionScheduleInstance?.items){
			flash.error = "Collection Schedule cannot be cancelled because there are items that is still in use."
		}else{
			collectionScheduleInstance?.cancel()
			collectionScheduleInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			if(collectionScheduleInstance?.save(flush:true)){
			flash.message = "Collection Schedule has been successfully cancelled!"
			}
		}
		redirect(action:'show', id: collectionScheduleInstance?.id)
	}

	def save = {
		def collectionScheduleInstance = new CollectionSchedule(params)
		collectionScheduleInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())

		if (collectionScheduleInstance.save(flush: true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'collectionSchedule.label', default: 'CollectionSchedule'), collectionScheduleInstance.id])}"
			redirect(action: "show", id: collectionScheduleInstance.id)
		}
		else {
			render(view: "create", model: [collectionScheduleInstance: collectionScheduleInstance])
		}
	}

	def show = {
		def collectionScheduleInstance = CollectionSchedule.get(params.id)
		if (collectionScheduleInstance) {
			def isUnmarkableAsComplete = collectionScheduleService.checkIfUnmarkableAsComplete(collectionScheduleInstance)
			[collectionScheduleInstance: collectionScheduleInstance, isUnmarkableAsComplete: isUnmarkableAsComplete]
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionSchedule.label', default: 'CollectionSchedule'), params.id])}"
				redirect(action: "list")
		}
	}

	def edit = {
		def collectionScheduleInstance = CollectionSchedule.get(params.id)
		if (collectionScheduleInstance) {
			return [collectionScheduleInstance: collectionScheduleInstance]
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionSchedule.label', default: 'CollectionSchedule'), params.id])}"
			redirect(action: "list")
		}
	}

	def update = {
		def collectionScheduleInstance = CollectionSchedule.get(params.id)
		if (collectionScheduleInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (collectionScheduleInstance.version > version) {

					collectionScheduleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'collectionSchedule.label', default: 'CollectionSchedule')] as Object[], "Another user has updated this CollectionSchedule while you were editing")
					render(view: "edit", model: [collectionScheduleInstance: collectionScheduleInstance])
					return
				}
			}
			collectionScheduleInstance.properties = params
			if (!collectionScheduleInstance.hasErrors() && collectionScheduleInstance.save(flush: true)) {
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'collectionSchedule.label', default: 'CollectionSchedule'), collectionScheduleInstance.id])}"
				redirect(action: "show", id: collectionScheduleInstance.id)
			}
			else {
				render(view: "edit", model: [collectionScheduleInstance: collectionScheduleInstance])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionSchedule.label', default: 'CollectionSchedule'), params.id])}"
			redirect(action: "list")
		}
	}

	def markAsComplete = {
		def collectionScheduleInstance = CollectionSchedule.get(params.id)
		if (collectionScheduleInstance) {
			if(!collectionScheduleInstance.isComplete()){
				collectionScheduleInstance?.complete()
				collectionScheduleInstance.closedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		
				if (collectionScheduleInstance?.save()) {
					flash.message = "Collection Schedule has been successfully closed!"
				} else {
					flash.message = "Collection Schedule cannot be closed!"
				}
			}
			redirect(action:'show', id: collectionScheduleInstance?.id)
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionSchedule.label', default: 'CollectionSchedule'), params.id])}"
			redirect(action: "list")
		}
	}

	def unmarkAsComplete = {
		def collectionScheduleInstance = CollectionSchedule.get(params.id)
		if (collectionScheduleInstance) {
			collectionScheduleInstance?.processing()
			collectionScheduleInstance.closedBy = ""
	
			if (collectionScheduleInstance?.save()) {
				flash.message = "Collection Schedule has been successfully unmarked as closed!"
				redirect(action:'show',id:collectionScheduleInstance?.id)
			} else {
				flash.message = "Collection Schedule cannot be unmarked as closed!"
				redirect(action: "show", id: collectionScheduleInstance.id)
			}
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionSchedule.label', default: 'CollectionSchedule'), params.id])}"
			redirect(action: "list")
		}
	}

	def doPrint ={
		redirect(action:'show',id:params?.id)
	}
}
