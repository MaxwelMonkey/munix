package com.munix

class ProductCategoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productCategoryInstanceList: ProductCategory.list(params), productCategoryInstanceTotal: ProductCategory.count()]
    }

    def create = {
        def productCategoryInstance = new ProductCategory()
        productCategoryInstance.properties = params
        return [productCategoryInstance: productCategoryInstance]
    }

    def save = {
        def productCategoryInstance = new ProductCategory(params)
        if (productCategoryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'productCategory.label', default: 'ProductCategory'), productCategoryInstance.id])}"
            redirect(action: "show", id: productCategoryInstance.id)
        }
        else {
            render(view: "create", model: [productCategoryInstance: productCategoryInstance])
        }
    }

    def show = {
        def productCategoryInstance = ProductCategory.get(params.id)
        if (productCategoryInstance) {
            [productCategoryInstance: productCategoryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productCategory.label', default: 'ProductCategory'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def productCategoryInstance = ProductCategory.get(params.id)
        if (productCategoryInstance) {
            return [productCategoryInstance: productCategoryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productCategory.label', default: 'ProductCategory'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def productCategoryInstance = ProductCategory.get(params.id)
        if (productCategoryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productCategoryInstance.version > version) {
                    
                    productCategoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'productCategory.label', default: 'ProductCategory')] as Object[], "Another user has updated this ProductCategory while you were editing")
                    render(view: "edit", model: [productCategoryInstance: productCategoryInstance])
                    return
                }
            }
            productCategoryInstance.properties = params
            if (!productCategoryInstance.hasErrors() && productCategoryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'productCategory.label', default: 'ProductCategory'), productCategoryInstance.id])}"
                redirect(action: "show", id: productCategoryInstance.id)
            }
            else {
                render(view: "edit", model: [productCategoryInstance: productCategoryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productCategory.label', default: 'ProductCategory'), params.id])}"
            redirect(action: "list")
        }
    }

}
