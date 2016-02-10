package com.munix

class SalesAgentController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def salesAgentService
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		if (!params.max) params.max = 100
		if (!params.offset) params.offset = 0
		if (!params.sort) params.sort = "identifier"
		if (!params.order) params.order = "asc"
		
		def salesAgentInstanceList = salesAgentService.generateList(params)
        [salesAgentInstanceList: salesAgentInstanceList, salesAgentInstanceTotal: salesAgentInstanceList.totalCount]
    }

    def create = {
        def salesAgentInstance = new SalesAgent()
        salesAgentInstance.properties = params
        return [salesAgentInstance: salesAgentInstance]
    }

    def save = {
        def salesAgentInstance = new SalesAgent(params)
        if (salesAgentInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'salesAgent.label', default: 'SalesAgent'), salesAgentInstance.id])}"
            redirect(action: "show", id: salesAgentInstance.id)
        }
        else {
            render(view: "create", model: [salesAgentInstance: salesAgentInstance])
        }
    }

    def show = {
        def salesAgentInstance = SalesAgent.get(params.id)
        if (!salesAgentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesAgent.label', default: 'SalesAgent'), params.id])}"
            redirect(action: "list")
        }
        else {
            [salesAgentInstance: salesAgentInstance]
        }
    }

    def edit = {
        def salesAgentInstance = SalesAgent.get(params.id)
        if (!salesAgentInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesAgent.label', default: 'SalesAgent'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [salesAgentInstance: salesAgentInstance]
        }
    }

    def update = {
        def salesAgentInstance = SalesAgent.get(params.id)
        if (salesAgentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (salesAgentInstance.version > version) {
                    
                    salesAgentInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'salesAgent.label', default: 'SalesAgent')] as Object[], "Another user has updated this SalesAgent while you were editing")
                    render(view: "edit", model: [salesAgentInstance: salesAgentInstance])
                    return
                }
            }
            salesAgentInstance.properties = params
            if (!salesAgentInstance.hasErrors() && salesAgentInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'salesAgent.label', default: 'SalesAgent'), salesAgentInstance.id])}"
                redirect(action: "show", id: salesAgentInstance.id)
            }
            else {
                render(view: "edit", model: [salesAgentInstance: salesAgentInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesAgent.label', default: 'SalesAgent'), params.id])}"
            redirect(action: "list")
        }
    }


    def search ={

        def identifier = params.identifier
        def firstname = params.firstname
        def lastname = params.lastname
        def counter = 0
        def salesAgentList
        def salesAgentInstance

        if(identifier!=""){
            def salesAgentCriteria = SalesAgent.createCriteria()
            salesAgentList = salesAgentCriteria{
                ilike("identifier", "%${identifier}%")
                 maxResults(10)
                    if (params.order == 'asc') {
                        order("identifier","desc")
                    }else{
                        order("identifier","asc")
                    }
            }

        }


        if(firstname!=""){
              def salesAgentCriteria = SalesAgent.createCriteria()
            salesAgentList = salesAgentCriteria{
                ilike("firstName", "%${firstname}%")
                 maxResults(10)
                    if (params.order == 'asc') {
                        order("firstName","desc")
                    }else{
                        order("firstName","asc")
                    }

            }
        }

        if(lastname!=""){
              def salesAgentCriteria = SalesAgent.createCriteria()
            salesAgentList = salesAgentCriteria{
                ilike("lastName", "%${lastname}%")
                 maxResults(10)
                    if (params.order == 'asc') {
                        order("lastName","desc")
                    }else{
                        order("lastName","asc")
                    }
            }
        }

        salesAgentList.collect{
            salesAgentInstance = SalesAgent.get(it.id)
            counter++
        }

         params.max = Math.min(params.max ? params.int('max') : 10, 100)
       render(view:'list',model:[salesAgentInstanceList: salesAgentList, salesAgentInstanceTotal:counter, params:salesAgentInstance])

    }
}
