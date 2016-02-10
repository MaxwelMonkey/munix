package com.munix

class MaterialReleaseItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [materialReleaseItemInstanceList: MaterialReleaseItem.list(params), materialReleaseItemInstanceTotal: MaterialReleaseItem.count()]
    }

    def create = {
        def materialReleaseItemInstance = new MaterialReleaseItem()
        materialReleaseItemInstance.properties = params
        return [materialReleaseItemInstance: materialReleaseItemInstance]
    }

    def save = {
        def materialReleaseItemInstance = new MaterialReleaseItem(params)
        materialReleaseItemInstance.materialRelease = MaterialRelease.get(params.id)
        if (materialReleaseItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), materialReleaseItemInstance.id])}"
            redirect(action: "show", id: params.id, controller:"materialRelease")
        }
        else {
            render(view: "create", model: [materialReleaseItemInstance: materialReleaseItemInstance])
        }
    }

    def show = {
        def materialReleaseItemInstance = MaterialReleaseItem.get(params.id)
        if (materialReleaseItemInstance) {
            [materialReleaseItemInstance: materialReleaseItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def materialReleaseItemInstance = MaterialReleaseItem.get(params.id)
        if (materialReleaseItemInstance) {
            return [materialReleaseItemInstance: materialReleaseItemInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), params.id])}"
            redirect(action: "show", id: materialReleaseItemInstance.materialRealease.id, controller:"materialRelaese")
        }
    }

    def update = {
        def materialReleaseItemInstance = MaterialReleaseItem.get(params.id)
        if (materialReleaseItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (materialReleaseItemInstance.version > version) {
                    
                    materialReleaseItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem')] as Object[], "Another user has updated this MaterialReleaseItem while you were editing")
                    render(view: "edit", model: [materialReleaseItemInstance: materialReleaseItemInstance])
                    return
                }
            }
            materialReleaseItemInstance.properties = params
            if (!materialReleaseItemInstance.hasErrors() && materialReleaseItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), materialReleaseItemInstance.id])}"
                redirect(action: "show", id: materialReleaseItemInstance.materialRelease.id, controller:"materialRelease")
            }
            else {
                render(view: "edit", model: [materialReleaseItemInstance: materialReleaseItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def materialReleaseItemInstance = MaterialReleaseItem.get(params.id)
        if (materialReleaseItemInstance) {
            try {
                materialReleaseItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
