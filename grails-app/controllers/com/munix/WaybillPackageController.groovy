package com.munix

class WaybillPackageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [waybillPackageInstanceList: WaybillPackage.list(params), waybillPackageInstanceTotal: WaybillPackage.count()]
    }

    def create = {
        def waybillPackageInstance = new WaybillPackage()
        waybillPackageInstance.properties = params
        return [waybillPackageInstance: waybillPackageInstance]
    }

    def save = {
        def waybillPackageInstance = new WaybillPackage(params)
        def waybillInstance = Waybill.get(params.id)
        waybillPackageInstance?.waybill = waybillInstance

        def itemList = WaybillPackage.findAllWhere(waybill:waybillInstance).collect{it.packaging}

        if(waybillPackageInstance.packaging in itemList){
            flash.message = "Duplicate packaging already exists!"
            redirect(action: "show", controller:"waybill", id: waybillPackageInstance?.waybill.id)
        }
        
        else{
            if (waybillPackageInstance.save(flush: true)) {
                flash.message = "Waybill package details have been successfully created"
                redirect(action: "show", controller:"waybill", id: waybillPackageInstance?.waybill.id)
            }
            else {
                render(view: "create", id:waybillInstance.id)
            }
        }

    }

    def show = {
        def waybillPackageInstance = WaybillPackage.get(params.id)
        if (!waybillPackageInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waybillPackage.label', default: 'WaybillPackage'), params.id])}"
            redirect(action: "list")
        }
        else {
            [waybillPackageInstance: waybillPackageInstance]
        }
    }

    def edit = {
        def waybillPackageInstance = WaybillPackage.get(params.id)
        if (!waybillPackageInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waybillPackage.label', default: 'WaybillPackage'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [waybillPackageInstance: waybillPackageInstance]
        }
    }

    def update = {
        def waybillPackageInstance = WaybillPackage.get(params.id)
        if (waybillPackageInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (waybillPackageInstance.version > version) {
                    
                    waybillPackageInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'waybillPackage.label', default: 'WaybillPackage')] as Object[], "Another user has updated this WaybillPackage while you were editing")
                    render(view: "edit", model: [waybillPackageInstance: waybillPackageInstance])
                    return
                }
            }
            waybillPackageInstance.properties = params
            if (!waybillPackageInstance.hasErrors() && waybillPackageInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'waybillPackage.label', default: 'WaybillPackage'), waybillPackageInstance.id])}"
                redirect(action: "show", controller: "waybill", id: waybillPackageInstance?.waybill?.id)
            }
            else {
                render(view: "edit", model: [waybillPackageInstance: waybillPackageInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waybillPackage.label', default: 'WaybillPackage'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def waybillPackageInstance = WaybillPackage.get(params.id)
        def waybillId = waybillPackageInstance.waybill.id
        if (waybillPackageInstance) {
            try {
                waybillPackageInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'waybillPackage.label', default: 'WaybillPackage'), params.id])}"
                redirect(action: "show", id:waybillId, controller:"waybill")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'waybillPackage.label', default: 'WaybillPackage'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waybillPackage.label', default: 'WaybillPackage'), params.id])}"
            redirect(action: "list")
        }
    }
}
