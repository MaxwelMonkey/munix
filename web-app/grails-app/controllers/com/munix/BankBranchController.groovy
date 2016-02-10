package com.munix

class BankBranchController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [bankBranchInstanceList: BankBranch.list(params), bankBranchInstanceTotal: BankBranch.count()]
    }
	
    def create = {
        def bankBranchInstance = new BankBranch()
        bankBranchInstance.properties = params
        return [bankBranchInstance: bankBranchInstance]
    }

    def save = {
        def bankBranchInstance = new BankBranch(params)
        if (bankBranchInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), bankBranchInstance.id])}"
            redirect(action: "show", id: bankBranchInstance.id)
        }
        else {
            render(view: "create", model: [bankBranchInstance: bankBranchInstance])
        }
    }

    def show = {
        def bankBranchInstance = BankBranch.get(params.id)
        if (bankBranchInstance) {
            [bankBranchInstance: bankBranchInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def bankBranchInstance = BankBranch.get(params.id)
        if (bankBranchInstance) {
            return [bankBranchInstance: bankBranchInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def bankBranchInstance = BankBranch.get(params.id)
        if (bankBranchInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (bankBranchInstance.version > version) {
                    
                    bankBranchInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'bankBranch.label', default: 'BankBranch')] as Object[], "Another user has updated this BankBranch while you were editing")
                    render(view: "edit", model: [bankBranchInstance: bankBranchInstance])
                    return
                }
            }
            bankBranchInstance.properties = params
            if (!bankBranchInstance.hasErrors() && bankBranchInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), bankBranchInstance.id])}"
                redirect(action: "show", id: bankBranchInstance.id)
            }
            else {
                render(view: "edit", model: [bankBranchInstance: bankBranchInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def bankBranchInstance = BankBranch.get(params.id)
        if (bankBranchInstance) {
            try {
                bankBranchInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'bankBranch.label', default: 'BankBranch'), params.id])}"
            redirect(action: "list")
        }
    }
}
