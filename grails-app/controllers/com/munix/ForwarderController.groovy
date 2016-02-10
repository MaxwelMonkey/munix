package com.munix

class ForwarderController {
    static allowedMethods = [save: "POST", update: "POST"]
	
	def forwarderService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "id"
		if (!params.order) params.order = "asc"

		def forwarderInstanceList = forwarderService.generateList(params)
		[forwarderInstanceList: forwarderInstanceList, forwarderInstanceTotal: forwarderInstanceList.totalCount]
    }

    def create = {
        def forwarderInstance = new Forwarder()
        forwarderInstance.properties = params
        return [forwarderInstance: forwarderInstance]
    }

    def save = {
        def forwarderInstance = new Forwarder(params)
        if (forwarderInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'forwarder.label', default: 'Forwarder'), forwarderInstance.id])}"
            redirect(action: "show", id: forwarderInstance.id)
        }
        else {
            render(view: "create", model: [forwarderInstance: forwarderInstance])
        }
    }

    def show = {
        def forwarderInstance = Forwarder.get(params.id)
        if (forwarderInstance) {
            [forwarderInstance: forwarderInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'forwarder.label', default: 'Forwarder'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def forwarderInstance = Forwarder.get(params.id)
        if (forwarderInstance) {
            return [forwarderInstance: forwarderInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'forwarder.label', default: 'Forwarder'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def forwarderInstance = Forwarder.get(params.id)
        if (forwarderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (forwarderInstance.version > version) {
                    
                    forwarderInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'forwarder.label', default: 'Forwarder')] as Object[], "Another user has updated this Forwarder while you were editing")
                    render(view: "edit", model: [forwarderInstance: forwarderInstance])
                    return
                }
            }
            forwarderInstance.properties = params
            if (!forwarderInstance.hasErrors() && forwarderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'forwarder.label', default: 'Forwarder'), forwarderInstance.id])}"
                redirect(action: "show", id: forwarderInstance.id)
            }
            else {
                render(view: "edit", model: [forwarderInstance: forwarderInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'forwarder.label', default: 'Forwarder'), params.id])}"
            redirect(action: "list")
        }
    }


    def search ={

        def identifier = params.identifier
        def counter = 0
        def forwarderList
        def forwarderInstance

        if(identifier!=""){
            def forwarderCriteria = Forwarder.createCriteria()
            forwarderList = forwarderCriteria{
                ilike("identifier", "%${identifier}%")
                maxResults(10)
                if (params.order == 'asc') {
                    order("identifier","desc")
                }else{
                    order("identifier","asc")
                }
            }

        }

        forwarderList.collect{
            forwarderInstance = Forwarder.get(it.id)
            counter++
        }

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        render(view:'list',model:[forwarderInstanceList:forwarderList ,
                forwarderInstanceTotal: counter, params:forwarderInstance])

    }
}
