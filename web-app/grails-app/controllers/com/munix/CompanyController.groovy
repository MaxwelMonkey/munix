package com.munix

class CompanyController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]


    def index = {
        if(Company.list().size > 0){
            redirect(action: "show", params: params, id:1)
        }
        else{
            redirect(action: "create", params: params)
        }
    }

    def create = {
        def companyInstance = new Company()
        companyInstance.properties = params
        return [companyInstance: companyInstance]
    }

    def save = {
        def companyInstance = new Company(params)
        if (companyInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'company.label', default: 'Company'), companyInstance.id])}"
            redirect(action: "show", id: companyInstance.id)
        }
        else {
            render(view: "create", model: [companyInstance: companyInstance])
        }
    }

    def show = {
        def companyInstance = Company.get(params.id)
        if (companyInstance) {
            [companyInstance: companyInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def companyInstance = Company.get(params.id)
        if (companyInstance) {
            return [companyInstance: companyInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def companyInstance = Company.get(params.id)
        if (companyInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (companyInstance.version > version) {
                    
                    companyInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'company.label', default: 'Company')] as Object[], "Another user has updated this Company while you were editing")
                    render(view: "edit", model: [companyInstance: companyInstance])
                    return
                }
            }
            companyInstance.properties = params
            if (!companyInstance.hasErrors() && companyInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'company.label', default: 'Company'), companyInstance.id])}"
                redirect(action: "show", id: companyInstance.id)
            }
            else {
                render(view: "edit", model: [companyInstance: companyInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), params.id])}"
            redirect(action: "list")
        }
    }

}
