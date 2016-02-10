package com.munix

class AssemblerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [assemblerInstanceList: Assembler.list(params), assemblerInstanceTotal: Assembler.count()]
    }

    def create = {
        def assemblerInstance = new Assembler()
        assemblerInstance.properties = params
        return [assemblerInstance: assemblerInstance]
    }

    def save = {
        def assemblerInstance = new Assembler(params)
        if (assemblerInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'assembler.label', default: 'Assembler'), assemblerInstance.id])}"
            redirect(action: "show", id: assemblerInstance.id)
        }
        else {
            render(view: "create", model: [assemblerInstance: assemblerInstance])
        }
    }

    def show = {
        def assemblerInstance = Assembler.get(params.id)
        if (assemblerInstance) {
            [assemblerInstance: assemblerInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'assembler.label', default: 'Assembler'), params.id])}"
            redirect(action: "list")
        }
    }

    def edit = {
        def assemblerInstance = Assembler.get(params.id)
        if (assemblerInstance) {
            return [assemblerInstance: assemblerInstance]
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'assembler.label', default: 'Assembler'), params.id])}"
            redirect(action: "list")
        }
    }

    def update = {
        def assemblerInstance = Assembler.get(params.id)
        if (assemblerInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (assemblerInstance.version > version) {
                    
                    assemblerInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'assembler.label', default: 'Assembler')] as Object[], "Another user has updated this Assembler while you were editing")
                    render(view: "edit", model: [assemblerInstance: assemblerInstance])
                    return
                }
            }
            assemblerInstance.properties = params
            if (!assemblerInstance.hasErrors() && assemblerInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'assembler.label', default: 'Assembler'), assemblerInstance.id])}"
                redirect(action: "show", id: assemblerInstance.id)
            }
            else {
                render(view: "edit", model: [assemblerInstance: assemblerInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'assembler.label', default: 'Assembler'), params.id])}"
            redirect(action: "list")
        }
    }

}
