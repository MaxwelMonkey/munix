package com.munix

class ProductUnitController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productUnitInstanceList: ProductUnit.list(params), productUnitInstanceTotal: ProductUnit.count()]
    }

    def create = {
        def productUnitInstance = new ProductUnit()
        productUnitInstance.properties = params
        return [productUnitInstance: productUnitInstance]
    }

    def save = {
        def productUnitInstance = new ProductUnit(params)
        if (productUnitInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'productUnit.label', default: 'ProductUnit'), productUnitInstance.id])}"
            redirect(action: "show", id: productUnitInstance.id)
        }
        else {
            render(view: "create", model: [productUnitInstance: productUnitInstance])
        }
    }

    def show = {
        def productUnitInstance = ProductUnit.get(params.id)
        if (productUnitInstance) {
            [productUnitInstance: productUnitInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productUnit.label', default: 'ProductUnit'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def productUnitInstance = ProductUnit.get(params.id)
        if (productUnitInstance) {
            return [productUnitInstance: productUnitInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productUnit.label', default: 'ProductUnit'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def productUnitInstance = ProductUnit.get(params.id)
        if (productUnitInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productUnitInstance.version > version) {
                    
                    productUnitInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'productUnit.label', default: 'ProductUnit')] as Object[], "Another user has updated this ProductUnit while you were editing")
                    render(view: "edit", model: [productUnitInstance: productUnitInstance])
                    return
                }
            }
            productUnitInstance.properties = params
            if (!productUnitInstance.hasErrors() && productUnitInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'productUnit.label', default: 'ProductUnit'), productUnitInstance.id])}"
                redirect(action: "show", id: productUnitInstance.id)
            }
            else {
                render(view: "edit", model: [productUnitInstance: productUnitInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productUnit.label', default: 'ProductUnit'), params.id])}"
            redirect(action: "list")
        }
    }

}
