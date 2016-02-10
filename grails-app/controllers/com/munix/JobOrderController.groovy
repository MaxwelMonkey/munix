package com.munix

import grails.converters.*

class JobOrderController {

	static allowedMethods = [save: "POST", update: "POST", cancel: "POST", markAsComplete: "POST", approve: "POST"]
	def authenticateService
	def constantService
	def productSearchService
	def jobOrderService

	def index = {
		redirect(action: "list", params: params)
	}

	def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

		def assemblers = Assembler.list().sort{it.toString()}
		def jobOrders = jobOrderService.generateList(params)
		def dateMap = jobOrderService.generateDateStructList(params)
		return [jobOrderInstanceList: jobOrders.jobOrders, jobOrderInstanceTotal: jobOrders.jobOrdersTotal, assemblers: assemblers, statuses: JobOrder.Status, dateMap: dateMap]
	}

	def create = {
		def jobOrderInstance = new JobOrder()
		jobOrderInstance.properties = params
		return [jobOrderInstance: jobOrderInstance]
	}

	def save = {
		params.startDate_hour = params.startDateHour
		params.startDate_minute = params.startDateMinute
		params.targetDate_hour = params.targetDateHour
		params.targetDate_minute = params.targetDateMinute
		
		if(!params.qty) {
			params.qty = 0
		}
		
		def jobOrderInstance = new JobOrder(params)
		def user = authenticateService.userDomain()
		jobOrderInstance.preparedBy = FormatUtil.createTimeStamp(user)
		jobOrderInstance.requisition = new MaterialRequisition(preparedBy : user.username, jobOrder : jobOrderInstance)
		jobOrderInstance.product?.components?.each{
			jobOrderInstance.requisition?.addToItems(new MaterialRequisitionItem(
					unitsRequired: it.qty,
					component : it.component,
					requisition : jobOrderInstance.requisition
					))
		}

		if (jobOrderInstance.save(flush: true, cascade : true)) {
			flash.message = "${message(code: 'default.created.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), jobOrderInstance.id])}"
			redirect(action: "show", params:[jobOrderId : jobOrderInstance.id])
		}
		else {
			render(view: "create", model: [jobOrderInstance: jobOrderInstance])
		}
	}

    def show = {
        def jobOrderInstance = JobOrder.get(params.jobOrderId)
        if (jobOrderInstance) {
            def canBeCompleted = jobOrderService.isCompletable(jobOrderInstance)
            def canBeUnmarkedAsCompleted = jobOrderService.unmarkableAsComplete(jobOrderInstance)
            [jobOrderInstance: jobOrderInstance, canBeCompleted:canBeCompleted,canBeUnmarkedAsCompleted:canBeUnmarkedAsCompleted ]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), params.id])}"
            redirect(action: "list")
        }
    }

    def stockAvailability = {
        def jobOrderInstance = JobOrder.get(params.id)
        if (jobOrderInstance) {
            def canBeCompleted = jobOrderService.isCompletable(jobOrderInstance)
            def canBeUnmarkedAsCompleted = jobOrderService.unmarkableAsComplete(jobOrderInstance)
            [jobOrderInstance: jobOrderInstance, canBeCompleted:canBeCompleted,canBeUnmarkedAsCompleted:canBeUnmarkedAsCompleted ]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), params.id])}"
            redirect(action: "list")
        }
    }

	def edit = {
		def jobOrderInstance = JobOrder.get(params.jobOrderId)
		if (jobOrderInstance) {
			def jobOrderMaterials = jobOrderService.generateJobOrderMaterials(jobOrderInstance)
			[jobOrderInstance: jobOrderInstance, components: jobOrderMaterials]
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), params.id])}"
			redirect(action: "list")
		}
	}
	def jobOrderEdit = {
		def jobOrderInstance = JobOrder.get(params.jobOrderId)
		if (jobOrderInstance) {
			[jobOrderInstance: jobOrderInstance]
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), params.id])}"
			redirect(action: "list")
		}
	}
	
	def updateJobOrder = {
		def jobOrderInstance = JobOrder.get(params.id)
		if (jobOrderInstance) {
			params.startDate_hour = params.startDateHour
			params.startDate_minute = params.startDateMinute
			params.targetDate_hour = params.targetDateHour
			params.targetDate_minute = params.targetDateMinute
			jobOrderInstance.properties = params
			if(jobOrderInstance.save(flush:true)&&!jobOrderInstance.hasErrors()){
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), jobOrderInstance.id])}"
				redirect(action: "show", params:[jobOrderId: jobOrderInstance.id])
			}else{
				render(view: "jobOrderEdit", model: [jobOrderInstance: jobOrderInstance])
			}
		}
		else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), params.id])}"
			redirect(action: "list")
		}
	}

	def update = {
		def jobOrderInstance = JobOrder.get(params.id)
		def username = authenticateService.userDomain().username
		def isMaterialsEmpty = false

		if(!jobOrderInstance.requisition){
			jobOrderInstance.requisition = new MaterialRequisition(preparedBy : username, jobOrder : jobOrderInstance)
		}
		
		def jobOrder = new JobOrder()
		jobOrder.properties = jobOrderInstance.properties
		jobOrder.id = jobOrderInstance.id
		jobOrder?.requisition?.properties = params
		def components = jobOrder?.requisition?.items
		for(def item: components) {
			item.unitsRequired = item.unitsRequired ?: BigDecimal.ZERO 
		}
		def toBeDeleted = jobOrder?.requisition?.items.findAll {it.isDeleted}
		
		if (toBeDeleted) {
			if(toBeDeleted.size() >= components.size()) {
				isMaterialsEmpty = true
				toBeDeleted.each { deleted->
					deleted.isDeleted = false
				}
			} else {
				toBeDeleted.each { deleted->
					jobOrder.requisition.removeFromItems(deleted)
					deleted.delete()
				}
				jobOrder?.requisition?.items.removeAll(toBeDeleted)
			}
		}
		
		if (jobOrderInstance) {
			jobOrder.validate()
			if (isJobOrderVersionNotCorrect(jobOrderInstance, params)) {
				render(view: action, model: model)
			} else if(jobOrder.hasErrors() || isMaterialsEmpty) {
				if (isMaterialsEmpty) {
					flash.errors = "Job order must have at least one material item."
				}
				render(view: "edit", model: [jobOrderInstance: jobOrder, components: jobOrderService.generateJobOrderMaterials(jobOrder)])
			} else {
				jobOrderInstance.properties = jobOrder.properties
				jobOrderInstance.save(flush: true, cascade : true)
				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), jobOrderInstance.id])}"
				redirect(action: "show", params:[jobOrderId: jobOrderInstance.id])
			}
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'JobOrder'), params.id])}"
			redirect(action: "list")
		}
	}

	def markAsComplete = {
		def jobOrderInstance = JobOrder.get(params.id)
        if(jobOrderService.isCompletable(jobOrderInstance)){
            jobOrderInstance.complete()
            jobOrderInstance.endDate = new Date()
            jobOrderInstance.markAsCompleteBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
            flash.message = "The job order has been successfully completed!"
        }else{
            flash.message = "The job order cannot be completed!"
        }
		redirect(action:"show", params:[jobOrderId:jobOrderInstance?.id])
	}
	
    def unmarkAsComplete = {
		def jobOrderInstance = JobOrder.get(params.id)
        if(jobOrderService.unmarkableAsComplete(jobOrderInstance)){
            jobOrderInstance.approveMaterialReleases()
            jobOrderInstance.endDate = null
            jobOrderInstance.markAsCompleteBy = ""
            flash.message = "The job order has been unmarked as complete!"
        }else{
            flash.message = "The job order cannot be unmarked as complete!"
        }
		redirect(action:"show", params:[jobOrderId:jobOrderInstance?.id])
	}

	def approve = {
		def jobOrderInstance = JobOrder.get(params.id)
		if(!jobOrderInstance?.isJobOrderApproved()){
			jobOrderInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
	        jobOrderInstance.approveJobOrder()
			if(jobOrderInstance?.save(flush:true)) {
				flash.message = "The job order is ready for item release!"
			}
		}
		redirect(action:"show", params:[jobOrderId:jobOrderInstance?.id])
	}

	def unapprove = {
		def jobOrderInstance = JobOrder.get(params.id)
		if(jobOrderService.checkIfUnapprovable(jobOrderInstance)) {
			if(!jobOrderInstance?.isUnapproved()){
				jobOrderInstance.approvedBy = ""
				jobOrderInstance.unapprove()
				jobOrderInstance?.save(flush:true)
				flash.message = "The job order has been unapproved!"
			}
		} else {
			flash.error = "Job order cannot be unapproved! Active Material Release existing."
		}
		redirect(action:"show", params:[jobOrderId:jobOrderInstance?.id])
	}
	
	def addMaterialRelease = {
		def jobOrderInstance = JobOrder.get(params.id)
		if(jobOrderInstance.hasAllRequisitonItemsReleased()) {
			flash.error = "There are no more materials that need releasing."
			redirect(action:"show", params:[jobOrderId:jobOrderInstance?.id])
		} else {
			redirect(action: "create", controller: "materialRelease", id: jobOrderInstance?.id)
		}		
	}

	def addJobOut = {
		def jobOrderInstance = JobOrder.get(params.id)
		if(jobOrderInstance.isMaterialReleasesApproved()){
			if (jobOrderInstance.computeRemainingBalance() > 0) {
				redirect(action: "create", controller: "jobOut", id: jobOrderInstance?.id)
			} else {
				flash.error = "Job out can no longer be added because remaining balance is 0."
				redirect(action: "show", params: [jobOrderId:jobOrderInstance?.id])
			}
		} else {
			flash.error = "Job out cannot be added in this status. Status should be Material Releases Approved."
			redirect(action:"show", params:[jobOrderId:jobOrderInstance?.id])
		}
	}
	
	def cancel = {
		def jobOrderInstance = JobOrder.get(params.id)
		jobOrderInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
		jobOrderInstance.cancel()
		flash.message = "The job order is cancelled!"
		redirect(action:"show", params:[jobOrderId:jobOrderInstance?.id])
	}

	def approveMaterialReleases = {
		def jobOrderInstance = JobOrder.get(params.id)
		if (jobOrderInstance) {
            jobOrderInstance.approveMaterialReleases()
            jobOrderInstance.materialReleasesApprovedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			if (jobOrderInstance.hasAllMaterialReleasesApproved() && jobOrderInstance?.save(flush:true)) {

				flash.message = "Material Releases have been approved!"
			} else {
				flash.message = "Material Releases cannot be approved!"
			}
            redirect(action:"show", params: [jobOrderId: jobOrderInstance?.id])
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'Job Order'), params.id])}"
			redirect(action: "list")
		}
	}

	def unapproveMaterialReleases = {
		def jobOrderInstance = JobOrder.get(params.id)
		if (jobOrderInstance) {
			if (jobOrderInstance.hasAllJobOutCancelled()&&jobOrderInstance.isMaterialReleasesApproved() && jobOrderInstance?.save(flush:true)) {
                jobOrderInstance.unapproveMaterialReleases()
			    jobOrderInstance.materialReleasesApprovedBy = ""
				flash.message = "Material Releases have been unapproved!"
			} else {
				flash.error = "Material Releases cannot be unapproved because there is at least one Job Out!"
			}
            redirect(action:"show", params: [jobOrderId: jobOrderInstance?.id])
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'jobOrder.label', default: 'Job Order'), params.id])}"
			redirect(action: "list")
		}
	}

    private isJobOrderVersionNotCorrect(jobOrderInstance, params){
        def isNotCorrect = false
        if(params?.version){
            def version = params.version.toLong()
            if (jobOrderInstance.version > version) {
                jobOrderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'jobOrder.label', default: 'JobOrder')] as Object[], "Another user has updated this JobOrder while you were editing")
                isNotCorrect = true
            }
        }
        return isNotCorrect
    }

	def retrieveJobOrderMaterials = {
		def searchField = params.sSearch
		def products = Product.list().findAll {it.identifier.toLowerCase() =~ searchField.toLowerCase() || it.description.toLowerCase() =~ searchField.toLowerCase()}
		def data = createJobOrderMaterialsDataMap(products)

		def jsonResponse = [iTotalRecords : data.size(), iTotalDisplayRecords : data.size(), aaData : data]
		render jsonResponse as JSON
	}

	private List createJobOrderMaterialsDataMap(List products) {
		try{
		def data = []
		def productId = params.productId
		def qty = params.jobOrderQty.toLong()
		def wList = Warehouse.list().sort{it.identifier}
		products?.each {
			def pc = ProductComponent.findByComponentAndProduct(it, Product.get(productId.toLong()))
			if(it.isComponent) {
				def product = it
				def dataMap = []
				dataMap.addAll(
						it.id,
						it.identifier,
						pc ? pc.computeNeededQty(qty) : qty,
						pc ? pc.qty : new BigDecimal("1"),
						it?.formatDescription()
						)
				wList.each{
					def warehouse = it
					def stock = product.stocks?.find{it.warehouse?.id == warehouse.id}  
					dataMap.add(stock?.formatQty())
				}
				data.add(dataMap)
			}
		}
		return data
		}catch(Exception e){
			e.printStackTrace()
			return []
		}
	}
}
