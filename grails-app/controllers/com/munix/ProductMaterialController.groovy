package com.munix

class ProductMaterialController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productMaterialInstanceList: ProductMaterial.list(params), productMaterialInstanceTotal: ProductMaterial.count()]
    }

    def create = {
        def productMaterialInstance = new ProductMaterial()
        productMaterialInstance.properties = params
        return [productMaterialInstance: productMaterialInstance]
    }

    def save = {
        def productMaterialInstance = new ProductMaterial(params)
        if (productMaterialInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'productMaterial.label', default: 'ProductMaterial'), productMaterialInstance.id])}"
            redirect(action: "show", id: productMaterialInstance.id)
        }
        else {
            render(view: "create", model: [productMaterialInstance: productMaterialInstance])
        }
    }

    def show = {
        def productMaterialInstance = ProductMaterial.get(params.id)
        if (productMaterialInstance) {
            [productMaterialInstance: productMaterialInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productMaterial.label', default: 'ProductMaterial'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def productMaterialInstance = ProductMaterial.get(params.id)
        if (productMaterialInstance) {
            return [productMaterialInstance: productMaterialInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productMaterial.label', default: 'ProductMaterial'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def productMaterialInstance = ProductMaterial.get(params.id)
        if (productMaterialInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productMaterialInstance.version > version) {
                    
                    productMaterialInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'productMaterial.label', default: 'ProductMaterial')] as Object[], "Another user has updated this ProductMaterial while you were editing")
                    render(view: "edit", model: [productMaterialInstance: productMaterialInstance])
                    return
                }
            }
            productMaterialInstance.properties = params
            if (!productMaterialInstance.hasErrors() && productMaterialInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'productMaterial.label', default: 'ProductMaterial'), productMaterialInstance.id])}"
                redirect(action: "show", id: productMaterialInstance.id)
            }
            else {
                render(view: "edit", model: [productMaterialInstance: productMaterialInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productMaterial.label', default: 'ProductMaterial'), params.id])}"
            redirect(action: "list")
        }
    }

}
