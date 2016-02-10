package com.munix

class ProductModelController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productModelInstanceList: ProductModel.list(params), productModelInstanceTotal: ProductModel.count()]
    }

    def create = {
        def productModelInstance = new ProductModel()
        productModelInstance.properties = params
        return [productModelInstance: productModelInstance]
    }

    def save = {
        def productModelInstance = new ProductModel(params)
        if (productModelInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'productModel.label', default: 'ProductModel'), productModelInstance.id])}"
            redirect(action: "show", id: productModelInstance.id)
        }
        else {
            render(view: "create", model: [productModelInstance: productModelInstance])
        }
    }

    def show = {
        def productModelInstance = ProductModel.get(params.id)
        if (productModelInstance) {
            [productModelInstance: productModelInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productModel.label', default: 'ProductModel'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def productModelInstance = ProductModel.get(params.id)
        if (productModelInstance) {
            return [productModelInstance: productModelInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productModel.label', default: 'ProductModel'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def productModelInstance = ProductModel.get(params.id)
        if (productModelInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productModelInstance.version > version) {
                    
                    productModelInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'productModel.label', default: 'ProductModel')] as Object[], "Another user has updated this ProductModel while you were editing")
                    render(view: "edit", model: [productModelInstance: productModelInstance])
                    return
                }
            }
            productModelInstance.properties = params
            if (!productModelInstance.hasErrors() && productModelInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'productModel.label', default: 'ProductModel'), productModelInstance.id])}"
                redirect(action: "show", id: productModelInstance.id)
            }
            else {
                render(view: "edit", model: [productModelInstance: productModelInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productModel.label', default: 'ProductModel'), params.id])}"
            redirect(action: "list")
        }
    }

}
