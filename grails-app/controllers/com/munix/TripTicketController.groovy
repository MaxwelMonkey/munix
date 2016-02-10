package com.munix

class TripTicketController {

    static allowedMethods = [save: "POST", update: "POST", markAsComplete: "POST"]
    def authenticateService
    def constantService
	def tripTicketService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

		def tripTicketInstanceList = tripTicketService.generateList(params)
		def dateMap = tripTicketService.generateDateStructList(params)
		[tripTicketInstanceList: tripTicketInstanceList, tripTicketInstanceTotal: tripTicketInstanceList.totalCount, dateMap: dateMap]

    }

    def create = {
        def tripTicketInstance = new TripTicket()
        tripTicketInstance.properties = params

        def waybillInstance = []
        Waybill.list().each{
            if(it.tripTicket == null && !it.isCancelled()){
                waybillInstance << it
            }
        }

        def directDeliveryInstance = []
        DirectDelivery.list().each{
            if(it.tripTicket == null && !it.isCancelled()){
                directDeliveryInstance << it
            }
        }

        return [tripTicketInstance: tripTicketInstance, directDeliveryInstance:directDeliveryInstance, waybillInstance:waybillInstance]
    }

    def save = {
        def tripTicketInstance = new TripTicket(params)
		tripTicketInstance.preparedBy = FormatUtil.createTimeStamp(authenticateService.userDomain())

        List<Long> waybills = request.getParameterValues('waybills').collect { it.toLong() }
        waybills?.each{
            def waybillInstance = Waybill.get(it)
            def itemInstance = new TripTicketWaybillItem(item:waybillInstance)
            itemInstance.type = "Waybill"
            tripTicketInstance.addToItems(itemInstance)
        }

        List<Long> deliveries = request.getParameterValues('deliveries').collect { it.toLong() }
        deliveries?.each{
            def directDeliveryInstance = DirectDelivery.get(it)
            def itemInstance = new TripTicketDeliveryItem(item:directDeliveryInstance)
            itemInstance.type = "Direct Delivery"
            tripTicketInstance.addToItems(itemInstance)
        }
        if (tripTicketInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'tripTicket.label', default: 'TripTicket'), tripTicketInstance.id])}"
            redirect(action: "show", id: tripTicketInstance.id)
        }
        else {
            render(view: "create", model: [tripTicketInstance: tripTicketInstance])
        } 
    }

    def show = {
        def tripTicketInstance = TripTicket.get(params.id)
        if (!tripTicketInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tripTicket.label', default: 'TripTicket'), params.id])}"
            redirect(action: "list")
        }
        else {
            [tripTicketInstance: tripTicketInstance]
        }
    }

    def edit = {
        def tripTicketInstance = TripTicket.get(params.id)

        if (!tripTicketInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tripTicket.label', default: 'TripTicket'), params.id])}"
            redirect(action: "list")
        }
        else {
            def waybillInstance = []
            Waybill.list().each{
                if(it.tripTicket == null){
                    waybillInstance << it
                }
            }

            def directDeliveryInstance = []
            DirectDelivery.list().each{
                if(it.tripTicket == null){
                    directDeliveryInstance << it
                }
            }

            return [tripTicketInstance: tripTicketInstance, directDeliveryInstance:directDeliveryInstance, waybillInstance:waybillInstance]
        }
    }

    def update = {
        def tripTicketInstance = TripTicket.get(params.id)
        if (tripTicketInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (tripTicketInstance.version > version) {
                    
                    tripTicketInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'tripTicket.label', default: 'TripTicket')] as Object[], "Another user has updated this TripTicket while you were editing")
                    render(view: "edit", model: [tripTicketInstance: tripTicketInstance])
                    return
                }
            }
            tripTicketInstance.properties = params

            List<Long> waybills = request.getParameterValues('waybills').collect { it.toLong() }
            waybills?.each{
                def waybillInstance = Waybill.get(it)
                def itemInstance = new TripTicketWaybillItem(item:waybillInstance)
                itemInstance.type = "Waybill"
                tripTicketInstance.addToItems(itemInstance)
            }

            List<Long> deliveries = request.getParameterValues('deliveries').collect { it.toLong() }
            deliveries?.each{
                def directDeliveryInstance = DirectDelivery.get(it)
                def itemInstance = new TripTicketDeliveryItem(item: directDeliveryInstance)
                itemInstance.type = "Direct Delivery"
                tripTicketInstance.addToItems(itemInstance)
            }

            if (!tripTicketInstance.hasErrors() && tripTicketInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'tripTicket.label', default: 'TripTicket'), tripTicketInstance.id])}"
                redirect(action: "show", id: tripTicketInstance.id)
            }
            else {
                render(view: "edit", model: [tripTicketInstance: tripTicketInstance])

            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tripTicket.label', default: 'TripTicket'), params.id])}"
            redirect(action: "list")
        }
    }

    def markAsComplete = {
        def tripTicketInstance = TripTicket.get(params.id)
		tripTicketInstance.markAsCompleteBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
        tripTicketInstance?.complete()
        tripTicketInstance?.items?.each{
            it.item.complete()
        }
        tripTicketInstance?.save(flush:true)
        redirect(action: "show", id: tripTicketInstance.id)
    }
	def unapprove={
		def tripTicketInstance = TripTicket.get(params.id)
		tripTicketInstance?.processing()
		tripTicketInstance?.items?.each{
			it.item.processing()
		}
		tripTicketInstance?.save(flush:true)
		redirect(action: "show", id: tripTicketInstance.id)
	}
    def removeItem = {
        def tripTicketItemInstance = TripTicketItem.get(params.id)
        tripTicketItemInstance.item.tripTicket = null

        tripTicketItemInstance.delete(flush: true)
        flash.message = "The item has been successfully removed from Trip Ticket"
        redirect(action: "show", id: params.tripTicketId)
    }

    def print = {
        def tripTicketInstance = TripTicket.get(params.id)
        [tripTicketInstance: tripTicketInstance]
    }

	def cancel = {
		def tripTicketInstance = TripTicket.get(params.id)

		if (tripTicketInstance.items.size() > 0){
			flash.message = "Unable to cancel! Please remove all deliveries/waybills first!"
		} else {
			tripTicketInstance.cancelledBy = FormatUtil.createTimeStamp(authenticateService.userDomain())
			tripTicketInstance?.cancel()
			tripTicketInstance?.items?.each{
				it.item.cancel()
			}

			tripTicketInstance?.save()
			flash.message = "Trip ticket has now been Cancelled."

		}

		redirect(action:'show',id:tripTicketInstance?.id)
	}

}
