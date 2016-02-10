package com.munix

import grails.converters.*

class MaterialReleaseController {

    static allowedMethods = [save: "POST", update: "POST", cancel: "POST", approve: "POST"]
	def messageSource
    def authenticateService
    def constantService
    def materialReleaseService
    def jobOrderService
    def stockCardService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = 100
		params.sort = "id"
        [materialReleaseInstanceList: MaterialRelease.list(params), materialReleaseInstanceTotal: MaterialRelease.count()]
    }

    def create = {
        def jobOrder = JobOrder.get(params.id)
        def materialReleaseInstance = new MaterialRelease()
		def materialRequisitionItems = jobOrderService.getNotCompletedRequisitionItems(jobOrder)
        return [materialReleaseInstance: materialReleaseInstance, jobOrder : jobOrder, materialRequisitionItems: materialRequisitionItems]
    }

    def save = {
		def materialReleaseInstance = new MaterialRelease(params)
		def jobOrderInstance = JobOrder.get(params.id)
		def materialRequisitionItems = jobOrderService.getNotCompletedRequisitionItems(jobOrderInstance)
		
		def ifNoReleaseValues = materialReleaseService.hasNoReleaseValues(materialReleaseInstance)
		if(ifNoReleaseValues) {
			flash.error = "Material Release cannot be created. There are no items to be released!"
        }
        else {
			materialReleaseInstance.jobOrder = jobOrderInstance
			materialReleaseInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			if (materialReleaseInstance.save(flush: true)){
				flash.message = "${message(code: 'default.created.message', args: [message(code: 'materialRelease.label', default: 'MaterialRelease'), materialReleaseInstance.id])}"
				redirect(action: "show", id:materialReleaseInstance.id)
			} else {
				flash.error = messageSource?.getMessage(materialReleaseInstance.errors.fieldError, Locale.getDefault())
			}
        }
		render(view: "create", model: [jobOrder: jobOrderInstance, materialRequisitionItems: materialRequisitionItems])
    }

    def show = {
        def materialReleaseInstance = MaterialRelease.get(params.id)
		def materialReleaseItems = materialReleaseInstance?.items?.findAll {it.qty > 0}
        if (materialReleaseInstance) {
            [materialReleaseInstance: materialReleaseInstance, materialReleaseItems: materialReleaseItems]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialRelease.label', default: 'MaterialRelease'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def materialReleaseInstance = MaterialRelease.get(params.id)
		def remainingBalanceList = materialReleaseService.getRemainingBalanceList(materialReleaseInstance)
		
		return [materialReleaseInstance: materialReleaseInstance, remainingBalanceList: remainingBalanceList]
    }

    def update = {
        def materialReleaseInstance = MaterialRelease.get(params.id)
        if (materialReleaseInstance) {
			def remainingBalanceList = materialReleaseService.getRemainingBalanceList(materialReleaseInstance)
			
			def hasValidReleaseValues = materialReleaseService.checkIfHasValidReleaseValuesForEdit(materialReleaseInstance, params)
			
			if(!hasValidReleaseValues) {
				flash.error = "Material Release cannot be updated. There are no items to be released!"
				render(view: "edit", model: [materialReleaseInstance: materialReleaseInstance, remainingBalanceList: remainingBalanceList])
				return
			}
			
			materialReleaseInstance.properties = params
			
            if (!materialReleaseInstance.hasErrors() && (materialReleaseInstance.save(flush: true))) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'materialRelease.label', default: 'MaterialRelease'), materialReleaseInstance.id])}"
                redirect(action: "show", id: materialReleaseInstance.id)
            }
            else {
                render(view: "edit", model: [materialReleaseInstance: materialReleaseInstance, remainingBalanceList: remainingBalanceList])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialRelease.label', default: 'MaterialRelease'), params.id])}"
            redirect(action: "list")
        }
    }

    def cancel = {
        def materialReleaseInstance = MaterialRelease.get(params.id)
		if (materialReleaseInstance) {
			materialReleaseInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
	        materialReleaseInstance?.cancel()
	        if (materialReleaseInstance?.save()) {
		        flash.message = "Material Release has been successfully cancelled!"
	        } else {
				flash.message = "Material Release cannot be cancelled!"
			}
            redirect(action:'show', id: materialReleaseInstance?.id)
		} else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialRelease.label', default: 'MaterialRelease'), params.id])}"
            redirect(action: "list")
        }
    }
	
    def approve = {
        def materialReleaseInstance = MaterialRelease.get(params.id)
		if (materialReleaseInstance) {
            if(materialReleaseService.isApprovable(materialReleaseInstance)){
                materialReleaseInstance?.approve()
                materialReleaseInstance.approvedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
				materialReleaseService.updateItemsCost(materialReleaseInstance)
                if (materialReleaseInstance?.save()) {
                    materialReleaseService.releaseStocksInWarehouse(materialReleaseInstance)
                    stockCardService.createApprovedMaterialReleaseStockCards(materialReleaseInstance)
                    flash.message = "Material Release has been successfully approved!"
                } else {
                    flash.message = "Material Release cannot be approved!"
                }
            }
	        else{
                flash.message = "Material Release cannot be approved!"
            }
            redirect(action:'show', id: materialReleaseInstance?.id)
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialRelease.label', default: 'Material Release'), params.id])}"
			redirect(action: "list", controller: "jobOrder")
		}
    }
	def unapprove = {
        def materialReleaseInstance = MaterialRelease.get(params.id)
        if(materialReleaseService.isUnapprovable(materialReleaseInstance)){
            materialReleaseInstance?.unapprove()
            materialReleaseInstance.approvedBy = ""
            materialReleaseInstance?.save()
            materialReleaseService.returnStocksToWarehouse(materialReleaseInstance)
            stockCardService.createUnapprovedMaterialReleaseStockCards(materialReleaseInstance)
            flash.message = "Material Release has been successfully unapproved!"
        }else{
            flash.message = "Material Release cannot be unapproved!"
        }
        redirect(action:'show',id:materialReleaseInstance?.id)
    }
}
