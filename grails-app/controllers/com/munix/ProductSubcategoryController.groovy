package com.munix

class ProductSubcategoryController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [productSubcategoryInstanceList: ProductSubcategory.list(params), productSubcategoryInstanceTotal: ProductSubcategory.count()]
    }

    def create = {
        def productSubcategoryInstance = new ProductSubcategory()
        productSubcategoryInstance.properties = params
        return [productSubcategoryInstance: productSubcategoryInstance]
    }

    def save = {
        def productSubcategoryInstance = new ProductSubcategory(params)
        if (productSubcategoryInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'productSubcategory.label', default: 'ProductSubcategory'), productSubcategoryInstance.id])}"
            redirect(action: "show", id: productSubcategoryInstance.id)
        }
        else {
            render(view: "create", model: [productSubcategoryInstance: productSubcategoryInstance])
        }
    }

    def show = {
        def productSubcategoryInstance = ProductSubcategory.get(params.id)
        if (productSubcategoryInstance) {
            [productSubcategoryInstance: productSubcategoryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productSubcategory.label', default: 'ProductSubcategory'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def productSubcategoryInstance = ProductSubcategory.get(params.id)
        if (productSubcategoryInstance) {
            return [productSubcategoryInstance: productSubcategoryInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productSubcategory.label', default: 'ProductSubcategory'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def productSubcategoryInstance = ProductSubcategory.get(params.id)
        if (productSubcategoryInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productSubcategoryInstance.version > version) {
                    
                    productSubcategoryInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'productSubcategory.label', default: 'ProductSubcategory')] as Object[], "Another user has updated this ProductSubcategory while you were editing")
                    render(view: "edit", model: [productSubcategoryInstance: productSubcategoryInstance])
                    return
                }
            }
            productSubcategoryInstance.properties = params
            if (!productSubcategoryInstance.hasErrors() && productSubcategoryInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'productSubcategory.label', default: 'ProductSubcategory'), productSubcategoryInstance.id])}"
                redirect(action: "show", id: productSubcategoryInstance.id)
            }
            else {
                render(view: "edit", model: [productSubcategoryInstance: productSubcategoryInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'productSubcategory.label', default: 'ProductSubcategory'), params.id])}"
            redirect(action: "list")
        }
    }

}
