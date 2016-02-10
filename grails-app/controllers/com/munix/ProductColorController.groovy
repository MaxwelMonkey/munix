package com.munix

class ProductColorController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productColorInstanceList: ProductColor.list(params), productColorInstanceTotal: ProductColor.count()]
    }

    def create = {
        def productColorInstance = new ProductColor()
        productColorInstance.properties = params
        return [productColorInstance: productColorInstance]
    }

    def save = {
        def productColorInstance = new ProductColor(params)
        if (productColorInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'productColor.label', default: 'ProductColor'), productColorInstance.id])}"
            redirect(action: "show", id: productColorInstance.id)
        }
        else {
            render(view: "create", model: [productColorInstance: productColorInstance])
        }
    }

    def show = {
        def productColorInstance = ProductColor.get(params.id)
        if (productColorInstance) {
            [productColorInstance: productColorInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productColor.label', default: 'ProductColor'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def productColorInstance = ProductColor.get(params.id)
        if (productColorInstance) {
            return [productColorInstance: productColorInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productColor.label', default: 'ProductColor'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def productColorInstance = ProductColor.get(params.id)
        if (productColorInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productColorInstance.version > version) {
                    
                    productColorInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'productColor.label', default: 'ProductColor')] as Object[], "Another user has updated this ProductColor while you were editing")
                    render(view: "edit", model: [productColorInstance: productColorInstance])
                    return
                }
            }
            productColorInstance.properties = params
            if (!productColorInstance.hasErrors() && productColorInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'productColor.label', default: 'ProductColor'), productColorInstance.id])}"
                redirect(action: "show", id: productColorInstance.id)
            }
            else {
                render(view: "edit", model: [productColorInstance: productColorInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productColor.label', default: 'ProductColor'), params.id])}"
            redirect(action: "list")
        }
    }

}
