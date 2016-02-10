package com.munix

class WaybillController {

    static allowedMethods = [save: "POST", update: "POST", cancel: "POST"]
    def authenticateService
    def waybillService
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"
		
		def waybillInstanceList = waybillService.generateList(params)
		def dateMap = waybillService.generateDateStructList(params)
		[waybillInstanceList: waybillInstanceList, waybillInstanceTotal: waybillInstanceList.totalCount, dateMap: dateMap]
    }

    def create = {
        def waybillInstance = new Waybill()
        waybillInstance.properties = params
        if(params.id){
            def customerInstance = Customer.get(params?.id)
            def deliveryInstance = waybillService.getDeliveryListForWaybill(customerInstance)
            return [waybillInstance: waybillInstance, deliveryInstance: deliveryInstance]
        }

        else{
            return [waybillInstance: waybillInstance]
        }
    }

    def save = {
        def waybillInstance = new Waybill(params)
        def customerInstance = Customer.get(params."customer.id")
		
		if(customerInstance) {
		
			waybillInstance.customer = customerInstance
			waybillInstance.declaredValue = customerInstance.declaredValue
			waybillInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())

        	if (waybillInstance.save(flush: true)) {
            	flash.message = "${message(code: 'default.created.message', args: [message(code: 'waybill.label', default: 'Waybill'), waybillInstance.id])}"
            	redirect(action: "show", id: waybillInstance.id)
        	}
		
        	else {
            	params.id = params."customer.id"
            	def deliveryInstance = waybillService.getDeliveryListForWaybill(customerInstance)
            	render(view: "create", model: [waybillInstance: waybillInstance,deliveryInstance:deliveryInstance])
        	}
		}
		else {
			flash.error = "Please select a customer."
			redirect(action: "create")
		}
    }

    def show = {
        def waybillInstance = Waybill.get(params.id)
        if (!waybillInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waybill.label', default: 'Waybill'), params.id])}"
            redirect(action: "list")
        }
        else {
            [waybillInstance: waybillInstance]
        }
    }

    def edit = {
        def waybillInstance = Waybill.get(params.id)
        if (!waybillInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waybill.label', default: 'Waybill'), params.id])}"
            redirect(action: "list")
        }else if(waybillInstance.tripTicket){
            flash.error = "Waybill cannot be edited because it has at least one trip ticket."
        }
        else {
            params.id=waybillInstance.customer.id
            def deliveryInstance = waybillService.getDeliveryListForWaybill(waybillInstance.customer)
            return [waybillInstance: waybillInstance, deliveryInstance: deliveryInstance]
        }
    }

    def update = {
        def waybillInstance = Waybill.get(params.id)
        if (waybillInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (waybillInstance.version > version) {
                    waybillInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'waybill.label', default: 'Waybill')] as Object[], "Another user has updated this Waybill while you were editing")
                    render(view: "edit", model: [waybillInstance: waybillInstance])
                    return
                }
            }
            if(!params?."forwarder.id"){
                flash.message = "Forwarder cannot be empty"
                def deliveryInstance = waybillService.getDeliveryListForWaybill(waybillInstance.customer)
                render(view: "edit", model: [waybillInstance: waybillInstance,deliveryInstance:deliveryInstance])
                return
            }else if(waybillInstance.tripTicket){
                flash.error = "Waybill cannot be edited because it has at least one trip ticket."
            }else{
                waybillInstance.properties = params
                if (!waybillInstance.hasErrors() && waybillInstance.save(flush: true)) {
                    flash.message = "${message(code: 'default.updated.message', args: [message(code: 'waybill.label', default: 'Waybill'), waybillInstance.id])}"
                    redirect(action: "show", id: waybillInstance.id)
                }
                else {
                    render(view: "edit", model: [waybillInstance: waybillInstance])
                }
            }

        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waybill.label', default: 'Waybill'), params.id])}"
            redirect(action: "list")
        }
    }

    def deliveryRemove = {
        def deliveryInstance = SalesDelivery.get(params.id)
        def waybillInstance = Waybill.get(params.waybillId)
        waybillInstance.removeFromDeliveries(deliveryInstance)
        waybillInstance.save()
        flash.message= "${deliveryInstance} removed from the waybill!"
        redirect (action:"show", id:waybillInstance.id)
    }

	def waybillPackageRemove = {
		def waybillPackageInstance = WaybillPackage.get(params.id)
		def waybillInstance = Waybill.get(params.waybillId)
		waybillInstance.removeFromPackages(waybillPackageInstance)
		waybillPackageInstance.delete()
		waybillInstance.save()
		flash.message= "${waybillPackageInstance.toString()} removed from the waybill!"
		redirect (action:"show", id:waybillInstance.id)
	}
	
    def print = {
        def waybillInstance = Waybill.get(params.id)
        [waybillInstance: waybillInstance]
    }
	
	def cancel = {
		def waybillInstance = Waybill.get(params.id)
        if(waybillInstance.tripTicket){
            flash.error = "Waybill cannot be cancelled because it has at least one trip ticket."
        }else{
    		waybillInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
            waybillInstance?.cancel()
            waybillInstance?.save()
            flash.message = "Waybill has been successfully cancelled!"
        }
		redirect(action:'show', id: waybillInstance?.id)
	}
}
