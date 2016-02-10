package com.munix

class CheckStatusController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [checkStatusInstanceList: CheckStatus.list(params), checkStatusInstanceTotal: CheckStatus.count()]
    }

    def create = {
        def checkStatusInstance = new CheckStatus()
        checkStatusInstance.properties = params
        return [checkStatusInstance: checkStatusInstance]
    }

    def save = {
        def checkStatusInstance = new CheckStatus(params)
        if (!checkStatusInstance.hasErrors() && checkStatusInstance.save()) {
            flash.message = "checkStatus.created"
            flash.args = [checkStatusInstance.id]
            flash.defaultMessage = "CheckStatus ${checkStatusInstance.id} created"
            redirect(action: "show", id: checkStatusInstance.id)
        }
        else {
            render(view: "create", model: [checkStatusInstance: checkStatusInstance])
        }
    }

    def show = {
        def checkStatusInstance = CheckStatus.get(params.id)
        if (checkStatusInstance) {
            return [checkStatusInstance: checkStatusInstance]
        }
        else {
            flash.message = "checkStatus.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "CheckStatus not found with id ${params.id}"
            redirect(action: "list")
        }
    }

    def edit = {
        def checkStatusInstance = CheckStatus.get(params.id)
        if (checkStatusInstance) {
            return [checkStatusInstance: checkStatusInstance]
        }
        else {
            flash.message = "checkStatus.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "CheckStatus not found with id ${params.id}"
            redirect(action: "list")
        }
    }

    def update = {
        def checkStatusInstance = CheckStatus.get(params.id)
        if (checkStatusInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (checkStatusInstance.version > version) {
                    
                    checkStatusInstance.errors.rejectValue("version", "checkStatus.optimistic.locking.failure", "Another user has updated this CheckStatus while you were editing")
                    render(view: "edit", model: [checkStatusInstance: checkStatusInstance])
                    return
                }
            }
            checkStatusInstance.properties = params
            if (!checkStatusInstance.hasErrors() && checkStatusInstance.save()) {
                flash.message = "checkStatus.updated"
                flash.args = [params.id]
                flash.defaultMessage = "CheckStatus ${params.id} updated"
                redirect(action: "show", id: checkStatusInstance.id)
            }
            else {
                render(view: "edit", model: [checkStatusInstance: checkStatusInstance])
            }
        }
        else {
            flash.message = "checkStatus.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "CheckStatus not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

}
