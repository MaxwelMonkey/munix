package com.munix

class DirectDeliveryPackageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [directDeliveryPackageInstanceList: DirectDeliveryPackage.list(params), directDeliveryPackageInstanceTotal: DirectDeliveryPackage.count()]
    }

    def create = {
        def directDeliveryPackageInstance = new DirectDeliveryPackage()
		directDeliveryPackageInstance.properties = params
        def directDeliveryInstance = DirectDelivery.get(params.id)
		
		if(directDeliveryInstance?.tripTicket){
			flash.error = "Package cannot be added because direct delivery has at least one trip ticket!"
			redirect(action: "show", controller:"directDelivery", id: directDeliveryInstance.id)
		} else{
			return [directDeliveryPackageInstance: directDeliveryPackageInstance]
		}
    }

    def save = {
        def directDeliveryPackageInstance = new DirectDeliveryPackage(params)
        def directDeliveryInstance = DirectDelivery.get(params.id)
        directDeliveryPackageInstance?.directDelivery = directDeliveryInstance

        def itemList = DirectDeliveryPackage.findAllWhere(directDelivery:directDeliveryInstance).collect{it.packaging}

        if(directDeliveryPackageInstance.packaging in itemList){
            flash.message = "Duplicate packaging already exists!"
            redirect(action: "show", controller:"directDelivery", id: directDeliveryPackageInstance?.directDelivery.id)
        }
        else{
            if (directDeliveryPackageInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), directDeliveryPackageInstance.id])}"
                redirect(controller:"directDelivery",action: "show", id: directDeliveryPackageInstance?.directDelivery.id)
            }
            else {
                render(view: "create", model: [directDeliveryPackageInstance: directDeliveryPackageInstance])
            }
        }
    }

    def show = {
        def directDeliveryPackageInstance = DirectDeliveryPackage.get(params.id)
        if (directDeliveryPackageInstance) {
            [directDeliveryPackageInstance: directDeliveryPackageInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def directDeliveryPackageInstance = DirectDeliveryPackage.get(params.id)
        if (directDeliveryPackageInstance) {
            return [directDeliveryPackageInstance: directDeliveryPackageInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def directDeliveryPackageInstance = DirectDeliveryPackage.get(params.id)
        if (directDeliveryPackageInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (directDeliveryPackageInstance.version > version) {
                    
                    directDeliveryPackageInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage')] as Object[], "Another user has updated this DirectDeliveryPackage while you were editing")
                    render(view: "edit", model: [directDeliveryPackageInstance: directDeliveryPackageInstance])
                    return
                }
            }
            directDeliveryPackageInstance.properties = params
            if (!directDeliveryPackageInstance.hasErrors() && directDeliveryPackageInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), directDeliveryPackageInstance.id])}"
                redirect(controller:"directDelivery", action: "show", id: directDeliveryPackageInstance.directDelivery.id)
            }
            else {
                render(view: "edit", model: [directDeliveryPackageInstance: directDeliveryPackageInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def directDeliveryPackageInstance = DirectDeliveryPackage.get(params.id)
        def directDeliveryId = directDeliveryPackageInstance?.directDelivery?.id
        if (directDeliveryPackageInstance) {
            try {
                directDeliveryPackageInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), params.id])}"
                redirect(action: "show", controller: "directDelivery", id: directDeliveryId)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'directDeliveryPackage.label', default: 'DirectDeliveryPackage'), params.id])}"
            redirect(action: "list")
        }
    }
}
