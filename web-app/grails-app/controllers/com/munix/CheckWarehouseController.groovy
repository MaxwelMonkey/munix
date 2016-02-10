package com.munix

class CheckWarehouseController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        if (!params.max) params.max = 100
        [checkWarehouseInstanceList: CheckWarehouse.list(params), checkWarehouseInstanceTotal: CheckWarehouse.count()]
    }

    def create = {
        def checkWarehouseInstance = new CheckWarehouse()
        checkWarehouseInstance.properties = params
        return [checkWarehouseInstance: checkWarehouseInstance]
    }

    def save = {
        def checkWarehouseInstance = new CheckWarehouse(params)
        
        if(checkWarehouseInstance.isDefault){
            CheckWarehouse.list().each{
                it.isDefault = false
            }
        }

        if (checkWarehouseInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'checkWarehouse.label', default: 'CheckWarehouse'), checkWarehouseInstance.id])}"
            redirect(action: "show", id: checkWarehouseInstance.id)
        }
        else {
            render(view: "create", model: [checkWarehouseInstance: checkWarehouseInstance])
        }
    }

    def show = {
        def checkWarehouseInstance = CheckWarehouse.get(params.id)
        if (checkWarehouseInstance) {
            [checkWarehouseInstance: checkWarehouseInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkWarehouse.label', default: 'CheckWarehouse'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def checkWarehouseInstance = CheckWarehouse.get(params.id)
        if (checkWarehouseInstance) {
            return [checkWarehouseInstance: checkWarehouseInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkWarehouse.label', default: 'CheckWarehouse'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def checkWarehouseInstance = CheckWarehouse.get(params.id)
        if (checkWarehouseInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (checkWarehouseInstance.version > version) {
                    
                    checkWarehouseInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'checkWarehouse.label', default: 'CheckWarehouse')] as Object[], "Another user has updated this CheckWarehouse while you were editing")
                    render(view: "edit", model: [checkWarehouseInstance: checkWarehouseInstance])
                    return
                }
            }
            checkWarehouseInstance.properties = params

            if(checkWarehouseInstance.isDefault){
                CheckWarehouse.list().each{
                    if(checkWarehouseInstance.id != it.id){
                        it.isDefault = false
                    }
                }
            }

            if (!checkWarehouseInstance.hasErrors() && checkWarehouseInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'checkWarehouse.label', default: 'CheckWarehouse'), checkWarehouseInstance.id])}"
                redirect(action: "show", id: checkWarehouseInstance.id)
            }
            else {
                render(view: "edit", model: [checkWarehouseInstance: checkWarehouseInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'checkWarehouse.label', default: 'CheckWarehouse'), params.id])}"
            redirect(action: "list")
        }
    }

     def search ={

        def identifier = params.identifier
        def counter = 0
        def checkWarehouseList
        def checkWarehouseInstance

        if(identifier!=""){
            def checkWareHouseCriteria = CheckWarehouse.createCriteria()
            checkWarehouseList = checkWareHouseCriteria{
                ilike("identifier", "%${identifier}%")
                 maxResults(10)
                    if (params.order == 'asc') {
                        order("identifier","desc")
                    }else{
                        order("identifier","asc")
                    }
            }

        }

        checkWarehouseList.collect{
            checkWarehouseInstance = CheckWarehouse.get(it.id)
            counter++
        }

         params.max = Math.min(params.max ? params.int('max') : 10, 100)
       render(view:'list',model:[checkWarehouseInstanceList:checkWarehouseList,
               checkWarehouseInstanceTotal: counter, params:checkWarehouseInstance])

    }
}
