package com.munix

class DirectDeliveryController {

    static allowedMethods = [save: "POST", update: "POST", cancel: "POST"]
    def authenticateService
    def directDeliveryService
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"
		
        def directDeliveryInstanceList = directDeliveryService.getDirectDeliveryListWithCriteria(params)
		def dateMap = directDeliveryService.generateDateStructList(params)
        [directDeliveryInstanceList: directDeliveryInstanceList, directDeliveryInstanceTotal: directDeliveryInstanceList.totalCount, dateMap: dateMap]
    }

    def create = {
        def directDeliveryInstance = new DirectDelivery()
        directDeliveryInstance.properties = params
        if(params.id){
            def customerInstance = Customer.get(params?.id)
            def deliveryList = SalesDelivery.findAllByCustomer(customerInstance)
            Set deliveryInstance = []
            deliveryList.each{ delivery ->
                if(!delivery.isUnapproved() && !delivery.isCancelled()&& delivery.isDeliver() && !delivery.waybill && !delivery.directDelivery){
                    deliveryInstance << delivery
                }
            }

            return [directDeliveryInstance: directDeliveryInstance, deliveryInstance: deliveryInstance]
        }

        else{
            return [directDeliveryInstance: directDeliveryInstance]
        }

    }

    def save = {
        def directDeliveryInstance = new DirectDelivery(params)
        def customerInstance = Customer.get(params."customer.id")
        directDeliveryInstance.customer = customerInstance
        directDeliveryInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())

        if (directDeliveryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'directDelivery.label', default: 'DirectDelivery'), directDeliveryInstance.id])}"
            redirect(action: "show", id: directDeliveryInstance.id)
        }
        else {
            render(view: "create", model: [directDeliveryInstance: directDeliveryInstance])
        }
    }

    def show = {
        def directDeliveryInstance = DirectDelivery.get(params.id)
        if (directDeliveryInstance) {
            [directDeliveryInstance: directDeliveryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directDelivery.label', default: 'DirectDelivery'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def directDeliveryInstance = DirectDelivery.get(params.id)
        if (directDeliveryInstance) {
            if(directDeliveryInstance.tripTicket){
                flash.error = "Direct Delivery cannot be editted because it has at least one trip ticket!"
				redirect(action: "show", id: directDeliveryInstance.id)
            }else{
                def deliveryList = SalesDelivery.findAllByCustomer(Customer.get(directDeliveryInstance?.customer?.id))
                Set deliveryInstance = []
                deliveryList.each{ delivery ->
                    if(!delivery.isUnapproved() && !delivery.isCancelled()&& delivery.isDeliver() && !delivery.waybill && !delivery.directDelivery){
                        deliveryInstance << delivery
                    }
				}
                return [directDeliveryInstance: directDeliveryInstance, deliveryInstance: deliveryInstance]
            }

        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directDelivery.label', default: 'DirectDelivery'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def directDeliveryInstance = DirectDelivery.get(params.id)
        if (directDeliveryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (directDeliveryInstance.version > version) {
                    
                    directDeliveryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'directDelivery.label', default: 'DirectDelivery')] as Object[], "Another user has updated this DirectDelivery while you were editing")
                    render(view: "edit", model: [directDeliveryInstance: directDeliveryInstance])
                    return
                }
            }
            if(directDeliveryInstance.tripTicket){
                flash.error = "Direct Delivery cannot be editted because it has at least one trip ticket!"
				redirect(action: "show", id: directDeliveryInstance.id)
            }else{
                directDeliveryInstance.properties = params
                if (!directDeliveryInstance.hasErrors() && directDeliveryInstance.save(flush: true)) {
                    flash.message = "${message(code: 'default.updated.message', args: [message(code: 'directDelivery.label', default: 'DirectDelivery'), directDeliveryInstance.id])}"
                    redirect(action: "show", id: directDeliveryInstance.id)
                }
                else {
                    render(view: "edit", model: [directDeliveryInstance: directDeliveryInstance])
                }
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directDelivery.label', default: 'DirectDelivery'), params.id])}"
            redirect(action: "list")
        }
    }

    def deliveryRemove = {
        def deliveryInstance = SalesDelivery.get(params.id)
        def directDeliveryInstance = DirectDelivery.get(params.directDeliveryId)
        directDeliveryInstance.removeFromDeliveries(deliveryInstance)
        directDeliveryInstance.save()
        flash.message= "${deliveryInstance} removed from the waybill!"
        redirect (action:"show", id:directDeliveryInstance.id)
    }
	
	def deliveryPackageRemove = {
		def directDeliveryPackageInstance = DirectDeliveryPackage.get(params.id)
		def directDeliveryInstance = DirectDelivery.get(params.directDeliveryId)
		directDeliveryInstance.removeFromPackages(directDeliveryPackageInstance)
		directDeliveryPackageInstance.delete()
		directDeliveryInstance.save()
		flash.message= "${directDeliveryPackageInstance.toString()} removed from the direct delivery!"
		redirect (action:"show", id:directDeliveryInstance.id)
	}
	
    def cancel = {
        def directDeliveryInstance = DirectDelivery.get(params.id)
		if(directDeliveryInstance.isCancelable()) {
			directDeliveryInstance?.cancel()
			directDeliveryInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			directDeliveryInstance?.save()
			flash.message = "Direct Delivery has been successfully cancelled!"
		} else {
		    flash.error = "Direct Delivery cannot be cancelled because it has at least one trip ticket!"
		}
        redirect(action:'show', id: directDeliveryInstance?.id)
    }
	
}
