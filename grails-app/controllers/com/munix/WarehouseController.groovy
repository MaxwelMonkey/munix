package com.munix

class WarehouseController {

    static allowedMethods = [save: "POST", update: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [warehouseInstanceList: Warehouse.list(params), warehouseInstanceTotal: Warehouse.count()]
    }

    def create = {
        def warehouseInstance = new Warehouse()
        warehouseInstance.properties = params
        return [warehouseInstance: warehouseInstance]
    }

    def save = {
        def warehouseInstance = new Warehouse(params)

        Product.list().each{ product ->
            def stockInstance = new Stock()
            stockInstance.warehouse = warehouseInstance
            stockInstance.product = product
            warehouseInstance.addToStocks(stockInstance)
        }

        if (warehouseInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'warehouse.label', default: 'Inventory Warehouse'), warehouseInstance.id])}"
            redirect(action: "show", id: warehouseInstance.id)
        }
        else {
            render(view: "create", model: [warehouseInstance: warehouseInstance])
        }
    }

    def show = {
        def warehouseInstance = Warehouse.get(params.id)
        if (!warehouseInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'warehouse.label', default: 'Inventory Warehouse'), params.id])}"
            redirect(action: "list")
        }
        else {
            [warehouseInstance: warehouseInstance]
        }
    }

    def edit = {
        def warehouseInstance = Warehouse.get(params.id)
        if (!warehouseInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'warehouse.label', default: 'Inventory Warehouse'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [warehouseInstance: warehouseInstance]
        }
    }

    def update = {
        def warehouseInstance = Warehouse.get(params.id)
        if (warehouseInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (warehouseInstance.version > version) {
                    
                    warehouseInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'warehouse.label', default: 'Inventory Warehouse')] as Object[], "Another user has updated this Inventory Warehouse while you were editing")
                    render(view: "edit", model: [warehouseInstance: warehouseInstance])
                    return
                }
            }
            warehouseInstance.properties = params
            if (!warehouseInstance.hasErrors() && warehouseInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'warehouse.label', default: 'Inventory Warehouse'), warehouseInstance.id])}"
                redirect(action: "show", id: warehouseInstance.id)
            }
            else {
                render(view: "edit", model: [warehouseInstance: warehouseInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'warehouse.label', default: 'Inventory Warehouse'), params.id])}"
            redirect(action: "list")
        }
    }

    def search ={

        def identifier = params.identifier
        def counter = 0
        def warehouseList
        def warehouseIntance

        if(identifier!=""){
            def warehousetCriteria = Warehouse.createCriteria()
            warehouseList = warehousetCriteria{
                ilike("identifier", "%${identifier}%")
                 maxResults(10)
                    if (params.order == 'asc') {
                        order("identifier","desc")
                    }else{
                        order("identifier","asc")
                    }
            }

        }

        warehouseList.collect{
            warehouseIntance = Warehouse.get(it.id)
            counter++
        }

         params.max = Math.min(params.max ? params.int('max') : 10, 100)
       render(view:'list',model:[warehouseInstanceList: warehouseList, warehouseInstanceTotal: counter, params:warehouseIntance])

    }
}
