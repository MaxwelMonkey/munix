package com.munix

class PackagingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [packagingInstanceList: Packaging.list(params), packagingInstanceTotal: Packaging.count()]
    }

    def create = {
        def packagingInstance = new Packaging()
        packagingInstance.properties = params
        return [packagingInstance: packagingInstance]
    }

    def save = {
        def packagingInstance = new Packaging(params)
        if (packagingInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'packaging.label', default: 'Packaging'), packagingInstance.id])}"
            redirect(action: "show", id: packagingInstance.id)
        }
        else {
            render(view: "create", model: [packagingInstance: packagingInstance])
        }
    }

    def show = {
        def packagingInstance = Packaging.get(params.id)
        if (packagingInstance) {
            [packagingInstance: packagingInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'packaging.label', default: 'Packaging'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def packagingInstance = Packaging.get(params.id)
        if (packagingInstance) {
            return [packagingInstance: packagingInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'packaging.label', default: 'Packaging'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def packagingInstance = Packaging.get(params.id)
        if (packagingInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (packagingInstance.version > version) {
                    
                    packagingInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'packaging.label', default: 'Packaging')] as Object[], "Another user has updated this Packaging while you were editing")
                    render(view: "edit", model: [packagingInstance: packagingInstance])
                    return
                }
            }
            packagingInstance.properties = params
            if (!packagingInstance.hasErrors() && packagingInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'packaging.label', default: 'Packaging'), packagingInstance.id])}"
                redirect(action: "show", id: packagingInstance.id)
            }
            else {
                render(view: "edit", model: [packagingInstance: packagingInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'packaging.label', default: 'Packaging'), params.id])}"
            redirect(action: "list")
        }
    }

}
