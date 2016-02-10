package com.munix

class ProductBrandController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productBrandInstanceList: ProductBrand.list(params), productBrandInstanceTotal: ProductBrand.count()]
    }

    def create = {
        def productBrandInstance = new ProductBrand()
        productBrandInstance.properties = params
        return [productBrandInstance: productBrandInstance]
    }

    def save = {
        def productBrandInstance = new ProductBrand(params)
        if (productBrandInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'productBrand.label', default: 'ProductBrand'), productBrandInstance.id])}"
            redirect(action: "show", id: productBrandInstance.id)
        }
        else {
            render(view: "create", model: [productBrandInstance: productBrandInstance])
        }
    }

    def show = {
        def productBrandInstance = ProductBrand.get(params.id)
        if (productBrandInstance) {
            [productBrandInstance: productBrandInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productBrand.label', default: 'ProductBrand'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def productBrandInstance = ProductBrand.get(params.id)
        if (productBrandInstance) {
            return [productBrandInstance: productBrandInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productBrand.label', default: 'ProductBrand'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def productBrandInstance = ProductBrand.get(params.id)
        if (productBrandInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productBrandInstance.version > version) {
                    
                    productBrandInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'productBrand.label', default: 'ProductBrand')] as Object[], "Another user has updated this ProductBrand while you were editing")
                    render(view: "edit", model: [productBrandInstance: productBrandInstance])
                    return
                }
            }
            productBrandInstance.properties = params
            if (!productBrandInstance.hasErrors() && productBrandInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'productBrand.label', default: 'ProductBrand'), productBrandInstance.id])}"
                redirect(action: "show", id: productBrandInstance.id)
            }
            else {
                render(view: "edit", model: [productBrandInstance: productBrandInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productBrand.label', default: 'ProductBrand'), params.id])}"
            redirect(action: "list")
        }
    }

}
