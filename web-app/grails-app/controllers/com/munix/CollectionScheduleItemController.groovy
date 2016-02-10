package com.munix
import grails.converters.*

class CollectionScheduleItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def collectionScheduleItemService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [collectionScheduleItemInstanceList: CollectionScheduleItem.list(params), collectionScheduleItemInstanceTotal: CollectionScheduleItem.count()]
    }

    def create = {
        def collectionScheduleItemInstance = new CollectionScheduleItem()
        collectionScheduleItemInstance.properties = params				
		def counterReceiptList=collectionScheduleItemService.generateCounterReceiptList(CollectionSchedule.get(params?.id))
        return [collectionScheduleItemInstance: collectionScheduleItemInstance, counterReceiptList: counterReceiptList]
    }

    def save = {
        def collectionScheduleItemInstance = new CollectionScheduleItem(params)
        collectionScheduleItemInstance?.schedule = CollectionSchedule.get(params?.id)
        
        if (collectionScheduleItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), collectionScheduleItemInstance.id])}"
            redirect(controller: "collectionSchedule", action: "show", id: collectionScheduleItemInstance.schedule.id)
        }
        else {
            def counterReceiptList=collectionScheduleItemService.generateCounterReceiptList(collectionScheduleItemInstance.schedule)
            render(view: "create", model: [collectionScheduleItemInstance: collectionScheduleItemInstance, counterReceiptList: counterReceiptList])
        }
    }

    def show = {
        def collectionScheduleItemInstance = CollectionScheduleItem.get(params.id)
        if (collectionScheduleItemInstance) {
            [collectionScheduleItemInstance: collectionScheduleItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def collectionScheduleItemInstance = CollectionScheduleItem.get(params.id)
        if (collectionScheduleItemInstance) {
            return [collectionScheduleItemInstance: collectionScheduleItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def collectionScheduleItemInstance = CollectionScheduleItem.get(params.id)
        if (collectionScheduleItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (collectionScheduleItemInstance.version > version) {
                    
                    collectionScheduleItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem')] as Object[], "Another user has updated this CollectionScheduleItem while you were editing")
                    render(view: "edit", model: [collectionScheduleItemInstance: collectionScheduleItemInstance])
                    return
                }
            }
            collectionScheduleItemInstance.properties = params
            if (!collectionScheduleItemInstance.hasErrors() && collectionScheduleItemInstance.save(flush: true)) {
				collectionScheduleItemService.completeCounterCollectionDate(collectionScheduleItemInstance)
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), collectionScheduleItemInstance.id])}"
                redirect(controller: "collectionSchedule", action: "show", id: collectionScheduleItemInstance.schedule.id)
            }
            else {
                render(view: "edit", model: [collectionScheduleItemInstance: collectionScheduleItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def collectionScheduleItemInstance = CollectionScheduleItem.get(params.id)
        def collectionScheduleId = collectionScheduleItemInstance?.schedule?.id
        if (collectionScheduleItemInstance) {
            try {
				removeCounterReceiptDate(collectionScheduleItemInstance)
                collectionScheduleItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), params.id])}"
                redirect(controller:"collectionSchedule", action: "show", id:collectionScheduleId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'collectionScheduleItem.label', default: 'CollectionScheduleItem'), params.id])}"
            redirect(action: "list")
        }
    }
	
	private void removeCounterReceiptDate(CollectionScheduleItem collectionScheduleItemInstance) {
		if ("Collection".equals(collectionScheduleItemInstance.type) || "Both".equals(collectionScheduleItemInstance.type)) {
			collectionScheduleItemInstance.counterReceipt.collectionDate = null
		}
		if ("Counter".equals(collectionScheduleItemInstance.type) || "Both".equals(collectionScheduleItemInstance.type)) {
			collectionScheduleItemInstance.counterReceipt.counterDate = null
		}
	}
	
	def updateSelectType ={
        def typeList = collectionScheduleItemService.generateTypeList(params.selectedValue)
        def amount = CounterReceipt.get(params.selectedValue).computeInvoicesTotal()
		def jsonMap=[typeList:typeList,amount:amount]
		render jsonMap as JSON
	}
}
